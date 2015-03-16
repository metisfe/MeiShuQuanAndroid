package com.metis.meishuquan.model.provider;

import android.os.Handler;

import com.metis.meishuquan.model.contract.Timeline;

/**
 * Created by wudi on 3/15/2015.
 */
public abstract class DataProvider {
    public static boolean IsUsingMockProvider = false;
    private static DataProvider apiMockDataProvider = new MockApiDataProvider();
    private static DataProvider apiDataProvider = new ApiDataProvider();
    private static ProviderPreference defaultPreference = ProviderPreference.PreferApi;
    private static Handler defaultCallbackHandler = null;
    private static Object synchronizeObject = new Object();
//    private final static ProviderRequestHandler<CommentPostResult> defaultLikeCommentRequestHandler = new ProviderRequestHandler<CommentPostResult>()
//    {
//        @Override
//        protected void onLoadCompleted()
//        {
//            //Do Nothing.
//        }
//    };

    public static DataProvider getInstance()
    {
        return IsUsingMockProvider ? apiMockDataProvider : apiDataProvider;
    }

    public static DataProvider getMockDataProvider()
    {
        return apiMockDataProvider;
    }

    public void getTopStoryTimeline(Timeline timeline, ProviderRequestHandler<Timeline> requestHandler)
    {
        getTopStoryTimeline(getDefaultPreference(), timeline, requestHandler);
    }

    public void getTopStoryTimeline(ProviderPreference preference, Timeline timeline, ProviderRequestHandler<Timeline> requestHandler)
    {
        // http://co3.api.score.binginternal.com/api/v2/mobile/topstory/timeline
        getTimeline(preference, "topstory/timeline", timeline, requestHandler);
    }

    public static void setDefaultUIThreadHandler(Handler callbackHandler)
    {
        synchronized (synchronizeObject)
        {
            defaultCallbackHandler = callbackHandler;
        }
    }

    public static Handler getDefaultUIThreadHandler()
    {
        synchronized (synchronizeObject)
        {
            return defaultCallbackHandler;
        }
    }

    public static ProviderPreference getDefaultPreference()
    {
        synchronized (synchronizeObject)
        {
            return defaultPreference;
        }
    }

    protected static void executeWithError(ProviderRequestStatus status, ProviderRequestHandler<?> requestHandler)
    {
        if (requestHandler == null)
        {
            return;
        }

        requestHandler.RequestStatus = status;
        requestHandler.Result = null;
        executeWithHandler(requestHandler);
    }

    protected static void executeWithHandler(ProviderRequestHandler<?> requestHandler)
    {
        Handler handler = getDefaultUIThreadHandler();
        if (handler == null)
        {
            return;
        }

        handler.post(new ApiCallbackRunner(requestHandler));
    }

    protected static void executeWithWrongParameter(String parameterErrorMessage, ProviderRequestHandler<?> requestHandler)
    {
        if (requestHandler != null)
        {
            requestHandler.ErrorMessage = parameterErrorMessage;
        }

        executeWithError(ProviderRequestStatus.Load_Failed_DueTo_WrongInputParameters, requestHandler);
    }

    public abstract void getTimeline(ProviderPreference preference, String apiPath, Timeline timeline, ProviderRequestHandler<Timeline> requestHandler);

}
