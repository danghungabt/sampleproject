package com.example.sampleproject.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleproject.R;
import com.example.sampleproject.adapter.DashBoardPollutantAdapter;
import com.example.sampleproject.adapter.DashBoardWeatherAdapter;
import com.example.sampleproject.databinding.FragmentDashboardDetailBinding;
import com.example.sampleproject.model.WeatherAssetModel;
import com.example.sampleproject.repository.WeatherAssetRepository;
import com.example.sampleproject.viewmodel.DashBoardDetailViewModel;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashBoardDetailFragment extends Fragment {
    private FragmentDashboardDetailBinding binding;
    private DashBoardWeatherAdapter weatherRecViewAdapter;
    private DashBoardPollutantAdapter pollutantRecViewAdapter;
    private String assetId;
    private LinearLayout statusContainer;
    private TextView place, time;
    private LinearLayout back;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardDetailBinding.inflate(inflater, container, false);
        initiate(binding);
        setupWeatherRecyclerView(binding);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.createNavigateOnClickListener(R.id.action_detail_to_basic).onClick(v);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initiate(FragmentDashboardDetailBinding binding) {
        back = binding.btnNavBack;
        statusContainer = binding.containerDashboardDetailStatus;
        place = binding.txtDashboardDetailPlace;
        time = binding.txtDashboardDetailTime;

        Bundle arguments = getArguments();
        if (arguments != null) {
            assetId = arguments.getString("assetId");
        }

        WeatherAssetRepository weatherAssetRepository = new WeatherAssetRepository(getContext());
        DashBoardDetailViewModel dashboardViewModel = new DashBoardDetailViewModel(assetId, weatherAssetRepository);
        weatherRecViewAdapter = new DashBoardWeatherAdapter(getContext());
        pollutantRecViewAdapter = new DashBoardPollutantAdapter(getContext());

        dashboardViewModel.getData().observe(getViewLifecycleOwner(), new Observer<WeatherAssetModel>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChanged(WeatherAssetModel weatherAsset) {
                if (weatherAsset != null) {
                    if (weatherAsset.attributes.place != null) {
                        place.setText(weatherAsset.attributes.place.value.toString());
                    } else {
                        place.setText(getString(R.string.default_place));
                    }
                    time.setText(getCurrentTime());
                    if (weatherAsset.attributes.aqi != null) {
                        Double aqiValue = (Double) weatherAsset.attributes.aqi.value;
                        if (aqiValue < 50) {
                            statusContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.good));
                        } else {
                            statusContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bad));
                        }
                    } else {
                        statusContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bad));
                    }

                    weatherRecViewAdapter.setWeatherAttributes(getWeatherList(weatherAsset.attributes));
                    pollutantRecViewAdapter.setPollutants(getPollutantList(weatherAsset.attributes));

                }
            }
        });
    }

    private void setupWeatherRecyclerView(FragmentDashboardDetailBinding binding) {
        RecyclerView weatherRecView = binding.dashboardDetailWeatherRecView;
        weatherRecView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        weatherRecView.setFocusable(false);
        weatherRecView.setNestedScrollingEnabled(false);
        weatherRecView.setAdapter(weatherRecViewAdapter);
        weatherRecView.setHasFixedSize(true);

        RecyclerView pollutantRecView = binding.dashboardDetailPollutantRecView;
        pollutantRecView.setLayoutManager(new GridLayoutManager(getContext(), 2));
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
//    private void setupChart(FragmentDashboardDetailBinding binding, WeatherAsset weatherAsset)  {
//        lineChart = binding.chart;
//
//        long from = weatherAsset.attributes.humidity.timestamp;
//        String type = weatherAsset.attributes.humidity.name;
//
//        AssetDataPointRequestBody body = new AssetDataPointRequestBody(from,addTenHours(from), type ,100);
//                dataPointRepository.getListDataPoint(weatherAsset.id,type,body).observe(getViewLifecycleOwner(), new Observer<List<AssetDataPoint>>() {
//                    @Override
//                    public void onChanged(List<AssetDataPoint> assetDataPoints) {
//                        if(assetDataPoints != null) {
//
//                            // Customize legend
//                            Legend legend = lineChart.getLegend();
//                            legend.setTextSize(20);
//                            legend.setForm(Legend.LegendForm.LINE);
//                            legend.setFormSize(20);
//                            legend.setFormToTextSpace(10);
//
//                            XAxis xAxis = lineChart.getXAxis();
//                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//                            xAxis.setLabelRotationAngle(-60);
//                            xAxis.setTextSize(10);
//                            xAxis.setGridLineWidth(2);
//                            xAxis.setAxisMinimum(0);
//                            xAxis.setAxisMaximum(assetDataPoints.size() - 1);
//
//                            YAxis yAxis = lineChart.getAxisLeft();
//                            yAxis.setTextSize(12);
//                            yAxis.setGridLineWidth(2);
//                            yAxis.setAxisMinimum(0);
//                            yAxis.setLabelCount(8);
//
//                            List<Entry> entries = new ArrayList<>();
//                            for (int i = 0; i < assetDataPoints.size() - 1;i++) {
//                                entries.add(new Entry(i, assetDataPoints.get(i).y));
//                            }
//
//                            LineDataSet dataSet1 = new LineDataSet(entries,null);
//                            dataSet1.setLineWidth(5f);
//
//                            LineData lineData = new LineData(dataSet1);
//                            lineChart.setData(lineData);
//                            lineChart.invalidate();
//
//                        }
//                    }
//                });
//
//
//    }

