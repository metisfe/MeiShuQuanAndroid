package com.metis.meishuquan.activity.info;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.metis.meishuquan.R;
import com.metis.meishuquan.view.shared.TitleView;

public class AdvanceActivity extends BaseActivity {

    private TitleView mTitleView = null;

    private EditText mAdvanceEt, mContactEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance);

        mAdvanceEt = (EditText)findViewById(R.id.advance_input);
        mContactEt = (EditText)findViewById(R.id.advance_contact);

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

            }
        });

    }

}
