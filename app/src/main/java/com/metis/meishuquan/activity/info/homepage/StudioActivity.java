package com.metis.meishuquan.activity.info.homepage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.base.ActivityDispatcher;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.WebActivity;
import com.metis.meishuquan.activity.course.ChooseCourseActivity;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.activity.info.DepartmentActivity;
import com.metis.meishuquan.activity.info.DepartmentEditActivity;
import com.metis.meishuquan.activity.info.InfoActivity;
import com.metis.meishuquan.adapter.circle.CircleMomentAdapter;
import com.metis.meishuquan.adapter.commons.ConstellationAdapter;
import com.metis.meishuquan.adapter.commons.SimplePrvsAdapter;
import com.metis.meishuquan.adapter.studio.AchievementAdapter;
import com.metis.meishuquan.adapter.studio.CircleListAdapter;
import com.metis.meishuquan.adapter.studio.InfoAdapter;
import com.metis.meishuquan.adapter.studio.UserInfoAdapter;
import com.metis.meishuquan.adapter.studio.WorkAdapter;
import com.metis.meishuquan.adapter.topline.ToplineCustomAdapter;
import com.metis.meishuquan.fragment.Topline.ItemInfoFragment;
import com.metis.meishuquan.fragment.circle.MomentDetailFragment;
import com.metis.meishuquan.fragment.commons.InputDialogFragment;
import com.metis.meishuquan.fragment.commons.InputFragment;
import com.metis.meishuquan.fragment.commons.ListDialogFragment;
import com.metis.meishuquan.fragment.commons.StudioFragment;
import com.metis.meishuquan.manager.common.UserManager;
import com.metis.meishuquan.model.BLL.CircleOperator;
import com.metis.meishuquan.model.BLL.CommonOperator;
import com.metis.meishuquan.model.circle.MomentsGroup;
import com.metis.meishuquan.model.commons.Achievement;
import com.metis.meishuquan.model.BLL.StudioBaseInfo;
import com.metis.meishuquan.model.BLL.StudioOperator;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.BLL.WorkInfo;
import com.metis.meishuquan.model.circle.CCircleDetailModel;
import com.metis.meishuquan.model.commons.Profile;
import com.metis.meishuquan.model.commons.Studio;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.course.CourseChannelItem;
import com.metis.meishuquan.model.enums.IdTypeEnum;
import com.metis.meishuquan.model.topline.News;
import com.metis.meishuquan.model.topline.NewsInfo;
import com.metis.meishuquan.util.GlobalData;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.PatternUtils;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.tencent.connect.UserInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StudioActivity extends BaseActivity implements
        StudioFragment.OnMenuItemClickListener,
        RadioGroup.OnCheckedChangeListener,
        /*InfoAdapter.OnInfoItemClickListener,*/
        ConstellationAdapter.OnItemClickListener{

    private static final String TAG = StudioActivity.class.getSimpleName();

    public static final String KEY_USER_ID = User.KEY_USER_ID,
                                KEY_USER_ROLE = User.KEY_USER_ROLE;

    public static final int
            REQUEST_CODE_DEPARTMENT = 600,
            REQUEST_CODE_SCHOOL = 601,
            REQUEST_CODE_CAMERA = 222,
            REQUEST_CODE_GALLERY = 333,
            REQUEST_CODE_COVER_CAMERA = 234,
            REQUEST_CODE_COVER_GALLERY = 345;

    private View mTitleView = null;
    private ImageView mTitleProfile = null;
    private TextView mTitleName = null, mSubTitleName = null;

    private StudioFragment mStudioFragment = null;

    private BaseAdapter mAdapter = null;

    private long mUserId = MainApplication.userInfo.getUserId();

    private User mUser = null;

    private List<AchievementAdapter.AchievementDelegate> mAchievementList = null;
    private List<WorkInfo> mWorkInfoList = null;
    private List<News> mNewsList = null;
    private List<CCircleDetailModel> mCircleList = null;

    private String mCameraOutputPath = null;

    private int mIndexForTab1, mIndexForTab2, mIndexForTab3;

    private TextView mCustomRight = null;

    private SimplePrvsAdapter.OnPrvsItemClickListener mPrvsListener = new SimplePrvsAdapter.OnPrvsItemClickListener() {

        @Override
        public void onItemClick(View view, UserInfoOperator.SimpleProvince province) {
            ListDialogFragment.getInstance().dismiss();
            UserManager.updateMyInfo(User.KEY_REGION, province.getProvinceId() + "");
            mUser.setRegion(province.getProvinceId());
            if (mAdapter instanceof UserInfoAdapter) {
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    private StudioBaseInfo mInfo = null;
    private boolean canEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio);

        mUserId = getIntent().getLongExtra(KEY_USER_ID, -1);
        if (mUserId == -1) {
            mUserId = getIntent().getLongExtra(ActivityDispatcher.KEY_USER_ID, -1);
        }
        canEdit = mUserId == MainApplication.userInfo.getUserId();

        mTitleView = LayoutInflater.from(this).inflate(R.layout.layout_studio_title, null);
        getTitleView().addCenterView(mTitleView);
        mTitleProfile = (ImageView)mTitleView.findViewById(R.id.studio_title_profile);
        mTitleName = (TextView)mTitleView.findViewById(R.id.studio_title_name);
        mSubTitleName = (TextView)mTitleView.findViewById(R.id.studio_title_meishuquan_id);
        /*ImageLoaderUtils.getImageLoader(this).displayImage("http://images.apple.com/cn/live/2015-mar-event/images/751591e0653867230e700d3a99157780826cce88_xlarge.jpg",
                mTitleProfile,
                ImageLoaderUtils.getRoundDisplayOptions(getResources().getDimensionPixelSize(R.dimen.studio_profile_size)));*/

        mStudioFragment = (StudioFragment)getSupportFragmentManager().findFragmentById(R.id.studio_fragment);
        mStudioFragment.setOnMenuItemClickListener(this);

        mStudioFragment.setOnCheckedChangeListener(this);
        mStudioFragment.setOnNextPageListener(new StudioFragment.OnNextPageListener() {
            @Override
            public void onLoadNextPage() {
                int id = mStudioFragment.getRadioGroup().getCheckedRadioButtonId();
                switch (id) {
                    case R.id.studio_list_header_tab1:
                        loadFirstTab();
                        break;
                    case R.id.studio_list_header_tab2:
                        loadSecondTab();
                        break;
                    case R.id.studio_list_header_tab3:
                        loadThirdTab();
                        break;
                }
                /*onCheckedChanged (mStudioFragment.getRadioGroup(), id);*/
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Toast.makeText(this, "onSaveInstanceState ", Toast.LENGTH_SHORT).show();
        outState.putString("cameraPath", mCameraOutputPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Toast.makeText(this, "onRestoreInstanceState " + savedInstanceState.getString("cameraPath"), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //TODO
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.more));
        progressDialog.show();
        loadUser(mUserId, new UserInfoOperator.OnGetListener<User>() {
            @Override
            public void onGet(boolean succeed, User user) {
                if (succeed) {
                    mUser = user;
                    //TODO
//                    mUser.setUserRole(3);
//                    mUser.setUserId(100090);
                    loadFirstTab();
                    fillUser(user);
                    progressDialog.dismiss();
                    if (mUser.getUserRoleEnum() == IdTypeEnum.STUDIO) {
                        loadStudioInfo(mUser.getUserId(), new UserInfoOperator.OnGetListener<StudioBaseInfo>() {
                            @Override
                            public void onGet(boolean succeed, StudioBaseInfo o) {
                                if (succeed) {
                                    mInfo = o;
                                    fillStudioInfo(mInfo);
                                } else {

                                }
                            }
                        });
                    }
                }
            }
        });

    }

    private void fillStudioInfo (StudioBaseInfo info) {
        mStudioFragment.setStudioBaseInfo(info);
    }

    private void fillUser (final User user) {
        if (MainApplication.userInfo != null && user.getUserId() != MainApplication.userInfo.getUserId()) {
            mCustomRight = new TextView(this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.setMargins(0, 0, (int)(10 * getResources().getDisplayMetrics().density), 0);
            getTitleView().addView(mCustomRight, params);
            switch (user.getRelationType()) {
                case 0:
                    mCustomRight.setText(R.string.studio_focus);
                    mCustomRight.setTextColor(getResources().getColor(android.R.color.white));
                    mCustomRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            payAttention(user);
                        }
                    });
                    break;
                case 1:
                    mCustomRight.setText(R.string.studio_has_focused);
                    mCustomRight.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                    mCustomRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelAttention(user);
                        }
                    });
                    break;
                case 2:
                    mCustomRight.setText(R.string.studio_focus);
                    mCustomRight.setTextColor(getResources().getColor(android.R.color.white));
                    mCustomRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            payAttention(user);
                        }
                    });
                    break;
                case 3:
                    mCustomRight.setText(R.string.studio_focused_each);
                    mCustomRight.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
                    mCustomRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelAttention(user);
                        }
                    });
                    break;
            }
        }
        mStudioFragment.setSelfIntroduce(user.getSelfIntroduce());
        ImageLoaderUtils.getImageLoader(this)
                .displayImage(user.getUserAvatar(),
                        mTitleProfile,
                        ImageLoaderUtils.getRoundDisplayOptionsStill(getResources().getDimensionPixelSize(R.dimen.studio_profile_size)));
        mTitleName.setText(user.getName());
        if (user.getUserRoleEnum() == IdTypeEnum.STUDIO) {
            mStudioFragment.setTabTitle(
                    R.string.studio_tab_top_line,
                    R.string.studio_tab_glory,
                    R.string.studio_tab_works);
        } else {
            mStudioFragment.setTabTitle(
                    R.string.studio_tab_daily,
                    R.string.studio_tab_album,
                    R.string.studio_tab_info_details
            );
        }
        mSubTitleName.setText(mUser.getAccout());
        mStudioFragment.setUser(user);
        mStudioFragment.setOnCoverLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (MainApplication.userInfo == null || mUser.getUserId() != MainApplication.userInfo.getUserId()) {
                    return false;
                }
                ListDialogFragment.getInstance().setAdapter(new MyAdapter(REQUEST_CODE_COVER_GALLERY, REQUEST_CODE_COVER_CAMERA));
                ListDialogFragment.getInstance().show(getSupportFragmentManager(), TAG);
                return false;
            }
        });
        //mSubTitleName.setText(user.get);
    }

    private void cancelAttention (final User user) {
        //mCustomRight.setEnabled(false);
        CircleOperator.getInstance().cancelAttention(user.getUserId(), new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                mCustomRight.setEnabled(true);
                if (result.isSuccess()) {
                    if (user.getRelationType() == 3) {
                        user.setRelationType(2);
                        mCustomRight.setText(R.string.studio_focus);
                        mCustomRight.setTextColor(getResources().getColor(android.R.color.white));
                    } else if (user.getRelationType() == 1) {
                        user.setRelationType(0);
                        mCustomRight.setText(R.string.studio_focus);
                        mCustomRight.setTextColor(getResources().getColor(android.R.color.white));
                    }
                    mCustomRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            payAttention(user);
                        }
                    });
                }
            }
        });
    }

    private List<MomentsGroup> mMomentGroups = null;
    private void payAttention (final User user) {
        //mCustomRight.setEnabled(false);
        if (mMomentGroups != null) {
            showPopMenu(user, mMomentGroups);
        } else {
            CommonOperator.getInstance().getMomentsGroupsAsync(new UserInfoOperator.OnGetListener<List<MomentsGroup>>() {
                @Override
                public void onGet(boolean succeed, List<MomentsGroup> momentsGroups) {
                    if (succeed) {
                        mMomentGroups = momentsGroups;
                        showPopMenu(user, mMomentGroups);
                    }
                }
            });
        }

    }

    private void showPopMenu (final User user, List<MomentsGroup> momentsGroups) {
        PopupMenu popupMenu = new PopupMenu(StudioActivity.this, mCustomRight);
        popupMenu.inflate(R.menu.studio_menu);
        for (int i = 0; i < momentsGroups.size(); i++) {
            MomentsGroup group = momentsGroups.get(i);
            popupMenu.getMenu().add(R.id.studio_menu, group.id, i, group.name);
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                CircleOperator.getInstance().attention(user.getUserId(), item.getItemId(), new ApiOperationCallback<ReturnInfo<String>>() {
                    @Override
                    public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                        mCustomRight.setEnabled(true);
                        if (result.isSuccess()) {
                            if (user.getRelationType() == 0) {
                                mCustomRight.setText(R.string.studio_has_focused);
                                mCustomRight.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                                user.setRelationType(1);
                            } else if (user.getRelationType() == 2) {
                                mCustomRight.setText(R.string.studio_focused_each);
                                mCustomRight.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
                                user.setRelationType(3);
                            }
                            mCustomRight.setOnClickListener(null);
                        }
                    }
                });
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onMenuItemClick(StudioFragment.MenuItem item, int position) {
        Intent it = null;
        switch (item.id) {
            case R.id.studio_menu_introduce:
                it = new Intent(this, WebActivity.class);
                it.putExtra(WebActivity.KEY_URL, "http://www.meishuquan.net/Studio/StudioDesc.aspx?studioid=" + mUser.getUserId());
                it.putExtra(WebActivity.KEY_TITLE, getString(R.string.studio_introduce));
                /*it = new Intent(this, StudioDetailActivity.class);
                it.putExtra(StudioDetailActivity.KEY_STUDIO_INFO, mInfo);*/
                break;
            case R.id.studio_menu_album:
                it = new Intent(this, StudioAlbumActivity.class);
                break;
            case R.id.studio_menu_team:
                it = new Intent(this, TeacherTeamActivity.class);
                break;
            case R.id.studio_menu_course_arrangement:
                it = new Intent(this, CourseArrangementActivity.class);
                break;
            case R.id.studio_menu_video:
                it = new Intent(this, VideoListActivity.class);
                break;
            case R.id.studio_menu_charge:
                /*it = new Intent(this, ChargeActivity.class);*/
                it = new Intent(this, WebActivity.class);
                it.putExtra(WebActivity.KEY_URL, "http://www.meishuquan.net/studio/ChargeStandardView.ASPX?studioid=" + mUser.getUserId());
                it.putExtra(WebActivity.KEY_TITLE, getString(R.string.studio_charge));
                break;
            case R.id.studio_menu_book_publish:
                it = new Intent(this, BookListActivity.class);
                break;
            case R.id.studio_menu_contact_us:
                //TODO
                it = new Intent(this, ContactUsActivity.class);
                break;
        }
        it.putExtra(StudioBaseInfo.KEY_STUDIO_ID, mUser.getUserId());
        startActivity(it);
    }

    private void loadStudioInfo (long sutdioId, UserInfoOperator.OnGetListener<StudioBaseInfo> listener) {
        StudioOperator.getInstance().getStudioBaseInfo(sutdioId, listener);
    }

    private void loadUser (long userId, UserInfoOperator.OnGetListener<User> listener) {
        UserInfoOperator.getInstance().getUserInfo(userId, listener);
    }

    private void loadFirstTab () {
        final ListView lv = mStudioFragment.getListView();
        if (lv != null) {
            lv.setOnItemClickListener(null);
        }
        if (mUser != null) {
            if (mUser.getUserRoleEnum() == IdTypeEnum.STUDIO) {
                if (mNewsList == null || mNewsList.isEmpty()) {
                    mAdapter = StudioFragment.EmptyAdapter.getInstance(this);
                    StudioOperator.getInstance().getMyNewsList(mUser.getUserId(), 0, new UserInfoOperator.OnGetListener<List<News>>() {
                        @Override
                        public void onGet(boolean succeed, List<News> newses) {
                            if (succeed) {
                                mNewsList = newses;
                                if (mStudioFragment.getCheckTabId() == R.id.studio_list_header_tab1) {
                                    mAdapter = new ToplineCustomAdapter(StudioActivity.this, mNewsList);
                                    mStudioFragment.setAdapter(mAdapter);
                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            News news = mNewsList.get(i - 1);
                                            int newsId = news.getNewsId();

                                            ItemInfoFragment itemInfoFragment = new ItemInfoFragment();
                                            itemInfoFragment.setTitleBarVisible(false);
                                            Bundle args = new Bundle();
                                            args.putInt("newsId", newsId);
                                            args.putString(ItemInfoFragment.KEY_SHARE_IMG_URL, news.getImgUrl());
                                            itemInfoFragment.setArguments(args);

                                            FragmentManager fm = StudioActivity.this.getSupportFragmentManager();
                                            FragmentTransaction ft = fm.beginTransaction();
//            ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                                            ft.add(R.id.content_container, itemInfoFragment);
                                            ft.addToBackStack(null);
                                            ft.commit();
                                            }
                                    });
                                }
                            }
                        }
                    });
                } else {
                    if (!(mAdapter instanceof ToplineCustomAdapter)) {
                        mAdapter = new ToplineCustomAdapter(StudioActivity.this, mNewsList);
                        mStudioFragment.setAdapter(mAdapter);
                    }
                    final News lastNews = mNewsList.get(mNewsList.size() - 1);
                    StudioOperator.getInstance().getMyNewsList(mUser.getUserId(), lastNews.getNewsId(), new UserInfoOperator.OnGetListener<List<News>>() {
                        @Override
                        public void onGet(boolean succeed, List<News> newses) {
                            if (succeed) {
                                mNewsList.addAll(newses);
                                for (News n : newses) {
                                    int index = mNewsList.indexOf(n);
                                    if (index >= 0) {
                                        continue;
                                    }
                                    mNewsList.add(n);
                                }
                                if (mStudioFragment.getCheckTabId() == R.id.studio_list_header_tab1) {
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
                }
            } else {
                if (mCircleList == null || mCircleList.isEmpty()) {
                    mAdapter = StudioFragment.EmptyAdapter.getInstance(this);
                    StudioOperator.getInstance().getMyCircleList(mUser.getUserId(), 0, new UserInfoOperator.OnGetListener<List<CCircleDetailModel>>() {
                        @Override
                        public void onGet(boolean succeed, List<CCircleDetailModel> cCircleDetailModels) {
                            if (succeed) {
                                mCircleList = cCircleDetailModels;

                                if (mStudioFragment.getCheckTabId() == R.id.studio_list_header_tab1) {
                                    mAdapter = new CircleMomentAdapter(StudioActivity.this, cCircleDetailModels, null);
                                    mStudioFragment.setAdapter(mAdapter);
                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            MomentDetailFragment momentDetailFragment = new MomentDetailFragment();
                                            momentDetailFragment.setTitleViewVisible(View.GONE);
//                Bundle args = new Bundle();
//                args.putInt("newsId", newsId);
//                itemInfoFragment.setArguments(args);

                                            // refresh load more listview has header
                                            GlobalData.moment = mCircleList.get(position - 1);

                                            FragmentManager fm = StudioActivity.this.getSupportFragmentManager();
                                            FragmentTransaction ft = fm.beginTransaction();
                                            ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                                            ft.add(R.id.content_container, momentDetailFragment);
                                            ft.addToBackStack(null);
                                            ft.commit();
                                        }
                                    });
                                }

                            }
                        }
                    });
                } else {
                    if (!(mAdapter instanceof  CircleMomentAdapter)) {
                        mAdapter = new CircleMomentAdapter(StudioActivity.this, mCircleList, null);
                        mStudioFragment.setAdapter(mAdapter);
                    }
                    final CCircleDetailModel lastMode = mCircleList.get(mCircleList.size() - 1);
                    StudioOperator.getInstance().getMyCircleList(mUser.getUserId(), lastMode.id, new UserInfoOperator.OnGetListener<List<CCircleDetailModel>>() {
                        @Override
                        public void onGet(boolean succeed, List<CCircleDetailModel> cCircleDetailModels) {
                            if (succeed) {
                                for (CCircleDetailModel model : cCircleDetailModels) {
                                    int index = mCircleList.indexOf(model);
                                    /*boolean equalsLast = lastMode.equals(model);
                                    Log.v(TAG, "equalsLast=" + equalsLast);*/
                                    if (index >= 0) {
                                        continue;
                                    }
                                    mCircleList.add(model);
                                }
                                if (mStudioFragment.getCheckTabId() == R.id.studio_list_header_tab1) {
                                    mAdapter.notifyDataSetChanged();
                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            MomentDetailFragment momentDetailFragment = new MomentDetailFragment();
                                            momentDetailFragment.setTitleViewVisible(View.GONE);
//                Bundle args = new Bundle();
//                args.putInt("newsId", newsId);
//                itemInfoFragment.setArguments(args);

                                            // refresh load more listview has header
                                            GlobalData.moment = mCircleList.get(position - 1);

                                            FragmentManager fm = StudioActivity.this.getSupportFragmentManager();
                                            FragmentTransaction ft = fm.beginTransaction();
                                            ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                                            ft.add(R.id.content_container, momentDetailFragment);
                                            ft.addToBackStack(null);
                                            ft.commit();
                                        }
                                    });
                                }
                            }
                        }
                    });
                }

            }
        } else {
            mAdapter = StudioFragment.EmptyAdapter.getInstance(this);
        }
    }

    private void loadSecondTab () {
        Log.v(TAG, "loadSecondTab");
        ListView lv = mStudioFragment.getListView();
        if (lv != null) {
            lv.setOnItemClickListener(null);
        }
        if (mUser != null) {
            if (mUser.getUserRoleEnum() == IdTypeEnum.STUDIO) {
                //辉煌成绩
                if (mAchievementList == null || mAchievementList.isEmpty()) {
                    mAdapter = StudioFragment.EmptyAdapter.getInstance(this);
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage(getString(R.string.more));
                    progressDialog.show();
                    StudioOperator.getInstance().getAchievementList(mUser.getUserId(), 0, new UserInfoOperator.OnGetListener<List<Achievement>>() {
                        @Override
                        public void onGet(boolean succeed, List<Achievement> achievements) {
                            progressDialog.dismiss();
                            if (succeed) {
                                List<AchievementAdapter.AchievementDelegate> delegates = new ArrayList<AchievementAdapter.AchievementDelegate>();
                                for (Achievement a : achievements) {
                                    delegates.add(new AchievementAdapter.AchievementDelegate(a));
                                }
                                mAchievementList = delegates;
                                if (mStudioFragment.getCheckTabId() == R.id.studio_list_header_tab2) {
                                    mAdapter = new AchievementAdapter(StudioActivity.this, mAchievementList);
                                    mStudioFragment.setAdapter(mAdapter);
                                }
                            }

                        }
                    });
                } else {
                    if (!(mAdapter instanceof AchievementAdapter)) {
                        mAdapter = new AchievementAdapter(this, mAchievementList);
                        mStudioFragment.setAdapter(mAdapter);
                    }
                    final AchievementAdapter.AchievementDelegate lastAchievement = mAchievementList.get(mAchievementList.size() - 1);
                    StudioOperator.getInstance().getAchievementList(mUser.getUserId(), lastAchievement.achievement.getAchievementId(), new UserInfoOperator.OnGetListener<List<Achievement>>() {
                        @Override
                        public void onGet(boolean succeed, List<Achievement> achievements) {
                            if (succeed) {
                                for (Achievement a : achievements) {
                                    AchievementAdapter.AchievementDelegate delegate = new AchievementAdapter.AchievementDelegate(a);
                                    int index = mAchievementList.indexOf(delegate);
                                    if (index >= 0) {
                                        continue;
                                    }
                                    mAchievementList.add(delegate);
                                }
                                //mAchievementList.addAll(achievements);
                                if (mStudioFragment.getCheckTabId() == R.id.studio_list_header_tab2) {
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                    });
                }
            } else {
                if (mWorkInfoList == null || mWorkInfoList.isEmpty()) {
                    mAdapter = StudioFragment.EmptyAdapter.getInstance(this);
                    UserInfoOperator.getInstance().getMyPhoto(mUser.getUserId(), new UserInfoOperator.OnGetListener<List<Profile>>() {
                        @Override
                        public void onGet(boolean succeed, List<Profile> profiles) {
                            if (succeed) {
                                List<WorkInfo> workInfos = new ArrayList<WorkInfo>();
                                for (Profile p : profiles) {
                                    workInfos.add(convertFrom(p));
                                }
                                mWorkInfoList = workInfos;
                                if (mStudioFragment.getCheckTabId() == R.id.studio_list_header_tab2) {
                                    mAdapter = new WorkAdapter(StudioActivity.this, mWorkInfoList);
                                    mStudioFragment.setAdapter(mAdapter);
                                }
                            }
                        }
                    });
                } else {
                    if (!(mAdapter instanceof WorkAdapter)) {
                        mAdapter = new WorkAdapter(StudioActivity.this, mWorkInfoList);
                        mStudioFragment.setAdapter(mAdapter);
                    }
                }
                //获取个人相册
                /*Log.v(TAG, "1 getWorks mWorkInfoList == null " + (mWorkInfoList == null));
                if (mWorkInfoList == null || mWorkInfoList.isEmpty()) {
                    mAdapter = StudioFragment.EmptyAdapter.getInstance(this);
                    StudioOperator.getInstance().getWorks(mUser.getUserId(), 0, 0, new UserInfoOperator.OnGetListener<List<WorkInfo>>() {
                        @Override
                        public void onGet(boolean succeed, List<WorkInfo> workInfo) {
                            if (succeed) {
                                mWorkInfoList = workInfo;
                                if (mStudioFragment.getCheckTabId() == R.id.studio_list_header_tab2) {
                                    mAdapter = new WorkAdapter(StudioActivity.this, mWorkInfoList);
                                    mStudioFragment.setAdapter(mAdapter);
                                }
                                Log.v(TAG, "2 getWorks mWorkInfoList == null " + (mWorkInfoList == null));

                            }
                        }
                    });
                } else {
                    if (!(mAdapter instanceof WorkAdapter)) {
                        mAdapter = new WorkAdapter(StudioActivity.this, mWorkInfoList);
                        mStudioFragment.setAdapter(mAdapter);
                    }
                    WorkInfo lastWork = mWorkInfoList.get(mWorkInfoList.size() - 1);
                    StudioOperator.getInstance().getWorks(mUser.getUserId(), lastWork.getStudioAlbumId(), 0, new UserInfoOperator.OnGetListener<List<WorkInfo>>() {
                        @Override
                        public void onGet(boolean succeed, List<WorkInfo> workInfo) {
                            if (succeed) {
                                for (WorkInfo info : workInfo) {
                                    int index = mWorkInfoList.indexOf(info);
                                    if (index >= 0) {
                                        continue;
                                    }
                                    mWorkInfoList.add(info);
                                }
                                if (mStudioFragment.getCheckTabId() == R.id.studio_list_header_tab2) {
                                    *//*mAdapter = new WorkAdapter(StudioActivity.this, mWorkInfoList);
                                    mStudioFragment.setAdapter(mAdapter);*//*
                                    mAdapter.notifyDataSetChanged();
                                }
                                Log.v(TAG, "2 getWorks mWorkInfoList == null " + (mWorkInfoList == null));

                            }
                        }
                    });
                }*/

            }
        } else {
            mAdapter = StudioFragment.EmptyAdapter.getInstance(this);
        }
    }

    private WorkInfo convertFrom (Profile profile) {
        WorkInfo info = new WorkInfo();
        info.setPhotoThumbnail(profile.getThumbnails());
        info.setPhotoUrl(profile.getOriginalImage());
        return info;
    }

    private void loadThirdTab () {
        Log.v(TAG, "loadThirdTab");
        ListView lv = mStudioFragment.getListView();
        if (lv != null) {
            lv.setOnItemClickListener(null);
        }
        if (mUser != null) {
            if (mUser.getUserRoleEnum() == IdTypeEnum.STUDIO) {
                //获取画室作品信息
                if (mWorkInfoList == null || mWorkInfoList.isEmpty()) {
                    mAdapter = StudioFragment.EmptyAdapter.getInstance(this);
                    StudioOperator.getInstance().getWorks(mUser.getUserId(), 0, 1, new UserInfoOperator.OnGetListener<List<WorkInfo>>() {
                        @Override
                        public void onGet(boolean succeed, List<WorkInfo> workInfo) {
                            if (succeed) {
                                mWorkInfoList = workInfo;
                                if (mStudioFragment.getCheckTabId() == R.id.studio_list_header_tab3) {
                                    mAdapter = new WorkAdapter(StudioActivity.this, mWorkInfoList);
                                    mStudioFragment.setAdapter(mAdapter);
                                }

                            }
                        }
                    });
                } else {
                    if (!(mAdapter instanceof WorkAdapter)) {
                        mAdapter = new WorkAdapter(StudioActivity.this, mWorkInfoList);
                        mStudioFragment.setAdapter(mAdapter);
                    }
                    final WorkInfo info = mWorkInfoList.get(mWorkInfoList.size() - 1);
                    StudioOperator.getInstance().getWorks(mUser.getUserId(), info.getStudioAlbumId(), 1, new UserInfoOperator.OnGetListener<List<WorkInfo>>() {
                        @Override
                        public void onGet(boolean succeed, List<WorkInfo> workInfo) {
                            if (succeed) {
                                for (WorkInfo info : workInfo) {
                                    int index = mWorkInfoList.indexOf(info);
                                    if (index >= 0) {
                                        continue;
                                    }
                                    mWorkInfoList.add(info);
                                }
                                if (mStudioFragment.getCheckTabId() == R.id.studio_list_header_tab3) {
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
                }
            } else {
                //InfoAdapter adapter = new InfoAdapter(StudioActivity.this, mUser);
                //adapter.setOnInfoItemClickListener(StudioActivity.this);
                UserInfoAdapter adapter = new UserInfoAdapter(this, mUser, mUser.getUserId() == MainApplication.userInfo.getUserId());
                adapter.setOnClickListener(mInfoClickListener);
                mAdapter = adapter;
            }
        } else {
            mAdapter = StudioFragment.EmptyAdapter.getInstance(this);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.studio_list_header_tab1:
                loadFirstTab();
                break;
            case R.id.studio_list_header_tab2:
                loadSecondTab();
                break;
            case R.id.studio_list_header_tab3:
                loadThirdTab();
                break;
        }
        mStudioFragment.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            /*case InputActivity.REQUEST_CODE_NICK:
                if (resultCode == RESULT_OK) {
                    CharSequence nick = data.getCharSequenceExtra(InputActivity.KEY_DEFAULT_STR);
                    adapter.setNickName(nick.toString());
                    UserManager.updateMyInfo(User.KEY_NICK_NAME, nick.toString());
                }
                break;
            case InputActivity.REQUEST_CODE_RECENTS:
                if (resultCode == RESULT_OK) {
                    String recents = data.getCharSequenceExtra(InputActivity.KEY_DEFAULT_STR).toString();
                    UserManager.updateMyInfo(User.KEY_NICK_NAME, recents);
                    adapter.setSelfSignature(recents);
                }
                break;
            case InputActivity.REQUEST_CODE_MEISHUQUAN_ID:
                if (resultCode == RESULT_OK) {
                    String id = data.getCharSequenceExtra(InputActivity.KEY_DEFAULT_STR).toString();
                    //Pattern pattern = new Pattern("");
                    if (!PatternUtils.PATTERN_MEISHUQUAN_ID.matcher(id.toString()).matches()) {
                        Toast.makeText(this, R.string.info_meishuquan_id_illegal, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    UserManager.updateMyInfo(User.KEY_ACCOUNT, id);
                    adapter.setMeishuquanId(id);
                }
                break;
            case InputActivity.REQUEST_CODE_DEPARTMENT_ADDRESS:
                if (resultCode == RESULT_OK) {
                    String address = data.getCharSequenceExtra(InputActivity.KEY_DEFAULT_STR).toString();
                    UserManager.updateMyInfo(User.KEY_LOCATIONADDRESS, address);
                    adapter.setDepartmentAddress(address);
                }
                break;*/
            case REQUEST_CODE_DEPARTMENT:
                if (resultCode == RESULT_OK) {
                    long id = data.getIntExtra(User.KEY_USER_ID, 0);
                    String name = data.getStringExtra(User.KEY_NICK_NAME);
                    //String address = data.getStringExtra(User.KEY_LOCATIONADDRESS);
                    //mUser.setLocationAddress(address);
                    if (mAdapter instanceof UserInfoAdapter) {
                        ((UserInfoAdapter)mAdapter).setStudioName(name);
                        mAdapter.notifyDataSetChanged();
                    }
                    UserManager.updateMyInfo(User.KEY_LOCATION_STUDIO, id + "");
                }
                break;
            case REQUEST_CODE_SCHOOL:
                if (resultCode == RESULT_OK) {
                    //long id = data.getIntExtra(User.KEY_USER_ID, 0);
                    String name = data.getStringExtra(User.KEY_NICK_NAME);
                    //String address = data.getStringExtra(User.KEY_LOCATIONADDRESS);
                    mUser.setLocationSchool(name);
                    //mUser.setLocationAddress(address);
                    if (mAdapter instanceof UserInfoAdapter) {
                        ((UserInfoAdapter)mAdapter).setLocationSchool(name);
                        mAdapter.notifyDataSetChanged();
                    }
                    UserManager.updateMyInfo(User.KEY_LOCATION_SCHOOL, name);
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {

                    Bitmap bmp = (Bitmap)data.getExtras().get("data");

                    try {
                        File file = new File(getCacheDir(), System.currentTimeMillis() + ".jpg");
                        FileOutputStream fos = new FileOutputStream(file);
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        Log.v(TAG, "onActivityResult mCameraPath=" + file.getAbsolutePath());
                        if (mUser != null) {
                            mUser.setUserAvatar(file.getPath());
                        }
                        final int size = getResources().getDimensionPixelSize(R.dimen.studio_profile_size);
                        ImageLoaderUtils.getImageLoader(this).displayImage(ImageDownloader.Scheme.FILE.wrap(file.getPath()), mTitleProfile, ImageLoaderUtils.getRoundDisplayOptionsStill(size));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    //UserInfoOperator.getInstance().updateUserProfile(MainApplication.userInfo.getUserId(), mCameraOutputPath);
                    UserInfoOperator.getInstance().updateUserProfile(MainApplication.userInfo.getUserId(), bmp);

                    mAdapter.notifyDataSetChanged();
                }
                break;
            case REQUEST_CODE_GALLERY:
                if (resultCode == RESULT_OK) {
                    final String path = ImageLoaderUtils.getRealFilePath(this, data.getData());
                    Log.v(TAG, "onActivityResult " + path);
                    final int profileSize = getResources().getDimensionPixelSize(R.dimen.studio_profile_size);
                    mUser.setUserAvatar(ImageDownloader.Scheme.FILE.wrap(path));
                    ImageLoaderUtils.getImageLoader(this).displayImage(ImageDownloader.Scheme.FILE.wrap(path), mTitleProfile, ImageLoaderUtils.getRoundDisplayOptionsStill(profileSize));
                    UserInfoOperator.getInstance().updateUserProfile(MainApplication.userInfo.getUserId(), path);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case InfoActivity.REQUEST_CODE_CHOOSE_COURSE:
                if (resultCode == RESULT_OK) {
                    List<CourseChannelItem> list = (List<CourseChannelItem>)data.getExtras().getSerializable("tags");
                    /*JsonArray array = new JsonArray();
                    for (CourseChannelItem item : list) {
                        JsonObject object = new JsonObject();
                        object.addProperty(CourseChannelItem.KEY_CHANNEL_ID, item.getChannelId());
                        object.addProperty(CourseChannelItem.KEY_CHANNEL_NAME, item.getChannelName());
                        array.add(object);
                    }
                    String json = array.toString().replaceAll("\\{", "\\(");
                    json = json.replaceAll("\\}", "\\)");*/
                    StringBuilder builder = new StringBuilder();
                    StringBuilder nameBuilder = new StringBuilder();
                    for (CourseChannelItem item : list) {
                        builder.append(item.getChannelId() + ",");
                        nameBuilder.append(item.getChannelName() + " ");
                    }
                    UserManager.updateMyInfo(User.KEY_GOODSUBJECTS, builder.toString());
                    mUser.setGoodSubjects(builder.toString());
                    if (mAdapter instanceof UserInfoAdapter) {
                        ((UserInfoAdapter)mAdapter).setGoodAtSubjects(nameBuilder.toString());
                        mAdapter.notifyDataSetChanged();
                    }
                    Log.v(TAG, "REQUEST_CODE_CHOOSE_COURSE " + builder);
                }
                break;
            case REQUEST_CODE_COVER_GALLERY:
                if (resultCode == RESULT_OK) {
                    final String path = ImageLoaderUtils.getRealFilePath(this, data.getData());
                    Log.v(TAG, "onActivityResult " + path);
                    final int profileSize = getResources().getDimensionPixelSize(R.dimen.studio_profile_size);
                    mUser.setBackgroundImg(ImageDownloader.Scheme.FILE.wrap(path));
                    mStudioFragment.setCover(path);
                    UserInfoOperator.getInstance().updateUserCover(MainApplication.userInfo.getUserId(), path);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case REQUEST_CODE_COVER_CAMERA:
                if (resultCode == RESULT_OK) {
                    Log.v(TAG, "onActivityResult mCameraPath=" + mCameraOutputPath);
                    mUser.setBackgroundImg(ImageDownloader.Scheme.FILE.wrap(mCameraOutputPath));
                    final int size = getResources().getDimensionPixelSize(R.dimen.studio_profile_size);
                    UserInfoOperator.getInstance().updateUserCover(MainApplication.userInfo.getUserId(), mCameraOutputPath);
                    mStudioFragment.setCover(mCameraOutputPath);
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onItemClick(View view, String name) {
        ListDialogFragment.getInstance().dismiss();
        if (mAdapter != null && mAdapter instanceof InfoAdapter) {
            InfoAdapter adapter = (InfoAdapter) mAdapter;
            adapter.setConstellation(name);
            UserManager.updateMyInfo(User.KEY_HOROSCOPE, name);
        }
    }

    /*@Override
    public void onClick(View view, InfoAdapter.Item item, User user) {
        if (item.title.equals(getString(R.string.info_nick))) {
            startInputActivityForResult(getString(R.string.info_modify_nick), item.content, true, InputActivity.REQUEST_CODE_NICK);
        } else if (item.title.equals(getString(R.string.info_recents))) {
            startInputActivityForResult(getString(R.string.info_recents), item.content, false, InputActivity.REQUEST_CODE_RECENTS);
        } else if (item.title.equals(getString(R.string.info_gender))) {
            showDialog();
        } else if (item.title.equals(getString(R.string.info_ages))) {
            showBirthdayDialog();
        } else if (item.title.equals(getString(R.string.info_constellation))) {
            ListDialogFragment fragment = ListDialogFragment.getInstance();
            ConstellationAdapter adapter = ConstellationAdapter.getInstance(this, item.content);
            adapter.setOnItemClickListener(this);
            fragment.setAdapter(adapter);
            fragment.show(getSupportFragmentManager(), TAG);
        } else if (item.title.equals(getString(R.string.info_provience))) {
            ListDialogFragment fragment = ListDialogFragment.getInstance();
            SimplePrvsAdapter adapter = SimplePrvsAdapter.getInstance(this, item.content);
            adapter.setOnItemClickListener(mPrvsListener);
            fragment.setAdapter(adapter);
            fragment.show(getSupportFragmentManager(), TAG);
        } else if (item.title.equals(getString(R.string.info_meishuquan_id))) {
            startInputActivityForResult(item.title, item.content, true, InputActivity.REQUEST_CODE_MEISHUQUAN_ID);
        } else if (item.title.equals(getString(R.string.info_department_address))) {
            startInputActivityForResult(getString(R.string.info_department_address), item.content, false, InputActivity.REQUEST_CODE_DEPARTMENT_ADDRESS);
        }
    }*/

    public String camera(int requestCode) {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir = new File(Environment.getExternalStorageDirectory()
                + "/myimage/");
        if (!dir.exists()) {
            dir.mkdir();
        } else {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    files[i].delete();
                }
            }
        }
        File file = new File(dir, String.valueOf(System.currentTimeMillis())
                + ".jpg");
        //path = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        //openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, requestCode);
        Log.v(TAG, "file.absPath=" + file.getAbsolutePath());
        return file.getAbsolutePath();
    }

    public void pickFromGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
        startActivityForResult(intent, requestCode);
    }

    private Dialog mDialog = null;
    private String mGender = null;
    public void showDialog () {
        if (MainApplication.userInfo == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.info_gender);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_gender_chooser, null);
        builder.setView(contentView);
        builder.setPositiveButton(R.string.gender_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDialog.dismiss();
                mAdapter.notifyDataSetChanged();
            }
        });
        RadioGroup group = (RadioGroup)contentView.findViewById(R.id.gender_group);
        RadioButton female = (RadioButton)contentView.findViewById(R.id.gender_famale);
        RadioButton male = (RadioButton)contentView.findViewById(R.id.gender_male);
        final String femaleStr = getString(R.string.gender_famale);
        final String maleStr = getString(R.string.gender_male);

        if (mGender == null) {
            mGender = mUser.getGender();
        }
        if (mGender.equals(femaleStr)) {
            female.setChecked(true);
        } else {
            male.setChecked(true);
        }
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.gender_famale) {
                    mGender = femaleStr;
                } else {
                    mGender = maleStr;
                }
                mUser.setGender(mGender);
                UserManager.updateMyInfo(User.KEY_GENDER, mGender);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter != null && mAdapter instanceof UserInfoAdapter) {
                            Log.v(TAG, "showDialog " + mGender + " try to notify refresh");
                            UserInfoAdapter adapter = (UserInfoAdapter) mAdapter;
                            adapter.setGender(mGender);

                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                //updateInfo(User.KEY_GENDER, mGenderView.getSecondaryText().toString());
            }
        });
        mDialog = builder.create();
        mDialog.show();
    }

    private DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, final int i, final int i1, final int i2) {
            int nowYear = Calendar.getInstance().get(Calendar.YEAR);
            int nowMonth = Calendar.getInstance().get(Calendar.MONTH);
            int nowDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            if (i > nowYear) {
                Toast.makeText(StudioActivity.this, R.string.info_illegal_birthday, Toast.LENGTH_SHORT).show();
                return;
            } else if (i == nowYear) {
                if (i1 > nowMonth){
                    Toast.makeText(StudioActivity.this, R.string.info_illegal_birthday, Toast.LENGTH_SHORT).show();
                    return;
                } else if (i1 == nowMonth) {
                    if (i2 > nowDay) {
                        Toast.makeText(StudioActivity.this, R.string.info_illegal_birthday, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mAdapter != null && mAdapter instanceof UserInfoAdapter) {
                        String birthday = PatternUtils.formatToDateStr(i, i1 + 1, i2);
                        Log.v(TAG, "onDateSet birthday=" + birthday + " adapter=" + mAdapter.getClass().getSimpleName());
                        mUser.setBirthday(birthday + "T00:00:00Z");
                        UserManager.updateMyInfo(User.KEY_BIRTHDAY, birthday);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            });

            Log.v(TAG, "onDateSet " + i + " " + i1 + " " + i2);


        }
    };

    public void showBirthdayDialog () {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar,
                mDateListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private View.OnClickListener mInfoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!canEdit) {
                return;
            }
            Intent it = null;
            switch (v.getId()) {
                case R.id.info_profile_container:
                    ListDialogFragment.getInstance().setAdapter(new MyAdapter(REQUEST_CODE_GALLERY, REQUEST_CODE_CAMERA));
                    ListDialogFragment.getInstance().show(getSupportFragmentManager(), TAG);
                    break;
                case R.id.info_nick:
                    showDialogAndUpdate(getString(R.string.info_nick), mUser.getName(), "", new InputDialogFragment.OnOkListener() {
                        @Override
                        public void onOkClick(View view, CharSequence cs) {
                            if (PatternUtils.PATTERN_NICK_NAME.matcher(cs).matches()) {
                                UserManager.updateMyInfo(User.KEY_NICK_NAME, cs.toString());
                                mUser.setName(cs.toString());
                            } else {
                                Toast.makeText(StudioActivity.this, R.string.studio_wrong_format, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    //startInputActivityForResult(getString(R.string.info_modify_nick), mUser.getName(), true, InputActivity.REQUEST_CODE_NICK);
                    break;
                case R.id.info_meishuquan_id:

                    if (TextUtils.isEmpty(mUser.getAccout())) {
                        showDialogAndUpdate(getString(R.string.info_meishuquan_id), "", getString(R.string.info_meishuquan_tip), new InputDialogFragment.OnOkListener() {
                            @Override
                            public void onOkClick(View view, CharSequence cs) {
                                if (PatternUtils.PATTERN_MEISHUQUAN_ID.matcher(cs).matches()) {
                                    mUser.setAccout(cs.toString());
                                    UserManager.updateMyInfo(User.KEY_ACCOUNT, cs.toString());
                                } else {
                                    Toast.makeText(StudioActivity.this, R.string.studio_wrong_format, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    break;
                case R.id.info_recents_container:
                    showDialogAndUpdate(getString(R.string.info_recents), mUser.getSelfSignature(), getString(R.string.info_recents), new InputDialogFragment.OnOkListener() {
                        @Override
                        public void onOkClick(View view, CharSequence cs) {
                            mUser.setSelfSignature(cs.toString());
                            UserManager.updateMyInfo(User.KEY_SELFSIGNATURE, cs.toString());
                        }
                    });
                    //startInputActivityForResult(getString(R.string.info_recents), mRecentsContentTv.getText(), false, InputActivity.REQUEST_CODE_RECENTS);
                    break;
                case R.id.info_gender:
                    showDialog();
                    break;
                case R.id.info_age:
                    showBirthdayDialog();
                    //startInputActivityForResult(getString(R.string.info_ages), mAgeView.getSecondaryText(), true, InputActivity.REQUEST_CODE_AGE, InputType.TYPE_CLASS_NUMBER);
                    break;
                case R.id.info_constellation:
                    final ListDialogFragment fragment = ListDialogFragment.getInstance();
                    ConstellationAdapter adapter = ConstellationAdapter.getInstance(StudioActivity.this, mUser.getHoroscope());
                    adapter.setOnItemClickListener(new ConstellationAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, String name) {
                            mUser.setHoroscope(name);
                            UserManager.updateMyInfo(User.KEY_HOROSCOPE, name);
                            fragment.dismiss();
                            if (mAdapter != null && mAdapter instanceof UserInfoAdapter) {
                                UserInfoAdapter userInfoAdapter = (UserInfoAdapter)mAdapter;
                                userInfoAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    fragment.setAdapter(adapter);
                    fragment.show(getSupportFragmentManager(), TAG);
                    break;
                case R.id.info_department_address:
                    /*showDialogAndUpdate(getString(R.string.info_department_address), mUser.getLocationAddress(), getString(R.string.info_department_address), User.KEY_LOCATIONADDRESS, new InputDialogFragment.OnOkListener() {
                        @Override
                        public void onOkClick(View view, CharSequence cs) {
                            mUser.setLocationAddress(cs.toString());
                        }
                    });*/
                    break;
                case R.id.info_cv:
                    showDialogAndUpdate(getString(R.string.info_cv), mUser.getUserResume(), getString(R.string.info_cv), new InputDialogFragment.OnOkListener() {
                        @Override
                        public void onOkClick(View view, CharSequence cs) {
                            mUser.setUserResume(cs.toString());
                            UserManager.updateMyInfo(User.KEY_USER_RESUME, cs.toString());
                        }
                    });
                    break;
                case R.id.info_achievement:
                    showDialogAndUpdate(getString(R.string.info_achievement), mUser.getAchievement(), getString(R.string.info_achievement), new InputDialogFragment.OnOkListener() {
                        @Override
                        public void onOkClick(View view, CharSequence cs) {
                            mUser.setAchievement(cs.toString());
                            UserManager.updateMyInfo(User.KEY_ACHIEVEMENT, cs.toString());
                        }
                    });
                    break;
                case R.id.info_department:
                    int requestCode = REQUEST_CODE_SCHOOL;
                    if (mUser.getUserRoleEnum() != IdTypeEnum.STUDENT) {
                        requestCode = REQUEST_CODE_DEPARTMENT;
                    }
                    Intent departIt = new Intent(StudioActivity.this, DepartmentActivity.class);
                    departIt.putExtra(DepartmentActivity.KEY_REQUEST_CODE, requestCode);
                    startActivityForResult(departIt, requestCode);
                    break;
                case R.id.info_school:
                    Intent schoolIt = new Intent(StudioActivity.this, DepartmentActivity.class);
                    schoolIt.putExtra(DepartmentActivity.KEY_CONTENT, mUser.getLocationSchool());
                    schoolIt.putExtra(DepartmentActivity.KEY_REQUEST_CODE, REQUEST_CODE_SCHOOL);
                    startActivityForResult(schoolIt, REQUEST_CODE_SCHOOL);
                    break;
                case R.id.info_studio:
                    Intent studioIt = new Intent (StudioActivity.this, DepartmentEditActivity.class);
                    //TODO studioIt.putExtra(DepartmentActivity.KEY_CONTENT, mUser.getStu)
                    studioIt.putExtra(DepartmentActivity.KEY_REQUEST_CODE, REQUEST_CODE_DEPARTMENT);
                    startActivityForResult(studioIt, REQUEST_CODE_DEPARTMENT);
                    break;
                case R.id.info_provience:
                    ListDialogFragment provinceFragment = ListDialogFragment.getInstance();
                    SimplePrvsAdapter provinceAdapter = SimplePrvsAdapter.getInstance(StudioActivity.this, mUser.getRegion());
                    provinceAdapter.setOnItemClickListener(mPrvsListener);
                    provinceFragment.setAdapter(provinceAdapter);
                    provinceFragment.show(getSupportFragmentManager(), TAG);
                    break;
                case R.id.info_good_at:
                    List<CourseChannelItem> mCourseItems = null;
                    it = new Intent (StudioActivity.this, ChooseCourseActivity.class);
                    it.putExtra(ChooseCourseActivity.OLDSELECTEDCHANNELITEMS, (Serializable) mCourseItems);
                    startActivityForResult(it, InfoActivity.REQUEST_CODE_CHOOSE_COURSE);
                    break;
                /*case R.id.info_provience:
                    showCityFragment();
                    break;
                case R.id.info_department:
                    startActivity(new Intent (this, DepartmentActivity.class));
                    break;
                case R.id.info_department_address:
                    startInputActivityForResult(getString(R.string.info_department_address), mDepartmentAddrView.getSecondaryText(), false, InputActivity.REQUEST_CODE_DEPARTMENT_ADDRESS);
                    break;
                case R.id.info_good_at:
                    it = new Intent (this, ChooseCourseActivity.class);
                    it.putExtra(ChooseCourseActivity.OLDSELECTEDCHANNELITEMS, (Serializable)mCourseItems);
                    startActivityForResult(it, REQUEST_CODE_CHOOSE_COURSE);
                    break;
                case R.id.info_cv:
                    startInputActivityForResult(mCvView.getText().toString(), mCvView.getSecondaryText(), false, InputActivity.REQUEST_CODE_CV);
                    break;
                case R.id.info_achievement:
                    startInputActivityForResult(mAchievementView.getText().toString(), mAchievementView.getSecondaryText(), false, InputActivity.REQUEST_CODE_ACHIEVEMENT);
                    break;*/
            }
        }
    };

    private void showDialogAndUpdate (String title, String text, String hint, final InputDialogFragment.OnOkListener listener) {
        InputDialogFragment fragment = new InputDialogFragment ();
        fragment.setTitle(title);
        fragment.setText(text);
        fragment.setHint(hint);
        fragment.setOnOkListener(new InputDialogFragment.OnOkListener() {
            @Override
            public void onOkClick(View view, CharSequence cs) {
                if (listener != null) {
                    listener.onOkClick(view, cs);
                }
                //updateInfo(key, cs.toString());
            }
        });
        fragment.show(getSupportFragmentManager(), TAG);
        /*InputDialogFragment.getInstance(title, text, hint, new InputDialogFragment.OnOkListener() {
            @Override
            public void onOkClick(View view, CharSequence cs) {
                if (listener != null) {
                    listener.onOkClick(view, cs);
                }
                //updateInfo(key, cs.toString());
            }
        }).show(getSupportFragmentManager(), "");*/
    }



    /*public void updateInfo (String key, String value) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(key, value);
        UserInfoOperator.getInstance().updateUserInfo(MainApplication.userInfo.getUserId(), map);
    }*/

    class MyAdapter extends BaseAdapter {

        int[] mTitleResArr = {
                R.string.info_profile_gallery,
                R.string.info_profile_camera};

        int mRequestCodeGallery, mRequestCodeCamera;

        public MyAdapter (int requestCodeGallery, int requestCodeCamera) {
            mRequestCodeGallery = requestCodeGallery;
            mRequestCodeCamera = requestCodeCamera;
        }

        @Override
        public int getCount() {
            return mTitleResArr.length;
        }

        @Override
        public String getItem(int position) {
            return getString(mTitleResArr[position]);
        }

        @Override
        public long getItemId(int position) {
            return mTitleResArr[position];
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(StudioActivity.this).inflate(R.layout.layout_list_dialog_item, null);
            TextView tv = (TextView)convertView.findViewById(R.id.list_dialog_item);
            tv.setText(getItem(position));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (mTitleResArr[position]) {
                        case R.string.info_profile_gallery:
                            pickFromGallery(mRequestCodeGallery);
                            break;
                        case R.string.info_profile_camera:
                            mCameraOutputPath = camera(mRequestCodeCamera);
                            Log.v(TAG, "mCameraOutputPath " + mCameraOutputPath);
                            break;
                    }
                    ListDialogFragment.getInstance().dismiss();
                }
            });
            return tv;
        }

    }

    class NewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 200;
        }

        @Override
        public String getItem(int position) {
            return position + " new";
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(StudioActivity.this);
            tv.setText(getItem(position));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return tv;
        }
    }
}
