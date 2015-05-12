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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.PrvcEnumAdapter;
import com.metis.meishuquan.adapter.commons.SimplePrvsAdapter;
import com.metis.meishuquan.model.BLL.ActiveOperator;
import com.metis.meishuquan.model.BLL.TopListItem;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.util.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/4/29.
 */
public class ActListFragment extends ActiveListFragment {

    private static final String TAG = ActListFragment.class.getSimpleName();

    private static ActListFragment sFragment = new ActListFragment();

    public static ActListFragment getInstance () {
        return sFragment;
    }

    /*private Spinner mFilter1, mFilter2, mFilter3;
    private ListView mActListView = null;*/

    private ArrayList<String>
            mFilterData1 = new ArrayList<String>(),
            mFilterData2 = new ArrayList<String>();

    private int mIndex = 1;

    //private List<TopListItem> mDataList = new ArrayList<TopListItem>();

    private String mKeyWords = "";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        getFilterSpinner1().setAdapter(new SimpleAdapter(mFilterData1));
        getFilterSpinner2().setAdapter(new SimpleAdapter(mFilterData2));
        getFilterSpinner3().setAdapter(new PrvcEnumAdapter());

        getFilterSpinner1().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v(TAG, "mFilter1 onItemSelected " + i + " " + l);
                reLoadDataList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getFilterSpinner2().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v(TAG, "mFilter2 onItemSelected " + i + " " + l);
                reLoadDataList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getFilterSpinner3().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v(TAG, "mFilter3 onItemSelected " + i + " " + l);
                reLoadDataList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void needReloadData(int selectedIndex1, int selectedIndex2, int selectedIndex3) {
        reLoadDataList();
    }

    private void reLoadDataList () {
        mIndex = 1;
        loadDataList(mIndex, new UserInfoOperator.OnGetListener<List<TopListItem>>() {
            @Override
            public void onGet(boolean succeed, List<TopListItem> topListItems) {
                if (succeed) {
                    List<TopListDelegate> delegates = new ArrayList<TopListDelegate>();
                    for (TopListItem item : topListItems) {
                        delegates.add(new TopListDelegate(item));
                    }
                    onReloadFinished(delegates);
                    /*mDataList.clear();
                    mDataList.addAll(topListItems);
                    mAdapter.notifyDataSetChanged();*/
                }
            }
        });
    }

    private void loadDataListNextPage () {
        /*loadDataList(mIndex + 1, new UserInfoOperator.OnGetListener<List<TopListItem>>() {
            @Override
            public void onGet(boolean succeed, List<TopListItem> topListItems) {
                if (succeed) {
                    mIndex++;
                    mDataList.addAll(topListItems);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });*/
    }

    private void loadDataList(int index, UserInfoOperator.OnGetListener<List<TopListItem>> listener) {
        int filter1 = (int)getFilterSpinner1().getSelectedItemId();
        int filter2 = (int)getFilterSpinner2().getSelectedItemId();
        int filter3 = (int)getFilterSpinner3().getSelectedItemId();

        switch (filter1) {
            case 1:
                ActiveOperator.getInstance().topListByStudent(filter2, filter3, index, mKeyWords, listener);
                break;
            case 2:
                ActiveOperator.getInstance().topListByStudio(filter2, filter3, index, mKeyWords, listener);
                break;
        }
    }

    public void searchContent (String keyWords) {
        mKeyWords = keyWords;
        reLoadDataList();
    }

    public void clearSearch () {
        mKeyWords = "";
        reLoadDataList();
    }

    public class SimpleAdapter extends BaseAdapter {

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
            return position + 1;
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
