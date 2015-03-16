package com.metis.meishuquan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.metis.meishuquan.fragment.BaseFragment;
import com.metis.meishuquan.fragment.main.ClassFragment;
import com.metis.meishuquan.fragment.main.CommentFragment;
import com.metis.meishuquan.fragment.main.MyInfoFragment;
import com.metis.meishuquan.fragment.main.ToplineFragment;
import com.metis.meishuquan.framework.util.TextureRender;
import com.metis.meishuquan.ui.SelectedTabType;
import com.metis.meishuquan.util.ContractUtility;
import com.metis.meishuquan.util.Environments;
import com.metis.meishuquan.util.GlobalData;
import com.metis.meishuquan.util.SystemUtil;
import com.metis.meishuquan.util.UnifiedConfigurationOverride;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.util.ViewUtils;
import com.metis.meishuquan.view.DialogManager;
import com.metis.meishuquan.view.shared.TabBar;

import java.util.Properties;

public class MainActivity extends FragmentActivity implements TabBar.TabSelectedListener {
    private static FragmentManager fm;
    private static final String PressBackAgainToQuiteApplicationMessage = "Back agin to quit";
    private boolean doWantToQuite;

    private String currentFragment;

    public static MainActivity self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        self = this;

        doWantToQuite = false;
        setContentView(R.layout.activity_mainactivity);

        MainApplication.MainActivity = this;
        MainActivity.fm = this.getSupportFragmentManager();
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        MainApplication.setDisplayMetrics(dm);

        TextureRender.getInstance().setNeedLoadImageFromServer(!AppStatus.isNoImageModeOn());

        Properties prop = new UnifiedConfigurationOverride().loadConfig(MainApplication.MainActivity, false);
        if (prop == null) {
            prop = new UnifiedConfigurationOverride().loadConfig(MainApplication.MainActivity, true);
        }

        Environments.loadConfig(prop);

        AppStatus.readSelectedTabType();
        if (GlobalData.getInstance().getTabTypeSelected() != SelectedTabType.UnKnown) {
            selectTab(GlobalData.getInstance().getTabTypeSelected());

            ViewUtils.delayExecute(new Runnable() {
                @Override
                public void run() {
                    ViewUtils.createAppShortcut(self);
                }
            }, 5000);
        }
        else {
            navigateTo(ToplineFragment.class, false);
        }

