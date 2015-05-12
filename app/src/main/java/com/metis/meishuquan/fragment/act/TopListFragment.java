package com.metis.meishuquan.fragment.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.TopListItem;
import com.metis.meishuquan.model.BLL.UserInfoOperator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/4/29.
 */
public class TopListFragment extends ActiveListFragment {

    private static final String TAG = TopListFragment.class.getSimpleName();

    private static TopListFragment sFragment = new TopListFragment();

    public static TopListFragment getInstance () {
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

    }

    @Override
    public String getFilterTitle1() {
        return getString(R.string.act_filter_title_pk);
    }

    @Override
    public String getFilterTitle2() {
        return getString(R.string.act_filter_title_spec);
    }

    @Override
    public String getFilterTitle3() {
        return getString(R.string.act_filter_title_area);
    }

    @Override
    public void onFilterClick(View view) {
        switch (view.getId()) {
            case R.id.act_list_filter_1:
                SpecFragment.getInstance().setCallback(new SpecFragment.Callback() {
                    @Override
                    public void onCallback(int position, String name) {
                        removeFragment(SpecFragment.getInstance());
                        getFilterSpinner1().setText(name);
                        //TODO
                        //needReloadData();
                    }
                });
                addFragment(SpecFragment.getInstance());
                break;
            case R.id.act_list_filter_2:
                PKSwitchFragment.getInstance().setCallback(new SpecFragment.Callback() {
                    @Override
                    public void onCallback(int position, String name) {
                        removeFragment(PKSwitchFragment.getInstance());
                        getFilterSpinner2().setText(name);
                        //TODO
                        //need
                    }
                });
                addFragment(PKSwitchFragment.getInstance());
                break;
            case R.id.act_list_filter_3:
                showAreaChooseFragment(new AreaSelectFragment.OnAreaChooseListener() {
                    @Override
                    public void onChoose(AreaSelectFragment.Areable area) {
                        getFilterSpinner3().setText(area.getTitle());
                        //TODO
                        //need
                    }
                });
                break;
        }
    }

    @Override
    public void needReloadData(int selectedIndex1, int selectedIndex2, int selectedIndex3) {
        reLoadDataList(selectedIndex1 + 1, selectedIndex2 + 2, selectedIndex3);
    }

    private void reLoadDataList (int filter1, int filter2, int filter3) {
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

       /* switch (filter1) {
            case 1:
                ActiveOperator.getInstance().topListByStudent(filter2, filter3, index, mKeyWords, listener);
                break;
            case 2:
                ActiveOperator.getInstance().topListByStudio(filter2, filter3, index, mKeyWords, listener);
                break;
        }*/
    }

    public void searchContent (String keyWords) {
        mKeyWords = keyWords;
        //TODO
        //reLoadDataList();
    }

    public void clearSearch () {
        mKeyWords = "";
        //TODO
        //ssreLoadDataList();
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
