package com.metis.meishuquan.activity.circle;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.meishuquan.R;

public class SearchUserInfoActivity extends ActionBarActivity {
    private ImageView imgAvatar;
    private TextView tvUsername;
    private Button btnAddFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_user_info_for_qr);

        initView();
        bindData();
        initEvent();
    }

    private void initView() {
        this.imgAvatar = (ImageView) this.findViewById(R.id.id_img_user_portrait);
        this.tvUsername = (TextView) this.findViewById(R.id.id_tv_user_name);
        this.btnAddFriend = (Button) this.findViewById(R.id.id_btn_add_friend);
    }

    private void bindData() {

    }

    private void initEvent() {

    }
}
