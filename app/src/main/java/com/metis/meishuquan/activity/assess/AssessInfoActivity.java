package com.metis.meishuquan.activity.assess;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.model.BLL.CommonOperator;
import com.metis.meishuquan.model.assess.Assess;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.SupportStepTypeEnum;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

public class AssessInfoActivity extends FragmentActivity {

    private Button btnBack;

    private TextView tvName, tvGrade, tvType, tvPublishTime, tvAssessState, tvContent, tvSupportCount, tvCommentCount, tvAddOne;
    private SmartImageView imgPortrait, imgContent;
    private LinearLayout llSupport, llComment;
    private ImageView imgSupport;
    private boolean isPressSupport = false;
    private boolean isPressComment = false;

    private Assess assess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assess_info);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            assess = (Assess) bundle.getSerializable("assess");
        }
        initView();
        bindData(assess);
        initEvent();
    }

    private void initView() {
        this.btnBack = (Button) this.findViewById(R.id.id_btn_assess_info_back);
        this.tvName = (TextView) this.findViewById(R.id.id_username);
        this.tvGrade = (TextView) this.findViewById(R.id.id_tv_grade);
        this.tvType = (TextView) this.findViewById(R.id.id_tv_content_type);
        this.tvPublishTime = (TextView) this.findViewById(R.id.id_createtime);
        this.tvAssessState = (TextView) this.findViewById(R.id.id_tv_comment_state);
        this.tvContent = (TextView) this.findViewById(R.id.id_tv_content);
        this.tvSupportCount = (TextView) this.findViewById(R.id.id_tv_support_count);
        this.tvCommentCount = (TextView) this.findViewById(R.id.id_tv_comment_count);
        this.imgPortrait = (SmartImageView) this.findViewById(R.id.id_img_portrait);
        this.imgContent = (SmartImageView) this.findViewById(R.id.id_img_content);
        this.tvAddOne = (TextView) this.findViewById(R.id.id_tv_add_one);

        this.llSupport = (LinearLayout) this.findViewById(R.id.id_ll_support);
        this.llComment = (LinearLayout) this.findViewById(R.id.id_ll_comment_count);
        this.imgSupport = (ImageView) this.findViewById(R.id.id_img_assess_support);
    }

    private void initEvent() {
        //返回
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //赞
        this.llSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                support();
            }
        });

        //评论
        this.llComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void support() {
        if (!MainApplication.isLogin()) {
            startActivity(new Intent(AssessInfoActivity.this, LoginActivity.class));
            return;
        }
        //赞+1动画
        int count = assess.getSupportCount();
        Object supportCount = tvSupportCount.getTag();
        if (supportCount != null) {
            int temp = (int) supportCount;
            if (temp == count + 1) {
                Toast.makeText(MainApplication.UIContext, "已赞", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Animation animation = AnimationUtils.loadAnimation(AssessInfoActivity.this, R.anim.support_add_one);
        tvAddOne.setVisibility(View.VISIBLE);
        tvAddOne.startAnimation(animation);
        int addCount = count + 1;
        tvSupportCount.setText("(" + addCount + ")");
        tvSupportCount.setTag(count + 1);
        tvSupportCount.setTextColor(Color.RED);
        imgSupport.setImageDrawable(getResources().getDrawable(R.drawable.icon_support));
        new Handler().postDelayed(new Runnable() {
            public void run() {
                tvAddOne.setVisibility(View.GONE);
            }
        }, 1000);

        //后台执行赞操作
        CommonOperator.getInstance().supportOrStep(MainApplication.userInfo.getUserId(), assess.getId(), SupportStepTypeEnum.Assess, 1, new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                    Log.i("supportOrStep", "赞成功");
                } else if (result != null) {
                    Log.i("Error:", result.getMessage());
                } else {
                    exception.printStackTrace();
                }
            }
        });
    }

    private void bindData(Assess assess) {
        if (assess == null) {
            Toast.makeText(this, "加载失败,请重试", Toast.LENGTH_SHORT).show();
            return;
        }
        //姓名
        this.tvName.setText(assess.getUser().getName().isEmpty() ? "" : assess.getUser().getName().trim());
        //头像
        this.imgPortrait.setImageUrl(assess.getUser().getAvatar(), R.drawable.default_user_dynamic);
        //年级
        this.tvGrade.setText(assess.getUser().getGrade().isEmpty() ? "" : assess.getUser().getGrade());
        //点评状态
        if (assess.getCommentCount() > 0) {
            this.tvAssessState.setText("已点评");
        } else {
            this.tvAssessState.setText("未点评");
        }
        //类型
        this.tvType.setText(assess.getAssessChannel().getChannelName().isEmpty() ? "" : assess.getAssessChannel().getChannelName());
        //创建时间
        this.tvPublishTime.setText(assess.getCreateTime());
        //正文内容
        this.tvContent.setText(assess.getDesc());
        //图片
        this.imgContent.setMinimumHeight(assess.getThumbnails().getHeigth() * 2);
        this.imgContent.setMinimumWidth(assess.getThumbnails().getWidth() * 2);
        this.imgContent.setImageUrl(assess.getThumbnails().getUrl());
        //赞数量和评论数量
        this.tvSupportCount.setText("赞(" + assess.getSupportCount() + ")");
        this.tvCommentCount.setText("评论(" + assess.getCommentCount() + ")");
    }
}
