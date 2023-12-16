package com.example.sampleproject.model;

import com.google.gson.annotations.SerializedName;

public class TokenModel {
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
    public TokenModel(String accessToken, int expire, int refreshExpire, String refreshToken, String tokenType, int notBeforePolicy, String sessionState, String scope)
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public int getRefreshExpire() {
        return refreshExpire;
    }

    public void setRefreshExpire(int refreshExpire) {
        this.refreshExpire = refreshExpire;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getNotBeforePolicy() {
        return notBeforePolicy;
    }

    public void setNotBeforePolicy(int notBeforePolicy) {
        this.notBeforePolicy = notBeforePolicy;
    }

    public String getSessionState() {
        return sessionState;
    }

    public void setSessionState(String sessionState) {
        this.sessionState = sessionState;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
