package com.metis.meishuquan.activity.info;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.metis.meishuquan.R;
import com.metis.meishuquan.view.shared.MyInfoBtn;
import com.metis.meishuquan.view.shared.TitleView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private TitleView mTitleView = null;

    private MyInfoBtn mScoreBtn = null, mVersionBtn = null,
            mAboutMeishuquanBtn = null, mStatementBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mTitleView = (TitleView)findViewById(R.id.title);
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mScoreBtn = (MyInfoBtn)this.findViewById(R.id.about_score);
        mVersionBtn = (MyInfoBtn)findViewById(R.id.about_version);
        mAboutMeishuquanBtn = (MyInfoBtn)findViewById(R.id.about_meishuquan);
        mStatementBtn = (MyInfoBtn)findViewById(R.id.about_statement);

        mScoreBtn.setOnClickListener(this);
        mVersionBtn.setOnClickListener(this);
        mAboutMeishuquanBtn.setOnClickListener(this);
        mStatementBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent it = null;
        switch (v.getId()) {
            case R.id.about_score:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=" + /*"com.android.chrome"*/this.getPackageName()));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "no activity found to handle this intent", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.about_version:
                try {
                    PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
                    Toast.makeText(this, getString(R.string.about_current_version) + ":" + info.versionName, Toast.LENGTH_SHORT).show();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.about_meishuquan:
                String content = readStringFromAssets("about");
                it = new Intent (this, TextActivity.class);
                it.putExtra(TextActivity.KEY_CONTENT, content);
                it.putExtra(TextActivity.KEY_TITLE, mAboutMeishuquanBtn.getText());
                startActivity(it);
                break;
            case R.id.about_statement:

                it = new Intent (this, TextActivity.class);
                it.putExtra(TextActivity.KEY_CONTENT, readStringFromAssets("statement"));
                it.putExtra(TextActivity.KEY_TITLE, mStatementBtn.getText());
                startActivity(it);
                break;
        }
    }

    private String readStringFromAssets (String path) {
        try {
            InputStream is = getAssets().open(path);
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(reader);
            String content = null;
            StringBuilder sb = new StringBuilder();
            while ((content = br.readLine()) != null) {
                sb.append(content + "\n");
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
