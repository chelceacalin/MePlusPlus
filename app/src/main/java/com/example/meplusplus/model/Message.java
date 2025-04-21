package com.example.meplusplus.model;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
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


    @NonNull
    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", whosentit='" + whosentit + '\'' +
                '}';
    }

}
