package com.rastargame.rick.read.base;

public interface BasePresenter <T extends BaseView>{
    void attachView(T view);
    void detachView();

}
