package com.metis.meishuquan.fragment.act;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.PrvcEnumAdapter;
import com.metis.meishuquan.model.BLL.TopListItem;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.College;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/5/6.
 */
public class StudioListFragment extends ActiveListFragment {

    private int mIndex = 1;

    private ArrayList<String> mFilterData3 = new ArrayList<String>();
    private List<College> mCollegeList = null;

    private PrvcEnumAdapter mAdapter1 = new PrvcEnumAdapter();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] array = getResources().getStringArray(R.array.act_filter_2);
        for (int i = 0; i < array.length; i++) {
            mFilterData3.add(array[i]);
        }

        UserInfoOperator.getInstance().getCollegeList("", new UserInfoOperator.OnGetListener<List<College>>() {
            @Override
            public void onGet(boolean succeed, List<College> colleges) {
                if (succeed) {
                    mCollegeList = colleges;
                }
            }
        });

        reloadDataList();
    }

    private void reloadDataList () {
        mIndex = 1;
        loadDataList(mIndex, new UserInfoOperator.OnGetListener<List<TopListItem>>() {
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

    private void loadDataListMore () {
        loadDataList(mIndex + 1, new UserInfoOperator.OnGetListener<List<TopListItem>>() {
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
        });
    }

    private void loadDataList (int index, UserInfoOperator.OnGetListener<List<TopListItem>> listener) {

        //ActiveOperator.getInstance().getStudioList(11, filter2, filter3, index, listener);
    }


    @Override
    public String getFilterTitle1() {
        return null;
    }

    @Override
    public String getFilterTitle2() {
        return null;
    }

    @Override
    public String getFilterTitle3() {
        return null;
    }

    @Override
    public void onFilterClick(View view) {
        switch (view.getId()) {
            case R.id.act_list_filter_1:
                showAreaChooseFragment(new AreaSelectFragment.OnAreaChooseListener() {
                    @Override
                    public void onChoose(AreaSelectFragment.Areable area) {
                        getFilterSpinner1().setText(area.getTitle());
                    }
                });
                break;
        }
    }

    @Override
    public void needReloadData(int selectedIndex1, int selectedIndex2, int selectedIndex3) {
        reloadDataList();
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
                view = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.act_list_spinner_item, null);
            }
            TextView tv = (TextView)view.findViewById(R.id.item_text);
            tv.setText(getItem(i).getName());
            return view;
        }
    }
}
