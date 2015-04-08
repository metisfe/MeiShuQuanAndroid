package com.metis.meishuquan.fragment.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.metis.meishuquan.MainActivity;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;

import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

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

    // 填写从短信SDK应用后台注册得到的APPKEY
    private static String APPKEY = "6a2f9f137b4c";//463db7238681  27fe7909f8e8
    // 填写从短信SDK应用后台注册得到的APPSECRET
    private static String APPSECRET = "4becef73e97fb61e6fd46a11c1ab1557";//

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

//        SMSSDK.initSDK(MainApplication.UIContext, APPKEY, APPSECRET);
//        EventHandler eh=new EventHandler(){
//
//            @Override
//            public void afterEvent(int event, int result, Object data) {
//
//                Message msg = new Message();
//                msg.arg1 = event;
//                msg.arg2 = result;
//                msg.obj = data;
//                handler.sendMessage(msg);
//            }
//
//        };
//        SMSSDK.registerEventHandler(eh);
    }

//    Handler handler=new Handler(){
//
//        @Override
//        public void handleMessage(Message msg) {
//            // TODO Auto-generated method stub
//            super.handleMessage(msg);
//            int event = msg.arg1;
//            int result = msg.arg2;
//            Object data = msg.obj;
//            Log.e("event", "event=" + event);
//            if (result == SMSSDK.RESULT_COMPLETE) {
//                //短信注册成功后，返回MainActivity,然后提示新好友
//                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
//                    Toast.makeText(MainApplication.UIContext, "提交验证码成功", Toast.LENGTH_SHORT).show();
//                    //textView2.setText("提交验证码成功");
//                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
//                    Toast.makeText(MainApplication.UIContext, "验证码已经发送", Toast.LENGTH_SHORT).show();
//                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//返回支持发送验证码的国家列表
//                    Toast.makeText(MainApplication.UIContext, "获取国家列表成功", Toast.LENGTH_SHORT).show();
//
//                }
//            } else {
//
//            }
//
//        }
//
//    };

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
//                String phone = etUserName.getText().toString().trim();
//                String verCode = etVerificationCode.getText().toString().trim();
//                if (phone.isEmpty()) {
//                    Toast.makeText(MainApplication.UIContext, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
//                }
//                if (verCode.isEmpty()) {
//                    Toast.makeText(MainApplication.UIContext, "请输入验证码", Toast.LENGTH_SHORT).show();
//                }
//                SMSSDK.submitVerificationCode("86", phone, verCode);
            }
        });

        btnGetVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time.start();//开始计时
                String phone = etUserName.getText().toString().trim();
                Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
                if (p.matcher(phone).matches()) {
                    //发送验证码
                    SMSSDK.getVerificationCode("86", phone);
                } else {
                    Toast.makeText(MainApplication.UIContext, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
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
