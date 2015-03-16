package com.metis.meishuquan.adapter.timeline;

import android.content.Context;
import android.view.View;

/**
 * Created by wudi on 3/15/2015.
 */
public abstract class SingleHeaderTimelineAdapter extends ExtendableTimelineAdapter
{
    public SingleHeaderTimelineAdapter(Context context, boolean isProfileTimeline)
    {
        super(context, isProfileTimeline);
    }

    @Override
    protected int getHeaderItemCount()
    {
        return 1;
    }

    @Override
    protected int getHeaderViewType(int position)
    {
        return 0;
    }

    @Override
    protected int getHeaderViewTypeCount()
    {
        return 1;
    }

    @Override
    protected long getHeaderItemId(int position)
    {
        return position + (this.data != null ? this.data.getCurrentSize() : 0);
    }

    public abstract View getHeaderView();
}
