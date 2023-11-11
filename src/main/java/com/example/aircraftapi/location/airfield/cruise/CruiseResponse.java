package com.example.aircraftapi.location.airfield.cruise;

import com.example.aircraftapi.aircraft.Aircraft;
import com.example.aircraftapi.aircraft.armament.Armament;
import com.example.aircraftapi.location.Location;
import com.example.aircraftapi.location.airfield.Airfield;
import com.example.aircraftapi.weather.WeatherData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;
import java.util.*;

@Getter
@Setter
public class CruiseResponse {
    private AircraftDTO aircraft;
    private AirfieldDTO startingAirfield;
    private AirfieldDTO finishingAirfield;
    private Double time;
    private Double distance;
    private Long cruiseSpeed;
    private Map<String,WeatherData> weatherMap;
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    private static class AircraftDTO {
        private String name;
        private List<ArmamentDTO> armamentList;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    private static class AirfieldDTO {
        private String name;
        private String latitude;
        private String longitude;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    private static class ArmamentDTO{
        private String name;
        private Long quantity;
    }
    public CruiseResponse(List<Armament> armamentList, Aircraft aircraft, Airfield startAirfield, Airfield finishAirfield, Double time, Double distance, Map<Location, WeatherData> weatherMap){
        this.aircraft = aircraftToDTO(aircraft, armamentList);
        this.startingAirfield = airfieldToDTO(startAirfield);
        this.finishingAirfield = airfieldToDTO(finishAirfield);
        this.time = time;
        this.distance = distance;
        this.cruiseSpeed = aircraft.getCruiseSpeed();
        this.weatherMap = weatherDataParse(weatherMap);
    }
    private AircraftDTO aircraftToDTO(Aircraft aircraft, List<Armament> armamentList){
        AircraftDTO aircraftDTO = new AircraftDTO();
        aircraftDTO.setName(aircraft.getName());
        aircraftDTO.setArmamentList(armamentToDTO(armamentList));
        return aircraftDTO;
    }
    private AirfieldDTO airfieldToDTO(Airfield airfield){
        AirfieldDTO airfieldDTO = new AirfieldDTO();
        airfieldDTO.setName(airfield.getName());
        airfieldDTO.setLatitude(airfield.getLatitude());
        airfieldDTO.setLongitude(airfield.getLongitude());
        return airfieldDTO;
    }
    private List<ArmamentDTO> armamentToDTO(List<Armament> armamentList){
        List<ArmamentDTO> armamentDTOList = new ArrayList<>();
        for(Armament armament : armamentList){
            armamentDTOList.add(new ArmamentDTO(armament.getName(), armament.getQuantity()));
        }
        return armamentDTOList;
    }
    private Map<String, WeatherData> weatherDataParse(Map<Location, WeatherData> inputWeatherMap){
        Set<Location> waypoints = inputWeatherMap.keySet();
        Map<String, WeatherData> outputWeatherMap = new HashMap<>();
        for(Location location : waypoints){
            outputWeatherMap.put(location.getLatitude() + "," + location.getLongitude(), inputWeatherMap.get(location));
        }
        return outputWeatherMap;
    }
}


