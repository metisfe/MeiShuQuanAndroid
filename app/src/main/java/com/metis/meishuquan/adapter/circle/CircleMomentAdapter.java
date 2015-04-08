package com.metis.meishuquan.adapter.circle;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.assess.Assess;
import com.metis.meishuquan.model.circle.CircleMoment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CircleMomentAdapter extends BaseAdapter {
    private List<CircleMoment> momentList = new ArrayList<CircleMoment>();
    private ViewHolder holder;

    public CircleMomentAdapter(List<CircleMoment> momentList) {
        this.momentList = momentList;
    }

    private class ViewHolder {
        //TODO
    }

    @Override
    public int getCount() {
        return momentList.size();
    }

    @Override
    public CircleMoment getItem(int i) {
        return momentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup view) {
        convertView = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_circle_moment_list_item, null);
        return convertView;
    }
}