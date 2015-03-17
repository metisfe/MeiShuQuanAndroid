package com.metis.meishuquan.fragment.TopBarFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.BaseFragment;

/**
 * Fragment:TopBar Fragment
 *
 * Created by wj on 15/3/17.
 */
public class ItemFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View contextView = inflater.inflate(R.layout.fragment_topline_topbar_item, container, false);
        TextView mTextView = (TextView) contextView.findViewById(R.id.textview);

        //获取父级传递过来的参数
        Bundle mBundle = getArguments();
        String title = mBundle.getString("arg");

        mTextView.setText(title);

        return contextView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
