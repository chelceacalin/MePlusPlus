package com.example.meplusplus.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Weight {

    String weight;

    public Weight() {
        this.weight = "N/A";
    }
}
