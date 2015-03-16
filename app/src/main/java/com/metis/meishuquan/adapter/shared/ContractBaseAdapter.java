package com.metis.meishuquan.adapter.shared;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.metis.meishuquan.model.contract.ContractBase;

/**
 * Created by wudi on 3/15/2015.
 */

public abstract class ContractBaseAdapter<T> extends ModelBaseAdapter<ContractBase<T>>
{
    protected ContractBase<T> previousData;

    public ContractBaseAdapter(Context context)
    {
        super(context);
    }

    @Override
    public int getCount()
    {
        return this.data == null ? 0 : data.getCurrentSize();
    }

    @Override
    public Object getItem(int position)
    {
        ContractBase<T> data = this.data;
        if (data == null)
        {
            return null;
        }

        return data.getItem(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public abstract View getView(int position, View convertView, ViewGroup parent);

    public void setPreviousData()
    {
        this.previousData = this.data;
    }

    @Override
    public void setData(ContractBase<T> data)
    {
        super.setData(data);
    }

    public int calculateDifferentItemCount()
    {
        if (this.data == null)
        {
            return 0;
        }

        return this.data.calculateDifferentItemCount(this.previousData);
    }

    @Override
    public boolean isEmpty()
    {
        return super.isEmpty() || this.data.getCurrentSize() == 0;
    }

    public int getContentHeight()
    {
        return Integer.MAX_VALUE;
    }
}