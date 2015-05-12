package com.metis.meishuquan.fragment.Topline;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.topline.ToplineCustomAdapter;
import com.metis.meishuquan.model.BLL.ActiveOperator;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.ActiveInfo;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.topline.News;
import com.metis.meishuquan.model.topline.ToplineNewsList;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.ImageUtil;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.view.shared.DragListView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment:TopBar Fragment
 * <p/>
 * Created by wj on 15/3/17.
 */
public class ItemFragment extends Fragment {

    private static final int activityId = -1;
    private boolean isShowActivity;


    private DragListView listView;
    private List<News> list = new ArrayList<News>();
    private ImageView imgAct;//活动
    private int channelId = -1;
    private TopLineOperator operator;

    private ToplineCustomAdapter toplineAdapter;
    private ActiveInfo activeInfo = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //接收频道ID
        Bundle mBundle = this.getArguments();
        if (mBundle != null) {
            channelId = mBundle.getInt("channelId");
        }

        //初始化列表数据
        initData();

        View contextView = inflater.inflate(R.layout.fragment_topline_topbar_list, container, false);

        //初始化
        initView(contextView);

        if (channelId == 17) {
            View headerView = inflater.inflate(R.layout.view_act_topline, null, false);
            getActiveInfo(headerView);//获取活动详情
        }

        //初始化事件
        initEvent();
        return contextView;
    }

    private void getActiveInfo(final View headerView) {
        ActiveOperator.getInstance().getActiveDetail(new UserInfoOperator.OnGetListener<ActiveInfo>() {
            @Override
            public void onGet(boolean succeed, ActiveInfo info) {
                if (succeed) {
                    activeInfo = info;
                    initHeaderView(headerView);
                    ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(activeInfo.getImage(), imgAct);
                    listView.addHeaderView(headerView);
                }
            }
        });
    }

    private void initHeaderView(View headerView) {
        this.imgAct = (ImageView) headerView.findViewById(R.id.id_img_active);

        this.imgAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:进入活动详情
                Toast.makeText(MainApplication.UIContext, "进入活动详情", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化
     *
     * @param contextView
     */
    private void initView(View contextView) {
        //view
        this.listView = (DragListView) contextView.findViewById(R.id.listview_topbar_fragment);

        //初始化成员
        toplineAdapter = new ToplineCustomAdapter(getActivity(), list);
        this.listView.setAdapter(toplineAdapter);
    }

    private void initEvent() {
        this.listView.setOnRefreshListener(new DragListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(0, DragListView.REFRESH);
            }
        });

        this.listView.setOnLoadListener(new DragListView.OnLoadListener() {
            @Override
            public void onLoad() {
                if (channelId != -1) {
                    getData(list.get(list.size() - 1).getNewsId(), DragListView.LOAD);
                }
            }
        });
    }

    private void initData() {
        getData(0, DragListView.REFRESH);
    }

    //加载更多
    public void getData(final int lastNewsId, final int what) {
        operator = TopLineOperator.getInstance();
        operator.getNewsListByChannelId(new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    ToplineNewsList data = gson.fromJson(json, new TypeToken<ToplineNewsList>() {
                    }.getType());

                    if (what == DragListView.REFRESH) {
                        listView.onRefreshComplete();
                        list.clear();
                        list.addAll(data.getData());
                    } else if (what == DragListView.LOAD) {
                        listView.onLoadComplete();
                        list.addAll(data.getData());
                    }
                    listView.setResultSize(data.getData().size());
                    toplineAdapter.notifyDataSetChanged();
                }
            }
        }, channelId, lastNewsId);
    }
}
