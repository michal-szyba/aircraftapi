package com.example.aircraftapi.weather;

import com.example.aircraftapi.location.Location;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather/current")
    public WeatherData getCurrentWeather(@RequestBody Location location){
        return weatherService.getWeatherData(location);
    }

}
