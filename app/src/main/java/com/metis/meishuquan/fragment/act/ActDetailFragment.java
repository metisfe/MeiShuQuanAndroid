package com.metis.meishuquan.fragment.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.meishuquan.R;

/**
 * Created by WJ on 2015/4/29.
 */
public class ActDetailFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_act_detail, null, true);
    }
}
