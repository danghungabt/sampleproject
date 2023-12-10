package com.example.sampleproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sampleproject.adapter.RealmRoleAdapter;
import com.example.sampleproject.api.ClientAPI;
import com.example.sampleproject.api.InterfaceAPI;
import com.example.sampleproject.model.RealmModel;
import com.example.sampleproject.model.RealmRoleModel;
import com.example.sampleproject.model.UserModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RealmInfoActivity extends AppCompatActivity {

    private InterfaceAPI apiInterface;
    private String realm = "master";
    RealmRoleAdapter adapter;
    RecyclerView recyclerView;
    TextView txtDisplayName;
    RealmModel realmModel;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm_info);

        recyclerView = findViewById(R.id.recyclerView);
        txtDisplayName = findViewById(R.id.txtDisplayname);
        btnBack = findViewById(R.id.btnBack);

        List<RealmRoleModel> realmRoleModels = new ArrayList<>();

        apiInterface =  ClientAPI.getClient().create(InterfaceAPI.class);

        Call<RealmModel> call = apiInterface.getRealm(realm);
        call.enqueue(new Callback<RealmModel>() {
            @Override
            public void onResponse(Call<RealmModel> call, Response<RealmModel> response) {
                Log.d("API CALL", response.code()+"");
                //Log.d ("API CALL", response.toString());
                realmModel = response.body();

                if(realmModel != null) {
//                    Log.d("API CALL", asset.type + "");
                    txtDisplayName.setText(realmModel.displayName);
                    for(RealmRoleModel realmRoleModel : realmModel.realmRoles){
                        realmRoleModels.add(realmRoleModel);
                    }
                } else {
                }

                adapter = new RealmRoleAdapter(realmRoleModels);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }

            @Override
            public void onFailure(Call<RealmModel> call, Throwable t) {
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