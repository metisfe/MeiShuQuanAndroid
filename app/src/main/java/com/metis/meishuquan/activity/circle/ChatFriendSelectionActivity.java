package com.metis.meishuquan.activity.circle;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.circle.StartFriendPickFragment;

import java.util.ArrayList;

/**
 * Created by wudi on 4/11/2015.
 */
public class ChatFriendSelectionActivity extends FragmentActivity {
    private ArrayList<String> excludeList;
    private String fromType, targetId, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_chatfriendselectionactivity);
        StartFriendPickFragment startFriendPickFragment = new StartFriendPickFragment();

        excludeList = getIntent().getStringArrayListExtra("excludelist");
        fromType = getIntent().getStringExtra("fromtype");
        targetId = getIntent().getStringExtra("targetid");
        title = getIntent().getStringExtra("title");

        Bundle args = new Bundle();
        args.putString("fromtype", fromType);
        args.putString("targetid", targetId);
        args.putString("title", title);
        args.putStringArrayList("excludelist", excludeList);
        args.putBoolean("fromactivity", true);
        startFriendPickFragment.setArguments(args);

        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.activity_circle_chatfriendselectionactivity_container, startFriendPickFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}