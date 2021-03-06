package com.metis.meishuquan.model.BLL;

import android.util.Log;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.circle.CCircleDetailModel;
import com.metis.meishuquan.model.circle.CParamCircleComment;
import com.metis.meishuquan.model.circle.CRelatedCircleModel;
import com.metis.meishuquan.model.circle.CircleMoments;
import com.metis.meishuquan.model.circle.CirclePushBlogParm;
import com.metis.meishuquan.model.commons.FocusOrFollower;
import com.metis.meishuquan.model.commons.Result;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.MessageTypeEnum;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.SystemUtil;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.util.List;

/**
 * Created by wangjin on 15/4/13.
 */
public class CircleOperator {

    private static final String TAG = CircleOperator.class.getSimpleName();

    private static final String Log_PubLishComment_url = "Log_PubLishComment_url";
    private static final String URL_PUSHBLOG = "v1.1/Circle/PushBlog?";
    private static final String URL_ATTENTION = "v1.1/Circle/FocusUserForGroup?";
    private static final String URL_CANCEL_ATTENTION = "v1.1/Circle/CancelAttention?";
    private static final String URL_DELETE_CIRCLE = "v1.1/Circle/DeleteMyCircle?circleid=";
    private static final String URL_CIRCLE_DETAIL = "v1.1/Circle/CircleDetail?";
    private static final String URL_CIRCLE_PUSH_COMMENT = "v1.1/Circle/PushCommentByPost?";
    private static final String URL_CIRCLE_AT_ME = "v1.1/Message/AndIRelated?";
    private static final String URL_GET_FOCUS_LIST = "v1.1/Circle/GetFocusUserList";
    private static final String URL_GET_DEFAULT_CHATROOM_LIST = "v1.1/Message/GetDefaultDiscussion";
    private static final String URL_DELETE_COMMENT = "v1.1/Circle/DeleteMyCommnet?";

    private static CircleOperator operator = null;
    private boolean flag;

    private CircleOperator() {
        flag = ApiDataProvider.initProvider();
    }

    public static CircleOperator getInstance() {
        if (operator == null) {
            operator = new CircleOperator();
        }
        return operator;
    }

