package com.metis.meishuquan.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.metis.meishuquan.R;
import com.metis.meishuquan.view.circle.CircleTitleBar;

/**
 * Created by wudi on 4/7/2015.
 */
public class ChatActivity extends FragmentActivity {
    private CircleTitleBar titleBar;
    private String type, title, targetId;

    private void getParams() {
        this.type = this.getIntent().getStringExtra("type");
        this.targetId = this.getIntent().getStringExtra("targetId");
        this.title = this.getIntent().getStringExtra("title");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParams();
        getIntent().setData(Uri.parse("rong://io.rong.imkit.demo").buildUpon().appendPath("conversation").appendPath(type)
                .appendQueryParameter("targetId", targetId).appendQueryParameter("title", title).build());
        setContentView(R.layout.activity_circle_chatactivity);
        this.titleBar = (CircleTitleBar) this.findViewById(R.id.activity_circle_chatactivity_titlebar);
        titleBar.setText(title);
        titleBar.setRightButton("config", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, ChatConfigActivity.class);
                intent.putExtra("type", "discussion");
                intent.putExtra("targetId", targetId);
                startActivity(intent);
            }
        });

        titleBar.setLeftButton("back", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getParams();
    }
}
