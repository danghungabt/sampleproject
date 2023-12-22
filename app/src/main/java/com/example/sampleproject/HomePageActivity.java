package com.example.sampleproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sampleproject.utils.ContextUtils;

import java.util.Locale;
import java.util.Objects;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Objects.requireNonNull(getSupportActionBar()).hide();

        SharedPreferences sharedPreferences = LocalStorage.getInstance(this);
        String accessToken = sharedPreferences.getString("access_token", null);
        long expiresIn = sharedPreferences.getLong("expires_in", 0);

        Intent intent = new Intent(HomePageActivity.this,ContainerActivity.class);

        if (accessToken != null && expiresIn > (System.currentTimeMillis() / 1000)) {
                startActivity(intent);
        }

        // Ánh xạ các nút và gắn lắng nghe sự kiện
        findViewById(R.id.btnSignIn).setOnClickListener(v -> {
            // Chuyển đến màn hình Sign In
            Intent intent1 = new Intent(HomePageActivity.this, AuthActivity.class);
            intent1.putExtra("screen", "login");
            startActivity(intent1);
        });

        findViewById(R.id.buttonSignUp).setOnClickListener(v -> {
            // Chuyển đến màn hình Sign Up
            Intent intent12 = new Intent(HomePageActivity.this, AuthActivity.class);
            intent12.putExtra("screen", "register");
            startActivity(intent12);
        });
    }
}