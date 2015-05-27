package com.metis.meishuquan.view.circle;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.circle.CUserModel;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;

/**
 * Created by wudi on 4/7/2015.
 */
public class CircleGridIcon extends RelativeLayout {
    private ImageView imageView;
    private TextView textView;
    private View deleteIcon;
    private Context context;
    public int type;

    public CircleGridIcon(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(getContext()).inflate(R.layout.view_circle_circlegridicon, this);
        this.imageView = (ImageView) this.findViewById(R.id.view_circle_circlegridicon_image);
        this.textView = (TextView) this.findViewById(R.id.view_circle_circlegridicon_text);
        this.deleteIcon = this.findViewById(R.id.view_circle_circlegridicon_deleteicon);
    }

    public void setData(String url, String name, final String uid) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.default_portrait_fang);
        } else {
            ImageLoaderUtils.getImageLoader(context).displayImage(url, imageView, ImageLoaderUtils.getRoundDisplayOptions(getResources().getDimensionPixelSize(R.dimen.user_portrait_height), R.drawable.default_portrait_fang));
        }
        textView.setText(name);
    }

    public void setPlusMinus(boolean plus) {
        this.imageView.setImageResource(plus ? R.drawable.view_circle_plus : R.drawable.view_circle_minus);
        this.textView.setText("");
        type = plus ? 1 : 2;
    }

    public void setEditMode(boolean editMode) {
        this.deleteIcon.setVisibility(editMode ? VISIBLE : GONE);
    }
}
