package com.metis.meishuquan.fragment.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.activity.info.homepage.StudioActivity;
import com.metis.meishuquan.model.BLL.TopListItem;
import com.metis.meishuquan.util.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/5/6.
 */
public abstract class ActiveListFragment extends Fragment implements View.OnClickListener, AreaSelectFragment.OnAreaChooseListener{

    private Button mFilter1, mFilter2, mFilter3;
    private ListView mActListView = null;
    private ImageView mSearchBtn = null;
    private EditText mSearchEt = null;

    private List<TopListDelegate> mDataList = new ArrayList<TopListDelegate>();
    private TopListAdapter mAdapter = new TopListAdapter(mDataList);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_act_list, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFilter1 = (Button) view.findViewById(R.id.act_list_filter_1);
        mFilter2 = (Button) view.findViewById(R.id.act_list_filter_2);
        mFilter3 = (Button) view.findViewById(R.id.act_list_filter_3);
        mActListView = (ListView) view.findViewById(R.id.act_list);

        mSearchBtn = (ImageView)view.findViewById(R.id.search_btn);
        mSearchEt = (EditText)view.findViewById(R.id.search_content_input);

        //mActListView.setAdapter(mAdapter);
        View emptyView = view.findViewById(R.id.act_empty);
        mActListView.setEmptyView(emptyView);
        mActListView.setAdapter(mAdapter);

        mFilter1.setText(getFilterTitle1());
        mFilter2.setText(getFilterTitle2());
        mFilter3.setText(getFilterTitle3());

        mFilter1.setOnClickListener(this);
        mFilter2.setOnClickListener(this);
        mFilter3.setOnClickListener(this);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = mSearchEt.getText().toString();
                onSearchClicked(content);
            }
        });
        mSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    onSearchContentCleared ();
                }
            }
        });
    }

    public abstract String getFilterTitle1 ();

    public abstract String getFilterTitle2 ();

    public abstract String getFilterTitle3 ();

    public abstract boolean canChooseStudio ();

    @Override
    public void onClick(View view) {
        onFilterClick(view);
    }

    public abstract void onFilterClick (View view);

    public ListView getListView () {
        return mActListView;
    }

    public Button getFilterSpinner1 () {
        return mFilter1;
    }

    public Button getFilterSpinner2 () {
        return mFilter2;
    }

    public Button getFilterSpinner3 () {
        return mFilter3;
    }

    public void onSearchClicked (String content) {

    }

    public void onSearchContentCleared () {

    }

    private AreaSelectFragment.OnAreaChooseListener mTempListener = null;

    public void addFragment (Fragment fragment) {
        ((BaseActivity) getActivity()).addFragment(fragment);
    }

    public void removeFragment (Fragment fragment) {
        ((BaseActivity)getActivity()).removeFragment(fragment);
    }

    public void showAreaChooseFragment (AreaSelectFragment.OnAreaChooseListener listener) {
        mTempListener = listener;
        AreaSelectFragment.getInstance().setOnAreaChooseListener(this);
        addFragment(AreaSelectFragment.getInstance());
    }

    @Override
    public void onChoose(AreaSelectFragment.Areable area) {
        AreaSelectFragment.getInstance().setOnAreaChooseListener(null);
        removeFragment(AreaSelectFragment.getInstance());
        if (mTempListener != null) {
            mTempListener.onChoose(area);
            mTempListener = null;
        }
        //needReloadData();
    }

    public TopListItem getSelectedStudioItem () {
        return mAdapter.getSelectedItem();
    }

    public abstract void needReloadData (int selectedIndex1, int selectedIndex2, int selectedIndex3);

    public void onReloadFinished (List<TopListDelegate> data) {
        mDataList.clear();
        mDataList.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    public class TopListAdapter extends BaseAdapter {

        private List<TopListDelegate> mData = null;
        private TopListDelegate mSelectedDelegate = null;

        public TopListAdapter (List<TopListDelegate> data) {
            mData = data;
        }

        public TopListItem getSelectedItem () {
            if (mSelectedDelegate == null) {
                return null;
            }
            return mSelectedDelegate.getTopListItem();
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
                view = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.layout_active_top_list_item, null);
                holder.selectBtn = (RadioButton)view.findViewById(R.id.top_list_item_check_box);
                holder.profileIv = (ImageView)view.findViewById(R.id.top_list_item_profile);
                holder.nameTv = (TextView)view.findViewById(R.id.top_list_item_name);
                holder.locationTv = (TextView)view.findViewById(R.id.top_list_item_location);
                holder.joinCountTv = (TextView)view.findViewById(R.id.top_list_item_join_count);
                holder.commentCountTv = (TextView)view.findViewById(R.id.top_list_item_comment_count);
                holder.supportCountTv = (TextView)view.findViewById(R.id.top_list_item_support_count);
                view.setTag(holder);
            } else {
                holder = (ViewHolder)view.getTag();
            }
            final TopListDelegate itemDelegate = getItem(i);
            final TopListItem item = itemDelegate.getTopListItem();
            holder.selectBtn.setVisibility(canChooseStudio() ? View.VISIBLE : View.GONE);
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
            holder.profileIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(getActivity(), StudioActivity.class);
                    it.putExtra(StudioActivity.KEY_USER_ID, item.getUserId());
                    startActivity(it);
                }
            });
            if (canChooseStudio()) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mSelectedDelegate != null) {
                            mSelectedDelegate.setChecked(false);
                        }
                        itemDelegate.setChecked(true);
                        mSelectedDelegate = itemDelegate;
                        notifyDataSetChanged();
                    }
                });
            }

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
