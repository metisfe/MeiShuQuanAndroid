package com.metis.meishuquan;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.model.BLL.CommonOperator;
import com.metis.meishuquan.model.commons.AndroidVersion;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.IdTypeEnum;
import com.metis.meishuquan.model.enums.LoginStateEnum;
import com.metis.meishuquan.model.login.LoginUserData;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.model.provider.DataProvider;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.util.GlobalData;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.util.Utils;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by wudi on 3/15/2015.
 */
public class MainApplication extends Application {

    private static DisplayMetrics displayMetrics;
    public static android.content.res.Resources Resources;
    public static String PackageName;
    public static MainActivity MainActivity;
    public static android.content.res.AssetManager AssetManager;

    public static Thread UIThread;
    public static Handler Handler;
    public static Context UIContext;
    public static RongIMClient rongClient;
    public static RongIM rongIM;
    public static User userInfo;//登录或注册成功后会更新此对象，默认对象中有登录状态及默认的UserId

    @Override
    public void onCreate() {
        super.onCreate();

        //Thread.setDefaultUncaughtExceptionHandler(CrashHandler.getInstance(this));

        //set UMeng debugable false
        com.umeng.socialize.utils.Log.LOG = false;

        Resources = this.getResources();
        PackageName = this.getPackageName();
        AssetManager = this.getAssets();
        UIThread = Thread.currentThread();
        UIContext = this.getApplicationContext();
        Handler = new Handler();
        userInfo = getUserInfoFromSharedPreferences();
        if (userInfo.getUserRole() == IdTypeEnum.TEACHER.getVal() || userInfo.getUserRole() == IdTypeEnum.STUDIO.getVal()) {
            GlobalData.tabs.add(1);
            GlobalData.tabs.add(3);
        } else {
            GlobalData.tabs.clear();
        }

        DataProvider.setDefaultUIThreadHandler(Handler);
        ApiDataProvider.initProvider();
        RongIM.init(this);
        ChatManager.userRongId = userInfo.getRongCloudId();
        rongConnect(userInfo.getToken());
    }

    /**
     * 连接融云服务器
     */
    public static void rongConnect(String token) {
        if (TextUtils.isEmpty(token) || token.length() < 50) return;
        try {
            rongIM = RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onSuccess(String s) {
                    if (MainApplication.rongIM != null) {
                        MainApplication.rongIM.setReceiveMessageListener(new RongIM.OnReceiveMessageListener() {
                            @Override
                            public void onReceived(RongIMClient.Message message, int i) {
                                ChatManager.onReceive(message);
                            }
                        });
                    }
                }

                @Override
                public void onError(ErrorCode errorCode) {
                    Log.e("rongConnect", errorCode.toString());
                    MainApplication.rongClient = null;
                    MainApplication.rongIM = null;
                    ChatManager.userRongId = "";
                }
            });
            rongClient = rongIM.getRongIMClient();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ChatManager.refreshFriendData();
        ChatManager.getMyWatchGroupFromApi(new ChatManager.OnGroupListReceivedListener() {
            @Override
            public void onReceive(List<String> ids) {
                if (ids != null) {
                    ChatManager.setMyWatchGroup(ids);
                }
            }
        });
    }

    public static void setDisplayMetrics(DisplayMetrics dm) {
        displayMetrics = dm;
    }

    public static DisplayMetrics getDisplayMetrics() {
        return displayMetrics;
    }

    private User getUserInfoFromSharedPreferences() {
        String json = SharedPreferencesUtil.getInstanse(UIContext).getStringByKey(SharedPreferencesUtil.USER_LOGIN_INFO);
        if (json.isEmpty()) {
            return new User();
        }
        LoginUserData user = new Gson().fromJson(json, new TypeToken<LoginUserData>() {
        }.getType());
        return user.getData();
    }

    public static boolean isLogin() {
        if (userInfo.getAppLoginState() == LoginStateEnum.NO) {
            return false;
        }
//        else if (userInfo.getAppLoginState() == LoginStateEnum.YES) {
//            return checkLoginState();
//        }
        return true;
    }

    public static String getSession() {
        return userInfo.getCookie();
    }


    private static boolean isNormal;//账号是否正常

    private static boolean checkLoginState() {
        CommonOperator.getInstance().checkLoginState(new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                if (result != null && result.isSuccess()) {
                    isNormal = true;
                } else {
                    SharedPreferencesUtil.getInstanse(UIContext).delete(SharedPreferencesUtil.USER_LOGIN_INFO);
                    MainApplication.userInfo = new User();
                    Toast.makeText(MainApplication.UIContext, "账号异常！请重新登录！", Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(UIContext, LoginActivity.class));
                    isNormal = false;
                }
            }
        });
        return isNormal;
    }
}
