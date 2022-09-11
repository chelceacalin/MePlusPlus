package com.example.meplusplus.DataSets;

import androidx.annotation.NonNull;

public class WaterModel {

    int sumWater;

    public WaterModel(){
        this.sumWater=0;
    }

    public WaterModel( int sumWater) {
        this.sumWater = sumWater;
    }


    public int getSumWater() {
        return sumWater;
    }

    public void setSumWater(int sumWater) {
        this.sumWater = sumWater;
    }

    @NonNull
    @Override
    public String toString() {
        return "WaterModel{" +
                ", sumWater=" + sumWater +
                '}';
    }
}
