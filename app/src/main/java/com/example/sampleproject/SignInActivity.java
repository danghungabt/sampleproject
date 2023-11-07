package com.example.sampleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sampleproject.Model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();

        // Ánh xạ các trường nhập liệu
        editTextUsername = findViewById(R.id.editTextUsernameSignIn);
        editTextPassword = findViewById(R.id.editTextPasswordSignIn);

        // Ánh xạ các nút
        Button buttonBack = findViewById(R.id.buttonBackSignIn);
        Button buttonSignIn = findViewById(R.id.buttonSignIn);

        // Xử lý sự kiện nút "Back"
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng màn hình Sign In
            }
        });

        // Xử lý sự kiện nút "Sign In"
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện xác thực đăng nhập tại đây
                // Để đơn giản, bạn có thể in ra các giá trị nhập liệu
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                // In ra Logcat để kiểm tra
                System.out.println("Username: " + username);
                System.out.println("Password: " + password);

                // Đây là nơi bạn sẽ thêm xác thực đăng nhập
                callLoginApi(username, password);
            }
        });
    }

    private void callLoginApi(String username, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://uiot.ixxc.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIInterface apiService = retrofit.create(APIInterface.class);
        Call<LoginResponse> call = apiService.login("openremote", username, password, "password");

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    String accessToken = loginResponse.getAccessToken();

                    // Sử dụng accessToken ở đây
                    // Ví dụ: Lưu vào SharedPreferences, gửi đến server khác, vv.
                    APIClient.setToken(accessToken);

                    // Chuyển đến màn hình Main
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}