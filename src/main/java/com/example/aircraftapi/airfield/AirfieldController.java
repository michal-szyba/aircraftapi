package com.example.aircraftapi.airfield;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AirfieldController {
    private final AirfieldRepository airfieldRepository;

    public AirfieldController(AirfieldRepository airfieldRepository) {
        this.airfieldRepository = airfieldRepository;
    }
    @GetMapping("/airfield/{id}")
    public ResponseEntity<?> details(@PathVariable Long id){
        Optional<Airfield> optionalAirfield = airfieldRepository.findById(id);
        if(optionalAirfield.isPresent()){
            Airfield airfield = optionalAirfield.get();
            return ResponseEntity.ok(airfield);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("airfield not found");
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
}
