/*
package com.example.sampleproject.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sampleproject.adapter.DashboardBasicAdapter;
import com.example.sampleproject.databinding.FragmentDashboardBasicBinding;
import com.example.sampleproject.repository.WeatherAssetRepository;
import com.example.sampleproject.viewmodel.DashboardBasicViewModel;

public class DashboardBasicFragment extends Fragment {
    private FragmentDashboardBasicBinding binding;
    private DashboardBasicAdapter dashboardRecViewAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBasicBinding.inflate(inflater, container, false);
        initiate();
        setupRecyclerView(binding);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initiate() {
        WeatherAssetRepository weatherAssetRepository = new WeatherAssetRepository(getContext());
        DashboardBasicViewModel dashboardViewModel = new DashboardBasicViewModel(weatherAssetRepository);
        dashboardRecViewAdapter = new DashboardBasicAdapter(getContext());
        dashboardViewModel.getData().observe(getViewLifecycleOwner(), weatherAssets -> {
            if(weatherAssets != null) {
                dashboardRecViewAdapter.setWeatherAssets(weatherAssets);
            }
        });
    }

    private void setupRecyclerView(FragmentDashboardBasicBinding binding) {
        RecyclerView recyclerView = binding.dashboardBasicRecView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(dashboardRecViewAdapter);

        recyclerView.setHasFixedSize(true);
    }
}*/
