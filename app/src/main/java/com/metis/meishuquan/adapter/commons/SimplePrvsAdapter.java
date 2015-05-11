package com.metis.meishuquan.adapter.commons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.studio.UserInfoAdapter;
import com.metis.meishuquan.model.BLL.UserInfoOperator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/5/4.
 */
public class SimplePrvsAdapter extends BaseAdapter {

    private static SimplePrvsAdapter sAdapter = null;

    public static SimplePrvsAdapter getInstance (Context context) {
        return getInstance(context, -1);
    }

    public static SimplePrvsAdapter getInstance (Context context, int selected) {
        if (sAdapter == null) {
            sAdapter = new SimplePrvsAdapter(context.getApplicationContext());
        }
        sAdapter.setSelectedOne(selected);
        return sAdapter;
    }

    private List<UserInfoOperator.SimpleProvince> mDataList = new ArrayList<UserInfoOperator.SimpleProvince>();
    private Context mContext = null;
    private int mSelectedOne = -1;
    private OnPrvsItemClickListener mItemListener = null;

    public SimplePrvsAdapter (Context context) {
        mContext = context;
        UserInfoOperator.getInstance().getProvinceList(new UserInfoOperator.OnGetListener<List<UserInfoOperator.SimpleProvince>>() {
            @Override
            public void onGet(boolean succeed, List<UserInfoOperator.SimpleProvince> simpleProvinces) {
                if (succeed) {
                    if (mDataList.isEmpty()) {
                        mDataList.addAll(simpleProvinces);
                        notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void setSelectedOne (int str) {
        mSelectedOne = str;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public UserInfoOperator.SimpleProvince getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_list_dialog_item, null);
        TextView tv = (TextView)convertView.findViewById(R.id.list_dialog_item);
        tv.setText(getItem(i).getName());
        if (getItem(i).getProvinceId() == mSelectedOne) {
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
        public void onItemClick (View view, UserInfoOperator.SimpleProvince province);
    }
}
