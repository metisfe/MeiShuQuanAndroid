package com.metis.meishuquan.activity.circle;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.R;
import com.metis.meishuquan.model.circle.CUserModel;
import com.metis.meishuquan.model.circle.UserAdvanceInfo;
import com.metis.meishuquan.util.ImageLoaderUtils;

public class SearchUserInfoActivity extends FragmentActivity {
    public static final String KEY_USER_INFO = "user_info";

    private ImageView imgAvatar;
    private TextView tvUsername;
    private Button btnAddFriend;
    private Button btnBack;
    private Button btnSendRequest;//发送邀请
    private Button btnSendChat;//发起聊天
    private CUserModel userInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_user_info_for_qr);

        //接收参数
        if (getIntent().getExtras() != null) {
            userInfo = (CUserModel) getIntent().getExtras().getSerializable(KEY_USER_INFO);
        }

        initView();
        bindData(userInfo);
        initEvent();
    }

    private void initView() {
        this.btnBack = (Button) this.findViewById(R.id.id_btn_back);
        this.imgAvatar = (ImageView) this.findViewById(R.id.id_img_user_portrait);
        this.tvUsername = (TextView) this.findViewById(R.id.id_tv_user_name);
        this.btnAddFriend = (Button) this.findViewById(R.id.id_btn_add_friend);
        this.btnSendRequest = (Button) this.findViewById(R.id.id_btn_request_friend);
        this.btnSendChat = (Button) this.findViewById(R.id.id_btn_send_chat);

        if (userInfo.relation == 1) {//好友
            this.btnSendChat.setVisibility(View.VISIBLE);
        } else if (userInfo.relation == 0) {//不是好友
            this.btnAddFriend.setVisibility(View.VISIBLE);
        } else if (userInfo.relation == 2) {//邀请
            this.btnSendRequest.setVisibility(View.VISIBLE);
        }
    }

    private void bindData(CUserModel userInfo) {
        if (userInfo != null) {
            //头像
            ImageLoaderUtils.getImageLoader(this).displayImage(userInfo.avatar, this.imgAvatar,
                    ImageLoaderUtils.getRoundDisplayOptions(getResources().getDimensionPixelSize(R.dimen.user_portrait_height)));
            //昵称
            this.tvUsername.setText(userInfo.name);
        }
    }

    private void initEvent() {
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:显示头像大图
            }
        });

        this.btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:添加好友
                Intent intent = new Intent(SearchUserInfoActivity.this, RequestMessageActivity.class);
                intent.putExtra(RequestMessageActivity.KEY_TATGETID, userInfo.userId);
                startActivity(intent);
                finish();
            }
        });

        this.btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SearchUserInfoActivity.this, "发送邀请成功", Toast.LENGTH_SHORT).show();
                finish();
                if (userInfo != null) {
                    send(userInfo.name, "Hi, 我在美术圈，你可以通过我的手机号加我为好友#美术圈是你最好的朋友圈#下载地址:http://www.meishuquan.net/Down.aspx");
                }
            }
        });

        this.btnSendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchUserInfoActivity.this, ChatActivity.class);
                intent.putExtra("title", userInfo.name);
                intent.putExtra("targetId", userInfo.rongCloud);
                intent.putExtra("type", "PRIVATE");
                startActivity(intent);
                finish();
            }
        });
    }

    private void send(String number, String message) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        sendIntent.putExtra("sms_body", message);
        try {
            startActivity(sendIntent);
        } catch (Exception e) {
            Toast.makeText(this, R.string.act_not_found_exception, Toast.LENGTH_SHORT).show();
        }

    }
}
