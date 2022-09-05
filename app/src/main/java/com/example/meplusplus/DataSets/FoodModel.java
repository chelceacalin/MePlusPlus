package com.example.meplusplus.DataSets;

import androidx.annotation.NonNull;

import java.util.Objects;

public class FoodModel {
    String name;
    String MeasureUnits;
    String sugar;
    float calories;
    int quantity;
    float fats;
    float carbs;
    float protein;


public FoodModel(){
    this.name="N/A";
    quantity=0;
    MeasureUnits ="N/A";
    calories=0;
    fats=0;
    carbs=0;
    protein=0;
    sugar="N/A";
}


    public FoodModel(String name, float calories, int quantity, String MeasureUnits, float fats, float carbs, float protein, String  sugar) {
        this.name = name;
        this.calories = calories;
        this.quantity = quantity;
        this.MeasureUnits = MeasureUnits;
        this.fats = fats;
        this.carbs = carbs;
        this.protein = protein;
        this.sugar = sugar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodModel foodModel = (FoodModel) o;
        return Float.compare(foodModel.calories, calories) == 0 && quantity == foodModel.quantity && Float.compare(foodModel.fats, fats) == 0 && Float.compare(foodModel.carbs, carbs) == 0 && Float.compare(foodModel.protein, protein) == 0 && Objects.equals(name, foodModel.name) && Objects.equals(MeasureUnits, foodModel.MeasureUnits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, calories, quantity, MeasureUnits, fats, carbs, protein, sugar);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMeasureUnits() {
        return MeasureUnits;
    }

    public void setMeasureUnits(String measureUnits) {
        this.MeasureUnits = measureUnits;
    }

    public float getFats() {
        return fats;
    }

    public void setFats(float fats) {
        this.fats = fats;
    }

    public float getCarbs() {
        return carbs;
    }

    public void setCarbs(float carbs) {
        this.carbs = carbs;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public String  getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    @NonNull
    @Override
    public String toString() {
        return
                "Name: " + name + "\n" +
                        "Quantity: " + quantity + "\n"+
                        "Calories: " + calories + "\n"+
                "UnitOfMeasure: '" + MeasureUnits +  "\n"+
                "Fats: " + fats + "\n"+
                "Carbs: " + carbs + "\n"+
                "Protein: " + protein + "\n"+
                "Sugar: " + sugar + "\n";
    }
}
