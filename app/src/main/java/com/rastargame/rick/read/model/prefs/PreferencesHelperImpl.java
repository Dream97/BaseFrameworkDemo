package com.rastargame.rick.read.model.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.rastargame.rick.read.app.MyApplication;

import javax.inject.Inject;

public class PreferencesHelperImpl implements PreferencesHelper {
    private SharedPreferences sharedPreferences;
    private static final String SP_NAME="my_sp";

    @Inject
    public PreferencesHelperImpl() {
        sharedPreferences =  MyApplication.getInstance().getSharedPreferences(SP_NAME, Context.
                MODE_PRIVATE);
    }


    @Override
    public void setToken(String token) {
        sharedPreferences.edit().putString("token",token).commit();
    }

    @Override
    public String getToken() {
        return sharedPreferences.getString("token","");
    }

}
