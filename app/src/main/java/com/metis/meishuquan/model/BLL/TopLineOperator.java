package com.metis.meishuquan.model.BLL;

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
    private final String CHANNELLIST_URL = "v1.1/Channel/ChannelList?userId=1&type=1";

    public TopLineOperator() {
    }

    /**
     * 根据网络状态获取TopBar的频道数据（有网络时，从网络上获取；无网络时，从本地缓存中获取；缓存中无数据，加载默认数据）
     */
    public void getChannelItems(ApiOperationCallback<ReturnInfo<String>> callBack) {
        final boolean flag = ApiDataProvider.initProvider();
        if (flag) {
            ApiDataProvider.getmClient().invokeApi(CHANNELLIST_URL, null, HttpGet.METHOD_NAME, null,
                    (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callBack);
        }
    }

    //更新服务器数据
}
