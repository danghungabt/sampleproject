package com.example.sampleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sampleproject.api.ClientAPI;
import com.example.sampleproject.api.InterfaceAPI;
import com.example.sampleproject.model.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity {

    private InterfaceAPI apiInterface;
    private String userId = "4e3a4496-2f19-4813-bf00-09407d1ee8cb";
    private String realm = "master";
    private TextView  txtFirstName, txtLastName, txtEmail, txtUserName, txtRealm;
    private ImageButton btnToUserRoles, btnToRealmInfo, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        apiInterface =  ClientAPI.getClient().create(InterfaceAPI.class);

        btnToUserRoles = findViewById(R.id.btnToUserRoles);
        btnToRealmInfo = findViewById(R.id.btnToRealmInfo);
        btnBack = findViewById(R.id.btnBack);

        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtEmail = findViewById(R.id.txtEmail);
        txtRealm = findViewById(R.id.txtRealm);


        Call<UserModel> call = apiInterface.getUser(realm, userId);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                Log.d("API CALL", response.code()+"");
                //Log.d ("API CALL", response.toString());
                UserModel userModel = response.body();

                if(userModel != null) {
//                    Log.d("API CALL", asset.type + "");
                    txtFirstName.setText(userModel.firstName);
                    txtLastName.setText(userModel.lastName);
                    txtRealm.setText(userModel.realm);
                    txtEmail.setText(userModel.email);
                } else {
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d("API CALL", t.getMessage().toString() +"");

                //t.printStackTrace();

            }
        });

        btnToUserRoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserRealmRoleActivity.class);
                startActivity(intent);
            }
        });

        btnToRealmInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RealmInfoActivity.class);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}