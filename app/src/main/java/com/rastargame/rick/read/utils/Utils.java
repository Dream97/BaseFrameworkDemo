package com.rastargame.rick.read.utils;
import android.content.Context;
import android.net.ConnectivityManager;
import com.rastargame.rick.read.app.MyApplication;


public class Utils {
    public static boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getInstance().
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }
}

