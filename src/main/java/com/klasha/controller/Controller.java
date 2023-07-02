package com.klasha.controller;

import com.klasha.services.Service;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("klasha")
@RequiredArgsConstructor
public class Controller<T> {

    private final Service service;

    @GetMapping("filter/{limit}")
    public ResponseEntity<Map<String, List>> filter(@PathVariable("limit") int  limit) throws IOException, ParseException {
        return ResponseEntity.ok(service.filter(limit));
    }

    @GetMapping("stateAndCities/{country}")
    public ResponseEntity<Map<String,List>> stateAndCities(@PathVariable("country") String country) throws IOException, ParseException {
        return ResponseEntity.ok(service.statesAndCites(country));
    }

    @GetMapping("data/{country}")
    public ResponseEntity<Map<String,T>> getData(@PathVariable("country") String country) throws IOException, ParseException {
        return ResponseEntity.ok(service.countryData(country));
    }

}