package com.metis.meishuquan.activity.info.homepage;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.model.BLL.StudioBaseInfo;
import com.metis.meishuquan.model.BLL.StudioOperator;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.util.ImageLoaderUtils;

public class ContactUsActivity extends BaseActivity {

    private static final String TAG = ContactUsActivity.class.getSimpleName();

    private TextView mDetailsTv = null;
    private ImageView mMapIv = null;

    private int mStudioId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        mStudioId = getIntent().getIntExtra(StudioBaseInfo.KEY_STUDIO_ID, 0);

        mDetailsTv = (TextView)findViewById(R.id.contact_us_details);
        mMapIv = (ImageView)findViewById(R.id.contact_us_map_img);

    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.studio_contact_us);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        StudioOperator.getInstance().getStudioBaseInfo(mStudioId, new UserInfoOperator.OnGetListener<StudioBaseInfo>() {
            @Override
            public void onGet(boolean succeed, StudioBaseInfo studioBaseInfo) {
                if (!succeed) {
                    return;
                }
                StringBuilder sb = new StringBuilder();
                String address = studioBaseInfo.getAddress();
                if (!TextUtils.isEmpty(address)) {
                    sb.append(getString(R.string.studio_address, address) + "\n");
                }
                String phone = studioBaseInfo.getTelephone();
                if (!TextUtils.isEmpty(phone)) {
                    sb.append(getString(R.string.studio_phone, phone) + "\n");
                }
                String weibo = studioBaseInfo.getMicroblog();
                if (!TextUtils.isEmpty(weibo)) {
                    sb.append(getString(R.string.studio_weibo, weibo) + "\n");
                }
                String wechat = studioBaseInfo.getWeChat();
                if (!TextUtils.isEmpty(wechat)) {
                    sb.append(getString(R.string.studio_wechat, wechat) + "\n");
                }
                String website = studioBaseInfo.getWebSite();
                if (!TextUtils.isEmpty(website)) {
                    sb.append(getString(R.string.studio_website, website) + "\n");
                }
                mDetailsTv.setText(sb.toString());
                Log.v(TAG, "onPostCreate=" + studioBaseInfo.getAddressPhoto());
                if (!TextUtils.isEmpty(studioBaseInfo.getAddressPhoto())) {
                    ImageLoaderUtils.getImageLoader(ContactUsActivity.this).displayImage(
                            studioBaseInfo.getAddressPhoto(), mMapIv,
                            ImageLoaderUtils.getNormalDisplayOptions(R.drawable.ic_launcher)
                    );
                }

            }
        });
    }
}
