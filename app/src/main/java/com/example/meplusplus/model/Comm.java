package com.example.meplusplus.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Comm {
    String id;
    String publisher;
    String strike;

    public Comm() {
        this.id = "N/A";
        this.publisher = "N/A";
        this.strike = "N/A";
    }
}
