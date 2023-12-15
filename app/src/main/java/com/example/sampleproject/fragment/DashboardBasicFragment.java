package com.example.sampleproject.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sampleproject.adapter.DashboardBasicAdapter;
import com.example.sampleproject.databinding.FragmentDashboardBasicBinding;
import com.example.sampleproject.model.WeatherAssetModel;
import com.example.sampleproject.repository.WeatherAssetRepository;
import com.example.sampleproject.viewmodel.DashboardBasicViewModel;

import java.util.List;

public class DashboardBasicFragment extends Fragment {
    private FragmentDashboardBasicBinding binding;
    private WeatherAssetRepository weatherAssetRepository;
    private DashboardBasicViewModel dashboardViewModel;
    private DashboardBasicAdapter dashboardRecViewAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBasicBinding.inflate(inflater, container, false);
        initiate(binding);
        setupRecyclerView(binding);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initiate(FragmentDashboardBasicBinding binding) {
        weatherAssetRepository = new  WeatherAssetRepository(getContext());
        dashboardViewModel = new DashboardBasicViewModel(weatherAssetRepository);
        dashboardRecViewAdapter = new DashboardBasicAdapter(getContext());
        dashboardViewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<WeatherAssetModel>>() {
            @Override
            public void onChanged(List<WeatherAssetModel> weatherAssets) {
                if(weatherAssets != null) {
                    dashboardRecViewAdapter.setWeatherAssets(weatherAssets);
                }
            }
        });
    }

    private void setupRecyclerView(FragmentDashboardBasicBinding binding) {
        RecyclerView recyclerView = binding.dashboardBasicRecView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(dashboardRecViewAdapter);

        recyclerView.setHasFixedSize(true);
    }
}