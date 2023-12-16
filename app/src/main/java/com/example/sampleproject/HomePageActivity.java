package com.example.sampleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class HomePageActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getSupportActionBar().hide();

        sharedPreferences = LocalStorage.getInstance(this);
        String accessToken = sharedPreferences.getString("access_token", null);

        Intent intent = new Intent(HomePageActivity.this,ContainerActivity.class);
        if (accessToken != null) {
            startActivity(intent);
        }

        // Ánh xạ các nút và gắn lắng nghe sự kiện
        findViewById(R.id.btnSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình Sign In
                Intent intent = new Intent(HomePageActivity.this, AuthActivity.class);
                intent.putExtra("screen", "login");
                startActivity(intent);
            }
        });

        findViewById(R.id.buttonSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình Sign Up
                Intent intent = new Intent(HomePageActivity.this, AuthActivity.class);
                intent.putExtra("screen", "register");
                startActivity(intent);
            }
        });
    }
}