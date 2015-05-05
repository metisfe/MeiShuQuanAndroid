package com.metis.meishuquan.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.commons.InputFragment;
import com.metis.meishuquan.view.shared.TitleView;

public class InputActivity extends FragmentActivity {

    public static final String
            KEY_TITLE = "title",
            KEY_DEFAULT_STR = "default_str",
            KEY_SINGLE_LINE = "single_line",
            KEY_REQUEST_CODE = "request_code",
            KEY_INPUT_TYPE = "input_type",
            KEY_HINT = "input_hint";

    public static final int
            REQUEST_CODE_NICK = 100,
            REQUEST_CODE_RECENTS = 102,
            REQUEST_CODE_CV = 104,
            REQUEST_CODE_AGE = 106,
            REQUEST_CODE_DEPARTMENT_ADDRESS = 108,
            REQUEST_CODE_ACHIEVEMENT = 110,
            REQUEST_CODE_MEISHUQUAN_ID = 112;

    private TitleView mTitleView = null;
    private InputFragment mInputFragment = null;
    private TextView mExtraTipTv = null;

    private CharSequence mDefaultStr = null;
    private boolean isSingleLine = false;
    private String mHint = null;

    private int mRequestCode = REQUEST_CODE_NICK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Intent it = getIntent();
        mDefaultStr = it.getCharSequenceExtra(KEY_DEFAULT_STR);
        mHint = it.getStringExtra(KEY_HINT);
        isSingleLine = it.getBooleanExtra(KEY_SINGLE_LINE, isSingleLine);
        mRequestCode = it.getIntExtra(KEY_REQUEST_CODE, mRequestCode);
        String title = it.getStringExtra(KEY_TITLE);
        final int inputType = it.getIntExtra(KEY_INPUT_TYPE, InputType.TYPE_NULL);

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
                String text = mInputFragment.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(InputActivity.this, R.string.input_not_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent it = new Intent();
                it.putExtra(KEY_DEFAULT_STR, mInputFragment.getText());
                setResult(RESULT_OK, it);
                finish();
            }
        });
        mTitleView.setTitleText(title);

        mInputFragment = (InputFragment)getSupportFragmentManager().findFragmentById(R.id.input_fragment);
        mInputFragment.setSingleLine(isSingleLine);
        mInputFragment.setText(mDefaultStr);
        mInputFragment.setHint(mHint);
        mInputFragment.setInputType(inputType);

        mExtraTipTv = (TextView)findViewById(R.id.input_extra_tip);
        mExtraTipTv.setVisibility(mRequestCode == REQUEST_CODE_NICK ? View.VISIBLE : View.GONE);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
