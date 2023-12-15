package com.example.sampleproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.sampleproject.model.WeatherAssetModel;
import com.example.sampleproject.repository.WeatherAssetRepository;

import java.util.List;

public class DashboardBasicViewModel extends ViewModel {
    private  LiveData<List<WeatherAssetModel>> weatherAssets;

    public DashboardBasicViewModel(WeatherAssetRepository weatherAssetRepository) {
        weatherAssets = weatherAssetRepository.getAsset();
    }

    public LiveData<List<WeatherAssetModel>> getData() {
        return weatherAssets;
    }
}