package com.metis.meishuquan.activity.assess;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.assess.Assess;

public class AssessInfoActivity extends FragmentActivity {

    private Button btnBack;

    private TextView tvName, tvGrade, tvType, tvPublishTime, tvAssessState, tvContent, tvSupportCount, tvCommentCount;
    private SmartImageView imgPortrait, imgContent;

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
    }

    private void initEvent() {
        //返回
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
