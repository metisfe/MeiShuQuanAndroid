package com.metis.meishuquan.activity.info;

import android.graphics.Bitmap;
import android.graphics.drawable.ShapeDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.manager.common.QrCodeMakerTask;

public class InviteActivity extends BaseActivity {

    private ImageView mQrIv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        mQrIv = (ImageView)findViewById(R.id.invite_qr_code);

        new QrCodeMakerTask().makeQrCode(
                new QrCodeMakerTask.Task("https://www.baidu.com/", getResources().getDimensionPixelSize(R.dimen.invite_qr_code_size)),
                new QrCodeMakerTask.Callback() {
                    @Override
                    public void onCallback(Bitmap bitmap) {
                        mQrIv.setImageBitmap(bitmap);
                    }
                });

    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.title_activity_invite);
    }
}
