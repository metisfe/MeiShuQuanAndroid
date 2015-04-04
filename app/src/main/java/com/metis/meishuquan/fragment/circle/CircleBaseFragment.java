package com.metis.meishuquan.fragment.circle;

import android.support.v4.app.Fragment;

import com.metis.meishuquan.view.circle.CircleTitleBar;

/**
 * Created by wudi on 4/4/2015.
 */
public abstract class CircleBaseFragment extends Fragment {
    private CircleTitleBar titleBar;
    protected CircleTitleBar getTitleBar()
    {
        return titleBar;
    }

    public void setTitleBar(CircleTitleBar titleBar)
    {
        this.titleBar = titleBar;
    }

    public abstract void timeToSetTitleBar();
}
