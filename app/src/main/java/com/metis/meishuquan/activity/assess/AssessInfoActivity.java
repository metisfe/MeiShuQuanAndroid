package com.metis.meishuquan.activity.assess;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.model.BLL.AssessOperator;
import com.metis.meishuquan.model.BLL.CommonOperator;
import com.metis.meishuquan.model.assess.Assess;
import com.metis.meishuquan.model.assess.AssessComment;
import com.metis.meishuquan.model.assess.AssessSupportAndComment;
import com.metis.meishuquan.model.commons.SimpleUser;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.CommentTypeEnum;
import com.metis.meishuquan.model.enums.SupportStepTypeEnum;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.ArrayList;
import java.util.List;

public class AssessInfoActivity extends FragmentActivity {

    private String TAG = "getAssessSupportAndComment";
    private Button btnBack;

    private TextView tvName, tvGrade, tvType, tvPublishTime, tvAssessState, tvContent, tvSupportCount, tvCommentCount, tvAddOne;
    private SmartImageView imgPortrait, imgContent;
    private LinearLayout llSupport, llComment;
    private ImageView imgSupport;
    private ListView listView;
    private View headerView;

    private Assess assess;
    private AssessSupportAndComment assessSupportAndComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assess_info);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            assess = (Assess) bundle.getSerializable("assess");
            assessSupportAndComment = (AssessSupportAndComment) bundle.getSerializable("assessSupportAndComment");
        }
        initView();
        addHeaderView();
        initHeaderView();
        bindData(assess);
        initHeaderEvent();
        initEvent();

        String[] strs = new String[10];

        for (int i = 0; i < 10; i++) {
            strs[i] = "data-----" + i;
        }
        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, strs));
    }

    private void addHeaderView() {
        headerView = View.inflate(this, R.layout.view_assess_info_header, null);
        listView.addHeaderView(headerView);
    }

    private void initHeaderView() {
        this.tvName = (TextView) headerView.findViewById(R.id.id_username);
        this.tvGrade = (TextView) headerView.findViewById(R.id.id_tv_grade);
        this.tvType = (TextView) headerView.findViewById(R.id.id_tv_content_type);
        this.tvPublishTime = (TextView) headerView.findViewById(R.id.id_createtime);
        this.tvAssessState = (TextView) headerView.findViewById(R.id.id_tv_comment_state);
        this.tvContent = (TextView) headerView.findViewById(R.id.id_tv_content);
        this.tvSupportCount = (TextView) headerView.findViewById(R.id.id_tv_support_count);
        this.tvCommentCount = (TextView) headerView.findViewById(R.id.id_tv_comment_count);
        this.imgPortrait = (SmartImageView) headerView.findViewById(R.id.id_img_portrait);
        this.imgContent = (SmartImageView) headerView.findViewById(R.id.id_img_content);
        this.tvAddOne = (TextView) headerView.findViewById(R.id.id_tv_add_one);

        this.llSupport = (LinearLayout) headerView.findViewById(R.id.id_ll_support);
        this.llComment = (LinearLayout) headerView.findViewById(R.id.id_ll_comment_count);
        this.imgSupport = (ImageView) headerView.findViewById(R.id.id_img_assess_support);
    }

    private void initHeaderEvent() {
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

    private void initView() {
        this.btnBack = (Button) this.findViewById(R.id.id_btn_assess_info_back);
        this.listView = (ListView) this.findViewById(R.id.id_list_assess_info_comment);
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
        tvSupportCount.setText("赞(" + addCount + ")");
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

    private class AssessInfoAdapter extends BaseAdapter {
        private int PORTRAIT_MAX_COUNT = 7;
        private int TYPE_COUNT = 4;
        private int portraitLines;
        private int current_type;
        private List<CommentTypeEnum> commentTypes;

        private Context mContext;
        private AssessSupportAndComment assessSupportAndComment;
        private List<SimpleUser> lstSupportUser = new ArrayList<SimpleUser>();
        private List<AssessComment> lstAssessComment = new ArrayList<AssessComment>();

        public AssessInfoAdapter(Context mContext, AssessSupportAndComment assessSupportAndComment) {
            this.mContext = mContext;
            this.assessSupportAndComment = assessSupportAndComment;
            if (assessSupportAndComment != null) {
                lstSupportUser = assessSupportAndComment.getSupportUserList();
                lstAssessComment = assessSupportAndComment.getAssessCommentList();
            }
        }

        public List<CommentTypeEnum> getCommentTypes() {
            commentTypes = new ArrayList<CommentTypeEnum>();
            int supportCount = lstSupportUser.size();
            portraitLines = supportCount % PORTRAIT_MAX_COUNT == 0 ? supportCount / PORTRAIT_MAX_COUNT : supportCount / PORTRAIT_MAX_COUNT + 1;

            //添加赞类型列表（行数）
            for (int i = 0; i < portraitLines; i++) {
                commentTypes.add(CommentTypeEnum.Portrait);
            }

            //添加回复类型
            for (int i = 0; i < lstAssessComment.size(); i++) {
                int type = lstAssessComment.get(i).getCommentType();
                if (type == CommentTypeEnum.Text.getVal()) {
                    commentTypes.add(CommentTypeEnum.Text);
                } else if (type == CommentTypeEnum.Image.getVal()) {
                    commentTypes.add(CommentTypeEnum.Image);
                } else if (type == CommentTypeEnum.Voice.getVal()) {
                    commentTypes.add(CommentTypeEnum.Voice);
                }
            }
            return commentTypes;
        }


        @Override
        public int getViewTypeCount() {
            return TYPE_COUNT;
        }

        @Override
        public int getItemViewType(int position) {
            commentTypes = getCommentTypes();
            if (commentTypes.get(position) == CommentTypeEnum.Portrait) {
                return CommentTypeEnum.Portrait.getVal();
            } else if (commentTypes.get(position) == CommentTypeEnum.Text) {
                return CommentTypeEnum.Text.getVal();
            } else if (commentTypes.get(position) == CommentTypeEnum.Image) {
                return CommentTypeEnum.Image.getVal();
            } else if (commentTypes.get(position) == CommentTypeEnum.Voice) {
                return CommentTypeEnum.Text.getVal();
            }
            return 0;
        }

        @Override
        public int getCount() {
            return portraitLines + lstAssessComment.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        class PortraitViewHolder {
            LinearLayout ll_portrait = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_assess_reply_comment_type_pic, null);

        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            current_type = getItemViewType(i);

            if (current_type ==CommentTypeEnum.Portrait.getVal())
                if (view == null) {

                }

            return null;
        }
    }
}
