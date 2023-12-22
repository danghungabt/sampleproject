package com.example.sampleproject.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sampleproject.R;
import com.example.sampleproject.model.LightAssetModel;
import com.example.sampleproject.model.WeatherAssetModel;
import com.example.sampleproject.repository.WeatherAssetRepository;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

public class HomeFragment extends Fragment {
    private double weatherLatitude, weatherLongitude;
    private double lightLatitude, lightLongitude;
    private WeatherAssetModel weatherAssetModel;
    private LightAssetModel lightAssetModel;

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng weatherMarker = new LatLng(weatherLatitude, weatherLongitude);
//            markerList.add(googleMap.addMarker(new MarkerOptions().position(weatherMarker).title("weather")));
            googleMap.addMarker(new MarkerOptions().position(weatherMarker).title("Default Weather"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(weatherMarker));

            LatLng lightMarker = new LatLng(lightLatitude, lightLongitude);
//            markerList.add(googleMap.addMarker(new MarkerOptions().position(lightMarker).title("light")));
            googleMap.addMarker(new MarkerOptions().position(lightMarker).title("Light"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(lightMarker));

            // Set the bounds
            LatLngBounds bounds = new LatLngBounds(
                    new LatLng(10.86, 106.79),   // southwest
                    new LatLng(10.88, 106.82)    // northeast
            );
            googleMap.setLatLngBoundsForCameraTarget(bounds);

            // Set the default zoom level
            float defaultZoom = 15.0f;
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(defaultZoom));

            // Set min and max zoom levels
            googleMap.setMinZoomPreference(14);
            googleMap.setMaxZoomPreference(19);

            googleMap.setOnMarkerClickListener(marker -> {
                if(Objects.equals(marker.getTitle(), "Default Weather")){
                    showPopupWeatherAsset(weatherAssetModel);
                }else {
                    showPopupLightAsset(lightAssetModel);
                }

                return false;
            });

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        initiate();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void initiate() {
        WeatherAssetRepository repository = new WeatherAssetRepository(getContext());
        repository.getAssetById("5zI6XqkQVSfdgOrZ1MyWEf").observe(getViewLifecycleOwner(), weatherAsset -> {
            if (weatherAsset.attributes.location.value != null) {
                weatherLongitude = weatherAsset.attributes.location.value.coordinates.get(0);
                weatherLatitude = weatherAsset.attributes.location.value.coordinates.get(1);
                weatherAssetModel = weatherAsset;
            }
        });

        repository.getLightAssetById("6iWtSbgqMQsVq8RPkJJ9vo").observe(getViewLifecycleOwner(), lightAsset -> {
            if (lightAsset.attributes.location.value != null) {
                lightLongitude = lightAsset.attributes.location.value.coordinates.get(0);
                lightLatitude = lightAsset.attributes.location.value.coordinates.get(1);
                lightAssetModel = lightAsset;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showPopupLightAsset(LightAssetModel lightAssetModel) {
        TextView tvName, tvType;
        // Inflate the popup layout
        @SuppressLint("InflateParams") View popupView = getLayoutInflater().inflate(R.layout.popup_light_asset, null);

        tvName = popupView.findViewById(R.id.tvName);
        tvType = popupView.findViewById(R.id.tvType);

        tvName.setText(lightAssetModel.name);
        tvType.setText(lightAssetModel.type);

        // Dismiss the popup when clicked outside
        popupView.setOnTouchListener((v, event) -> true);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(popupView);
        bottomSheetDialog.show();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showPopupWeatherAsset(WeatherAssetModel weatherAssetModel) {
        TextView tvHumidity, tvRainfall, tvTemperature, tvWindDirection, tvWindSpeed, tvName;
        // Inflate the popup layout
        @SuppressLint("InflateParams") View popupView = getLayoutInflater().inflate(R.layout.popup_weather_asset, null);

        tvHumidity = popupView.findViewById(R.id.tvHumidity);
        tvRainfall = popupView.findViewById(R.id.tvRainfall);
        tvTemperature = popupView.findViewById(R.id.tvTemperature);
        tvWindDirection = popupView.findViewById(R.id.tvWindDirection);
        tvWindSpeed = popupView.findViewById(R.id.tvWindSpeed);
        tvName = popupView.findViewById(R.id.tvName);

        tvHumidity.setText(weatherAssetModel.attributes.humidity.value.toString());
        tvRainfall.setText(weatherAssetModel.attributes.rainfall.value.toString());
        tvTemperature.setText(weatherAssetModel.attributes.temperature.value.toString());
        tvWindDirection.setText(weatherAssetModel.attributes.windDirection.value.toString());
        tvWindSpeed.setText(weatherAssetModel.attributes.windSpeed.value.toString());
        tvName.setText(weatherAssetModel.name);

        // Dismiss the popup when clicked outside
        popupView.setOnTouchListener((v, event) -> true);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(popupView);
        bottomSheetDialog.show();
    }
}