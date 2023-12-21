package com.example.sampleproject.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sampleproject.R;

import java.util.Objects;

public class SignUpFragment extends Fragment {

    private EditText editTextUsername, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonBack, buttonSignUp;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initiate(view);

        // Xử lý sự kiện nút "Back"
        buttonBack.setOnClickListener(v -> {
            Objects.requireNonNull(getActivity()).finish(); // Đóng màn hình Sign Up
        });

        // Xử lý sự kiện nút "Sign Up"
        buttonSignUp.setOnClickListener(v -> toLoadingFragment());

        return view;
    }

    private void initiate(View view){
        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword1);
        editTextConfirmPassword = view.findViewById(R.id.editTextConfirmPassword);

        Bundle bundle = getArguments();
        if(bundle != null) {
            String errorMessage = bundle.getString("error");
            Toast.makeText(getActivity(), "Register error : " + errorMessage, Toast.LENGTH_SHORT).show();
        }

        // Ánh xạ các nút
        buttonBack = view.findViewById(R.id.buttonBack);
        buttonSignUp = view.findViewById(R.id.buttonSignUp);
    }

    private void toLoadingFragment() {

        String username = editTextUsername.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("email", email);
        bundle.putString("password", password);
        bundle.putString("confirmPassword", confirmPassword);

        FragmentManager fmgr = Objects.requireNonNull(getActivity()).getSupportFragmentManager();

        LoadingFragment loadingFragment = new LoadingFragment();

        loadingFragment.setArguments(bundle);

        FragmentTransaction ft = fmgr.beginTransaction();

        ft.replace(R.id.container_body, loadingFragment);

        ft.commit();
    }
}