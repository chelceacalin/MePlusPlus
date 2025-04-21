package com.example.meplusplus.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WaterModel {

    int sumWater;

    public WaterModel() {
        this.sumWater = 0;
    }

}
