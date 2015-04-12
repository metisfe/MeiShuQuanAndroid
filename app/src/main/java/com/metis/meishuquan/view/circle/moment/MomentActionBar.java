package com.metis.meishuquan.view.circle.moment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.metis.meishuquan.R;

/**
 * Created by jx on 4/7/2015.
 */
public class MomentActionBar extends LinearLayout {
    private LinearLayout repostActionLayout, commentActionLayout, likeActionLayout;

    public MomentActionBar(Context context) {
        super(context);
        init();
    }

    public MomentActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.fragment_circle_moment_action_bar, this);
        repostActionLayout = (LinearLayout) this.findViewById(R.id.action_repost);
        commentActionLayout = (LinearLayout) this.findViewById(R.id.action_comment);
        likeActionLayout = (LinearLayout) this.findViewById(R.id.action_like);
    }
}
