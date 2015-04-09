package com.metis.meishuquan.model.BLL;

import android.util.Log;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.login.User;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.SystemUtil;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;

/**
 * Created by WJ on 2015/4/9.
 */
public class UserInfoOperator {

    private static UserInfoOperator sOperator = new UserInfoOperator();

    private static String URL_ = "v1.1/UserCenter/GetUser?userId=0";

    public static UserInfoOperator getInstance () {
        return sOperator;
    }

    private UserInfoOperator () {

    }

    public void getUserInfo (String uid, final OnGetListener<User> userListener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            ApiDataProvider.getmClient().invokeApi(URL_, null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        User user = gson.fromJson(json, new TypeToken<User>(){}.getType());
                        if (user != null) {
                            userListener.onGet(true, user);
                        } else {
                            userListener.onGet(false, null);
                        }

                    } else {
                        userListener.onGet(false, null);
                    }

                }
            });
            /*if (flag) {
                StringBuilder sb = new StringBuilder(LOGIN);
                sb.append("account=" + account);
                sb.append("&pwd=" + pwd);
                ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }*/
        }
    }

    public interface OnGetListener<T> {
        public void onGet (boolean succeed, T t);
    }
}
