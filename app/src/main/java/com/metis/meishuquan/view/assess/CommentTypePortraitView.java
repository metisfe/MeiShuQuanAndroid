package com.metis.meishuquan.view.assess;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.metis.meishuquan.R;
import com.metis.meishuquan.model.commons.SimpleUser;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.view.course.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论带语音视图
 * <p/>
 * Created by wangjin on 15/4/23.
 */
public class CommentTypePortraitView extends RelativeLayout {

    private FlowLayout flImgs;

    private List<ImageView> lstImageViews;
    private Context context;


    public void setAssessComment(List<SimpleUser> supportUser) {
        initData(supportUser);
    }

    public CommentTypePortraitView(Context context) {
        super(context);
        this.context = context;
        this.lstImageViews = new ArrayList<ImageView>();
        LayoutInflater.from(context).inflate(R.layout.layout_assess_comment_type_support_portrait, this);
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

    private void initView(CommentTypePortraitView view) {
        this.flImgs = (FlowLayout) view.findViewById(R.id.id_flow_user_portrait);
    }

    private void initData(final List<SimpleUser> supportUser) {
        for (int i = 0; i < supportUser.size(); i++) {
            final SimpleUser simpleUser = supportUser.get(i);
            ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.layout_imageview_user_portrait, null).findViewById(R.id.id_img_user_portrait);
            imageView.setMaxWidth(30);
            imageView.setMaxHeight(30);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoaderUtils.getImageLoader(context).displayImage(supportUser.get(i).getAvatar(), imageView);

            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "进入" + simpleUser.getName() + "的个人主页", Toast.LENGTH_SHORT).show();
                }
            });

            this.flImgs.addView(imageView);
            this.lstImageViews.add(imageView);
        }
    }

    private void intEvent() {

    }
}
