package com.example.sampleproject.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LightAssetModel {
    @SerializedName("id")
    public String id;
    @SerializedName("version")
    public float version;
    @SerializedName("createdOn")
    public long createdOn;
    @SerializedName("name")
    public String name;
    @SerializedName("type")
    public String type;
    @SerializedName("attributes")
    public Attributes attributes;

    public static class Attributes {
        @SerializedName("location")
        public Location location;
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
