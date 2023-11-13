package com.example.sampleproject.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("access_token")
    private String accessToken;

    // Các trường dữ liệu khác có thể được thêm tùy theo cần thiết

    public String getAccessToken() {
        return accessToken;
    }
}
