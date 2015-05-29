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

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.fragment.act.StudentCanceledFragment;
import com.metis.meishuquan.fragment.act.StudentJoinedFragment;

import java.util.List;

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

    public static class StudentAdapter extends RecyclerView.Adapter<AbsViewHolder> {

        private Context mContext = null;
        private List<? extends DelegateImpl> mDataList = null;

        public StudentAdapter (Context context, List<? extends DelegateImpl> dataList) {
            mContext = context;
            mDataList = dataList;
        }

        @Override
        public AbsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(AbsViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    }

    public abstract class AbsViewHolder <T> extends RecyclerView.ViewHolder{
        public AbsViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindData (Context context, T t);
    }

    public static interface DelegateImpl<T> {
        public T getSource ();
    }

    public static enum DelegateType {

        DIVIDER_TITLE (0, 0);

        private int mType, mLayoutId;

        DelegateType (int type, int layoutId) {
            mType = type;
            mLayoutId = layoutId;
        }
    }
}
