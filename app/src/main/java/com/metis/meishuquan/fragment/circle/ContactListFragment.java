package com.metis.meishuquan.fragment.circle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.meishuquan.R;

/**
 * Created by wudi on 4/2/2015.
 */
public class ContactListFragment extends Fragment {
    private ViewGroup rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_contactlistfragment, container, false);

        return rootView;
    }
}
