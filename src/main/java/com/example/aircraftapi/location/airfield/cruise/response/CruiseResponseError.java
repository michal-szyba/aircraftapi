package com.example.aircraftapi.location.airfield.cruise.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class CruiseResponseError extends CruiseResponse{
    private String message;
    private HttpStatus status;


}
