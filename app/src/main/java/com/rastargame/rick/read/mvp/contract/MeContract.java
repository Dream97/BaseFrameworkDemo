package com.rastargame.rick.read.mvp.contract;

import com.rastargame.rick.read.base.BasePresenter;
import com.rastargame.rick.read.base.BaseView;

/**
 * Author: Rick
 * Email: zhiyuanfeng@rastar.com
 * Date: 2019/1/7
 */
public class MeContract {
    public interface View extends BaseView {

    }

    public interface Presenter extends BasePresenter<View> {

    }
}
