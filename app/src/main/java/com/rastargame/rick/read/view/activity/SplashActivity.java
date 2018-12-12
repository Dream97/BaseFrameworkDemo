package com.rastargame.rick.read.view.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.rastargame.rick.read.R;
import com.rastargame.rick.read.base.MvpBaseActivity;
import com.rastargame.rick.read.presenter.SplashContract;
import com.rastargame.rick.read.presenter.SplashPresenter;
import com.rastargame.rick.read.utils.AppUtils;
import com.rastargame.rick.read.utils.ImageLoader;
import com.rastargame.rick.read.view.widget.FixedImageView;

import butterknife.BindView;

public class SplashActivity extends MvpBaseActivity<SplashPresenter> implements SplashContract.View {
    @BindView(R.id.splash_img)
    FixedImageView mFixedImageView;

    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void init() {
        applyPermission();
//        presenter.getSplash(AppUtils.getDeviceId(this));
    }

    public void applyPermission(){
        if(Build.VERSION.SDK_INT>=23){
            //检查是否已经给了权限
            boolean flag = true;
            for (int i = 0; i<needPermissions.length; i++) {
                int checkpermission= ContextCompat.checkSelfPermission(getApplicationContext(),needPermissions[i]);
                if(checkpermission!=PackageManager.PERMISSION_GRANTED){//没有给权限
                    //参数分别是当前活动，权限字符串数组，requestcode
                    ActivityCompat.requestPermissions(this,needPermissions,1);
                    flag = false;
                    break;
                }
            }
            if (flag) {
                presenter.getSplash(AppUtils.getDeviceId(this));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //grantResults数组与权限字符串数组对应，里面存放权限申请结果
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"已授权",Toast.LENGTH_SHORT).show();
            presenter.getSplash(AppUtils.getDeviceId(this));
        }else{
            Toast.makeText(this,"拒绝授权",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getLayout() {
        return R.layout.activity_splash;
    }


    @Override
    public void showErrorMsg(String msg) {
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showBackground(String imgUrl) {
        ImageLoader.displayImage(this, imgUrl, mFixedImageView);
        animWelcomeImage();
    }
    private void animWelcomeImage() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFixedImageView, "translationX", -100F);
        animator.setDuration(1500L).start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
