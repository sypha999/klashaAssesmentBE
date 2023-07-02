package com.klasha.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class Helper <T>{
    public ResponseEntity<JSONObject> makeRequestWithRedirect(String req,String url){
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
        final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        final HttpClient httpClient = HttpClientBuilder.create()
                .setRedirectStrategy(new DefaultRedirectStrategy())
                .build();
        factory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(factory);

        HttpEntity<String> httpEntity = new HttpEntity<>( req,headers);
        return restTemplate.postForEntity(url,httpEntity, JSONObject.class);
    }

    public String capitalizeFirst(String s){
        return s.charAt(0)+s.substring(1).toLowerCase();
    }

    public List extractData(List<Map> mp,String key) throws ParseException {
        List<String> data = new ArrayList<>();
        for(Map myMap:mp){
            data.add(this.capitalizeFirst(this.parseJson(myMap.toString()).get(key).toString()));
        }
   return data;
    }

    public JSONObject parseJson(String js) throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(js);
    }

    public String writeAsString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    public String getRequestableState(String state){
        state= state.toLowerCase();
        if(state.contains("lagos")){return "Lagos";}
        if(state.contains("state")){
            return this.capitalizeFirst(state.substring(0,state.indexOf("state")));
    }
        else{ return this.capitalizeFirst(state);}
}



}