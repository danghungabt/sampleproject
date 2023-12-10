package com.example.sampleproject.model;

import com.google.gson.annotations.SerializedName;

public class UserRealmRoleModel {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("description")
    public String description;
    @SerializedName("composite")
    public boolean composite;
    @SerializedName("assigned")
    public boolean assigned;
}
