package com.metis.meishuquan.activity.info;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.commons.DataListFragment;
import com.metis.meishuquan.view.shared.TitleView;

public abstract class DataListActivity extends FragmentActivity {

    private TitleView mTitleView = null;

    private DataListFragment mDataListFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);

        mTitleView = (TitleView)this.findViewById(R.id.title);
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDataListFragment = (DataListFragment)getSupportFragmentManager().findFragmentById(R.id.my_favorites_fragment);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadData(0);
    }

    public abstract void loadData (int index);
}
