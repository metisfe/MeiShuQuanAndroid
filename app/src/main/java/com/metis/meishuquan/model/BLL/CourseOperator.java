package com.metis.meishuquan.model.BLL;

import android.util.Log;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.course.CourseChannel;
import com.metis.meishuquan.model.course.CourseChannelData;
import com.metis.meishuquan.model.course.CourseChannelItem;
import com.metis.meishuquan.model.enums.CourseType;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.util.SystemUtil;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjin on 15/4/16.
 */
public class CourseOperator {
    private static CourseOperator operator = null;

    private final String COURSELIST = "v1.1/Course/CourseList?";
    private final String COURSEDETIAL = "v1.1/Course/CourseDetial?";
    private final String COURSECOMMENTLIST = "v1.1/Course/CommentList?";
    private final String COURSEIMGLIST = "v1.1/Course/CourseImgList?";
    private final String COURSECHANNELLIST = "v1.1/Channel/CourseChannelList?";
    private final String SESSION = MainApplication.userInfo.getCookie();

    private boolean flag;

    private CourseOperator() {
        flag = ApiDataProvider.initProvider();
    }

    public static CourseOperator getInstance() {
        if (operator == null) {
            operator = new CourseOperator();
        }
        return operator;
    }

    /**
     * 获得课程列表数据
     *
     * @param tags         id数组字符串
     * @param orderType    1,推荐，2最新，3，热播，4图片
     * @param querycontent 重询内容
     * @param index        索引
     * @param callback
     */
    public void getCouseList(String tags, CourseType orderType, String querycontent, int index, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder PATH = new StringBuilder(COURSELIST);
                PATH.append("tags=" + tags);
                PATH.append("&orderType=" + orderType.getVal());
                PATH.append("&querycontent=" + querycontent);
                PATH.append("&index=" + index);
                PATH.append("&session=" + SESSION);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            } else {
                Toast.makeText(MainApplication.UIContext, "无网络", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 获得课程详情
     *
     * @param id       课程Id
     * @param callback
     */
    public void getCourseDetial(int id, ApiOperationCallback callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder PATH = new StringBuilder(COURSEDETIAL);
                PATH.append("id=" + id);
                PATH.append("&session=" + SESSION);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            } else {
                Toast.makeText(MainApplication.UIContext, "无网络", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 获得课程评论列表
     *
     * @param courseId 课程Id
     * @param lastId   最后一条数据索引
     * @param callback
     */
    public void getCourseCommentList(int courseId, int lastId, ApiOperationCallback callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder PATH = new StringBuilder(COURSECOMMENTLIST);
                PATH.append("courseId=" + courseId);
                PATH.append("&lastId=" + lastId);
                PATH.append("&session=" + SESSION);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            } else {
                Toast.makeText(MainApplication.UIContext, "无网络", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 获得课程图片列表
     *
     * @param tag       id数组字符串
     * @param orderType 1,推荐，2最新，3，最多收藏，4图片
     * @param type      0图片，1视频
     * @param index     索引（页）
     */
    public void getCourseImgList(String tag, CourseType orderType, int type, int index) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder PATH = new StringBuilder(COURSEIMGLIST);
                PATH.append("tag=" + tag);
                PATH.append("&orderType=" + orderType.getVal());
                PATH.append("&type=" + type);
                PATH.append("&index=" + index);
                PATH.append("&session=" + SESSION);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {
                    @Override
                    public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                        if (result != null && result.getInfo().equals(String.valueOf(0))) {
                            String json = new Gson().toJson(result);
                            Log.i("getCourseImgList", json);
                            SharedPreferencesUtil.getInstanse(MainApplication.UIContext).update(SharedPreferencesUtil.COURSEIMGLIST, json);
                        }
                    }
                });
            } else {
                Toast.makeText(MainApplication.UIContext, "无网络", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getCourseChannelList(ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuilder PATH = new StringBuilder(COURSECHANNELLIST);
                PATH.append("session=" + SESSION);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            } else {
                Toast.makeText(MainApplication.UIContext, "无网络", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addCourseChannelToCathe() {
        CourseOperator.getInstance().getCourseChannelList(new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                if (result != null && Integer.parseInt(result.getInfo()) == 0) {
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    SharedPreferencesUtil.getInstanse(MainApplication.UIContext).update(SharedPreferencesUtil.COURSECHANNELLIST, json);
                }
            }
        });
    }
}
