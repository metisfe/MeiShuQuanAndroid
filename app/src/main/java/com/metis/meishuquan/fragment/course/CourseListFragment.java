package com.metis.meishuquan.fragment.course;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.course.CourseInfoActivity;
import com.metis.meishuquan.adapter.course.CourseListAdapter;
import com.metis.meishuquan.model.BLL.CourseOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.course.Course;
import com.metis.meishuquan.model.course.CourseData;
import com.metis.meishuquan.model.enums.CourseType;
import com.metis.meishuquan.view.shared.DragListView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjin on 15/4/16.
 */
public class CourseListFragment extends Fragment {
    private Button btnrecommend, btnNewPublish, btnHotCourse;
    private DragListView listView;

    private CourseListAdapter adapter;
    private List<Course> list = new ArrayList<Course>();
    private CourseType type = CourseType.Recommend;
    private static int index = 1;
    private String tags = "";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_class_course_list, container, false);

        initData();
        initView(rootView);
        initEvent();

        return rootView;
    }

    private void initData() {
        index = 1;
        getData(null, index, type, DragListView.REFRESH);
    }

    //刷新或加载更多
    public void getData(String tags, final int index, CourseType type, final int what) {
        //请求数据
        CourseOperator.getInstance().getCouseList(tags, type, "", index, new ApiOperationCallback<ReturnInfo<String>>() {

            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    Log.i("getCourseList_data", json);
                    CourseData data = gson.fromJson(json, new TypeToken<CourseData>() {
                    }.getType());
                    if (what == DragListView.REFRESH) {
                        listView.onRefreshComplete();
                        list.clear();
                        list.addAll(data.getData());
                    } else if (what == DragListView.LOAD) {
                        if (data.getData().size() == 30) {
                            CourseListFragment.index++;
                        }
                        listView.onLoadComplete();
                        list.addAll(data.getData());
                    }
                    listView.setResultSize(data.getData().size());
                    adapter.notifyDataSetChanged();
                } else if (result != null && result.getInfo().equals(String.valueOf(1))) {
                    Log.e("getCourseList_data", result.getMessage());
                } else {
                    Log.e("getCourseList_data", "请求失败");
                }
            }
        });
    }

    private void initView(ViewGroup rootView) {
        this.btnrecommend = (Button) rootView.findViewById(R.id.id_btn_recommend);
        this.btnNewPublish = (Button) rootView.findViewById(R.id.id_btn_new_publish);
        this.btnHotCourse = (Button) rootView.findViewById(R.id.id_btn_hot_course);
        this.listView = (DragListView) rootView.findViewById(R.id.id_listview_course);

        //初始化成员
        adapter = new CourseListAdapter(getActivity(), list);
        this.listView.setAdapter(adapter);
    }

    private void initEvent() {
        this.listView.setOnRefreshListener(new DragListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(tags, 1, type, DragListView.REFRESH);
            }
        });

        this.listView.setOnLoadListener(new DragListView.OnLoadListener() {
            @Override
            public void onLoad() {
                getData(tags, index, type, DragListView.LOAD);
            }
        });

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Course course = list.get(i);
                if (course != null) {
                    Intent intent = new Intent(getActivity(), CourseInfoActivity.class);
                    intent.putExtra("courseId", course.getCourseId());
                    getActivity().startActivity(intent);
                }

            }
        });

        this.btnrecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonChecked(btnrecommend);
                setButtonUnChecked(new Button[]{btnNewPublish, btnHotCourse});
                type = CourseType.Recommend;
                getData(tags, 1, type, DragListView.REFRESH);
            }
        });

        this.btnNewPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonChecked(btnNewPublish);
                setButtonUnChecked(new Button[]{btnrecommend, btnHotCourse});
                type = CourseType.NewPublish;
                getData(tags, 1, type, DragListView.REFRESH);
            }
        });

        this.btnHotCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonChecked(btnHotCourse);
                setButtonUnChecked(new Button[]{btnNewPublish, btnrecommend});
                type = CourseType.HotCourse;
                getData(tags, 1, type, DragListView.REFRESH);
            }
        });
    }

    private void setButtonChecked(Button btn) {
        btn.setTextColor(Color.rgb(251, 109, 109));
    }

    private void setButtonUnChecked(Button[] btns) {
        for (Button button : btns) {
            button.setTextColor(Color.rgb(126, 126, 126));
        }
    }
}
