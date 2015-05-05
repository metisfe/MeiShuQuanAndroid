package com.metis.meishuquan.adapter.commons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metis.meishuquan.R;

/**
 * Created by WJ on 2015/5/4.
 */
public class SimplePrvsAdapter extends BaseAdapter {

    private static SimplePrvsAdapter sAdapter = null;

    public static SimplePrvsAdapter getInstance (Context context) {
        return getInstance(context, null);
    }

    public static SimplePrvsAdapter getInstance (Context context, String selected) {
        if (sAdapter == null) {
            sAdapter = new SimplePrvsAdapter(context.getApplicationContext());
        }
        sAdapter.setSelectedOne(selected);
        return sAdapter;
    }

    private Context mContext = null;
    private String[] mArray = null;
    private String mSelectedOne = null;
    private OnPrvsItemClickListener mItemListener = null;

    public SimplePrvsAdapter (Context context) {
        mContext = context;
        mArray = context.getResources().getStringArray(R.array.act_filter_3);
    }

    public void setSelectedOne (String str) {
        mSelectedOne = str;
    }

    @Override
    public int getCount() {
        return mArray.length;
    }

    @Override
    public String getItem(int i) {
        return mArray[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_list_dialog_item, null);
        TextView tv = (TextView)convertView.findViewById(R.id.list_dialog_item);
        tv.setText(getItem(i));
        if (getItem(i).equals(mSelectedOne)) {
            tv.setTextColor(mContext.getResources().getColor(R.color.text_red));
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemListener != null) {
                    mItemListener.onItemClick(view, getItem(i));
                }
            }
        });
        return convertView;
    }

    public void setOnItemClickListener (OnPrvsItemClickListener listener) {
        mItemListener = listener;
    }

    public static interface OnPrvsItemClickListener {
        public void onItemClick (View view, String name);
    }
}
