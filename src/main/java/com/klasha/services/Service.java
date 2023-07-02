package com.klasha.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.klasha.exceptions.CountryNotFound;
import com.klasha.exceptions.CustomException;
import com.klasha.exceptions.IllegalInputException;
import com.klasha.exceptions.RateNotFound;
import com.klasha.utils.*;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;


import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service <T>{
    private final Helper helper;

    @Value("${cities.filter}")
    private String filterUrl;

    @Value("${countries.states}")
    private String statesUrl;

    @Value("${cities.states}")
    private String citiesUrl;

    @Value("${country.population}")
    private String populationUrl;

    @Value("${capital.city}")
    private String capitalUrl;

    @Value("${country.location}")
    private String locationUrl;

    @Value("${country.currency}")
    private String currencyUrl;

    public Map<String ,List> filter(int limit) throws IOException, ParseException {

        CityFilter gh  = new CityFilter(limit,"dsc","population","ghana");
        CityFilter nz  = new CityFilter(limit,"dsc","population","new zealand");
        CityFilter it  = new CityFilter(limit,"dsc","population","italy");

        if(limit<1) throw new IllegalInputException("");

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

        ResponseEntity response;
        try{
           response = helper.makeRequestWithRedirect(req,statesUrl);
        }
        catch (Exception e){
            throw new CountryNotFound("");
        }

        Object all = response.getBody();
        JSONObject all2 = (JSONObject) helper.parseJson(all.toString()).get("data");
        List <String> allStates = helper.extractData((List<Map>) all2.get("states"),"name");

        HashMap<String,List> data = new HashMap<>();
        for(String state:allStates){
            List <String> allCities = new ArrayList<>();

            Cities city = new Cities(country, helper.getRequestableState(state));
            String req2 = helper.writeAsString(city);
            ResponseEntity resp;
            try {
                resp = helper.makeRequestWithRedirect(req2,citiesUrl);
            }
            catch (Exception e){
                throw new CustomException("Something went wrong");
            }
            JSONObject allc = helper.parseJson(resp.getBody().toString());
            List<String> cities = (List<String>) allc.get("data");

            data.put(state,cities);
        }

        return data;
    }

    public Map<String,T> countryData(String country) throws JsonProcessingException, ParseException {

        Map<String,T> data = new HashMap<>();
        States states = new States(country);
        String  req = helper.writeAsString(states);
        ResponseEntity response;
        try {
            response = helper.makeRequestWithRedirect(req,populationUrl);
        }
        catch (Exception e){
            throw new CountryNotFound("");
        }
        Object allPop = response.getBody();
        JSONObject all2 = (JSONObject) helper.parseJson(allPop.toString()).get("data");
        List <String> allPopulation = helper.extractData((List<Map>) all2.get("populationCounts"),"value");
        data.put("Population", (T) allPopulation.get(allPopulation.size()-1));

        Object allCap = helper.makeRequestWithRedirect(req,capitalUrl).getBody();
        JSONObject allCap2 = (JSONObject) helper.parseJson(allCap.toString()).get("data");
        data.put("Capital", (T) allCap2.get("capital"));

        Object loc = helper.makeRequestWithRedirect(req,locationUrl).getBody();
        JSONObject loc2 = (JSONObject) helper.parseJson(loc.toString()).get("data");
        Location location =  new Location();
        location.setLatitude(Double.parseDouble(loc2.get("lat").toString()));
        location.setLongitude(Double.parseDouble(loc2.get("long").toString()));
        data.put("Location", (T) location);

        Object curr = helper.makeRequestWithRedirect(req,currencyUrl).getBody();
        JSONObject curr2 = (JSONObject) helper.parseJson(curr.toString()).get("data");
        Iso iso = new Iso();
        iso.setIso2(curr2.get("iso2").toString());
        iso.setIso3(curr2.get("iso3").toString());
        data.put("Currency", (T) curr2.get("currency").toString());
        data.put("ISO 2&3", (T) iso);


        return data;
    }

    public Convert convertCurrency(GetRateDto rateDto) throws ParseException, IOException {
        States states = new States(rateDto.getCountry());
        String  req = helper.writeAsString(states);
        ResponseEntity response;
        try {
            response = helper.makeRequestWithRedirect(req,currencyUrl);
        }
        catch (Exception e){
            throw new CountryNotFound("");
        }
        Object curr = response.getBody();
        JSONObject curr2 = (JSONObject) helper.parseJson(curr.toString()).get("data");
        Convert convert = new Convert();
        convert.setCurrency(curr2.get("currency").toString());
        Map<String,Double> rates = helper.loadExchageRate();
        Double rate ;
        Double convAmount;
        String search = curr2.get("currency").toString() +" to "+rateDto.getCurrency();

        rate = rates.get(search);
        if (rate == null){
            throw new RateNotFound("Rate not available");
        }
        else{
            DecimalFormat df = new DecimalFormat("0.0000");
            df.setRoundingMode(RoundingMode.UP);
            convAmount = Double.valueOf(df.format(rateDto.getAmount()/rate));
        }

        convert.setAmount(convAmount);


        return convert;
    }

}