package com.metis.meishuquan.fragment.ToplineFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.BaseFragment;

/**
 * Created by xiaoxiao on 15/3/27.
 */
public class CommentListFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.activity_channel_manage_subscribe,null,false);

        return rootView;
    }


}
