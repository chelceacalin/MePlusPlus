package com.example.meplusplus.DataSets;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Message {

    String message;
    String whosentit;

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

    @Override
    public int hashCode() {
        return Objects.hash(message, whosentit);
    }

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
