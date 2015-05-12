package com.metis.meishuquan.activity.act;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.fragment.act.ActDetailFragment;
import com.metis.meishuquan.fragment.act.TopListFragment;

public class ActDetailActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private View mCustomTitle = null;

    private ActDetailFragment mDetailFragment = ActDetailFragment.getInstance();
    private TopListFragment mListFragment = TopListFragment.getInstance();

    private RadioGroup mGroup = null;
    private RadioButton mDetailBtn, mListBtn;
    private View mSearchView = null;
    private EditText mSearchInput = null;

    private boolean isSearchShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_detail);

        mCustomTitle = LayoutInflater.from(this).inflate(R.layout.layout_act_title, null);
        getTitleView().addCenterView(mCustomTitle);

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
    public void onTitleRightPressed() {
        if (mGroup.getCheckedRadioButtonId() != R.id.act_title_list) {
            return;
        }
        if (isSearchShowing) {
            hideSearchView();
        } else {
            showSearchView();
        }
    }

    private void showSearchView () {
        if (!isSearchShowing) {
            if (mSearchView == null) {
                mSearchView = LayoutInflater.from(this).inflate(R.layout.layout_act_search_title, null);
                mSearchInput = (EditText)mSearchView.findViewById(R.id.search_input);
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
            //getTitleView().setImageRightResource();
            mSearchInput.requestFocus();
            isSearchShowing = true;
        }
    }

    private void hideSearchView () {
        if (isSearchShowing) {
            getTitleView().addCenterView(mCustomTitle);
            getTitleView().removeCenterView(mSearchView);
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
