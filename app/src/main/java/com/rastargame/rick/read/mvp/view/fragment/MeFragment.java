package com.rastargame.rick.read.mvp.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rastargame.rick.read.R;
import com.rastargame.rick.read.base.MvpBaseFragment;
import com.rastargame.rick.read.mvp.contract.MeContract;
import com.rastargame.rick.read.mvp.presenter.MePresenter;

/**
 * Author: Rick
 * Email: zhiyuanfeng@rastar.com
 * Date: 2019/1/7
 */
public class MeFragment extends MvpBaseFragment<MePresenter> implements MeContract.View {
    @Override
    protected void setInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void init() {

    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void showErrorMsg(String msg) {

    }
}
