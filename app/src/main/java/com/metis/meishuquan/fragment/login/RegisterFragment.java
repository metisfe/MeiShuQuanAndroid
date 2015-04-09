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

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;

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
                if (phone.isEmpty()) {
                    Toast.makeText(MainApplication.UIContext, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
                if (verCode.isEmpty()) {
                    Toast.makeText(MainApplication.UIContext, "请输入验证码", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnGetVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time.start();//开始计时
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
