package com.example.sampleproject.model;

import com.google.gson.annotations.SerializedName;

public class Token {
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("expires_in")
    private int expire;
    @SerializedName("refresh_expires_in")
    private int refreshExpire;
    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("not-before-policy")
    private int notBeforePolicy;
    @SerializedName("session_state")
    private String sessionState;
    @SerializedName("scope")
    private String scope;
    public Token(String accessToken, int expire, int refreshExpire, String refreshToken, String tokenType, int notBeforePolicy, String sessionState, String scope)
    {
        this.accessToken = accessToken;
        this.expire = expire;
        this.refreshExpire = refreshExpire;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.notBeforePolicy = notBeforePolicy;
        this.sessionState = sessionState;
        this.scope = scope;
    }

    public String getAccessToken()
    {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }
}
