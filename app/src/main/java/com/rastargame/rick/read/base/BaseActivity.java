package com.rastargame.rick.read.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity{

    public static final String TAG = BaseActivity.class.getSimpleName();

    public abstract void init();

    public abstract int getLayout();

    private long mExitTime;//记录是否在两秒内

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);//ButterKnife绑定组件
        onCreateView();
        init();
    }

    public void onCreateView() {
    }


    /**
     * 退出软件设计
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK&& event.getAction() == KeyEvent.ACTION_DOWN){
//            if((System.currentTimeMillis()-mExitTime)>2000){//第一次按返回键
//                Toast.makeText(this, "再次按返回键退出软件",Toast.LENGTH_SHORT).show();
//                mExitTime = System.currentTimeMillis();
//            }else {
//                //杀死当前进程
//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(0);
//            }
            finish();
            return true;
        }else {
            return super.onKeyDown(keyCode,event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
