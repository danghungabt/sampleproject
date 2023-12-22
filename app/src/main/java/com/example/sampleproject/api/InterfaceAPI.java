package com.example.sampleproject.api;

import com.example.sampleproject.model.WeatherAssetModel;
import com.example.sampleproject.model.TokenModel;
import com.example.sampleproject.model.UserModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InterfaceAPI {


    @GET("api/master/asset/{assetID}")
    Call<WeatherAssetModel> getAsset(@Path("assetID") String assetID);//, @Header("Authorization") String auth);

    @FormUrlEncoded
    @POST("auth/realms/master/protocol/openid-connect/token")
    Call<TokenModel> getToken(
            @Field("client_id") String clientId,
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grantType
    );

    @GET("api/master/user/{realm}/{userId}")
    Call<UserModel> getUser(@Path("realm") String realm, @Path("userId") String userId);

    /*@GET("api/master/user/{realm}/userRoles/{userId}")
    Call<UserRealmRoleModel[]> getUserRealmRole(@Path("realm") String realm, @Path("userId") String userId);*/

/*    @GET("api/master/realm/{realm}")
    Call<RealmModel> getRealm(@Path("realm") String realm);*/

}
