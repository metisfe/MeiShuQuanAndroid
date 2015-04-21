package com.metis.meishuquan.fragment.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.UserOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.LoginStateEnum;
import com.metis.meishuquan.model.login.LoginUserData;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.util.Utils;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Fragment:登录
 * Created by wj on 15/4/5.
 */
public class LoginFragment extends Fragment {
    private Button btnRegister, btnLogin, btnBack, btnResetPwd;
    private EditText etUserName, etPwd;

    private FragmentManager fragmentManager;
    private UserOperator userOperator;

    private boolean isPressLogin = true;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //缓存注册所需的身份数据
        userOperator = UserOperator.getInstance();
        userOperator.addUserRoleToCache();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_user_login, null, false);
        initView(rootView);
        initEvent();
        return rootView;
    }

    private void initView(ViewGroup rootView) {
        btnBack = (Button) rootView.findViewById(R.id.id_btn_user_login_back);
        btnLogin = (Button) rootView.findViewById(R.id.id_btn_user_login);
        btnRegister = (Button) rootView.findViewById(R.id.id_btn_user_register);
        btnResetPwd = (Button) rootView.findViewById(R.id.id_btn_login_reset_pwd);
        etUserName = (EditText) rootView.findViewById(R.id.id_et_login_username);
        etPwd = (EditText) rootView.findViewById(R.id.id_et_login_pwd);

        fragmentManager = getActivity().getSupportFragmentManager();
    }

    private void initEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {//返回
            @Override
            public void onClick(View view) {
                Utils.hideInputMethod(getActivity(), etUserName);
                Utils.hideInputMethod(getActivity(), etPwd);
                getActivity().finish();
            }
        });
        //登录
        btnLogin.setOnClickListener(new View.OnClickListener() {//登录
            @Override
            public void onClick(View view) {
                if (!isPressLogin) {
                    return;
                }
                String accout = etUserName.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                if (!verify()) {
                    return;
                }
                userOperator.login(accout, pwd, new ApiOperationCallback<ReturnInfo<String>>() {
                    @Override
                    public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                        if (result != null && result.getInfo().equals(String.valueOf(0))) {
                            Gson gson = new Gson();
                            String json = gson.toJson(result);
                            Log.e("userInfo", json);
                            //json to object
                            final LoginUserData user = gson.fromJson(json, new TypeToken<LoginUserData>() {
                            }.getType());
                            //set login state
                            user.getData().setAppLoginState(LoginStateEnum.YES);

                            //connect to Rong
                            String token = user.getData().getToken();
                            ChatManager.userRongId = user.getData().getRongCloudId();
                            MainApplication.rongConnect(token);

                            //add userInfo into sharedPreferences
                            Gson gson1 = new Gson();
                            String finalUserInfoJson = gson1.toJson(user);
                            SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(getActivity());
                            spu.update(SharedPreferencesUtil.USER_LOGIN_INFO, finalUserInfoJson);

                            //update field of UserInfo to main application
                            MainApplication.userInfo = user.getData();

                            //hide input method
                            Utils.hideInputMethod(getActivity(), etPwd);
                            Utils.hideInputMethod(getActivity(), etUserName);
                            Toast.makeText(MainApplication.UIContext, "登录成功", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            isPressLogin = false;
                        } else {
                            Toast.makeText(MainApplication.UIContext, "账号与密码不匹配，请重新输入", Toast.LENGTH_SHORT).show();
                            isPressLogin = true;
                        }
                    }
                });
            }
        });
        //注册
        btnRegister.setOnClickListener(new View.OnClickListener() {//注册
            @Override
            public void onClick(View view) {
                //hide input method
                Utils.hideInputMethod(getActivity(), etPwd);
                Utils.hideInputMethod(getActivity(), etUserName);

                //turn to selectIdFragment
                SelectIdFragment selectIdFragment = new SelectIdFragment();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                ft.add(R.id.id_rl_login_main, selectIdFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        //重置密码
        btnResetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hide input method
                Utils.hideInputMethod(getActivity(), etPwd);
                Utils.hideInputMethod(getActivity(), etUserName);

                ResetPwdFragment resetPwdFragment = new ResetPwdFragment();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.add(R.id.id_rl_login_main, resetPwdFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    private boolean verify() {
        String accout = etUserName.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        if (accout.isEmpty()) {
            Toast.makeText(MainApplication.UIContext, "请输入登录账号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pwd.isEmpty()) {
            Toast.makeText(MainApplication.UIContext, "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
