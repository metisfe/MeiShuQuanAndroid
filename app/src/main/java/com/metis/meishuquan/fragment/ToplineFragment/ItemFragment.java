package com.metis.meishuquan.fragment.ToplineFragment;

import android.os.Bundle;
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

/**
 * Fragment:TopBar Fragment
 * <p/>
 * Created by wj on 15/3/17.
 */
public class ItemFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView listView;


    private ToplineCustomAdapter toplineAdapter;
    private ArrayAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View contextView = inflater.inflate(R.layout.fragment_topline_topbar_list, container, false);

        //初始化
        initView(contextView);

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
        this.listView = (ListView) contextView.findViewById(R.id.listview_topbar_fragment);

        //初始化成员
        toplineAdapter=new ToplineCustomAdapter(getActivity(),R.layout.fragment_topline_topbar_list_item);
        //this.adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, getDataSource());
        this.listView.setAdapter(toplineAdapter);

    }

    private void initEvent() {
        this.listView.setOnItemClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //接收数据
        //加载数据
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ItemInfoFragment itemInfoFragment= new ItemInfoFragment();
        FragmentManager fm=getActivity().getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_in,R.anim.fragment_out);
        ft.add(R.id.content_container, itemInfoFragment);
        ft.commit();
    }



}
