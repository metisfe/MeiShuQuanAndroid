package com.metis.meishuquan.activity.act;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.fragment.act.StudioListFragment;
import com.metis.meishuquan.model.BLL.ActiveOperator;
import com.metis.meishuquan.model.BLL.TopListItem;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.ActiveInfo;

public class SelectStudioActivity extends BaseActivity {

    private StudioListFragment mStudioListFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_studio);

        mStudioListFragment = (StudioListFragment)getSupportFragmentManager().findFragmentById(R.id.select_studio_fragment);
    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.act_select_studio);
    }

    @Override
    public String getTitleRight() {
        return getString(R.string.gender_ok);
    }

    @Override
    public void onTitleRightPressed() {
        final TopListItem item = mStudioListFragment.getSelectedStudioItem();
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
        });

    }
}
