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

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.course.ChooseCourseActivity;
import com.metis.meishuquan.fragment.course.CourseListFragment;
import com.metis.meishuquan.fragment.course.CoursePicListFragment;
import com.metis.meishuquan.view.shared.TabBar;

/**
 * Created by wangjin on 3/15/2015.
 */
public class ClassFragment extends Fragment {
    private TabBar tabBar;
    private Button btnChooseClass, btnPicture;
    private boolean isPicList = false;

    private FragmentManager fm;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_classfragment, container, false);

        initView(rootView);
        addCourseListFragment();
        initEvent();

        return rootView;
    }

    private void addCourseListFragment() {
        CourseListFragment courseListFragment = new CourseListFragment();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.id_rl_container_list, courseListFragment);
        ft.commit();
    }

    private void initView(ViewGroup rootView) {
        this.btnChooseClass = (Button) rootView.findViewById(R.id.id_btn_choose_class);
        this.btnPicture = (Button) rootView.findViewById(R.id.id_btn_choose_pic);

        this.tabBar = (TabBar) rootView.findViewById(R.id.fragment_shared_classfragment_tab_bar);
        this.tabBar.setTabSelectedListener(MainApplication.MainActivity);

        fm = getActivity().getSupportFragmentManager();
    }

    private void initEvent() {
        btnChooseClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), ChooseCourseActivity.class));
            }
        });

        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPicList) {
                    isPicList = true;
                    btnPicture.setText("类别");
                    CoursePicListFragment coursePicListFragment = new CoursePicListFragment();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.id_rl_container_list, coursePicListFragment);
                    ft.commit();
                } else {
                    isPicList = false;
                    btnPicture.setText("图片");
                    CourseListFragment courseListFragment = new CourseListFragment();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.id_rl_container_list, courseListFragment);
                    ft.commit();
                }
            }
        });
    }
}
