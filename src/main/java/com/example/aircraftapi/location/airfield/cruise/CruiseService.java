package com.example.aircraftapi.location.airfield.cruise;

import com.example.aircraftapi.aircraft.Aircraft;
import com.example.aircraftapi.aircraft.AircraftRepository;
import com.example.aircraftapi.aircraft.armament.Armament;
import com.example.aircraftapi.aircraft.armament.ArmamentRepository;
import com.example.aircraftapi.error.exceptions.BadCruiseRequestException;
import com.example.aircraftapi.location.Location;
import com.example.aircraftapi.location.airfield.Airfield;
import com.example.aircraftapi.location.airfield.AirfieldRepository;
import com.example.aircraftapi.location.airfield.cruise.response.CruiseResponse;
import com.example.aircraftapi.location.airfield.cruise.response.CruiseResponseError;
import com.example.aircraftapi.location.airfield.cruise.response.CruiseResponseSuccess;
import com.example.aircraftapi.navigator.Navigator;
import com.example.aircraftapi.weather.WeatherData;
import com.example.aircraftapi.weather.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.aircraftapi.location.airfield.cruise.response.CruiseResponseSuccess.ArmamentDTO;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class CruiseService {
    private  final AircraftRepository aircraftRepository;
    private final AirfieldRepository airfieldRepository;
    private final ArmamentRepository armamentRepository;
    private final WeatherService weatherService;
    public CruiseService(AircraftRepository aircraftRepository, AirfieldRepository airfieldRepository, ArmamentRepository armamentRepository, WeatherService weatherService, Navigator navigator){
        this.aircraftRepository = aircraftRepository;
        this.airfieldRepository = airfieldRepository;
        this.armamentRepository = armamentRepository;
        this.weatherService = weatherService;
    }
    public ResponseEntity<CruiseResponse> calculateCruise(CruiseRequest cruiseRequest){
        Aircraft aircraft = aircraftRepository.findById(cruiseRequest.getAircraftId()).get();
        Airfield startAirfield = airfieldRepository.findById(cruiseRequest.getStartAirfieldId()).get();
        Airfield finishAirfield = airfieldRepository.findById(cruiseRequest.getFinishAirfieldId()).get();

        try{
            double distance = Navigator.calculateDistance(startAirfield, finishAirfield);
            if(distance > aircraft.getMaxRange()){
                throw new BadCruiseRequestException("out of range");
            } else {
                calculateArmamentWeight(cruiseRequest.getArmamentMap())
                        .filter(armamentWeight -> armamentWeight <= aircraft.getMaxTakeoffWeight())
                        .orElseThrow(() -> new BadCruiseRequestException("aircraft overloaded"));
                long time = (long) (distance / aircraft.getCruiseSpeed());
                CruiseResponseSuccess responseSuccess = new CruiseResponseSuccess(
                        mappingArmamentDTO(cruiseRequest),
                        aircraft,
                        startAirfield,
                        finishAirfield,
                        time,
                        distance,
                        weatherDataParse(getWeatherForCruise(startAirfield, finishAirfield)),
                        cruiseRequest.getStartTime(),
                        cruiseRequest.getStartTime().plusMinutes(time));
                return ResponseEntity.ok(responseSuccess);
            }
        } catch (BadCruiseRequestException e){
            CruiseResponseError responseError = new CruiseResponseError(e.getMessage(), HttpStatus.BAD_REQUEST);
            return ResponseEntity.badRequest().body(responseError);
        }
    }

    private Optional<Double> calculateArmamentWeight(Map<Long, Long> armamentMap){
        double weight = 0.0;
        for(Long key : armamentMap.keySet()) {
            if (armamentRepository.findById(key).isPresent()) {
                weight += armamentRepository.findById(key).get().getWeight() * armamentMap.get(key);
            } else {
                return Optional.empty();
            }
        }
        return Optional.of(weight);
    }
    private List<ArmamentDTO> mappingArmamentDTO(CruiseRequest request){
        List<ArmamentDTO> armamentDTOList = new ArrayList<>();
        Map<Long, Long> armamentMap = request.getArmamentMap();
        armamentMap.forEach((armamentId, quantity) -> {
            Optional<Armament> armamentOptional = armamentRepository.findById(armamentId);
            armamentOptional.ifPresent(armament -> {
                String name = armament.getName();
                armamentDTOList.add(new CruiseResponseSuccess.ArmamentDTO(name, quantity));
            });
        });
        return armamentDTOList;
    }
    private Map<Location, WeatherData> getWeatherForCruise(Location start, Location finish){
        List<Location> waypoints = Navigator.getIntervals(start, finish, 100.0);
        Map<Location, WeatherData> weatherMap = new HashMap<>();
        for(Location waypoint : waypoints){
            weatherMap.put(waypoint, weatherService.getWeatherData(waypoint));
        }
        return weatherMap;

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
