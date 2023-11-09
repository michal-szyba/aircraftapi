package com.example.aircraftapi.weather;


import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WeatherData {
    @SerializedName("current")
    private Current current;
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Current {
        private String time;
        private String temperature_2m;
        private String relative_humidity_2m;
        private String snowfall;
        private String rain;
        private String cloud_cover;
        private String wind_speed_10m;
        private String wind_direction_10m;
        private String wind_gusts_10m;

    }

}
