package com.metis.meishuquan.activity.info;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.CircleOperator;
import com.metis.meishuquan.model.BLL.CommonOperator;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.circle.CUserModel;
import com.metis.meishuquan.model.circle.MomentsGroup;
import com.metis.meishuquan.model.commons.FocusOrFollower;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.IdTypeEnum;
import com.metis.meishuquan.model.topline.UserMark;
import com.metis.meishuquan.util.ActivityUtils;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.ScrollBottomListener;
import com.metis.meishuquan.view.common.delegate.AbsDelegate;
import com.metis.meishuquan.view.common.delegate.AbsViewHolder;
import com.metis.meishuquan.view.common.delegate.DelegateAdapter;
import com.metis.meishuquan.view.common.delegate.DelegateImpl;
import com.metis.meishuquan.view.common.delegate.DelegateType;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.ArrayList;
import java.util.List;

public class FocusActivity extends BaseActivity {

    private static final String TAG = FocusActivity.class.getSimpleName();

    public static final String
            KEY_USER_ID = "user_id",
            KEY_FOCUS_TYPE = "focus_type";
    public static final int
            TYPE_FOCUS = 0,
            TYPE_FOLLOWER = 1;

    private RecyclerView mFocusRecyclerView = null;

    private LinearLayoutManager mLinearManager = null;

    private List<AbsDelegate> mDataList = new ArrayList<AbsDelegate>();
    private FocusAdapter mAdapter = null;

