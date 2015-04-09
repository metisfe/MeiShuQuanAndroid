package com.metis.meishuquan.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.R;
import com.metis.meishuquan.framework.WebAccessManager;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.view.shared.MyInfoBtn;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

public class InfoActivity extends FragmentActivity implements View.OnClickListener {

    private MyInfoBtn mNickView = null;

    private View mRecentsContainer = null;
    private TextView mRecentsContentTv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        /*UserInfoOperator.getInstance().getUserInfo("0", new ApiOperationCallback<ReturnInfo<String>> () {

            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                Log.v()
            }
        });*/

        this.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mNickView = (MyInfoBtn)findViewById(R.id.info_nick);
        mRecentsContainer = findViewById(R.id.info_recents_container);
        mRecentsContentTv = (TextView)findViewById(R.id.info_recents_content);

        mNickView.setOnClickListener(this);
        mRecentsContainer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent it = null;
        switch (v.getId()) {
            case R.id.info_nick:
                it = new Intent(this, InputActivity.class);
                it.putExtra(InputActivity.KEY_DEFAULT_STR, mNickView.getSecondaryText());
                it.putExtra(InputActivity.KEY_SINGLE_LINE, true);
                startActivityForResult(it, InputActivity.REQUEST_CODE_NICK);
                break;
            case R.id.info_recents_container:
                it = new Intent(this, InputActivity.class);
                it.putExtra(InputActivity.KEY_DEFAULT_STR, mRecentsContentTv.getText());
                it.putExtra(InputActivity.KEY_SINGLE_LINE, false);
                startActivityForResult(it, InputActivity.REQUEST_CODE_RECENTS);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case InputActivity.REQUEST_CODE_NICK:
                if (resultCode == RESULT_OK) {
                    CharSequence nick = data.getCharSequenceExtra(InputActivity.KEY_DEFAULT_STR);
                    Toast.makeText(this, "onActivityResult " + requestCode + " " + resultCode + " " + nick, Toast.LENGTH_SHORT).show();
                    mNickView.setSecondaryText(nick);
                }

                break;
            case InputActivity.REQUEST_CODE_RECENTS:
                if (resultCode == RESULT_OK) {
                    CharSequence recents = data.getCharSequenceExtra(InputActivity.KEY_DEFAULT_STR);
                    Toast.makeText(this, "onActivityResult " + requestCode + " " + resultCode + " " + recents, Toast.LENGTH_SHORT).show();
                    mRecentsContentTv.setText(recents);
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
