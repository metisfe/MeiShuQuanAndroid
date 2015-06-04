package com.metis.meishuquan.model.provider;

import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.framework.cache.DiskCache;
import com.metis.meishuquan.model.contract.Pagination;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.contract.Timeline;
import com.metis.meishuquan.util.SystemUtil;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wudi on 3/15/2015.
 */
public class ApiDataProvider extends DataProvider {
    private static MobileServiceClient mClient;
    private static final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private static final String DebugTag = "Model.Provider.ApiDataProvider";
    private static final String Default_Api_Version = "2";
    private static final String Api_Version_V3 = "3";
    private static final String FORMAL = "https://metisapi.azure-mobile.cn";
    private static final String FORMAL_KEY = "JhSUSARkPDywIlrCKJKQzOJIttIYWU24";
    private static final String TEST = "https://metisapipre.azure-mobile.cn";
    private static final String TEST_KEY = "UnGLUQBCdpfYOzgupfoYTFQVcgKovC64";

    public static String API_ROOT;
    private static String trackingCode;
    private static Map<String, String> apiBeds;
    private static String currentDefaultBed;

    private static final String KEY_ACTIVITY_ID = "ActivityId";
    private static final String KEY_CANARY = "Canary";
    private static final String KEY_SNS_ID = "SnsId";
    private static final String KEY_ENABLETOPMARK = "EnableTopMark";

    private static final String KEY_COMMENT_POST_MESSAGE = "Message";
    private static final String KEY_COMMENT_POST_TIMESTAMP = "Timestamp";
    private static final String VALUE_TRUE = "true";

    public static MobileServiceClient getmClient() {
        return mClient;
    }

