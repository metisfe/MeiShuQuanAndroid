package com.metis.meishuquan.activity.info.homepage;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.model.BLL.Achievement;
import com.metis.meishuquan.model.BLL.StudioOperator;
import com.metis.meishuquan.model.BLL.UserInfoOperator;

public class AchievementDetailActivity extends BaseActivity {

    public static final String KEY_ACHIEVEMENT_ID = "achievementId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_detail);

        int achievementId = getIntent().getIntExtra(KEY_ACHIEVEMENT_ID, 0);
        StudioOperator.getInstance().getAchievementDetail(achievementId, new UserInfoOperator.OnGetListener<Achievement>() {
            @Override
            public void onGet(boolean succeed, Achievement achievement) {

            }
        });
    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.studio_tab_glory);
    }
}
