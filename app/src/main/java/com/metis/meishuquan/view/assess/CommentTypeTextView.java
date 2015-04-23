package com.metis.meishuquan.view.assess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.model.assess.AssessComment;
import com.metis.meishuquan.ui.display.SquareRoundDisplayer;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Created by wangjin on 15/4/23.
 */
public class CommentTypeTextView extends RelativeLayout {
    private Context context;
    private AssessComment assessComment;

    private ImageView imgPortrait;
    private TextView tvCommentUser, tvReply, tvReplyUser, tvContent, tvCommentTime;


    public void setAssessComment(AssessComment assessComment) {
        this.assessComment = assessComment;
        initData(assessComment);
    }

    public CommentTypeTextView(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_assess_reply_comment_type_text, this);
        initView(this);
        intEvent();
    }

    private void initView(CommentTypeTextView commentTypeTextView) {
        this.imgPortrait = (ImageView) commentTypeTextView.findViewById(R.id.id_img_comment_user_portrait);
        this.tvCommentUser = (TextView) commentTypeTextView.findViewById(R.id.id_tv_comment_user_name);
        this.tvReply = (TextView) commentTypeTextView.findViewById(R.id.id_tv_reply);
        this.tvReplyUser = (TextView) commentTypeTextView.findViewById(R.id.id_tv_reply_user_name);
        this.tvContent = (TextView) commentTypeTextView.findViewById(R.id.id_tv_comment_content);
        this.tvCommentTime = (TextView) commentTypeTextView.findViewById(R.id.id_tv_comment_createtime);
    }

    private void initData(AssessComment assessComment) {
        if (assessComment != null) {
            //评论人头像
            if (assessComment.getUser() != null) {

                ImageLoaderUtils.getImageLoader(this.context).
                        displayImage(assessComment.getUser().getAvatar(),
                                this.imgPortrait,
                                ImageLoaderUtils.getRoundDisplayOptions(R.dimen.user_portrait_height, R.drawable.default_user_dynamic));
            }
            //评论人昵称
            this.tvCommentUser.setText(assessComment.getUser().getName());
            if (assessComment.getReplyUser() == null || assessComment.getReplyUser().getName().isEmpty()) {
                this.tvReply.setVisibility(View.GONE);
                this.tvReplyUser.setVisibility(View.GONE);
            }
            //被回复人昵称
            this.tvReplyUser.setText(assessComment.getReplyUser().getName());
            //评论内容
            this.tvContent.setText(assessComment.getContent());
            //评论时间
            this.tvCommentTime.setText(assessComment.getCommentDateTime());
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
