package com.metis.meishuquan.fragment.Topline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import com.metis.meishuquan.activity.act.ActDetailActivity;
import com.metis.meishuquan.activity.login.LoginActivity;
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
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.umeng.analytics.MobclickAgent;

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
    private View headerView = null;
    private TopLineOperator operator;

    private ToplineCustomAdapter toplineAdapter;
    private ActiveInfo activeInfo = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //接收频道ID
        Bundle mBundle = this.getArguments();
        if (mBundle != null) {
            channelId = mBundle.getInt("channelId");
            Log.i("current_channelId", String.valueOf(channelId));
        }

        //初始化列表数据
        initData();

        View contextView = inflater.inflate(R.layout.fragment_topline_topbar_list, container, false);

        //初始化
        initView(contextView);

        //6 为推荐 17 为超级美术生
        if (channelId == 17 || channelId == 6) {
            headerView = inflater.inflate(R.layout.view_act_topline, null, false);
            getActiveInfo(headerView);//获取活动详情
        }

        //初始化事件
        initEvent();

        //初始化成员
        toplineAdapter = new ToplineCustomAdapter(getActivity(), list);
        AnimationAdapter mAnimAdapter = new AlphaInAnimationAdapter(toplineAdapter);
        mAnimAdapter.setAbsListView(listView);
        listView.setAdapter(mAnimAdapter);
//        this.listView.setAdapter(toplineAdapter);
        return contextView;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ItemFragment"); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ItemFragment");
    }

    private void getActiveInfo(final View headerView) {
        ActiveOperator.getInstance().getActiveDetail(new UserInfoOperator.OnGetListener<ActiveInfo>() {
            @Override
            public void onGet(boolean succeed, ActiveInfo info) {
                if (succeed) {
                    activeInfo = info;
                    initHeaderView(headerView);
                    listView.addHeaderView(headerView);
                    if (channelId == 17) {
                        ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(activeInfo.getImage(), imgAct);
                    } else if (channelId == 6) {
                        ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(activeInfo.getTopImage(), imgAct);
                    }
                }
            }
        });
    }

    private void initHeaderView(View headerView) {
        this.imgAct = (ImageView) headerView.findViewById(R.id.id_img_active);

        this.imgAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainApplication.isLogin()) {
                    startActivity(new Intent(getActivity(), ActDetailActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });
    }

    /**
     * 初始化
     *
     * @param contextView
     */
    private void initView(View contextView) {
        this.listView = (DragListView) contextView.findViewById(R.id.listview_topbar_fragment);
        this.listView.setPageSize(30);
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

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (list.size() == 0) {
                    return;
                }
                if ((i) <= (list.size() + 1)) {
                    int newsId = 0;
                    if (headerView == null) {
                        newsId = list.get(i - 1).getNewsId();//获取新闻Id
                    } else {
                        newsId = list.get(i - 2).getNewsId();//获取新闻Id
                    }

                    ItemInfoFragment itemInfoFragment = new ItemInfoFragment();
                    Bundle args = new Bundle();
                    args.putInt("newsId", newsId);
                    args.putString(ItemInfoFragment.KEY_SHARE_IMG_URL, headerView == null ? list.get(i - 1).getImgUrl() : list.get(i - 2).getImgUrl());
                    itemInfoFragment.setArguments(args);

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
//            ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                    ft.add(R.id.content_container, itemInfoFragment);
                    ft.addToBackStack(null);
                    ft.commit();
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
                    listView.invalidate();
                }
            }
        }, channelId, lastNewsId);
    }
}
