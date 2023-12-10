package com.example.sampleproject.model;

import com.google.gson.annotations.SerializedName;

public class RealmModel {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("displayName")
    public String displayName;
    @SerializedName("enabled")
    public Boolean enabled;
    @SerializedName("notBefore")
    public Integer notBefore;
    @SerializedName("resetPasswordAllowed")
    public Boolean resetPasswordAllowed;
    @SerializedName("duplicateEmailsAllowed")
    public Boolean duplicateEmailsAllowed;
    @SerializedName("rememberMe")
    public Boolean rememberMe;
    @SerializedName("registrationAllowed")
    public Boolean registrationAllowed;
    @SerializedName("registrationEmailAsUsername")
    public Boolean registrationEmailAsUsername;
    @SerializedName("verifyEmail")
    public Boolean verifyEmail;
    @SerializedName("loginWithEmail")
    public Boolean loginWithEmail;
    @SerializedName("loginTheme")
    public String loginTheme;
    @SerializedName("accountTheme")
    public String accountTheme;
    @SerializedName("adminTheme")
    public String adminTheme;
    @SerializedName("emailTheme")
    public String emailTheme;
    @SerializedName("accessTokenLifespan")
    public Integer accessTokenLifespan;
    @SerializedName("realmRoles")
    public RealmRoleModel[] realmRoles;
}
