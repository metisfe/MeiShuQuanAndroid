package com.metis.meishuquan.adapter.timeline;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by wudi on 3/15/2015.
 */
public class TopStoryTimelineAdapter extends SingleHeaderTimelineAdapter
{
    private TextView view;

    public TopStoryTimelineAdapter(Context context, boolean isProfileTimeline)
    {
        super(context, isProfileTimeline);
    }

    @Override
    protected View getHeaderView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = new TextView(this.context);
            view = (TextView) convertView;
            view.setText("this is the promotion view");
        }

        return convertView;
    }

    public void onRefreshData()
    {
        if (this.view != null)
        {
            //this.view.onRefreshData();
        }
    }

    @Override
    public View getHeaderView()
    {
        return this.view;
    }
}
