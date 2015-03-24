package com.metis.meishuquan.model.BLL;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.util.SystemUtil;
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
     * 将频道集合数据添加至sharedPreferences中
     */
    public void addChannelItemsToLoacal() {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {//判断网络状态
            //加载网络数据
            if (flag) {
                ApiDataProvider.getmClient().invokeApi("v1.1/Channel/ChannelList?userId=1&type=1", null,
                        HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(),
                        new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                if (result.getInfo().equals(String.valueOf(0))) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(result);
                                    SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
                                    spu.delete(SharedPreferencesUtil.CHANNELS);
                                    spu.add(SharedPreferencesUtil.CHANNELS, json);//添加至缓存中
                                } else {
                                    Log.e("meishuquan", "网络状态码不为0");
                                }
                            }
                        });
            } else {
                Toast.makeText(MainApplication.UIContext, "网络异常", Toast.LENGTH_LONG).show();
            }

        }
    }


    /**
     * 根据频道Id获取新闻列表
     *
     * @param callBack
     * @param channelId
     * @param lastNewsId 等于0时，获取最新数据;设为newId时，用于加载数据（分页）
     */
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

    //更新服务器数据
}
