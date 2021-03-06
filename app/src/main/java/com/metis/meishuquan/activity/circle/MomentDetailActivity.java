package com.metis.meishuquan.activity.circle;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;

import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.circle.MomentDetailFragment;
import com.metis.meishuquan.util.GlobalData;

/**
 * Activity:圈子详情
 */
public class MomentDetailActivity extends FragmentActivity {

    public static final String KEY_MOMENT_ID = "KEY_MOMENT_ID";
    private int momentId = 0;

    private ViewGroup popupRoot;
    private int attachViewCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_detail);

        popupRoot = (ViewGroup) this.findViewById(R.id.popup_attach);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            momentId = bundle.getInt(KEY_MOMENT_ID, 0);
            navigateToDetail(momentId);
        }
    }

    private void navigateToDetail(int momentId) {
        MomentDetailFragment momentDetailFragment = new MomentDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MomentDetailFragment.KEY_MOMENT_ID, momentId);
        momentDetailFragment.setArguments(bundle);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
        ft.add(R.id.id_ll_moment_detail_container, momentDetailFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void removeAllAttachedView() {
        if (popupRoot != null) {
            popupRoot.removeAllViews();
            attachViewCount = 0;
        }
    }

    public void removeAttachedView(View view) {
        if (popupRoot != null && view != null) {
            popupRoot.removeView(view);
            attachViewCount--;
        }
    }

    public void addAttachView(View view) {
        if (popupRoot != null && view != null) {
            popupRoot.addView(view);
            attachViewCount++;
        }
    }
}
