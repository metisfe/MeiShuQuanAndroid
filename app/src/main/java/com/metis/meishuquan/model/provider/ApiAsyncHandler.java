package com.metis.meishuquan.model.provider;

import android.os.Handler;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.metis.meishuquan.util.ContractUtility;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wudi on 3/15/2015.
 */
public abstract class ApiAsyncHandler<T> extends AsyncHttpResponseHandler {
    private static int NONETWORK_STATUSCODE = 0;
    public T PreviousResult;
    public ProviderRequestHandler<T> ProviderRequestHandler;
    public String apiUrl;
    public boolean needToCache;
    public Runnable customizedUIRunner;
    public Runnable reinvokeApi;

    public ApiAsyncHandler(String apiUrl, T previousResult, ProviderRequestHandler<T> providerRequestHandler)
    {
        this.apiUrl = apiUrl;
        this.PreviousResult = previousResult;
        this.ProviderRequestHandler = providerRequestHandler;
        this.needToCache = true;
        this.reinvokeApi = null;
    }

    public void onFailedFromCache()
    {
        //w("Fail From Cache.[URL]: %s", this.apiUrl);
        Handler uiThreadHandler = DataProvider.getDefaultUIThreadHandler();
        if (uiThreadHandler != null && this.ProviderRequestHandler != null && !this.ProviderRequestHandler.isCancelled())
        {
            this.ProviderRequestHandler.Result = this.PreviousResult;
            this.ProviderRequestHandler.RequestStatus = ProviderRequestStatus.Load_Failed_DueTo_CacheMissing;
            uiThreadHandler.post(new ApiCallbackRunner(this));
        }
    }

    public void onSuccessFromCache(String content)
    {
        //.w("Success From Cache. [URL]: %s", this.apiUrl);
        //d("[Content]: %s", content);
        ProviderRequestStatus updatedStatus = handleResponseContent(content);
        Handler uiThreadHandler = DataProvider.getDefaultUIThreadHandler();
        if (uiThreadHandler != null && this.ProviderRequestHandler != null && !this.ProviderRequestHandler.isCancelled())
        {
            //.w("Post ProviderRequestHandler to UI-Thread to execute");

            this.ProviderRequestHandler.Result = this.PreviousResult;
            this.ProviderRequestHandler.RequestStatus = updatedStatus;
            uiThreadHandler.post(new ApiCallbackRunner(this));
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
    {
        if (statusCode >= 200 && statusCode < 300)
        {
            String content = responseBody != null && responseBody.length > 0 ? new String(responseBody) : "";

            //"Success From API. [URL]: %s", this.apiUrl);
            //.d("[Content]: %s", content);
            // handle normal response
            ProviderRequestStatus updatedStatus = handleResponseContent(content);

            Handler uiThreadHandler = DataProvider.getDefaultUIThreadHandler();
            if (uiThreadHandler != null && this.ProviderRequestHandler != null && !this.ProviderRequestHandler.isCancelled())
            {
                //"Post ProviderRequestHandler to UI-Thread to execute");

                this.ProviderRequestHandler.Result = this.PreviousResult;
                this.ProviderRequestHandler.RequestStatus = updatedStatus;
                uiThreadHandler.post(new ApiCallbackRunner(this));
            }
        }
        else
        {
            // send to onfailure for error handling
            onFailure(statusCode, headers, responseBody, null);
        }
    }

    @Override
    public void onFailure(final int statusCode, Header[] headers, byte[] responseBody, Throwable error)
    {
        //"Failed From API. [URL]: %s", this.apiUrl);
        //("[StatusCode]: %d", statusCode);
        if (this.ProviderRequestHandler != null
                && this.ProviderRequestHandler.Preference != ProviderPreference.ApiOnly
                && ApiDataProvider.isInCache(this.apiUrl))
        {
            String content = ApiDataProvider.getFromCache(this.apiUrl);
            if (content != null && content.length() > 0)
            {
                this.onSuccessFromCache(content);
                return;
            }
        }

        Handler uiThreadHandler = DataProvider.getDefaultUIThreadHandler();
        if (uiThreadHandler != null && this.ProviderRequestHandler != null && !this.ProviderRequestHandler.isCancelled())
        {
            this.ProviderRequestHandler.Result = this.PreviousResult;
            if (statusCode == NONETWORK_STATUSCODE)
            {
                this.ProviderRequestHandler.RequestStatus = ProviderRequestStatus.Load_Failed_DueTo_NoInternetConnection;
            }
            else
            {
                this.ProviderRequestHandler.RequestStatus = ProviderRequestStatus.Load_Failed;
            }

            uiThreadHandler.post(new ApiCallbackRunner(this));
        }
    }

    public void onFailureDueToConnection()
    {
        onFailure(NONETWORK_STATUSCODE,null,null,null);
    }

    protected ProviderRequestStatus handleResponseContent(String content)
    {
        JSONObject jsonObject = parseJSONObject(content);
        ProviderRequestStatus updatedStatus;

        if (jsonObject != null)
        {
            if (this.needToCache)
            {
                ApiDataProvider.setToCache(this.apiUrl, content);
            }

            updatedStatus = handleSuccess(jsonObject);
            if (updatedStatus == ProviderRequestStatus.Load_Succeeded)
            {
                // Override to load from api.
                updatedStatus = ProviderRequestStatus.Load_Succeeded_From_Api;
            }
        }
        else
        {
            updatedStatus = ProviderRequestStatus.Load_Failed_DueTo_InvalidJSONResult;
        }

        return updatedStatus;
    }

    protected abstract ProviderRequestStatus handleSuccess(JSONObject jsonObject);

    private JSONObject parseJSONObject(String content)
    {
        JSONObject jsonObject = null;
        try
        {
            jsonObject = new JSONObject(content);
        }
        catch (JSONException ex)
        {
        }

        return jsonObject;
    }
}

