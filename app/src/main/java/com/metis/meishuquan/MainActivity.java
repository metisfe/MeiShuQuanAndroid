package com.metis.meishuquan;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.fragment.commons.FragmentWaitingForAssess;
import com.metis.meishuquan.fragment.commons.FragmentWaitingForClass;
import com.metis.meishuquan.fragment.main.AssessFragment;
import com.metis.meishuquan.fragment.main.CircleFragment;
import com.metis.meishuquan.fragment.main.ClassFragment;
import com.metis.meishuquan.fragment.main.MyInfoFragment;
import com.metis.meishuquan.fragment.main.ToplineFragment;
import com.metis.meishuquan.framework.util.TextureRender;
import com.metis.meishuquan.model.BLL.CommonOperator;
import com.metis.meishuquan.model.commons.AndroidVersion;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.push.MainPushService;
import com.metis.meishuquan.ui.SelectedTabType;
import com.metis.meishuquan.util.Environments;
import com.metis.meishuquan.util.GlobalData;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.util.SystemUtil;
import com.metis.meishuquan.util.UnifiedConfigurationOverride;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.util.ViewUtils;
import com.metis.meishuquan.view.DialogManager;
import com.metis.meishuquan.view.shared.TabBar;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import java.util.Date;
import java.util.Properties;

public class MainActivity extends FragmentActivity implements TabBar.TabSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String PressBackAgainToQuiteApplicationMessage = "再按一次退出";
    private ViewGroup popupRoot;
    private boolean doWantToQuite;
    private boolean isNormal;//检验账号是否有异常
    private int attachViewCount = 0;

    public static MainActivity self;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            navigateTo(CircleFragment.class);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent pushAgent = PushAgent.getInstance(this);
        pushAgent.setPushIntentServiceClass(MainPushService.class);
        pushAgent.enable();

        PushAgent.getInstance(this).onAppStart();

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
        updateApp(MainActivity.this);
        CommonOperator.getInstance().getMomentsGroupsToCache();//获取朋友圈分组信息

        IntentFilter intentFilter = new IntentFilter("join_succeed");
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);

        /*
        发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。

        启动时发送：APP启动时发送当次启动数据和上次的使用时长等缓存数据，当次使用过程中产生的自定义事件数据缓存在客户端，下次启动时发送
         */
        MobclickAgent.updateOnlineConfig(this);

        //SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
        AnalyticsConfig.enableEncrypt(true);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 1001 && resultCode == RESULT_OK) {
//            navigateTo(CircleFragment.class);
//        }
//    }


    @Override
    public void onResume() {
        super.onResume();
        DialogManager.getInstance().resetDialog();
        MobclickAgent.onResume(this);

        if (SystemUtil.isWiFiConnected(MainApplication.MainActivity)) {
            // auto check update
//            new SettingCheckUpdate().start(false);

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
        MobclickAgent.onPause(this);
        //CountManager.getInstance().save();
        //MobclickAgent.onPause(this);
        //AppStatus.keepSelectedTabType();
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
                navigateTo(FragmentWaitingForAssess.class);
//                navigateTo(AssessFragment.class);
                break;
            case Class:
                navigateTo(FragmentWaitingForClass.class);
//                navigateTo(ClassFragment.class);
                break;
            case MyInfo:
                navigateTo(MyInfoFragment.class);
                break;
            default:
                if (MainApplication.isLogin()) {
                    navigateTo(CircleFragment.class);
                } else {
                    startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), 1001);
                }
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
        ft.commitAllowingStateLoss();
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
                AppStatus.clear();
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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
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
                || name.equals(ClassFragment.class.getSimpleName())
                || name.equals(FragmentWaitingForClass.class.getSimpleName())
                || name.equals(FragmentWaitingForAssess.class.getSimpleName());
    }

    public void removeAllAttachedView() {
        if (popupRoot != null) {
            popupRoot.removeAllViews();
            attachViewCount = 0;
        }
    }

    public void removeAttachedView(View view) {
        if (popupRoot != null && view != null) {
            popupRoot.removeView(view);
            attachViewCount--;
        }
    }

    public void addAttachView(View view) {
        if (popupRoot != null && view != null) {
            popupRoot.addView(view);
            attachViewCount++;
        }
    }

    public void updateApp(final Context context) {
        //TODO:比较上次检测时间是否超过24小时
//        String json = SharedPreferencesUtil.getInstanse(MainApplication.UIContext).getStringByKey(SharedPreferencesUtil.LAST_APP_VERSION);
//        if (!json.isEmpty()) {
//            ReturnInfo info = new Gson().fromJson(json, new TypeToken<ReturnInfo<AndroidVersion>>() {
//            }.getType());
//            Date lastCheckTime = ((AndroidVersion) info.getData()).getLastCheckTime();
//            Date now = new Date();
//            long i = now.getTime() - lastCheckTime.getTime();
//            long day = i / (24 * 60 * 60 * 1000);
//            if (day < 1) {
//                return;
//            }
//        }

        CommonOperator.getInstance().getVersion(new ApiOperationCallback<ReturnInfo<AndroidVersion>>() {
            @Override
            public void onCompleted(ReturnInfo<AndroidVersion> result, Exception exception, ServiceFilterResponse response) {
                if (result != null && result.isSuccess()) {
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    ReturnInfo info = gson.fromJson(json, new TypeToken<ReturnInfo<AndroidVersion>>() {
                    }.getType());
                    ((AndroidVersion) info.getData()).setLastCheckTime(new Date());
                    SharedPreferencesUtil.getInstanse(self).update(SharedPreferencesUtil.LAST_APP_VERSION, gson.toJson(info));

                    final AndroidVersion androidVersion = (AndroidVersion) info.getData();
                    String curentVersion = "";
                    try {
                        PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
                        curentVersion = pinfo.versionName;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    Log.i("curentVersion", curentVersion);
                    if (androidVersion != null && !androidVersion.getVersionNumber().equals(curentVersion)) {
                        String msg = "现有可用更新,版本号为 V" + androidVersion.getVersionNumber() + "，是否下载更新？";
                        new AlertDialog.Builder(context)
                                .setTitle("提示")
                                .setMessage(msg)
                                .setPositiveButton("现在更新", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent();
                                        intent.setAction("android.intent.action.VIEW");
                                        Uri uri = Uri.parse(androidVersion.getDownUrl());
                                        intent.setData(uri);
                                        context.startActivity(intent);
                                    }
                                })
                                .setNegativeButton("以后", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .show();
                    }
                }
            }
        });
    }

    private static void checkLoginState(String session) {
        if (!MainApplication.isLogin()) {
            return;
        }
        CommonOperator.getInstance().checkLoginState(session, new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                if (result != null && result.isSuccess()) {
                } else {
                    MainApplication.userInfo = new User();
                    Toast.makeText(MainApplication.UIContext, "账号异常！请重新登录，建议您修改密码", Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(MainApplication.UIContext, LoginActivity.class));
                }
            }
        });
    }
}
