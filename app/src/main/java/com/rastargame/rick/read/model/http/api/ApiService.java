package com.rastargame.rick.read.model.http.api;

import com.rastargame.rick.read.model.entity.SplashEntity;


import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Create by Rick
 * DATE 2018/12/11
 */
public interface ApiService {

    @GET("static/picture_list.txt")
    Flowable<SplashEntity> getSplash(@Query("client") String client, @Query("version") String version, @Query("time") Long time, @Query("device_id") String deviceId);
}
