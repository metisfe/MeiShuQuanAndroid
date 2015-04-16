package com.metis.meishuquan.fragment.course;

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
import android.widget.Button;

import com.google.gson.Gson;
import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.course.CourseListAdapter;
import com.metis.meishuquan.model.BLL.CourseOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.course.Course;
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
    private int index = 1;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Course> result = (List<Course>) msg.obj;
            switch (msg.what) {
                case DragListView.REFRESH:
                    listView.onRefreshComplete();
                    list.clear();
                    list.addAll(result);
                    break;
                case DragListView.LOAD:
                    listView.onLoadComplete();
                    list.addAll(result);
                    break;
            }
            listView.setResultSize(result.size());
            adapter.notifyDataSetChanged();
        }

        ;
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_class_course_list, container, false);

        initData();
        initView(rootView);
        initEvent();

        return rootView;
    }

    private void initData() {
        loadData(DragListView.REFRESH);
    }

    private void loadData(final int what) {
        // 从本地或服务器获取数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage();
                msg.what = what;
                msg.obj = getData();
                handler.sendMessage(msg);
            }
        }).start();
    }

    private List<Course> getData() {
        getData(index, type);
        return new ArrayList<Course>();
    }

    //加载更多
    public void getData(int index, CourseType type) {
        //请求数据
//        SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
//        String json = spu.getStringByKey(SharedPreferencesUtil.COURSE_LIST);
//        if (!json.isEmpty()) {
//            Gson gson= new Gson();
//
//        }
        CourseOperator.getInstance().getCouseList("", type, "", index, new ApiOperationCallback<ReturnInfo<String>>() {

            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                if (result != null && result.getInfo().equals(String.valueOf(0))) {
//                    Message msg = handler.obtainMessage();
//                    msg.what = DragListView.LOAD;
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    Log.i("getCouseList_data", json);
                } else if (result != null && result.getErrorCode().equals(String.valueOf(0))) {
                    Log.e("getCouseList", result.getMessage());
                } else {
                    Log.e("getCousetList", "请求失败");
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
                loadData(DragListView.REFRESH);
            }
        });

        this.listView.setOnLoadListener(new DragListView.OnLoadListener() {
            @Override
            public void onLoad() {

            }
        });


        this.btnrecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonChecked(btnrecommend);
                setButtonUnChecked(new Button[]{btnNewPublish, btnHotCourse});
            }
        });

        this.btnNewPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonChecked(btnNewPublish);
                setButtonUnChecked(new Button[]{btnrecommend, btnHotCourse});
            }
        });

        this.btnHotCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonChecked(btnHotCourse);
                setButtonUnChecked(new Button[]{btnNewPublish, btnrecommend});
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
