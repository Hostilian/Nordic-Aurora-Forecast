package com.example;

import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testPredictAurora() {
        String mockWeatherData = "{\"properties\":{\"timeseries\":[{\"time\":\"2023-05-15T23:00:00Z\",\"data\":{\"instant\":{\"details\":{\"cloud_area_fraction\":10.0,\"air_temperature\":-2.0}}}}]}}";
        assertTrue(Main.predictAurora(mockWeatherData));

        mockWeatherData = "{\"properties\":{\"timeseries\":[{\"time\":\"2023-05-15T12:00:00Z\",\"data\":{\"instant\":{\"details\":{\"cloud_area_fraction\":10.0,\"air_temperature\":-2.0}}}}]}}";
        assertFalse(Main.predictAurora(mockWeatherData));

        mockWeatherData = "{\"properties\":{\"timeseries\":[{\"time\":\"2023-05-15T23:00:00Z\",\"data\":{\"instant\":{\"details\":{\"cloud_area_fraction\":50.0,\"air_temperature\":-2.0}}}}]}}";
        assertFalse(Main.predictAurora(mockWeatherData));
    }
}