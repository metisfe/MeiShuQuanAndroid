package com.metis.meishuquan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.enums.ProvinceEnum;

/**
 * Created by WJ on 2015/5/12.
 */
public class PrvcEnumAdapter extends BaseAdapter{

    private ProvinceEnum[] enums = ProvinceEnum.values();

    @Override
    public int getCount() {
        return enums.length;
    }

    @Override
    public ProvinceEnum getItem(int i) {
        return enums[i];
    }

    @Override
    public long getItemId(int i) {
        return enums[i].getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.act_list_spinner_item, null);
        }
        TextView tv = (TextView)view.findViewById(R.id.item_text);
        tv.setText(getItem(i).getName());
        return view;
    }


}
