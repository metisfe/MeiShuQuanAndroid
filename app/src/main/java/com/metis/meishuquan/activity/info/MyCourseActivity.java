package com.metis.meishuquan.activity.info;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.course.CourseInfoActivity;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.Item;
import com.metis.meishuquan.util.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

public class MyCourseActivity extends DataListActivity {

    private static final String TAG = MyCourseActivity.class.getSimpleName();

    private List<Item> mData = new ArrayList<Item>();
    private CourseAdapter mAdapter = null;

    private int mIndex = 1;

    @Override
    public String getTitleText() {
        return getString(R.string.my_info_classes);
    }

    @Override
    public void loadData(final int index) {
        UserInfoOperator.getInstance().getCourseList(MainApplication.userInfo.getUserId() + "",
                index, new UserInfoOperator.OnGetListener<List<Item>>() {
                    @Override
                    public void onGet(boolean succeed, List<Item> objects) {
                        if (!succeed) {
                            return;
                        }
                        mIndex = index;
                        if (mAdapter == null) {
                            mAdapter = new CourseAdapter();
                            setAdapter(mAdapter);
                        }

                        if (index == 1) {
                            onRefreshComplete();
                            mData.clear();
                        }
                        onLoadMoreComplete();
                        mData.addAll(objects);
                        notifyDataSetChanged();
                        if (objects != null) {
                            Log.v(TAG, "getFavoriteList objects.size=" + objects.size());
                        } else {
                            Toast.makeText(MyCourseActivity.this, "MyCourseActivity load data failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onLoadMore() {
        loadData(mIndex + 1);
    }

    @Override
    public void onRefresh() {
        loadData(1);
    }

    private class CourseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Item getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            final Item item = getItem(position);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(MyCourseActivity.this).inflate(R.layout.fragment_class_course_list_item, null);
                holder.imgSmall = (ImageView) convertView.findViewById(R.id.id_img_class);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.id_tv_title);
                holder.tvSourse = (TextView) convertView.findViewById(R.id.id_tv_source);
                holder.tvReadCount = (TextView) convertView.findViewById(R.id.id_tv_read_count);
                holder.tvCommentCount = (TextView) convertView.findViewById(R.id.id_tv_comment_count);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tvTitle.setText(item.getTitle());
            holder.tvSourse.setText(item.getSource());
            holder.tvCommentCount.setText(getString(R.string.my_course_comment_count, item.getCommentCount()));
            holder.tvReadCount.setText(getString(R.string.my_course_read_count, item.getPageViewCount()));
            ImageLoaderUtils.getImageLoader(MyCourseActivity.this)
                    .displayImage(item.getImage(), holder.imgSmall, ImageLoaderUtils.getNormalDisplayOptions(R.drawable.ic_launcher));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyCourseActivity.this, CourseInfoActivity.class);
                    intent.putExtra("courseId", item.getId());
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    private static class ViewHolder {
        ImageView imgSmall;
        TextView tvTitle, tvSourse, tvReadCount, tvCommentCount;
    }
}
