package com.metis.meishuquan.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.commons.InputFragment;

public class InputActivity extends FragmentActivity {

    public static final String
            KEY_DEFAULT_STR = "default_str",
            KEY_SINGLE_LINE = "single_line";

    private InputFragment mInputFragment = null;

    private String mDefaultStr = null;
    private boolean isSingleLine = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        this.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.putExtra(KEY_DEFAULT_STR, mInputFragment.getText());
                setResult(200, it);
                finish();
            }
        });

        Intent it = getIntent();
        mDefaultStr = it.getStringExtra(KEY_DEFAULT_STR);
        isSingleLine = it.getBooleanExtra(KEY_SINGLE_LINE, isSingleLine);

        mInputFragment = (InputFragment)getSupportFragmentManager().findFragmentById(R.id.input_fragment);
        mInputFragment.setSingleLine(isSingleLine);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
