package com.metis.meishuquan.fragment.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;

import com.metis.meishuquan.R;

/**
 * Created by WJ on 2015/5/6.
 */
public class ActiveListFragment extends Fragment {

    private Spinner mFilter1, mFilter2, mFilter3;
    private ListView mActListView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_act_list, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFilter1 = (Spinner) view.findViewById(R.id.act_list_filter_1);
        mFilter2 = (Spinner) view.findViewById(R.id.act_list_filter_2);
        mFilter3 = (Spinner) view.findViewById(R.id.act_list_filter_3);
        mActListView = (ListView) view.findViewById(R.id.act_list);
        //mActListView.setAdapter(mAdapter);
        View emptyView = view.findViewById(R.id.act_empty);
        mActListView.setEmptyView(emptyView);
    }

    public ListView getListView () {
        return mActListView;
    }

    public Spinner getFilterSpinner1 () {
        return mFilter1;
    }

    public Spinner getFilterSpinner2 () {
        return mFilter2;
    }

    public Spinner getFilterSpinner3 () {
        return mFilter3;
    }

}
