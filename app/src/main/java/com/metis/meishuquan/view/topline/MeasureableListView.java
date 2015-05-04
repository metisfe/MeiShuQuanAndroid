package com.metis.meishuquan.view.topline;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 可以测量高度的自定义ListView
 * <p/>
 * Created by wangjin on 15/5/4.
 */
public class MeasureableListView extends ListView {
    public MeasureableListView(Context context) {
        this(context, null);
    }

    public MeasureableListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeasureableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
