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
public class SpecFragment extends MultiListViewFragment {

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
            mAdapter = new SimpleAdapter(getActivity(), getStringArray());
            mAdapterList.add(mAdapter);
        }
        setAdapterList(mAdapterList);
    }

    public void setCallback (Callback callback) {
        mCallback = callback;
    }

    public String[] getStringArray () {
        String[] mSpecs = getActivity().getResources().getStringArray(R.array.act_filter_1);
        return mSpecs;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public class SimpleAdapter extends BaseAdapter {

        private Context mContext = null;
        private String[] mSpecs = null;
        private int mSelectedIndex = -1;

        public SimpleAdapter (Context context, String[] array) {
            mContext = context;
            mSpecs = array;
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_list_dialog_item, null);
            }

            TextView tv = (TextView)view.findViewById(R.id.list_dialog_item);
            tv.setText(getItem(i));
            view.setBackgroundColor(getResources().getColor(mSelectedIndex == i ? R.color.ltgray : android.R.color.white));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSelectedIndex = i;
                    notifyDataSetChanged();
                    if (mCallback != null) {
                        mCallback.onCallback(i, mSpecs[i]);
                    }
                }
            });
            return view;
        }
    }

    public interface Callback {
        public void onCallback (int position, String name);
    }

}
