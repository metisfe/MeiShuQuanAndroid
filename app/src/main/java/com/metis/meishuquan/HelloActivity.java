package com.metis.meishuquan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class HelloActivity extends ActionBarActivity {

    private static final String KEY_VERSION = "last_version";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences shared = HelloActivity.this.getSharedPreferences(KEY_VERSION, MODE_PRIVATE);
                int version = shared.getInt(KEY_VERSION, 0);
                try {
                    PackageInfo pi=HelloActivity.this.getPackageManager().getPackageInfo(HelloActivity.this.getPackageName(), 0);
                    int versionCode = pi.versionCode;
                    if (version == versionCode) {
                        startActivity(new Intent(HelloActivity.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(HelloActivity.this, GuideActivity.class));
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putInt(KEY_VERSION, versionCode);
                        editor.commit();
                    }

                    finish();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }, 2000);
    }

}
