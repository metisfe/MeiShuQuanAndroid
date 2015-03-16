package com.metis.meishuquan.model.provider;

/**
 * Created by wudi on 3/15/2015.
 */
public final class ApiCallbackRunner implements Runnable
{
    private ApiAsyncHandler<?> apiAsyncHandler;
    private ProviderRequestHandler<?> requestHandler;

    public ApiCallbackRunner(ProviderRequestHandler<?> requestHandler)
    {
        this.requestHandler = requestHandler;
    }

    public ApiCallbackRunner(ApiAsyncHandler<?> apiAsyncHandler)
    {
        this.apiAsyncHandler = apiAsyncHandler;
    }

    @Override
    public void run()
    {
        if ((this.apiAsyncHandler == null || this.apiAsyncHandler.ProviderRequestHandler == null) && this.requestHandler == null)
        {
            //"AsyncCallbackRunner can't execute in UI Thread due to either AsyncHandler or AsyncHandler.ProviderReqeustHandler or RequestHandler is null");
            return;
        }

        if (this.requestHandler != null)
        {
            //"Invoke this.requestHandler.loadComplete() for postProcess in UIThread");
            this.requestHandler.loadComplete();
        }
        else
        {
            if (this.apiAsyncHandler.customizedUIRunner != null)
            {
                //"Invoke this.apiAsyncHandler.customizedUIRunner.run() for postProcess in UIThread");
                this.apiAsyncHandler.customizedUIRunner.run();
            }

            //"Invoke this.apiAsyncHandler.customizedUIRunner.loadComplete() for postProcess in UIThread");
            this.apiAsyncHandler.ProviderRequestHandler.loadComplete();
        }
    }
}