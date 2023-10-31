package com.example.aircraftapi.navigator;

import com.example.aircraftapi.airfield.Airfield;
import com.example.aircraftapi.airfield.AirfieldRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.lang.Math.*;

public class Navigator {
    private static final double EARTH_RADIUS = 6371.0;


    private static Double parseCoordinates(String coordinate){
        Pattern pattern = Pattern.compile("([-+]?\\d+\\.\\d+)([NSEW])");
        Matcher matcher = pattern.matcher(coordinate);
        if (matcher.find()) {
            double value = Double.parseDouble(matcher.group(1));
            char direction = matcher.group(2).charAt(0);
            if (direction == 'S' || direction == 'W') {
                value = -value;
            }
            return value;
        } else {
            return null;
        }
    }
    //Output of calculateDistance() is in kilometers, because the EARTH_RADIUS is in kilometers.
    public static Double calculateDistance(Airfield startingPoint, Airfield finishingPoint){
        double latitude1 = parseCoordinates(startingPoint.getLatitude());
        double longitude1 = parseCoordinates(startingPoint.getLongitude());

        double latitude2 = parseCoordinates(finishingPoint.getLatitude());
        double longitude2 = parseCoordinates(finishingPoint.getLongitude());

        double dlon = longitude2 - longitude1;
        double dlat = latitude2 - latitude1;
        double a = pow(sin(toRadians(dlat) / 2), 2) + cos(toRadians(latitude1)) * cos(toRadians(latitude2)) * pow(sin(toRadians(dlon) / 2), 2);
        double c = 2 * atan2(sqrt(a), sqrt(1 - a));
        double distance = EARTH_RADIUS * c;

        return distance;
    }
}