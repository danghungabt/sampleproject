package com.example.sampleproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.sampleproject.fragment.LoadingFragment;
import com.example.sampleproject.fragment.SignInFragment;
import com.example.sampleproject.fragment.SignUpFragment;
import com.example.sampleproject.utils.ContextUtils;

import java.util.Locale;

public class AuthActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        String screen = intent.getStringExtra("screen");

        if(screen.equals("login")){
            toFragment(new SignInFragment());
        }else {
            toFragment(new SignUpFragment());
        }
    }

    private void toFragment(Fragment fragment) {

        FragmentManager fmgr = getSupportFragmentManager();

        FragmentTransaction ft = fmgr.beginTransaction();

        ft.add(R.id.container_body, fragment);

        ft.commit();
    }

}