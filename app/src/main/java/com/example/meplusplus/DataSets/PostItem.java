package com.example.meplusplus.DataSets;

import androidx.annotation.NonNull;

/*
       Status: RFP
       CREATED DATE: 8/25/2022
       UPDATED DATE: 8/25/2022
 */
@SuppressWarnings("ALL")
public class PostItem {
    String description;
    String imageurl;
    String postid;
    String publisher;
    String ingredients;

    public PostItem() {
        this.description = "N/A";
        this.imageurl = "N/A";
        this.postid = "N/A";
        this.publisher = "N/A";
        ingredients="";
    }

    public PostItem(String description, String imageurl, String postid, String publisher,String ingredients) {
        this.description = description;
        this.imageurl = imageurl;
        this.postid = postid;
        this.publisher = publisher;
        this.ingredients=ingredients;
    }

    @NonNull
    @Override
    public String toString() {
        return "PostItem{" +
                "description='" + description + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", postid='" + postid + '\'' +
                ", publisher='" + publisher + '\'' +
                '}';
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
