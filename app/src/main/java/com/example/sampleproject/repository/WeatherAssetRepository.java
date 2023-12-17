package com.example.sampleproject.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sampleproject.api.ApiClient;
import com.example.sampleproject.api.WeatherAssetService;
import com.example.sampleproject.model.LightAssetModel;
import com.example.sampleproject.model.WeatherAssetModel;
import com.example.sampleproject.model.WeatherAssetRequestBodyModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherAssetRepository {
    private WeatherAssetService assetService;
    public WeatherAssetRepository(Context context) {
//        assetService = ClientAPI.getClient().create(WeatherAssetService.class);
        assetService = ApiClient.getClient(context).create(WeatherAssetService.class);
    }

    //List of Weather Asset
    String[] types = {"WeatherAsset"};
    WeatherAssetRequestBodyModel requestBody = new WeatherAssetRequestBodyModel(types);

    public LiveData<List<WeatherAssetModel>> getAsset() {
        MutableLiveData<List<WeatherAssetModel>> data = new MutableLiveData<>();
        Call<List<WeatherAssetModel>> call = assetService.getAsset(requestBody);
        call.enqueue(new Callback<List<WeatherAssetModel>>() {
            @Override
            public void onResponse(Call<List<WeatherAssetModel>> call, Response<List<WeatherAssetModel>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else  {
                    Log.e("API CALL", "Request failed with code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<WeatherAssetModel>> call, Throwable t) {
                // Handle network error
                Log.e("API CALL", "Request failed with code: " +  t.getMessage());
            }
        });
        return data;
    }

    //Weather Asset by Id
    public LiveData<WeatherAssetModel> getAssetById(String assetId) {
        MutableLiveData<WeatherAssetModel> data = new MutableLiveData<>();
        Call<WeatherAssetModel> call = assetService.getAssetById(assetId);
        call.enqueue(new Callback<WeatherAssetModel>() {
            @Override
            public void onResponse(Call<WeatherAssetModel> call, Response<WeatherAssetModel> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else  {
                    Log.e("API CALL", "Request failed with code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<WeatherAssetModel> call, Throwable t) {
                // Handle network error
                Log.e("API CALL", "Request failed with code: " +  t.getMessage());
            }
        });
        return data;
    }

    //Light Asset by Id
    public LiveData<LightAssetModel> getLightAssetById(String assetId) {
        MutableLiveData<LightAssetModel> data = new MutableLiveData<>();
        Call<LightAssetModel> call = assetService.getLightAssetById(assetId);
        call.enqueue(new Callback<LightAssetModel>() {
            @Override
            public void onResponse(Call<LightAssetModel> call, Response<LightAssetModel> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else  {
                    Log.e("API CALL", "Request failed with code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<LightAssetModel> call, Throwable t) {
                // Handle network error
                Log.e("API CALL", "Request failed with code: " +  t.getMessage());
            }
        });
        return data;
    }
}