    private int mType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);

        final long userId = getIntent().getIntExtra(KEY_USER_ID, 0);
        mType = getIntent().getIntExtra(KEY_FOCUS_TYPE, 0);

        mFocusRecyclerView = (RecyclerView)findViewById(R.id.focus_recycler_view);

        mLinearManager = new LinearLayoutManager(this);
        mFocusRecyclerView.setLayoutManager(mLinearManager);
        mFocusRecyclerView.setOnScrollListener(new ScrollBottomListener() {

            @Override
            public void onScrolledBottom() {
                Log.v(TAG, "onScrolledBottom");
                long lastId = 0;
                if (mDataList.size() > 0) {
                    lastId = ((FocusOrFollower)(mDataList.get(mDataList.size() - 1).getSource())).getId();
                }
                loadData(userId, lastId);
            }
        });

        mAdapter = new FocusAdapter(this, mDataList);
        mFocusRecyclerView.setAdapter(mAdapter);
        if (mType == TYPE_FOLLOWER) {
            setTitleCenter(getString(R.string.info_fans_list));
        }
        loadData(userId, 0);
    }

    private void loadData (long userId, final long lastId) {
        CircleOperator.getInstance().getFocusList(userId, lastId, mType, new UserInfoOperator.OnGetListener<List<FocusOrFollower>>() {
                    @Override
                    public void onGet(boolean succeed, List<FocusOrFollower> focusOrFollowers) {
                        /*if (mType == TYPE_FOLLOWER) {
                            List<FollowerDelegate> delegates = new ArrayList<FollowerDelegate>();
                            for (FocusOrFollower er : focusOrFollowers) {
                                delegates.add(new FollowerDelegate(er));
                            }
                            mDataList.clear();
                            mDataList.addAll(delegates);
                        } else {*/
                        if (!succeed) {
                            return;
                        }
                        List<FocusDelegate> delegates = new ArrayList<FocusDelegate>();
                        for (FocusOrFollower er : focusOrFollowers) {
                            delegates.add(new FocusDelegate(er));
                        }
                        if (lastId == 0) {
                            mDataList.clear();
                        }
                        mDataList.addAll(delegates);
                        //}
                        mAdapter.notifyDataSetChanged();
                    }
                }
        );
    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.info_focus_list);
    }

    private void payAttention (CUserModel model, final FocusDelegate delegate, final View targetView) {
        //mCustomRight.setEnabled(false);
        //UserMark userMark = delegate.getSource().getUserMark();
        final int userId = Integer.parseInt(model.getUserId());
        CommonOperator.getInstance().getMomentsGroupsAsync(new UserInfoOperator.OnGetListener<List<MomentsGroup>>() {
            @Override
            public void onGet(boolean succeed, List<MomentsGroup> momentsGroups) {
                if (succeed) {
                    PopupMenu popupMenu = new PopupMenu(FocusActivity.this, targetView);
                    popupMenu.inflate(R.menu.studio_menu);
                    for (int i = 0; i < momentsGroups.size(); i++) {
                        MomentsGroup group = momentsGroups.get(i);
                        popupMenu.getMenu().add(R.id.studio_menu, group.id, i, group.name);
                    }
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            CircleOperator.getInstance().attention(userId, item.getItemId(), new ApiOperationCallback<ReturnInfo<String>>() {
                                @Override
                                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                    Log.v(TAG, "attention callback = " + response.getContent());
                                    if (result.isSuccess()) {
                                        FocusOrFollower source = delegate.getSource();
                                        if (source.getRelationType() == FocusOrFollower.TYPE_FOCUS_ME) {
                                            source.setRelationType(FocusOrFollower.TYPE_FOCUS_EACH);
                                        } else if (source.getRelationType() == FocusOrFollower.TYPE_NONE) {
                                            source.setRelationType(FocusOrFollower.TYPE_I_FOCUS);
                                        }
                                        mAdapter.notifyDataSetChanged();
                                        /*if (user.getRelationType() == 0) {
                                        } else if (user.getRelationType() == 2) {
                                        }*/
                                    }
                                }
                            });
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            }
        });
    }

    private class FocusAdapter extends DelegateAdapter {

        public FocusAdapter(Context context, List<? extends DelegateImpl> dataList) {
            super(context, dataList);
        }

        @Override
        public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, View itemView) {
            AbsViewHolder holder = null;
            /*switch (mType) {
                case TYPE_FOCUS:*/
                    holder = new FocusHolder(itemView);
                    /*break;
                case TYPE_FOLLOWER:
                    holder = new FollowerHolder(itemView);
                    break;
            }*/
            return holder;
        }
    }

    private class FocusHolder extends AbsViewHolder<FocusDelegate> {

        public ImageView profileIv;
        public TextView nameTv, infoTv;
        public ImageButton mBtn;

        public FocusHolder(View itemView) {
            super(itemView);
            profileIv = (ImageView)itemView.findViewById(R.id.focus_item_profile);
            nameTv = (TextView)itemView.findViewById(R.id.focus_item_name);
            infoTv = (TextView)itemView.findViewById(R.id.focus_item_info);
            mBtn = (ImageButton)itemView.findViewById(R.id.focus_item_btn);
        }

        @Override
        public void bindData(final Context context, List<? extends DelegateImpl> dataList, final FocusDelegate focusDelegate) {
            FocusOrFollower source = focusDelegate.getSource();
            final CUserModel model = source.getUsermodel();
            nameTv.setText(model.name);
            infoTv.setText(IdTypeEnum.getUserRoleByType(model.identity).getStringResource());
            ImageLoaderUtils.getImageLoader(context).displayImage(
                    model.avatar,
                    profileIv,
                    ImageLoaderUtils.getNormalDisplayOptions(R.drawable.default_portrait_fang));
            mBtn.setVisibility(focusDelegate.isMySelf ? View.GONE : View.VISIBLE);
            if (source.getRelationType() == FocusOrFollower.TYPE_FOCUS_EACH) {
                mBtn.setImageResource(R.drawable.ic_focused_each);
            } else if (source.getRelationType() == FocusOrFollower.TYPE_I_FOCUS) {
                mBtn.setImageResource(R.drawable.ic_has_focused);
            } else {
                mBtn.setImageResource(R.drawable.ic_focus_on);
            }
            mBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final FocusOrFollower source = focusDelegate.getSource();
                    if (source.getRelationType() == FocusOrFollower.TYPE_FOCUS_EACH) {
                        CircleOperator.getInstance().cancelAttention(source.getUserId(), new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> stringReturnInfo, Exception e1, ServiceFilterResponse serviceFilterResponse) {
                                if (stringReturnInfo.isSuccess()) {
                                    source.setRelationType(FocusOrFollower.TYPE_FOCUS_ME);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    } else if (source.getRelationType() == FocusOrFollower.TYPE_I_FOCUS) {
                        CircleOperator.getInstance().cancelAttention(source.getUserId(), new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> stringReturnInfo, Exception e1, ServiceFilterResponse serviceFilterResponse) {
                                if (stringReturnInfo.isSuccess()) {
                                    source.setRelationType(FocusOrFollower.TYPE_NONE);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    } else {
                        payAttention(model, focusDelegate, mBtn);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityUtils.startNameCardActivity(context, Long.parseLong(model.getUserId()));
                }
            });
        }
    }

    /*private class FollowerHolder extends AbsViewHolder<FollowerDelegate> {

        public ImageView profileIv;
        public TextView nameTv, infoTv;
        public ImageButton mBtn;

        public FollowerHolder(View itemView) {
            super(itemView);
            profileIv = (ImageView)itemView.findViewById(R.id.focus_item_profile);
            nameTv = (TextView)itemView.findViewById(R.id.focus_item_name);
            infoTv = (TextView)itemView.findViewById(R.id.focus_item_info);
            mBtn = (ImageButton)itemView.findViewById(R.id.focus_item_btn);
        }

        @Override
        public void bindData(final Context context, List<? extends DelegateImpl> dataList, FollowerDelegate followerDelegate) {
            FocusOrFollower source = followerDelegate.getSource();
            final CUserModel model = source.getUsermodel();
            nameTv.setText(model.name);
            infoTv.setText(IdTypeEnum.getUserRoleByType(model.identity).getStringResource());
            ImageLoaderUtils.getImageLoader(context).displayImage(
                    model.avatar,
                    profileIv,
                    ImageLoaderUtils.getNormalDisplayOptions(R.drawable.default_portrait_fang));
            if (source.getUserMark().isMutualAttention()) {
                mBtn.setImageResource(R.drawable.ic_focused_each);
            } else if (source.getUserMark().isAttention()) {
                mBtn.setImageResource(R.drawable.ic_has_focused);
            } else {
                mBtn.setImageResource(R.drawable.ic_focus_on);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityUtils.startNameCardActivity(context, Long.parseLong(model.getUserId()));
                }
            });
        }
    }*/

    private class FocusDelegate extends AbsDelegate<FocusOrFollower> {

        public boolean isMySelf = false;

        public FocusDelegate(FocusOrFollower focusItem) {
            super(focusItem);
            int userId = Integer.parseInt(focusItem.getUsermodel().getUserId());
            isMySelf = MainApplication.userInfo != null && userId == MainApplication.userInfo.getUserId();
        }

        @Override
        public DelegateType getDelegateType() {
            if (mType == TYPE_FOLLOWER) {
                return DelegateType.USER_FOLLOWER;
            }
            return DelegateType.USER_FOCUS;
        }
    }

    /*private class FollowerDelegate extends AbsDelegate<FocusOrFollower> {

        public FollowerDelegate(FocusOrFollower focusOrFollower) {
            super(focusOrFollower);
        }

        @Override
        public DelegateType getDelegateType() {
            return DelegateType.USER_FOLLOWER;
        }
    }*/

}
