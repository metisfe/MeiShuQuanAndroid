package com.metis.meishuquan.fragment.login;

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
import android.widget.EditText;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.UserOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.RequestCodeTypeEnum;
import com.metis.meishuquan.model.login.RegisterCode;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.regex.Pattern;

/**
 * Created by wj on 15/4/6.
 */
public class ResetPwdFragment extends Fragment {
    private Button btnBack, btnSubmit, btnGetVerificationCode;
    private EditText etPhone, etVerificationCode, etNewPwd;

    private FragmentManager fm;
    private TimeCount time;
    private String code = "";//验证码
    private UserOperator userOperator;
    private Pattern pattern = Pattern.compile("^1\\d{10}");

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_user_reset_pwd, null, false);
        initView(rootView);
        initEvent();
        return rootView;
    }

    private void initView(ViewGroup rootView) {
        btnBack = (Button) rootView.findViewById(R.id.id_btn_user_resetpwd_back);
        btnSubmit = (Button) rootView.findViewById(R.id.id_resetpwd_btn_submit);
        btnGetVerificationCode = (Button) rootView.findViewById(R.id.id_regsiter_btn_verificationCode);
        etPhone = (EditText) rootView.findViewById(R.id.id_resetpwd_et_phone);
        etVerificationCode = (EditText) rootView.findViewById(R.id.id_resister_et_verificationCode);
        etNewPwd = (EditText) rootView.findViewById(R.id.id_resetpwd_et_new_pwd);

        fm = getActivity().getSupportFragmentManager();
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        userOperator = UserOperator.getInstance();
    }

    private void initEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(ResetPwdFragment.this);
                ft.commit();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = etPhone.getText().toString().trim();
                String verCode = etVerificationCode.getText().toString().trim();
                String pwd = etNewPwd.getText().toString().trim();
                if (phone.isEmpty()) {
                    Toast.makeText(getActivity(), "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pattern.matcher(phone).matches()) {
                    Toast.makeText(getActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (verCode.isEmpty()) {
                    Toast.makeText(getActivity(), "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (verCode.length() != 4) {
                    Toast.makeText(getActivity(), "请输入4位验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd.isEmpty()) {
                    Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
                    etNewPwd.requestFocus();
                    return;
                }
                String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pwdReg = Pattern.compile(regEx);
                if (pwdReg.matcher(etNewPwd.getText().toString().trim()).matches()) {
                    Toast.makeText(getActivity(), "密码中不能包含有特殊字符", Toast.LENGTH_SHORT).show();
                    etNewPwd.requestFocus();
                    return;
                }
                if (pwd.length() < 6 || pwd.length() > 12) {
                    Toast.makeText(getActivity(), "密码长度应在6-14位之间", Toast.LENGTH_SHORT).show();
                    etNewPwd.requestFocus();
                    return;
                }
                userOperator.forgetPwd(phone, verCode, pwd, new ApiOperationCallback<ReturnInfo<String>>() {
                    @Override
                    public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                        if (result != null && result.getInfo().equals(String.valueOf(0))) {
                            Toast.makeText(getActivity(), "修改成功,请重新登录", Toast.LENGTH_SHORT).show();
                            //getActivity().finish();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.remove(ResetPwdFragment.this);
                            ft.commit();
                        } else if (result != null && result.getErrorCode().equals(String.valueOf(0))) {
                            Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btnGetVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = etPhone.getText().toString();
                String code = etVerificationCode.getText().toString().trim();
                if (phone.isEmpty()) {
                    Toast.makeText(getActivity(), "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pattern.matcher(phone).matches()) {
                    Toast.makeText(getActivity(), "您输入的手机格式有误", Toast.LENGTH_SHORT).show();
                    return;
                }
                time.start();//开始计时
                userOperator.getRequestCode(phone, RequestCodeTypeEnum.RESETPWD, new ApiOperationCallback<ReturnInfo<String>>() {
                    @Override
                    public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                        if (result != null && result.getInfo().equals(String.valueOf(0))) {
                            Log.i(getClass().getSimpleName(), "验证码请求发送成功!");
                        } else if (result != null && result.getInfo().equals(String.valueOf(1))) {
                            if (result.getErrorCode().equals("0")) {
                                Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
                                time.onFinish();
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 时间计数器
     */
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
