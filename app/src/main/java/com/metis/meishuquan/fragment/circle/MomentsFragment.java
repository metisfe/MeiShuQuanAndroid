package com.metis.meishuquan.fragment.circle;

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

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.circle.CircleMomentAdapter;
import com.metis.meishuquan.adapter.topline.ToplineCustomAdapter;
import com.metis.meishuquan.fragment.Topline.ItemInfoFragment;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.circle.CircleMoment;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.topline.News;
import com.metis.meishuquan.model.topline.ToplineNewsList;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.view.shared.DragListView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jx on 4/7/2015.
 */
public class MomentsFragment extends CircleBaseFragment {
    @Override
    public void timeToSetTitleBar() {
        getTitleBar().setText("this is the moments page");
    }

    private DragListView listView;
    private List<CircleMoment> list = new ArrayList<CircleMoment>();
    private int channelId = 6;
    private TopLineOperator operator;

    private CircleMomentAdapter circleMomentAdapter;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<CircleMoment> result = (List<CircleMoment>) msg.obj;
            switch (msg.what) {
                case DragListView.REFRESH:
                    listView.onRefreshComplete();
                    list.clear();
                    list.addAll(result);
                    break;
                case DragListView.LOAD:
                    listView.onLoadComplete();
                    list.addAll(result);
                    break;
            }
            listView.setResultSize(result.size());
            circleMomentAdapter.notifyDataSetChanged();
        }

        ;
    };


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //初始化列表数据
        initData();

        View contextView = inflater.inflate(R.layout.fragment_circle_momentsfragment, container, false);

        //初始化
        initView(contextView);

        //初始化事件
        initEvent();
        return contextView;
    }

    /**
     * 初始化
     *
     * @param contextView
     */
    private void initView(View contextView) {
        //view
        this.listView = (DragListView) contextView.findViewById(R.id.fragment_circle_moments_list);

        //初始化成员
        circleMomentAdapter = new CircleMomentAdapter(list);
        this.listView.setAdapter(circleMomentAdapter);
    }

    private void initEvent() {
        this.listView.setOnRefreshListener(new DragListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(DragListView.REFRESH);
            }
        });

        this.listView.setOnLoadListener(new DragListView.OnLoadListener() {
            @Override
            public void onLoad() {
                //TODO
            }
        });
    }

    private void initData() {
        loadData(DragListView.REFRESH);
    }

    private void loadData(final int what) {
        // 从本地或服务器获取数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage();
                msg.what = what;
                msg.obj = getData();
                handler.sendMessage(msg);
            }
        }).start();
    }

    // 从缓存中拿之前加载好的数据
    public List<News> getData() {
        ToplineNewsList result = new ToplineNewsList();
        SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
        if (channelId != -1) {
            String jsonString = spu.getStringByKey(String.valueOf(channelId));
            if (!jsonString.equals("")) {
                Gson gson = new Gson();
                result = gson.fromJson(jsonString, new TypeToken<ToplineNewsList>() {
                }.getType());
            } else {
                this.getData(0);
            }
        }
        return result.getData();
    }

    //加载数据（加载更多）
    public void getData(final int lastNewsId) {
        operator = TopLineOperator.getInstance();
        operator.getNewsListByChannelId(new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                Message msg = handler.obtainMessage();
                msg.what = DragListView.LOAD;
                Gson gson = new Gson();
                if (result != null) {
                    String json = gson.toJson(result);
                    ToplineNewsList data = gson.fromJson(json, new TypeToken<ToplineNewsList>() {
                    }.getType());
                    msg.obj = data.getData();
                    handler.sendMessage(msg);
                } else {
                    //getData(lastNewsId);
                    Log.e("头条列表", "头条列表数据获取失败");
                }
                //TODO:添加至缓存
            }
        }, channelId, lastNewsId);
    }

}
