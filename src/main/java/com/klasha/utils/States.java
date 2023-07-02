package com.klasha.utils;

import lombok.Data;


@Data
public class States {
    private String  country;

    public States(String country) {
        this.country = country;
    }
}