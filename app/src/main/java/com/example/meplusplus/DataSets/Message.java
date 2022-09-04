package com.example.meplusplus.DataSets;

import com.example.meplusplus.Chatting.MessageActivity;

public class Message {

    String message;
    String whosentit;
public Message(){
    this.message="N/A";
    this.whosentit="N/A";
}

    public Message(String message, String whosentit) {
        this.message = message;
        this.whosentit = whosentit;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWhosentit() {
        return whosentit;
    }

    public void setWhosentit(String whosentit) {
        this.whosentit = whosentit;
    }
}
