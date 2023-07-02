package com.klasha.utils;

import lombok.Data;

@Data
public class GetRateDto {

    private String country;
    private String currency;
    private Double amount;
}