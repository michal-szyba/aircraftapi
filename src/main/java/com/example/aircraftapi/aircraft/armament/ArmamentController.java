package com.example.aircraftapi.aircraft.armament;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ArmamentController {
    private final ArmamentRepository armamentRepository;

    public ArmamentController(ArmamentRepository armamentRepository) {
        this.armamentRepository = armamentRepository;
    }
    @GetMapping("/armament/{id}")
    public ResponseEntity<Armament> getOne(@PathVariable Long id){
        Optional<Armament> optionalArmament = armamentRepository.findById(id);
        if(optionalArmament.isPresent()){
            Armament armament = optionalArmament.get();
            return ResponseEntity.ok(armament);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @PostMapping("/armament/add")
    public ResponseEntity<Armament> add(@RequestBody Armament armament){
        Armament addedArmament = armamentRepository.save(armament);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedArmament);
    }
    @PostMapping("/armament/addlist")
    public ResponseEntity<List<Armament>> add(@RequestBody List<Armament> armaments){
        armamentRepository.saveAll(armaments);
        return ResponseEntity.status(HttpStatus.CREATED).body(armaments);
    }
    @DeleteMapping("/armament/remove/{id}")
    public ResponseEntity<?> remove(@PathVariable Long id){
        Optional<Armament> optionalArmament = armamentRepository.findById(id);
        if(optionalArmament.isPresent()){
            Armament armament = optionalArmament.get();
            armamentRepository.delete(armament);
            return ResponseEntity.ok(String.format("removed armament from database: %s id: %d", armament.getName(), armament.getId()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("armament not found");
        }
    }
    @GetMapping("/armament/all")
    public ResponseEntity<List<Armament>> getAll(){
        List<Armament> armamentList = armamentRepository.findAll();
        return ResponseEntity.ok(armamentList);
    }
}
