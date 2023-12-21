package com.example.sampleproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sampleproject.R;
import com.example.sampleproject.model.WeatherAssetModel;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<WeatherAssetModel> {
    private final Context mContext;
    private final List<WeatherAssetModel> weatherAssetModels;

    public CustomSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<WeatherAssetModel> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.weatherAssetModels = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @NonNull
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    public View getCustomView(int position, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View row = inflater.inflate(R.layout.item_spinner_custom, parent, false);

        TextView textView = row.findViewById(R.id.tvNameAsset);
        textView.setText(weatherAssetModels.get(position).name);

        return row;
    }


}
