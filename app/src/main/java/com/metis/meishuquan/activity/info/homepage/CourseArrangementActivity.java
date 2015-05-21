package com.metis.meishuquan.activity.info.homepage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.model.BLL.StudioBaseInfo;
import com.metis.meishuquan.model.BLL.StudioOperator;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.CourseArrangeInfo;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.model.course.CourseInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CourseArrangementActivity extends BaseActivity {

    private static final String TAG = CourseArrangementActivity.class.getSimpleName();

    private int mUserId = 0;

    private List<CourseInfoDelegate> mDataList = new ArrayList<CourseInfoDelegate>();

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
                    //CourseArrangeInfo info = courseArrangeInfos.get(0);
                    mDataList.clear();
                    List<CourseInfoDelegate> delegates = new ArrayList<CourseInfoDelegate>();
                    for (CourseArrangeInfo d : courseArrangeInfos) {
                        delegates.add(new CourseInfoDelegate(d));
                    }
                    mDataList.addAll(delegates);
                    mAdapter.notifyDataSetChanged();
                    //Log.v(TAG, "getCourseArrangeList day = " + info.getCourseEndDate());
                }
            }
        });
    }

    private class CourseInfoDelegate {

        CourseArrangeInfo mInfo = null;
        boolean showDetails = false;

        CourseInfoDelegate (CourseArrangeInfo info) {
            mInfo = info;
        }
    }

    private SimpleDateFormat mFormat = new SimpleDateFormat("MM");

    private class ArrangeAdapter extends BaseAdapter {

        public ArrangeAdapter () {

        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public CourseInfoDelegate getItem(int i) {
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
                holder.arrow = (ImageView)view.findViewById(R.id.arrow);
                view.setTag(holder);
            } else {
                holder = (ViewHolder)view.getTag();
            }
            final CourseInfoDelegate delegate = getItem(i);
            CourseArrangeInfo info = getItem(i).mInfo;
            holder.titleView.setText(getString(R.string.month, mFormat.format(info.getCourseBeginDate())));
            holder.subTitleView.setText(info.getCourseName());
            holder.detailsView.setText(info.getCourseInfo());
            holder.detailsView.setVisibility(delegate.showDetails ? View.VISIBLE : View.GONE);
            holder.arrow.setRotation(delegate.showDetails ? 180 : 0);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delegate.showDetails = !delegate.showDetails;
                    notifyDataSetChanged();
                }
            });
            return view;
        }
    }

    private class ViewHolder {
        TextView titleView, subTitleView, detailsView;
        ImageView arrow;
    }
}
