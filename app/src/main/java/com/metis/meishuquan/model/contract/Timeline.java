package com.metis.meishuquan.model.contract;

import com.metis.meishuquan.util.ContractUtility;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wudi on 3/15/2015.
 */

public final class Timeline extends ContractBase<Moment>
{
    private static final long serialVersionUID = 1L;

    private static String IdKey = "Id";
    private static String DefaultMomentListKey = "TimelineItemList";

    public static Timeline create(Moment moment)
    {
        if (moment == null)
        {
            return null;
        }

        Timeline timeline = new Timeline();
        timeline.items = new ArrayList<Moment>();
        timeline.items.add(moment);
        timeline.previousSize = 0;
        timeline.pagination = Pagination.defaultValue();
        return timeline;
    }

    public static Timeline parse(JSONObject jsonObject)
    {
        return parse(jsonObject, DefaultMomentListKey);
    }

    public static Timeline parse(JSONObject jsonObject, String momentListKey)
    {
        if (jsonObject == null || momentListKey == null || momentListKey.length() == 0)
        {
            return null;
        }

        Timeline timeline = new Timeline();
        timeline.timelineItemListKey = momentListKey;
        String id = ContractUtility.getString(jsonObject, IdKey, null);
        timeline.init(jsonObject, timeline.timelineItemListKey, new ContractUtility.ArrayItemParser<Moment>()
        {
            @Override
            public Moment parse(JSONObject jsonObject)
            {
                return Moment.parse(jsonObject);
            }
        });

        return timeline;
    }

    private String timelineItemListKey;

    private Timeline()
    {
    }

    public void updateSocialCount(Timeline timeline)
    {
        if (timeline == null || timeline.getCurrentSize() == 0)
        {
            return;
        }

        for (int i = 0, j = this.getCurrentSize(); i < j; i++)
        {
            Moment moment = this.getItem(i);
            if (moment == null)
            {
                continue;
            }

            for (int k = 0, l = timeline.getCurrentSize(); k < l; k++)
            {
                Moment target = this.getItem(i);
                if (target == null || !target.equals(moment))
                {
                    continue;
                }

                moment.LikeCount = Math.max(moment.LikeCount, target.LikeCount);
                moment.ShareCount = Math.max(moment.ShareCount, target.ShareCount);
                moment.CommentCount = Math.max(moment.CommentCount, target.CommentCount);

                break;
            }
        }
    }

    @Override
    public Timeline loadMore(JSONObject jsonObject)
    {
        if (jsonObject == null)
        {
            return this;
        }

        this.update(jsonObject, this.timelineItemListKey, new ContractUtility.ArrayItemParser<Moment>()
        {
            @Override
            public Moment parse(JSONObject jsonObject)
            {
                return Moment.parse(jsonObject);
            }
        });

        return this;
    }
}