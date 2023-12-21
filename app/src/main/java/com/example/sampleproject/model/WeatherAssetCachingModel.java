package com.example.sampleproject.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherAssetCachingModel {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("attributes")
    public Attributes attributes;

    public static class Attributes {
        @SerializedName("sunIrradiance")
        public Measurement sunIrradiance;
        @SerializedName("rainfall")
        public Measurement rainfall;
        @SerializedName("uVIndex")
        public Measurement uVIndex;
        @SerializedName("sunAzimuth")
        public Measurement sunAzimuth;
        @SerializedName("sunZenith")
        public Measurement sunZenith;
        @SerializedName("temperature")
        public Measurement temperature;
        @SerializedName("humidity")
        public Measurement humidity;
        @SerializedName("windDirection")
        public Measurement windDirection;
        @SerializedName("windSpeed")
        public Measurement windSpeed;
        @SerializedName("sunAltitude")
        public Measurement sunAltitude;

        @SerializedName("PM25")
        public Pollutant pm25;
        @SerializedName("PM10")
        public Pollutant pm10;
        @SerializedName("CO2")
        public Pollutant co2;
        @SerializedName("AQI_Predict")
        public Pollutant aqiPredict;
        @SerializedName("AQI")
        public Pollutant aqi;
        @SerializedName("SO2")
        public Pollutant so2;
        @SerializedName("NO2")
        public Pollutant no2;
        @SerializedName("CO2_average")
        public Pollutant co2Average;

        public static class Measurement {
            @SerializedName("value")
            public Object value;
            @SerializedName("name")
            public String name;
        }
        public static class Pollutant {
            @SerializedName("value")
            public Object value;
            @SerializedName("name")
            public String name;
        }
    }
}
