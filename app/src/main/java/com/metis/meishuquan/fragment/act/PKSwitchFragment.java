package com.metis.meishuquan.fragment.act;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

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

    private SimpleAdapter mAdapter = null;
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
            mAdapter = new SimpleAdapter(getActivity(), mArray);
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
}
