package com.example.aircraftapi.location.airfield;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.aircraftapi.navigator.Navigator.calculateDistance;

@RestController
public class AirfieldController {
    private final AirfieldRepository airfieldRepository;

    public AirfieldController(AirfieldRepository airfieldRepository) {
        this.airfieldRepository = airfieldRepository;
    }
    @GetMapping("/airfield/{id}")
    public ResponseEntity<Airfield> details(@PathVariable Long id){
        Optional<Airfield> optionalAirfield = airfieldRepository.findById(id);
        if(optionalAirfield.isPresent()){
            Airfield airfield = optionalAirfield.get();
            return ResponseEntity.ok(airfield);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/airfield/add")
    public ResponseEntity<Airfield> add(@RequestBody Airfield airfield){
        Airfield addedAirfield = airfieldRepository.save(airfield);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedAirfield);
    }
    @PostMapping("/airfield/addlist")
    public ResponseEntity<List<Airfield>> add(@RequestBody List<Airfield> airfields){
        List<Airfield> addedAirfields = airfieldRepository.saveAll(airfields);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedAirfields);
    }
    @GetMapping("/airfield/getdist/{id1}/{id2}")
    public ResponseEntity<String> getDistance(@PathVariable Long id1, @PathVariable Long id2){
        Optional<Airfield> startOptional = airfieldRepository.findById(id1);
        Optional<Airfield> finishOptional = airfieldRepository.findById(id2);
        if(startOptional.isEmpty() || finishOptional.isEmpty()){
            return ResponseEntity.badRequest().body("one or both airfields do not exist");
        } else {
            Airfield start = startOptional.get();
            Airfield finish = finishOptional.get();
            return ResponseEntity.ok(calculateDistance(start, finish).toString());
        }

    }
}
