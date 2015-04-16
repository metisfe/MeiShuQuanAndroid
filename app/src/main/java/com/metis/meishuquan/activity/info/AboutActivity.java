package com.metis.meishuquan.activity.info;

import android.content.Intent;
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

public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private MyInfoBtn mScoreBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        this.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mScoreBtn = (MyInfoBtn)this.findViewById(R.id.about_score);
        mScoreBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_score:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=" + this.getPackageName()));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "no activity found to handle this intent", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
