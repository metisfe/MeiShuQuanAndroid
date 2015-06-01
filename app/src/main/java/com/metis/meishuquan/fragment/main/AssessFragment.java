package com.metis.meishuquan.fragment.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.assess.AssessInfoActivity;
import com.metis.meishuquan.activity.info.ImagePreviewActivity;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.adapter.topline.DataHelper;
import com.metis.meishuquan.fragment.Topline.ItemFragment;
import com.metis.meishuquan.fragment.assess.AssessListItemFragment;
import com.metis.meishuquan.fragment.assess.AssessPublishFragment;
import com.metis.meishuquan.fragment.assess.ChooseCityFragment;
import com.metis.meishuquan.fragment.assess.FilterConditionForAssessListFragment;
import com.metis.meishuquan.fragment.circle.CircleBaseFragment;
import com.metis.meishuquan.model.BLL.AssessOperator;
import com.metis.meishuquan.model.assess.AllAssess;
import com.metis.meishuquan.model.assess.Assess;
import com.metis.meishuquan.model.assess.AssessListFilter;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.AssessStateEnum;
import com.metis.meishuquan.model.enums.QueryTypeEnum;
import com.metis.meishuquan.model.topline.ChannelItem;
import com.metis.meishuquan.push.UnReadManager;
import com.metis.meishuquan.util.GlobalData;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.view.popup.AssessChoosePhotoPopupWindow;
import com.metis.meishuquan.view.popup.ChoosePhotoPopupWindow;
import com.metis.meishuquan.view.shared.DragListView;
import com.metis.meishuquan.view.shared.TabBar;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.viewpagerindicator.TabPageIndicator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 模块：点评
 * <p/>
 * Created by wj on 3/15/2015.
 */
public class AssessFragment extends Fragment {
    private static final String KEY_LIST_FILTER = "assessListFilter";
    private static final String TAG = "getAssessComment";
    private static final int TAKE_PHOTO = 1;
    private static final int PICK_PICTURE = 2;
    private ViewGroup rootView;
    private TabBar tabBar;

    private ViewPager viewPager;
    private TabPageIndicator indicator;
    private TabPageIndicatorAdapter fragmentPagerAdapter;
    private Button btnRegion, btnFilter, btnPublishComment;

    private AssessListFilter assessListFilter;

    private String photoPath = "";
    private FragmentManager fm;

    private UnReadManager.Observable mObservable = new UnReadManager.Observable() {
        @Override
        public void onChanged(String tag, int count, int delta) {
            manageTip(tag, count, delta);
        }
    };

