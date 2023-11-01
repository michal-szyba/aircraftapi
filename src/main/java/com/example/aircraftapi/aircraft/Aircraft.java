package com.example.aircraftapi.aircraft;

import com.example.aircraftapi.aircraft.armament.Armament;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int firstFlight;
    private String type;
    @OneToMany
    private List<Armament> armament;
    private int maxCrew;
    private Float length;
    private Float wingspan;
    private Long emptyWeight;
    private Long maxTakeoffWeight;
    private Float twRatio;
    private Long maxSpeed;
    private Long cruiseSpeed;
    private String radar;
    private String rwr;
    private String additionalEquipment;
    private Long maxRange; //"range" is a reserved SQL keyword


}
