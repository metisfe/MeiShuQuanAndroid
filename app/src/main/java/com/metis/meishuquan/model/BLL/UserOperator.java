package com.metis.meishuquan.model.BLL;

import com.metis.meishuquan.model.provider.ApiDataProvider;

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

    public void login(String account, String pwd) {

    }

    public void register(String phone, String code, String pwd) {

    }


    public void getRequestCode(String phone) {

    }

    public void resetCode() {

    }
}
