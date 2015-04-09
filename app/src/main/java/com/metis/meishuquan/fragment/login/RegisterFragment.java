package com.metis.meishuquan.fragment.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.UserOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.login.RegisterCode;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.regex.Pattern;

/**
 * Fragment:用户注册
 * Created by wj on 15/4/5.
 */
public class RegisterFragment extends Fragment {
    private Button btnBack, btnSubmit, btnGetVerificationCode;
    private EditText etUserName, etVerificationCode, etPwd;
    private CheckBox chLicense;

    private FragmentManager fm;
    private TimeCount time;
    private UserOperator userOperator;
    private Fragment parentFragment;

    private String requestCode = "";

    public void setParentFragment(Fragment parentFragment) {
        this.parentFragment = parentFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        userOperator = UserOperator.getInstance();
    }

    private void initEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(RegisterFragment.this);
                ft.commit();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = etUserName.getText().toString().trim();
                String verCode = etVerificationCode.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                if (phone.isEmpty()) {
                    Toast.makeText(MainApplication.UIContext, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
                if (!p.matcher(phone).matches()) {
                    Toast.makeText(MainApplication.UIContext, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (verCode.isEmpty()) {
                    Toast.makeText(MainApplication.UIContext, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd.isEmpty()) {
                    Toast.makeText(MainApplication.UIContext, "请输入密码", Toast.LENGTH_SHORT).show();
                    etPwd.requestFocus();
                    return;
                }
                String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pwdReg = Pattern.compile(regEx);
                if (!pwdReg.matcher(etPwd.getText().toString().trim()).matches()) {
                    Toast.makeText(MainApplication.UIContext, "密码中不能包含有特殊字符", Toast.LENGTH_SHORT).show();
                    etPwd.requestFocus();
                    return;
                }
                if (pwd.length() < 6 || pwd.length() > 12) {
                    Toast.makeText(MainApplication.UIContext, "密码长度应在6-14位之间", Toast.LENGTH_SHORT).show();
                    etPwd.requestFocus();
                    return;
                }
                if (!verCode.equals("")) {
                    int i = verCode.compareTo(requestCode);
                    if (i == 0 && parentFragment != null) {
                        userOperator.register(phone, requestCode, pwd, new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                    FragmentTransaction ft = fm.beginTransaction();
                                    ft.replace(R.id.content_container, parentFragment);
                                    ft.commit();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(MainApplication.UIContext, "验证码验证超时，请重新验证", Toast.LENGTH_SHORT).show();
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
                time.start();//开始计时
                String phone = etUserName.getText().toString().trim();
                if (phone.isEmpty()) {
                    Toast.makeText(MainApplication.UIContext, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
                if (!p.matcher(phone).matches()) {
                    Toast.makeText(MainApplication.UIContext, "您输入的手机格式有误", Toast.LENGTH_SHORT).show();
                    return;
                }
                userOperator.getRequestCode(phone, new ApiOperationCallback<ReturnInfo<String>>() {
                    @Override
                    public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                        if (result != null && result.getInfo().equals(String.valueOf(0))) {
                            Gson gson = new Gson();
                            String json = gson.toJson(result);
                            RegisterCode code = gson.fromJson(json, new TypeToken<RegisterCode>() {
                            }.getType());
                            requestCode = code.getData();
                        }
                    }
                });
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
            requestCode = "null";
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            btnGetVerificationCode.setClickable(false);
            btnGetVerificationCode.setText(millisUntilFinished / 1000 + "秒");
        }
    }
}
