package com.metis.meishuquan.fragment.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.UserOperator;

/**
 * Fragment:登录
 * Created by wj on 15/4/5.
 */
public class LoginFragment extends Fragment {
    private Button btnRegister, btnLogin, btnBack, btnResetPwd;
    private EditText etUserName, etPwd;

    private FragmentManager fragmentManager;
    private UserOperator userOperator;

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
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.remove(LoginFragment.this);
                ft.commit();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {//登录
            @Override
            public void onClick(View view) {
                //TODO:
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.remove(LoginFragment.this);
                ft.commit();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {//注册
            @Override
            public void onClick(View view) {
                SelectIdFragment selectIdFragment = new SelectIdFragment();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.add(R.id.content_container, selectIdFragment);
                ft.commit();
            }
        });
        btnResetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetPwdFragment resetPwdFragment = new ResetPwdFragment();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.add(R.id.content_container, resetPwdFragment);
                ft.commit();
            }
        });

    }
}
