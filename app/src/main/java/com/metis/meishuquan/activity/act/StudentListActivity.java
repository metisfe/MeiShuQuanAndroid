package com.metis.meishuquan.activity.act;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.fragment.act.StudentCanceledFragment;
import com.metis.meishuquan.fragment.act.StudentJoinedFragment;
import com.metis.meishuquan.push.UnReadManager;
import com.metis.meishuquan.view.Common.delegate.AbsDelegate;
import com.metis.meishuquan.view.Common.delegate.AbsViewHolder;
import com.metis.meishuquan.view.Common.delegate.DelegateAdapter;
import com.metis.meishuquan.view.Common.delegate.DelegateImpl;
import com.metis.meishuquan.view.Common.delegate.DelegateType;

import java.util.List;

import static com.metis.meishuquan.view.Common.delegate.DelegateType.DIVIDER_TITLE;

public class StudentListActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{

    private static final String TAG = StudentListActivity.class.getSimpleName();

    private View mCustomView = null;
    private RadioGroup mRadioGroup = null;

    private RadioButton leftBtn, rightBtn;

    private StudentJoinedFragment mJoinedFragment = new StudentJoinedFragment();
    private StudentCanceledFragment mCanceledFragment = new StudentCanceledFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_act_title, null);
        getTitleView().addCenterView(mCustomView);

        mRadioGroup = (RadioGroup)mCustomView.findViewById(R.id.act_title_group);
        leftBtn = (RadioButton)mCustomView.findViewById(R.id.act_title_details);
        rightBtn = (RadioButton)mCustomView.findViewById(R.id.act_title_list);

        leftBtn.setText(R.string.act_has_joined_title);
        rightBtn.setText(R.string.act_canceled_joined_title);

        mRadioGroup.setOnCheckedChangeListener(this);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        leftBtn.setChecked(true);
        showFragment(mJoinedFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UnReadManager.getInstance(this).notifyByTag(UnReadManager.TAG_NEW_STUDENT, 0);
    }

    private Fragment mLastFragment = null;
    private void showFragment (Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fragment.isAdded()) {
            ft.show(fragment);
        } else {
            ft.add(R.id.fragment_container, fragment);
        }
        ft.commit();
        if (mLastFragment != null) {
            hideFragment(mLastFragment);
        }
        mLastFragment = fragment;
    }

    private void hideFragment (Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(fragment);
        ft.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.act_title_details:
                showFragment(mJoinedFragment);
                break;
            case R.id.act_title_list:
                showFragment(mCanceledFragment);
                break;
        }
    }

    public static class StudentAdapter extends DelegateAdapter {

        public StudentAdapter(Context context, List<? extends DelegateImpl> dataList) {
            super(context, dataList);
        }

        @Override
        public AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, View itemView) {
            DelegateType type = DelegateType.getDelegateTypeByType(viewType);
            switch (type) {
                case DIVIDER_TITLE:
                    return new StudentViewHolder(itemView);
            }
            return null;
        }
    }

    public static class StudentViewHolder extends AbsViewHolder<HeaderDelegate> {

        public TextView headerTv = null;

        public StudentViewHolder(View itemView) {
            super(itemView);
            headerTv = (TextView)itemView.findViewById(R.id.student_item_header);
        }

        @Override
        public void bindData(Context context, List<? extends DelegateImpl> dataList, HeaderDelegate headerDelegate) {
            headerTv.setText(headerDelegate.getSource());
        }
    }

    public static class HeaderDelegate extends AbsDelegate<String> {

        public HeaderDelegate(String s) {
            super(s);
        }

        @Override
        public DelegateType getDelegateType() {
            return DelegateType.DIVIDER_TITLE;
        }
    }

    //public static class StudentDelegate extends AbsDelegate

}
