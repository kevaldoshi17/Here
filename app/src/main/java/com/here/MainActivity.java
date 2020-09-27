package com.here;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.here.models.LocalBusiness;
import com.here.models.MyLocationListener;
import com.here.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    public static double latestLatitude = -1.0;
    public static double latestLongitude = -1.0;
    private static int messageIndex = 100;
    LocationManager locationManager;
    private TextInputLayout messageInputLayout;
    private TextInputEditText messageInputEditText;
    private FloatingActionButton sendMessageFab;
    private RecyclerView messageRecycler;
    private MessageListAdapter messageListAdapter;
    private List<Message> messageList;
    private List<LocalBusiness> localBusinessList;
    private User self = new User("you");
    private User bot = new User("HereBot");
    List<String> urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageInputLayout = findViewById(R.id.messageInputLayout);
        messageInputEditText = findViewById(R.id.messageInputEditText);
        sendMessageFab = findViewById(R.id.sendMessageFab);

        messageRecycler = findViewById(R.id.messageListRecyclerView);

        setListenerOnFab();

        messageList = new ArrayList<>();
        localBusinessList = new ArrayList<>();
        urls = new ArrayList<>();

        messageListAdapter = new MessageListAdapter(this, messageList, localBusinessList);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));
        messageRecycler.setAdapter(messageListAdapter);

        findLatestLatitudeLongitude();

    }

    private void findLatestLatitudeLongitude() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener mlocListener = new MyLocationListener(this);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO Prompt permission
//            Toast.makeText(this, "Give Location Permission", Toast.LENGTH_SHORT).show();
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, mlocListener);

    }

    private void setListenerOnFab() {
        sendMessageFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence typedMessage = messageInputEditText.getText();
                if (typedMessage != null) {
                    String userMessage = typedMessage.toString().trim();
                    if (!userMessage.trim().isEmpty()) {
                        sendMessage(userMessage);
                    }
                }
                messageInputEditText.setText("");
                urls.add("");
            }
        });
    }

    private void sendMessage(final String message) {
        addMessage(message, self, 1);

        final RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://d63fe6c43c57.ngrok.io/user_sentences";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse", response);
                getResponse();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                String errorString = error.toString();
                if (response != null && response.data != null) {
                    errorString = new String(response.data);
                    Log.i("log error", errorString);
                }
                Toast.makeText(MainActivity.this, errorString, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("body", message);
                params.put("latitude",latestLatitude+"");
                params.put("longitude",latestLongitude+"");
                Log.i("sending", params.toString());
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void getResponse() {
        final RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://d63fe6c43c57.ngrok.io/response";

        addMessage("Okay, searching...", bot, 2);
        urls.add("");
        urls.add("");

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String searchTerm = "";

                        try {
                            JSONObject responseJson = new JSONArray(response).getJSONObject(0);
                            searchTerm = responseJson.getJSONObject("queryContext").getString("originalQuery");
                            JSONArray placesArray = responseJson.getJSONObject("places").getJSONArray("value");
                            List<LocalBusiness> businessList = getBusinessList(placesArray);

                            String heresAList = "Here's a list of places where you can eat " + searchTerm;
                            addMessage(heresAList, bot, 2);

                            for (int i=0; i<Math.min(5,businessList.size());i++) {
                                LocalBusiness business = businessList.get(i);
                                StringBuilder businessDetailsMessageBody = new StringBuilder();
                                businessDetailsMessageBody.append(business.getName()).append("\n");
                                businessDetailsMessageBody.append(business.getContactNumber()).append("\n");
                                businessDetailsMessageBody.append(business.getAddress()).append("\n");
                                businessDetailsMessageBody.append(business.getUrl());
                                urls.add(business.getUrl());

                                addMessage(businessDetailsMessageBody.toString(), bot, 2);
                            }

                        } catch (JSONException e) {
                            Log.e("Here", "JSON error: " + e.getMessage());
                            Toast.makeText(MainActivity.this, "An error occurred while parsing JSON.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", error.toString());
                Toast.makeText(MainActivity.this, "An error occurred while getting a response :(", Toast.LENGTH_LONG).show();
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 2, 1.0f));

        // 1 sec lag
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                queue.add(stringRequest);
            }
        }, 2000);
    }

    private List<LocalBusiness> getBusinessList(JSONArray places) throws JSONException {
        List<LocalBusiness> businessList = new ArrayList<>();

        for (int i = 0; i < places.length(); i++) {
            JSONObject place = places.getJSONObject(i);
            String type = place.getString("_type");
            String name = place.getString("name");
            String url = place.getString("url");
            String address = place.getJSONObject("address").getString("text");
            String contactNumber = place.getString("telephone");

            businessList.add(new LocalBusiness(type, address, name, contactNumber, url));
        }

        return businessList;
    }

    private void addMessage(String message, User sender, int type) {
        messageList.add(new Message(message, sender, Calendar.getInstance().getTimeInMillis(), type));
        messageListAdapter.setList(messageList, localBusinessList, urls);
        messageListAdapter.notifyDataSetChanged();
        messageRecycler.smoothScrollToPosition(messageList.size() - 1);
    }
}