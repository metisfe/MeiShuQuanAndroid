package com.metis.meishuquan.fragment.course;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.metis.meishuquan.R;
import com.metis.meishuquan.view.shared.DragListView;

/**
 * Created by wangjin on 15/4/16.
 */
public class CoursePicListFragment extends Fragment {
    private Button btnrecommend, btnNewPublish, btnHotCourse;
    private DragListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_class_img_list, container, false);

        initView(rootView);
        initEvent();

        return rootView;
    }

    private void initView(ViewGroup rootView) {
        this.btnrecommend = (Button) rootView.findViewById(R.id.id_btn_recommend);
        this.btnNewPublish = (Button) rootView.findViewById(R.id.id_btn_new_publish);
        this.btnHotCourse = (Button) rootView.findViewById(R.id.id_btn_hot_course);
        this.listView = (DragListView) rootView.findViewById(R.id.id_listview_course_img);
    }

    private void initEvent() {
        this.btnrecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonChecked(btnrecommend);
                setButtonUnChecked(new Button[]{btnNewPublish, btnHotCourse});
            }
        });

        this.btnNewPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonChecked(btnNewPublish);
                setButtonUnChecked(new Button[]{btnrecommend, btnHotCourse});
            }
        });

        this.btnHotCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonChecked(btnHotCourse);
                setButtonUnChecked(new Button[]{btnNewPublish, btnrecommend});
            }
        });
    }

    private void setButtonChecked(Button btn) {
        btn.setTextColor(Color.rgb(251, 109, 109));
    }

    private void setButtonUnChecked(Button[] btns) {
        for (Button button : btns) {
            button.setTextColor(Color.rgb(126, 126, 126));
        }
    }


}
