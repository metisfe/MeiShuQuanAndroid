package com.metis.meishuquan.view.circle.moment.comment;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;


public class EmotionSelectionGridItem extends LinearLayout
{
    ImageView imageViewEmotion;
    String emotionName;

    public EmotionSelectionGridItem(Context context)
    {
        super(context);
        this.init(context);
    }

    public EmotionSelectionGridItem(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.init(context);
    }

    public void setEmotionName(String name)
    {
        this.emotionName = name;
        update();
    }

    private void update()
    {
        int id = CommentUtility.EmotionIds.get(this.emotionName);
        imageViewEmotion.setImageResource(id);
    }

    protected void init(Context context)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.view_comment_emotionselectiongriditem, this);
        DisplayMetrics displayMetrics = MainApplication.getDisplayMetrics();
        float density = displayMetrics.density;
        rootView.setLayoutParams(new GridView.LayoutParams((int) (CommentUtility.EmotionIconSize * density), (int) (CommentUtility.EmotionIconSize * density)));

        imageViewEmotion = (ImageView) rootView.findViewById(R.id.view_comment_emotionselectiongriditem_image);
    }
}