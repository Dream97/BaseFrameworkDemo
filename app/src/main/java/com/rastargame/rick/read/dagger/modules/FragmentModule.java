package com.rastargame.rick.read.dagger.modules;

import android.support.v4.app.Fragment;

import com.rastargame.rick.read.base.BasePresenter;
import com.rastargame.rick.read.base.MvpBaseFragment;

import dagger.Module;

@Module
public class FragmentModule {
    Fragment mFragment;
    public FragmentModule(Fragment fragment) {
        this.mFragment = fragment;
    }
}
