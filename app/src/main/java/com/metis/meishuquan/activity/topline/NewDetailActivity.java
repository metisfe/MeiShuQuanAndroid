package com.metis.meishuquan.activity.topline;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.metis.meishuquan.MainActivity;
import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.Topline.ItemInfoFragment;

public class NewDetailActivity extends FragmentActivity {

    public static final String KEY_NEWS_ID = "newsId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail);

        int newsId = 0;
        if (getIntent().getExtras() != null) {
            newsId = getIntent().getExtras().getInt(KEY_NEWS_ID);
        }

        ItemInfoFragment itemInfoFragment = new ItemInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ItemInfoFragment.KEY_NEWSID, newsId);
        bundle.putInt(ItemInfoFragment.KEY_NAVAGT,1001);
        itemInfoFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.id_rl_news_info_container, itemInfoFragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(NewDetailActivity.this, MainActivity.class));
    }
}
