package com.example.meplusplus.DataSets;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Message {

    String messageID;
    String message;
    String whosentit;
    String toWhom;

    public Message() {
        this.messageID = "N/A";
        this.message = "N/A";
        this.whosentit = "N/A";
        this.toWhom = "N/A";
    }

    public Message(String messageID, String message, String whosentit, String toWhom) {
        this.messageID = messageID;
        this.message = message;
        this.whosentit = whosentit;
        this.toWhom = toWhom;
    }

    @NonNull
    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", whosentit='" + whosentit + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(message, message1.message) && Objects.equals(whosentit, message1.whosentit);
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getToWhom() {
        return toWhom;
    }

    public void setToWhom(String toWhom) {
        this.toWhom = toWhom;
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, whosentit);
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
