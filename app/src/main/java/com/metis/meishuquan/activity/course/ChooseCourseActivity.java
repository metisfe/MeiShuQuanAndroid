package com.metis.meishuquan.activity.course;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.CourseOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.course.Course;
import com.metis.meishuquan.model.course.CourseChannel;
import com.metis.meishuquan.model.course.CourseChannelData;
import com.metis.meishuquan.model.course.CourseChannelItem;
import com.metis.meishuquan.model.topline.ChannelItem;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.view.course.FlowLayout;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.ArrayList;
import java.util.List;

public class ChooseCourseActivity extends FragmentActivity {

    private Button btnBack, btnConfirm;
    private RelativeLayout rlAllChannel;
    private ListView listView;
    private CourseAdapter adapter;
    private List<CourseChannel> lstCourseChannel = new ArrayList<CourseChannel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getData();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_course);

        initView();
        initEvent();
    }

    private void initView() {
        this.btnBack = (Button) this.findViewById(R.id.id_btn_back);
        this.btnConfirm = (Button) this.findViewById(R.id.id_btn_confirm);
        this.rlAllChannel = (RelativeLayout) this.findViewById(R.id.id_rl_all_channel);
        this.listView = (ListView) this.findViewById(R.id.id_course_listview);

        adapter = new CourseAdapter(ChooseCourseActivity.this, lstCourseChannel);
        listView.setAdapter(adapter);
    }

    private void initEvent() {
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("tags", adapter.getSelectedChannels());
                ChooseCourseActivity.this.setResult(RESULT_OK, intent);
                finish();
            }
        });

        this.rlAllChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getData() {
        String json = SharedPreferencesUtil.getInstanse(this).getStringByKey(SharedPreferencesUtil.COURSECHANNELLIST);
        Gson gson = new Gson();
        CourseChannelData data = gson.fromJson(json, new TypeToken<CourseChannelData>() {
        }.getType());
        lstCourseChannel = data.getData();
    }

    class CourseAdapter extends BaseAdapter {

        private List<CourseChannel> mData;
        private List<CourseChannelItem> mCheckedItems = new ArrayList<CourseChannelItem>();
        private Context mContext;

        CourseAdapter(Context context, List<CourseChannel> mData) {
            this.mContext = context;
            this.mData = mData;
        }

        public String[] getSelectedChannels() {
            if (mCheckedItems.size() == 0) {
                return null;
            }
            String[] str = new String[mCheckedItems.size()];
            for (int i = 0; i < mCheckedItems.size(); i++) {
                str[i] = mCheckedItems.get(i).getChannelId();
            }
            return str;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public CourseChannel getItem(int i) {
            return mData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return mData.get(i).getChannelId();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                List<CourseChannelItem> mDataItem = mData.get(i).getChildChannelLists();
                view = LayoutInflater.from(mContext).inflate(R.layout.activity_class_choose_list_child_item, null);
                RelativeLayout rlCourseChannel = (RelativeLayout) view.findViewById(R.id.id_rl_main);
                TextView tvChannel = (TextView) view.findViewById(R.id.id_tv_channel_name);
                ImageView imgArrow = (ImageView) view.findViewById(R.id.id_img_arrow);
                final FlowLayout flowLayout = (FlowLayout) view.findViewById(R.id.id_fl_channels);

                tvChannel.setText(mData.get(i).getChannelName());

                addCourseChannelItem(flowLayout, mDataItem);
                rlCourseChannel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (flowLayout.getVisibility() == View.GONE) {
                            flowLayout.setVisibility(View.VISIBLE);
                        } else {
                            flowLayout.setVisibility(View.GONE);
                        }
                    }
                });
            }
            return view;
        }

        private void addCourseChannelItem(FlowLayout flowLayout, List<CourseChannelItem> courseChannelItems) {
            for (final CourseChannelItem item : courseChannelItems) {
                final Button button = (Button) getLayoutInflater().inflate(R.layout.choose_course_button, null).findViewById(R.id.id_btn_choose_course);
                button.setText(item.getChannelName());
                button.setTag(item);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CourseChannelItem item = (CourseChannelItem) button.getTag();
                        if (!item.isChecked()) {
                            item.setChecked(true);
                            button.setTextColor(Color.rgb(251, 109, 109));
                            mCheckedItems.add(item);
                        } else {
                            item.setChecked(false);
                            button.setTextColor(Color.rgb(126, 126, 126));
                            if (mCheckedItems.contains(item)) {
                                mCheckedItems.remove(item);
                            }
                        }
                    }
                });

                flowLayout.addView(button);
            }
        }
    }
}
