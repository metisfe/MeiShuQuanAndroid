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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wj on 15/4/2.
 */
public class GradeGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<Grade> lstGrade;
    public List<Grade> lstCheckedGrade = new ArrayList<Grade>();
    public List<Grade> lstOldCheckedGrade = new ArrayList<Grade>();

    public GradeGridViewAdapter(Context context, List<Grade> lstGrade, List<Grade> lstOldCheckedGrade) {
        this.context = context;
        this.lstGrade = lstGrade;
        this.lstOldCheckedGrade = lstOldCheckedGrade;
    }

    public List<Grade> getCheckedGrade() {
        return lstCheckedGrade;
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
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        final TextView textView;
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
        //判断是否包含上次已选的条件
        if (lstOldCheckedGrade.size() > 0 && lstOldCheckedGrade.contains(lstGrade.get(i))) {
            setCheckedTextViewColor(textView);
            lstGrade.get(i).setChecked(true);
            lstCheckedGrade.add(lstGrade.get(i));
        }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lstGrade.get(i).isChecked()) {
                    lstGrade.get(i).setChecked(false);
                    setUnCheckedTextViewColor(textView);
                    lstCheckedGrade.remove(lstGrade.get(i));
                } else {
                    lstGrade.get(i).setChecked(true);
                    setCheckedTextViewColor(textView);
                    lstCheckedGrade.add(lstGrade.get(i));
                }
            }
        });
        return convertView;
    }

    private void setCheckedTextViewColor(TextView tv) {
        tv.setTextColor(Color.rgb(251, 109, 109));
    }

    private void setUnCheckedTextViewColor(TextView tv) {
        tv.setTextColor(Color.rgb(126, 126, 126));
    }
}
