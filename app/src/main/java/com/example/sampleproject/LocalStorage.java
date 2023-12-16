package com.example.sampleproject;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {
    private static SharedPreferences sharedPreferences;

    public static SharedPreferences getInstance(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }
}
