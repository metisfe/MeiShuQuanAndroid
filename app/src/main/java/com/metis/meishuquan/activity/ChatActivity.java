package com.metis.meishuquan.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.metis.meishuquan.R;

/**
 * Created by wudi on 4/7/2015.
 */
public class ChatActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntent().setData(Uri.parse("rong://io.rong.imkit.demo").buildUpon().appendPath("conversation").appendPath("private")
                .appendQueryParameter("targetId","diwulechao2").appendQueryParameter("title", "张三").build());
        setContentView(R.layout.activity_circle_chatactivity);
    }
}
