package com.metis.meishuquan.fragment.act;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.circle.ReplyActivity;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.activity.info.homepage.StudioActivity;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.model.BLL.ActiveOperator;
import com.metis.meishuquan.model.BLL.TopListItem;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.circle.CirclePushBlogParm;
import com.metis.meishuquan.model.commons.ActiveInfo;
import com.metis.meishuquan.model.commons.Result;
import com.metis.meishuquan.model.enums.SupportTypeEnum;
import com.metis.meishuquan.util.ActivityUtils;
import com.metis.meishuquan.util.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/5/6.
 */
public abstract class ActiveListFragment extends Fragment implements View.OnClickListener, AreaSelectFragment.OnPlaceChooseListener{

    private static final String TAG = ActiveListFragment.class.getSimpleName();

    private Button mFilter1, mFilter2, mFilter3;
    private ListView mActListView = null;
    /*private ImageView mSearchBtn = null;
    private EditText mSearchEt = null;*/

    private boolean canEdit = false;

    private ActiveInfo mActiveInfo = null;
    private ActiveOperator.SimpleActiveInfo mSimpleInfo = null;

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
        mActListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

                switch (i) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (mActListView.getLastVisiblePosition() >= absListView.getChildCount() - 1 && !loadingMore) {
                            //needLoadMore();
                        }
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        /*mSearchBtn = (ImageView)view.findViewById(R.id.search_btn);
        mSearchEt = (EditText)view.findViewById(R.id.search_content_input);*/

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

        ActiveOperator.getInstance().getActiveDetail(new UserInfoOperator.OnGetListener<ActiveInfo>() {
            @Override
            public void onGet(boolean succeed, ActiveInfo activeInfo) {
                if (succeed) {
                    mActiveInfo = activeInfo;
                    ActiveOperator.getInstance().getMyActiveInfo(mActiveInfo.getpId(), new UserInfoOperator.OnGetListener<ActiveOperator.SimpleActiveInfo>() {
                        @Override
                        public void onGet(boolean succeed, ActiveOperator.SimpleActiveInfo simpleActiveInfo) {
                            if (succeed) {
                                mSimpleInfo = simpleActiveInfo;
                            }
                        }
                    });
                }
            }
        });

