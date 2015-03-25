package com.metis.meishuquan.fragment.ToplineFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.topline.ToplineCustomAdapter;
import com.metis.meishuquan.fragment.BaseFragment;
import com.metis.meishuquan.fragment.main.ToplineFragment;
import com.metis.meishuquan.view.shared.DragListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Fragment:TopBar Fragment
 * <p/>
 * Created by wj on 15/3/17.
 */
public class ItemFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private DragListView listView;
    private List<String> list = new ArrayList<String>();

    private ToplineCustomAdapter toplineAdapter;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<String> result = (List<String>) msg.obj;
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
            toplineAdapter.notifyDataSetChanged();
        };
    };


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        Bundle mBundle= this.getArguments();
//        int channelId=mBundle.getInt("channelId");
        //TODO:根据频道Id，获取频道列表数据

        View contextView = inflater.inflate(R.layout.fragment_topline_topbar_list, container, false);

        //初始化
        initView(contextView);
        initData();
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
        this.listView = (DragListView) contextView.findViewById(R.id.listview_topbar_fragment);

        //初始化成员
        toplineAdapter = new ToplineCustomAdapter(getActivity(), list);
        //this.adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, getDataSource());
        this.listView.setAdapter(toplineAdapter);

    }

    private void initEvent() {
        this.listView.setOnItemClickListener(this);
        this.listView.setOnRefreshListener(new DragListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(DragListView.REFRESH);
            }
        });

        this.listView.setOnLoadListener(new DragListView.OnLoadListener() {
            @Override
            public void onLoad() {
                loadData(DragListView.LOAD);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ItemInfoFragment itemInfoFragment = new ItemInfoFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
        ft.add(R.id.content_container, itemInfoFragment);
        ft.commit();
    }




    private void initData() {
        loadData(DragListView.REFRESH);
    }

    private void loadData(final int what) {
        // 这里模拟从服务器获取数据
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(700);
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

    // 测试数据
    public List<String> getData() {
        List<String> result = new ArrayList<String>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            long l = random.nextInt(10000);
            result.add("当前条目的ID：" + l);
        }
        return result;
    }


}
