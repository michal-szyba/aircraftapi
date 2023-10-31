package com.example.aircraftapi.aircraft;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AircraftController {

    private final AircraftRepository aircraftRepository;

    public AircraftController(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    @GetMapping("/aircraft/{id}")
    public ResponseEntity<?> details(@PathVariable Long id){
        Optional<Aircraft> optionalAircraft = aircraftRepository.findById(id);
        if(optionalAircraft.isPresent()){
            Aircraft aircraft = optionalAircraft.get();
            return ResponseEntity.ok(aircraft);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("aircraft not found");
        }
    }
    @PostMapping("/aircraft/add")
    public ResponseEntity<Aircraft> add(@RequestBody Aircraft aircraft){
        Aircraft addedAircraft = aircraftRepository.save(aircraft);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedAircraft);
    }
    @PostMapping("/aircraft/addlist")
    public ResponseEntity<List<Aircraft>> add(@RequestBody List<Aircraft> aircraftList){
        aircraftRepository.saveAll(aircraftList);
        return ResponseEntity.status(HttpStatus.CREATED).body(aircraftList);
    }
    @DeleteMapping("/aircraft/remove/{id}")
    public ResponseEntity<?> remove(@PathVariable Long id){
        Optional<Aircraft> optionalAircraft = aircraftRepository.findById(id);
        if(optionalAircraft.isPresent()){
            Aircraft aircraft = optionalAircraft.get();
            aircraftRepository.delete(aircraft);
            return ResponseEntity.ok(String.format("removed aircraft from database: %s id: %d", aircraft.getName(), aircraft.getId()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("aircraft not found");
        }
    }
    @GetMapping("/aircraft/all")
    public ResponseEntity<List<Aircraft>> getAll(){
        List<Aircraft> aircraftList = aircraftRepository.findAll();
        return ResponseEntity.ok(aircraftList);
    }


}