        Utils.showConfigureNetwork(this);
        onNewIntent(this.getIntent());
    }

    @Override
    public void onResume() {
        super.onResume();
        DialogManager.getInstance().resetDialog();

        if (SystemUtil.isWiFiConnected(MainApplication.MainActivity)) {
            // auto check update
            //new SettingCheckUpdate().start(false);

            // auto Unified Configuration Override
            new UnifiedConfigurationOverride().start();

            // check recommend Apps
            String url = Environments.CDNURL + Environments.RecommendAppsName;
            //new SettingRecommendAppsFragment().DownloadRecommendApps(url);

            // check for new dev reply to user feedbacks
            // when user start app through JPush notification, don't popup
            // dialog to bring user to feedback page
            //UMeng.getInstance().checkForNewDevReply(!GlobalData.getInstance().getIsAppOpenedFromJPushIntent());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //CountManager.getInstance().save();
        //MobclickAgent.onPause(this);
        AppStatus.keepSelectedTabType();
        //MusicFloatView.getInstance(MainApplication.MainActivity).hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        //JPushInterface.activityStopped(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //JPushInterface.activityStarted(this);
    }

    public void selectTab(SelectedTabType type) {
        onTabSelected(type);
    }

    @Override
    public void onTabSelected(SelectedTabType type) {
        //UMengSA.clickTabAndDuration(type, false, false);
        GlobalData.getInstance().setTabTypeSelected(type);
        switch (type) {
            case TopLine:
                navigateTo(ToplineFragment.class, null, false);
                break;
            case Comment:
                navigateTo(CommentFragment.class, null, false);
                break;
            case Class:
                navigateTo(ClassFragment.class, null, false);
                break;
            case MyInfo:
            default:
                navigateTo(MyInfoFragment.class, null, false);
                break;
        }
    }

    public void navigateTo(Class<? extends BaseFragment> fragment, Bundle args, boolean isNeedToBack) {
        FragmentTransaction ft = fm.beginTransaction();
        int size = fm.getBackStackEntryCount();

        BaseFragment baseFragment = null;
        try {
            baseFragment = fragment.newInstance();
        } catch (java.lang.InstantiationException e) {
        } catch (IllegalAccessException e) {
        }

        if (args != null) {
            baseFragment.setArguments(args);
        }

        if (!isFirstLevlPage(fragment.getSimpleName())) {
            ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out, R.anim.fragment_popin, R.anim.fragment_popout);
        } else {
            ft.setTransition(FragmentTransaction.TRANSIT_NONE);
        }

        ft.add(R.id.content_container, baseFragment, fragment.getSimpleName());
        ft.addToBackStack(fragment.getSimpleName());

        ft.commitAllowingStateLoss();


        if (isFirstLevlPage(fragment)) {
            this.showTabBar();
        }

    }

    public void navigateTo(Class<? extends BaseFragment> fragment, boolean isNeedToBack) {
        navigateTo(fragment, null, isNeedToBack);
    }

    public void clearBackStackAndThenNavigateTo(Class<? extends BaseFragment> fragment) {
        int bsCount = fm.getBackStackEntryCount();
        for (int i = 0; i < bsCount; i++) {
            FragmentTransaction ft = fm.beginTransaction();
            fm.popBackStack();
            ft.commitAllowingStateLoss();
        }

        this.navigateTo(fragment, false);
    }

    public void hideTabBar() {
    }

    public void showTabBar() {
    }

    public void goBack() {
        if (fm == null) {
            return;
        }

        // tag navigation
        String topFragmentName = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName();
        fm.popBackStackImmediate();

    }

    public boolean handleBackPressedInTopFragment() {
        if (fm == null) {
            return false;
        }

        //TODO: if we support back on fragment
        return false;
    }

    @Override
    public void onBackPressed() {
        int size = fm.getBackStackEntryCount();

        if (this.handleBackPressedInTopFragment()) {
            return;
        }

        if (size == 1 || isOnlyFirstLevelPageLeft()) {
            if (doWantToQuite) {
                this.finish();
            } else {
                Toast.makeText(this, PressBackAgainToQuiteApplicationMessage, Toast.LENGTH_LONG).show();
                ViewUtils.delayExecute(new Runnable() {
                    @Override
                    public void run() {
                        doWantToQuite = false;
                    }
                }, 2000);
                doWantToQuite = true;
            }
        } else {
            super.onBackPressed();
        }
    }

    public String getFragmentNameInStack(int offsetIndex) {
        if (fm == null) {
            return null;
        }

        int bsCount = fm.getBackStackEntryCount();

        if (offsetIndex >= bsCount) {
            return null;
        }

        return fm.getBackStackEntryAt(bsCount - offsetIndex - 1).getName();
    }

    public void popupTopFragment() {
        if (fm == null) {
            return;
        }

        int bsCount = fm.getBackStackEntryCount();

        if (bsCount > 0) {
            fm.popBackStack();
        }
    }

    @Override
    public void onDestroy() {
        DialogManager.getInstance().dismissDialog();
        super.onDestroy();
    }

    private boolean isFirstLevlPage(String name) {
        if (name == null || name.length() == 0) {
            return false;
        }

        return name.equals(ToplineFragment.class.getSimpleName())
                || name.equals(MyInfoFragment.class.getSimpleName())
                || name.equals(CommentFragment.class.getSimpleName())
                || name.equals(ClassFragment.class.getSimpleName());
    }

    private boolean isFirstLevlPage(Class<? extends BaseFragment> fragment) {
        if (fragment == null) {
            return false;
        }

        return isFirstLevlPage(fragment.getSimpleName());
    }

    private boolean isOnlyFirstLevelPageLeft() {
        int size = fm.getBackStackEntryCount();
        for (int i = size - 1; i >= 0; i--) {
            if (!isFirstLevlPage(fm.getBackStackEntryAt(size - 1).getName())) {
                return false;
            }
        }

        return true;
    }

    private void hideFragments(FragmentManager fm, FragmentTransaction ft, int size) {
        for (int i = 0; i < size; i++) {
            String name = fm.getBackStackEntryAt(i).getName();
            if (TextUtils.isEmpty(name)) {
                continue;
            }

            BaseFragment stackFragment = (BaseFragment) fm.findFragmentByTag(name);
            if (stackFragment != null && !stackFragment.isHidden()) {
                ft.hide(stackFragment);
            }
        }
    }

}
