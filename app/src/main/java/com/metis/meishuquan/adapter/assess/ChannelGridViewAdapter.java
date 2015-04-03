package com.metis.meishuquan.adapter.assess;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.model.assess.Channel;
import com.metis.meishuquan.model.assess.Grade;

import java.util.List;

/**
 * Created by wj on 15/4/2.
 */
public class ChannelGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<Channel> lstChannel;

    public ChannelGridViewAdapter(Context context, List<Channel> lstChannel) {
        this.context = context;
        this.lstChannel = lstChannel;
    }

    @Override
    public int getCount() {
        if (lstChannel != null) {
            return lstChannel.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (lstChannel != null) {
            return lstChannel.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        if (lstChannel != null) {
            return lstChannel.get(i).getChannelId();
        }
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //TextView,显示在GridView里
        TextView textView;
        if (view == null) {
            textView = new TextView(context);
            textView.setLayoutParams(new GridView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setTextColor(Color.rgb(126, 126, 126));
            textView.setTextSize(18);
        } else {
            textView = (TextView) view;
        }
        textView.setText(lstChannel.get(i).getChannelName());
        return textView;
    }
}
