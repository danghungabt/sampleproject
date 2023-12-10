package com.example.sampleproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleproject.R;
import com.example.sampleproject.model.RealmRoleModel;
import com.example.sampleproject.model.UserRealmRoleModel;
import com.example.sampleproject.viewholder.RealmRoleViewHolder;
import com.example.sampleproject.viewholder.UserRoleViewHolder;

import java.util.ArrayList;
import java.util.List;

public class UserRoleAdapter extends RecyclerView.Adapter<UserRoleViewHolder> {

    List<UserRealmRoleModel> list = new ArrayList<>();

    public UserRoleAdapter(List<UserRealmRoleModel> list){
        this.list = list;
    }

    @NonNull
    @Override
    public UserRoleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context
                = parent.getContext();
        LayoutInflater inflater
                = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.role_card, parent, false);

        UserRoleViewHolder viewHolder = new UserRoleViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserRoleViewHolder holder, int position) {
        holder.txtName.setText(list.get(position).name);
        holder.txtDescription.setText(list.get(position).description);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
