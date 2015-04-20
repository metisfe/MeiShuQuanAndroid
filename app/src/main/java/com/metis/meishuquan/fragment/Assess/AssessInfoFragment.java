package com.metis.meishuquan.fragment.assess;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.AssessOperator;
import com.metis.meishuquan.model.assess.Assess;

/**
 * Fragment:问题详情
 * Created by wj on 15/4/2.
 */
public class AssessInfoFragment extends Fragment {

    private FragmentManager fm;
    private Button btnBack;

    private TextView tvName, tvGrade, tvType, tvPublishTime, tvAssessState, tvContent, tvSupportCount, tvStepCount;
    private SmartImageView imgPortrait, imgContent;

    private Assess assess;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_assess_info, null);
        if (getArguments() != null) {
            assess = (Assess) getArguments().getSerializable("assess");
        }
        initView(rootView);
        initEvent();
        return rootView;
    }

    private void initView(ViewGroup rootView) {
        fm = getActivity().getSupportFragmentManager();
        this.btnBack = (Button) rootView.findViewById(R.id.id_btn_assess_info_back);
        this.tvName = (TextView) rootView.findViewById(R.id.id_username);
        this.tvGrade = (TextView) rootView.findViewById(R.id.id_tv_grade);
        this.tvType = (TextView) rootView.findViewById(R.id.id_tv_content_type);
        this.tvPublishTime = (TextView) rootView.findViewById(R.id.id_createtime);
        this.tvAssessState = (TextView) rootView.findViewById(R.id.id_tv_comment_state);
        this.tvContent = (TextView) rootView.findViewById(R.id.id_tv_content);
        this.tvSupportCount = (TextView) rootView.findViewById(R.id.id_tv_support_count);
        this.tvStepCount = (TextView) rootView.findViewById(R.id.id_tv_comment_count);
        this.imgPortrait = (SmartImageView) rootView.findViewById(R.id.id_img_portrait);
        this.imgContent = (SmartImageView) rootView.findViewById(R.id.id_img_content);
    }

    private void initEvent() {
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(AssessInfoFragment.this);
                ft.commit();
            }
        });
    }

    private void getData() {
//        AssessOperator.getInstance().
    }
}
