package com.metis.meishuquan.fragment.Topline;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.MediaWebActivity;
import com.metis.meishuquan.activity.topline.NewDetailActivity;
import com.metis.meishuquan.adapter.topline.ToplineCustomAdapter;
import com.metis.meishuquan.model.BLL.ActiveOperator;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.ActiveInfo;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.topline.News;
import com.metis.meishuquan.model.topline.ToplineNewsList;
import com.metis.meishuquan.util.ImageLoaderUtils;
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
        if (channelId == 6) {
            headerView = inflater.inflate(R.layout.view_act_topline, null, false);

            //获取活动详情
            getActiveInfo(headerView);
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
        ActiveOperator.getInstance().getActivityList(new UserInfoOperator.OnGetListener<List<ActiveInfo>>() {
            @Override
            public void onGet(boolean succeed, List<ActiveInfo> activeInfos) {
                if (succeed == true && activeInfos != null) {
                    if (channelId == 6) {
                        initHeaderView(headerView, activeInfos.get(0).getImage(), activeInfos.get(0).getTitle(), activeInfos.get(0).getContent(), activeInfos.get(0).getTopImage());
                        Log.i("activeInfos title", activeInfos.get(0).getTitle());
                        listView.addHeaderView(headerView);
                        ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(activeInfos.get(0).getTopImage(), imgAct);
                    }
                } else {

                }
            }
        });
    }

    private void initHeaderView(View headerView, final String url, final String title, final String content, final String imgUrl) {
        this.imgAct = (ImageView) headerView.findViewById(R.id.id_img_active);

        this.imgAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MediaWebActivity.class);
                intent.putExtra(MediaWebActivity.KEY_URL, url);
//                intent.putExtra(MediaWebActivity.KEY_URL, "http://www.meishuquan.net/H5/ContentDetial.ASPX?ID=10138&from=singlemessage&isappinstalled=1");
                intent.putExtra(MediaWebActivity.KEY_TITLE, title);
                intent.putExtra(MediaWebActivity.KEY_DESC, content);
                intent.putExtra(MediaWebActivity.KEY_IMAGE, imgUrl);
                Log.i("imgAct target url", url);
                getActivity().startActivity(intent);
//                if (MainApplication.isLogin()) {
//                    startActivity(new Intent(getActivity(), ActDetailActivity.class));
//                } else {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                }
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
                if (i <= (list.size() + 1)) {
                    int newsId = 0;
                    if (headerView == null) {
                        newsId = list.get(i - 1).getNewsId();//获取新闻Id
                    } else {
                        newsId = list.get(i - 2).getNewsId();//获取新闻Id
                    }

//                    ItemInfoFragment itemInfoFragment = new ItemInfoFragment();
//                    Bundle args = new Bundle();
//                    args.putInt("newsId", newsId);
//                    itemInfoFragment.setArguments(args);
//
//                    FragmentManager fm = getActivity().getSupportFragmentManager();
//                    FragmentTransaction ft = fm.beginTransaction();
//                    ft.add(R.id.content_container, itemInfoFragment);
//                    ft.addToBackStack(null);
//                    ft.commit();

                    Intent intent = new Intent(getActivity(), NewDetailActivity.class);
                    intent.putExtra(NewDetailActivity.KEY_NEWS_ID, newsId);
                    startActivity(intent);
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
