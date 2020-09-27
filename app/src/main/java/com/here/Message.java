package com.here;

import com.here.models.User;

class Message {
    private String message;
    private User sender;
    private long createdAt;
    private int type;
    private boolean isResponse;

    public Message(String message, User sender, long createdAt, int type) {
        this.message = message;
        this.sender = sender;
        this.createdAt = createdAt;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
