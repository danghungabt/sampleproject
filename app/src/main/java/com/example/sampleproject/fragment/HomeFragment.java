package com.example.sampleproject.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private double weatherLatitude, weatherLongitude;
    private double lightLatitude, lightLongitude;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            List<Marker> markerList = new ArrayList<>();
            LatLng weatherMarker = new LatLng(weatherLatitude, weatherLongitude);
            markerList.add(googleMap.addMarker(new MarkerOptions().position(weatherMarker ).title("Weather Asset").snippet("Weather Asset Snippet")));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(weatherMarker));

            LatLng lightMarker = new LatLng(lightLatitude, lightLongitude);
            markerList.add(googleMap.addMarker(new MarkerOptions().position(lightMarker).title("Light Asset").snippet("Light Asset Snippet")));
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

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    for (Marker myMarker : markerList){
                        if (marker.equals(myMarker)) { // Kiểm tra xem marker được click có phải là marker bạn đã tạo hay không
                            // Hiển thị popup (info window)
                            marker.showInfoWindow();
                            // Trả về true để ngăn không cho hệ thống xử lý sự kiện mặc định của Marker
                            return true;
                        }
                    }
                    return false;
                }
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
        repository.getAssetById("5zI6XqkQVSfdgOrZ1MyWEf").observe(getViewLifecycleOwner(), new Observer<WeatherAssetModel>() {
            @Override
            public void onChanged(WeatherAssetModel weatherAsset) {
                if(weatherAsset.attributes.location.value != null) {
                    weatherLongitude = weatherAsset.attributes.location.value.coordinates.get(0);
                    weatherLatitude = weatherAsset.attributes.location.value.coordinates.get(1);
                }
            }
        });

        repository.getLightAssetById("6iWtSbgqMQsVq8RPkJJ9vo").observe(getViewLifecycleOwner(), new Observer<LightAssetModel>() {
            @Override
            public void onChanged(LightAssetModel lightAsset) {
                if(lightAsset.attributes.location.value != null) {
                    lightLongitude = lightAsset.attributes.location.value.coordinates.get(0);
                    lightLatitude = lightAsset.attributes.location.value.coordinates.get(1);
                }
            }
        });
    }
}