package com.metis.meishuquan.activity.act;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.fragment.act.ActDetailFragment;
import com.metis.meishuquan.fragment.act.ActListFragment;

public class ActDetailActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private View mCustomTitle = null;

    private ActDetailFragment mDetailFragment = new ActDetailFragment();
    private ActListFragment mListFragment = new ActListFragment();

    private RadioGroup mGroup = null;
    private RadioButton mDetailBtn, mListBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_detail);

        mCustomTitle = LayoutInflater.from(this).inflate(R.layout.layout_act_title, null);
        getTitleView().setCenterView(mCustomTitle);

        mGroup = (RadioGroup)mCustomTitle.findViewById(R.id.act_title_group);
        mDetailBtn = (RadioButton)mCustomTitle.findViewById(R.id.act_title_details);
        mListBtn = (RadioButton)mCustomTitle.findViewById(R.id.act_title_list);

        mGroup.setOnCheckedChangeListener(this);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        addFragment(mDetailFragment, false);
        mDetailBtn.setChecked(true);
        getTitleView().setTitleRightVisible(View.GONE);
        getTitleView().setImageRightVisible(View.GONE);
        getTitleView().setImageRightResource(R.drawable.ic_launcher);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        removeLastFragment(false);
        switch (checkedId) {
            case R.id.act_title_details:
                getTitleView().setImageRightVisible(View.GONE);
                addFragment(mDetailFragment, false);
                break;
            case R.id.act_title_list:
                getTitleView().setImageRightVisible(View.VISIBLE);
                addFragment(mListFragment, false);
                break;
        }
    }
}
