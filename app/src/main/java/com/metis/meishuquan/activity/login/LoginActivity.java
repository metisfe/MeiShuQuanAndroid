package com.metis.meishuquan.activity.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.login.LoginFragment;

public class LoginActivity extends FragmentActivity {
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fm = this.getSupportFragmentManager();
        navigateTo(new LoginFragment());
    }

    private void navigateTo(Fragment fragment) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out, R.anim.fragment_popin, R.anim.fragment_popout);
        ft.add(R.id.id_rl_login_main, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (isFirstLevelPage(LoginFragment.class.getSimpleName())) {
            super.onBackPressed();
        }
    }

    private boolean isFirstLevelPage(String name) {
        if (name == null || name.length() == 0) {
            return false;
        }
        return name.equals(LoginFragment.class.getSimpleName());
    }
}
