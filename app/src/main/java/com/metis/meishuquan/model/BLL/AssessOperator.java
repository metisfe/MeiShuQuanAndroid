package com.metis.meishuquan.model.BLL;

import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.assess.AssessComment;
import com.metis.meishuquan.model.assess.AssessSupportAndComment;
import com.metis.meishuquan.model.assess.PushCommentParam;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.AssessStateEnum;
import com.metis.meishuquan.model.enums.FileUploadTypeEnum;
import com.metis.meishuquan.model.enums.QueryTypeEnum;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.util.SystemUtil;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务逻辑类：点评
 * <p/>
 * Created by wangjin on 15/3/20.
 */
public class AssessOperator {
    private boolean flag;
    private static AssessOperator operator = null;

    private final String AssessList = "v1.1/Assess/AssessList";//点评列表
    private final String AssessChannelList = "v1.1/Channel/AssessChannelList";//所有标签
    private final String UploadAssess = "v1.1/Assess/UploadAssess";//上传作品
    private final String Assess_Comment_Share = "v1.1/Assess/Share";//作品分享
    private final String Comment_Favorite = "v1.1/Comment/Favorite";//收藏(暂无此功能)
    private final String AttentionUser = "";//关注用户(暂无此功能)
    private final String PushComment = "v1.1/AssessComment/PushComment?";//发表评论
    private final String Friend = "";//获取好友/老师
    private final String FileUpload = "v1.1/File/Upload";//文件上传
    private final String Region = "v1.1/UserCenter/Region";
    private final String AssessSupportAndComment = "v1.1/AssessComment/AssessCommentAndSupportUserList";
    private final String SESSION = MainApplication.userInfo.getCookie();

    private AssessOperator() {
        flag = ApiDataProvider.initProvider();
    }

    public static AssessOperator getInstance() {
        if (operator == null) {
            operator = new AssessOperator();
        }
        return operator;
    }

