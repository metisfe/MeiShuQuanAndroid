package com.metis.meishuquan.view.Common;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by wangjin on 15/5/3.
 */
public class MatchParentImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener {


    private boolean mOnce = false;

    private float mInitScale;//放大的初始值

    private float mMinScale;//放大的最小值

    private float mMaxScale;//放大的最大值

    private Matrix mScaleMatrix;

    public MatchParentImageView(Context context) {
        this(context, null);
    }

    public MatchParentImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MatchParentImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScaleMatrix = new Matrix();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
        setScaleType(ScaleType.MATRIX);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if (!mOnce) {
//得到控件的宽和高
            int width = getWidth();
            int height = getHeight();
            //得到我们的图片，以及宽和高

            Drawable d = getDrawable();
            if (d == null) {
                return;
            }

            float scale = 1.0f;
            int dw = d.getIntrinsicWidth();
            int dh = d.getIntrinsicHeight();

            //如果图片的宽度大于控件的宽度，但是高于小于控件宽度，我们将其缩小
            if (dw > width && dh < height) {
                scale = width * 1.0f / dw;
            }

            //如果图片的高度大于控件高度，但是宽度小于控件宽度，我们将其缩小
            if (dh > height && dw < width) {
                scale = height * 1.0f / dh;
            }

            //
            if ((dw > width && dh > height) || (dw < width && dh < height)) {
                scale = Math.min((width * 1.0f) / dw, height * 1.0f / dw);
            }

            //得到缩放的比例
            mInitScale = scale;
            mMaxScale = mInitScale * 4;
            mMinScale = mInitScale * 2;

            //将图片移动至当前控件的中心
            int dx = getWidth() / 2 - dw / 2;
            int dy = getHeight() / 2 - dh / 2;

            mScaleMatrix.postTranslate(dx, dy);
            mScaleMatrix.postScale(mInitScale, mInitScale, width / 2, height / 2);
            setImageMatrix(mScaleMatrix);

            mOnce = true;
        }
    }
}
