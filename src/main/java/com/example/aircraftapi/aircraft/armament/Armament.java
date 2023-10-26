package com.example.aircraftapi.aircraft.armament;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Armament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private TypeEnum type;
    @Enumerated(EnumType.STRING)
    private TargetEnum target;
    @Enumerated(EnumType.STRING)
    private GuidanceEnum guidanceType;
    private Double weight;


}
