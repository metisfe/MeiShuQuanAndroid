package com.metis.meishuquan.activity.info;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.metis.meishuquan.R;
import com.metis.meishuquan.view.shared.TitleView;

public class ConstellationActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{

    public static final int REQUEST_CODE_CONSTELLATION = 202;

    public static final String KEY_CONSTELLATION = "Constellation";

    private TitleView mTitleView = null;

    private String[] mConstellationArr = null;
    private RadioGroup mGroup = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constellation);

        mConstellationArr = getResources().getStringArray(R.array.constellation);

        String con = getIntent().getStringExtra(KEY_CONSTELLATION);

        mTitleView = (TitleView)findViewById(R.id.title);
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mGroup = (RadioGroup)findViewById(R.id.constellation_radio_group);
        mGroup.setOnCheckedChangeListener(this);
        for (int i = 0; i < mConstellationArr.length; i++) {
            String conStr = mConstellationArr[i];
            RadioButton button = new RadioButton(this);
            ViewGroup.LayoutParams params = button.getLayoutParams();
            if (params == null) {
                params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            }
            button.setText(conStr);
            //button.setId(100 + i);
            button.setLayoutParams(params);
            button.setChecked(conStr.equals(con));
            mGroup.addView(button);
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        final int length = group.getChildCount();
        for (int i = 0; i < length; i++) {
            RadioButton button = (RadioButton)group.getChildAt(i);
            if (button.isChecked()) {
                Intent it = new Intent ();
                it.putExtra(KEY_CONSTELLATION, button.getText());
                setResult(RESULT_OK, it);
                finish();
            }
        }
    }
}
