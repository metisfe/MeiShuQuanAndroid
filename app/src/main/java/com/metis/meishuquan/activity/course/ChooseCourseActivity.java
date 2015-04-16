package com.metis.meishuquan.activity.course;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.metis.meishuquan.R;

public class ChooseCourseActivity extends FragmentActivity {

    private Button btnBack, btnConfirm;
    private RelativeLayout rlAllChannel;
    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_course);

        initView();
        initEvent();
    }

    private void initView() {
        this.btnBack = (Button) this.findViewById(R.id.id_btn_back);
        this.btnConfirm = (Button) this.findViewById(R.id.id_btn_confirm);
        this.rlAllChannel = (RelativeLayout) this.findViewById(R.id.id_rl_all_channel);
        this.expandableListView = (ExpandableListView) this.findViewById(R.id.id_course_listview);
    }

    private void initEvent() {
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        this.rlAllChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
