package com.metis.meishuquan.view.shared.error;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;

/**
 * Created by wudi on 3/15/2015.
 */

public abstract class BaseErrorView extends FrameLayout
{
    public interface ErrorListener
    {
        void OnErrorRefresh();

        void OnRecommendOption();
    }

    public enum ErrorType
    {
        NetworkNotAvailable, NoData, NoMyFollow, NoMyActivity, NoMyTimeline, NoTaImages
    }

    protected ErrorType errorType;

    protected ErrorListener errorlistener;

    public BaseErrorView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public BaseErrorView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public BaseErrorView(Context context)
    {
        super(context);
    }

    public ErrorType getErrorType()
    {
        return this.errorType;
    }

    public void setErrorListener(ErrorListener errorListener)
    {
        this.errorlistener = errorListener;
    }

    public void setErrorView(AdapterView<?> adapterView)
    {
        if (adapterView == null || adapterView.getParent() == null)
        {
            return;
        }

        if (this.getParent() != null)
        {
            if (this.getParent() == adapterView.getParent())
            {
                return;
            }
            ((ViewGroup) this.getParent()).removeView(this);
        }

        ((ViewGroup) adapterView.getParent()).addView(this);
        adapterView.setEmptyView(this);
    }

    public abstract void clear();

    public abstract void bind();

    public abstract void setErrorType(ErrorType errorType);
}
