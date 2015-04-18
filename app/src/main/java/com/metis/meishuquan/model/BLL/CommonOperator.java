package com.metis.meishuquan.model.BLL;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.BlockTypeEnum;
import com.metis.meishuquan.model.enums.SupportStepTypeEnum;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.SystemUtil;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;

import org.apache.http.client.methods.HttpGet;

/**
 * Created by wangjin on 15/4/13.
 */
public class CommonOperator {
    private static final String URL_SUPPORTORSTEP = "v1.1/Comment/Support";
    private static CommonOperator operator = null;
    private final String PUBLISHCOMMENT = "v1.1/Comment/PublishComment";//发表评论
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
    public void supportOrStep(int userid, int id, SupportStepTypeEnum type, int result, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuffer path = new StringBuffer(URL_SUPPORTORSTEP);
                path.append("?userid=" + userid);
                path.append("&id=" + id);
                path.append("&type=" + type.getVal());
                path.append("&result=" + result);
                path.append("&session=" + SESSION);
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
    public void publishComment(int userid, int newsId, String content, int replyCid, BlockTypeEnum blockType, ApiOperationCallback<ReturnInfo<String>> callback) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (flag) {
                StringBuffer path = new StringBuffer(PUBLISHCOMMENT);
                path.append("?userid=" + userid);
                path.append("&newsid=" + newsId);
                path.append("&content=" + content);
                path.append("&replyCid=" + replyCid);
                path.append("&blockType=" + blockType.getVal());
                path.append("&session=" + SESSION);
                ApiDataProvider.getmClient().invokeApi(path.toString(), null, HttpGet.METHOD_NAME, null,
                        (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }
        }
    }
}
