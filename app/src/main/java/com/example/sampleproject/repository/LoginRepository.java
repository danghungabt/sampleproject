package com.example.sampleproject.repository;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sampleproject.api.ApiClient;
import com.example.sampleproject.api.InterfaceAPI;
import com.example.sampleproject.model.TokenModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    private InterfaceAPI tokenService;
    public LoginRepository(Context context) {
        tokenService = ApiClient.getClient(context).create(InterfaceAPI.class);
    }

    public LiveData<TokenModel> login(String username, String password) {
        MutableLiveData<TokenModel> data = new MutableLiveData<>();

        Call<TokenModel> call = tokenService.getToken("openremote", "password", username, password);
        call.enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    // Handle login failure
                }
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                // Handle network errors
            }
        });
        return data;
    }
}