package com.metis.meishuquan.view.circle.moment;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.ImagePreviewActivity;
import com.metis.meishuquan.model.circle.CCircleDetailModel;
import com.metis.meishuquan.model.enums.SupportTypeEnum;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.view.circle.moment.comment.EmotionTextView;

import java.util.ArrayList;

/**
 * Created by wangjin on 15/5/22.
 */
public class CircleMomentViewItem extends LinearLayout {

    private LinearLayout rootview;
    private LinearLayout replyCircleContainer;
    private RelativeLayout topBar;
    private RelativeLayout btnMore;
    private EmotionTextView emotionTextView;
    private ImageView imageView;

    private ImageView imgHeader;
    private TextView tvName;
    private TextView tvRole;
    private TextView tvPublishTime;
    private TextView tvSourse;
    private EmotionTextView emotionContent;
    private MomentActionBar momentActionBar;

    private OnClickListener onClickListener;
    private OnClickListener onMoreButtonClickListener;
    private OnClickListener onImageClickListener;
    private OnClickListener onHeaderClickListner;
    private MomentActionBar.OnActionButtonClickListener onActionButtonClickListener;

    public CircleMomentViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initEvent();
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnMoreClickListener(OnClickListener onMoreButtonClickListener) {
        this.onMoreButtonClickListener = onMoreButtonClickListener;
    }

    public void setOnImageClickListener(OnClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    public void setOnHeaderClickListner(OnClickListener onHeaderClickListner) {
        this.onHeaderClickListner = onHeaderClickListner;
    }

    public void setOnActionButtonClickListener(MomentActionBar.OnActionButtonClickListener onActionButtonClickListener) {
        this.onActionButtonClickListener = onActionButtonClickListener;
    }

    private void initView() {
        this.rootview = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_circle_view_moment, this, false);
        this.topBar = (RelativeLayout) rootview.findViewById(R.id.id_rl_topbar);
        this.btnMore = (RelativeLayout) rootview.findViewById(R.id.id_btn_more);

        this.replyCircleContainer = (LinearLayout) rootview.findViewById(R.id.id_ll_reply_circle_type_container);
        this.emotionTextView = (EmotionTextView) rootview.findViewById(R.id.id_emotion_tv_content);
        this.imageView = (ImageView) rootview.findViewById(R.id.id_img_for_circle);

        this.imgHeader = (ImageView) rootview.findViewById(R.id.id_img_portrait);
        this.tvName = (TextView) rootview.findViewById(R.id.id_username);
        this.tvRole = (TextView) rootview.findViewById(R.id.id_tv_grade);
        this.tvPublishTime = (TextView) rootview.findViewById(R.id.id_createtime);
        this.tvSourse = (TextView) rootview.findViewById(R.id.tv_device);
        this.emotionContent = (EmotionTextView) rootview.findViewById(R.id.id_tv_content);

        this.momentActionBar = (MomentActionBar) rootview.findViewById(R.id.moment_action_bar);
    }

    private void initEvent() {
        //进入原帖
        this.replyCircleContainer.setOnClickListener(onClickListener);

        this.btnMore.setOnClickListener(onMoreButtonClickListener);

        //查看大图
        this.imageView.setOnClickListener(onImageClickListener);

        this.imgHeader.setOnClickListener(onHeaderClickListner);

        this.tvName.setOnClickListener(onHeaderClickListner);

    }

    public void bindData(CCircleDetailModel moment) {
        //头像
        ImageLoaderUtils.getImageLoader(getContext()).displayImage(moment.user.avatar, this.imgHeader);
        this.tvName.setText(moment.user.name);
//        tvRole.setText(moment.user.);//用户角色信息
        this.tvPublishTime.setText(Utils.getDisplayTime(moment.createTime));
        this.tvSourse.setText("来自" + moment.device);

        if (moment.relayCircle == null) {//原创类型
            this.replyCircleContainer.setBackgroundColor(getResources().getColor(R.color.white));
            //判断是否含有图片和文字，有则显示，没有则隐藏
            if (moment.images != null && moment.images.size() > 0) {
                this.imageView.setVisibility(View.VISIBLE);
                ImageLoaderUtils.getImageLoader(getContext()).displayImage(moment.images.get(0).Thumbnails, this.imageView);
            } else {
                this.imageView.setVisibility(View.GONE);
            }
            this.emotionContent.setText(moment.content.isEmpty() ? "分享图片" : moment.content);
        } else if (moment.relayCircle != null && moment.relayCircle.type == SupportTypeEnum.Circle.getVal()) {//转发圈子类型
            this.replyCircleContainer.setBackgroundColor(getResources().getColor(R.color.common_color_f1f2f4));
            if (moment.relayCircle.images != null && moment.relayCircle.images.size() > 0) {
                this.imageView.setVisibility(View.VISIBLE);
                ImageLoaderUtils.getImageLoader(getContext()).displayImage(moment.relayCircle.images.get(0).Thumbnails, this.imageView);
            } else {
                this.imageView.setVisibility(View.GONE);
            }
            this.emotionContent.setText(moment.relayCircle.desc.isEmpty() ? "转发微博" : moment.relayCircle.desc);
        }
    }
}