        /*mSearchBtn.setOnClickListener(new View.OnClickListener() {
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
        });*/
    }

    public abstract String getFilterTitle1 ();

    public abstract String getFilterTitle2 ();

    public abstract String getFilterTitle3 ();

    public abstract boolean canChooseStudio ();

    public void setCanEdit (boolean canEdit) {
        this.canEdit = canEdit;
    }

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

    private AreaSelectFragment.OnPlaceChooseListener mTempListener = null;

    public void addFragment (Fragment fragment) {
        ((BaseActivity) getActivity()).addFragment(fragment);
    }

    public void removeFragment (Fragment fragment) {
        ((BaseActivity)getActivity()).removeFragment(fragment);
    }

    public void showAreaChooseFragment (AreaSelectFragment.OnPlaceChooseListener listener) {
        mTempListener = listener;
        AreaSelectFragment.getInstance().setOnPlaceChooseListener(this);
        addFragment(AreaSelectFragment.getInstance());
    }

    @Override
    public void onChoose(AreaSelectFragment.CityArea areable, int provinceId, int cityId, int townId) {
        mDataList.clear();
        mAdapter.notifyDataSetChanged();
        AreaSelectFragment.getInstance().setOnPlaceChooseListener(null);
        removeFragment(AreaSelectFragment.getInstance());
        if (mTempListener != null) {
            mTempListener.onChoose(areable, provinceId, cityId, townId);
            mTempListener = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AreaSelectFragment.getInstance().reset();
        CollegeChooseFragment.getInstance().reset();
        PKSwitchFragment.getInstance().reset();
    }

    public TopListItem getSelectedStudioItem () {
        return mAdapter.getSelectedItem();
    }

    public abstract void needReloadData (int selectedIndex1, int selectedIndex2, int selectedIndex3);
    private boolean loadingMore = false;
    public void needLoadMore () {
        loadingMore = true;
    }

    public void onReloadFinished (List<TopListDelegate> data) {
        mDataList.clear();
        mDataList.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    public void onLoadMoreFinished (List<TopListDelegate> data) {
        mDataList.addAll(data);
        mAdapter.notifyDataSetChanged();
        loadingMore = false;
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
                holder.profileIv = (ImageView)view.findViewById(R.id.top_list_item_profile);
                holder.nameTv = (TextView)view.findViewById(R.id.top_list_item_name);
                holder.locationTv = (TextView)view.findViewById(R.id.top_list_item_location);
                holder.joinCountTv = (TextView)view.findViewById(R.id.top_list_item_join_count);
                holder.commentCountTv = (TextView)view.findViewById(R.id.top_list_item_comment_count);
                holder.supportCountTv = (TextView)view.findViewById(R.id.top_list_item_support_count);
                holder.joinBtn = (Button)view.findViewById(R.id.top_list_item_btn);
                view.setTag(holder);
            } else {
                holder = (ViewHolder)view.getTag();
            }
            final TopListDelegate itemDelegate = getItem(i);
            final TopListItem item = itemDelegate.getTopListItem();
            itemDelegate.setChecked(mSimpleInfo != null && mSimpleInfo.getStudioId() == item.getUserId());
            holder.nameTv.setText(item.getUserNickName());
            holder.locationTv.setText(item.getRegion());
            //act_has_joined
            holder.joinBtn.setVisibility(canEdit || itemDelegate.isChecked() ? View.VISIBLE : View.GONE);
            holder.joinBtn.setText(itemDelegate.isChecked ? R.string.act_has_joined : R.string.act_join);
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
                    ActivityUtils.startNameCardActivity(getActivity(), item.getUserId());
                    /*Intent it = new Intent(getActivity(), StudioActivity.class);
                    it.putExtra(StudioActivity.KEY_USER_ID, item.getUserId());
                    startActivity(it);*/
                }
            });
            holder.joinBtn.setSelected(itemDelegate.isChecked());
            holder.joinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!MainApplication.isLogin()) {
                        Toast.makeText(getActivity(), R.string.my_info_toast_not_login, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        return;
                    }
                    if (itemDelegate.isChecked()) {
                        return;
                    }
                    ActiveOperator.getInstance().getMyActiveInfo(mActiveInfo.getpId(), new UserInfoOperator.OnGetListener<ActiveOperator.SimpleActiveInfo>() {
                        @Override
                        public void onGet(boolean succeed, final ActiveOperator.SimpleActiveInfo simpleActiveInfo) {
                            if (!succeed) {
                                return;
                            }
                            if (simpleActiveInfo != null) {
                                mSimpleInfo = simpleActiveInfo;
                                if (simpleActiveInfo.getUpCount() < simpleActiveInfo.totalUpCount) {
                                    showDialog(getString(R.string.act_less_than_10, simpleActiveInfo.totalUpCount), getString(R.string.gender_ok), false, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    return;
                                }
                                final int remainCount = 3 - simpleActiveInfo.getUpdateCount();
                                final boolean canChooseStudio = remainCount > 0;
                                if (!canChooseStudio) {
                                    showDialog(MainApplication.UIContext.getString(R.string.act_no_chance), getString(R.string.gender_ok), false, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    return;
                                }
                                if (mSelectedDelegate != null) {
                                    mSelectedDelegate.setChecked(false);
                                }
                                itemDelegate.setChecked(true);
                                mSelectedDelegate = itemDelegate;
                                notifyDataSetChanged();
                                if (mActiveInfo != null) {
                                    if (!simpleActiveInfo.isJoin) {
                                        ActiveOperator.getInstance().selectStudio(item.getUserId(), mActiveInfo.getpId(), new UserInfoOperator.OnGetListener<Result>() {
                                            @Override
                                            public void onGet(boolean succeed, Result o) {
                                                if (succeed) {
                                                    mSimpleInfo.setStudioId(item.getUserId());
                                                    notifyDataSetChanged();
                                                    Toast.makeText(getActivity(), R.string.act_join_success, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity(), R.string.act_join_failed, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        showDialog(canChooseStudio ? getString(R.string.act_join_count, 3, remainCount) : getString(R.string.act_join_count_cost, 3), getString(R.string.gender_ok), false, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                ActiveOperator.getInstance().changeStudio(item.getUserId(), mActiveInfo.getpId(), new UserInfoOperator.OnGetListener<Result>() {
                                                    @Override
                                                    public void onGet(boolean succeed, Result result) {
                                                        if (getActivity() == null) {
                                                            return;
                                                        }
                                                        if (succeed) {
                                                            mSimpleInfo.setStudioId(item.getUserId());
                                                            notifyDataSetChanged();
                                                            Toast.makeText(getActivity(), R.string.act_join_success, Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(getActivity(), R.string.act_join_failed, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });

                                    }

                                }

                            } else {
                                showDialog(getString(R.string.act_join_unjoined), getString(R.string.act_modify_studio), true, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if (mActiveInfo != null) {
                                            ActiveOperator.getInstance().selectStudio(item.getUserId(), mActiveInfo.getpId(), new UserInfoOperator.OnGetListener() {
                                                @Override
                                                public void onGet(boolean succeed, Object o) {
                                                    if (mActiveInfo != null) {
                                                        Intent it = new Intent(getActivity(), ReplyActivity.class);
                                                        CirclePushBlogParm parm = new CirclePushBlogParm();
                                                        parm.setType(SupportTypeEnum.Activity.getVal());
                                                        parm.setRelayId(mActiveInfo.getpId());
                                                        it.putExtra(ReplyActivity.PARM, parm);
                                                        it.putExtra(ReplyActivity.TITLE, mActiveInfo.getTitle());
                                                        it.putExtra(ReplyActivity.CONTENT, mActiveInfo.getContent());
                                                        it.putExtra(ReplyActivity.IMAGEURL, mActiveInfo.getImage());
                                                        startActivity(it);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });


                }
            });
            /*if (canChooseStudio()) {
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
            }*/

            return view;
        }
    }

    private Dialog mDialog = null;
    private void showDialog (String msg, DialogInterface.OnClickListener positiveListener) {
        showDialog(msg, getString(R.string.gender_ok), true, positiveListener);
    }
    private void showDialog (String msg, String okMsg, boolean showCancel, DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msg);
        builder.setPositiveButton(okMsg, positiveListener);
        if (showCancel) {
            builder.setNegativeButton(R.string.alter_dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mDialog.dismiss();
                }
            });
        }

        mDialog = builder.create();
        mDialog.show();
    }

    private class ViewHolder {
        public ImageView profileIv;
        public TextView nameTv;
        public TextView locationTv;
        public TextView joinCountTv, commentCountTv, supportCountTv;
        public Button joinBtn;
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
