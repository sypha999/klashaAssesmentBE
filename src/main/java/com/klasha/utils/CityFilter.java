package com.klasha.utils;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CityFilter {
            private int limit;
            private String order;
            private String orderBy;
            private String country;
    public CityFilter(int limit, String order, String orderBy, String country) {
        this.limit = limit;
        this.order = order;
        this.orderBy = orderBy;
        this.country = country;
    }
}