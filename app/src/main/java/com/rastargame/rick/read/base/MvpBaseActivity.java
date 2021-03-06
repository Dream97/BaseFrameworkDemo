package com.rastargame.rick.read.base;
import com.rastargame.rick.read.app.MyApplication;
import com.rastargame.rick.read.dagger.components.ActivityComponent;
import com.rastargame.rick.read.dagger.components.DaggerActivityComponent;
import com.rastargame.rick.read.dagger.modules.ActivityModule;

import javax.inject.Inject;

public abstract class MvpBaseActivity<T extends  BasePresenter> extends BaseActivity implements BaseView{
    @Inject
    public T presenter;

    public abstract void setInject();

    public ActivityComponent getActivityComponent(){
        return DaggerActivityComponent.builder()
                .appComponent(MyApplication.getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }

    private ActivityModule getActivityModule(){
        return new ActivityModule(this);
    }



    @Override
    public void onCreateView(){
        super.onCreateView();
        setInject();
        if(presenter != null){
            presenter.attachView(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(presenter!= null){
            presenter.attachView(this);
        }
    }
}
