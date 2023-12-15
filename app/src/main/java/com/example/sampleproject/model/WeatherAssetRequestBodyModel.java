package com.example.sampleproject.model;

import com.google.gson.annotations.SerializedName;

public class WeatherAssetRequestBodyModel {
    @SerializedName("types")
    private String[] types;

    public WeatherAssetRequestBodyModel(String[] types) {
        this.types = types;
    }
}
