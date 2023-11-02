package com.example.aircraftapi.airfield.cruise;

import com.example.aircraftapi.aircraft.Aircraft;
import com.example.aircraftapi.aircraft.AircraftRepository;
import com.example.aircraftapi.aircraft.armament.Armament;
import com.example.aircraftapi.aircraft.armament.ArmamentRepository;
import com.example.aircraftapi.airfield.Airfield;
import com.example.aircraftapi.airfield.AirfieldRepository;
import com.example.aircraftapi.navigator.Navigator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class CruiseController {
    private final AircraftRepository aircraftRepository;
    private final AirfieldRepository airfieldRepository;
    private final ArmamentRepository armamentRepository;

    public CruiseController(AircraftRepository aircraftRepository, AirfieldRepository airfieldRepository, ArmamentRepository armamentRepository) {
        this.aircraftRepository = aircraftRepository;
        this.airfieldRepository = airfieldRepository;
        this.armamentRepository = armamentRepository;

    }

    @GetMapping("/cruise")
    public String cruise(@RequestBody CruiseRequest cruiseRequest) {
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
            return "not possible: out of range";
        } else {
            Double armamentWeight = 0.0;
            Double cruiseTime = 0.0;
            for(Long key : armamentMap.keySet()){
                Armament armament = armamentRepository.findById(key).get();
                armament.setQuantity(armamentMap.get(key));
                armamentWeight += armament.getWeight() * armament.getQuantity();
                cruiseTime += 0.25; //assuming each armament (no matter its quantity) would take around 15 minutes to load
                armamentList.add(armament);
            }
            if(armamentWeight > aircraft.getMaxTakeoffWeight() + armamentWeight){
                return "not possible: overloaded";
            } else {
                Double travelTime = distance / aircraft.getCruiseSpeed();
                cruiseTime += travelTime + 0.75; //additional 45 minutes for taxi, climbing, landing etc. This is not a precise calculation.
                return "estimated cruise time: " + cruiseTime;
            }
        }
    }
}
