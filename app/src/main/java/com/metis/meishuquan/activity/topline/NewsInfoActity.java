package com.metis.meishuquan.activity.topline;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.Topline.ItemInfoFragment;
import com.metis.meishuquan.fragment.login.LoginFragment;

public class NewsInfoActity extends FragmentActivity {

    private FragmentManager fm;
    private int newsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_info_actity);

        if (getIntent().getExtras() != null) {
            newsId = getIntent().getExtras().getInt("newsId");
        }

        fm = this.getSupportFragmentManager();
        ItemInfoFragment itemInfoFragment = new ItemInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("newsId", newsId);
        itemInfoFragment.setArguments(bundle);

        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out, R.anim.fragment_popin, R.anim.fragment_popout);
        ft.add(R.id.id_rl_news_info_main, new ItemInfoFragment());
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (isFirstLevelPage(ItemInfoFragment.class.getSimpleName())) {
            super.onBackPressed();
        }
    }

    private boolean isFirstLevelPage(String name) {
        if (name == null || name.length() == 0) {
            return false;
        }
        return name.equals(ItemInfoFragment.class.getSimpleName());
    }
}
