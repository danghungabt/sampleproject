package com.example.sampleproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleproject.R;
import com.example.sampleproject.model.WeatherAssetModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DashBoardWeatherAdapter extends RecyclerView.Adapter<DashBoardWeatherAdapter.DetailWeatherViewHolder>{
    private Context context;
    private List<WeatherAssetModel.Attributes.Measurement> weatherAttributes = new ArrayList<>();
    public DashBoardWeatherAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public DetailWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_weather,parent,false);
        return new DashBoardWeatherAdapter.DetailWeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailWeatherViewHolder holder, int position) {
        WeatherAssetModel.Attributes.Measurement weatherAttribute = weatherAttributes.get(position);
        holder.weatherName.setText(weatherAttribute.name);
        holder.weatherValue.setText(weatherAttribute.value.toString());

        int attributeResourceId = context.getResources()
                .getIdentifier(weatherAttribute.name, "string", context.getPackageName());
        if (attributeResourceId != 0) {
            holder.weatherUnit.setText(context.getString(attributeResourceId));
        }

        int drawableResourceId = context.getResources()
                .getIdentifier(weatherAttribute.name.toLowerCase(), "drawable", context.getPackageName());
        if (drawableResourceId != 0) {
            holder.weatherImage.setImageResource(drawableResourceId);
        }

    }

    @Override
    public int getItemCount() {
        return weatherAttributes.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setWeatherAttributes(List<WeatherAssetModel.Attributes.Measurement> weatherAttributes) {
        this.weatherAttributes = weatherAttributes.stream()
                .filter(measurement -> measurement.value != null)
                .collect(Collectors.toList());
        notifyDataSetChanged();
    }

    public class DetailWeatherViewHolder extends RecyclerView.ViewHolder {
        private CardView parent;
        //private LinearLayout statusContainer;
        private ImageView weatherImage;
        private TextView weatherName, weatherValue, weatherUnit;
        public DetailWeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.detail_weather_card);
            //statusContainer = itemView.findViewById(R.id.container_weather_status);
            weatherImage = itemView.findViewById(R.id.img_detail_weather);
            weatherName = itemView.findViewById(R.id.txt_weather_name) ;
            weatherValue = itemView.findViewById(R.id.txt_weather_value);
            weatherUnit = itemView.findViewById(R.id.txt_weather_unit);
        }
    }
}