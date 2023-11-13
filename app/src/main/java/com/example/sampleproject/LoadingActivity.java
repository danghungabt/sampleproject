package com.example.sampleproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sampleproject.api.ClientCustomAPI;
import com.example.sampleproject.api.InterfaceAPI;
import com.example.sampleproject.model.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private WebView webView;
//    private TokenService tokenService;
    private InterfaceAPI apiInterface;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;

    @SuppressLint({"MissingInflatedId", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        initiate();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains("openid-connect/auth"))
                {
                    String script = "javascript:window.open(document.querySelector('a').href);";
                    view.loadUrl(script);
                }

                if (url.contains("login-actions/registration"))
                {
                    String errorScript = "document.getElementsByClassName('helper-text')[0].getAttribute('data-error');";
                    view.evaluateJavascript(errorScript, err -> {
                        if (err.equals("null"))
                        {
                            String script = "javascript:document.getElementById('username').value = '" + username + "';" +
                                    "document.getElementById('email').value = '" + email + "';" +
                                    "document.getElementById('password').value = '" + password + "';" +
                                    "document.getElementById('password-confirm').value = '" + confirmPassword + "';" +
                                    "document.getElementById('kc-register-form').submit();";
                            view.evaluateJavascript(script,null);
                        }
                        else
                        {
                            Log.d("register error: ", err);
                        }
                    });
                }

                if (url.contains("manager"))
                {
                    accessHomeScreen();
                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });

        webView.loadUrl("https://uiot.ixxc.dev/");
    }

    private void initiate()
    {
        progressBar = findViewById(R.id.progressBar);
        webView = new WebView(getBaseContext());
        CookieManager.getInstance().removeAllCookies(null);
//        apiInterface = ClientAPI.getClient().create(InterfaceAPI.class);
        apiInterface = ClientCustomAPI.getClient().create(InterfaceAPI.class);

        Intent register = getIntent();
        Bundle bundle = register.getBundleExtra("register");
        username = bundle.getString("username");
        email = bundle.getString("email");
        password = bundle.getString("password");
        confirmPassword = bundle.getString("confirmPassword");
    }

    private void accessHomeScreen()
    {
        Call<Token> call = apiInterface.getToken("openremote", username, password, "password");
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful())
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    bundle.putString("password", password);

                    Intent login = new Intent(getApplicationContext(), HomePageActivity.class);
                    login.putExtra("login", bundle);
                    startActivity(login);
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.d("Register api error: ", t.toString());
            }
        });
    }
}