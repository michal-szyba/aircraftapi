package com.example.aircraftapi.weather;

import com.example.aircraftapi.location.Location;
import com.example.aircraftapi.navigator.Navigator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WeatherService {

    @Value("${weather.api.url}")
    private String weatherUrl;
    @Value("${weather.api.longitude}")
    private String longitudeToReplace;
    @Value("${weather.api.latitude}")
    private String latitudeToReplace;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public WeatherService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl(weatherUrl).build();
        this.objectMapper = objectMapper;
    }

    public WeatherData getWeatherData(Location location){
        Double latitude = Navigator.parseCoordinates(location.getLatitude());
        Double longitude = Navigator.parseCoordinates(location.getLongitude());
        String modifiedUrl = weatherUrl
                .replace(latitudeToReplace, latitude.toString())
                .replace(longitudeToReplace, longitude.toString());
        return webClient.get()
                .uri(modifiedUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::jsonToWeatherData)
                .block();

    }
    private WeatherData jsonToWeatherData(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, WeatherData.class);
        } catch (JsonProcessingException e) {
            e.getMessage();
        }
        return null;
    }
}
