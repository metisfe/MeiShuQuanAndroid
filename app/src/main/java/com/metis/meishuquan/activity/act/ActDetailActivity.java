package com.metis.meishuquan.activity.act;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.fragment.act.ActDetailFragment;
import com.metis.meishuquan.fragment.act.StudioListFragment;
import com.metis.meishuquan.fragment.act.TopListFragment;

public class ActDetailActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private View mCustomTitle = null;

    private ActDetailFragment mDetailFragment = ActDetailFragment.getInstance();
    private StudioListFragment mListFragment = StudioListFragment.getInstance();
    //private TopListFragment mListFragment = TopListFragment.getInstance();

    private RadioGroup mGroup = null;
    private RadioButton mDetailBtn, mListBtn;
    private View mSearchView = null;
    private EditText mSearchInput = null;
    private boolean isSearchShowing = false;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_detail);

        mCustomTitle = LayoutInflater.from(this).inflate(R.layout.layout_act_title, null);
        getTitleView().addCenterView(mCustomTitle);

        mGroup = (RadioGroup) mCustomTitle.findViewById(R.id.act_title_group);
        mDetailBtn = (RadioButton) mCustomTitle.findViewById(R.id.act_title_details);
        mListBtn = (RadioButton) mCustomTitle.findViewById(R.id.act_title_list);

        mGroup.setOnCheckedChangeListener(this);

        mDetailFragment.setOnClickListenerIfJoined(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //removeLastFragment(false);
                mListBtn.setChecked(true);
                /*getTitleView().setImageRightVisible(View.VISIBLE);
                addFragment(mListFragment, false);*/
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("join_succeed"));

    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onTitleRightPressed() {
        if (isSearchShowing) {
            hideSearchView();
        } else {
            showSearchView();
        }
    }

    @Override
    public String getTitleCenter() {
        return null;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        addFragment(mDetailFragment, false);
        mDetailBtn.setChecked(true);
        getTitleView().setTitleRightVisible(View.GONE);
        getTitleView().setImageRightVisible(View.GONE);
        getTitleView().setImageRightResource(R.drawable.rc_ic_atfriend_search);

    }

    private void showSearchView() {
        if (!isSearchShowing) {
            if (mSearchView == null) {
                mSearchView = LayoutInflater.from(this).inflate(R.layout.layout_act_search_title, null);
                mSearchInput = (EditText) mSearchView.findViewById(R.id.search_input);
                mSearchInput.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                            switch (keyEvent.getKeyCode()) {
                                case KeyEvent.KEYCODE_ENTER:
                                    mListFragment.searchContent(mSearchInput.getText().toString());
                                    break;
                            }
                        }
                        return false;
                    }
                });
            }
            getTitleView().removeCenterView(mCustomTitle);
            getTitleView().addCenterView(mSearchView);
            getTitleView().setImageRightResource(R.drawable.active_right_btn_close);
            mSearchInput.requestFocus();
            isSearchShowing = true;
        }
    }

    private void hideSearchView() {
        if (isSearchShowing) {
            getTitleView().addCenterView(mCustomTitle);
            getTitleView().removeCenterView(mSearchView);
            getTitleView().setImageRightResource(R.drawable.rc_ic_atfriend_search);
            mSearchInput.setText("");
            mListFragment.clearSearch();
            isSearchShowing = false;
        }
    }

    @Override
    public void onBackPressed() {
        if (isSearchShowing) {
            hideSearchView();
            return;
        }
        super.onBackPressed();
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