    public static boolean initProvider() {
        try {
//            mClient = new MobileServiceClient(
//                    FORMAL,
//                    FORMAL_KEY,
//                    MainApplication.UIContext);
            mClient = new MobileServiceClient(
                    TEST,
                    TEST_KEY,
                    MainApplication.UIContext);
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String getDefautlBed() {
        return currentDefaultBed;
    }

    public static void setDefaultBed(String bedName) {
        if (apiBeds == null || !apiBeds.containsKey(bedName)) {
            return;
        }

        String apiRoot = apiBeds.get(bedName);
        API_ROOT = apiRoot;

        currentDefaultBed = bedName;
    }

    public static void test() {
        try {
            mClient.invokeApi("v1.1/Channel/ChannelList?userId=1&type=1", null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {
                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    Log.d("hello", result.getInfo());
                }
            });

        } catch (Exception e) {
            Log.e("hello", e.getMessage());
        }
    }

    @Override
    public void getTimeline(ProviderPreference preference, String apiPath, Timeline timeline, ProviderRequestHandler<Timeline> requestHandler) {
        if (TextUtils.isEmpty(apiPath)) {
            executeWithWrongParameter("apiPath is null or empty", requestHandler);
            return;
        }

        Map<String, String> queryStrings = null;
        Pagination pagination = timeline != null ? timeline.getPagination() : null;
        if (pagination == null) {
            //Only want API return TopMarked Moment when refresh the data.
            queryStrings = new HashMap<String, String>();
            queryStrings.put(KEY_ENABLETOPMARK, VALUE_TRUE);
        }

        String apiUrl = checkAndGetApiUrl(pagination,
                apiPath,
                queryStrings,
                false,
                requestHandler,
                preference);
        if (TextUtils.isEmpty(apiUrl)) {
            return;
        }

        ApiAsyncHandler<Timeline> asyncHandler = new ApiAsyncHandler<Timeline>(apiUrl, timeline, requestHandler) {
            @Override
            public ProviderRequestStatus handleSuccess(JSONObject jsonObject) {
                if (this.PreviousResult == null) {
                    this.PreviousResult = Timeline.parse(jsonObject);
                } else {
                    this.PreviousResult.loadMore(jsonObject);
                    final Timeline timelie = this.PreviousResult;
                    this.customizedUIRunner = new Runnable() {
                        @Override
                        public void run() {
                            timelie.commitLoadMore();
                        }
                    };
                }

                return this.PreviousResult == null ? ProviderRequestStatus.Load_Failed_DueTo_IncompatibleResult : ProviderRequestStatus.Load_Succeeded;
            }
        };

        if (loadApiFromCache(preference, apiUrl, asyncHandler)) {
            return;
        }

        getRequest(apiUrl, asyncHandler);
    }

    public static boolean isInCache(String apiUrl) {
        apiUrl = removeTrackingCode(apiUrl);

        if (TextUtils.isEmpty(apiUrl)) {
            return false;
        }

        return DiskCache.getInstance().isCached(apiUrl);
    }

    public static String getFromCache(String apiUrl) {
        apiUrl = removeTrackingCode(apiUrl);

        if (TextUtils.isEmpty(apiUrl)) {
            return null;
        }

        return DiskCache.getInstance().getString(apiUrl);
    }

    public static void setToCache(String apiUrl, String content) {
        apiUrl = removeTrackingCode(apiUrl);

        if (TextUtils.isEmpty(apiUrl)) {
            return;
        }

        if (content == null) {
            content = "";
        }

        DiskCache.getInstance().cache(apiUrl, content);
    }

    private static String removeTrackingCode(String apiUrl) {
        //TODO:
        return apiUrl;
    }

    private static void getRequest(String apiUrl, ApiAsyncHandler<?> asyncHandler) {
        if (asyncHandler != null && asyncHandler.ProviderRequestHandler != null && asyncHandler.ProviderRequestHandler.isCancelled()) {
            return;
        }

        if (!SystemUtil.isNetworkAvailable(MainApplication.MainActivity)) {
            //"[%s] Due to connection issue, this get reqeust was skipped. the url is %s", DebugTag, apiUrl);
            executeWithError(ProviderRequestStatus.Load_Failed_DueTo_NoInternetConnection, asyncHandler.ProviderRequestHandler);
            asyncHandler.onFailureDueToConnection();
            return;
        }

        asyncHttpClient.get(apiUrl, asyncHandler);
    }

    private static void postJsonRequest(String apiUrl, JSONObject jsonObject, ApiAsyncHandler<?> asyncHandler) {
        postStringRequest(apiUrl, jsonObject.toString(), asyncHandler);
    }

    private static void postStringRequest(String apiUrl, String content, ApiAsyncHandler<?> asyncHandler) {
        StringEntity entity = null;
        asyncHandler.needToCache = false;

        //"[%s] Post to %s, with content: \n %s", DebugTag, apiUrl, content == null ? "" : content);

        if (!SystemUtil.isNetworkAvailable(MainApplication.MainActivity)) {
            //"[%s] Due to connection issue, this post reqeust was skipped. the url is %s", DebugTag, apiUrl);
            executeWithError(ProviderRequestStatus.Load_Failed_DueTo_NoInternetConnection, asyncHandler.ProviderRequestHandler);
            return;
        }

        try {
            entity = new StringEntity(content, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            executeWithException(ex, asyncHandler.ProviderRequestHandler);
            return;
        }

        asyncHttpClient.post(null, apiUrl, entity, "application/json", asyncHandler);
    }

    private static void executeWithException(Exception ex, ProviderRequestHandler<?> requestHandler) {
        if (requestHandler != null) {
            requestHandler.ErrorMessage = ex.toString();
        }

        executeWithError(ProviderRequestStatus.Load_Failed_DueTo_Exception, requestHandler);
    }

    private static boolean loadApiFromCache(ProviderPreference preference, String apiUrl, ApiAsyncHandler<?> asyncHandler) {
        if (preference != ProviderPreference.CacheOnly) {
            return false;
        }

        //"[%s]Start to load cache for URL: %s", DebugTag, apiUrl);
        if (!isInCache(apiUrl)) {
            //"[%s]Cache is missed", DebugTag);
            asyncHandler.onFailedFromCache();
            return true;
        }

        String content = getFromCache(apiUrl);
        if (content == null) {
            //"[%s]Cache content is empty", DebugTag);
            asyncHandler.onFailedFromCache();
            return true;
        }

        //"[%s]It seems cache content is ok, invoke callback to process", DebugTag);
        asyncHandler.onSuccessFromCache(content);
        return true;
    }

    private static String checkAndGetApiUrl(Pagination pagination,
                                            String apiPath,
                                            Map<String, String> queryStrings,
                                            boolean requireHttps,
                                            ProviderRequestHandler<?> requestHandler,
                                            ProviderPreference preference) {
        return checkAndGetApiUrl(pagination, apiPath, queryStrings, requireHttps, requestHandler, preference, Default_Api_Version);
    }

    private static String checkAndGetApiUrl(Pagination pagination,
                                            String apiPath,
                                            Map<String, String> queryStrings,
                                            boolean requireHttps,
                                            ProviderRequestHandler<?> requestHandler,
                                            ProviderPreference preference,
                                            String apiVersion) {
        if (requestHandler == null) {
            return null;
        }

        if (!checkPagination(pagination, requestHandler)) {
            return null;
        }

        String apiUrl = getApiUrl(requireHttps, apiPath, getPaginationQueryStrings(queryStrings, pagination), apiVersion);
        if (TextUtils.isEmpty(apiUrl)) {
            executeWithWrongParameter("Can't get valid ApiURL", requestHandler);
            return null;
        }

        requestHandler.Preference = preference;

        return apiUrl;
    }

    private static boolean checkPagination(Pagination pagination, ProviderRequestHandler<?> requestHandler) {
        if (pagination == null || pagination.HasMore) {
            return true;
        }

        executeWithWrongParameter("There is no more result according to pagination.", requestHandler);

        return false;
    }

    private static Map<String, String> getPaginationQueryStrings(Map<String, String> queryStrings, Pagination pagination) {
        if (pagination == null) {
            return queryStrings;
        }

        if (queryStrings == null) {
            queryStrings = new HashMap<String, String>();
        }

        if (pagination.Version != null) {
            queryStrings.put(Pagination.VersionKey, pagination.Version);
        }

        if (pagination.Start != null) {
            queryStrings.put(Pagination.StartKey, pagination.Start);
        }

        return queryStrings;
    }

    protected static String getApiUrl(boolean requireHttps, String path) {
        return getApiUrl(requireHttps, path, null, Default_Api_Version);
    }

    protected static String getApiUrl(boolean requireHttps, String path, Map<String, String> queryStrings, String apiVersion) {
        if (TextUtils.isEmpty(apiVersion)) {
            apiVersion = Default_Api_Version;
        }

        String apiPathQueryString = "";
        int queryStringMarkPosition = path.indexOf('?');
        if (queryStringMarkPosition > 0) {
            //path conatins querystring, we should split it.
            apiPathQueryString = path.substring(queryStringMarkPosition + 1).trim();
            path = path.substring(0, queryStringMarkPosition);

            if (apiPathQueryString.startsWith("&")) {
                apiPathQueryString = apiPathQueryString.substring(1);
            }

            if (apiPathQueryString.endsWith("&")) {
                apiPathQueryString = apiPathQueryString.substring(0, apiPathQueryString.length() - 1);
            }
        }

        if (path.startsWith("/")) {
            //remove '/'
            path = path.substring(1);
        }

        StringBuilder allPathBuilder = new StringBuilder();
        allPathBuilder.append(path);

        StringBuilder queryStringsBuilder = new StringBuilder();
        queryStringsBuilder.append(apiPathQueryString);
        if (queryStrings != null && queryStrings.size() > 0) {
            Iterator<Map.Entry<String, String>> iterator = queryStrings.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> item = iterator.next();
                String key = item.getKey();
                String value = item.getValue();

                if (TextUtils.isEmpty(key) || value == null) {
                    continue;
                }

                if (queryStringsBuilder.length() > 0) {
                    queryStringsBuilder.append("&");
                }

                queryStringsBuilder.append(encodeURLValue(key));
                queryStringsBuilder.append("=");
                queryStringsBuilder.append(encodeURLValue(value));
            }
        }

        if (queryStringsBuilder.length() > 0) {
            queryStringsBuilder.append("&");
        }

        if (!path.endsWith("?")) {
            allPathBuilder.append('?');
        }

        allPathBuilder.append(queryStringsBuilder);

        return getQueryUrl(requireHttps, API_ROOT, allPathBuilder.toString(), apiVersion);
    }

    private static String encodeURLValue(String value) {
        if (value == null) {
            return "";
        }

        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        return value;
    }

    private static String getQueryUrl(boolean requireHttps, String root, String path, String apiVersion) {
        String url = root.replace("${Path}", path);
        url = url.replace("${EncodedPath}", encodeURLValue(path));
        url = url.replace("${IsSecure}", requireHttps ? "s" : "");
        url = url.replace("${ApiVersion}", apiVersion);

        return url;
    }
}