package com.example.meplusplus.DataSets;

import androidx.annotation.NonNull;

/*
       CREATED DATE: 8/17/2022
       UPDATED DATE: 8/17/2022
 */
public class User {
    String username;
    String name;
    String email;
    String id;
    String bio;
    String imageurl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User()
    {
        this.username="N/A";
        this.name="N/A";
        this.email = "N/A";
        this.id = "N/A";
        this.bio = "N/A";
        this.imageurl="N/A";
    }
    public User(String username,String name, String email, String id, String bio, String imageurl) {
     this.username=username;
        this.name = name;
        this.email = email;
        this.id = id;
        this.bio = bio;
        this.imageurl = imageurl;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                ", bio='" + bio + '\'' +
                ", imageurl='" + imageurl + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
