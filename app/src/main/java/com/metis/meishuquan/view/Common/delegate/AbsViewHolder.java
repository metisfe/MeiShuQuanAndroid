package com.metis.meishuquan.view.common.delegate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public abstract class AbsViewHolder <T> extends RecyclerView.ViewHolder{
    public AbsViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindData (Context context, List<? extends DelegateImpl> dataList, T t);
}