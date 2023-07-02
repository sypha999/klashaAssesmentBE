package com.klasha.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.klasha.utils.Cities;
import com.klasha.utils.CityFilter;
import com.klasha.utils.Helper;
import com.klasha.utils.States;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {
    private final Helper helper;

    @Value("${cities.filter}")
    private String filterUrl;

    @Value("${countries.states}")
    private String statesUrl;

    @Value("${cities.states}")
    private String citiesUrl;

    public Map<String ,List> filter(int limit) throws IOException, ParseException {

        CityFilter gh  = new CityFilter(limit,"dsc","population","ghana");
        CityFilter nz  = new CityFilter(limit,"dsc","population","new zealand");
        CityFilter it  = new CityFilter(limit,"dsc","population","italy");

        String requestGh = helper.writeAsString(gh);
        String requestNz = helper.writeAsString(nz);
        String requestIt = helper.writeAsString(it);

        Object ghResponse = helper.makeRequestWithRedirect(requestGh,filterUrl).getBody();
        Object nzResponse = helper.makeRequestWithRedirect(requestNz,filterUrl).getBody();
        Object itResponse = helper.makeRequestWithRedirect(requestIt,filterUrl).getBody();

        JSONObject  ghData = helper.parseJson(ghResponse.toString());
        JSONObject  nzData = helper.parseJson(nzResponse.toString());
        JSONObject  itData = helper.parseJson(itResponse.toString());


        ghData = helper.parseJson(ghData.toString());
        nzData = helper.parseJson(nzData.toString());
        itData = helper.parseJson(itData.toString());

        List<String> ghList = helper.extractData((List<Map>) ghData.get("data"),"city");
        List<String> nzList = helper.extractData((List<Map>) nzData.get("data"),"city");
        List<String> itList = helper.extractData((List<Map>) itData.get("data"),"city");


        Map<String,List> data = new HashMap<>();
        data.put("Ghana",ghList);
        data.put("New Zealand",nzList);
        data.put("Italy",itList);

        return data;
    }

    public Map<String, List> statesAndCites(String country) throws JsonProcessingException, ParseException {
        States states = new States(country);
        String  req = helper.writeAsString(states);
        Object all = helper.makeRequestWithRedirect(req,statesUrl).getBody();
        JSONObject all2 = (JSONObject) helper.parseJson(all.toString()).get("data");
        List <String> allStates = helper.extractData((List<Map>) all2.get("states"),"name");

        HashMap<String,List> data = new HashMap<>();
        for(String state:allStates){
            List <String> allCities = new ArrayList<>();

            //fix bug on Lagos state
            if(state.contains("Lagos")){state= helper.getRequestableState(state);}

            Cities city = new Cities(country, state);
            String req2 = helper.writeAsString(city);
            ResponseEntity resp = helper.makeRequestWithRedirect(req2,citiesUrl);
            JSONObject allc = helper.parseJson(resp.getBody().toString());
            List<String> cities = (List<String>) allc.get("data");

            data.put(state,cities);

        }

        return data;
    }


    public void countryData(String country){
//        population
//        capital city
//        location
//                currency
//        ISO2&3

    }

}