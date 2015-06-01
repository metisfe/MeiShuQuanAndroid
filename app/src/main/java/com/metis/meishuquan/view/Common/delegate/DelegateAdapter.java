package com.metis.meishuquan.view.Common.delegate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class DelegateAdapter extends RecyclerView.Adapter<AbsViewHolder> {

    private Context mContext = null;
    private List<? extends DelegateImpl> mDataList = null;

    public DelegateAdapter(Context context, List<? extends DelegateImpl> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    @Override
    public AbsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(DelegateType.getLayoutIdByType(viewType), null);
        return onCreateAbsViewHolder(parent, viewType, view);
    }

    public abstract AbsViewHolder onCreateAbsViewHolder (ViewGroup parent, int viewType, View itemView);

    @Override
    public void onBindViewHolder(AbsViewHolder holder, int position) {
        holder.bindData(mContext, mDataList, getItem(position));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    private DelegateImpl getItem (int position) {
        return mDataList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getDelegateType().getType();
    }
}