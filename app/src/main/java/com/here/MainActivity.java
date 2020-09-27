package com.here;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.here.models.LocalBusiness;
import com.here.models.MyLocationListener;
import com.here.models.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout messageInputLayout;
    private TextInputEditText messageInputEditText;
    private FloatingActionButton sendMessageFab;

    private RecyclerView messageRecycler;
    private MessageListAdapter messageListAdapter;

    private List<Message> messageList;
    private List<LocalBusiness> localBusinessList;

    public static double latestLatitude = -1.0;
    public static double latestLongitude = -1.0;

    private User self = new User("you");
    private User bot = new User("HereBot");

    LocationManager locationManager;

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
            Toast.makeText(this, "Give Location Permission", Toast.LENGTH_SHORT).show();
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
            }
        });
    }

    private void sendMessage(String message) {
        messageList.add(new Message(message, self, Calendar.getInstance().getTimeInMillis(), 1));
        messageListAdapter.setList(messageList, localBusinessList);
        messageListAdapter.notifyDataSetChanged();
        messageRecycler.smoothScrollToPosition(messageList.size() - 1);

        getResponse(message);
    }

    private void getResponse(String input) {
        // TODO: API Call here.

        messageList.add(new Message("Okay", bot, Calendar.getInstance().getTimeInMillis(), 2));
        messageListAdapter.setList(messageList, localBusinessList);
        messageListAdapter.notifyDataSetChanged();
        messageRecycler.smoothScrollToPosition(messageList.size() - 1);
    }
}