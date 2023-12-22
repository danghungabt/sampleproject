package com.example.sampleproject.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

public class ContextUtils extends ContextWrapper {
    public ContextUtils(Context base) {
        super(base);
    }

    public static ContextWrapper updateLocale(Context context, Locale localeToSwitch){
        Context newContext = context;
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        LocaleList localeList = new LocaleList(localeToSwitch);
        LocaleList.setDefault(localeList);
        configuration.setLocales(localeList);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1){
            newContext = context.createConfigurationContext(configuration);
        }else {
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        return new ContextUtils(newContext);
    }
}
