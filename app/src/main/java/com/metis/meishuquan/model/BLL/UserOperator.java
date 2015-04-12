package com.metis.meishuquan.model.BLL;

import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.util.SystemUtil;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wj on 15/4/6.
 */
public class UserOperator {
    private boolean flag;
    private static UserOperator operator = null;

    private final String LOGIN = "v1.1/UserCenter/Login?";//登录
    private final String REGISTER = "v1.1/UserCenter/Register";//注册
    private final String REQUESTCODE = "v1.1/UserCenter/RegisterCode?";//获取验证码
    private final String RESETPWD = "";//重围密码
    private final String USERROLE = "v1.1/UserCenter/UserRole";

    private UserOperator() {
        flag = ApiDataProvider.initProvider();
    }

    public static UserOperator getInstance() {
        if (operator == null) {
            operator = new UserOperator();
        }
        return operator;
    }

    public void addUserDataToCache() {

    }

    public void login(String account, String pwd, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder sb = new StringBuilder(LOGIN);
                sb.append("account=" + account);
                sb.append("&pwd=" + pwd);
                List<Pair<String, String>> pram = new ArrayList<>();
                Pair<String, String> pair1 = new Pair<>("account", account);
                Pair<String, String> pair2 = new Pair<>("pwd", pwd);
                pram.add(pair1);
                pram.add(pair2);

                ApiDataProvider.getmClient().invokeApi(LOGIN, null, HttpPost.METHOD_NAME, pram, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        }
    }

    public void register(String phone, String code, String pwd, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder sb = new StringBuilder(REGISTER);
                sb.append("phone=" + phone);
                sb.append("&code=" + code);
                sb.append("&pwd=" + pwd);
                List<Pair<String, String>> pram = new ArrayList<>();
                Pair<String, String> pair1 = new Pair<>("phone", phone);
                Pair<String, String> pair2 = new Pair<>("code", code);
                Pair<String, String> pair3 = new Pair<>("pwd", pwd);
                pram.add(pair1);
                pram.add(pair2);
                pram.add(pair3);
                ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpPost.METHOD_NAME, pram, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        }
    }


    public void getRequestCode(String phone, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder sb = new StringBuilder(REQUESTCODE);
                sb.append("phone=" + phone);
                ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        }
    }

    public void resetPwd(String phone, String newPwd, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder sb = new StringBuilder(RESETPWD);
                sb.append("phone=" + phone);
                sb.append("&newPwd=" + newPwd);
                List<Pair<String, String>> pram = new ArrayList<>();
                Pair<String, String> pair1 = new Pair<>("phone", phone);
                Pair<String, String> pair2 = new Pair<>("newPwd", newPwd);
                pram.add(pair1);
                pram.add(pair2);
                ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpPost.METHOD_NAME, pram, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        }
    }

    public void addUserRoleToCache() {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                ApiDataProvider.getmClient().invokeApi(USERROLE, null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {
                    @Override
                    public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                        if (result != null && result.getInfo().equals(String.valueOf(0))) {
                            Gson gson = new Gson();
                            String json = gson.toJson(result);
                            if (!json.isEmpty()) {
                                Log.i("user_role", json);
                                SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
                                spu.delete(SharedPreferencesUtil.USER_ROLE);
                                spu.add(SharedPreferencesUtil.USER_ROLE, json);
                            }
                        }
                    }
                });
            }

        }

    }
}
