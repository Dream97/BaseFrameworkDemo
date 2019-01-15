package com.rastargame.rick.read.mvp.view.activity;

import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
import com.rastargame.rick.read.R;
import com.rastargame.rick.read.base.BaseActivity;
import com.rastargame.rick.read.mvp.view.fragment.HomeFragment;
import com.rastargame.rick.read.mvp.view.fragment.LibraryFragment;
import com.rastargame.rick.read.mvp.view.fragment.MeFragment;
import com.rastargame.rick.read.mvp.view.widget.MyTabView;

/**
 * Author: Rick
 * Email: zhiyuanfeng@rastar.com
 * Date: 2019/1/7
 */
public class Mainactivity extends BaseActivity {
    private FragmentTabHost mTabHost;

    private int[] mTabImage = new int[] {
            R.drawable.selector_tabitem_home,
            R.drawable.selector_tabitem_book,
            R.drawable.selector_tabitem_me
    };
    private int[] mTabText = new int[] {
            R.string.tab_home,
            R.string.tab_library,
            R.string.tab_me
    };
    private Class<?>[] mTabClass = new Class<?>[] {
            HomeFragment.class,
            LibraryFragment.class,
            MeFragment.class
    };
    @Override
    public void init() {
        initView();
        initTab();
    }

    /**
     * 初始化tab
     */
    private void initTab() {
        mTabHost = findViewById(android.R.id.tabhost);
        mTabHost.setup(this,
                getSupportFragmentManager(),
                R.id.frame_content
        );
        //去掉分割线
//        mTabHost.getTabWidget().setDividerDrawable(null);
        for (int i = 0; i < mTabImage.length; i++){

            //添加资源
            MyTabView myTabView = new MyTabView(this,R.layout.tab_item);
            myTabView.addImageView(mTabImage[i]);
            myTabView.addTextView(mTabText[i]);
            TabSpec tabSpec = mTabHost.newTabSpec(getString(mTabText[i]))
                    .setIndicator(myTabView);
            //添加下标和Fragment
            mTabHost.addTab(tabSpec, mTabClass[i], null);
//            mTabHost.getTabWidget().getChildTabViewAt(i).setBackground();
        }
    }

    /**
     * 获取资源
     * @param index
     * @return
     */
    private View getView(int index) {
        View view = getLayoutInflater().inflate(R.layout.tab_item,null);
        ImageView imageView = view.findViewById(R.id.tabwidget_item_image);
        imageView.setImageResource(mTabImage[index]);
        TextView textView = view.findViewById(R.id.tabwidget_item_text);
        textView.setText(getString(mTabText[index]));
        return view;
    }
    private void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }
}
