package com.metis.meishuquan.activity.info.homepage;

import android.os.Bundle;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.model.commons.Achievement;
import com.metis.meishuquan.model.BLL.StudioOperator;
import com.metis.meishuquan.model.BLL.UserInfoOperator;

public class AchievementDetailActivity extends BaseActivity {

    public static final String KEY_ACHIEVEMENT_ID = "achievementId";

    private TextView mInfoTv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_detail);

        mInfoTv = (TextView)findViewById(R.id.details_info);

        final int achievementId = getIntent().getIntExtra(KEY_ACHIEVEMENT_ID, 0);
        StudioOperator.getInstance().getAchievementDetail(achievementId, new UserInfoOperator.OnGetListener<Achievement>() {
            @Override
            public void onGet(boolean succeed, Achievement achievement) {
                if (succeed) {
                    mInfoTv.setText(achievement.getAchievementTitle() + "\n" + achievement.getAchievementInfo());
                }
            }
        });
    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.studio_tab_glory);
    }
}
