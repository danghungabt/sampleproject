package com.example.sampleproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail, editTextPassword, editTextConfirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        // Ánh xạ các trường nhập liệu
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword1);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        // Ánh xạ các nút
        Button buttonBack = findViewById(R.id.buttonBack);
        Button buttonSignUp = findViewById(R.id.buttonSignUp);

        // Xử lý sự kiện nút "Back"
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng màn hình Sign Up
            }
        });

        // Xử lý sự kiện nút "Sign Up"
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện xác thực và đăng ký tài khoản tại đây
                // Để đơn giản, bạn có thể in ra các giá trị nhập liệu
                String username = editTextUsername.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                Intent loading = new Intent(getApplicationContext(), LoadingActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("email", email);
                bundle.putString("password", password);
                bundle.putString("confirmPassword", confirmPassword);

                loading.putExtra("register", bundle);
                startActivity(loading);

            }
        });
    }
}