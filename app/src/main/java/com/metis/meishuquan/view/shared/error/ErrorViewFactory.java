package com.metis.meishuquan.view.shared.error;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by wudi on 3/15/2015.
 */

public class ErrorViewFactory
{
    private static DefaultErrorView defaultNoDataErrorView;

    private ErrorViewFactory()
    {
    }

    public static BaseErrorView getDefaultErrorView(Context context)
    {
        if (defaultNoDataErrorView == null)
        {
            defaultNoDataErrorView = new DefaultErrorView(context);
        }

        return defaultNoDataErrorView;
    }

    public static void initErrorView(AdapterView<?> adapterView, BaseErrorView.ErrorListener errorListener)
    {
        ViewGroup root = (ViewGroup) adapterView.getParent();
        if (root instanceof LinearLayout)
        {
            defaultNoDataErrorView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
        }
        else if (root instanceof RelativeLayout)
        {
            defaultNoDataErrorView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT));
        }
        else if (root instanceof FrameLayout)
        {
            defaultNoDataErrorView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT));
        }
        else
        {
            // Unsupported Layout Type
            return;
        }

        defaultNoDataErrorView.clear();
        defaultNoDataErrorView.setVisibility(View.GONE);
        defaultNoDataErrorView.setErrorListener(errorListener);

        defaultNoDataErrorView.setErrorView(adapterView);
    }
}