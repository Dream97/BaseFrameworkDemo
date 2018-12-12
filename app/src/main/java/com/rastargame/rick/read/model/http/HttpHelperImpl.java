package com.rastargame.rick.read.model.http;

import com.rastargame.rick.read.model.entity.SplashEntity;
import com.rastargame.rick.read.model.http.api.ApiService;

import javax.inject.Inject;

import io.reactivex.Flowable;

public class HttpHelperImpl implements HttpHelper {
    private ApiService mApiService;

    @Inject
    public HttpHelperImpl(ApiService apiService) {
        this.mApiService = apiService;
    }

    @Override
    public Flowable<SplashEntity> getSplash(String client, String version, Long time, String deviceId) {
        return mApiService.getSplash(client,version,time,deviceId);
    }
}
