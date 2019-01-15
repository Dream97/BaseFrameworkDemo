package com.rastargame.rick.read.dagger.components;

import com.rastargame.rick.read.dagger.modules.FragmentModule;
import com.rastargame.rick.read.dagger.scope.FragmentScope;
import com.rastargame.rick.read.mvp.view.fragment.HomeFragment;
import com.rastargame.rick.read.mvp.view.fragment.LibraryFragment;
import com.rastargame.rick.read.mvp.view.fragment.MeFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = {FragmentModule.class})
public interface FragmentComponent {
    void inject(HomeFragment homeFragment);
    void inject(LibraryFragment libraryFragment);
    void inject(MeFragment meFragment);
}
