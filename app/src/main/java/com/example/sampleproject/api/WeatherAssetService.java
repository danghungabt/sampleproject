package com.example.sampleproject.api;

import com.example.sampleproject.model.LightAssetModel;
import com.example.sampleproject.model.WeatherAssetModel;
import com.example.sampleproject.model.WeatherAssetRequestBodyModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WeatherAssetService {
    @POST("api/master/asset/query")
    Call<List<WeatherAssetModel>> getAsset(@Body WeatherAssetRequestBodyModel types);

    @GET("api/master/asset/{assetID}")
    Call<WeatherAssetModel> getAssetById(@Path("assetID") String assetID);

    @GET("api/master/asset/{assetID}")
    Call<LightAssetModel> getLightAssetById(@Path("assetID") String assetID);
}
