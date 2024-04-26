package com.example.aircraftapi.location.airfield.cruise.response;

import com.example.aircraftapi.aircraft.Aircraft;
import com.example.aircraftapi.location.airfield.Airfield;

import com.example.aircraftapi.weather.WeatherData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class CruiseResponseSuccess extends CruiseResponse {
    private AircraftDTO aircraft;
    private AirfieldDTO startingAirfield;
    private AirfieldDTO finishingAirfield;
    private Long time;
    private Double distance;
    private Long cruiseSpeed;
    private Map<String,WeatherData> weatherMap;
    private LocalDateTime startTime;
    private LocalDateTime estimatedFinishTime;
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class AircraftDTO {
        private String name;
        private List<ArmamentDTO> armamentList;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class AirfieldDTO {
        private String name;
        private String latitude;
        private String longitude;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ArmamentDTO{
        private String name;
        private Long quantity;
    }



        public CruiseResponseSuccess(List<ArmamentDTO> armamentList,
                                 Aircraft aircraft, Airfield startAirfield,
                                 Airfield finishAirfield, Long time,
                                 Double distance, Map<String, WeatherData> weatherMap,
                                 LocalDateTime startTime, LocalDateTime estimatedFinishTime){
        this.aircraft = aircraftToDTO(aircraft, armamentList);
        this.startingAirfield = airfieldToDTO(startAirfield);
        this.finishingAirfield = airfieldToDTO(finishAirfield);
        this.time = time;
        this.distance = distance;
        this.cruiseSpeed = aircraft.getCruiseSpeed();
        this.weatherMap = weatherMap;
        this.startTime = startTime;
        this.estimatedFinishTime = estimatedFinishTime;
    }
    private AircraftDTO aircraftToDTO(Aircraft aircraft, List<ArmamentDTO> armamentList){
        AircraftDTO aircraftDTO = new AircraftDTO();
        aircraftDTO.setName(aircraft.getName());
        aircraftDTO.setArmamentList(armamentList);
        return aircraftDTO;
    }
    private AirfieldDTO airfieldToDTO(Airfield airfield){
        AirfieldDTO airfieldDTO = new AirfieldDTO();
        airfieldDTO.setName(airfield.getName());
        airfieldDTO.setLatitude(airfield.getLatitude());
        airfieldDTO.setLongitude(airfield.getLongitude());
        return airfieldDTO;
    }




}


