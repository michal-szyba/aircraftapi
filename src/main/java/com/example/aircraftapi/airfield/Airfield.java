package com.example.aircraftapi.airfield;

import com.example.aircraftapi.airfield.coordinates.Coordinates;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Airfield {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Pattern(regexp = "([-+]?\\d+\\.\\d+)([NSEW])")
    private String longitude;
    @Pattern(regexp = "([-+]?\\d+\\.\\d+)([NS])")
    private String latitude;
    private String holder;
}
