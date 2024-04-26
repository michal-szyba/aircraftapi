package com.example.aircraftapi.location.airfield.cruise;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CruiseRequest {
    private Long aircraftId;
    private Long startAirfieldId;
    private Long finishAirfieldId;
    private Map<Long, Long> armamentMap; //first value is armament id, second value is quantity
    private LocalDateTime startTime;

}
