package com.metis.meishuquan.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jit.video.ControlVideoView;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;

public class TestActivity extends BaseActivity {

    private static final String TAG = TestActivity.class.getSimpleName();

    private ControlVideoView mCvv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mCvv = (ControlVideoView)findViewById(R.id.video);
        mCvv.setVideoPath("https://metisfile.blob.core.chinacloudapi.cn/asset-932c435d-1500-80c3-a911-f1e51bc8ca1a/%E5%8F%B0%E9%9D%A2%E9%9D%99%E7%89%A9%EF%BC%88%E4%BA%8C%EF%BC%89.mp4?sv=2012-02-12&sr=c&si=bfabcdf1-dcb9-423a-b1e6-81185d253ce9&sig=a7ouuLUTVdjetWgGfOQGShNOuSZIK%2FH4Cwa4%2FgBNLkY%3D&st=2015-06-26T06:09:12Z&se=2115-06-02T06:09:12Z");
        mCvv.playVideo();
        mCvv.setFullScreenClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCvv.isFullScreen()) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    mCvv.setFullScreen(true);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mCvv.setFullScreen(false);
                }

            }
        });

        mCvv.setOnPlayEndListener(new ControlVideoView.OnPlayEndListener() {
            @Override
            public void onEnd() {
                if (mCvv.isFullScreen()) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mCvv.setFullScreen(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    return;
                }
            }
        });

        this.getTitleView().setVisibility(View.GONE);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.v(TAG, "onConfigurationChanged");
    }

    @Override
    public void onBackPressed() {
        if (mCvv.isFullScreen()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mCvv.setFullScreen(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
