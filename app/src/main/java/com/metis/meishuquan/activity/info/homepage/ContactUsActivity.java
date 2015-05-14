package com.metis.meishuquan.activity.info.homepage;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.model.BLL.StudioBaseInfo;
import com.metis.meishuquan.model.BLL.StudioOperator;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.util.ImageLoaderUtils;

public class ContactUsActivity extends BaseActivity {

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
                mDetailsTv.setText(
                        studioBaseInfo.getAddress() + "\n" +
                                studioBaseInfo.getTelephone() + "\n" +
                        studioBaseInfo.getMicroblog() + "\n" +
                                studioBaseInfo.getWeChat() + "\n" +
                                studioBaseInfo.getWebSite() + "\n"
                );
                ImageLoaderUtils.getImageLoader(ContactUsActivity.this).displayImage(
                        studioBaseInfo.getAddressPhoto(), mMapIv,
                        ImageLoaderUtils.getNormalDisplayOptions(R.drawable.ic_launcher)
                );
            }
        });
    }
}
