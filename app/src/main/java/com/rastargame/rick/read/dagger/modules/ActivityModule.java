package com.rastargame.rick.read.dagger.modules;

import android.app.Activity;

import dagger.Module;

@Module
public class ActivityModule {
    Activity mActivity;
    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }
}
