package com.klasha.utils;

import lombok.Data;

@Data
public class Cities {
    private String  country;
    private String state;

    public Cities(String country, String state) {
        this.country = country;
        this.state = state;
    }
}