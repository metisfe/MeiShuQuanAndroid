package com.metis.meishuquan.model.BLL;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.RequestCodeTypeEnum;
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
    private final String REGISTER = "v1.1/UserCenter/Register?";//注册
    private final String REQUESTCODE = "v1.1/UserCenter/RegisterCode?";//获取验证码
    private final String FORGETPWD = "v1.1/UserCenter/ForgetPassword?";//忘记密码
    private final String USERROLE = "v1.1/UserCenter/UserRole?";
    private static final String SESSION = MainApplication.userInfo.getCookie();

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
                StringBuilder PATH = new StringBuilder(LOGIN);
                PATH.append("account=" + account);
                PATH.append("&pwd=" + pwd);
                PATH.append("&session=" + SESSION);
                List<Pair<String, String>> pram = new ArrayList<>();
                Pair<String, String> pair1 = new Pair<>("account", account);
                Pair<String, String> pair2 = new Pair<>("pwd", pwd);
                pram.add(pair1);
                pram.add(pair2);

                ApiDataProvider.getmClient().invokeApi(LOGIN, null, HttpPost.METHOD_NAME, pram, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        }
    }

    public void register(String phone, String code, String pwd, int roleId, String inviteCode, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder PATH = new StringBuilder(REGISTER);
                PATH.append("phone=" + phone);
                PATH.append("&code=" + code);
                PATH.append("&pwd=" + pwd);
                PATH.append("&roleId=" + roleId);
                if (!TextUtils.isEmpty(inviteCode)) {
                    PATH.append("&invitationcode=" + inviteCode);
                }
                PATH.append("&session=" + SESSION);
                List<Pair<String, String>> pram = new ArrayList<>();
                Pair<String, String> pair1 = new Pair<>("phone", phone);
                Pair<String, String> pair2 = new Pair<>("code", code);
                Pair<String, String> pair3 = new Pair<>("pwd", pwd);
                Pair<String, String> pair4 = new Pair<>("roleId", String.valueOf(roleId));
                pram.add(pair1);
                pram.add(pair2);
                pram.add(pair3);
                pram.add(pair4);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpPost.METHOD_NAME, pram, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        }
    }


    public void getRequestCode(String phone, RequestCodeTypeEnum operation, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder PATH = new StringBuilder(REQUESTCODE);
                PATH.append("phone=" + phone);
                PATH.append("&operation=" + operation.getVal());
                PATH.append("&session=" + SESSION);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        }
    }

    public void forgetPwd(String account, String code, String newPwd, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder PATH = new StringBuilder(FORGETPWD);
                PATH.append("account=" + account);
                PATH.append("&code=" + code);
                PATH.append("&newPwd=" + newPwd);
                PATH.append("&type=" + 1);
                PATH.append("&session=" + MainApplication.userInfo.getCookie());
                List<Pair<String, String>> pram = new ArrayList<>();
                Pair<String, String> pair1 = new Pair<>("account", account);
                Pair<String, String> pair2 = new Pair<>("code", code);
                Pair<String, String> pair3 = new Pair<>("newPwd", newPwd);
                Pair<String, String> pair4 = new Pair<>("type", String.valueOf(1));
                pram.add(pair1);
                pram.add(pair2);
                pram.add(pair3);
                pram.add(pair4);
//                ApiDataProvider.getmClient().invokeApi(FORGETPWD, null, HttpPost.METHOD_NAME, pram, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        }
    }

    public void addUserRoleToCache() {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder PATH = new StringBuilder(USERROLE);
                PATH.append("&session=" + SESSION);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {
                    @Override
                    public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                        if (result != null && result.getInfo().equals(String.valueOf(0))) {
                            Gson gson = new Gson();
                            String json = gson.toJson(result);
                            if (!json.isEmpty()) {
                                Log.i("user_role", json);
                                SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
                                spu.update(SharedPreferencesUtil.USER_ROLE, json);
                            }
                        }
                    }
                });
            }

        }

    }

    public void searchUser(String account, String pwd, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder PATH = new StringBuilder(LOGIN);
                PATH.append("account=" + account);
                PATH.append("&pwd=" + pwd);
                PATH.append("&session=" + SESSION);
                List<Pair<String, String>> pram = new ArrayList<>();
                Pair<String, String> pair1 = new Pair<>("account", account);
                Pair<String, String> pair2 = new Pair<>("pwd", pwd);
                pram.add(pair1);
                pram.add(pair2);

                ApiDataProvider.getmClient().invokeApi(LOGIN, null, HttpPost.METHOD_NAME, pram, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        }
    }
}
