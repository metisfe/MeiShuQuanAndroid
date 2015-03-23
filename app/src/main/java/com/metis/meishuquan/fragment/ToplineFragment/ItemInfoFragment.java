package com.metis.meishuquan.fragment.ToplineFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.BaseFragment;
import com.metis.meishuquan.fragment.main.ToplineFragment;

/**
 * 头条列表项详细信息界面
 *
 * Created by wj on 15/3/23.
 */
public class ItemInfoFragment extends BaseFragment implements View.OnClickListener {
    private Button btnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_topline_topbar_list_item_info,null,false);
        initView(rootView);
        initEvent();
        return rootView;
    }

    private void initEvent() {
        this.btnBack.setOnClickListener(this);
    }

    private void initView(ViewGroup rootView) {
        btnBack= (Button) rootView.findViewById(R.id.id_btn_back);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_btn_back:
                FragmentManager fm=getActivity().getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.remove(this);
                ft.commit();
                break;
        }
    }
}
