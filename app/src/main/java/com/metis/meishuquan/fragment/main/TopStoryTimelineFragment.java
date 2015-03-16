package com.metis.meishuquan.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.shared.ModelBaseAdapter;
import com.metis.meishuquan.adapter.timeline.TopStoryTimelineAdapter;
import com.metis.meishuquan.fragment.shared.TimelineBasedFragment;
import com.metis.meishuquan.model.contract.Timeline;
import com.metis.meishuquan.model.provider.DataProvider;
import com.metis.meishuquan.model.provider.ProviderPreference;
import com.metis.meishuquan.ui.SelectedTabType;
import com.metis.meishuquan.view.shared.TabBar;
import com.metis.meishuquan.view.shared.error.BaseErrorView;

/**
 * Created by wudi on 3/15/2015.
 */

public class TopStoryTimelineFragment extends TimelineBasedFragment
{
    private TabBar tabBar;
    //private HeadBar headBar;

    @Override
    protected ViewGroup onCreateDataView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_topstorytimelinefragment, container, false);
        //this.headBar = (HeadBar) rootView.findViewById(R.id.fragment_shared_timelinebasedfragment_headbar);
        this.tabBar = (TabBar) rootView.findViewById(R.id.fragment_shared_timelinebasedfragment_tab_bar);
        this.tabBar.setTabSelectedListener(MainApplication.MainActivity);
        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onCreateAdapter()
    {
        this.timelineAdapter = new TopStoryTimelineAdapter(this.getActivity(), false);
        this.dataList.setAdapter(this.timelineAdapter);
    }

    @Override
    protected void onRefreshData()
    {
        if (this.isFirstLoadingWithCache)
        {
            DataProvider.getInstance().getTopStoryTimeline(ProviderPreference.CacheOnly, null, this.requestHandler);
        }
        else
        {
            //w("Invoke TopStoryFragment.onRefreshData for PreferAPI Call");
            ((TopStoryTimelineAdapter) this.timelineAdapter).onRefreshData();
            DataProvider.getInstance().getTopStoryTimeline(null, this.requestHandler);
        }
    }

    @Override
    protected void onLoadMoreData()
    {
        DataProvider.getInstance().getTopStoryTimeline((Timeline) this.timelineAdapter.getData(), this.requestHandler);
    }

    @Override
    protected void setupErrorViewWhenNoData()
    {
        this.errorType = BaseErrorView.ErrorType.NoData;
        this.setupErrorView();
        //this.headBar.bringToFront();
        this.tabBar.bringToFront();
    }

    @Override
    protected void setupErrorViewWhenNoConnection()
    {
        super.setupErrorViewWhenNoConnection();
        //this.headBar.bringToFront();
        this.tabBar.bringToFront();
    }

    @Override
    protected ModelBaseAdapter<?> getAdapter()
    {
        return this.timelineAdapter;
    }
}