package com.metis.meishuquan.fragment.ToplineFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.BaseFragment;
import com.metis.meishuquan.view.shared.DragListView;

/**
 * Created by xiaoxiao on 15/3/27.
 */
public class CommentListFragment extends BaseFragment {

    private DragListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_topline_comment_list,null,false);

        initView(rootView);
        return rootView;
    }

    //初始化视图
    private void initView(ViewGroup rootView) {
        listView= (DragListView) rootView.findViewById(R.id.id_topline_comment_list);
    }




}