    private void manageTip (String tag, int count, int delta) {
        tabBar.setActivityTipVisible(count > 0 ? View.VISIBLE : View.GONE);
        tabBar.setActivityTipText(count + "");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UnReadManager.getInstance(getActivity()).registerObservable(UnReadManager.TAG_NEW_STUDENT, mObservable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UnReadManager.getInstance(getActivity()).unregisterObservable(UnReadManager.TAG_NEW_STUDENT, mObservable);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String tag = UnReadManager.TAG_NEW_STUDENT;
        int count = UnReadManager.getInstance(getActivity()).getCountByTag(tag);
        manageTip(tag, count, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AssessOperator assessOperator = AssessOperator.getInstance();
        assessOperator.AddRegionToCache();
        //加载过滤条件之标签数据
        assessOperator = AssessOperator.getInstance();
        assessOperator.addAssessChannelListToCache();//将过滤条件（年级、标签）加载至缓存

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_commentfragment, container, false);
        initView(rootView);
        initEvent();

        if (GlobalData.AssessIndex > 0) {
            indicator.setCurrentItem(GlobalData.AssessIndex);
            GlobalData.AssessIndex = 0;
        }
        return rootView;
    }

    //读取用户上次选择的筛选条件
    private AssessListFilter getCheckedData() {
        String json = SharedPreferencesUtil.getInstanse(MainApplication.UIContext).getStringByKey(SharedPreferencesUtil.CHECKED_ASSESS_FILTER + MainApplication.userInfo.getUserId());
        Gson gson = new Gson();
        if (!json.equals("")) {
            AssessListFilter data = gson.fromJson(json, new com.google.common.reflect.TypeToken<AssessListFilter>() {
            }.getType());
            return data;
        }
        return new AssessListFilter();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == FragmentActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO://拍照
                    openAssessPublishFragment(photoPath);
                    break;
                case PICK_PICTURE://相册
                    Uri uri = data.getData();
                    try {
                        //根据URL得到Bitmap
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(MainApplication.UIContext.getContentResolver(), uri);
                        //将返回的图片存入指定路径，以便上传至服务器
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
                        File file = new File(dir, String.valueOf(System.currentTimeMillis()) + ".jpg");
                        photoPath = file.getPath();

                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();

                        openAssessPublishFragment(photoPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void openAssessPublishFragment(String photoPath) {
        AssessPublishFragment assessPublishFragment = new AssessPublishFragment();
        Bundle bundle = new Bundle();
        bundle.putString("photoPath", photoPath);
        assessPublishFragment.setArguments(bundle);
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content_container, assessPublishFragment);
        ft.addToBackStack(null);
        ft.commit();
        this.photoPath = "";
        assessPublishFragment.setOnAssessPublishedListner(new AssessPublishFragment.OnAssessPublishedListner() {
            @Override
            public void refreshSescondTab(int index) {
                GlobalData.AssessIndex = index;
            }
        });
    }

    private void initView(ViewGroup rootView) {
        this.fm = getActivity().getSupportFragmentManager();
        this.tabBar = (TabBar) rootView.findViewById(R.id.fragment_shared_commentfragment_tab_bar);
        if (GlobalData.tabs.size() > 0) {
            for (int i = 0; i < GlobalData.tabs.size(); i++) {
                TabBar.showOrHide(GlobalData.tabs.get(i), true);
            }
        }

        this.viewPager = (ViewPager) rootView.findViewById(R.id.fragment_shared_assess_list_viewpager);
        this.indicator = (TabPageIndicator) rootView.findViewById(R.id.assess_indicator);
        this.tabBar.setTabSelectedListener(MainApplication.MainActivity);
        this.btnRegion = (Button) rootView.findViewById(R.id.id_btn_region);
        this.btnPublishComment = (Button) rootView.findViewById(R.id.id_btn_assess_comment);
        this.btnFilter = (Button) rootView.findViewById(R.id.id_btn_commentlist_filter);
        this.fragmentPagerAdapter = new TabPageIndicatorAdapter(getActivity().getSupportFragmentManager());
        this.fragmentPagerAdapter.setAssessListFilter(getCheckedData());

        //设置ViewPager适配器
        this.viewPager.setAdapter(fragmentPagerAdapter);

        //实例化TabPageIndicator然后设置ViewPager与之关联
        this.indicator.setViewPager(viewPager);
    }

    private void initEvent() {
        //全国
        this.btnRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseCityFragment chooseCityFragment = new ChooseCityFragment();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.content_container, chooseCityFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        //过滤条件
        this.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterConditionForAssessListFragment filterConditionForAssessListFragment = new FilterConditionForAssessListFragment();
                filterConditionForAssessListFragment.setFilterConditionListner(new OnFilterChanngedListner() {
                    @Override
                    public void setFilter(AssessListFilter filter) {
                        //添加至本地保存
                        Gson gson = new Gson();
                        String json = gson.toJson(filter);
                        Log.i("点评列表筛选条件", json);
                        SharedPreferencesUtil.getInstanse(MainApplication.UIContext).update(SharedPreferencesUtil.CHECKED_ASSESS_FILTER + MainApplication.userInfo.getUserId(), json);
                        //TODO:根据筛选条件刷新列表
                        fragmentPagerAdapter.setAssessListFilter(filter);
                        fragmentPagerAdapter.notifyDataSetChanged();
                        indicator.notifyDataSetChanged();
                    }
                });
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.content_container, filterConditionForAssessListFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        //提问
        this.btnPublishComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainApplication.isLogin()) {
                    AssessChoosePhotoPopupWindow choosePhotoPopupWindow = new AssessChoosePhotoPopupWindow(MainApplication.UIContext, AssessFragment.this, rootView);
                    choosePhotoPopupWindow.getPath(new OnPathChannedListner() {
                        @Override
                        public void setPath(String path) {
                            photoPath = path;
                        }
                    });

                    choosePhotoPopupWindow.setWordCheckedListner(new OnWordCheckedListner() {
                        @Override
                        public void openWordAssessPublishFragment() {
                            AssessPublishFragment assessPublishFragment = new AssessPublishFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("photoPath", photoPath);
                            assessPublishFragment.setArguments(bundle);
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.add(R.id.content_container, assessPublishFragment);
                            ft.addToBackStack(null);
                            ft.commit();
                            assessPublishFragment.setOnAssessPublishedListner(new AssessPublishFragment.OnAssessPublishedListner() {
                                @Override
                                public void refreshSescondTab(int index) {
                                    GlobalData.AssessIndex = index;
                                }
                            });
                        }
                    });
                } else {
                    startActivity(new Intent(MainApplication.UIContext, LoginActivity.class));
                }
            }
        });
    }

    public interface OnPathChannedListner {
        void setPath(String path);
    }

    public interface OnFilterChanngedListner {
        void setFilter(AssessListFilter assessListFilter);
    }

    public interface OnWordCheckedListner {
        void openWordAssessPublishFragment();
    }

    class TabPageIndicatorAdapter extends FragmentStatePagerAdapter {
        public String[] titles = new String[]{"精选推荐", "最新发布", "最热评论"};
        private AssessListFilter assessListFilter = null;

        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setAssessListFilter(AssessListFilter assessListFilter) {
            this.assessListFilter = assessListFilter;
        }

        @Override
        public AssessListItemFragment getItem(int position) {
            AssessListItemFragment itemFragment = new AssessListItemFragment();
            return itemFragment;
        }

        @Override
        public AssessListItemFragment instantiateItem(ViewGroup container, int position) {
            AssessListItemFragment itemFragment = (AssessListItemFragment) super.instantiateItem(container, position);
            String title = titles[position];
            if (assessListFilter == null) {
                assessListFilter = new AssessListFilter();
            }
            if (title.equals("精选推荐")) {
                itemFragment.setQueryType(QueryTypeEnum.RECOMMEND);
            } else if (title.equals("最新发布")) {
                itemFragment.setQueryType(QueryTypeEnum.NEW);
            } else if (title.equals("最热评论")) {
                itemFragment.setQueryType(QueryTypeEnum.HOT);
            }
            itemFragment.setAssessListFilter(assessListFilter);

            return itemFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }
}
