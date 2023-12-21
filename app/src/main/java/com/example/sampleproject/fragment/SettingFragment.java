package com.example.sampleproject.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sampleproject.AuthActivity;
import com.example.sampleproject.ContainerActivity;
import com.example.sampleproject.HomePageActivity;
import com.example.sampleproject.R;
import com.example.sampleproject.api.TokenManager;

import java.util.Objects;

public class SettingFragment extends Fragment {

    private Button btnLogout;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        intitedView(view);


        btnLogout.setOnClickListener(v -> {
            TokenManager tokenManager = TokenManager.getInstance(getContext());
            tokenManager.logout();
            Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), HomePageActivity.class);
            startActivity(intent);
        });


        return view;
    }

    private void intitedView(View view) {
        btnLogout = view.findViewById(R.id.logoutButton);
    }
}