package com.metis.meishuquan.view.shared;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.meishuquan.R;

/**
 * Created by WJ on 2015/4/9.
 */
public class TitleView extends RelativeLayout {

    private ImageView mBackIv = null;
    private TextView mTitleTv = null;

    private String mTitle = null;

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
        } finally {
            typedArray.recycle();
        }

        initView(context);
    }

    public void initView (Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_title, this);
        mTitleTv = (TextView)findViewById(R.id.title_tv);
        mBackIv = (ImageView)findViewById(R.id.back);

        mTitleTv.setText(mTitle);
    }

    public void setBackListener (OnClickListener listener) {
        mBackIv.setOnClickListener(listener);
    }
}
