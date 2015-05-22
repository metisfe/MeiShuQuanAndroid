package com.metis.meishuquan.view.circle.moment;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.ImagePreviewActivity;
import com.metis.meishuquan.model.circle.CCircleDetailModel;
import com.metis.meishuquan.model.contract.Moment;
import com.metis.meishuquan.model.enums.SupportTypeEnum;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.view.circle.moment.comment.EmotionTextView;

import java.util.ArrayList;

/**
 * Created by wangjin on 15/5/22.
 */
public class CircleReplyMomentViewItem extends LinearLayout {

    private LinearLayout rootview;
    private LinearLayout replyContainer;
    private RelativeLayout topBar;
    private RelativeLayout btnMore;

    private EmotionTextView emotionTextView;
    private ImageView imageView;
    private TextView tvTitle;
    private TextView tvDesc;

    private ImageView imgHeader;
    private ImageView imgIsTop;
    private ImageView imgChooseHuaShi;
    private ImageView imgAttention;
    private TextView tvName;
    private TextView tvRole;
    private TextView tvPublishTime;
    private TextView tvSourse;
    private EmotionTextView emotionContent;

    public MomentActionBar momentActionBar;

    private OnClickListener onClickListener;
    private OnClickListener onMoreBtnClickListener;
    private OnClickListener onChooseHuaShiListener;
    private OnClickListener onAttentionListener;
    private OnClickListener onReplyClickListner;
    private MomentActionBar.OnActionButtonClickListener onActionButtonClickListener;

    public CircleReplyMomentViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initEvent();
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnMoreBtnClickListener(OnClickListener onMoreBtnClickListener) {
        this.onMoreBtnClickListener = onMoreBtnClickListener;
    }

    public void setOnChooseHuaShiListener(OnClickListener onChooseHuaShiListener) {
        this.onChooseHuaShiListener = onChooseHuaShiListener;
    }

    public void setOnAttentionListener(OnClickListener onAttentionListener) {
        this.onAttentionListener = onAttentionListener;
    }

    public void setOnReplyClickListner(OnClickListener onReplyClickListner) {
        this.onReplyClickListner = onReplyClickListner;
    }

    public void setOnActionButtonClickListener(MomentActionBar.OnActionButtonClickListener onActionButtonClickListener) {
        this.onActionButtonClickListener = onActionButtonClickListener;
    }

    private void initView() {
        this.rootview = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_circle_view_reply_moment, this, false);

        this.topBar = (RelativeLayout) rootview.findViewById(R.id.id_rl_topbar);
        this.imageView = (ImageView) rootview.findViewById(R.id.id_img_top);
        this.btnMore = (RelativeLayout) rootview.findViewById(R.id.id_btn_more);

        this.replyContainer = (LinearLayout) rootview.findViewById(R.id.id_ll_reply_container);
        this.emotionTextView = (EmotionTextView) rootview.findViewById(R.id.id_emotion_tv_reply_content);
        this.imageView = (ImageView) rootview.findViewById(R.id.id_img_for_reply);
        this.imgChooseHuaShi = (ImageView) rootview.findViewById(R.id.id_img_choose_huashi);
        this.imgAttention = (ImageView) rootview.findViewById(R.id.id_img_attention);
        this.tvTitle = (TextView) rootview.findViewById(R.id.id_tv_title);
        this.tvDesc = (TextView) rootview.findViewById(R.id.id_tv_desc);

        this.imgHeader = (ImageView) rootview.findViewById(R.id.id_img_portrait);
        this.tvName = (TextView) rootview.findViewById(R.id.id_username);
        this.tvRole = (TextView) rootview.findViewById(R.id.id_tv_grade);
        this.tvPublishTime = (TextView) rootview.findViewById(R.id.id_createtime);
        this.tvSourse = (TextView) rootview.findViewById(R.id.tv_device);
        this.emotionContent = (EmotionTextView) rootview.findViewById(R.id.id_tv_content);

        this.momentActionBar = (MomentActionBar) rootview.findViewById(R.id.moment_action_bar);
    }

    private void initEvent() {
        //进入详情
        this.rootview.setOnClickListener(onClickListener);

        //删除
        this.btnMore.setOnClickListener(onMoreBtnClickListener);

        //选画室
        this.imgChooseHuaShi.setOnClickListener(onChooseHuaShiListener);

        //关注
        this.imgAttention.setOnClickListener(onAttentionListener);

        //进入活动或文章类型详情
        this.replyContainer.setOnClickListener(onReplyClickListner);
    }

    public void bindData(CCircleDetailModel moment) {
        //头像
        ImageLoaderUtils.getImageLoader(getContext()).displayImage(moment.user.avatar, this.imgHeader);
        this.tvName.setText(moment.user.name);
//        tvRole.setText(moment.user.);//用户角色信息
        this.tvPublishTime.setText(Utils.getDisplayTime(moment.createTime));
        this.tvSourse.setText("来自" + moment.device);

        if (moment.relayCircle != null) {
            if (moment.relayCircle.type == SupportTypeEnum.CircleActivity.getVal()) {//活动转发类型
                //不显示选画室按钮
                this.imgChooseHuaShi.setVisibility(View.GONE);
            } else if (moment.relayCircle.type == SupportTypeEnum.ActivityStudent.getVal() && moment.relayCircle.type == SupportTypeEnum.News.getVal()) {//活动类型
                if (moment.user.userId == MainApplication.userInfo.getUserId()) {
                    this.imgChooseHuaShi.setVisibility(View.VISIBLE);
                    this.replyContainer.setVisibility(View.VISIBLE);
                } else {
                    this.imgChooseHuaShi.setVisibility(View.GONE);
                    this.replyContainer.setVisibility(View.GONE);
                }
            }
            //缩略图
            ImageLoaderUtils.getImageLoader(getContext()).displayImage(moment.relayCircle.activityImg, this.imageView);
            //标题
            this.tvTitle.setText(moment.relayCircle.title);
            //描述
            this.tvDesc.setText(moment.relayCircle.desc);
            //转发描述
            this.emotionContent.setText(moment.content);
        }
    }
}
