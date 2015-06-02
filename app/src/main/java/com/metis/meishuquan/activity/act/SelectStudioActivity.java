package com.metis.meishuquan.activity.act;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.fragment.act.StudioListFragment;
import com.metis.meishuquan.push.PushType;
import com.metis.meishuquan.push.UnReadManager;

public class SelectStudioActivity extends BaseActivity {

    private StudioListFragment mStudioListFragment = null;

    private View mSearchView = null;
    private EditText mSearchInput = null;

    private boolean isSearchShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_studio);

        mStudioListFragment = (StudioListFragment)getSupportFragmentManager().findFragmentById(R.id.select_studio_fragment);
        mStudioListFragment.setCanEdit(true);

        //getTitleView().setImageRightResource(R.drawable.rc_ic_atfriend_search);

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
                                    mStudioListFragment.searchContent(mSearchInput.getText().toString());
                                    break;
                            }
                        }
                        return false;
                    }
                });
            }
            //getTitleView().removeCenterView(mCustomTitle);
            getTitleView().addCenterView(mSearchView);
            getTitleView().setImageRightResource(R.drawable.active_right_btn_close);
            //getTitleView().setImageRightResource();
            mSearchInput.requestFocus();
            isSearchShowing = true;
        }
    }

    private void hideSearchView () {
        if (isSearchShowing) {
            getTitleView().removeCenterView(mSearchView);
            mSearchInput.setText("");
            mStudioListFragment.clearSearch();
            isSearchShowing = false;
            getTitleView().setImageRightResource(R.drawable.rc_ic_atfriend_search);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UnReadManager.getInstance(this).notifyByTag(PushType.ACTIVITY.getTag(), 0);
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
    public String getTitleCenter() {
        return getString(R.string.act_select_studio);
    }

    @Override
    public void onTitleRightPressed() {
        /*if (isSearchShowing) {
            hideSearchView();
        } else {
            showSearchView();
        }*/
        /*final TopListItem item = mStudioListFragment.getSelectedStudioItem();
        if (item == null) {
            Toast.makeText(this, R.string.act_choose_one_plz, Toast.LENGTH_SHORT).show();
            return;
        }
        ActiveOperator.getInstance().getActiveDetail(new UserInfoOperator.OnGetListener<ActiveInfo>() {
            @Override
            public void onGet(boolean succeed, ActiveInfo activeInfo) {
                if (succeed) {
                    ActiveOperator.getInstance().selectStudio(item.getUserId(), activeInfo.getpId(), new UserInfoOperator.OnGetListener() {
                        @Override
                        public void onGet(boolean succeed, Object o) {
                            if (succeed) {
                                Toast.makeText(SelectStudioActivity.this, R.string.act_join_success, Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(SelectStudioActivity.this, R.string.act_join_failed, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });*/

    }
}
