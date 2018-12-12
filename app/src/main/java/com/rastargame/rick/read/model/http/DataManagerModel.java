package com.rastargame.rick.read.model.http;

import com.rastargame.rick.read.model.db.DBHelper;
import com.rastargame.rick.read.model.entity.SplashEntity;
import com.rastargame.rick.read.model.prefs.PreferencesHelper;

import io.reactivex.Flowable;

public class DataManagerModel implements HttpHelper, PreferencesHelper, DBHelper {
    private HttpHelper mHttpHelper;
    private DBHelper mDBHttpHelper;
    private PreferencesHelper mPreferencesHelper;

    public DataManagerModel(HttpHelper httpHelper, DBHelper dbHelper, PreferencesHelper preferencesHelper) {
        this.mHttpHelper = httpHelper;
        this.mDBHttpHelper = dbHelper;
        this.mPreferencesHelper = preferencesHelper;
    }
    @Override
    public Flowable<SplashEntity> getSplash(String client, String version, Long time, String deviceId) {
        return mHttpHelper.getSplash(client,version,time,deviceId);
    }
}
