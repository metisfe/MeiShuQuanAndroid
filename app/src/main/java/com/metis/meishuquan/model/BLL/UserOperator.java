package com.metis.meishuquan.model.BLL;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.SystemUtil;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;

import org.apache.http.client.methods.HttpGet;

/**
 * Created by wj on 15/4/6.
 */
public class UserOperator {
    private boolean flag;
    private static UserOperator operator = null;

    private final String LOGIN = "v1.1/UserCenter/Login?";//登录
    private final String REGISTER = "v1.1/UserCenter/Register?";//注册
    private final String REQUESTCODE = "v1.1/UserCenter/RegisterCode?";//获取验证码
    private final String RESETPWD = "";//重围密码

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
                ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
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
                ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        }
    }


    public void getRequestCode(String phone) {

    }

    public void resetCode() {
//        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
//            if (flag) {
//                StringBuilder sb = new StringBuilder(LOGIN);
//                sb.append("phone=" + phone);
//                sb.append("code=" + code);
//                sb.append("pwd=" + pwd);
//                ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
//            }
//        }
    }
}
