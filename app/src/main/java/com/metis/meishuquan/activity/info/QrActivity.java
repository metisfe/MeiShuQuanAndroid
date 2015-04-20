package com.metis.meishuquan.activity.info;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.commons.QRFragment;
import com.metis.meishuquan.view.shared.TitleView;

public class QrActivity extends BaseActivity {

    public static final String TAG = QrActivity.class.getSimpleName();

    public static final String KEY_DATA_STR = null;

    private QRFragment mQrFragment = null;

    private TitleView mTitleView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        final String string = getIntent().getStringExtra(KEY_DATA_STR);

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
                startActivity(new Intent (QrActivity.this, ImagePreviewActivity.class));
                //startActivity(new Intent(QrActivity.this, QrScanActivity.class));
            }
        });

        mQrFragment = (QRFragment)getSupportFragmentManager().findFragmentById(R.id.qr_fragment);
        mQrFragment.showQrCodeWith(string);
    }

}
