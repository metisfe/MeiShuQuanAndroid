package com.metis.meishuquan.view.circle.moment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.metis.meishuquan.R;

/**
 * Created by jx on 4/7/2015.
 */
public class MomentActionBar extends LinearLayout {
    private LinearLayout repostActionLayout, commentActionLayout, likeActionLayout;
    private TextView tvRepostCount, tvCommentCount, tvLikeCount;
    private OnActionButtonClickListener onActionButtonClickListener;

    public interface OnActionButtonClickListener
    {
        public void onComment();
        public void onLike();
    }

    public MomentActionBar(Context context) {
        super(context);
        init();
    }

    public MomentActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnActionButtonClickListener(OnActionButtonClickListener onActionButtonClickListener)
    {
        this.onActionButtonClickListener = onActionButtonClickListener;
    }

    public void setData(int repostCount, int commentCount, int likeCount)
    {
        this.tvRepostCount.setText(repostCount > 0 ? "" + repostCount : "");
        this.tvCommentCount.setText(commentCount > 0 ? "" + commentCount : "");
        this.tvLikeCount.setText(likeCount > 0 ? "" + likeCount : "");
    }

    public void init()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.fragment_circle_moment_action_bar, this);
        repostActionLayout = (LinearLayout) this.findViewById(R.id.action_repost);
        commentActionLayout = (LinearLayout) this.findViewById(R.id.action_comment);
        likeActionLayout = (LinearLayout) this.findViewById(R.id.action_like);

        tvRepostCount = (TextView) this.findViewById(R.id.tv_repost_count);
        tvCommentCount = (TextView) this.findViewById(R.id.tv_comment_count);
        tvLikeCount = (TextView) this.findViewById(R.id.tv_like_count);

        commentActionLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onActionButtonClickListener != null)
                {
                    onActionButtonClickListener.onComment();
                }
            }
        });

        likeActionLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onActionButtonClickListener != null)
                {
                    onActionButtonClickListener.onLike();
                }
            }
        });
    }
}
