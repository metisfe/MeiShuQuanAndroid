package com.metis.meishuquan.activity.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.act.ActDetailActivity;
import com.metis.meishuquan.activity.act.SelectStudioActivity;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.view.shared.MyInfoBtn;
import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = SettingActivity.class.getSimpleName();

    private MyInfoBtn mModifyPwdView, mClearCacheView, mAboutUsView;

    private Button mLogoutBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mModifyPwdView = (MyInfoBtn)this.findViewById(R.id.setting_modify_pwd);
        mModifyPwdView.setOnClickListener(this);
        mAboutUsView = (MyInfoBtn)this.findViewById(R.id.setting_about_us);
        mAboutUsView.setOnClickListener(this);
        mClearCacheView = (MyInfoBtn)this.findViewById(R.id.setting_clear_cache);
        mClearCacheView.setOnClickListener(this);

        mLogoutBtn = (Button) findViewById(R.id.setting_logout);
        mLogoutBtn.setOnClickListener(this);

        mClearCacheView.setSecondaryText(formatSize(b2m(getCacheSize())));

    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.my_info_settings);
    }

    @Override
    public void onTitleRightPressed() {
        super.onTitleRightPressed();
        //ActiveOperator.getInstance().getActiveDetail();
        //StudioOperator.getInstance().getStudioBaseInfo(10090);
        /*Intent it = new Intent(this, StudioActivity.class);
        it.putExtra(StudioActivity.KEY_USER_ID, 100090);
        startActivity(it);*/
        startActivity(new Intent(this, SelectStudioActivity.class));
        /*ActiveOperator.getInstance().selectStudio(0);*/
        //startActivity(new Intent(this, ActDetailActivity.class));
        //startActivity(new Intent (this, ShareActivity.class));
        /*final UMSocialService mController
                = UMServiceFactory.getUMSocialService("www.baidu.com");
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, "1104485283", "k9f8JhWppP5r1N5t");
        qZoneSsoHandler.addToSocialSDK();

        QZoneShareContent content = new QZoneShareContent();
        content.setTargetUrl("http://china.ynet.com/3.1/1504/30/10037267.html?source=bdxsy");
        content.setAppWebSite("http://image.baidu.com/");
        content.setShareContent("嘿嘿嘿，哈哈哈");
        content.setTitle("这里是title");
        mController.setShareMedia(content);
        mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT);
        mController.openShare(this, new SocializeListeners.SnsPostListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {

            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_modify_pwd:
                startActivity(new Intent (this, ChangePwdActivity.class));
                break;
            case R.id.setting_about_us:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.setting_logout:
                //disconnect rong
                if (MainApplication.rongIM != null) {
                    MainApplication.rongIM.disconnect();
                    MainApplication.rongIM = null;
                    MainApplication.rongClient = null;
                }

                //clear userinfo of MainApplication
                MainApplication.userInfo = new User();

                //clear sharedpreferences
                SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(this);
                spu.delete(SharedPreferencesUtil.USER_LOGIN_INFO);

                //tip
                //Toast.makeText(this, "���˳�", Toast.LENGTH_SHORT).show();
                this.finish();
                break;
            case R.id.setting_clear_cache:
                final int sizeBefore = getCacheSize();
                clear();
                mClearCacheView.setSecondaryText(formatSize(b2m(getCacheSize())));
                Toast.makeText(this, getString(R.string.setting_cleared, formatSize(b2m(sizeBefore - getCacheSize()))), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private int getCacheSize () {
        DiscCacheAware cache = ImageLoaderUtils.getImageLoader(this).getDiscCache();
        return cache.getCurrentSize();
    }

    private float b2m (int size) {
        return (float)size / (1024 * 1024);
    }

    private int clear () {
        DiscCacheAware cache = ImageLoaderUtils.getImageLoader(this).getDiscCache();
        cache.clear();
        return cache.getCurrentSize();
    }

    private String formatSize (float size) {
        float floatSize = size * 100;
        int left = (int)floatSize / 100;
        int right = (int)floatSize % 100;
        return left + "." + right + "m";
    }

}
