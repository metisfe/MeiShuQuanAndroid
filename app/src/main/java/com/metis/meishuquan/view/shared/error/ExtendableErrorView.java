package com.metis.meishuquan.view.shared.error;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

/**
 * Created by wudi on 3/15/2015.
 */

public abstract class ExtendableErrorView extends BaseErrorView
{
    protected ViewGroup headerGroup;

    public ExtendableErrorView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public ExtendableErrorView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ExtendableErrorView(Context context)
    {
        super(context);
    }

    protected abstract void inflateHeader();

    public void addHeaderView(View view)
    {
        if (view == null)
        {
            return;
        }

        if (view.getParent() != null)
        {
            if (view.getParent() == this.headerGroup)
            {
                return;
            }

            if (view.getParent() instanceof AdapterView<?>)
            {
                ((AdapterView<?>) view.getParent()).removeViewInLayout(view);
            }
            else
            {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }

        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        this.headerGroup.addView(view);
    }

    @Override
    public void clear()
    {
        if (this.headerGroup.getChildCount() == 0)
        {
            return;
        }

        this.headerGroup.removeAllViews();
    }

}
