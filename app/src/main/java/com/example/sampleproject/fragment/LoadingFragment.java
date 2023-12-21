package com.example.sampleproject.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.sampleproject.R;
import com.example.sampleproject.api.ClientCustomAPI;
import com.example.sampleproject.api.InterfaceAPI;
import com.example.sampleproject.model.TokenModel;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoadingFragment extends Fragment {

    private ProgressBar progressBar;
    private WebView webView;
    private InterfaceAPI apiInterface;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;

    private FragmentTransaction ft;

    private boolean registered;
    private boolean toSignIn;
    public LoadingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loading, container, false);

        initiateView(view);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains("openid-connect/auth"))
                {
                    String script = "javascript:window.open(document.querySelector('a').href);";
                    view.loadUrl(script);
                    Log.d("LoadingFragment","auth" );
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
                            registered = true;
                            Log.d("LoadingFragment","registration" );
                        }
                        else
                        {
                            Log.d("LoadingFragment", "register error " + err);
                            toSignUpFragment(err);
                        }
                    });
                }

                if (url.contains("manager") && registered)
                {
                    Log.d("LoadingFragment","manager registered" );
                    if(toSignIn) {
                        accessHomeScreen();
                        Log.d("LoadingFragment","manager registered and toSignIn" );
                    }
                    toSignIn = !toSignIn;
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

        return view;
    }

    private void initiateView(View view){

        FragmentManager fmgr = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        ft = fmgr.beginTransaction();

        registered = false;
        toSignIn = false;

        progressBar = view.findViewById(R.id.progressBar);
        webView = new WebView(getActivity().getBaseContext());
//        webView = view.findViewById(R.id.webview);
        CookieManager.getInstance().removeAllCookies(null);
        apiInterface = ClientCustomAPI.getClient().create(InterfaceAPI.class);

        Bundle bundle = getArguments();
        assert bundle != null;
        username = bundle.getString("username");
        email = bundle.getString("email");
        password = bundle.getString("password");
        confirmPassword = bundle.getString("confirmPassword");
    }

    private void accessHomeScreen()
    {
        Call<TokenModel> call = apiInterface.getToken("openremote", username, password, "password");
        call.enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                if (response.isSuccessful())
                {
                    Log.d("API CALL", response.body().getAccessToken()+"");
                    toSignInFragment();
                }
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                Log.d("Register api error: ", t.toString());
            }
        });
    }

    private void toSignInFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("password", password);

        SignInFragment signInFragment = new SignInFragment();

        signInFragment.setArguments(bundle);

        ft.replace(R.id.container_body, signInFragment);

        ft.commit();
    }

    private void toSignUpFragment(String error) {
        FragmentManager fmgr = Objects.requireNonNull(getActivity()).getSupportFragmentManager();

        SignUpFragment loadingFragment = new SignUpFragment();

        Bundle bundle = new Bundle();
        bundle.putString("error", error);

        loadingFragment.setArguments(bundle);

        FragmentTransaction ft = fmgr.beginTransaction();

        ft.replace(R.id.container_body, loadingFragment);

        ft.commit();
    }

}