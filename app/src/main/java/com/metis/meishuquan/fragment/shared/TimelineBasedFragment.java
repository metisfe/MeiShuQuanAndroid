package com.metis.meishuquan.fragment.shared;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.shared.ModelBaseAdapter;
import com.metis.meishuquan.adapter.timeline.TimelineAdapter;
import com.metis.meishuquan.model.contract.Timeline;
import com.metis.meishuquan.model.provider.ProviderRequestHandler;
import com.metis.meishuquan.util.ContractUtility;

/**
 * Created by wudi on 3/15/2015.
 */
public abstract class TimelineBasedFragment extends RefreshLoadMoreListBasedFragment<Timeline>
{
    protected TimelineAdapter timelineAdapter;
    protected boolean isProfileTimeline;

    @Override
    protected ViewGroup onCreateDataView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_shared_timelinebasedfragment, container, false);
        this.isProfileTimeline = false;
        return rootView;
    }

    @Override
    protected int getRefreshLoadMoreViewId()
    {
        return R.id.fragment_shared_timelinebasedfragment_listview;
    }

    @Override
    protected void onCreateAdapter()
    {
        this.timelineAdapter = new TimelineAdapter(this.getActivity(), this.isProfileTimeline);
        this.dataList.setAdapter(this.timelineAdapter);
    }

    @Override
    protected ModelBaseAdapter<?> getAdapter()
    {
        return this.timelineAdapter;
    }

    @Override
    protected void onUpdatingData(Timeline data)
    {
        this.timelineAdapter.setData(data);
    }

    @Override
    protected boolean isDataHasMore()
    {
        return this.hasMoreWithContractBaseAdapter(this.timelineAdapter);
    }

    @Override
    protected void onLoadSucceeded(Timeline data)
    {
        if (flag == 0)
        {
            this.timelineAdapter.setPreviousData();
        }

        if (this.timelineAdapter.getData() != data && ContractUtility.equals(this.timelineAdapter.getData(), data))
        {
            Timeline currentTimeline = ((Timeline)this.timelineAdapter.getData());
            if (currentTimeline != null)
            {
                currentTimeline.updateSocialCount(data);
            }

            //"Skip timeline data, because here is no new data returned from backend");

            // setup error view
            if (data != null && data.getCurrentSize() == 0)
            {
                this.setupErrorViewWhenNoData();
                this.onUpdatingData(data);
            }
        }
        else
        {
            //"Update timeline with %d items", data != null ? data.getCurrentSize() : 0);

            // setup error view
            if (data == null || data.getCurrentSize() == 0)
            {
                this.setupErrorViewWhenNoData();
            }
            else
            {
                this.errorType = null;
            }

            this.onUpdatingData(data);
        }

        this.refreshCount = this.timelineAdapter.calculateDifferentItemCount();
        if (this.flag == 0)
        {
            super.onLoadDataCompletedWithContactBaseData(data);
        }
        else
        {
            super.onLoadSucceeded(data);
        }
    }

    @Override
    protected void onLoadFailed(ProviderRequestHandler<Timeline> handler)
    {
        //"Fail to load timeline from API");
        super.onLoadFailed(handler);
    }
}