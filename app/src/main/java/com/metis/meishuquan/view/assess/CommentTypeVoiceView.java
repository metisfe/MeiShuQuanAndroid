package com.metis.meishuquan.view.assess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.R;
import com.metis.meishuquan.manager.common.PlayerManager;
import com.metis.meishuquan.model.assess.AssessComment;
import com.metis.meishuquan.util.ImageLoaderUtils;

/**
 * 评论带语音视图
 * <p/>
 * Created by wangjin on 15/4/23.
 */
public class CommentTypeVoiceView extends RelativeLayout {
    private Context context;
    private AssessComment assessComment;

    private ImageView imgPortrait;
    private TextView tvCommentUser, tvReply, tvReplyUser, tvContent, tvCommentTime;
    private Button btnPlayVoice;


    public void setAssessComment(AssessComment assessComment) {
        this.assessComment = assessComment;
        initData(assessComment);
    }

    public CommentTypeVoiceView(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_assess_reply_comment_type_voice, this);
        initView(this);
        intEvent();
    }

    private void initView(CommentTypeVoiceView commentTypeVoiceView) {
        this.imgPortrait = (ImageView) commentTypeVoiceView.findViewById(R.id.id_img_comment_user_portrait);
        this.tvCommentUser = (TextView) commentTypeVoiceView.findViewById(R.id.id_tv_comment_user_name);
        this.tvReply = (TextView) commentTypeVoiceView.findViewById(R.id.id_tv_reply);
        this.tvReplyUser = (TextView) commentTypeVoiceView.findViewById(R.id.id_tv_reply_user_name);
        this.tvContent = (TextView) commentTypeVoiceView.findViewById(R.id.id_tv_comment_content);
        this.tvCommentTime = (TextView) commentTypeVoiceView.findViewById(R.id.id_tv_comment_createtime);
        this.btnPlayVoice = (Button) commentTypeVoiceView.findViewById(R.id.id_btn_play_voice);
    }

    private void initData(AssessComment assessComment) {
        if (assessComment != null) {
            ImageLoaderUtils.getImageLoader(this.context).
                    displayImage(assessComment.getUser().getAvatar(),
                            this.imgPortrait,
                            ImageLoaderUtils.getRoundDisplayOptions(R.dimen.user_portrait_height));

            this.tvCommentUser.setText(assessComment.getUser().getName());//评论人
            if (assessComment.getReplyUser() == null || assessComment.getReplyUser().getName().isEmpty()) {
                this.tvReply.setVisibility(View.GONE);
                this.tvReplyUser.setVisibility(View.GONE);
            }
            this.tvReplyUser.setText(assessComment.getReplyUser().getName());//被回复人
            this.tvCommentTime.setText(assessComment.getCommentDateTime());//评论时间
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

        //播放语音
        this.btnPlayVoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (assessComment.getImgOrVoiceUrl() == null || assessComment.getImgOrVoiceUrl().size() == 0) {
                    Toast.makeText(context, "播放失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = assessComment.getImgOrVoiceUrl().get(0).getVoiceUrl();
                if (!url.isEmpty()) {
                    PlayerManager.getInstance(context).start(url);
                }
            }
        });
    }
}
