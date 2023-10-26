package com.example.aircraftapi.aircraft.armament;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Transactional
@Repository
public interface ArmamentRepository extends JpaRepository<Armament, Long> {
    Optional<Armament> findById(Long id);
}
