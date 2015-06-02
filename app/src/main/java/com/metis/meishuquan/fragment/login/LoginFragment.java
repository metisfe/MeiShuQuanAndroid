package com.metis.meishuquan.fragment.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.base.BaseFragment;
import com.metis.meishuquan.model.BLL.UserOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.IdTypeEnum;
import com.metis.meishuquan.model.enums.LoginStateEnum;
import com.metis.meishuquan.model.login.LoginUserData;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.util.GlobalData;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.view.shared.TabBar;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.regex.Pattern;

/**
 * Fragment:登录
 * Created by wj on 15/4/5.
 */
public class LoginFragment extends BaseFragment {
    private Button btnRegister, btnLogin, btnBack, btnResetPwd;
    private EditText etUserName, etPwd;

    private UserOperator userOperator;

    private boolean isPressLogin = false;
    private ProgressDialog progressDialog;

    private Pattern pattern = Pattern.compile("^1\\d{10}");


    @Override
    protected int onLayoutIdGenerated() {
        return R.layout.fragment_user_login;
    }

    @Override
    protected void onViewCreated(View parentView) {
        //缓存注册所需的身份数据
        userOperator = UserOperator.getInstance();
        userOperator.addUserRoleToCache();

        initView(parentView);
        initEvent();
    }

    private void initView(View rootView) {
        btnBack = (Button) rootView.findViewById(R.id.id_btn_user_login_back);
        btnLogin = (Button) rootView.findViewById(R.id.id_btn_user_login);
        btnRegister = (Button) rootView.findViewById(R.id.id_btn_user_register);
        btnResetPwd = (Button) rootView.findViewById(R.id.id_btn_login_reset_pwd);
        etUserName = (EditText) rootView.findViewById(R.id.id_et_login_username);
        etPwd = (EditText) rootView.findViewById(R.id.id_et_login_pwd);

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
                if (isPressLogin) {
                    return;
                }
                String accout = etUserName.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                if (!verify()) {
                    return;
                }
                progressDialog = ProgressDialog.show(getActivity(), "", "正在登录，请稍候！");
                userOperator.login(accout, pwd, new ApiOperationCallback<ReturnInfo<String>>() {
                    @Override
                    public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                        progressDialog.cancel();
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
                            Log.i("用户信息", finalUserInfoJson);
                            SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
                            spu.update(SharedPreferencesUtil.USER_LOGIN_INFO, finalUserInfoJson);

                            //persistent user data
                            getMetisSettings().persistentUser(user.getData());

                            //update field of UserInfo to main application
                            MainApplication.userInfo = user.getData();
                            //根据用户角色控制显示模块
                            if (user.getData().getUserRole() == IdTypeEnum.TEACHER.getVal() || user.getData().getUserRole() == IdTypeEnum.STUDIO.getVal()) {
                                GlobalData.tabs.add(1);
                                GlobalData.tabs.add(3);
                                TabBar.showOrHide(1, true);
                                TabBar.showOrHide(3, true);
                            } else {
                                GlobalData.tabs.clear();
                                TabBar.showOrHide(1, false);
                                TabBar.showOrHide(3, false);
                            }

                            //hide input method
                            Utils.hideInputMethod(getActivity(), etPwd);
                            Utils.hideInputMethod(getActivity(), etUserName);
                            Toast.makeText(MainApplication.UIContext, "登录成功", Toast.LENGTH_SHORT).show();
                            getActivity().setResult(Activity.RESULT_OK);
                            isPressLogin = false;
                            getActivity().finish();
                        } else if (result != null && result.getInfo().equals(String.valueOf(1))) {
                            Toast.makeText(MainApplication.UIContext, result.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            isPressLogin = false;
                        } else if (result != null || exception != null) {
                            progressDialog.dismiss();
                            Log.e("**LoginFragment", exception.getCause().toString());
                            isPressLogin = false;
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
                FragmentTransaction ft = getFragmentTransaction();
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
                FragmentTransaction ft =getFragmentTransaction();
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
//        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

        if (!pattern.matcher(accout).matches()) {
            Toast.makeText(getActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pwd.isEmpty()) {
            Toast.makeText(MainApplication.UIContext, "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
