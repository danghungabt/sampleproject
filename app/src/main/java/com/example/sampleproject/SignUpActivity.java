package com.example.sampleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        editTextPassword = findViewById(R.id.editTextPasswordSignIn);
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

                // In ra Logcat để kiểm tra
                System.out.println("Username: " + username);
                System.out.println("Email: " + email);
                System.out.println("Password: " + password);
                System.out.println("Confirm Password: " + confirmPassword);

                // Đây là nơi bạn sẽ thêm xác thực và đăng ký
            }
        });
    }
}