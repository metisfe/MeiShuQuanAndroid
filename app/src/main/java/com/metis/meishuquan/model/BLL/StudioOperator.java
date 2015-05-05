package com.metis.meishuquan.model.BLL;

import android.util.Log;

import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.SystemUtil;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;

/**
 * Created by WJ on 2015/5/5.
 */
public class StudioOperator {

    private static final String TAG = StudioOperator.class.getSimpleName();

    private static final String URL_STUDIO_BASE_INFO = "v1.1/Studio/StudioBasicInfo?studioId=";

    private static final String KEY_SESSION = "session";

    private static StudioOperator sOperator = new StudioOperator();

    public static StudioOperator getInstance () {
        return sOperator;
    }

    public void getStudioBaseInfo (int studioId) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_STUDIO_BASE_INFO);
            sb.append(studioId);
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());

            Log.v(TAG, "getStudioBaseInfo request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(result);
                        Log.v(TAG, "getStudioBaseInfo resultJson=" + resultJson);
                    }
                }
            });
        }
    }
}
