package com.rastargame.rick.read.mvp.contract;

import com.rastargame.rick.read.base.BasePresenter;
import com.rastargame.rick.read.base.BaseView;

public interface SplashContract {
    interface Presenter extends BasePresenter<View>{
        void getSplash(String deviceId);
    }

    interface View extends BaseView {
       void showBackground(String imgUrl);
    }
}
