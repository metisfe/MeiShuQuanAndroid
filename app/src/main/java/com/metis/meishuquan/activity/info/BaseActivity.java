package com.metis.meishuquan.activity.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.InputActivity;
import com.metis.meishuquan.view.shared.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/4/10.
 */
public class BaseActivity extends FragmentActivity {

    private ViewGroup mRootView = null;
    private TitleView mTitleView = null;
    private FrameLayout mViewContainer = null;
    private View mContentView = null;

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = (ViewGroup)LayoutInflater.from(this).inflate(R.layout.activity_base, null);
        mTitleView = (TitleView)mRootView.findViewById(R.id.base_title);
        mViewContainer = (FrameLayout)mRootView.findViewById(R.id.base_view_container);

        setTitleRight(getTitleRight());
        setTitleCenter(getTitleCenter());
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTitleBackPressed();
            }
        });
        mTitleView.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTitleRightPressed ();
            }
        });
    }

    public String getTitleCenter () {
        return null;
    }

    public String getTitleRight () {
        return null;
    }

    public void onTitleBackPressed () {
        onBackPressed();
    }

    public void onTitleRightPressed () {

    }

    @Override
    public View findViewById(int id) {
        return mViewContainer.findViewById(id);
    }

    public void hideTitleBar () {
        mTitleView.setVisibility(View.GONE);
    }

    public void showTitleBar () {
        mTitleView.setVisibility(View.VISIBLE);
    }

    public void setTitleCenter (String centerTitle) {
        mTitleView.setTitleText(centerTitle);
    }

    public void setTitleRight (String rightTitle) {
        mTitleView.setTitleRight(rightTitle);
    }

    public TitleView getTitleView () {
        return mTitleView;
    }

    public void addFragment (Fragment fragment, boolean addInBackStack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.base_view_container, fragment);
        if (addInBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
        mFragmentList.add(fragment);
    }

    public void addFragment (Fragment fragment) {
        addFragment(fragment, true);
    }

    public void removeFragment (Fragment fragment, boolean popBackStack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.remove(fragment);
        if (popBackStack) {
            manager.popBackStack();
        }
        ft.commit();
        mFragmentList.remove(fragment);
    }

    public void removeFragment (Fragment fragment) {
        removeFragment(fragment, true);
    }

    public void removeLastFragment (boolean hasInBackStack) {
        if (mFragmentList.isEmpty()) {
            return;
        }
        removeFragment(mFragmentList.get(mFragmentList.size() - 1), hasInBackStack);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFragmentList.clear();
    }

    @Override
    public void setContentView(int layoutResID) {
        mViewContainer.removeAllViews();
        mContentView = LayoutInflater.from(this).inflate(layoutResID, null, true);
        mViewContainer.addView(mContentView);
        this.setContentView(mRootView);
    }

    public void startInputActivityForResult (String title, CharSequence defStr, boolean singleLine, int requestCode) {
        startInputActivityForResult(title, defStr, singleLine, requestCode, InputType.TYPE_NULL);
    }

    public void startInputActivityForResult (String title, CharSequence defStr, boolean singleLine, int requestCode, int inputType) {
        startInputActivityForResult(title, defStr, null, singleLine, requestCode, inputType);
    }

    public void startInputActivityForResult (String title, CharSequence defStr, String hint, boolean singleLine, int requestCode, int inputType) {
        Intent it = new Intent(this, InputActivity.class);
        it.putExtra(InputActivity.KEY_TITLE, title);
        it.putExtra(InputActivity.KEY_DEFAULT_STR, defStr);
        it.putExtra(InputActivity.KEY_HINT, hint);
        it.putExtra(InputActivity.KEY_SINGLE_LINE, singleLine);
        it.putExtra(InputActivity.KEY_REQUEST_CODE, requestCode);
        it.putExtra(InputActivity.KEY_INPUT_TYPE, inputType);
        startActivityForResult(it, requestCode);
    }
}
