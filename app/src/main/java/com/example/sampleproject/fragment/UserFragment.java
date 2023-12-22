package com.example.sampleproject.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sampleproject.HomePageActivity;
import com.example.sampleproject.R;
import com.example.sampleproject.api.ApiClient;
import com.example.sampleproject.api.ClientAPI;
import com.example.sampleproject.api.InterfaceAPI;
import com.example.sampleproject.api.TokenManager;
import com.example.sampleproject.model.UserModel;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {

    private InterfaceAPI apiInterface;
    private String userId = "4e3a4496-2f19-4813-bf00-09407d1ee8cb";
    private String realm = "master";
    private TextView txtFirstName, txtLastName, txtEmail, txtUserName;
    private ProgressBar loadingProgressBar;
    private ScrollView svContent;
    private Spinner languageSpinner;
    private Button logoutButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        initiated(view);

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
                    txtEmail.setText(userModel.email);
                    txtUserName.setText(userModel.username);
                } else {
                }

                loadingProgressBar.setVisibility(View.INVISIBLE);
                svContent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d("API CALL", t.getMessage().toString() +"");

            }
        });

        logoutButton.setOnClickListener(v -> {
            TokenManager tokenManager = TokenManager.getInstance(getContext());
            tokenManager.logout();
            Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), HomePageActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void initiated(View view) {
        apiInterface =  ApiClient.getClient(getContext()).create(InterfaceAPI.class);
        txtFirstName = view.findViewById(R.id.txtFirstName);
        txtLastName = view.findViewById(R.id.txtLastName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtUserName = view.findViewById(R.id.txtUsername);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
        svContent = view.findViewById(R.id.svContent);
        languageSpinner = view.findViewById(R.id.languageSpinner);
        logoutButton = view.findViewById(R.id.logoutButton);

        ArrayList languageArrayList = new ArrayList<String>();
        languageArrayList.add("English");
        languageArrayList.add("Vietnamese");
        ArrayAdapter languageAdapter = new ArrayAdapter<String>(view.getContext(),
                R.layout.spinner_item, // Sử dụng tệp layout tùy chỉnh ở đây
                languageArrayList
        );

        languageAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        languageSpinner.setAdapter(languageAdapter);
    }
}