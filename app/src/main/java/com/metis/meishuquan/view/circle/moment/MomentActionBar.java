package com.metis.meishuquan.view.circle.moment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.metis.meishuquan.R;

/**
 * Created by jx on 4/7/2015.
 */
public class MomentActionBar extends LinearLayout {
    public LinearLayout repostActionLayout, commentActionLayout, likeActionLayout;
    public TextView tvRepostCount, tvCommentCount, tvLikeCount, tvAddOne;
    public ImageView imgSupport;
    public OnActionButtonClickListener onActionButtonClickListener;

    private ImageView imgTab1, imgTab2, imgTab3;

    public interface OnActionButtonClickListener {
        public void onReply();

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

    public void setOnActionButtonClickListener(OnActionButtonClickListener onActionButtonClickListener) {
        this.onActionButtonClickListener = onActionButtonClickListener;
    }

    public void setData(int repostCount, int commentCount, int likeCount) {
        this.tvRepostCount.setText(repostCount > 0 ? "" + repostCount : "");
        this.tvCommentCount.setText(commentCount > 0 ? "" + commentCount : "");
        this.tvLikeCount.setText(likeCount > 0 ? "(" + likeCount + ")" : "");
    }

    public void setCheck() {
        this.imgSupport.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_support));
        this.tvLikeCount.setTextColor(Color.RED);
    }

    public void setUncheck() {
        this.imgSupport.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_unsupport));
        this.tvLikeCount.setTextColor(getResources().getColor(R.color.common_color_7e7e7e));
    }

    public void showTab(int index) {
        switch (index) {
            case 0:
                this.imgTab1.setVisibility(View.VISIBLE);
                this.imgTab2.setVisibility(View.GONE);
                this.imgTab3.setVisibility(View.GONE);
                break;
            case 1:
                this.imgTab1.setVisibility(View.GONE);
                this.imgTab2.setVisibility(View.VISIBLE);
                this.imgTab3.setVisibility(View.GONE);
                break;
            case 2:
                this.imgTab1.setVisibility(View.GONE);
                this.imgTab2.setVisibility(View.GONE);
                this.imgTab3.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.fragment_circle_moment_action_bar, this);
        repostActionLayout = (LinearLayout) this.findViewById(R.id.action_repost);
        commentActionLayout = (LinearLayout) this.findViewById(R.id.action_comment);
        likeActionLayout = (LinearLayout) this.findViewById(R.id.action_like);

        tvRepostCount = (TextView) this.findViewById(R.id.tv_repost_count);
        tvCommentCount = (TextView) this.findViewById(R.id.tv_comment_count);
        tvLikeCount = (TextView) this.findViewById(R.id.tv_like_count);
        tvAddOne = (TextView) this.findViewById(R.id.id_tv_add_one);

        imgSupport = (ImageView) this.findViewById(R.id.id_img_support);

        imgTab1 = (ImageView) this.findViewById(R.id.id_tab_icon_1);
        imgTab2 = (ImageView) this.findViewById(R.id.id_tab_icon_2);
        imgTab3 = (ImageView) this.findViewById(R.id.id_tab_icon_3);

        commentActionLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onActionButtonClickListener != null) {
                    onActionButtonClickListener.onComment();
                }
            }
        });

        likeActionLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onActionButtonClickListener != null) {
                    onActionButtonClickListener.onLike();
                }
            }
        });

        repostActionLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onActionButtonClickListener != null) {
                    onActionButtonClickListener.onReply();
                }
            }
        });
    }
}
