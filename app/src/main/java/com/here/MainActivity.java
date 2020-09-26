package com.here;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout messageInputLayout;
    private TextInputEditText messageInputEditText;
    private FloatingActionButton sendMessageFab;

    private RecyclerView messageRecycler;
    private MessageListAdapter messageListAdapter;

    private List<Message> messageList;

    User self = new User("you");
    User bot = new User("HereBot");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageInputLayout = findViewById(R.id.messageInputLayout);
        messageInputEditText = findViewById(R.id.messageInputEditText);
        sendMessageFab = findViewById(R.id.sendMessageFab);

        messageRecycler = findViewById(R.id.messageListRecyclerView);

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

        messageList = new ArrayList<>();

//        messageList.add(new Message("I wanna eat some fast food", self, Calendar.getInstance().getTimeInMillis(), 1));
//        messageList.add(new Message("Great! Here are some restaurants in your local area", bot, Calendar.getInstance().getTimeInMillis(), 2));
//        messageList.add(new Message("Which of these has spicy food?", self, Calendar.getInstance().getTimeInMillis(), 1));
//        messageList.add(new Message("Cheng's has a reputation for spicy food.", bot, Calendar.getInstance().getTimeInMillis(), 2));
//        messageList.add(new Message("Sounds great", self, Calendar.getInstance().getTimeInMillis(), 1));
//        messageList.add(new Message("Do you want me to show the directions?", bot, Calendar.getInstance().getTimeInMillis(), 2));
//        messageList.add(new Message("yes please", self, Calendar.getInstance().getTimeInMillis(), 1));
//        messageList.add(new Message("Here you go!", bot, Calendar.getInstance().getTimeInMillis(), 2));


        messageListAdapter = new MessageListAdapter(this, messageList);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));
        messageRecycler.setAdapter(messageListAdapter);
    }

    private void sendMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        System.out.println(message);

        messageList.add(new Message(message, self, Calendar.getInstance().getTimeInMillis(),1));

        messageListAdapter.setMessageList(messageList);
        messageListAdapter.notifyDataSetChanged();

        getResponse(message);

    }

    private void getResponse(String input) {
        // TODO: API Call here.

        messageList.add(new Message("Okay", bot, Calendar.getInstance().getTimeInMillis(),2));

        messageListAdapter.setMessageList(messageList);
        messageListAdapter.notifyDataSetChanged();

    }

}