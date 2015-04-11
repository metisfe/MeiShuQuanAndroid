package com.metis.meishuquan.view.circle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.R;

/**
 * Created by wudi on 4/11/2015.
 */
public class ContactListItemView extends LinearLayout {
    private View checkView;
    private SmartImageView smartImageView;
    private TextView nameView, reasonView, addedView, buttonView;

    public ContactListItemView(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_circle_contactlistitemview, this);
        this.checkView = (View) this.findViewById(R.id.view_circle_contactlistitemview_check);
        this.smartImageView = (SmartImageView) this.findViewById(R.id.view_circle_contactlistitemview_image);
        this.nameView = (TextView) this.findViewById(R.id.view_circle_contactlistitemview_name);
        this.reasonView = (TextView) this.findViewById(R.id.view_circle_contactlistitemview_reason);
        this.addedView = (TextView) this.findViewById(R.id.view_circle_contactlistitemview_added);
        this.buttonView = (TextView) this.findViewById(R.id.view_circle_contactlistitemview_confirm);
    }

    public void setCheckMode(String title, String url, int checkStatus) {
        this.checkView.setVisibility(VISIBLE);
        switch (checkStatus) {
            case 0:
                this.checkView.setBackgroundResource(R.drawable.view_circle_checkcircle_uncheck);
                break;
            case 1:
                this.checkView.setBackgroundResource(R.drawable.view_circle_checkcircle_check);
                break;
            case -1:
                this.checkView.setBackgroundResource(R.drawable.view_circle_checkcircle_disable);
                break;
        }

        this.nameView.setText(title);
        this.smartImageView.setImageUrl(url);

        this.buttonView.setVisibility(GONE);
        this.addedView.setVisibility(GONE);
        this.reasonView.setVisibility(GONE);
    }

    public void setNormalMode(String title, String url) {
        this.nameView.setText(title);
        this.smartImageView.setImageUrl(url);

        this.checkView.setVisibility(GONE);
        this.buttonView.setVisibility(GONE);
        this.addedView.setVisibility(GONE);
        this.reasonView.setVisibility(GONE);
    }

    public void setAcceptMode(String title, String url, boolean mode) {
        this.nameView.setText(title);
        this.smartImageView.setImageUrl(url);

        this.checkView.setVisibility(GONE);
        this.reasonView.setVisibility(GONE);
        this.addedView.setVisibility(mode ? VISIBLE : GONE);
        this.buttonView.setVisibility(mode ? GONE : VISIBLE);
        this.buttonView.setText("接受");
    }

    public void setRequestMode(String title, String url, String reason, boolean mode) {
        this.nameView.setText(title);
        this.smartImageView.setImageUrl(url);

        this.checkView.setVisibility(GONE);
        this.reasonView.setVisibility(VISIBLE);
        this.reasonView.setText(reason);
        this.addedView.setVisibility(mode ? VISIBLE : GONE);
        this.buttonView.setVisibility(mode ? GONE : VISIBLE);
        this.buttonView.setText("增加");
    }
}
