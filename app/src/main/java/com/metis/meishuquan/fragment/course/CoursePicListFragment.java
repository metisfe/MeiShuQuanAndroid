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

import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.CourseOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.CourseType;
import com.metis.meishuquan.view.shared.DragListView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.List;

/**
 * Created by wangjin on 15/4/16.
 */
public class CoursePicListFragment extends Fragment {
    private Button btnrecommend, btnNewPublish, btnHotCourse;
    private LinearLayout oneLayout, twoLayout, threeLayout;
    private CourseType courseType = CourseType.Recommend;
    private String[] urls = new String[]{
            "http://img.my.csdn.net/uploads/201309/01/1378037235_3453.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037235_9280.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037234_3539.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037234_6318.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037194_2965.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037193_1687.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037193_1286.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037192_8379.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037178_9374.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037177_1254.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037177_6203.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037152_6352.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037151_9565.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037151_7904.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037148_7104.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037129_8825.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037128_5291.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037128_3531.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037127_1085.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037095_7515.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037094_8001.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037093_7168.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037091_4950.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949643_6410.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949642_6939.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949630_4505.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949630_4593.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949629_7309.jpg",
            "http://img.my.csdn.net/uploads/201308/31/1377949629_8247.jpg",};
    private String tag = "";
    private int index = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_class_img_list, container, false);
        initView(rootView);
        initData();
        initEvent();

        return rootView;
    }

    private void initData() {
        CourseOperator.getInstance().getCourseImgList(tag, courseType, 0, index, new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                
            }
        });

        int j = 0;
        for (int i = 0; i < urls.length; i++) {
            addImgView(urls[i], j);
            j++;
            if (j == 3) {
                j = 0;
            }
        }
    }

    private void initView(ViewGroup rootView) {
        this.btnrecommend = (Button) rootView.findViewById(R.id.id_btn_recommend);
        this.btnNewPublish = (Button) rootView.findViewById(R.id.id_btn_new_publish);
        this.btnHotCourse = (Button) rootView.findViewById(R.id.id_btn_hot_course);
        this.oneLayout = (LinearLayout) rootView.findViewById(R.id.id_ll_one);
        this.twoLayout = (LinearLayout) rootView.findViewById(R.id.id_ll_two);
        this.threeLayout = (LinearLayout) rootView.findViewById(R.id.id_ll_three);
    }

    private void addImgView(String url, int j) {
        SmartImageView imageView = new SmartImageView(getActivity());
        imageView.setImageUrl(url.trim());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
