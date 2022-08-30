package com.example.meplusplus.DataSets;

/*

       Status: RFP
       CREATED DATE: 8/27/2022
       UPDATED DATE: 8/27/2022
 */
@SuppressWarnings("ALL")
public class Comm {
    String id;
    String publisher;
    String strike;

    public Comm() {
        this.id = "N/A";
        this.publisher = "N/A";
        this.strike = "N/A";
    }

    public Comm(String id, String publisher, String strike) {

        this.id = id;
        this.publisher = publisher;
        this.strike = strike;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getStrike() {
        return strike;
    }

    public void setStrike(String strike) {
        this.strike = strike;
    }

    @Override
    public String toString() {
        return "Comm{" +
                "publisher='" + publisher + '\'' +
                ", strike='" + strike + '\'' +
                '}';
    }
}
