package com.metis.meishuquan.fragment.login;

import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.UserOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.LoginStateEnum;
import com.metis.meishuquan.model.enums.RequestCodeTypeEnum;
import com.metis.meishuquan.model.login.LoginUserData;
import com.metis.meishuquan.model.login.RegisterCode;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.util.Utils;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.regex.Pattern;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Fragment:用户注册
 * Created by wj on 15/4/5.
 */
public class RegisterFragment extends Fragment {
    private Button btnBack, btnSubmit, btnGetVerificationCode;
    private EditText etUserName, etVerificationCode, etPwd;
    private CheckBox chLicense;

    private FragmentManager fm;
    private TimeCount time = null;
    private UserOperator userOperator;
    private String requestCode = "";
    private int selectedId;//身份

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //接收身份信息
        if (getArguments() != null) {
            selectedId = getArguments().getInt("selectedId", -1);
        }

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_user_register, null, false);
        initView(rootView);
        initEvent();
        return rootView;
    }

    private void initView(ViewGroup rootView) {
        btnBack = (Button) rootView.findViewById(R.id.id_btn_user_register_back);
        btnSubmit = (Button) rootView.findViewById(R.id.id_register_btn_submit);
        btnGetVerificationCode = (Button) rootView.findViewById(R.id.id_regsiter_btn_verificationCode);
        etUserName = (EditText) rootView.findViewById(R.id.id_resister_et_username);
        etVerificationCode = (EditText) rootView.findViewById(R.id.id_resister_et_verificationCode);
        etPwd = (EditText) rootView.findViewById(R.id.id_register_et_pwd);
        chLicense = (CheckBox) rootView.findViewById(R.id.id_register_chb_license);

        fm = getActivity().getSupportFragmentManager();
        userOperator = UserOperator.getInstance();
    }

    private void initEvent() {
        //返回
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(RegisterFragment.this);
                ft.commit();
            }
        });
        //注册
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = etUserName.getText().toString().trim();
                String verCode = etVerificationCode.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                if (phone.isEmpty()) {
                    Toast.makeText(getActivity(), "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
                if (!p.matcher(phone).matches()) {
                    Toast.makeText(getActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (verCode.isEmpty()) {
                    Toast.makeText(getActivity(), "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd.isEmpty()) {
                    Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
                    etPwd.requestFocus();
                    return;
                }
                String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pwdReg = Pattern.compile(regEx);
                if (pwdReg.matcher(etPwd.getText().toString().trim()).matches()) {
                    Toast.makeText(getActivity(), "密码中不能包含有特殊字符", Toast.LENGTH_SHORT).show();
                    etPwd.requestFocus();
                    return;
                }
                if (pwd.length() < 6 || pwd.length() > 12) {
                    Toast.makeText(getActivity(), "密码长度应在6-14位之间", Toast.LENGTH_SHORT).show();
                    etPwd.requestFocus();
                    return;
                }
                if (!verCode.equals("")) {
                    int i = verCode.compareTo(requestCode);
                    if (i == 0) {
                        if (selectedId == -1) {
                            Log.e("roleId", "selectedRoleId为-1");
                        }
                        userOperator.register(phone, requestCode, pwd, selectedId, new ApiOperationCallback<ReturnInfo<String>>() {
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
                                    Toast.makeText(MainApplication.UIContext, "注册成功", Toast.LENGTH_SHORT).show();
                                    getActivity().finish();
                                } else if (result != null && result.getInfo().equals(String.valueOf(1))) {
                                    Toast.makeText(MainApplication.UIContext, result.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "验证码验证超时，请重新验证", Toast.LENGTH_SHORT).show();
                        etVerificationCode.setText("");
                        etVerificationCode.requestFocus();
                        return;
                    }
                }
            }
        });

        btnGetVerificationCode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String phone = etUserName.getText().toString().trim();
                if (phone.isEmpty()) {
                    Toast.makeText(getActivity(), "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
                if (!p.matcher(phone).matches()) {
                    Toast.makeText(getActivity(), "您输入的手机格式有误", Toast.LENGTH_SHORT).show();
                    return;
                }
                time = new TimeCount(60000, 1000);//构造CountDownTimer对象
                time.start();//开始计时
                userOperator.getRequestCode(phone, RequestCodeTypeEnum.REGISTOR, new ApiOperationCallback<ReturnInfo<String>>() {
                    @Override
                    public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                        if (result != null && result.getInfo().equals(String.valueOf(0))) {
                            Gson gson = new Gson();
                            String json = gson.toJson(result);
                            RegisterCode code = gson.fromJson(json, new TypeToken<RegisterCode>() {
                            }.getType());
                            requestCode = code.getData();
                        } else if (result != null && result.getInfo().equals(String.valueOf(1))) {
                            Toast.makeText(MainApplication.UIContext, result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        chLicense.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!chLicense.isChecked()) {
                    btnSubmit.setClickable(false);
                    btnSubmit.setBackgroundColor(Color.GRAY);
                } else {
                    btnSubmit.setClickable(true);
                    btnSubmit.setBackgroundColor(Color.RED);
                }
            }
        });
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            btnGetVerificationCode.setText("重新验证");
            btnGetVerificationCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            btnGetVerificationCode.setClickable(false);
            btnGetVerificationCode.setText(millisUntilFinished / 1000 + "秒");
        }
    }
}
