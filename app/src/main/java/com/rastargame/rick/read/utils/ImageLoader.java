package com.rastargame.rick.read.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.rastargame.rick.read.R;
import com.rastargame.rick.read.app.GlideApp;
import com.rastargame.rick.read.app.GlideOptions;


import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ImageLoader {

    public static void displayImage(Context context, String uri, ImageView imageView) {
        displayImage(context, uri, imageView, false, false);
    }

    public static void displayImage(Context context, String uri, ImageView imageView,
                                    boolean isCircle) {
        displayImage(context, uri, imageView, isCircle, false);
    }




    public static void displayImage(Context context, String uri, ImageView imageView, boolean
            isCircle, int defaultIconId) {
        GlideApp.with(context).asDrawable()
                .load(uri)
                .thumbnail(0.1f)
                .placeholder(defaultIconId)
                .error(defaultIconId)
                .transition(withCrossFade())
                .into(imageView);
    }


    public static void displayImage(Context context, String uri, ImageView imageView, boolean
            isCircle, boolean border) {

        if (isCircle) {
                GlideApp.with(context)
                        .load(uri)
                        .placeholder(R.mipmap.img_bg)
                        .apply(GlideOptions.circleCropTransform())
                        .into(imageView);

        } else {
            GlideApp.with(context).asDrawable()
                    .load(uri)
                    .thumbnail(0.1f)
//                    .placeholder(R.mipmap.img_bg)
                    .transition(GenericTransitionOptions.with(R.anim.item_alpha))
                    .transition(withCrossFade())
                    .into(imageView);
        }
    }
}
