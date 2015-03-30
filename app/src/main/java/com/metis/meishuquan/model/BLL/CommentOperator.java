package com.metis.meishuquan.model.BLL;

import android.graphics.Bitmap;
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
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 业务逻辑类：点评
 *
 * Created by wj on 15/3/20.
 */
public class CommentOperator {
    private boolean flag;
    private static CommentOperator operator = null;

    private final String AssessList = "v1.1/Assess/AssessList";//点评列表
    private final String AssessChannelList = "v1.1/Channel/ChannelList";//所有标签
    private final String UploadAssess = "";//上传作品
    private final String Assess_Comment_Share = "v1.1/Assess/Share";//作品分享
    private final String Comment_Favorite = "v1.1/Comment/Favorite";//收藏(暂无此功能)
    private final String AttentionUser = "";//关注用户
    private final String PushComment = "";//发表评价
    private final String Friend = "";//获取好友/老师
    private final String FileUpload = "v1.1/File/Upload";//文件上传


    private CommentOperator() {
        flag = ApiDataProvider.initProvider();
    }

    public static CommentOperator getInstance() {
        if (operator == null) {
            operator = new CommentOperator();
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
     * @param callback
     */
    public void getAssessList(boolean isAll, int type, List<Integer> grades, List<Integer> channelIds, int index,
                              ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder PATH = new StringBuilder(AssessList);
                PATH.append("?isAll=" + isAll);
                PATH.append("?type=" + type);
                PATH.append("?grades=" + grades);
                PATH.append("?channelIds=" + channelIds);
                PATH.append("?index=" + index);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        }
    }

    /**
     * 获取标签及年级
     */
    public void addAssessChannelListToCach() {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder PATH = new StringBuilder(AssessChannelList);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(result);
                                    //将json串添加至缓存
                                    SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
                                    spu.add(SharedPreferencesUtil.ASSESS_CHANNEL_LIST, json);
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
     * @param width
     * @param heigth
     * @param type
     * @param define       图片描述"图片大小+图片数量+图片字节长度"
     * @param imgByte      图片字节数组
     * @param callback
     */
    public void uploadAssess(final int userId, final String desc, final int channelId, final int friendUserId, final int width,
                             final int heigth, int type, String define, byte[] imgByte, final ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder FILEUPLOAD = new StringBuilder(FileUpload);
                FILEUPLOAD.append("?type=" + type);
                FILEUPLOAD.append("&define" + define);
                ApiDataProvider.getmClient().invokeApi(FILEUPLOAD.toString(), imgByte, HttpPost.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(),
                        new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(result);
                                    String fileObjectStr = "";
                                    try {
                                        JSONObject object = new JSONObject(json);
                                        JSONArray array = object.getJSONArray("data");
                                        fileObjectStr = array.getString(0);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    //获得文件上传后服务器发送来的Json串，再上传Assess
                                    StringBuilder PATH = new StringBuilder(UploadAssess);
                                    PATH.append("?userId=" + userId);
                                    PATH.append("&desc=" + desc);
                                    PATH.append("&channelId=" + channelId);
                                    PATH.append("&friendUserId=" + friendUserId);
                                    PATH.append("&width=" + width);
                                    PATH.append("&heigth=" + heigth);
                                    PATH.append("&file=" + fileObjectStr);
                                    ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpPost.METHOD_NAME, null,
                                            (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
                                }
                            }
                        });
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
                StringBuilder stringBuilder = new StringBuilder(Assess_Comment_Share);
                stringBuilder.append("?id" + id);
                stringBuilder.append("&type" + type);
                ApiDataProvider.getmClient().invokeApi(stringBuilder.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        }

    }

    public byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}
