package com.rastargame.rick.read.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;


import com.rastargame.rick.read.app.MyApplication;
import com.rastargame.rick.read.dagger.components.DaggerFragmentComponent;
import com.rastargame.rick.read.dagger.components.FragmentComponent;
import com.rastargame.rick.read.dagger.modules.FragmentModule;

import javax.inject.Inject;

public abstract class MvpBaseFragment<T extends BasePresenter> extends BaseFragment implements BaseView{
    @Inject
    public T presenter;

    protected abstract void setInject();

    public FragmentComponent getFragmentComponent(){
        return DaggerFragmentComponent.builder()
                .appComponent(MyApplication.getAppComponent())
                .fragmentModule(new FragmentModule(this))
                .build();
    }

    private FragmentModule getFragmentModule(){
        return new FragmentModule(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        setInject();
        if(presenter != null){
            presenter.attachView(this);
        }
        super.onViewCreated(view,savedInstanceState);
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        if(presenter!= null){
            presenter.detachView();
        }
    }

}
