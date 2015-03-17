package com.metis.meishuquan.adapter.topline;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.metis.meishuquan.adapter.shared.ContractBaseAdapter;
import com.metis.meishuquan.model.contract.Moment;

/**
 * Created by wudi on 3/15/2015.
 */
public class ToplineAdapter extends ContractBaseAdapter<Moment> {
    public ToplineAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        //Moment moment = (Moment) this.getItem(position);
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = new TextView(this.context);
        view.setText("hello world");
        view.setLayoutParams(new AbsListView.LayoutParams(1000,1000));
        // this line cause the Cast Exception, change to use AbsListView's LayoutParams
        return view;
    }
}
