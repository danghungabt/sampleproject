package com.example.sampleproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleproject.adapter.RealmRoleAdapter;
import com.example.sampleproject.adapter.UserRoleAdapter;
import com.example.sampleproject.api.ClientAPI;
import com.example.sampleproject.api.InterfaceAPI;
import com.example.sampleproject.model.RealmRoleModel;
import com.example.sampleproject.model.UserRealmRoleModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRealmRoleActivity extends AppCompatActivity {

    private InterfaceAPI apiInterface;
    private String userId = "4e3a4496-2f19-4813-bf00-09407d1ee8cb";
    private String realm = "master";
    private TextView textView;

    UserRoleAdapter adapter;
    RecyclerView recyclerView;
    UserRealmRoleModel userRealmRoleModel;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_realm_role);

        recyclerView = findViewById(R.id.recyclerView);
        btnBack = findViewById(R.id.btnBack);
        List<UserRealmRoleModel> userRealmRoleModels = new ArrayList<>();

        apiInterface =  ClientAPI.getClient().create(InterfaceAPI.class);

//        textView = findViewById(R.id.userRealmRole);

        Call<UserRealmRoleModel[]> call = apiInterface.getUserRealmRole(realm, userId);
        call.enqueue(new Callback<UserRealmRoleModel[]>() {
            @Override
            public void onResponse(Call<UserRealmRoleModel[]> call, Response<UserRealmRoleModel[]> response) {
                Log.d("API CALL", response.code()+"");
                //Log.d ("API CALL", response.toString());
//                List<UserRealmRoleModel> userRealmRole = response.body();

                UserRealmRoleModel[] userRealmRoles = response.body();
                if(userRealmRoles[0] != null) {
//                    Log.d("API CALL", asset.type + "");
//                    textView.setText(userRealmRoles[0].name + " "+ userRealmRoles[0].composite);
                    for(UserRealmRoleModel userRealmRoleModel : userRealmRoles){
                        userRealmRoleModels.add(userRealmRoleModel);
                    }
                } else {
                }
                adapter = new UserRoleAdapter(userRealmRoleModels);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }

            @Override
            public void onFailure(Call<UserRealmRoleModel[]> call, Throwable t) {
                Log.d("API CALL", t.getMessage().toString() +"");

                //t.printStackTrace();

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