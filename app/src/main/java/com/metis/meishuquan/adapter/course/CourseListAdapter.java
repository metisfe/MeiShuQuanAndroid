package com.metis.meishuquan.adapter.course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.image.SmartImage;
import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.course.Course;
import com.metis.meishuquan.model.topline.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoxiao on 15/4/17.
 */
public class CourseListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Course> lstData = new ArrayList<Course>();
    private Context mContext;

    private ViewHolder holder;

    public CourseListAdapter(Context context, List<Course> lstData) {
        this.mContext = context;
        this.lstData = lstData;
        this.mInflater = LayoutInflater.from(context);
    }

    private static class ViewHolder {
        SmartImageView imgSmall;
        TextView tvTitle, tvSourse, tvReadCount, tvCommentCount;
    }

    @Override
    public int getCount() {
        return lstData.size();
    }

    @Override
    public Course getItem(int i) {
        return lstData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lstData.get(i).getCourseId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.fragment_class_course_list_item, null, false);

            holder.imgSmall = (SmartImageView) convertView.findViewById(R.id.id_img_class);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.id_tv_title);
            holder.tvSourse = (TextView) convertView.findViewById(R.id.id_tv_source);
            holder.tvReadCount = (TextView) convertView.findViewById(R.id.id_tv_read_count);
            holder.tvCommentCount = (TextView) convertView.findViewById(R.id.id_tv_comment_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imgSmall.setImageUrl(lstData.get(i).getCoursePic());
        holder.tvTitle.setText(lstData.get(i).getTitle().trim());
        holder.tvSourse.setText(lstData.get(i).getAuthor() != null ? lstData.get(i).getAuthor().getName() : "");
        holder.tvReadCount.setText("阅读(" + lstData.get(i).getViewCount() + ")");
        holder.tvCommentCount.setText("评论(" + lstData.get(i).getCommentCount() + ")");

        return convertView;
    }
}
