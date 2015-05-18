package com.metis.meishuquan.fragment.circle;

import android.app.ProgressDialog;
import android.graphics.Path;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.circle.CPhoneFriend;
import com.metis.meishuquan.model.circle.PhoneFriend;
import com.metis.meishuquan.model.circle.ReturnOnlyInfo;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.view.circle.CircleTitleBar;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.util.ArrayList;

/**
 * Created by wudi on 4/18/2015.
 */
public class RequestMessageFragment extends Fragment {
    private CircleTitleBar titleBar;
    private ViewGroup rootView;
    private EditText editText;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_requestmessage, container, false);
        this.editText = (EditText) rootView.findViewById(R.id.fragment_circle_requestmessage_edittext);
        this.titleBar = (CircleTitleBar) rootView.findViewById(R.id.fragment_circle_requestmessage_titlebar);

        this.titleBar.setLeftButton("返回", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        this.titleBar.setText("好友验证");
        this.titleBar.setRightButton("发送", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String targetId = getArguments().getString("targetid");
                StringBuilder PATH = new StringBuilder("v1.1/Message/AddFriend");
                PATH.append("?session=");
                PATH.append(MainApplication.getSession());
                PATH.append("&userId=");
                PATH.append(targetId);
                PATH.append("&type=1");

                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.show();
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null,
                        HttpGet.METHOD_NAME, null, ReturnOnlyInfo.class,
                        new ApiOperationCallback<ReturnOnlyInfo>() {
                            @Override
                            public void onCompleted(ReturnOnlyInfo result, Exception exception, ServiceFilterResponse response) {
                                progressDialog.cancel();
                                if (result != null && result.option != null && result.option.isSuccess()) {
                                    Toast.makeText(getActivity(), "请求成功", Toast.LENGTH_LONG).show();
                                    getActivity().getSupportFragmentManager().popBackStack();
                                } else {
                                    Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        return rootView;
    }
}
