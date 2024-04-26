package com.example.aircraftapi.error.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class BadCruiseRequestException extends RuntimeException{
    public BadCruiseRequestException(String message){
        super(message);
    }

}
