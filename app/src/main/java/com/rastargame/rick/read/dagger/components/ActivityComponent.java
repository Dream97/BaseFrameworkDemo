package com.rastargame.rick.read.dagger.components;

import android.app.Activity;

import com.rastargame.rick.read.dagger.modules.ActivityModule;
import com.rastargame.rick.read.dagger.scope.ActivityScope;
import com.rastargame.rick.read.view.activity.SplashActivity;

import dagger.Component;
@ActivityScope
@Component(dependencies = AppComponent.class,modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(SplashActivity splashActivity);
}
