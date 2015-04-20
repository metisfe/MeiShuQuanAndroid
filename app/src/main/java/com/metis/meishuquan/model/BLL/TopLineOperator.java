package com.metis.meishuquan.model.BLL;

import android.graphics.Path;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.PrivateResultEnum;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.util.SystemUtil;
import com.metis.meishuquan.util.Utils;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 业务逻辑类：头条
 * <p/>
 * Created by wj on 15/3/20.
 */
public class TopLineOperator {
    private boolean flag;
    private static TopLineOperator operator = null;

    private final String CHANNELLIST_URL = "v1.1/Channel/ChannelList?";//根据用户Id和模块类型获得频道集合
    private final String CHANNEL_NEW_LIST_URL = "v1.1/News/NewsList?";//根据频道获取news列表
    private final String NEWS_INFO_URL = "v1.1/News/NewsDetail?";//根据newsId获得详情
    private final String COMMENT_LIST_NEWSID = "v1.1/Comment/CommentList?";//根据newsId获得评论列表
    private final String COMMENT_SUPPORT = "v1.1/Comment/Support?";//赞/踩

    private final String FAVORITE = "v1.1/Comment/Favorite?";//收藏
    private final String SESSION = MainApplication.userInfo.getCookie();


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
    public void addChannelItemsToLoacal(String userId, int type) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {//判断网络状态
            //加载网络数据
            if (flag) {
                StringBuilder PATH = new StringBuilder(CHANNELLIST_URL);
                PATH.append("userId=" + userId);
                PATH.append("&type=" + type);
                PATH.append("&session=" + SESSION);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null,
                        HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(),
                        new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(result);
                                    try {
                                        JSONObject jsonObject = new JSONObject(json);
                                        JSONObject chennels = jsonObject.getJSONObject("data");
                                        String data = chennels.toString();
                                        SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
                                        String val = spu.getStringByKey(SharedPreferencesUtil.CHANNELS);
                                        if (val.length() > 0) {
                                            spu.delete(SharedPreferencesUtil.CHANNELS);
                                        }
                                        Log.i("data", data);
                                        spu.add(SharedPreferencesUtil.CHANNELS, data);//添加至缓存中
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Log.e("ChannelItems", "result为空");
                                }
                            }
                        });
            } else {
                Toast.makeText(MainApplication.UIContext, "网络异常", Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * 根据频道Id获取新闻列表加入到缓存中
     *
     * @param channelId
     * @param lastNewsId 等于0时，获取最新数据;设为newsId时，用于加载数据（分页）
     */
    public void addNewsListByChannelIdToCache(final int channelId, int lastNewsId) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuffer PATH = new StringBuffer(CHANNEL_NEW_LIST_URL);
                PATH.append("ChanelId=" + channelId);
                PATH.append("&lastNewsId=" + lastNewsId);
                PATH.append("&session=" + SESSION);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                            @Override
                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                //添加至缓存
                                if (result != null) {
                                    if (result.getInfo().equals(String.valueOf(0))) {
                                        Gson gson = new Gson();
                                        String json = gson.toJson(result);
                                        try {
                                            SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
                                            String val = spu.getStringByKey(String.valueOf(channelId));
                                            if (val.length() > 0) {
                                                spu.delete(String.valueOf(channelId));
                                            }
                                            Log.i("channelId", json);
                                            spu.add(String.valueOf(channelId), json);//添加至缓存中
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Log.e("addNewsListToCache", "网络状态码不为0");
                                    }
                                }
                            }
                        });
            }
        }
    }

    /**
     * 根据频道Id获取新闻列表
     *
     * @param callback
     * @param channelId
     * @param lastNewsId
     */
    public void getNewsListByChannelId(ApiOperationCallback<ReturnInfo<String>> callback, int channelId, int lastNewsId) {
        if (flag) {
            StringBuffer PATH = new StringBuffer(CHANNEL_NEW_LIST_URL);
            PATH.append("ChanelId=" + channelId);
            PATH.append("&lastNewsId=" + lastNewsId);
            PATH.append("&session=" + SESSION);
            ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
                    (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
        }
    }

    /**
     * 根据newsId获得newsInfo
     *
     * @param newsId
     * @param callback
     */
    public void getNewsInfoById(int newsId, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuffer PATH = new StringBuffer(NEWS_INFO_URL);
                PATH.append("newsId=" + newsId);
                PATH.append("&session=" + SESSION);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        }
    }


    /**
     * 根据NewsId获得评论列表数据
     *
     * @param type          模块
     * @param newsId
     * @param lastCommentId
     * @param callback
     */
    public void getCommentListByNewId(int type, int newsId, int lastCommentId, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuffer PATH = new StringBuffer(COMMENT_LIST_NEWSID);
                PATH.append("type=" + type);
                PATH.append("&newsId=" + newsId);
                PATH.append("&lastCommentId=" + lastCommentId);
                PATH.append("&session=" + SESSION);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        }
    }

    /**
     * news 赞/踩
     *
     * @param userid
     * @param newsid
     * @param commentid
     * @param type
     * @param result    1赞 2踩
     */
    public void commentSurpot(int userid, int newsid, int commentid, int type, int result) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuffer PATH = new StringBuffer(COMMENT_SUPPORT);
                PATH.append("userid=" + userid);
                PATH.append("&newsid=" + newsid);
                PATH.append("&commentid=" + commentid);
                PATH.append("&type=" + type);
                PATH.append("&result=" + result);
                PATH.append("&session=" + SESSION);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                    Utils.alertMessageDialog("提示", "点赞成功");
                                }
                            }
                        });
            }
        }
    }

//    /**
//     * 收藏news
//     *
//     * @param userid
//     * @param id
//     * @param type
//     * @param result   1收藏，2取消
//     * @param callback
//     */
//    public void newsPrivate(int userid, int id, int type, PrivateResultEnum result, ApiOperationCallback<ReturnInfo<String>> callback) {
//        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
//            if (flag) {
//                StringBuffer PATH = new StringBuffer(FAVORITE);
//                PATH.append("userid=" + userid);
//                PATH.append("&id=" + id);
//                PATH.append("&type=" + type);
//                PATH.append("&result=" + result.getVal());
//                PATH.append("&session=" + SESSION);
//                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
//                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
//            }
//        }
//    }
}
