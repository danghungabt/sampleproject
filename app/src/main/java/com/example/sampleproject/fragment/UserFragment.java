package com.example.sampleproject.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sampleproject.ContainerActivity;
import com.example.sampleproject.HomePageActivity;
import com.example.sampleproject.LocalStorage;
import com.example.sampleproject.R;
import com.example.sampleproject.api.ApiClient;
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
    private TextView txtFirstName, txtLastName, txtEmail, txtUserName;
    private ProgressBar loadingProgressBar;
    private ScrollView svContent;
    private Spinner languageSpinner;
    private Button logoutButton;
    private String language;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        initiated(view);

        String realm = "master";
        String userId = "4e3a4496-2f19-4813-bf00-09407d1ee8cb";
        Call<UserModel> call = apiInterface.getUser(realm, userId);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                Log.d("API CALL", response.code() + "");
                //Log.d ("API CALL", response.toString());
                UserModel userModel = response.body();

                if (userModel != null) {
//                    Log.d("API CALL", asset.type + "");
                    txtFirstName.setText(userModel.firstName);
                    txtLastName.setText(userModel.lastName);
                    txtEmail.setText(userModel.email);
                    txtUserName.setText(userModel.username);
                }

                loadingProgressBar.setVisibility(View.INVISIBLE);
                svContent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d("API CALL", t.getMessage() + "");

            }
        });

        logoutButton.setOnClickListener(v -> {
            TokenManager tokenManager = TokenManager.getInstance(getContext());
            tokenManager.logout();
            Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), HomePageActivity.class);
            startActivity(intent);
        });

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equals("English") && language.equals("vi")) {
                    setLocale("en");
                }
                if (item.equals("Vietnamese") && language.equals("en")) {
                    setLocale("vi");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void initiated(View view) {
        apiInterface = ApiClient.getClient(getContext()).create(InterfaceAPI.class);
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

        SharedPreferences sharedPreferences = LocalStorage.getInstance(getContext());
        language = sharedPreferences.getString("language", "en");
        if (language.equals("en")) {
            languageSpinner.setSelection(0);
        } else {
            languageSpinner.setSelection(1);
        }
    }

    public void setLocale(String languageCode) {
        // Lưu ngôn ngữ đã chọn vào SharedPreferences (nếu cần)
        SharedPreferences sharedPreferences = LocalStorage.getInstance(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", languageCode);
        editor.apply();
        Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), ContainerActivity.class);
        startActivity(intent);
        getActivity().finish();

        // Restart activity để áp dụng ngôn ngữ mới
    }
}