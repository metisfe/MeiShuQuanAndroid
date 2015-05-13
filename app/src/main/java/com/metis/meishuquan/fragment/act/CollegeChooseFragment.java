package com.metis.meishuquan.fragment.act;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.commons.MultiListViewFragment;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.College;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/5/13.
 */
public class CollegeChooseFragment extends MultiListViewFragment implements AdapterView.OnItemClickListener {

    private static CollegeChooseFragment sFragment = new CollegeChooseFragment();

    private Callback mCallback = null;

    public static CollegeChooseFragment getInstance () {
        return sFragment;
    }

    private CollegeAdapter mAdapter = null;
    private List<BaseAdapter> mAdapterList = new ArrayList<BaseAdapter>();
    private List<College> mCollegeList = new ArrayList<College>();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mAdapter == null && mAdapterList.isEmpty()) {
            mAdapter = new CollegeAdapter(mCollegeList);
            mAdapterList.add(mAdapter);
        }

        setAdapterList(mAdapterList);
        UserInfoOperator.getInstance().getCollegeList("", new UserInfoOperator.OnGetListener<List<College>>() {
            @Override
            public void onGet(boolean succeed, List<College> colleges) {
                if (succeed) {
                    mCollegeList.clear();
                    mCollegeList.addAll(colleges);
                    mAdapter.notifyDataSetChanged();
                }

            }
        });
        getListView(0).setOnItemClickListener(this);
    }

    public void setCallback (Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mCallback != null) {
            mCallback.onCallback(i, mCollegeList.get(i));
        }
    }

    public class CollegeAdapter extends BaseAdapter {

        private List<College> mCollegeList = null;

        public CollegeAdapter (List<College> colleges) {
            mCollegeList = colleges;
        }

        @Override
        public int getCount() {
            return mCollegeList.size();
        }

        @Override
        public College getItem(int i) {
            return mCollegeList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return getItem(i).getpId();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_list_dialog_item, null);
            }
            TextView tv = (TextView)view.findViewById(R.id.list_dialog_item);
            tv.setText(getItem(i).getName());
            return view;
        }
    }

    public static interface Callback {
        public void onCallback (int i, College college);
    }
}
