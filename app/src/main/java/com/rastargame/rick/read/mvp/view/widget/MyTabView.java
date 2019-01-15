package com.rastargame.rick.read.mvp.view.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rastargame.rick.read.R;

public class MyTabView extends RelativeLayout {
    private ImageView mIvTab;
    private TextView mTvTab;
    private Context mContext;
    public MyTabView(Context context, int layoutId) {
        super(context);
        this.mContext = context;
        LayoutInflater.from(context).inflate(layoutId, this);
//        this.inflate(context, layoutId, null);
    }

    public void addImageView(int resId) {
        mIvTab = this.findViewById(R.id.tabwidget_item_image);
        mIvTab.setImageResource(resId);
    }

    public void addTextView(int resId) {
        mTvTab = this.findViewById(R.id.tabwidget_item_text);
        mTvTab.setText(resId);
    }
}
