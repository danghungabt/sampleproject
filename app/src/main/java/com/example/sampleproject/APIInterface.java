package com.example.sampleproject;

import com.example.sampleproject.Model.Asset;
import com.example.sampleproject.Model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterface {


    @GET("api/master/asset/{assetID}")
    Call<Asset> getAsset(@Path("assetID") String assetID);//, @Header("Authorization") String auth);

    @FormUrlEncoded
    @POST("auth/realms/master/protocol/openid-connect/token")
    Call<LoginResponse> login(
            @Field("client_id") String clientId,
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grantType
    );

}
