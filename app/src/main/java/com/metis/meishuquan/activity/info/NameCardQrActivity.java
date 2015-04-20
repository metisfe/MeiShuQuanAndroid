package com.metis.meishuquan.activity.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.commons.QRFragment;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.view.shared.TitleView;

public class NameCardQrActivity extends BaseActivity {

    public static final String TAG = NameCardQrActivity.class.getSimpleName();

    public static final String KEY_DATA_STR = null;

    private QRFragment mQrFragment = null;

    private TitleView mTitleView = null;
    private ImageView mProfileIv = null;
    private TextView mNameTv, mGradeTv, mIntroduceTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        //final String string = getIntent().getStringExtra(KEY_DATA_STR);

        mTitleView = (TitleView)findViewById(R.id.title);
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleView.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent (QrActivity.this, ImagePreviewActivity.class));
                startActivity(new Intent(NameCardQrActivity.this, QrScanActivity.class));
            }
        });

        JsonObject json = new JsonObject();
        User user = MainApplication.userInfo;
        json.addProperty(User.KEY_USER_ID, user.getUserId());
        json.addProperty(User.KEY_NICK_NAME, user.getName());
        json.addProperty(User.KEY_USERAVATAR, user.getUserAvatar());
        mQrFragment = (QRFragment)getSupportFragmentManager().findFragmentById(R.id.qr_fragment);
        mQrFragment.showQrCodeWith(json.toString());
        mQrFragment.setOnImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mProfileIv = (ImageView)findViewById(R.id.qr_profile);
        mNameTv = (TextView)findViewById(R.id.qr_name);
        mGradeTv = (TextView)findViewById(R.id.qr_grade);
        mIntroduceTv = (TextView)findViewById(R.id.qr_self_introduce);

        ImageLoaderUtils.getImageLoader(this).displayImage(
                user.getUserAvatar(),
                mProfileIv,
                ImageLoaderUtils.getRoundDisplayOptions(getResources().getDimensionPixelSize(R.dimen.my_info_profile_size)));
        mNameTv.setText(user.getName());
        mGradeTv.setText(user.getGrade());
        mIntroduceTv.setText(user.getSelfIntroduce());
    }

}
