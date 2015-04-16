package com.metis.meishuquan.adapter.course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.metis.meishuquan.model.course.Course;
import com.metis.meishuquan.model.topline.News;

import java.util.List;

/**
 * Created by xiaoxiao on 15/4/17.
 */
public class CourseListAdapter extends BaseAdapter {

    private int resourseId;
    private LayoutInflater mInflater;
    private List<Course> lstData;
    private Context mContext;

    private ViewHolder holder;

    public CourseListAdapter(Context context, List<Course> lstData) {
        this.mContext = context;
        this.lstData = lstData;
        this.mInflater = LayoutInflater.from(context);
    }

    private static class ViewHolder {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
