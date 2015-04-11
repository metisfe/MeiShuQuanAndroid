package com.metis.meishuquan.activity.info;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;

import com.metis.meishuquan.activity.circle.InputActivity;

/**
 * Created by WJ on 2015/4/10.
 */
public class BaseActivity extends FragmentActivity {

    public void startInputActivityForResult (String title, CharSequence defStr, boolean singleLine, int requestCode) {
        startInputActivityForResult(title, defStr, singleLine, requestCode, InputType.TYPE_NULL);
    }

    public void startInputActivityForResult (String title, CharSequence defStr, boolean singleLine, int requestCode, int inputType) {
        Intent it = new Intent(this, InputActivity.class);
        it.putExtra(InputActivity.KEY_TITLE, title);
        it.putExtra(InputActivity.KEY_DEFAULT_STR, defStr);
        it.putExtra(InputActivity.KEY_SINGLE_LINE, singleLine);
        it.putExtra(InputActivity.KEY_REQUEST_CODE, requestCode);
        it.putExtra(InputActivity.KEY_INPUT_TYPE, inputType);
        startActivityForResult(it, requestCode);
    }
}
