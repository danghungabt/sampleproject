package com.example.sampleproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sampleproject.ContainerActivity;
import com.example.sampleproject.R;
import com.example.sampleproject.api.ClientAPI;
import com.example.sampleproject.api.ClientCustomAPI;
import com.example.sampleproject.api.InterfaceAPI;
import com.example.sampleproject.api.TokenManager;
import com.example.sampleproject.model.TokenModel;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInFragment extends Fragment {

    private EditText editTextUsername, editTextPassword;
    private Button buttonBack, buttonSignIn;
    private TokenManager tokenManager;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        initiate(view);

        // Xử lý sự kiện nút "Back"
        buttonBack.setOnClickListener(v -> {
            Objects.requireNonNull(getActivity()).finish(); // Đóng màn hình Sign In
        });

        // Xử lý sự kiện nút "Sign In"
        buttonSignIn.setOnClickListener(v -> {
            // Thực hiện xác thực đăng nhập tại đây
            // Để đơn giản, bạn có thể in ra các giá trị nhập liệu
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();

            // Đây là nơi bạn sẽ thêm xác thực đăng nhập
            callLoginApi(username, password);
        });

        return view;
    }

    private void initiate(View view){
        editTextUsername = view.findViewById(R.id.editTextUsernameSignIn);
        editTextPassword = view.findViewById(R.id.editTextPasswordSignIn);
        buttonBack = view.findViewById(R.id.buttonBackSignIn);
        buttonSignIn = view.findViewById(R.id.buttonSignIn);
        TextView btnSignUp = view.findViewById(R.id.textView10);

        tokenManager = TokenManager.getInstance(getContext());

        Bundle bundle = getArguments();
        if(bundle != null) {
            Toast.makeText(getActivity(), "Register Success", Toast.LENGTH_SHORT).show();
            String username = bundle.getString("username");
            String password = bundle.getString("password");
            editTextUsername.setText(username);
            editTextPassword.setText(password);
        }
        btnSignUp.setOnClickListener(view1 -> toSignUpFragment());
    }

    private void callLoginApi(String username, String password) {
        InterfaceAPI apiService = ClientCustomAPI.getClient().create(InterfaceAPI.class);
        Call<TokenModel> call = apiService.getToken("openremote", username, password, "password");

        call.enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                if (response.isSuccessful()) {
                    TokenModel loginResponse = response.body();
                    String accessToken = loginResponse.getAccessToken();

                    // Sử dụng accessToken ở đây
                    // Ví dụ: Lưu vào SharedPreferences, gửi đến server khác, vv.
                    ClientAPI.setToken(accessToken);

                    tokenManager.login(
                            loginResponse.getAccessToken(),
                            loginResponse.getRefreshToken(),
                            loginResponse.getExpire(),
                            loginResponse.getRefreshExpire()
                    );
                    // Chuyển đến màn hình Main
                    Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), ContainerActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), "Login fail", Toast.LENGTH_SHORT).show();
                editTextPassword.setText("");
                editTextUsername.setText("");
            }
        });
    }

    private void toSignUpFragment() {
        FragmentManager fmgr = Objects.requireNonNull(getActivity()).getSupportFragmentManager();

        SignUpFragment loadingFragment = new SignUpFragment();

        FragmentTransaction ft = fmgr.beginTransaction();

        ft.replace(R.id.container_body, loadingFragment);

        ft.commit();
    }
}