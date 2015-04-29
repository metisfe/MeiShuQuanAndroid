package com.metis.meishuquan.view.shared;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.meishuquan.R;

/**
 * Created by WJ on 2015/4/9.
 */
public class TitleView extends RelativeLayout {

    private ImageView mBackIv = null, mRightIv = null;
    private TextView mTitleTv = null, mTitleRightTv = null;
    private FrameLayout mTitleContainer = null;

    private String mTitle = null, mTitleRight = null;

    private Drawable mImageRight = null;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleView);
        try {
            mTitle = typedArray.getString(R.styleable.TitleView_titleText);
            mTitleRight = typedArray.getString(R.styleable.TitleView_titleTextRight);
            mImageRight = typedArray.getDrawable(R.styleable.TitleView_titleImageRight);
        } finally {
            typedArray.recycle();
        }

        initView(context);
    }

    public void initView (Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_title, this);
        mTitleTv = (TextView)findViewById(R.id.title_tv);
        mTitleRightTv = (TextView)findViewById(R.id.title_right);
        mBackIv = (ImageView)findViewById(R.id.back);
        mRightIv = (ImageView)findViewById(R.id.image_right);
        mTitleContainer = (FrameLayout)findViewById(R.id.title_container);

        mTitleTv.setText(mTitle);
        mTitleRightTv.setText(mTitleRight);
        mRightIv.setImageDrawable(mImageRight);
    }

    public void setBackListener (OnClickListener listener) {
        mBackIv.setOnClickListener(listener);
    }

    public void setRightListener (OnClickListener listener) {
        mTitleRightTv.setOnClickListener(listener);
    }

    public void setTitleRight (String title) {
        mTitleRight = title;
        mTitleRightTv.setText(title);
    }

    public void setTitleRightVisible (int visible) {
        mTitleRightTv.setVisibility(visible);
    }

    public void setTitleText (String title) {
        mTitleTv.setText(title);
    }

    public void setCenterView (View view) {
        mTitleContainer.addView(view);
    }

    public void setImageRightResource (int resource) {
        setImageRightDrawable(getContext().getDrawable(resource));
    }

    public void setImageRightDrawable (Drawable drawable) {
        mImageRight = drawable;
        mRightIv.setImageDrawable(drawable);
    }

    public void setImageRightVisible (int visible) {
        mRightIv.setVisibility(visible);
    }
}
