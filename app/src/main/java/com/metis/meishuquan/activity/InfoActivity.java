package com.metis.meishuquan.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.metis.meishuquan.R;
import com.metis.meishuquan.view.shared.MyInfoBtn;

public class InfoActivity extends FragmentActivity implements View.OnClickListener {

    private MyInfoBtn mNickView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        this.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mNickView = (MyInfoBtn)findViewById(R.id.info_nick);
        mNickView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_nick:
                Intent it = new Intent(this, InputActivity.class);
                it.putExtra(InputActivity.KEY_DEFAULT_STR, mNickView.getSecondaryText());
                it.putExtra(InputActivity.KEY_SINGLE_LINE, true);
                startActivityForResult(it, 200);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this, "onActivityResult", Toast.LENGTH_SHORT).show();
        if (resultCode == 200) {
            mNickView.setSecondaryText(data.getStringExtra(InputActivity.KEY_DEFAULT_STR));
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
