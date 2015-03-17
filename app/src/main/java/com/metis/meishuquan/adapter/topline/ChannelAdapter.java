package com.metis.meishuquan.adapter.topline;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.timeline.TimelineAdapter;
import com.metis.meishuquan.model.contract.ContractBase;
import com.metis.meishuquan.model.contract.Moment;
import com.metis.meishuquan.view.shared.RefreshLoadMoreListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wudi on 3/15/2015.
 */
public class ChannelAdapter extends PagerAdapter {
    private Context context;

    public ChannelAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        final RefreshLoadMoreListView listView = new RefreshLoadMoreListView(context);
//        final ToplineAdapter adapter = new ToplineAdapter(context);
//        listView.setAdapter(adapter);
//        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        listView.setOnRefreshListener(new RefreshLoadMoreListView.OnRefreshLoadMoreListener() {
//            @Override
//            public void onRefresh() {
//                List<Moment> datas = new ArrayList<Moment>();
//                datas.add(new Moment());
//                datas.add(new Moment());
//
//                adapter.setData(ContractBase.CreateContractBase(datas));
//                //TODO:1 use real api to get data onLoadCompleted in callback
//                listView.onLoadDataCompletedWithMessage(true,"load two");
//
//            }
//
//            @Override
//            public void onLoadMore() {
//                //TODO:
//            }
//
//            @Override
//            public boolean hasMore() {
//                //TODO:
//                return false;
//            }
//        });
//
//        //TODO: use cache to firstRefresh then refresh again without cache
//        listView.firstRefresh();
//
//        container.addView(listView, 0);
//        return listView;

        TextView tv=new TextView(context);
        return tv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object != null) {
            container.removeView((View) object);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
