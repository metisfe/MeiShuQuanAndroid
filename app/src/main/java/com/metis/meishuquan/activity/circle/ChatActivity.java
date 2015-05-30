package com.metis.meishuquan.activity.circle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.metis.meishuquan.R;
import com.metis.meishuquan.view.circle.CircleTitleBar;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by wudi on 4/7/2015.
 */
public class ChatActivity extends FragmentActivity {
    private CircleTitleBar titleBar;
    private String type, title, targetId;

    private void getParams(Intent intent) {
        this.type = intent.getStringExtra("type");
        this.targetId = intent.getStringExtra("targetId");
        this.title = intent.getStringExtra("title");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParams(getIntent());
        getIntent().setData(Uri.parse("rong://io.rong.imkit.demo").buildUpon().appendPath("conversation").appendPath(type.toLowerCase())
                .appendQueryParameter("targetId", targetId).appendQueryParameter("title", title).build());
        setContentView(R.layout.activity_circle_chatactivity);
        this.titleBar = (CircleTitleBar) this.findViewById(R.id.activity_circle_chatactivity_titlebar);
        titleBar.setText(title);
        titleBar.setRightButton("", R.drawable.icon_circle_chat, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, ChatConfigActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("targetId", targetId);
                startActivity(intent);
            }
        });

        titleBar.setLeftButton("返回", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getParams(intent);
    }
}
