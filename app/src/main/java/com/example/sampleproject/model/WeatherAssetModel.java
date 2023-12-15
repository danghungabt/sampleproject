package com.example.sampleproject.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherAssetModel {
    @SerializedName("id")
    public String id;
    @SerializedName("version")
    public float version;
    @SerializedName("createdOn")
    public long createdOn;
    @SerializedName("name")
    public String name;
    @SerializedName("accessPublicRead")
    public boolean accessPublicRead;
    @SerializedName("parentId")
    public String parentId;
    @SerializedName("realm")
    public String realm;
    @SerializedName("type")
    public String type;
    @SerializedName("path")
    public List<String> path;
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

        @SerializedName("location")
        public Location location;
        @SerializedName("place")
        public Place place;

        public static class Measurement {
            @SerializedName("type")
            public String type;
            @SerializedName("value")
            public Object value;
            @SerializedName("name")
            public String name;
            @SerializedName("timestamp")
            public long timestamp;
        }
        public static class Pollutant {
            @SerializedName("type")
            public String type;
            @SerializedName("value")
            public Object value;
            @SerializedName("name")
            public String name;
            @SerializedName("timestamp")
            public long timestamp;
        }
        public static class Place {
            @SerializedName("type")
            public String type;
            @SerializedName("value")
            public Object value;
            @SerializedName("name")
            public String name;
            @SerializedName("timestamp")
            public long timestamp;
        }
    }

    public static class Location {
        @SerializedName("type")
        public String type;
        @SerializedName("value")
        public GeoJsonPoint value;
        @SerializedName("name")
        public String name;
        @SerializedName("timestamp")
        public long timestamp;
    }

    public static class GeoJsonPoint {
        @SerializedName("coordinates")
        public List<Double> coordinates;
        @SerializedName("type")
        public String type;
    }

}
