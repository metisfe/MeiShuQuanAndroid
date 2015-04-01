package com.metis.meishuquan.fragment.Assess;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.BaseFragment;

/**
 * Fragment:点评列表过滤条件
 * Created by wj on 15/4/1.
 */
public class FilterConditionForAssessListFragment extends BaseFragment {
    private FragmentManager fm;
    private Button btnConfirm;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_assess_list_condition_filter, null);
        initView(rootView);
        initEvent();
        return rootView;
    }

    private void initView(ViewGroup rootView) {
        fm=getActivity().getSupportFragmentManager();
        this.btnConfirm= (Button) rootView.findViewById(R.id.id_btn_confirm);
    }

    private void initEvent(){
        this.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft=fm.beginTransaction();
                ft.remove(FilterConditionForAssessListFragment.this);
                ft.commit();
            }
        });
    }
}
