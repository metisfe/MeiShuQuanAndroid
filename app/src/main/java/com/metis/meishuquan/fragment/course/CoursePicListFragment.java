package com.metis.meishuquan.fragment.course;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.CourseOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.course.CourseImg;
import com.metis.meishuquan.model.enums.CourseType;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjin on 15/4/16.
 */
public class CoursePicListFragment extends Fragment {
    private Button btnRecommend, btnNewPublish, btnHotCourse;
    private LinearLayout oneLayout, twoLayout, threeLayout;
    private CourseType orderType = CourseType.Recommend;
    private List<CourseImg> lstCourseImg = new ArrayList<CourseImg>();
    private String tag = "";
    private int index = 1;
    private int type = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            tag = bundle.getString("tags");
        }
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_class_img_list, container, false);
        initView(rootView);
        initData(tag, orderType, type, index);
        initEvent();

        return rootView;
    }

    private void initData(String tag, CourseType orderType, int type, int index) {
        CourseOperator.getInstance().getCourseImgList(tag, orderType, type, index, new ApiOperationCallback<ReturnInfo<CourseImg>>() {
            @Override
            public void onCompleted(ReturnInfo<CourseImg> result, Exception exception, ServiceFilterResponse response) {
                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    ReturnInfo returnInfo = gson.fromJson(json, new TypeToken<ReturnInfo<List<CourseImg>>>() {
                    }.getType());
                    List<CourseImg> data = (List<CourseImg>) returnInfo.getData();
                    if (data == null || data.size() == 0) {
                        return;
                    }
                    lstCourseImg.addAll(data);

                    int j = 0;
                    for (int i = 0; i < lstCourseImg.size(); i++) {
                        addImgView(lstCourseImg.get(i), j);
                        j++;
                        if (j == 3) {
                            j = 0;
                        }
                    }
                }
            }
        });
    }

    private void initView(ViewGroup rootView) {
        this.btnRecommend = (Button) rootView.findViewById(R.id.id_btn_recommend);
        this.btnNewPublish = (Button) rootView.findViewById(R.id.id_btn_new_publish);
        this.btnHotCourse = (Button) rootView.findViewById(R.id.id_btn_hot_course);
        this.oneLayout = (LinearLayout) rootView.findViewById(R.id.id_ll_one);
        this.twoLayout = (LinearLayout) rootView.findViewById(R.id.id_ll_two);
        this.threeLayout = (LinearLayout) rootView.findViewById(R.id.id_ll_three);
    }

    private void addImgView(CourseImg courseImg, int j) {
        ImageView imageView = new ImageView(getActivity());
        ImageLoaderUtils.getImageLoader(getActivity()).displayImage(courseImg.getThumbnails(), imageView, ImageLoaderUtils.getNormalDisplayOptions(R.drawable.img_topline_default));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        int width = courseImg.getWidth() * 2;
        int height = courseImg.getHeight() * 2;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        imageView.setLayoutParams(lp);
        imageView.setPadding(5, 5, 5, 5);

        if (j == 0) {
            oneLayout.addView(imageView);
        } else if (j == 1) {
            twoLayout.addView(imageView);
        } else if (j == 2) {
            threeLayout.addView(imageView);
        }
    }

    private void initEvent() {
        this.btnRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonChecked(btnRecommend);
                setButtonUnChecked(new Button[]{btnNewPublish, btnHotCourse});

                removeAllPhoto();
                orderType = CourseType.Recommend;
                initData(tag, orderType, type, index);
            }
        });

        this.btnNewPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonChecked(btnNewPublish);
                setButtonUnChecked(new Button[]{btnRecommend, btnHotCourse});

                removeAllPhoto();
                orderType = CourseType.NewPublish;
                initData(tag, orderType, type, index);
            }
        });

        this.btnHotCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonChecked(btnHotCourse);
                setButtonUnChecked(new Button[]{btnNewPublish, btnRecommend});

                removeAllPhoto();
                orderType = CourseType.HotCourse;
                initData(tag, orderType, type, index);
            }
        });
    }

    private void removeAllPhoto() {
        oneLayout.removeAllViews();
        twoLayout.removeAllViews();
        threeLayout.removeAllViews();
        lstCourseImg.clear();
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
