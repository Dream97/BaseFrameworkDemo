package com.rastargame.rick.read.mvp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rastargame.rick.read.R;
import com.rastargame.rick.read.mvp.view.widget.ClassifyGridView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 图书分类页面
 * Author: Rick
 * Email: zhiyuanfeng@rastar.com
 * Date: 2019/1/15
 */
public class ClassifyGVAdapter extends BaseAdapter{
    private String[] mClassifyNames = new String[] {
        "科幻","修仙","神话","科普","教育","少儿","校园","青春","历史","科技"
    };
    private Context mContext;

    public ClassifyGVAdapter(Context context) {
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return mClassifyNames.length;
    }

    @Override
    public String getItem(int position) {
        return mClassifyNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_classify_cell, parent, false);
        MyHolder myHolder = new MyHolder(view);
        myHolder.mTvCellName.setText(mClassifyNames[position]);
        return view;
    }

    class MyHolder {
        @BindView(R.id.tv_classify_cell_name)
        TextView mTvCellName;
        public MyHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
