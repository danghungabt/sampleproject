package com.example.sampleproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.sampleproject.model.WeatherAssetModel;
import com.example.sampleproject.repository.WeatherAssetRepository;

public class DashBoardDetailViewModel extends ViewModel {
    private String assetId;
    private LiveData<WeatherAssetModel> weatherAsset;

    public DashBoardDetailViewModel(String assetId, WeatherAssetRepository weatherAssetRepository) {
        this.assetId = assetId;
        weatherAsset = weatherAssetRepository.getAssetById(this.assetId);
    }

    public LiveData<WeatherAssetModel> getData() {
        return weatherAsset;
    }
}
