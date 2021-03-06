package com.metis.meishuquan.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.course.ChooseCourseActivity;
import com.metis.meishuquan.fragment.course.CourseListFragment;
import com.metis.meishuquan.fragment.course.CoursePicListFragment;
import com.metis.meishuquan.model.course.CourseChannelItem;
import com.metis.meishuquan.push.PushType;
import com.metis.meishuquan.push.UnReadManager;
import com.metis.meishuquan.util.GlobalData;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.view.shared.TabBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjin on 3/15/2015.
 */
public class ClassFragment extends Fragment {
    private TabBar tabBar;
    private Button btnChooseClass, btnPicture;
    private TextView tvCourseChannelBar;
    private boolean isPicList = false;
    private List<CourseChannelItem> lstCheckedCourseChannelItems = new ArrayList<CourseChannelItem>();
    private CourseListFragment courseListFragment;
    private CoursePicListFragment coursePicListFragment;
    private String courseChannelBarText = "";
    private String tags = "";

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
        UnReadManager.getInstance(getActivity()).registerObservable(PushType.ACTIVITY.getTag(), mObservable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UnReadManager.getInstance(getActivity()).unregisterObservable(PushType.ACTIVITY.getTag(), mObservable);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String tag = PushType.ACTIVITY.getTag();
        int count = UnReadManager.getInstance(getActivity()).getCountByTag(tag);
        manageTip(tag, count, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_classfragment, container, false);

        initView(rootView);
        addCourseListFragment();
        initEvent();

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data.getExtras() == null) return;
        List<CourseChannelItem> items = (List<CourseChannelItem>) data.getExtras().getSerializable("tags");
        if (items != null && items.size() >= 0) {
            lstCheckedCourseChannelItems = items;
            updateListView(lstCheckedCourseChannelItems);
            Gson gson = new Gson();
            String json = gson.toJson(items);
            SharedPreferencesUtil.getInstanse(MainApplication.UIContext).update(SharedPreferencesUtil.CHECKED_CHANNEL_ITEMS + MainApplication.userInfo.getUserId(), json);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private List<CourseChannelItem> getOldChannels() {
        final String json = SharedPreferencesUtil.getInstanse(MainApplication.UIContext).getStringByKey(SharedPreferencesUtil.CHECKED_CHANNEL_ITEMS + MainApplication.userInfo.getUserId());
        List<CourseChannelItem> mOldCheckedItems = null;
        if (!json.isEmpty()) {
            mOldCheckedItems = new Gson().fromJson(json, new TypeToken<List<CourseChannelItem>>() {
            }.getType());
        }
        return mOldCheckedItems;
    }

    private void updateListView(List<CourseChannelItem> lstCheckedCourseChannelItems) {
        if (lstCheckedCourseChannelItems == null || lstCheckedCourseChannelItems.size() == 0) {
            tags = "";
            courseChannelBarText = "全部";
            this.tvCourseChannelBar.setText(courseChannelBarText);
            return;
        }
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < lstCheckedCourseChannelItems.size(); i++) {
            sb.append(lstCheckedCourseChannelItems.get(i).getChannelId() + "");
            if (i < lstCheckedCourseChannelItems.size() - 1) {
                sb.append(",");
            }
        }
        tags = sb.toString();
        StringBuilder sb1 = new StringBuilder();
        if (lstCheckedCourseChannelItems.size() > 0) {
            for (int i = 0; i < lstCheckedCourseChannelItems.size(); i++) {
                sb1.append(lstCheckedCourseChannelItems.get(i).getChannelName().trim());
                if (i < lstCheckedCourseChannelItems.size() - 1) {
                    sb1.append(",");
                }
            }
        }
        courseChannelBarText = sb1.toString();
        this.tvCourseChannelBar.setText(courseChannelBarText);

        if (!isPicList) {
            courseListFragment = new CourseListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("tags", tags);
            courseListFragment.setArguments(bundle);
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.id_rl_container_list, courseListFragment);
            ft.commit();
        } else {
            coursePicListFragment = new CoursePicListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("tags", tags);
            coursePicListFragment.setArguments(bundle);
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.id_rl_container_list, coursePicListFragment);
            ft.commit();
        }
    }

    private void addCourseListFragment() {
        courseListFragment = new CourseListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tags", tags);
        courseListFragment.setArguments(bundle);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.id_rl_container_list, courseListFragment);
        ft.commit();
    }

    private void initView(ViewGroup rootView) {
        this.btnChooseClass = (Button) rootView.findViewById(R.id.id_btn_choose_class);
        this.btnPicture = (Button) rootView.findViewById(R.id.id_btn_choose_pic);
        this.tvCourseChannelBar = (TextView) rootView.findViewById(R.id.id_tv_courselist_filter);
        this.courseChannelBarText = getTags();
        this.tvCourseChannelBar.setText(courseChannelBarText);

        this.tabBar = (TabBar) rootView.findViewById(R.id.fragment_shared_classfragment_tab_bar);
//        if (GlobalData.tabs.size() > 0) {
//            for (int i = 0; i < GlobalData.tabs.size(); i++) {
//                TabBar.showOrHide(GlobalData.tabs.get(i), true);
//            }
//        }
        this.tabBar.setTabSelectedListener(MainApplication.MainActivity);

        fm = getActivity().getSupportFragmentManager();
    }

    private void initEvent() {
        btnChooseClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChooseCourseActivity.class);
                intent.putExtra("OldSelectedCourseChannelItems", (java.io.Serializable) getOldChannels());
                startActivityForResult(intent, 101);
            }
        });

        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPicList) {
                    isPicList = true;
                    btnPicture.setText("课程");
                    coursePicListFragment = new CoursePicListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("tags", tags);
                    coursePicListFragment.setArguments(bundle);
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.remove(courseListFragment);
                    ft.add(R.id.id_rl_container_list, coursePicListFragment);
                    ft.commit();
                } else {
                    isPicList = false;
                    btnPicture.setText("图片");
                    courseListFragment = new CourseListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("tags", tags);
                    courseListFragment.setArguments(bundle);
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.remove(coursePicListFragment);
                    ft.add(R.id.id_rl_container_list, courseListFragment);
                    ft.commit();
                }
            }
        });
    }

    private String getTags() {
        String json = SharedPreferencesUtil.getInstanse(MainApplication.UIContext).getStringByKey(SharedPreferencesUtil.CHECKED_CHANNEL_ITEMS + MainApplication.userInfo.getUserId());
        if (!json.isEmpty()) {
            lstCheckedCourseChannelItems = new Gson().fromJson(json, new TypeToken<List<CourseChannelItem>>() {
            }.getType());
            if (lstCheckedCourseChannelItems.size() == 0) {
                tags = "";
                return "全部";
            }
            StringBuilder sb = new StringBuilder();
            StringBuilder sbTagId = new StringBuilder();
            for (int i = 0; i < lstCheckedCourseChannelItems.size(); i++) {
                sb.append(lstCheckedCourseChannelItems.get(i).getChannelName().trim());
                sbTagId.append(String.valueOf(lstCheckedCourseChannelItems.get(i).getChannelId()));
                if (i < lstCheckedCourseChannelItems.size() - 1) {
                    sb.append(",");
                    sbTagId.append(",");
                }
            }
            this.tags = sbTagId.toString();
            return sb.toString();
        }
        return courseChannelBarText = "全部";
    }
}
