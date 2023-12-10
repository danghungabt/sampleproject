package com.example.sampleproject.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleproject.R;

public class UserRoleViewHolder extends RecyclerView.ViewHolder {
    public TextView txtName;
    public TextView txtDescription;
    public View view;
    public UserRoleViewHolder(View itemView) {
        super(itemView);
        txtName = itemView.findViewById(R.id.txtName);
        txtDescription = itemView.findViewById(R.id.txtDescription);
    }
}
