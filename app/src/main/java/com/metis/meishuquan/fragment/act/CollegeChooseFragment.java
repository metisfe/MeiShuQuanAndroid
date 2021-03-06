package com.metis.meishuquan.fragment.act;

import android.os.Bundle;
import android.util.Log;
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
public class CollegeChooseFragment extends MultiListViewFragment {

    private static final String TAG = CollegeChooseFragment.class.getSimpleName();

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
            UserInfoOperator.getInstance().getCollegeList("", new UserInfoOperator.OnGetListener<List<College>>() {
                @Override
                public void onGet(boolean succeed, List<College> colleges) {
                    if (isDetached()) {
                        return;
                    }
                    if (succeed) {
                        mCollegeList.clear();
                        mCollegeList.add(College.getDefaultOne(getActivity()));
                        mCollegeList.addAll(colleges);
                        mAdapter.notifyDataSetChanged();
                    }

                }
            });
        }
        setAdapterList(mAdapterList);
    }

    public void setCallback (Callback callback) {
        mCallback = callback;
    }

    public class CollegeAdapter extends BaseAdapter {

        private List<College> mCollegeList = null;
        private int mSelectedIndex = -1;

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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_list_dialog_item, null);
            }
            TextView tv = (TextView)view.findViewById(R.id.list_dialog_item);
            tv.setText(getItem(i).getName());
            view.setBackgroundColor(getResources().getColor(mSelectedIndex == i ? R.color.ltgray : android.R.color.white));
            view.setSelected(true);
            view.setPressed(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSelectedIndex = i;
                    if (mCallback != null) {
                        mCallback.onCallback(i, mCollegeList.get(i));
                    }
                    notifyDataSetChanged();
                }
            });
            return view;
        }
    }

    public static interface Callback {
        public void onCallback (int i, College college);
    }
}
