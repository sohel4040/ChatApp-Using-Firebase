package com.example.firebasechat;

public class Chat {
    String sender;
    String receiver;
    String message;
    boolean seen;

    public Chat(String sender, String receiver, String message,boolean seen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message=message;
        this.seen=seen;
    }

    public boolean isseen() {
        return seen;
    }

    public void setisseen(boolean isseen) {
        this.seen = isseen;
    }

    public Chat()
    {

    }
    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

}
