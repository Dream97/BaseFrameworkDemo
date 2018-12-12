package com.rastargame.rick.read.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RxPresenter<T extends BaseView> implements BasePresenter<T>{
    public T view;
    private CompositeDisposable compositeDisposable;

    private void unSubscribe(){
        if(compositeDisposable!=null){
            compositeDisposable.clear();
        }
    }
    protected void addSubscribe(Disposable subscription){
        if(compositeDisposable == null){
            compositeDisposable = new CompositeDisposable();
        }

        compositeDisposable.add(subscription);
    }


    /**
     * Presenter 与 View 建立连接
     * @param view
     */
    @Override
    public void attachView(T view) {
        this.view = view;
    }

    /**
     * Presenter 与 View 连接断开
     * 当对应的页面不存在时，在Callback的中调用页面的方法会报空指针异常
     */
    @Override
    public void detachView() {
        this.view = null;
    }
}
