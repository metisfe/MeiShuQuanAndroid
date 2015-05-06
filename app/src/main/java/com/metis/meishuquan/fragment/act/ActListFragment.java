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
import com.metis.meishuquan.model.BLL.ActiveOperator;
import com.metis.meishuquan.model.BLL.TopListItem;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.util.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

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
    private ListView mActListView = null;

    private ArrayList<String>
            mFilterData1 = new ArrayList<String>(),
            mFilterData2 = new ArrayList<String>(),
            mFilterData3 = new ArrayList<String>();

    private int mIndex = 1;

    private List<TopListItem> mDataList = new ArrayList<TopListItem>();
    private TopListAdapter mAdapter = new TopListAdapter(mDataList);

    private String mKeyWords = "";

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
        mActListView = (ListView)view.findViewById(R.id.act_list);
        mActListView.setAdapter(mAdapter);
        View emptyView = view.findViewById(R.id.act_empty);
        mActListView.setEmptyView(emptyView);

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
                reLoadDataList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mFilter2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v(TAG, "mFilter2 onItemSelected " + i + " " + l);
                reLoadDataList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mFilter3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void reLoadDataList () {
        mIndex = 1;
        loadDataList(mIndex, new UserInfoOperator.OnGetListener<List<TopListItem>>() {
            @Override
            public void onGet(boolean succeed, List<TopListItem> topListItems) {
                if (succeed) {
                    mDataList.clear();
                    mDataList.addAll(topListItems);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void loadDataListNextPage () {
        loadDataList(mIndex + 1, new UserInfoOperator.OnGetListener<List<TopListItem>>() {
            @Override
            public void onGet(boolean succeed, List<TopListItem> topListItems) {
                if (succeed) {
                    mIndex++;
                    mDataList.addAll(topListItems);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void loadDataList(int index, UserInfoOperator.OnGetListener<List<TopListItem>> listener) {
        int filter1 = (int)mFilter1.getSelectedItemId();
        int filter2 = (int)mFilter2.getSelectedItemId();
        int filter3 = (int)mFilter3.getSelectedItemId();

        switch (filter1) {
            case 1:
                ActiveOperator.getInstance().topListByStudent(filter2, 11, index, mKeyWords, listener);
                break;
            case 2:
                ActiveOperator.getInstance().topListByStudio(filter2, 11, index, mKeyWords, listener);
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

    private class TopListAdapter extends BaseAdapter {

        private List<TopListItem> mData = null;

        public TopListAdapter (List<TopListItem> data) {
            mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public TopListItem getItem(int i) {
            return mData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                holder = new ViewHolder();
                View v = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.layout_active_top_list_item, null);
                holder.profileIv = (ImageView)v.findViewById(R.id.top_list_item_profile);
                holder.nameTv = (TextView)v.findViewById(R.id.top_list_item_name);
                holder.locationTv = (TextView)v.findViewById(R.id.top_list_item_location);
                holder.joinCountTv = (TextView)v.findViewById(R.id.top_list_item_join_count);
                holder.commentCountTv = (TextView)v.findViewById(R.id.top_list_item_comment_count);
                holder.supportCountTv = (TextView)v.findViewById(R.id.top_list_item_support_count);
                view.setTag(holder);
            } else {
                holder = (ViewHolder)view.getTag();
            }
            TopListItem item = getItem(i);
            holder.nameTv.setText(item.getUserNickName());
            holder.locationTv.setText(item.getProvince());
            ImageLoaderUtils.getImageLoader(getActivity()).displayImage(
                    item.getUserAvatar(),
                    holder.profileIv,
                    ImageLoaderUtils.getRoundDisplayOptions(MainApplication.UIContext.getResources().getDimensionPixelSize(R.dimen.active_list_profile_size))
            );
            holder.joinCountTv.setText(
                    MainApplication.UIContext.getString(R.string.act_joined_count, item.getRegisterCount())
            );
            holder.commentCountTv.setText(
                    MainApplication.UIContext.getString(R.string.act_comment_count, item.getCommentCount())
            );
            holder.supportCountTv.setText(
                    MainApplication.UIContext.getString(R.string.act_support_count, item.getUpCount())
            );
            return view;
        }
    }

    private class ViewHolder {
        public RadioButton selectBtn;
        public ImageView profileIv;
        public TextView nameTv;
        public TextView locationTv;
        public TextView joinCountTv, commentCountTv, supportCountTv;
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
