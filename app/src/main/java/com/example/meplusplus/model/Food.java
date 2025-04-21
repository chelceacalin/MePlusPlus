package com.example.meplusplus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Food {

    String itemID;
    Float sumCalories;
    Float sumProtein;
    Float sumCarbs;
    Float sumFats;
    Float sumSugar;
}
