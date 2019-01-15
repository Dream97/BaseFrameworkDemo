package com.rastargame.rick.read.mvp.presenter;

import com.rastargame.rick.read.base.RxPresenter;
import com.rastargame.rick.read.model.http.DataManagerModel;
import com.rastargame.rick.read.mvp.contract.HomeContract;

import javax.inject.Inject;

/**
 * Author: Rick
 * Email: zhiyuanfeng@rastar.com
 * Date: 2019/1/7
 */
public class HomePresenter extends RxPresenter<HomeContract.View> implements HomeContract.Presenter {
    @Inject
    public HomePresenter(DataManagerModel dataManagerModel) {

    }
}
