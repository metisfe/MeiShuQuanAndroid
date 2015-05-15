package com.metis.meishuquan.util;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;

/**
 * Created by wangjin on 15/5/15.
 */
public class Helper {
    private static Animation animation;
    private static Helper helper = null;
    private static Context context;

    private Helper(Context context) {
        this.context = context;
    }

    public static Helper getInstance(Context context) {
        if (helper == null) {
            helper = new Helper(context);
        }
        return helper;
    }

    //点赞加1效果
    public void supportOrStep(TextView tvCount, final TextView tvAddOne, ImageView img, int count, boolean isSupport) {
        animation = AnimationUtils.loadAnimation(MainApplication.UIContext, R.anim.support_add_one);

        tvAddOne.setVisibility(View.VISIBLE);
        tvAddOne.startAnimation(animation);
        int addCount = count + 1;
        tvCount.setText("(" + addCount + ")");
        tvCount.setTag(count + 1);
        tvCount.setTextColor(Color.RED);
        if (isSupport) {
            img.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_support));
        } else {
            img.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_step));
        }

        new Handler().postDelayed(new Runnable() {
            public void run() {
                tvAddOne.setVisibility(View.GONE);
            }
        }, 500);
    }
}
