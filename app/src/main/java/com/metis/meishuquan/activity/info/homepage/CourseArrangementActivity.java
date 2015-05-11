package com.metis.meishuquan.activity.info.homepage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.model.BLL.StudioBaseInfo;
import com.metis.meishuquan.model.BLL.StudioOperator;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.CourseArrangeInfo;
import com.metis.meishuquan.model.commons.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CourseArrangementActivity extends BaseActivity {

    private static final String TAG = CourseArrangementActivity.class.getSimpleName();

    private int mUserId = 0;

    private List<CourseArrangeInfo> mDataList = new ArrayList<CourseArrangeInfo>();

    private ListView mArrangeLv = null;

    private ArrangeAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_arrangement);

        mUserId = getIntent().getIntExtra(StudioBaseInfo.KEY_STUDIO_ID, 0);

        mAdapter = new ArrangeAdapter();

        mArrangeLv = (ListView)findViewById(R.id.arrange_list_view);
        mArrangeLv.setAdapter(mAdapter);
    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.studio_course_arrangement);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        StudioOperator.getInstance().getCourseArrangeList(mUserId, 0, new UserInfoOperator.OnGetListener<List<CourseArrangeInfo>>() {
            @Override
            public void onGet(boolean succeed, List<CourseArrangeInfo> courseArrangeInfos) {
                if (succeed) {
                    CourseArrangeInfo info = courseArrangeInfos.get(0);
                    mDataList.clear();
                    mDataList.addAll(courseArrangeInfos);
                    mAdapter.notifyDataSetChanged();
                    Log.v(TAG, "getCourseArrangeList day = " + info.getCourseEndDate());
                }
            }
        });
    }

    private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");

    private class ArrangeAdapter extends BaseAdapter {

        public ArrangeAdapter () {

        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public CourseArrangeInfo getItem(int i) {
            return mDataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(CourseArrangementActivity.this).inflate(R.layout.layout_course_arrange_item, null);
                holder = new ViewHolder ();
                holder.titleView = (TextView)view.findViewById(R.id.item_title);
                holder.subTitleView = (TextView)view.findViewById(R.id.item_sub_title);
                holder.detailsView = (TextView)view.findViewById(R.id.item_details);
                view.setTag(holder);
            } else {
                holder = (ViewHolder)view.getTag();
            }
            holder.titleView.setText(mFormat.format(getItem(i).getCourseBeginDate()) + "-" + mFormat.format(getItem(i).getCourseEndDate()));
            holder.subTitleView.setText(getItem(i).getCourseName());
            holder.detailsView.setText(getItem(i).getCourseInfo());
            return view;
        }
    }

    private class ViewHolder {
        TextView titleView, subTitleView, detailsView;
    }
}
