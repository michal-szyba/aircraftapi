package com.example.aircraftapi.location.airfield;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Transactional
@Repository
public interface AirfieldRepository extends JpaRepository<Airfield, Long> {
    Optional<Airfield> findById(Long id);
}
