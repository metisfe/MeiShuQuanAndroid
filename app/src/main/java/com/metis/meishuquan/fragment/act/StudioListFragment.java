package com.metis.meishuquan.fragment.act;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.PrvcEnumAdapter;
import com.metis.meishuquan.model.BLL.ActiveOperator;
import com.metis.meishuquan.model.BLL.TopListItem;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.ActiveInfo;
import com.metis.meishuquan.model.commons.College;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/5/6.
 */
public class StudioListFragment extends ActiveListFragment {

    private static final String TAG = StudioListFragment.class.getSimpleName();

    private static StudioListFragment sFragment = new StudioListFragment();

    public static StudioListFragment getInstance () {
        return sFragment;
    }

    private int mIndex = 1;

    private ArrayList<String> mFilterData3 = new ArrayList<String>();

    private int mProvinceId, mCityId, mTownId;
    private int mFilter2, mFilter3;
    private String mKey = "";
    private ActiveInfo mActiveInfo = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] array = getResources().getStringArray(R.array.act_filter_2);
        for (int i = 0; i < array.length; i++) {
            mFilterData3.add(array[i]);
        }
        ActiveOperator.getInstance().getActiveDetail(new UserInfoOperator.OnGetListener<ActiveInfo>() {
            @Override
            public void onGet(boolean succeed, ActiveInfo activeInfo) {
                if (succeed) {
                    mActiveInfo = activeInfo;
                    reloadDataList(0, mFilter2, mFilter3);
                }
            }
        });
    }

    private void reloadDataList (int filter1, int filter2, int filter3) {
        mIndex = 1;
        loadDataList(filter1, filter2, filter3, mIndex, new UserInfoOperator.OnGetListener<List<TopListItem>>() {
            @Override
            public void onGet(boolean succeed, List<TopListItem> topListItems) {
                if (succeed) {
                    final int length = topListItems.size();
                    List<TopListDelegate> delegates = new ArrayList<TopListDelegate>();
                    for (int i = 0; i < length; i++) {
                        TopListDelegate delegate = new TopListDelegate(topListItems.get(i));
                        delegates.add(delegate);
                    }
                    onReloadFinished(delegates);
                }
            }
        });
    }

    @Override
    public void onSearchClicked(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        mKey = content;
        needReloadData(0, mFilter2, mFilter3);
        mKey = "";
    }

    @Override
    public void onSearchContentCleared() {
        mKey = "";
        needReloadData(0, mFilter2, mFilter3);
    }

    public void searchContent (String content) {
        mKey = content;
        needReloadData(0, mFilter2, mFilter3);
    }

    public void clearSearch () {
        mKey = "";
        needReloadData(0, mFilter2, mFilter3);
    }

    private void loadDataListMore () {
        /*loadDataList(mIndex + 1, new UserInfoOperator.OnGetListener<List<TopListItem>>() {
            @Override
            public void onGet(boolean succeed, List<TopListItem> topListItems) {
                if (succeed) {
                    mIndex++;
                    final int length = topListItems.size();
                    List<TopListDelegate> delegates = new ArrayList<TopListDelegate>();
                    for (int i = 0; i < length; i++) {
                        TopListDelegate delegate = new TopListDelegate(topListItems.get(i));
                        delegates.add(delegate);
                    }
                }
            }
        });*/
    }

    private void loadDataList (int filter1, int filter2, int filter3, int index, UserInfoOperator.OnGetListener<List<TopListItem>> listener) {
        if (mActiveInfo != null) {
            ActiveOperator.getInstance().getStudioList(mProvinceId, mCityId, mTownId, mActiveInfo.getpId(), filter3, filter2, 0, index, mKey, listener);
        }

    }


    @Override
    public String getFilterTitle1() {
        return getString(R.string.act_filter_title_area);
    }

    @Override
    public String getFilterTitle2() {
        return getString(R.string.act_filter_title_college);
    }

    @Override
    public String getFilterTitle3() {
        return getString(R.string.act_filter_title_spec);
    }

    @Override
    public boolean canChooseStudio() {
        return true;
    }

    @Override
    public void onFilterClick(View view) {
        switch (view.getId()) {
            case R.id.act_list_filter_1:
                showAreaChooseFragment(new AreaSelectFragment.OnPlaceChooseListener() {
                    @Override
                    public void onChoose(AreaSelectFragment.Areable areable, int provinceId, int cityId, int townId) {
                        mProvinceId = provinceId;
                        mCityId = cityId;
                        mTownId = townId;
                        getFilterSpinner1().setText(areable.getTitle());
                        needReloadData(0, mFilter2, mFilter3);
                    }
                });
                break;
            case R.id.act_list_filter_2:
                addFragment(CollegeChooseFragment.getInstance());
                CollegeChooseFragment.getInstance().setCallback(new CollegeChooseFragment.Callback() {
                    @Override
                    public void onCallback(int i, College college) {
                        getFilterSpinner2().setText(college.getName());
                        removeFragment(CollegeChooseFragment.getInstance());
                        mFilter2 = college.getpId();
                        needReloadData(0, mFilter2, mFilter3);
                    }
                });
                break;
            case R.id.act_list_filter_3:
                addFragment(PKSwitchFragment.getInstance());
                PKSwitchFragment.getInstance().setCallback(new SpecFragment.Callback() {
                    @Override
                    public void onCallback(int position, String name) {
                        removeFragment(PKSwitchFragment.getInstance());
                        getFilterSpinner3().setText(name);
                        mFilter3 = position + 1;
                        needReloadData(0, mFilter2, mFilter3);
                    }
                });
                break;
        }
    }

    @Override
    public void needReloadData(int selectedIndex1, int selectedIndex2, int selectedIndex3) {
        reloadDataList(selectedIndex1, selectedIndex2, selectedIndex3);
    }

    @Override
    public void needLoadMore() {
        Log.v(TAG, "needLoadMore ");
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
