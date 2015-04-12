package com.metis.meishuquan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.metis.meishuquan.fragment.main.CircleFragment;
import com.metis.meishuquan.fragment.main.ClassFragment;
import com.metis.meishuquan.fragment.main.AssessFragment;
import com.metis.meishuquan.fragment.main.MyInfoFragment;
import com.metis.meishuquan.fragment.main.ToplineFragment;
import com.metis.meishuquan.framework.util.TextureRender;
import com.metis.meishuquan.model.assess.Bimp;
import com.metis.meishuquan.ui.SelectedTabType;
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
    private static final String PressBackAgainToQuiteApplicationMessage = "再按一次退出";
    private ViewGroup popupRoot;
    private boolean doWantToQuite;
    private int attachViewCount = 0;

    public static MainActivity self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        self = this;


        doWantToQuite = false;
        setContentView(R.layout.activity_mainactivity);
        popupRoot = (ViewGroup) this.findViewById(R.id.popup_attach);

        MainApplication.MainActivity = this;
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
        } else {
            navigateTo(ToplineFragment.class);
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
            //new UnifiedConfigurationOverride().start();

            // check recommend Apps
            //String url = Environments.CDNURL + Environments.RecommendAppsName;
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
        GlobalData.getInstance().setTabTypeSelected(type);
        switch (type) {
            case TopLine:
                navigateTo(ToplineFragment.class);
                break;
            case Comment:
                navigateTo(AssessFragment.class);
                break;
            case Class:
                navigateTo(ClassFragment.class);
                break;
            case MyInfo:
                navigateTo(MyInfoFragment.class);
                break;
            default:
                navigateTo(CircleFragment.class);
                break;
        }
    }

    public void navigateTo(Class<? extends Fragment> fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment baseFragment = null;
        try {
            baseFragment = fragment.newInstance();
        } catch (Exception e) {
        }

        if (!isFirstLevelPage(fragment.getSimpleName())) {
            ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out, R.anim.fragment_popin, R.anim.fragment_popout);
        } else {
            ft.setTransition(FragmentTransaction.TRANSIT_NONE);
        }

        ft.replace(R.id.content_container, baseFragment, fragment.getSimpleName());
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (attachViewCount > 0) {
            removeAllAttachedView();
            return;
        }

        int size = getSupportFragmentManager().getBackStackEntryCount();
        if (size == 0) {
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

    @Override
    public void onDestroy() {
        DialogManager.getInstance().dismissDialog();
        super.onDestroy();
    }

    private boolean isFirstLevelPage(String name) {
        if (name == null || name.length() == 0) {
            return false;
        }

        return name.equals(ToplineFragment.class.getSimpleName())
                || name.equals(MyInfoFragment.class.getSimpleName())
                || name.equals(AssessFragment.class.getSimpleName())
                || name.equals(CircleFragment.class.getSimpleName())
                || name.equals(ClassFragment.class.getSimpleName());
    }

    public void removeAllAttachedView()
    {
        if (popupRoot!=null)
        {
            popupRoot.removeAllViews();
            attachViewCount = 0;
        }
    }

    public void removeAttachedView(View view)
    {
        if (popupRoot!=null && view!=null)
        {
            popupRoot.removeView(view);
            attachViewCount --;
        }
    }

    public void addAttachView(View view)
    {
        if (popupRoot!=null && view!=null)
        {
            popupRoot.addView(view);
            attachViewCount ++;
        }
    }
}
