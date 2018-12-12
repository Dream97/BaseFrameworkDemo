package com.rastargame.rick.read.dagger.modules;

import com.rastargame.rick.read.app.MyApplication;
import com.rastargame.rick.read.model.db.DBHelperImpl;
import com.rastargame.rick.read.model.db.DBHelper;
import com.rastargame.rick.read.model.http.DataManagerModel;
import com.rastargame.rick.read.model.http.HttpHelperImpl;
import com.rastargame.rick.read.model.http.HttpHelper;
import com.rastargame.rick.read.model.prefs.PreferencesHelper;
import com.rastargame.rick.read.model.prefs.PreferencesHelperImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private MyApplication mMyApplication;
    public AppModule(MyApplication myApplication) {
        this.mMyApplication = myApplication;
    }

    @Provides
    @Singleton
    MyApplication provideMyApplication(){
        return  mMyApplication;
    }

    @Provides
    @Singleton
    HttpHelper provideHttpHelper(HttpHelperImpl httpHelperImpl) {
        return httpHelperImpl;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(PreferencesHelperImpl preferencesHelperImpl) {
        return preferencesHelperImpl;
    }

    @Provides
    @Singleton
    DBHelper provideDBHelper(DBHelperImpl dbHelperImpl) {
        return dbHelperImpl;
    }

    @Provides
    @Singleton
    DataManagerModel provideManagerModel(HttpHelperImpl httpHelperImpl, DBHelperImpl dbHelperImpl, PreferencesHelperImpl preferencesHelperImpl) {
        return new DataManagerModel(httpHelperImpl, dbHelperImpl, preferencesHelperImpl);
    }

}
