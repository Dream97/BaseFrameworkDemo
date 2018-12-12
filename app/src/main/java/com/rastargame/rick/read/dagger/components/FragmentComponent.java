package com.rastargame.rick.read.dagger.components;

import com.rastargame.rick.read.dagger.modules.FragmentModule;
import com.rastargame.rick.read.dagger.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = {FragmentModule.class})
public interface FragmentComponent {

}
