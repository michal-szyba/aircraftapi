package com.example.aircraftapi.location.airfield.cruise;

import com.example.aircraftapi.aircraft.Aircraft;
import com.example.aircraftapi.aircraft.AircraftRepository;
import com.example.aircraftapi.aircraft.armament.Armament;
import com.example.aircraftapi.aircraft.armament.ArmamentRepository;
import com.example.aircraftapi.location.airfield.Airfield;
import com.example.aircraftapi.location.airfield.AirfieldRepository;
import com.example.aircraftapi.location.Location;
import com.example.aircraftapi.location.airfield.cruise.response.CruiseResponse;
import com.example.aircraftapi.navigator.Navigator;
import com.example.aircraftapi.weather.WeatherData;
import com.example.aircraftapi.weather.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.*;

@RestController
public class CruiseController {
    private final AircraftRepository aircraftRepository;
    private final AirfieldRepository airfieldRepository;
    private final ArmamentRepository armamentRepository;
    private final WeatherService weatherService;
    private final CruiseService cruiseService;

    public CruiseController(AircraftRepository aircraftRepository, AirfieldRepository airfieldRepository, ArmamentRepository armamentRepository, WeatherService weatherService, CruiseService cruiseService) {
        this.aircraftRepository = aircraftRepository;
        this.airfieldRepository = airfieldRepository;
        this.armamentRepository = armamentRepository;

        this.weatherService = weatherService;
        this.cruiseService = cruiseService;
    }

    @GetMapping(value ="/cruise", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CruiseResponse> cruise(@RequestBody CruiseRequest cruiseRequest) {
        return cruiseService.calculateCruise(cruiseRequest);
    }
    @GetMapping("/waypoints/{id1}/{id2}/{interval}")
    public List<Location> waypoints(@PathVariable Long id1,
                                    @PathVariable Long id2,
                                    @PathVariable Double interval){
        Airfield start = airfieldRepository.findById(id1).get();
        Airfield finish = airfieldRepository.findById(id2).get();
        return Navigator.getIntervals(start, finish, interval);
    }

}
