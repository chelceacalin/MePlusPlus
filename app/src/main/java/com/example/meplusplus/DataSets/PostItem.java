package com.example.meplusplus.DataSets;

/*
       CREATED DATE: 8/25/2022
       UPDATED DATE: 8/25/2022
 */
public class PostItem {
     String description;
     String imageurl;
     String postid;
     String publisher;

    public PostItem() {
        this.description="N/A";
        this.imageurl="N/A";
        this.postid="N/A";
        this.publisher="N/A";
    }

    public PostItem(String description, String imageurl, String postid, String publisher) {
        this.description = description;
        this.imageurl = imageurl;
        this.postid = postid;
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "PostItem{" +
                "description='" + description + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", postid='" + postid + '\'' +
                ", publisher='" + publisher + '\'' +
                '}';
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
