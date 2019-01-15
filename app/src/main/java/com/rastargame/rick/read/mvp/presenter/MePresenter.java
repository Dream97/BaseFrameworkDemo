package com.rastargame.rick.read.mvp.presenter;

import com.rastargame.rick.read.base.RxPresenter;
import com.rastargame.rick.read.model.http.DataManagerModel;
import com.rastargame.rick.read.mvp.contract.MeContract;

import javax.inject.Inject;

/**
 * Author: Rick
 * Email: zhiyuanfeng@rastar.com
 * Date: 2019/1/7
 */
public class MePresenter extends RxPresenter<MeContract.View> implements MeContract.Presenter {
    @Inject
    public MePresenter (DataManagerModel dataManagerModel) {

    }
}
