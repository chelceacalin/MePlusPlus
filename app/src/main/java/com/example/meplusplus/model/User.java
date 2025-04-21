package com.example.meplusplus.model;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    String username;
    String name;
    String email;
    String id;
    String bio;
    String imageurl;

    public User() {
        this.username = "N/A";
        this.name = "N/A";
        this.email = "N/A";
        this.id = "N/A";
        this.bio = "N/A";
        this.imageurl = "N/A";
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
