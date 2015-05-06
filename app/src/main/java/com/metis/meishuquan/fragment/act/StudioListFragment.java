package com.metis.meishuquan.fragment.act;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
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
 * Created by WJ on 2015/5/6.
 */
public class StudioListFragment extends ActiveListFragment {

    private List<TopListDelegate> mDataList = new ArrayList<TopListDelegate>();
    private TopListAdapter mAdapter = new TopListAdapter(mDataList);

    private int mIndex = 1;

    private TopListDelegate mSelectedDelegate = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getFilterSpinner1().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reloadDataList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getFilterSpinner2().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reloadDataList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getFilterSpinner3().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reloadDataList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                    mDataList.clear();
                    final int length = topListItems.size();
                    List<TopListDelegate> delegates = new ArrayList<TopListDelegate>();
                    for (int i = 0; i < length; i++) {
                        TopListDelegate delegate = new TopListDelegate(topListItems.get(i));
                        delegates.add(delegate);
                    }
                    mDataList.addAll(delegates);
                    mAdapter.notifyDataSetChanged();
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
                    mDataList.addAll(delegates);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void loadDataList (int index, UserInfoOperator.OnGetListener<List<TopListItem>> listener) {
        int filter1 = (int)getFilterSpinner1().getSelectedItemId();
        int filter2 = (int)getFilterSpinner2().getSelectedItemId();
        int filter3 = (int)getFilterSpinner3().getSelectedItemId();

        ActiveOperator.getInstance().getStudioList(11, filter2, filter3, index, listener);
    }

    private class TopListAdapter extends BaseAdapter {

        private List<TopListDelegate> mData = null;

        public TopListAdapter (List<TopListDelegate> data) {
            mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public TopListDelegate getItem(int i) {
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
            final TopListDelegate itemDelegate = getItem(i);
            final TopListItem item = itemDelegate.getTopListItem();
            holder.selectBtn.setVisibility(View.VISIBLE);
            holder.selectBtn.setChecked(itemDelegate.isChecked());
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
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mSelectedDelegate != null) {
                        mSelectedDelegate.setChecked(false);
                    }
                    itemDelegate.setChecked(true);
                    mSelectedDelegate = itemDelegate;
                    ActiveOperator.getInstance().selectStudio(item.getpId());
                    getActivity().finish();
                }
            });
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

    private class TopListDelegate {

        private TopListItem mItem = null;
        private boolean isChecked = false;

        public TopListDelegate (TopListItem item) {
            mItem = item;
        }

        public TopListItem getTopListItem () {
            return mItem;
        }

        public void setChecked (boolean checked) {
            isChecked = checked;
        }

        public boolean isChecked () {
            return isChecked;
        }
    }
}
