package com.example.meplusplus.model;

import androidx.annotation.NonNull;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FoodModel {
    String name;
    String MeasureUnits;
    float sugar;
    float calories;
    int quantity;
    float fats;
    float carbs;
    float protein;


    public FoodModel() {
        this.name = "N/A";
        quantity = 0;
        MeasureUnits = "N/A";
        calories = 0;
        fats = 0;
        carbs = 0;
        protein = 0;
        sugar = 0;
    }


    public FoodModel(String name, float calories, int quantity, String MeasureUnits, float fats, float carbs, float protein, float sugar) {
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

    @NonNull
    @Override
    public String toString() {
        return
                "Name: " + name + "\n" +
                        "Quantity: " + quantity + "\n" +
                        "Calories: " + calories + "\n" +
                        "UnitOfMeasure: '" + MeasureUnits + "\n" +
                        "Fats: " + fats + "\n" +
                        "Carbs: " + carbs + "\n" +
                        "Protein: " + protein + "\n" +
                        "Sugar: " + sugar + "\n";
    }
}
