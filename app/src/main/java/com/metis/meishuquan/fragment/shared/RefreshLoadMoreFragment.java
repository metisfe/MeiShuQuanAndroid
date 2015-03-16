package com.metis.meishuquan.fragment.shared;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.shared.ContractBaseAdapter;
import com.metis.meishuquan.adapter.shared.ModelBaseAdapter;
import com.metis.meishuquan.fragment.BaseFragment;
import com.metis.meishuquan.model.contract.ContractBase;
import com.metis.meishuquan.model.provider.ProviderRequestHandler;
import com.metis.meishuquan.model.provider.ProviderRequestStatus;
import com.metis.meishuquan.view.shared.RefreshLoadMoreListView;
import com.metis.meishuquan.view.shared.error.BaseErrorView;
import com.metis.meishuquan.view.shared.error.ErrorViewFactory;

/**
 * Created by wudi on 3/15/2015.
 */

public abstract class RefreshLoadMoreFragment<T> extends BaseFragment
{
    protected static final String LoadDataCountFormat = "load %d messages";
    protected static final String LoadFailFormat = "fail, try again later";

    protected RefreshLoadMoreListView dataList;
    protected ProviderRequestHandler<T> requestHandler;
    protected BaseErrorView errorView;
    protected boolean isFirstLoadingWithCache;
    protected int refreshCount;

    protected BaseErrorView.ErrorType errorType;

    // 0:refresh 1:loading more
    protected int flag = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = onCreateDataView(inflater, container, savedInstanceState);

        this.dataList = (RefreshLoadMoreListView) rootView.findViewById(this.getRefreshLoadMoreViewId());
        this.dataList.setMessageType(1);
        this.dataList.setOnRefreshListener(new RefreshLoadMoreListView.OnRefreshLoadMoreListener()
        {
            @Override
            public void onRefresh()
            {
                flag = 0;
                onRefreshData();
            }

            @Override
            public void onLoadMore()
            {
                flag = 1;
                onLoadMoreData();
            }

            @Override
            public boolean hasMore()
            {
                return isDataHasMore();
            }
        });

        this.onCreateAdapter();
        this.createProviderRequestHandler();

        // init adapter
        this.initAdapter();

        // error view
        this.errorView = ErrorViewFactory.getDefaultErrorView(this.getActivity());

        // Load from cache at first
        this.isFirstLoadingWithCache = true;
        this.dataList.firstRefresh();

        return rootView;
    }

    protected void setupErrorView()
    {
        ErrorViewFactory.initErrorView(this.dataList, new BaseErrorView.ErrorListener()
        {
            @Override
            public void OnRecommendOption()
            {
            }

            @Override
            public void OnErrorRefresh()
            {
                dataList.firstRefresh();
            }
        });

        this.errorView.setErrorType(errorType);
        this.errorView.bind();
    };

    @Override
    public void onResume()
    {
        super.onResume();
        this.createProviderRequestHandler();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        this.clearProviderRequestHandler();
    }

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        if (!hidden && this.errorType != null)
        {
            switch (this.errorType)
            {
                case NetworkNotAvailable:
                    this.setupErrorViewWhenNoConnection();
                    break;
                default:
                    this.setupErrorViewWhenNoData();
                    break;
            }

            this.getAdapter().notifyDataSetChanged();
        }
    }

    protected boolean hasMoreWithContractBaseAdapter(ContractBaseAdapter<?> adapter)
    {
        return adapter == null || adapter.getData() == null ? false : adapter.getData().hasMore();
    }

    private boolean retryFirstLoadDueToCache(T data)
    {
        if (this.isFirstLoadingWithCache)
        {
            if (data != null)
            {
                onUpdatingData(data);
            }

            this.isFirstLoadingWithCache = false;
            this.onRefreshData();

            return true;
        }

        return this.isFirstLoadingWithCache;
    }

    protected void createProviderRequestHandler()
    {
        if (this.requestHandler != null)
        {
            return;
        }

        this.requestHandler = new ProviderRequestHandler<T>()
        {
            @Override
            protected void onLoadCompleted()
            {
                if (this.isSucceeded())
                {
                    errorView.clear();

                    if (retryFirstLoadDueToCache(this.Result))
                    {
                    }
                    else
                    {
                        onLoadSucceeded(this.Result);
                    }
                }
                else
                {
                    if (!retryFirstLoadDueToCache(this.Result))
                    {
                        onLoadFailed(this);
                    }
                    else
                    {
                        //"OnLoadCompleted is invoked but no following function calls due to Cache Data Reason");
                    }
                }
            }
        };
    }

    protected void clearProviderRequestHandler()
    {
        if (this.requestHandler == null)
        {
            return;
        }

        this.requestHandler.cancel();
        this.requestHandler = null;
    }

    protected abstract ViewGroup onCreateDataView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void onCreateAdapter();

    protected abstract int getRefreshLoadMoreViewId();

    protected abstract void onUpdatingData(T data);

    protected boolean isContentGreaterThanViewHeight()
    {
        return true;
    }

    private void loadMoreIfNotFullScreen()
    {
        ModelBaseAdapter<?> adapter = this.getAdapter();

        if (!(adapter instanceof ContractBaseAdapter<?>))
        {
            return;
        }

        if (this.hasMoreWithContractBaseAdapter((ContractBaseAdapter<?>)adapter) &&
                !this.isContentGreaterThanViewHeight())
        {
            this.onLoadMoreData();
        }
    }

    protected void onLoadDataCompletedWithContactBaseData(ContractBase<?> data)
    {
        this.dataList.onLoadDataCompletedWithMessage(true, this.refreshCount == 0 ? null : String.format(LoadDataCountFormat, this.refreshCount));
        loadMoreIfNotFullScreen();
    }

    protected void onLoadSucceeded(T data)
    {
        this.dataList.onLoadDataCompletedWithMessage(true, null);
        loadMoreIfNotFullScreen();
    }

    protected void onLoadFailed(ProviderRequestHandler<T> handler)
    {
        if (this.getAdapter().getData() == null)
        {
            if (handler.RequestStatus == ProviderRequestStatus.Load_Failed_DueTo_NoInternetConnection)
            {
                this.setupErrorViewWhenNoConnection();
            }
            else
            {
                this.setupErrorViewWhenNoData();
            }

            this.getAdapter().notifyDataSetChanged();
        }
        else
        {
            this.dataList.onLoadDataCompletedWithMessage(false, LoadFailFormat);
        }
    }

    protected abstract boolean isDataHasMore();

    protected abstract void onRefreshData();

    protected abstract void onLoadMoreData();

    protected void setupErrorViewWhenNoData()
    {
        this.errorType = BaseErrorView.ErrorType.NoData;
        this.setupErrorView();
    }

    protected void setupErrorViewWhenNoConnection()
    {
        this.errorType = BaseErrorView.ErrorType.NetworkNotAvailable;
        this.setupErrorView();
    }

    protected abstract ModelBaseAdapter<?> getAdapter();

    protected abstract void initAdapter();
}
