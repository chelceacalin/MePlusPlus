package com.example.meplusplus.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
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
        ingredients = "";
    }
}
