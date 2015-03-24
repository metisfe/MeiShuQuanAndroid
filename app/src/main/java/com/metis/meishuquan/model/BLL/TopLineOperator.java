package com.metis.meishuquan.model.BLL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;

/**
 * 业务逻辑类：头条
 * <p/>
 * Created by wj on 15/3/20.
 */
public class TopLineOperator {
    private boolean flag;
    private static TopLineOperator operator = null;

    private final String CHANNELLIST_URL = "v1.1/Channel/ChannelList?userId=1&type=1";
    private final String CHANNEL_INFO_URL = "v1.1/News/NewsList?";

    private TopLineOperator() {
        flag = ApiDataProvider.initProvider();
    }

    public static TopLineOperator getInstance() {
        if (operator == null) {
            operator = new TopLineOperator();
        }
        return operator;
    }

    /**
     * 根据网络状态获取TopBar的频道数据（有网络时，从网络上获取；无网络时，从本地缓存中获取；缓存中无数据，加载默认数据）
     */
    public void getChannelItems(ApiOperationCallback<ReturnInfo<String>> callBack) {
        if (flag) {
            ApiDataProvider.getmClient().invokeApi(CHANNELLIST_URL, null, HttpGet.METHOD_NAME, null,
                    (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callBack);
        }
    }


    public void getNewsListByChannelId(ApiOperationCallback<ReturnInfo<String>> callBack, int channelId, int lastNewsId) {
        if (flag) {
            StringBuffer PATH = new StringBuffer(CHANNEL_INFO_URL);
            PATH.append("ChanelId=" + channelId);
            PATH.append("&");
            PATH.append("lastNewsId=" + lastNewsId);
            ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
                    (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callBack);
        }
    }

    /**
     * 判断是否有网络
     *
     * @param context 上下文
     * @return
     */
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     *
     * @param context 上下文
     * @return
     */
    public boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     *
     * @param context
     * @return
     */
    public boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    //更新服务器数据
}
