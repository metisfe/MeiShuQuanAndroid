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

    private Drawable mSrcDrawable = null;
    private String mText = null;
    private String mSecondaryText = null;

    private boolean mImageVisible = true;
    private boolean mSecondaryTextVisible = true;

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

        mIv.setImageDrawable(mSrcDrawable);
        mTv.setText(mText);
        mSecondaryTv.setText(mSecondaryText);

        mIv.setVisibility(mImageVisible ? View.VISIBLE : View.GONE);
        mSecondaryTv.setVisibility(mSecondaryTextVisible ? View.VISIBLE : View.GONE);
    }

    public void setSecondaryText (String txt) {
        mSecondaryText = txt;
        mSecondaryTv.setText(txt);
    }

    public CharSequence getSecondaryText () {
        return mSecondaryTv.getText();
    }
}