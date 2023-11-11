package com.example.aircraftapi.location.airfield.cruise;

import com.example.aircraftapi.aircraft.Aircraft;
import com.example.aircraftapi.aircraft.AircraftRepository;
import com.example.aircraftapi.aircraft.armament.Armament;
import com.example.aircraftapi.aircraft.armament.ArmamentRepository;
import com.example.aircraftapi.location.airfield.Airfield;
import com.example.aircraftapi.location.airfield.AirfieldRepository;
import com.example.aircraftapi.location.Location;
import com.example.aircraftapi.navigator.Navigator;
import com.example.aircraftapi.weather.WeatherData;
import com.example.aircraftapi.weather.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.http.HttpResponse;
import java.util.*;

@RestController
public class CruiseController {
    private final AircraftRepository aircraftRepository;
    private final AirfieldRepository airfieldRepository;
    private final ArmamentRepository armamentRepository;
    private final WeatherService weatherService;

    public CruiseController(AircraftRepository aircraftRepository, AirfieldRepository airfieldRepository, ArmamentRepository armamentRepository, WeatherService weatherService) {
        this.aircraftRepository = aircraftRepository;
        this.airfieldRepository = airfieldRepository;
        this.armamentRepository = armamentRepository;

        this.weatherService = weatherService;
    }

    @GetMapping("/cruise")
    public ResponseEntity<?> cruise(@RequestBody CruiseRequest cruiseRequest) {
        Long aircraftId = cruiseRequest.getAircraftId();
        Long startAirfieldId = cruiseRequest.getStartAirfieldId();
        Long finishAirfieldId = cruiseRequest.getFinishAirfieldId();

        Aircraft aircraft = aircraftRepository.findById(aircraftId).get();
        Airfield startAirfield = airfieldRepository.findById(startAirfieldId).get();
        Airfield finishAirfield = airfieldRepository.findById(finishAirfieldId).get();
        Map<Long, Long> armamentMap = cruiseRequest.getArmamentMap();

        List<Armament> armamentList = new ArrayList<>();
        Double distance = Navigator.calculateDistance(startAirfield, finishAirfield);

        if(distance > aircraft.getMaxRange()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not possible: out of range");
        } else {
            Double armamentWeight = 0.0;
            for(Long key : armamentMap.keySet()){
                Armament armament = armamentRepository.findById(key).get();
                armament.setQuantity(armamentMap.get(key));
                armamentWeight += armament.getWeight() * armament.getQuantity();
                armamentList.add(armament);
            }
            if(armamentWeight + aircraft.getEmptyWeight() > aircraft.getMaxTakeoffWeight()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not possible: overloaded");
            } else {
                Double travelTime = distance / aircraft.getCruiseSpeed();
                List<Location> waypoints = Navigator.getIntervals(startAirfield, finishAirfield, 100.0);
                Map<Location, WeatherData> weatherMap = new HashMap<>();
                for(Location location : waypoints){
                    weatherMap.put(location,weatherService.getWeatherData(location));
                }
                CruiseResponse response = new CruiseResponse(armamentList, aircraft, startAirfield, finishAirfield, travelTime, distance, weatherMap);
                return ResponseEntity.ok(response);
            }
        }
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
