package com.metis.meishuquan.adapter.studio;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.metis.meishuquan.activity.info.homepage.AchievementDetailActivity;
import com.metis.meishuquan.model.BLL.Achievement;
import com.metis.meishuquan.view.shared.MyInfoBtn;

import java.util.List;

/**
 * Created by WJ on 2015/5/7.
 */
public class AchievementAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<Achievement> mDataList = null;

    public AchievementAdapter (Context context, List<Achievement> list) {
        mContext = context;
        mDataList = list;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Achievement getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Achievement achievement = getItem(i);
        MyInfoBtn btn = new MyInfoBtn(mContext);
        btn.setText(achievement.getAchievementTitle());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, AchievementDetailActivity.class);
                it.putExtra(AchievementDetailActivity.KEY_ACHIEVEMENT_ID, achievement.getAchievementId());
                mContext.startActivity(it);
            }
        });
        return btn;
    }
}
