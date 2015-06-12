package com.metis.meishuquan.activity.circle;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.circle.CUserModel;
import com.metis.meishuquan.util.ActivityUtils;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.view.circle.CircleTitleBar;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

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
        MainApplication.refreshRong();
        this.titleBar = (CircleTitleBar) this.findViewById(R.id.activity_circle_chatactivity_titlebar);
        titleBar.setText(title);
        if (!this.type.equals(RongIMClient.ConversationType.CHATROOM.toString())) {
            titleBar.showRight();
            titleBar.setRightButton("", R.drawable.icon_circle_chat, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChatActivity.this, ChatConfigActivity.class);
                    intent.putExtra("type", type);
                    intent.putExtra("targetId", targetId);
                    startActivity(intent);
                }
            });
        } else {
            titleBar.hideRight();
        }

        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            //聊天界面，用户头像点击事件，点击进入个人主页
            @Override
            public boolean onClickUserPortrait(Context context, RongIMClient.ConversationType conversationType, RongIMClient.UserInfo userInfo) {
                String json = SharedPreferencesUtil.getInstanse(MainApplication.UIContext).getStringByKey(SharedPreferencesUtil.CONTACTS + MainApplication.userInfo.getUserId());
                List<CUserModel> userModels = new Gson().fromJson(json, new TypeToken<List<CUserModel>>() {
                }.getType());
                String userId = "";
                if (userInfo.getUserId().equals(MainApplication.userInfo.getRongCloudId())) {
                    userId = String.valueOf(MainApplication.userInfo.getUserId());
                } else {
                    for (int i = 0; i < userModels.size(); i++) {
                        if (userModels.get(i).getUserName().equals(userInfo.getName())) {
                            userId = String.valueOf(userModels.get(i).userId);
                            break;
                        }
                    }
                }
                ActivityUtils.startNameCardActivity(context, Long.parseLong(userId));
                return true;
            }

            //消息点击事件（未实现）
            @Override
            public boolean onClickMessage(Context context, RongIMClient.Message message) {
                return false;
            }
        });

//        //更新本地缓存的用户信息
//        RongIM.GetUserInfoProvider userInfoProvider = new RongIM.GetUserInfoProvider() {
//            @Override
//            public RongIMClient.UserInfo getUserInfo(String userId) {
//                return null;
//            }
//        };
//        userInfoProvider.getUserInfo(targetId);
//        RongIM.setGetUserInfoProvider(userInfoProvider, true);

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
