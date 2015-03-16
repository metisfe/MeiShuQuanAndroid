package com.metis.meishuquan.adapter.timeline;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.metis.meishuquan.adapter.shared.ContractBaseAdapter;
import com.metis.meishuquan.model.contract.Moment;
import com.metis.meishuquan.model.contract.Timeline;

/**
 * Created by wudi on 3/15/2015.
 */

public class TimelineAdapter extends ContractBaseAdapter<Moment>
{
    protected boolean isProfileTimeline;
    protected boolean isInnerViewTimeline;

    public TimelineAdapter(Context context, boolean isProfileTimeline)
    {
        super(context);
        this.isProfileTimeline = isProfileTimeline;
        this.isInnerViewTimeline = false;
    }

    @Override
    public int getItemViewType(int position)
    {
        Moment moment = (Moment) this.getItem(position);
        if (moment == null)
        {
            return 0;
        }

        switch (moment.Type)
        {
            case RepostTweet:
                return 2;
            case Tweet:
                return 1;
            case News:
            case Video:
            default:
                return 0;
        }
    }

    @Override
    public int getViewTypeCount()
    {
        // 0: for NewsVideoMomentCard
        // 1: for TweetMomentCard
        // 2: for TweetRepostMomentCard
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
//        MomentCard view = null;
//        Moment moment = (Moment) this.getItem(position);
//        if (moment == null)
//        {
//            return view;
//        }
//
//        if (convertView != null)
//        {
//            view = (MomentCard) convertView;
//        }
//        else
//        {
//            view = MomentCard.create(moment.Type, this.context, this.isProfileTimeline, this.isInnerViewTimeline);
//        }
//
//        view.setMoment((Timeline) this.data, position);
        TextView view = new TextView(this.context);
        view.setText("hello world");

        return view;
    }
}