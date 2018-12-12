package com.rastargame.rick.read.app;

import android.app.Application;

import com.rastargame.rick.read.dagger.components.AppComponent;
import com.rastargame.rick.read.dagger.components.DaggerAppComponent;
import com.rastargame.rick.read.dagger.modules.AppModule;


public class MyApplication extends Application {
    private static MyApplication mMyApplication;
    private static AppComponent mAppComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        if (mMyApplication == null) {
            mMyApplication = this;
        }
    }

    public static MyApplication getInstance(){
        if(mMyApplication == null){
            return new MyApplication();
        }else {
            return mMyApplication;
        }
    }
    public static AppComponent getAppComponent() {
        if(mAppComponent == null) {
            mAppComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(mMyApplication))
                    .build();
        }
        return mAppComponent;
    }

}