    /**
     * 发微博
     *
     * @param param
     * @param callback
     */
    public void pushBlog(CirclePushBlogParm param, ApiOperationCallback<ReturnInfo<CCircleDetailModel>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuffer PATH = new StringBuffer(URL_PUSHBLOG);
                PATH.append("session=" + MainApplication.userInfo.getCookie());
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), param, HttpPost.METHOD_NAME, null,
                        (Class<ReturnInfo<CCircleDetailModel>>) new ReturnInfo<CCircleDetailModel>().getClass(), callback);
            }
        } else {
            Toast.makeText(MainApplication.UIContext, "网络不给力，请稍候再试", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 圈子评论
     *
     * @param param    内容
     * @param callback 回调
     */
    public void pushCommentByPost(CParamCircleComment param, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuffer PATH = new StringBuffer(URL_CIRCLE_PUSH_COMMENT);
                PATH.append("session=" + MainApplication.userInfo.getCookie());
                Log.i("URL_CIRCLE_PUSH_COMMENT", PATH.toString());
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), param, HttpPost.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        } else {
            Toast.makeText(MainApplication.UIContext, "网络不给力，请稍候再试", Toast.LENGTH_SHORT).show();
        }
    }

    /*?userid={userid}&lastid={lastid}&foucetype={foucetype}*/
    public void getFocusList(long userId, long lastId, int type, final UserInfoOperator.OnGetListener<List<FocusOrFollower>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuffer PATH = new StringBuffer(URL_GET_FOCUS_LIST);
                PATH.append("?userid=" + userId);
                PATH.append("&lastid=" + lastId);
                PATH.append("&focustype=" + type);
                PATH.append("&session=" + MainApplication.userInfo.getCookie());
                Log.v(TAG, "getFocusList request=" + PATH);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<Result<String>>) new Result().getClass(), new ApiOperationCallback<Result<String>>() {
                            @Override
                            public void onCompleted(Result<String> stringResult, Exception e1, ServiceFilterResponse serviceFilterResponse) {
                                Log.v(TAG, "getFocusList callback=" + serviceFilterResponse.getContent());
                                if (stringResult != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(stringResult);
                                    Log.v(TAG, "getFocusList result=" + json);
                                    Result<List<FocusOrFollower>> result = gson.fromJson(json, new TypeToken<Result<List<FocusOrFollower>>>() {
                                    }.getType());
                                    if (listener != null) {
                                        listener.onGet(result.getOption().getStatus() == 0, result.getData());
                                    }
                                }
                            }
                        });
            }
        } else {
            Toast.makeText(MainApplication.UIContext, "网络不给力，请稍候再试", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteCircle(int circleId, ApiOperationCallback<Result<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuffer PATH = new StringBuffer(URL_DELETE_CIRCLE);
                PATH.append(circleId);
                PATH.append("&session=" + MainApplication.userInfo.getCookie());
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<Result<String>>) new Result().getClass(), callback);
            }
        } else {
            Toast.makeText(MainApplication.UIContext, "网络不给力，请稍候再试", Toast.LENGTH_SHORT).show();
        }
    }

    public void attention(int userId, int groupId, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuffer PATH = new StringBuffer(URL_ATTENTION);
                PATH.append("userId=" + userId);
                PATH.append("&groupId=" + groupId);
                PATH.append("&session=" + MainApplication.userInfo.getCookie());
                Log.i("attention_url", PATH.toString());
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        } else {
            Toast.makeText(MainApplication.UIContext, "网络不给力，请稍候再试", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelAttention(long userId, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuffer PATH = new StringBuffer(URL_CANCEL_ATTENTION);
                PATH.append("userId=" + userId);
                PATH.append("&session=" + MainApplication.userInfo.getCookie());
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        } else {
            Toast.makeText(MainApplication.UIContext, "网络不给力，请稍候再试", Toast.LENGTH_SHORT).show();
        }
    }

    //获取朋友圈详情
    public void getMomentDetail(int id, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuffer PATH = new StringBuffer(URL_CIRCLE_DETAIL);
                PATH.append("id=" + id);
                PATH.append("&session=" + MainApplication.userInfo.getCookie());
                Log.i("moment_detail", PATH.toString());
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        } else {
            Toast.makeText(MainApplication.UIContext, "网络不给力，请稍候再试", Toast.LENGTH_SHORT).show();
        }
    }


    public void getAtMeData(int lastId, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuffer PATH = new StringBuffer(URL_CIRCLE_AT_ME);
                PATH.append("type=" + MessageTypeEnum.ATME.getVal());
                PATH.append("&lastId=" + lastId);
                PATH.append("&session=" + MainApplication.userInfo.getCookie());
                Log.i("moment_detail", PATH.toString());
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        } else {
            Toast.makeText(MainApplication.UIContext, "网络不给力，请稍候再试", Toast.LENGTH_SHORT).show();
        }
    }

    public void getCommentMeData(int lastId, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuffer PATH = new StringBuffer(URL_CIRCLE_AT_ME);
                PATH.append("type=" + MessageTypeEnum.COMMENTME.getVal());
                PATH.append("&lastId=" + lastId);
                PATH.append("&session=" + MainApplication.userInfo.getCookie());
                Log.i("moment_detail", PATH.toString());
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        } else {
            Toast.makeText(MainApplication.UIContext, "网络不给力，请稍候再试", Toast.LENGTH_SHORT).show();
        }
    }

    public void getDefaultChatRomm(ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuffer PATH = new StringBuffer(URL_GET_DEFAULT_CHATROOM_LIST);
                PATH.append("?session=" + MainApplication.userInfo.getCookie());
                Log.i("default_chatroom", PATH.toString());
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        } else {
            Toast.makeText(MainApplication.UIContext, "网络不给力，请稍候再试", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteComment(int circleid, int commnetid, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuffer PATH = new StringBuffer(URL_DELETE_COMMENT);
                PATH.append("circleid=" + circleid);
                PATH.append("&commnetid=" + commnetid);
                PATH.append("&session=" + MainApplication.userInfo.getCookie());
                Log.i("url_deleteComment", PATH.toString());
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpPost.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        } else {
            Toast.makeText(MainApplication.UIContext, "网络不给力，请稍候再试", Toast.LENGTH_SHORT).show();
        }
    }
}
