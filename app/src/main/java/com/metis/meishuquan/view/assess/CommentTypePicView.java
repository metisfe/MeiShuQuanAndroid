package com.metis.meishuquan.view.assess;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.assess.AssessComment;
import com.metis.meishuquan.util.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论带语音视图
 * <p/>
 * Created by wangjin on 15/4/23.
 */
public class CommentTypePicView extends RelativeLayout {

    private LinearLayout llImgs;
    private ImageView imgPortrait;
    private TextView tvCommentUser, tvReply, tvReplyUser, tvContent, tvCommentTime;


    private List<ImageView> lstImageViews = new ArrayList<ImageView>();
    private AssessComment assessComment;
    private Context context;
    private boolean windowAttached;


    public void setAssessComment(AssessComment assessComment) {
        this.assessComment = assessComment;
        initData(assessComment);
    }

    public CommentTypePicView(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_assess_reply_comment_type_pic, this);
        initView(this);
        intEvent();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (ImageView imageView : lstImageViews) {
            if (imageView != null && imageView.getDrawable() != null) {
                ((BitmapDrawable) imageView.getDrawable()).getBitmap().recycle();
                imageView.setImageDrawable(null);
            }
        }
        lstImageViews = null;
    }

    private void initView(CommentTypePicView commentTypePicView) {
        this.imgPortrait = (ImageView) commentTypePicView.findViewById(R.id.id_img_comment_user_portrait);
        this.tvCommentUser = (TextView) commentTypePicView.findViewById(R.id.id_tv_comment_user_name);
        this.tvReply = (TextView) commentTypePicView.findViewById(R.id.id_tv_reply);
        this.tvReplyUser = (TextView) commentTypePicView.findViewById(R.id.id_tv_reply_user_name);
        this.tvCommentTime = (TextView) commentTypePicView.findViewById(R.id.id_tv_comment_createtime);
        this.llImgs = (LinearLayout) commentTypePicView.findViewById(R.id.id_ll_imgs);
    }

    private void initData(AssessComment assessComment) {
        llImgs.removeAllViews();
        if (assessComment.getUser() != null) {

            ImageLoaderUtils.getImageLoader(this.context).
                    displayImage(assessComment.getUser().getAvatar(),
                            this.imgPortrait,
                            ImageLoaderUtils.getRoundDisplayOptions(this.context.getResources().getDimensionPixelSize(R.dimen.user_portrait_height), R.drawable.default_user_dynamic));

            this.tvCommentUser.setText(assessComment.getUser().getName());//评论人
            if (assessComment.getReplyUser() == null || assessComment.getReplyUser().getName().isEmpty()) {
                this.tvReply.setVisibility(View.GONE);
                this.tvReplyUser.setVisibility(View.GONE);
            }
            this.tvReplyUser.setText(assessComment.getReplyUser().getName());//被回复人
            this.tvCommentTime.setText(assessComment.getCommentDateTime());//评论时间

            int imgCount = assessComment.getImgOrVoiceUrl().size();
            for (int i = 0; i < imgCount; i++) {
                ImageView imageView = new SmartImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


                int width = assessComment.getImgOrVoiceUrl().get(i).getThumbnailsWidth();
                int height = assessComment.getImgOrVoiceUrl().get(i).getThumbnailsHeight();
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width * 2, height * 2);
                lp.setMargins(5, 5, 5, 5);
                imageView.setLayoutParams(lp);

                ImageLoaderUtils.getImageLoader(context).displayImage(assessComment.getImgOrVoiceUrl().get(i).getThumbnails(), imageView);

                this.llImgs.addView(imageView);
                this.lstImageViews.add(imageView);
            }
        }
    }

    private void intEvent() {
        this.tvCommentUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        this.tvReplyUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
