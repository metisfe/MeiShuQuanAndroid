package com.metis.meishuquan.activity.circle;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.circle.ReturnOnlyInfo;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.view.circle.CircleTitleBar;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;

public class RequestMessageActivity extends FragmentActivity {
    public static final String KEY_TATGETID = "targetid";

    private CircleTitleBar titleBar;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_message);

        this.editText = (EditText) this.findViewById(R.id.fragment_circle_requestmessage_edittext);
        this.editText.setText(MainApplication.userInfo.getName());
        this.titleBar = (CircleTitleBar) this.findViewById(R.id.fragment_circle_requestmessage_titlebar);

        titleBar.setLeftButton("返回", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.setText("好友验证");
        titleBar.setRightButton("发送", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int targetId = getIntent().getExtras().getInt(KEY_TATGETID);
                StringBuilder PATH = new StringBuilder("v1.1/Message/AddFriend");
                PATH.append("?session=");
                PATH.append(MainApplication.userInfo.getCookie());
                PATH.append("&userId=");
                PATH.append(targetId);
                PATH.append("&type=1");

                final ProgressDialog progressDialog = new ProgressDialog(RequestMessageActivity.this);
                progressDialog.show();
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null,
                        HttpGet.METHOD_NAME, null, ReturnOnlyInfo.class,
                        new ApiOperationCallback<ReturnOnlyInfo>() {
                            @Override
                            public void onCompleted(ReturnOnlyInfo result, Exception exception, ServiceFilterResponse response) {
                                progressDialog.cancel();
                                if (result != null && result.option != null && result.option.isSuccess()) {
                                    Toast.makeText(RequestMessageActivity.this, "请求成功，等待对方验证", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(RequestMessageActivity.this, "请求失败，请重试", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });
    }
}
