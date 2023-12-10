package com.example.sampleproject.model;

import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("id")
    public String id;
    @SerializedName("realm")
    public String realm;
    @SerializedName("realmId")
    public String realmId;
    @SerializedName("firstName")
    public String firstName;
    @SerializedName("lastName")
    public String lastName;
    @SerializedName("email")
    public String email;
    @SerializedName("enabled")
    public boolean enabled;
    @SerializedName("createdOn")
    public Long createdOn;
    @SerializedName("serviceAccount")
    public boolean serviceAccount;
    @SerializedName("username")
    public String username;
}
