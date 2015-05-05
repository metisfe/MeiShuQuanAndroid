package com.metis.meishuquan.fragment.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.ActiveOperator;

import java.util.ArrayList;

/**
 * Created by WJ on 2015/4/29.
 */
public class ActListFragment extends Fragment {

    private static final String TAG = ActListFragment.class.getSimpleName();

    private static ActListFragment sFragment = new ActListFragment();

    public static ActListFragment getInstance () {
        return sFragment;
    }

    private Spinner mFilter1, mFilter2, mFilter3;

    private ArrayList<String>
            mFilterData1 = new ArrayList<String>(),
            mFilterData2 = new ArrayList<String>(),
            mFilterData3 = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_act_list, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFilter1 = (Spinner)view.findViewById(R.id.act_list_filter_1);
        mFilter2 = (Spinner)view.findViewById(R.id.act_list_filter_2);
        mFilter3 = (Spinner)view.findViewById(R.id.act_list_filter_3);

        if (mFilterData1.isEmpty()) {
            String[] filterArr1 = getResources().getStringArray(R.array.act_filter_1);
            for (int i = 0; i < filterArr1.length; i++) {
                mFilterData1.add(filterArr1[i]);
            }
        }

        if (mFilterData2.isEmpty()) {
            String[] filterArr2 = getResources().getStringArray(R.array.act_filter_2);
            for (int i = 0; i < filterArr2.length; i++) {
                mFilterData2.add(filterArr2[i]);
            }
        }

        if (mFilterData3.isEmpty()) {
            String[] filterArr3 = getResources().getStringArray(R.array.act_filter_3);
            for (int i = 0; i < filterArr3.length; i++) {
                mFilterData3.add(filterArr3[i]);
            }
        }

        mFilter1.setAdapter(new SimpleAdapter(mFilterData1));
        mFilter2.setAdapter(new SimpleAdapter(mFilterData2));
        mFilter3.setAdapter(new SimpleAdapter(mFilterData3));

        mFilter1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v(TAG, "mFilter1 onItemSelected " + i + " " + l);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mFilter2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v(TAG, "mFilter2 onItemSelected " + i + " " + l);
                ActiveOperator.getInstance().topListByStudio(i + 1, 11, 1, "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mFilter3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v(TAG, "mFilter3 onItemSelected " + i + " " + l);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private class SimpleAdapter extends BaseAdapter {

        private ArrayList<String> mNameList = null;

        public SimpleAdapter (ArrayList<String> nameList) {
            mNameList = nameList;
        }

        @Override
        public int getCount() {
            return mNameList.size();
        }

        @Override
        public String getItem(int position) {
            return mNameList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.act_list_spinner_item, null);
            }
            TextView tv = (TextView)convertView.findViewById(R.id.item_text);
            tv.setText(getItem(position));
            return convertView;
        }
    }
}
