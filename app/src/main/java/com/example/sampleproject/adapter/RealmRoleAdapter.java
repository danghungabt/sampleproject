package com.example.sampleproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleproject.R;
import com.example.sampleproject.model.RealmRoleModel;
import com.example.sampleproject.viewholder.RealmRoleViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RealmRoleAdapter extends RecyclerView.Adapter<RealmRoleViewHolder> {

    List<RealmRoleModel> list = new ArrayList<>();

    public RealmRoleAdapter(List<RealmRoleModel> list){
        this.list = list;
    }

    @NonNull
    @Override
    public RealmRoleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context
                = parent.getContext();
        LayoutInflater inflater
                = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.role_card, parent, false);

        RealmRoleViewHolder viewHolder = new RealmRoleViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RealmRoleViewHolder holder, int position) {
        holder.txtName.setText(list.get(position).name);
        holder.txtDescription.setText(list.get(position).description);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
