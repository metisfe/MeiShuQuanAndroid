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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wj on 15/4/2.
 */
public class ChannelGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<Channel> lstChannel;
    public List<Channel> lstCheckedChannel = new ArrayList<Channel>();
    public List<Channel> lstOldCheckedChannel = new ArrayList<Channel>();

    public ChannelGridViewAdapter(Context context, List<Channel> lstChannel, List<Channel> lstOldCheckedChannel) {
        this.context = context;
        this.lstChannel = lstChannel;
        this.lstOldCheckedChannel = lstOldCheckedChannel;
    }

    public List<Channel> getCheckedChannel() {
        return lstCheckedChannel;
    }

    @Override
    public int getCount() {
        if (lstChannel != null) {
            return lstChannel.size();
        }
        return 0;
    }

    @Override
    public Channel getItem(int i) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final TextView textView;
        if (view == null) {
            textView = new TextView(context);
            textView.setLayoutParams(new GridView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setTextColor(Color.rgb(126, 126, 126));
            textView.setTextSize(15);
            view = textView;
            view.setTag(textView);
        } else {
            textView = (TextView) view.getTag();
        }
        textView.setText(lstChannel.get(i).getChannelName());
        //判断是否包含上次已选的条件
        if (lstOldCheckedChannel.size() > 0 && lstOldCheckedChannel.contains(lstChannel.get(i))) {
            setCheckedTextViewColor(textView);
            lstChannel.get(i).setChecked(true);
            lstCheckedChannel.add(lstChannel.get(i));
        }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lstChannel.get(i).isChecked()) {
                    lstChannel.get(i).setChecked(false);
                    setUnCheckedTextViewColor(textView);
                    lstCheckedChannel.remove(lstChannel.get(i));
                } else {
                    lstChannel.get(i).setChecked(true);
                    setCheckedTextViewColor(textView);
                    lstCheckedChannel.add(lstChannel.get(i));
                }
            }
        });
        return view;
    }

    private void setCheckedTextViewColor(TextView tv) {
        tv.setTextColor(Color.rgb(251, 109, 109));
    }

    private void setUnCheckedTextViewColor(TextView tv) {
        tv.setTextColor(Color.rgb(126, 126, 126));
    }
}
