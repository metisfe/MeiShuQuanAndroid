package com.metis.meishuquan.model.BLL;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.circle.CCircleDetailModel;
import com.metis.meishuquan.model.circle.CirclePushBlogParm;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.SystemUtil;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

/**
 * Created by wangjin on 15/4/13.
 */
public class CircleOperator {
    private static final String Log_PubLishComment_url = "Log_PubLishComment_url";
    private static final String URL_PUSHBLOG = "v1.1/Circle/PushBlog?";

    private static CircleOperator operator = null;
    private static final String SESSION = MainApplication.userInfo.getCookie();
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
                PATH.append("session=" + SESSION);
                ApiDataProvider.getmClient().invokeApi(PATH.toString(), param, HttpPost.METHOD_NAME, null,
                        (Class<ReturnInfo<CCircleDetailModel>>) new ReturnInfo<CCircleDetailModel>().getClass(), callback);
            }
        }
    }
}
