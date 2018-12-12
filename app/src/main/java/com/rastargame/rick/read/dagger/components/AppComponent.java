package com.rastargame.rick.read.dagger.components;

import com.rastargame.rick.read.app.MyApplication;
import com.rastargame.rick.read.dagger.modules.AppModule;
import com.rastargame.rick.read.dagger.modules.HttpModule;
import com.rastargame.rick.read.model.http.DataManagerModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
//@Component
@Component(modules = {AppModule.class, HttpModule.class})
public interface AppComponent {
    MyApplication getContext();
    DataManagerModel getDataMangerModel();
}
