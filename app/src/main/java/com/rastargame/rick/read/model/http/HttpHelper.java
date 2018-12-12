package com.rastargame.rick.read.model.http;

import com.rastargame.rick.read.model.entity.SplashEntity;

import io.reactivex.Flowable;

public interface HttpHelper {
    Flowable<SplashEntity> getSplash(String client, String version, Long time, String deviceId);
}
