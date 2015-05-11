package com.metis.meishuquan.fragment.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.metis.meishuquan.util.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/5/6.
 */
public abstract class ActiveListFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private Spinner mFilter1, mFilter2, mFilter3;
    private ListView mActListView = null;

    private List<TopListDelegate> mDataList = new ArrayList<TopListDelegate>();
    private TopListAdapter mAdapter = new TopListAdapter(mDataList);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_act_list, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFilter1 = (Spinner) view.findViewById(R.id.act_list_filter_1);
        mFilter2 = (Spinner) view.findViewById(R.id.act_list_filter_2);
        mFilter3 = (Spinner) view.findViewById(R.id.act_list_filter_3);
        mActListView = (ListView) view.findViewById(R.id.act_list);
        //mActListView.setAdapter(mAdapter);
        View emptyView = view.findViewById(R.id.act_empty);
        mActListView.setEmptyView(emptyView);
        mActListView.setAdapter(mAdapter);

        mFilter1.setOnItemSelectedListener(this);
        mFilter2.setOnItemSelectedListener(this);
        mFilter3.setOnItemSelectedListener(this);
    }



    public ListView getListView () {
        return mActListView;
    }

    public Spinner getFilterSpinner1 () {
        return mFilter1;
    }

    public Spinner getFilterSpinner2 () {
        return mFilter2;
    }

    public Spinner getFilterSpinner3 () {
        return mFilter3;
    }

    public abstract void needReloadData (int selectedIndex1, int selectedIndex2, int selectedIndex3);

    public void onReloadFinished (List<TopListDelegate> data) {
        mDataList.clear();
        mDataList.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        needReloadData(mFilter1.getSelectedItemPosition(), mFilter2.getSelectedItemPosition(), mFilter3.getSelectedItemPosition());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class TopListAdapter extends BaseAdapter {

        private List<TopListDelegate> mData = null;
        private TopListDelegate mSelectedDelegate = null;

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


    public class TopListDelegate {

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
