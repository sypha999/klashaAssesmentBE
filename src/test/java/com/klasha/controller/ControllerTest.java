package com.klasha.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.klasha.services.Service;
import com.klasha.utils.Convert;
import com.klasha.utils.GetRateDto;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

class ControllerTest {

    @Test
    void testFilter() throws IOException, ParseException {

        Service service = mock(Service.class);
        when(service.filter(anyInt())).thenReturn(new HashMap<>());
        ResponseEntity<Map<String, List>> actualFilterResult = (new Controller<>(service)).filter(5);
        assertTrue(actualFilterResult.hasBody());
        assertEquals(200, actualFilterResult.getStatusCodeValue());
        assertTrue(actualFilterResult.getHeaders().isEmpty());
        verify(service).filter(anyInt());
    }

    @Test
    void testStateAndCities() throws IOException, ParseException {
        Service service = mock(Service.class);
        when(service.statesAndCites(Mockito.<String>any())).thenReturn(new HashMap<>());
        ResponseEntity<Map<String, List>> actualStateAndCitiesResult = (new Controller<>(service)).stateAndCities("Nigeria");
        assertTrue(actualStateAndCitiesResult.hasBody());
        assertEquals(200, actualStateAndCitiesResult.getStatusCodeValue());
        assertTrue(actualStateAndCitiesResult.getHeaders().isEmpty());
        verify(service).statesAndCites(Mockito.<String>any());
    }

    @Test
    void testGetData() throws IOException, ParseException {
        Service service = mock(Service.class);
        when(service.countryData(Mockito.<String>any())).thenReturn(new HashMap<>());
        ResponseEntity<Map<String, Object>> actualData = (new Controller<>(service)).getData("GB");
        assertTrue(actualData.hasBody());
        assertEquals(200, actualData.getStatusCodeValue());
        assertTrue(actualData.getHeaders().isEmpty());
        verify(service).countryData(Mockito.<String>any());
    }

    @Test
    void testGetRate() throws IOException, ParseException {
        Convert convert = new Convert();
        convert.setAmount(10.0d);
        convert.setCurrency("GBP");
        Service service = mock(Service.class);
        when(service.convertCurrency(Mockito.<GetRateDto>any())).thenReturn(convert);
        Controller<Object> controller = new Controller<>(service);

        GetRateDto rateDto = new GetRateDto();
        rateDto.setAmount(10.0d);
        rateDto.setCountry("GB");
        rateDto.setCurrency("GBP");
        ResponseEntity<Convert> actualRate = controller.getRate(rateDto);
        assertTrue(actualRate.hasBody());
        assertTrue(actualRate.getHeaders().isEmpty());
        assertEquals(200, actualRate.getStatusCodeValue());
        verify(service).convertCurrency(Mockito.<GetRateDto>any());
    }
}