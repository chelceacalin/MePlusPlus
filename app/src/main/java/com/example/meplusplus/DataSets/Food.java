package com.example.meplusplus.DataSets;

public class Food {

String itemID;
Float sumCalories;
Float sumProtein;
Float sumCarbs;
Float sumFats;
Float sumSugar;

    public Food() {
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public Float getSumCalories() {
        return sumCalories;
    }

    public void setSumCalories(Float sumCalories) {
        this.sumCalories = sumCalories;
    }

    public Float getSumProtein() {
        return sumProtein;
    }

    public void setSumProtein(Float sumProtein) {
        this.sumProtein = sumProtein;
    }

    public Float getSumCarbs() {
        return sumCarbs;
    }

    public void setSumCarbs(Float sumCarbs) {
        this.sumCarbs = sumCarbs;
    }

    public Float getSumFats() {
        return sumFats;
    }

    public void setSumFats(Float sumFats) {
        this.sumFats = sumFats;
    }

    public Float getSumSugar() {
        return sumSugar;
    }

    public void setSumSugar(Float sumSugar) {
        this.sumSugar = sumSugar;
    }

    public Food(String itemID, Float sumCalories, Float sumProtein, Float sumCarbs, Float sumFats, Float sumSugar) {
        this.itemID = itemID;
        this.sumCalories = sumCalories;
        this.sumProtein = sumProtein;
        this.sumCarbs = sumCarbs;
        this.sumFats = sumFats;
        this.sumSugar = sumSugar;
    }
}
