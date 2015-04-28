package com.metis.meishuquan.adapter.assess;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.model.assess.Grade;

import java.util.List;

/**
 * Created by wj on 15/4/2.
 */
public class GradeGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<Grade> lstGrade;

    public GradeGridViewAdapter(Context context, List<Grade> lstGrade) {
        this.context = context;
        this.lstGrade = lstGrade;
    }

    public void setData(List<Grade> lstGrade) {
        this.lstGrade.clear();
        this.lstGrade = lstGrade;
    }

    @Override
    public int getCount() {
        if (lstGrade != null) {
            return lstGrade.size();
        }
        return 0;
    }

    @Override
    public Grade getItem(int i) {
        if (lstGrade != null) {
            return lstGrade.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        if (lstGrade != null) {
            return lstGrade.get(i).getId();
        }
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        TextView textView;
        if (convertView == null) {
            textView = new TextView(context);
            textView.setLayoutParams(new GridView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setTextColor(Color.rgb(126, 126, 126));
            textView.setTextSize(15);
            convertView = textView;
            convertView.setTag(textView);
        } else {
            textView = (TextView) convertView.getTag();
        }
        textView.setText(lstGrade.get(i).getName());
        return convertView;
    }
}
