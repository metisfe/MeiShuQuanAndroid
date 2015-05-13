package com.metis.meishuquan.fragment.act;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.commons.MultiListViewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/5/12.
 */
public class PKSwitchFragment extends MultiListViewFragment implements AdapterView.OnItemClickListener{

    private static PKSwitchFragment sFragment = new PKSwitchFragment();

    public static PKSwitchFragment getInstance () {
        return sFragment;
    }

    private String[] mArray = null;

    private SpecFragment.SimpleAdapter mAdapter = null;
    private List<BaseAdapter> mAdapterList = null;

    private SpecFragment.Callback mCallback = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArray = getResources().getStringArray(R.array.act_filter_2);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mAdapter == null) {
            mAdapter = new SpecFragment.SimpleAdapter(getActivity(), mArray);
            mAdapterList = new ArrayList<BaseAdapter>();
            mAdapterList.add(mAdapter);
        }
        setAdapterList(mAdapterList);
        getListView(0).setOnItemClickListener(this);
    }

    public void setCallback (SpecFragment.Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mCallback != null) {
            mCallback.onCallback(i, mArray[i]);
        }
    }
}
