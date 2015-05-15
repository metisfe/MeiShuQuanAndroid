package com.metis.meishuquan.model.BLL;

import android.util.Log;
import android.util.Pair;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.commons.AndroidVersion;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.BlockTypeEnum;
import com.metis.meishuquan.model.enums.FileUploadTypeEnum;
import com.metis.meishuquan.model.enums.PrivateResultEnum;
import com.metis.meishuquan.model.enums.PrivateTypeEnum;
import com.metis.meishuquan.model.enums.SupportTypeEnum;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.SystemUtil;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjin on 15/4/13.
 */
public class CommonOperator {
    private static final String Log_PubLishComment_url = "Log_PubLishComment_url";

    private static final String URL_SUPPORTORSTEP = "v1.1/Comment/Support";
    private static CommonOperator operator = null;
    private final String PUBLISHCOMMENT = "v1.1/Comment/PublishComment";//发表评论
    private final String PRIVATE = "v1.1/Comment/Favorite";
    private final String FileUpload = "v1.1/File/Upload";//文件上传
    private final String ANDROIDVERSION = "v1.1/Default/AndroidVersion";//获取最新版本
    private final String CHECKLOGINSTATE = "v1.1/Default/Start?session=" + MainApplication.getSession();//校验账号状态

    private final String SESSION = MainApplication.userInfo.getCookie();
    private boolean flag;

    private CommonOperator() {
        flag = ApiDataProvider.initProvider();
    }

    public static CommonOperator getInstance() {
        if (operator == null) {
            operator = new CommonOperator();
        }
        return operator;
    }

    /**
     * 赞或踩
     *
     * @param userid   用户Id
     * @param id       根据类型传指定Id
     * @param type     类型 赞类型Assess = 1,AssessComment = 2,News = 3,NewsComment = 4,Course = 5,CourseComment = 6,Circle=7,CircleComment =8
     * @param result   1:赞  2:踩
     * @param callback 回调
     */
    public void supportOrStep(int userid, int id, SupportTypeEnum type, int result, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuffer path = new StringBuffer(URL_SUPPORTORSTEP);
                path.append("?userid=" + userid);
                path.append("&id=" + id);
                path.append("&type=" + type.getVal());
                path.append("&result=" + result);
                path.append("&session=" + MainApplication.getSession());
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
     * @param blockType 0头条，2课程
     * @param callback
     */
    public void publishComment(int userid, int newsId, String content, int replyCid, BlockTypeEnum blockType, ApiOperationCallback<ReturnInfo<String>> callback) {
        String encodeContent = URLEncoder.encode(content);
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder path = new StringBuilder(PUBLISHCOMMENT);
                path.append("?userid=" + userid);
                path.append("&newsid=" + newsId);
                path.append("&content=" + encodeContent);
                path.append("&replyCid=" + String.valueOf(replyCid));
                path.append("&blockType=" + String.valueOf(blockType.getVal()));
                path.append("&session=" + MainApplication.getSession());
                Log.i(Log_PubLishComment_url, path.toString());
                ApiDataProvider.getmClient().invokeApi(path.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        }
    }

    /**
     * 收藏
     *
     * @param userid   用户Id
     * @param id       类型Id
     * @param type     类型
     * @param result   收藏或取消收藏
     * @param callback
     */
    public void favorite(int userid, int id, PrivateTypeEnum type, PrivateResultEnum result, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuffer path = new StringBuffer(PRIVATE);
                path.append("?userid=" + userid);
                path.append("&id=" + id);
                path.append("&type=" + type.getVal());
                path.append("&result=" + result.getVal());
                path.append("&session=" + SESSION);
                Log.i("收藏Url", path.toString());
                ApiDataProvider.getmClient().invokeApi(path.toString(), null, HttpGet.METHOD_NAME, null,
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

    public void getVersion(ApiOperationCallback<ReturnInfo<AndroidVersion>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                ApiDataProvider.getmClient().invokeApi(ANDROIDVERSION, null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<AndroidVersion>>) new ReturnInfo<AndroidVersion>().getClass(), callback);
            }
        }
    }

    public void checkLoginState(ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                ApiDataProvider.getmClient().invokeApi(CHECKLOGINSTATE, null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        }
    }
}
