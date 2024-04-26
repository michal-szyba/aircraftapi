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
    private final AirfieldRepository airfieldRepository;
    private final CruiseService cruiseService;

    public CruiseController(AirfieldRepository airfieldRepository, CruiseService cruiseService) {
        this.airfieldRepository = airfieldRepository;
        this.cruiseService = cruiseService;
    }

    @GetMapping(value ="/cruise", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CruiseResponse> cruise(@RequestBody CruiseRequest cruiseRequest) {
        return cruiseService.calculateCruise(cruiseRequest);
    }
    @GetMapping("/waypoints/{id1}/{id2}/{interval}")
    public ResponseEntity<?> waypoints(@PathVariable Long id1,
                                    @PathVariable Long id2,
                                    @PathVariable Double interval){
        Optional<Airfield> startOptional = airfieldRepository.findById(id1);
        Optional<Airfield> finishOptional = airfieldRepository.findById(id2);
        if(startOptional.isEmpty() || finishOptional.isEmpty()){
            return ResponseEntity.badRequest().body("one or both airfields do not exist");
        } else {
            Airfield start = startOptional.get();
            Airfield finish = finishOptional.get();
            return ResponseEntity.ok().body(Navigator.getIntervals(start, finish, interval));
        }
    }

}
