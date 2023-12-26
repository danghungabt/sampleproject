package com.example.sampleproject.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FeatureFragment extends Fragment {
    private Spinner spinner;
    private RecyclerView weatherRecView, pollutantRecView;
    private LinearLayout linearLayoutContent;
    private ProgressBar progressBar;

    private CustomSpinnerAdapter customSpinnerAdapter;

    private DashBoardWeatherAdapter weatherRecViewAdapter;
    private DashBoardPollutantAdapter pollutantRecViewAdapter;
    private List<WeatherAssetModel> weatherAssetModelList;
    private TextView tvValueAQI, tvState, tvValuePM25, tvTemperatureValue, tvWindFeedValue, tvHumidityValue;
    private ImageView imvState;
    private LinearLayout linearLayoutState;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feature, container, false);
        initiate(view);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                WeatherAssetModel weatherAssetModel = weatherAssetModelList.get(position);
                // Xử lý khi một mục được chọn

                // Sau khi xử lý, cập nhật UI của RecyclerView dựa trên item được chọn
                updateRecyclerView(weatherAssetModel);
                loadDataGeneral(weatherAssetModel);
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

    @SuppressLint("NotifyDataSetChanged")
    private void updateRecyclerView(WeatherAssetModel weatherAssetModel) {
        weatherRecViewAdapter.setWeatherAttributes(getWeatherList(weatherAssetModel.attributes));
        pollutantRecViewAdapter.setPollutants(getPollutantList(weatherAssetModel.attributes));
        weatherRecViewAdapter.notifyDataSetChanged();
        pollutantRecViewAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initiate(View view) {

        spinner = view.findViewById(R.id.spinner);
        weatherRecView = view.findViewById(R.id.dashboard_detail_weather_rec_view);
        pollutantRecView = view.findViewById(R.id.dashboard_detail_pollutant_rec_view);
        progressBar = view.findViewById(R.id.loadingProgressBar);
        linearLayoutContent = view.findViewById(R.id.linearLayoutContent);
        tvState = view.findViewById(R.id.tvState);
        tvValueAQI = view.findViewById(R.id.tvValueAQI);
        tvHumidityValue = view.findViewById(R.id.tvHumidityValue);
        tvTemperatureValue = view.findViewById(R.id.tvTemperatureValue);
        tvWindFeedValue = view.findViewById(R.id.tvWindFeedValue);
        tvValuePM25 = view.findViewById(R.id.tvValuePM25);
        imvState = view.findViewById(R.id.imvState);
        linearLayoutState = view.findViewById(R.id.linearLayoutState);

        WeatherAssetRepository weatherAssetRepository = new WeatherAssetRepository(getContext());
        DashboardBasicViewModel dashboardViewModel = new DashboardBasicViewModel(weatherAssetRepository);
        dashboardViewModel.getData().observe(getViewLifecycleOwner(), weatherAssets -> {
            if(weatherAssets != null) {

                customSpinnerAdapter = new CustomSpinnerAdapter(Objects.requireNonNull(getContext()), R.layout.item_spinner_custom, weatherAssets);
                spinner.setVisibility(View.VISIBLE);
                spinner.setAdapter(customSpinnerAdapter);
                weatherAssetModelList = weatherAssets;
                weatherRecViewAdapter = new DashBoardWeatherAdapter(getContext());
                pollutantRecViewAdapter = new DashBoardPollutantAdapter(getContext());
                weatherRecViewAdapter.setWeatherAttributes(getWeatherList(weatherAssetModelList.get(0).attributes));
                weatherRecViewAdapter.notifyDataSetChanged();
                pollutantRecViewAdapter.setPollutants(getPollutantList(weatherAssetModelList.get(0).attributes));
                pollutantRecViewAdapter.notifyDataSetChanged();

                setupWeatherRecyclerView();
                loadDataGeneral(weatherAssets.get(0));

                progressBar.setVisibility(View.GONE);
                linearLayoutContent.setVisibility(View.VISIBLE);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void loadDataGeneral(WeatherAssetModel weatherAssetModel) {

        boolean isAQIGood = false, isPM25Good = false, isDefine = true;
        int textColor;

        if(weatherAssetModel.attributes.aqi != null && weatherAssetModel.attributes.aqi.value !=null){
            tvValueAQI.setText(weatherAssetModel.attributes.aqi.value.toString());
            if(Double.parseDouble(weatherAssetModel.attributes.aqi.value.toString()) < 50){
                isAQIGood = true;
            }
        }else {
            isDefine = false;
            tvValueAQI.setText("?");
        }

        if(weatherAssetModel.attributes.pm25 != null && weatherAssetModel.attributes.pm25.value !=null){
            tvValuePM25.setText(weatherAssetModel.attributes.pm25.value.toString());
            if(Double.parseDouble(weatherAssetModel.attributes.pm25.value.toString()) < 50){
                isPM25Good = true;
            }
        }else {
            isDefine = false;
            tvValuePM25.setText("?");
        }

        if(weatherAssetModel.attributes.temperature != null && weatherAssetModel.attributes.temperature.value !=null){
            tvTemperatureValue.setText(weatherAssetModel.attributes.temperature.value.toString());
        }else {
            tvTemperatureValue.setText("?");
        }

        if(weatherAssetModel.attributes.windSpeed != null && weatherAssetModel.attributes.windSpeed.value !=null){
            tvWindFeedValue.setText(weatherAssetModel.attributes.windSpeed.value.toString());
        }else {
            tvWindFeedValue.setText("?");
        }

        if(weatherAssetModel.attributes.humidity != null && weatherAssetModel.attributes.humidity.value !=null){
            tvHumidityValue.setText(weatherAssetModel.attributes.humidity.value.toString());
        }else {
            tvHumidityValue.setText("?");
        }

        if(isDefine) {
            if (isAQIGood && isPM25Good) {
                tvState.setText("GOOD");
                textColor = ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.primaryVariantColor);
                tvValueAQI.setTextColor(textColor);
                tvState.setTextColor(textColor);
                imvState.setImageResource(R.drawable.good_icon);
                linearLayoutState.setBackgroundResource(R.color.goodColor);
            } else {
                tvState.setText("BAD");
                textColor = ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.badIconColor);
                tvValueAQI.setTextColor(textColor);
                tvState.setTextColor(textColor);
                imvState.setImageResource(R.drawable.bad_icon);
                linearLayoutState.setBackgroundResource(R.color.badColor);
            }
        }else {
            tvState.setText("UNDEFINE");
            textColor = ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.badIconColor);
            tvValueAQI.setTextColor(textColor);
            tvState.setTextColor(textColor);
            imvState.setImageResource(R.drawable.bad_icon);
            linearLayoutState.setBackgroundResource(R.color.badColor);
        }

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
}

