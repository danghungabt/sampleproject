package com.example.sampleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sampleproject.api.ClientAPI;
import com.example.sampleproject.api.InterfaceAPI;
import com.example.sampleproject.model.WeatherAssetModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class MainActivity extends AppCompatActivity {

    InterfaceAPI apiInterface;
    //String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJLcHRXNWJCcTlsRGliY2s5NHI3TldHQVl0SHBrUFI3N1A4V0hMWDVIX1E0In0.eyJleHAiOjE2NjUyMDc4MzMsImlhdCI6MTY2NTEyMTQ0OCwiYXV0aF90aW1lIjoxNjY1MTIxNDMzLCJqdGkiOiIxZjc3OTFjMy00OGJmLTQ4NGUtODI3MS1kOGY0NWNiNTcyMzkiLCJpc3MiOiJodHRwczovLzEwMy4xMjYuMTYxLjE5OS9hdXRoL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMTAxZGQ1MmMtMjNiYS00ZjM4LWExMjQtYjc4MGUxYjVhODFiIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoib3BlbnJlbW90ZSIsInNlc3Npb25fc3RhdGUiOiI5MjYyNGM1Yi1iNjM3LTRjNDItOWVhYS02MzhkNmRkZDJiNjQiLCJhY3IiOiIwIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHBzOi8vbG9jYWxob3N0IiwiaHR0cHM6Ly8xMDMuMTI2LjE2MS4xOTkiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDp1c2VycyIsInJlYWQ6bG9ncyIsInJlYWQ6bWFwIiwicmVhZDpydWxlcyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBlbWFpbCBwcm9maWxlIiwic2lkIjoiOTI2MjRjNWItYjYzNy00YzQyLTllYWEtNjM4ZDZkZGQyYjY0IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ1c2VyMSJ9.U0pMuxOkHW8pZKBVmTlQeIzGq-WtQoHzxCIgf2-gmB8vsAFCZlajaBvO8jJpupmS7NzegIFI6TcfAXkXFZXxPIJAmPb0a4RtFr-pthoOtERpct5TjxZvrEfJjNcC1J_EM1aumUJvOUZE7goVl4qG4UCw08Su0wIcmztWthLQu3CTgNuO5XwBqORJwGqIZAdSLyRSjhx4970nU4C_z-DRxVMtv1jlDfUyHeIHUA6c9gEiEY5PpbR6bnHDslb9Ta3MUOzFEdMd_PcXZtNxsJFKaeD_YKp-n9dmIQ45DY5-JKa6Mefo92NuAO_X9Jv5SA3FB5hj4dW1ODxjbnCqR3ZqTg";
    //String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE2OTYwNTgwMDUsImlhdCI6MTY5NTk3MzAwNiwiYXV0aF90aW1lIjoxNjk1OTcxNjA1LCJqdGkiOiJjYmE0NmI0OC05NmRlLTQ2MGYtOTJlMC02NGZhNGMxZGI5OWUiLCJpc3MiOiJodHRwczovL3Vpb3QuaXh4Yy5kZXYvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjRlM2E0NDk2LTJmMTktNDgxMy1iZjAwLTA5NDA3ZDFlZThjYiIsInR5cCI6IkJlYXJlciIsImF6cCI6Im9wZW5yZW1vdGUiLCJzZXNzaW9uX3N0YXRlIjoiYjM2ZWNlNzUtZTQ3My00ODNmLTlhNjktYjZmMWNhMzY5OTZjIiwiYWNyIjoiMCIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3Vpb3QuaXh4Yy5kZXYiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDptYXAiLCJyZWFkOnJ1bGVzIiwicmVhZDppbnNpZ2h0cyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiJiMzZlY2U3NS1lNDczLTQ4M2YtOWE2OS1iNmYxY2EzNjk5NmMiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJVc2VyIFRlc3QiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ1c2VyIiwiZ2l2ZW5fbmFtZSI6IlVzZXIiLCJmYW1pbHlfbmFtZSI6IlRlc3QiLCJlbWFpbCI6InVzZXJAaXh4Yy5kZXYifQ.SsG5B86mVXUlvtW603vyDNloEEgRQnVUX_WrQkiJld1ai9BocRS1FEuoaxiVNJYzWom3HO3_ipNAVoVD1EvDMA7C8B21carIHRHpNHWZuZnl-w7EO3kPVBQYtoQr44sFRdZl-f8HRNc5kvQY9P4LwcJBgyywkuGqmlRuwg1Svs-k6nvf_jM7oJ-o0UyPOfPw_wMLyqkGDpxF6dbcdHhLTN6ws33tChgnz-5HkrUEMyg4qw_V9ggoqIp2oc3T6zqHJu4Di63oJs5fSJ3XG9gr4nkBODcc-J1_G_nVLN512cNTeQxuA0y6uksJqIxa7xZrjFCBvwRx5GJF-qwHDac4Yg";
    String assessId = "5zI6XqkQVSfdgOrZ1MyWEf";
    private Button btnToUserInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        TextView txttype = (TextView)findViewById(R.id.textView1);
        btnToUserInfo = findViewById(R.id.btnToUserInfo);
        txttype.setText("HELLO MY FRIEND");
//        apiInterface = ClientAPI.getClient().create(InterfaceAPI.class);

        apiInterface =  ClientAPI.getClient().create(InterfaceAPI.class);

//        Call<Asset> call = apiInterface.getAsset("6H4PeKLRMea1L0WsRXXWp9");//, "Bearer "+ token);
        Call<WeatherAssetModel> call = apiInterface.getAsset( assessId);
        call.enqueue(new Callback<WeatherAssetModel>() {
            @Override
            public void onResponse(Call<WeatherAssetModel> call, Response<WeatherAssetModel> response) {
                Log.d("API CALL", response.code()+"");
                //Log.d ("API CALL", response.toString());
                WeatherAssetModel asset = response.body();

                if(asset != null) {
                    Log.d("API CALL", asset.type + "");
                    txttype.setText(asset.name + " " + asset.type);
                } else {
                    txttype.setText("Unauthorized");
                }
            }

            @Override
            public void onFailure(Call<WeatherAssetModel> call, Throwable t) {
                Log.d("API CALL", t.getMessage().toString() +"");

                //t.printStackTrace();

            }
        });

        btnToUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("mainactivity", "OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("mainactivity", "OnResume");
    }
}