package com.example.sampleproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleproject.R;
import com.example.sampleproject.model.WeatherAssetModel;
import com.example.sampleproject.utils.ConvertTimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DashBoardPollutantAdapter extends RecyclerView.Adapter<DashBoardPollutantAdapter.DetailPollutantViewHolder>{
    private Context context;
    private List<WeatherAssetModel.Attributes.Pollutant> pollutants = new ArrayList<>();
    public DashBoardPollutantAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public DetailPollutantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_pollutant,parent,false);
        return new DetailPollutantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailPollutantViewHolder holder, int position) {
        WeatherAssetModel.Attributes.Pollutant pollutant = pollutants.get(position);
        holder.name.setText(pollutant.name);
        holder.value.setText(pollutant.value.toString());
        holder.tvTime.setText(ConvertTimeUtils.convertTimestampToDate(pollutant.timestamp));
        @SuppressLint("DiscouragedApi") int attributeResourceId = context.getResources()
                .getIdentifier(pollutant.name, "string", context.getPackageName());
        if (attributeResourceId != 0) {
            if(context.getString(attributeResourceId).equals("")){
                holder.unit.setText("");
            }else {
                holder.unit.setText("(" + context.getString(attributeResourceId) + ")");
            }
        }

    }

    @Override
    public int getItemCount() {
        return pollutants.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPollutants(List<WeatherAssetModel.Attributes.Pollutant> pollutants) {
        this.pollutants = pollutants.stream()
                .filter(measurement -> measurement.value != null)
                .collect(Collectors.toList());
        notifyDataSetChanged();
    }

    public static class DetailPollutantViewHolder extends RecyclerView.ViewHolder {
        // private LinearLayout statusContainer;
        private final TextView name;
        private final TextView value;
        private final TextView unit;
        private final TextView tvTime;
        public DetailPollutantViewHolder(@NonNull View itemView) {
            super(itemView);
            CardView parent = itemView.findViewById(R.id.detail_pollutant_card);
            //statusContainer = itemView.findViewById(R.id.container_pollutant_status);
            name = itemView.findViewById(R.id.txt_pollutant_name) ;
            value = itemView.findViewById(R.id.txt_pollutant_value);
            unit = itemView.findViewById(R.id.pollutant_unit);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
