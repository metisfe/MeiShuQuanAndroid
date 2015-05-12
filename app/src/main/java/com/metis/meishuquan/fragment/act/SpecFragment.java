package com.metis.meishuquan.fragment.act;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.commons.ConstellationAdapter;
import com.metis.meishuquan.fragment.commons.MultiListViewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/5/12.
 */
public class SpecFragment extends MultiListViewFragment implements AdapterView.OnItemClickListener {

    private static SpecFragment sFragment = new SpecFragment();

    public static SpecFragment getInstance () {
        return sFragment;
    }

    private SimpleAdapter mAdapter = null;
    private List<BaseAdapter> mAdapterList = null;

    private Callback mCallback = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mAdapterList == null) {
            mAdapterList = new ArrayList<BaseAdapter>();
            mAdapter = new SimpleAdapter(getActivity());
            mAdapterList.add(mAdapter);
        }
        setAdapterList(mAdapterList);
        getListView(0).setOnItemClickListener(this);
    }

    public void setCallback (Callback callback) {
        mCallback = callback;
    }

    public String[] getStringArray () {
        String[] mSpecs = getActivity().getResources().getStringArray(R.array.act_filter_1);
        return mSpecs;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getActivity(), "onItemClick " + i, Toast.LENGTH_SHORT).show();
        String[] mSpecs = getStringArray();
        if (mCallback != null) {
            mCallback.onCallback(i, mSpecs[i]);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    private class SimpleAdapter extends BaseAdapter {

        private Context mContext = null;
        private String[] mSpecs = null;

        public SimpleAdapter (Context context) {
            mContext = context;
            mSpecs = getStringArray();
        }

        @Override
        public int getCount() {
            return mSpecs.length;
        }

        @Override
        public String getItem(int i) {
            return mSpecs[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_list_dialog_item, null);
            TextView tv = (TextView)view.findViewById(R.id.list_dialog_item);
            tv.setText(getItem(i));
            return view;
        }
    }

    public interface Callback {
        public void onCallback (int position, String name);
    }

}
