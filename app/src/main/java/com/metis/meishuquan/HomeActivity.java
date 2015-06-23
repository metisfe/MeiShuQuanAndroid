package com.metis.meishuquan;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.metis.meishuquan.fragment.main.AssessFragment;
import com.metis.meishuquan.fragment.main.CircleFragment;
import com.metis.meishuquan.widget.DockBar;

public class HomeActivity extends FragmentActivity {

    private DockBar mDockBar = null;

    private Fragment mCurrentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDockBar = (DockBar)this.findViewById(R.id.home_dock_bar);

        AssessFragment assessFragment = new AssessFragment();
        CircleFragment circleFragment = new CircleFragment();

        DockBar.Dock appDock = new DockBar.Dock(this, 0, R.drawable.ic_launcher, R.string.app_name, assessFragment);
        DockBar.Dock helloDock = new DockBar.Dock(this, 1, R.drawable.ic_launcher, R.string.hello_world, circleFragment);
        mDockBar.addDock(appDock);
        mDockBar.addDock(helloDock);

        mDockBar.setOnDockItemClickListener(new DockBar.OnDockItemClickListener() {
            @Override
            public void onDockClick(View view, DockBar.Dock dock) {
                if (mCurrentFragment != null) {
                    hideFragment(mCurrentFragment);
                }
                showFragment(dock.target);
                Toast.makeText(HomeActivity.this, dock.title, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showFragment (Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (!fragment.isAdded()) {
            ft.add(R.id.home_container, fragment);
        } else {
            ft.show(fragment);
        }
        ft.commit();
        mCurrentFragment = fragment;
    }

    public void hideFragment (Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(fragment);
        ft.commit();
        mCurrentFragment = null;
    }
}
