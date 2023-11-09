package com.example.aircraftapi.navigator;

import com.example.aircraftapi.location.airfield.Airfield;
import com.example.aircraftapi.location.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.lang.Math.*;

public class Navigator {
    private static final double EARTH_RADIUS = 6371.0;


    public static Double parseCoordinates(String coordinate){
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

    public static List<Location> getIntervals(Airfield startingPoint,
                                              Airfield finishingPoint,
                                              Double segmentLength){
        List<Location> waypoints = new ArrayList<>();
        Integer intervals = (int) Math.ceil(calculateDistance(startingPoint, finishingPoint) / segmentLength); //the number of intervals must be an int, because it would not make sense to have for example 3.4 waypoints between starting and finishing point

        Double startingLatitude = parseCoordinates(startingPoint.getLatitude());
        Double startingLongitude = parseCoordinates(startingPoint.getLongitude());

        Double finishingLatitude = parseCoordinates(finishingPoint.getLatitude());
        Double finishingLongitude = parseCoordinates(finishingPoint.getLongitude());

        for(int i = 0; i < intervals; i++){
            Double fraction = (double) i / intervals;
            Double latitudeValue = startingLatitude + fraction * (finishingLatitude - startingLatitude);
            String latitudeDirection = (latitudeValue < 0) ? "S" : "N";

            Double longitudeValue = startingLongitude + fraction * (finishingLongitude - startingLongitude);
            String longitudeDirection = (longitudeValue < 0) ? "W" : "E";

            waypoints.add(new Location(String.format(Locale.ENGLISH, "%.8f%s", Math.abs(latitudeValue), latitudeDirection),
                                          String.format(Locale.ENGLISH, "%.8f%s", Math.abs(longitudeValue), longitudeDirection)));
        }
        System.out.println(waypoints.size());
        return waypoints;

    }
}