    /**
     * 根据条件获得点评列表
     *
     * @param isAll      全部 isAll为True时，其他条件失效
     * @param type       类型 type 1最新 2已评价 3未评价
     * @param grades     年级
     * @param channelIds 标签
     * @param index      index=1取最新数据，排序先按照默认时间排序
     * @param queryType  0为全部，1为热点，2为最新
     * @param region     0为全国
     * @param callback
     */
    public void getAssessList(int region, boolean isAll, AssessStateEnum type, List<Integer> grades, List<Integer> channelIds, int index, QueryTypeEnum queryType,
                              ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder PATH = new StringBuilder(AssessList);
                PATH.append("?isAll=" + isAll);
                PATH.append("&type=" + type.getVal());
                PATH.append("&grades=" + grades);
                PATH.append("&channelIds=" + channelIds);
                PATH.append("&index=" + index);
                PATH.append("&queryType=" + queryType.getVal());
                PATH.append("&region=" + region);
                PATH.append("&session=" + SESSION);
                Log.i("assesslisturl", PATH.toString());
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        }
    }

    /**
     * 根据条件获得点评列表
     *
     * @param isAll      全部 isAll为True时，其他条件失效
     * @param type       类型 type 1最新 2已评价 3未评价
     * @param grades     年级
     * @param channelIds 标签
     * @param index      index=1取最新数据，排序先按照默认时间排序
     */
    public void addAssessListToCache(boolean isAll, int type, List<Integer> grades, List<Integer> channelIds, int index, int queryType) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder PATH = new StringBuilder(AssessList);
                PATH.append("?isAll=" + true);
                PATH.append("&type=" + 1);
                PATH.append("&grades=" + 0);
                PATH.append("&channelIds=" + 0);
                PATH.append("&index=" + index);
                PATH.append("&queryType=" + queryType);
                PATH.append("&session=" + SESSION);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(result);
                                    SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
                                    spu.delete(SharedPreferencesUtil.ALL_ASSESS_LIST);
                                    spu.add(SharedPreferencesUtil.ALL_ASSESS_LIST, json);
                                }
                            }
                        });
            }
        }
    }

    /**
     * 获取标签及年级
     */
    public void addAssessChannelListToCache() {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder PATH = new StringBuilder(AssessChannelList);
                PATH.append("?&session=" + SESSION);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(result);
                                    //将json串添加至缓存
                                    SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
                                    spu.update(SharedPreferencesUtil.ASSESS_LIST_FILTER_DATA, json);
                                }
                            }
                        });
            }
        }
    }

    /**
     * 发表提问
     *
     * @param userId
     * @param desc
     * @param channelId
     * @param friendUserId
     * @param callback
     */
    public void uploadAssess(int userId, String desc, int channelId, int friendUserId, String fileStr, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                //获得文件上传后服务器发送来的Json串，再上传Assess
                StringBuilder PATH = new StringBuilder(UploadAssess);
                PATH.append("?userId=" + userId);
                PATH.append("&desc=" + desc);
                PATH.append("&channelId=" + channelId);
                PATH.append("&friendUserId=" + friendUserId);
                PATH.append("&file=" + fileStr);
                PATH.append("&session=" + SESSION);

                List<Pair<String, String>> pram = new ArrayList<>();
                Pair<String, String> pair1 = new Pair<>("userId", String.valueOf(userId));
                Pair<String, String> pair2 = new Pair<>("desc", desc);
                Pair<String, String> pair3 = new Pair<>("channelId", String.valueOf(channelId));
                Pair<String, String> pair4 = new Pair<>("friendUserId", String.valueOf(friendUserId));
                Pair<String, String> pair5 = new Pair<>("file", fileStr);
                Pair<String, String> pair6 = new Pair<>("session", MainApplication.userInfo.getCookie());
                Log.e("session", MainApplication.userInfo.getCookie());
                pram.add(pair1);
                pram.add(pair2);
                pram.add(pair3);
                pram.add(pair4);
                pram.add(pair5);
                pram.add(pair6);
                ApiDataProvider.getmClient().invokeApi(UploadAssess, null, HttpPost.METHOD_NAME, pram,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }

        }
    }

    /**
     * 上传文件
     *
     * @param type     上传文件类型
     * @param define   文件格式定义
     * @param imgByte  文件的byte[]
     * @param callback
     */
    public void fileUpload(FileUploadTypeEnum type, String define, byte[] imgByte, ServiceFilterResponseCallback callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder FILEUPLOAD = new StringBuilder(FileUpload);
                FILEUPLOAD.append("?type=" + type.getVal());
                FILEUPLOAD.append("&define=" + define);
                FILEUPLOAD.append("&session=" + SESSION);
                List<Pair<String, String>> pram = new ArrayList<>();
                Pair<String, String> pair1 = new Pair<String, String>("type", String.valueOf(type));
                Pair<String, String> pair2 = new Pair<String, String>("define", define);
                pram.add(pair1);
                pram.add(pair2);
                ApiDataProvider.getmClient().invokeApi(FILEUPLOAD.toString(), imgByte, HttpPost.METHOD_NAME, null, pram, callback);
            }
        }
    }

    /**
     * 作品分享/点评分享
     *
     * @param id       作品Id或评论Id
     * @param type     type:0作品分享，1：评论分享
     * @param callback
     */
    public void shareAssess(int id, int type, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder PATH = new StringBuilder(Assess_Comment_Share);
                PATH.append("?id" + id);
                PATH.append("&type" + type);
                PATH.append("&session=" + SESSION);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        }
    }

    public void getSupportAndComment(int assessId, ApiOperationCallback<ReturnInfo<AssessSupportAndComment>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder PATH = new StringBuilder(AssessSupportAndComment);
                PATH.append("?assessId=" + assessId);
                PATH.append("&session=" + SESSION);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<AssessSupportAndComment>>) new ReturnInfo<AssessSupportAndComment>().getClass(), callback);
            }
        }
    }

    public void pushComment(PushCommentParam param, ApiOperationCallback<ReturnInfo<AssessComment>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder PATH = new StringBuilder(PushComment);
                PATH.append("&session=" + SESSION);
                List<Pair<String, String>> pram = new ArrayList<>();
                Gson gson = new Gson();
                String json = gson.toJson(param);
                Pair<String, String> pair1 = new Pair<String, String>("param", json);
                Pair<String, String> pair2 = new Pair<String, String>("session", SESSION);
                pram.add(pair1);
                pram.add(pair2);
                ApiDataProvider.getmClient().invokeApi(PushComment, null, HttpPost.METHOD_NAME, pram, (Class<ReturnInfo<AssessComment>>) new ReturnInfo<AssessComment>().getClass(), callback);
            }
        }
    }

    /**
     * 获取地区
     */
    public void AddRegionToCache() {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder PATH = new StringBuilder(Region);
                PATH.append("?session=" + SESSION);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(result);
                                    //缓存至本地
                                    if (!json.isEmpty()) {
                                        SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
                                        spu.add(SharedPreferencesUtil.REGION, json);
                                    }
                                }
                            }
                        });
            }
        }
    }
}
