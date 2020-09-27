package com.here;

import android.content.Context;
import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.here.models.LocalBusiness;

import io.github.ponnamkarthik.richlinkpreview.RichLinkView;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;

class MessageListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private List<Message> messageList;
    private List<LocalBusiness> businessList;
    private List<String> urls;

    public MessageListAdapter(Context mContext, List<Message> messageList, List<LocalBusiness> businessList) {
        this.mContext = mContext;
        this.messageList = messageList;
        this.businessList = businessList;
    }

    public void setList(List<Message> messageList, List<LocalBusiness> localBusinessList, List<String> urls) {
        this.messageList = messageList;
        this.businessList = localBusinessList;
        this.urls = urls;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getType() == 1) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message, urls.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {

        TextView messageBody, timeText, nameText;
        ImageView profilePicture;
        RecyclerView businessRecyclerView;
        RichLinkView richLinkView;
        ResponseAdapter responseAdapter;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageBody = itemView.findViewById(R.id.received_message_body);
            timeText = itemView.findViewById(R.id.received_message_timestamp);
            nameText = itemView.findViewById(R.id.recevied_message_sender_name);
            profilePicture = itemView.findViewById(R.id.received_message_sender_profile_picture);
            richLinkView = itemView.findViewById(R.id.richLink);
//            businessRecyclerView = itemView.findViewById(R.id.rv_business);

            responseAdapter = new ResponseAdapter(mContext, businessList);

        }

        void bind(Message message, final String url) {

//            businessRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//            businessRecyclerView.setAdapter(responseAdapter);

            messageBody.setText(message.getMessage());
            timeText.setText(DateUtils.formatDateTime(mContext, message.getCreatedAt(), DateUtils.FORMAT_SHOW_TIME));
            nameText.setText(message.getSender().getName());
            System.out.println("url is "+url);
            if(url.startsWith("https")) {
                richLinkView.setLink(url, new ViewListener() {
                    @Override
                    public void onSuccess(boolean status) {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
//            if(!url.equals("")) {
//
//            }

        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageBody, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageBody = itemView.findViewById(R.id.sent_message_body);
            timeText = itemView.findViewById(R.id.sent_message_timestamp);
        }

        void bind(Message message) {
            messageBody.setText(message.getMessage());
            timeText.setText(DateUtils.formatDateTime(mContext, message.getCreatedAt(), DateUtils.FORMAT_SHOW_TIME));
        }
    }
}
