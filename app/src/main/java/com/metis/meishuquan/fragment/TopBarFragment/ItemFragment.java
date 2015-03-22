package com.metis.meishuquan.fragment.TopBarFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.topline.ToplineAdapter;
import com.metis.meishuquan.adapter.topline.ToplineCustomAdapter;
import com.metis.meishuquan.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment:TopBar Fragment
 * <p/>
 * Created by wj on 15/3/17.
 */
public class ItemFragment extends BaseFragment {

    private ListView listView;

    private ToplineCustomAdapter toplineAdapter;
    private ArrayAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View contextView = inflater.inflate(R.layout.fragment_topline_topbar_list, container, false);

        //初始化
        initView(contextView);

        return contextView;
    }

    /**
     * 初始化
     *
     * @param contextView
     */
    private void initView(View contextView) {
        //view
        this.listView = (ListView) contextView.findViewById(R.id.listview_topbar_fragment);

        //初始化成员
        toplineAdapter=new ToplineCustomAdapter(getActivity(),R.layout.fragment_topline_topbar_list_item);
        //this.adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, getDataSource());
        this.listView.setAdapter(toplineAdapter);

    }

    public static List<String> getDataSource() {
        List<String> data = new ArrayList<String>();
        data.add("北京");
        data.add("上海");
        data.add("广州");
        data.add("天津");
        data.add("重庆");
        data.add("四川");
        data.add("黑龙江");
        return data;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //接收数据
        //加载数据
    }
}
