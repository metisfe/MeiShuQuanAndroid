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
public class ConstellationAdapter extends BaseAdapter {

    private static ConstellationAdapter sAdapter = null;

    public static ConstellationAdapter getInstance (Context context, String selected) {
        if (sAdapter == null) {
            sAdapter = new ConstellationAdapter(context.getApplicationContext());
        }
        sAdapter.setSelected(selected);
        return sAdapter;
    }

    public static ConstellationAdapter getInstance (Context context) {
        return getInstance(context, null);
    }

    private Context mContext = null;
    private String[] mArray = null;
    private String mSelectedOne = null;
    private OnItemClickListener mItemListener = null;

    public ConstellationAdapter (Context context) {
        mContext = context;
        mArray = mContext.getResources().getStringArray(R.array.constellation);
    }

    public void setSelected (String str) {
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

    public void setOnItemClickListener (OnItemClickListener listener) {
        mItemListener = listener;
    }

    public static interface OnItemClickListener {
        public void onItemClick (View view, String name);
    }
}
