package com.metis.meishuquan.model.BLL;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.contract.ReturnInfo;
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

    private final String CHANNELLIST_URL = "v1.1/Channel/ChannelList?userId=1&type=0";//根据用户Id和模块类型获得频道集合
    private final String CHANNEL_NEW_LIST_URL = "v1.1/News/NewsList";//根据频道获取news列表
    private final String NEWS_INFO_URL = "v1.1/News/NewsDetail";//根据newsId获得详情
    private final String COMMENT_LIST_NEWSID = "v1.1/Comment/CommentList";//根据newsId获得评论列表
    private final String COMMENT_SUPPORT = "v1.1/Comment/Support";//赞/踩
    private final String PUBLISHCOMMENT = "v1.1/Comment/PublishComment";//发表评论
    private final String FAVORITE = "v1.1/Comment/Favorite";//收藏


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
                ApiDataProvider.getmClient().invokeApi(CHANNELLIST_URL, null,
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
                                    Log.e("addChannelItemsToLoacal", "网络状态码不为0,头条频道缓存失败");
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
                PATH.append("?ChanelId=" + channelId);
                PATH.append("&");
                PATH.append("lastNewsId=" + lastNewsId);
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
            PATH.append("?ChanelId=" + channelId);
            PATH.append("&");
            PATH.append("lastNewsId=" + lastNewsId);
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
                StringBuffer path = new StringBuffer(NEWS_INFO_URL);
                path.append("?newsId=" + newsId);
                ApiDataProvider.getmClient().invokeApi(path.toString(), null, HttpGet.METHOD_NAME, null,
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
                StringBuffer path = new StringBuffer(COMMENT_LIST_NEWSID);
                path.append("?type=" + type);
                path.append("&newsId=" + newsId);
                path.append("&lastCommentId=" + lastCommentId);
                ApiDataProvider.getmClient().invokeApi(path.toString(), null, HttpGet.METHOD_NAME, null,
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
                StringBuffer path = new StringBuffer(COMMENT_SUPPORT);
                path.append("?userid=" + userid);
                path.append("&id=" + newsid);
                path.append("&commentid=" + commentid);
                path.append("&type=" + type);
                path.append("&result=" + result);
                ApiDataProvider.getmClient().invokeApi(path.toString(), null, HttpGet.METHOD_NAME, null,
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

    /**
     * 收藏news
     *
     * @param userid
     * @param newsId
     * @param type
     * @param callback
     */
    public void newsPrivate(int userid, int newsId, int type, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuffer path = new StringBuffer(FAVORITE);
                path.append("?userid=" + userid);
                path.append("&id=" + newsId);
                path.append("&type=" + type);
                ApiDataProvider.getmClient().invokeApi(path.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        }
    }

    /**
     * 发表评论
     *
     * @param userid
     * @param newsId
     * @param replyCid  不是子评论时默认为0
     * @param blockType 0头条，1点评，2课程
     * @param callback
     */
    public void publishComment(int userid, int newsId, String content, int replyCid, int blockType, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuffer path = new StringBuffer(PUBLISHCOMMENT);
                path.append("?userid=" + userid);
                path.append("&newsid=" + newsId);
                path.append("&content=" + content);
                path.append("&replyCid=" + replyCid);
                path.append("&blockType=" + blockType);
                ApiDataProvider.getmClient().invokeApi(path.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        }
    }


}
