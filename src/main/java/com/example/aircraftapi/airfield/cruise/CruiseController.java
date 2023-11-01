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
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
public class CruiseController {
    private final AircraftRepository aircraftRepository;
    private final AirfieldRepository airfieldRepository;
    private final ArmamentRepository armamentRepository;
    private final Navigator navigator;

    public CruiseController(AircraftRepository aircraftRepository, AirfieldRepository airfieldRepository, ArmamentRepository armamentRepository) {
        this.aircraftRepository = aircraftRepository;
        this.airfieldRepository = airfieldRepository;
        this.armamentRepository = armamentRepository;
        this.navigator = new Navigator();
    }

    @GetMapping("/cruise")
    public String cruise(@RequestBody Long aircraftId,
                         @RequestBody Long startAirfield,
                         @RequestBody Long finishAirfield,
                         @RequestBody(required = false) Map<Long, Long> armamentMap) {
        Aircraft aircraft = aircraftRepository.getReferenceById(aircraftId);
        Double cruiseTime = 0.0;
        List<Armament> armamentList = new ArrayList<>();
        Iterator<Map.Entry<Long, Long>> iterator = armamentMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Armament armament = armamentRepository.getReferenceById(iterator.next().getKey());
            armament.setQuantity(iterator.next().getValue());
            cruiseTime += armament.getQuantity() * 0.25; //each armament takes 15 minutes to load on the aircraft
            armamentList.add(armament);
        }
        Double armedWeight = 0.0;
        for (Armament arm : armamentList) {
            armedWeight += arm.getWeight() * arm.getQuantity();
        }
        if (armedWeight > aircraft.getMaxTakeoffWeight() - aircraft.getEmptyWeight()) { //ignoring fuel weight for simplicity of calculations
            return "not possible: overloaded";
        }
        Double distance = navigator.calculateDistance(airfieldRepository.getReferenceById(startAirfield),
                                                      airfieldRepository.getReferenceById(finishAirfield));
        if(distance > aircraft.getMaxRange()){
            return "not possible: out of range";
        }

        cruiseTime += distance / aircraft.getCruiseSpeed() + 0.75; //45 minutes for climbing, accelerating, landing etc.
        return "estimated time: " + cruiseTime;

    }
}
