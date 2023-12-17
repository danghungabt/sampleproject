package com.example.sampleproject.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleproject.R;
import com.example.sampleproject.adapter.CustomSpinnerAdapter;
import com.example.sampleproject.adapter.DashBoardPollutantAdapter;
import com.example.sampleproject.adapter.DashBoardWeatherAdapter;
import com.example.sampleproject.model.WeatherAssetModel;
import com.example.sampleproject.repository.WeatherAssetRepository;
import com.example.sampleproject.viewmodel.DashboardBasicViewModel;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FeatureFragment extends Fragment {
    private Spinner spinner;
    private RecyclerView weatherRecView, pollutantRecView;
    private  View view;

    private WeatherAssetRepository weatherAssetRepository;
    private DashboardBasicViewModel dashboardViewModel;
    private CustomSpinnerAdapter customSpinnerAdapter;

    private DashBoardWeatherAdapter weatherRecViewAdapter;
    private DashBoardPollutantAdapter pollutantRecViewAdapter;
    private String assetId;
    private List<WeatherAssetModel> weatherAssetModelList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_feature, container, false);
        initiate(view);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                WeatherAssetModel weatherAssetModel = weatherAssetModelList.get(position);
                // Xử lý khi một mục được chọn

                // Sau khi xử lý, cập nhật UI của RecyclerView dựa trên item được chọn
                updateRecyclerView(weatherAssetModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có mục nào được chọn
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void updateRecyclerView(WeatherAssetModel weatherAssetModel) {
        weatherRecViewAdapter.setWeatherAttributes(getWeatherList(weatherAssetModel.attributes));
        pollutantRecViewAdapter.setPollutants(getPollutantList(weatherAssetModel.attributes));
        weatherRecViewAdapter.notifyDataSetChanged();
        pollutantRecViewAdapter.notifyDataSetChanged();
    }

    private void initiate(View view) {

        spinner = view.findViewById(R.id.spinner);
        weatherRecView = view.findViewById(R.id.dashboard_detail_weather_rec_view);
        pollutantRecView = view.findViewById(R.id.dashboard_detail_pollutant_rec_view);

        weatherAssetRepository = new  WeatherAssetRepository(getContext());
        dashboardViewModel = new DashboardBasicViewModel(weatherAssetRepository);
        dashboardViewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<WeatherAssetModel>>() {
            @Override
            public void onChanged(List<WeatherAssetModel> weatherAssets) {
                if(weatherAssets != null) {
                    customSpinnerAdapter = new CustomSpinnerAdapter(getContext(), R.layout.item_spinner_custom, weatherAssets);
                    spinner.setAdapter(customSpinnerAdapter);
                    weatherAssetModelList = weatherAssets;
                    weatherRecViewAdapter = new DashBoardWeatherAdapter(getContext());
                    pollutantRecViewAdapter = new DashBoardPollutantAdapter(getContext());
                    weatherRecViewAdapter.setWeatherAttributes(getWeatherList(weatherAssetModelList.get(0).attributes));
                    weatherRecViewAdapter.notifyDataSetChanged();
                    pollutantRecViewAdapter.setPollutants(getPollutantList(weatherAssetModelList.get(0).attributes));
                    pollutantRecViewAdapter.notifyDataSetChanged();

                    setupWeatherRecyclerView();
                }
            }
        });
    }

    private void setupWeatherRecyclerView() {
        weatherRecView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        weatherRecView.setFocusable(false);
        weatherRecView.setNestedScrollingEnabled(false);
        weatherRecView.setAdapter(weatherRecViewAdapter);
        weatherRecView.setHasFixedSize(true);

        pollutantRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        pollutantRecView.setFocusable(false);
        pollutantRecView.setNestedScrollingEnabled(false);
        pollutantRecView.setAdapter(pollutantRecViewAdapter);
    }

    private List<WeatherAssetModel.Attributes.Measurement> getWeatherList(WeatherAssetModel.Attributes attributes) {
        // Loop through each field using reflection
        List<WeatherAssetModel.Attributes.Measurement> values = new ArrayList<>();
        for (Field field : WeatherAssetModel.Attributes.class.getDeclaredFields()) {
            try {
                Object value = field.get(attributes);
                if (value instanceof WeatherAssetModel.Attributes.Measurement) {
                    values.add((WeatherAssetModel.Attributes.Measurement) value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return values;
    }

    private List<WeatherAssetModel.Attributes.Pollutant> getPollutantList(WeatherAssetModel.Attributes attributes) {
        // Loop through each field using reflection
        List<WeatherAssetModel.Attributes.Pollutant> values = new ArrayList<>();
        for (Field field : WeatherAssetModel.Attributes.class.getDeclaredFields()) {
            try {
                Object value = field.get(attributes);
                if (value instanceof WeatherAssetModel.Attributes.Pollutant) {
                    values.add((WeatherAssetModel.Attributes.Pollutant) value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return values;
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date currentTime = new Date();
        return sdf.format(currentTime);
    }

}

