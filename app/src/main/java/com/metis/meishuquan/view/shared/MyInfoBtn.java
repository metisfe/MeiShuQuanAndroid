package com.metis.meishuquan.view.shared;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.meishuquan.R;

/**
 * Created by WJ on 2015/4/7.
 */
public class MyInfoBtn extends RelativeLayout {

    private ImageView mIv = null;
    private TextView mTv = null;
    private TextView mSecondaryTv = null;
    private ImageView mArrowIv = null;
    private TextView mTipTv = null;

    private Drawable mSrcDrawable = null;
    private String mText = null;
    private CharSequence mSecondaryText = null;

    private boolean mImageVisible = true;
    private boolean mSecondaryTextVisible = true;
    private boolean mArrowVisible = true;
    private Drawable mArrowDrawable = null;

    public MyInfoBtn(Context context) {
        this(context, null);
    }

    public MyInfoBtn(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyInfoBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typed = context.obtainStyledAttributes(attrs, R.styleable.MyInfoBtn);
        try {
            mSrcDrawable = typed.getDrawable(R.styleable.MyInfoBtn_src);
            mText = typed.getString(R.styleable.MyInfoBtn_text);
            mSecondaryText = typed.getString(R.styleable.MyInfoBtn_secondaryText);
            mImageVisible = typed.getBoolean(R.styleable.MyInfoBtn_imageVisible, true);
            mSecondaryTextVisible = typed.getBoolean(R.styleable.MyInfoBtn_secondaryTextVisible, true);
            mArrowVisible = typed.getBoolean(R.styleable.MyInfoBtn_arrowVisible, true);
            mArrowDrawable = typed.getDrawable(R.styleable.MyInfoBtn_arrowImage);

        } finally {
            typed.recycle();
        }

        init(context);
    }

    private void init (Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_my_info_btn, this);
        mIv = (ImageView)this.findViewById(R.id.my_info_btn_img);
        mTv = (TextView)this.findViewById(R.id.my_info_btn_text);
        mSecondaryTv = (TextView)this.findViewById(R.id.my_info_btn_secondary_text);
        mArrowIv = (ImageView)this.findViewById(R.id.my_info_btn_arrow);
        mTipTv = (TextView)this.findViewById(R.id.my_info_tip);

        mIv.setImageDrawable(mSrcDrawable);
        mTv.setText(mText);
        mSecondaryTv.setText(mSecondaryText);

        setImageVisible(mImageVisible);
        mSecondaryTv.setVisibility(mSecondaryTextVisible ? View.VISIBLE : View.GONE);
        mArrowIv.setVisibility(mArrowVisible ? View.VISIBLE : View.INVISIBLE);
        if (mArrowDrawable != null) {
            setArrowDrawable(mArrowDrawable);
        }
    }

    public void setArrowImageResource (int resource) {
        mArrowDrawable = getResources().getDrawable(resource);
        mArrowIv.setImageDrawable(mArrowDrawable);
    }

    public void setArrowDrawable (Drawable drawable) {
        mArrowDrawable = drawable;
        mArrowIv.setImageDrawable(mArrowDrawable);
    }

    public void setArrowRotation (float rotation) {
        mArrowIv.setRotation(rotation);
    }

    public void setText (int stringId) {
        setText(getContext().getString(stringId));
    }

    public void setText (String text) {
        mText = text;
        mTv.setText(mText);
    }

    public void setSecondaryText (CharSequence txt) {
        mSecondaryText = txt;
        mSecondaryTv.setText(txt);
    }

    public void setTipTvVisible (int visible) {
        mTipTv.setVisibility(visible);
    }

    public void setTipText (CharSequence cs) {
        mTipTv.setText(cs);
    }

    public CharSequence getSecondaryText () {
        return mSecondaryTv.getText();
    }

    public CharSequence getText () {
        return mTv.getText();
    }

    public void setArrowVisible (int visible) {
        mArrowIv.setVisibility(visible);
    }

    public void setImageVisible (boolean visible) {
        mImageVisible = visible;
        mIv.setVisibility(mImageVisible ? View.VISIBLE : View.GONE);
    }
}
