package com.metis.meishuquan.adapter.timeline;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wudi on 3/15/2015.
 */

public abstract class ExtendableTimelineAdapter extends TimelineAdapter
{
    public ExtendableTimelineAdapter(Context context, boolean isProfileTimeline)
    {
        super(context, isProfileTimeline);
    }

    @Override
    public int getCount()
    {
        return super.getCount() + this.getHeaderItemCount();
    }

    protected abstract int getHeaderItemCount();

    public int getItemViewType(int position)
    {
        int headerItemCount = this.getHeaderItemCount();
        if (position < headerItemCount)
        {
            return super.getViewTypeCount() + this.getHeaderViewType(position);
        }

        return super.getItemViewType(position - headerItemCount);
    }

    protected abstract int getHeaderViewType(int position);

    @Override
    public int getViewTypeCount()
    {
        return super.getViewTypeCount() + this.getHeaderViewTypeCount();
    }

    protected abstract int getHeaderViewTypeCount();

    @Override
    public long getItemId(int position)
    {
        int headerItemCount = this.getHeaderItemCount();
        if (position < headerItemCount)
        {
            return this.getHeaderItemId(position);
        }

        return super.getItemId(position - headerItemCount);
    }

    protected abstract long getHeaderItemId(int position);

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        int headerItemCount = this.getHeaderItemCount();
        if (position < headerItemCount)
        {
            return this.getHeaderView(position, convertView, parent);
        }
        else
        {
            return super.getView(position - headerItemCount, convertView, parent);
        }
    }

    protected abstract View getHeaderView(int position, View convertView, ViewGroup parent);
}
