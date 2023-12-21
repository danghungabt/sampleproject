package com.example.sampleproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleproject.R;
import com.example.sampleproject.fragment.DashBoardDetailFragment;
import com.example.sampleproject.model.WeatherAssetModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardBasicAdapter extends RecyclerView.Adapter<DashboardBasicAdapter.BasicWeatherViewHolder> {
    private Context context;
    private List<WeatherAssetModel> weatherAssets = new ArrayList<>();
    public DashboardBasicAdapter(Context context) {this.context = context;}

    @NonNull
    @Override
    public BasicWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_basic_weather,parent,false);
        return new BasicWeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasicWeatherViewHolder holder, @SuppressLint("RecyclerView") int position) {
        WeatherAssetModel.Attributes assetAttribute = weatherAssets.get(position).attributes;
        if(assetAttribute.place != null) {
            holder.placeText.setText(assetAttribute.place.value.toString());
        }
        if(assetAttribute.pm25 != null) {
            holder.pollutantText.setText(assetAttribute.pm25.value.toString());
        }
        if(assetAttribute.aqi != null) {
             Double aqiValue = (Double)assetAttribute.aqi.value ;
            if( aqiValue < 50 ) {
                holder.qualityText.setText(R.string.good_weather_status);
                holder.qualityText.setBackgroundResource(R.drawable.custom_weather_good);
            } else  {
                holder.qualityText.setText(R.string.bad_weather_status);
                holder.qualityText.setBackgroundResource(R.drawable.custom_weather_bad);
            }
        }

        holder.parent.setOnClickListener(v -> {
           // FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            DashBoardDetailFragment dashBoardDetailFragment = new DashBoardDetailFragment();
            Bundle bundle = new Bundle();
            if (weatherAssets.get(position) != null) {
                String id = weatherAssets.get(position).id;
                bundle.putString("assetId", id);
                dashBoardDetailFragment.setArguments(bundle);
                Navigation.createNavigateOnClickListener(R.id.action_basic_to_detail,bundle).onClick(holder.itemView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return weatherAssets.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setWeatherAssets(List<WeatherAssetModel> weatherAssets) {
        this.weatherAssets = weatherAssets;
        notifyDataSetChanged();
    }

    public static class BasicWeatherViewHolder extends RecyclerView.ViewHolder {
        private final TextView placeText;
        private final TextView pollutantText;
        private final TextView qualityText;
        private final LinearLayout parent;

        public BasicWeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.basic_weather_card);
            placeText = itemView.findViewById(R.id.txt_place);
            pollutantText = itemView.findViewById(R.id.txt_pollutant_status);
            qualityText = itemView.findViewById(R.id.txt_quality);
        }
    }

}
