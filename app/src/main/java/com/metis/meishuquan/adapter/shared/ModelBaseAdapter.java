package com.metis.meishuquan.adapter.shared;

import android.content.Context;
import android.widget.BaseAdapter;

/**
 * Created by wudi on 3/15/2015.
 */
public abstract class ModelBaseAdapter<T> extends BaseAdapter
{
    protected T data;
    protected Context context;

    public ModelBaseAdapter(Context context)
    {
        super();
        this.context = context;
    }

    public void setData(T data)
    {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public T getData()
    {
        return this.data;
    }

    @Override
    public boolean isEmpty()
    {
        return this.data == null;
    }
}