package com.metis.meishuquan.view.circle;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.view.shared.ArrowView;

/**
 * Created by wudi on 4/12/2015.
 */
public class PopupAddWindow extends RelativeLayout {
    private ArrowView arrowView;
    private TextView tv1, tv2, tv3;

    public PopupAddWindow(Context context, final View.OnClickListener onFinish, final View.OnClickListener onTV1Click, final View.OnClickListener onTV2Click, final View.OnClickListener onTV3Click) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.view_circle_popupaddwindow, this);
        this.tv1 = (TextView) this.findViewById(R.id.view_circle_popupaddwindow_tv1);
        this.tv2 = (TextView) this.findViewById(R.id.view_circle_popupaddwindow_tv2);
        this.tv3 = (TextView) this.findViewById(R.id.view_circle_popupaddwindow_tv3);
        this.arrowView = (ArrowView) this.findViewById(R.id.view_circle_popupaddwindow_arrow);
        int arrowWidth = getResources().getDimensionPixelOffset(R.dimen.view_circle_popupaddwindow_arrow_width);
        int arrowHeight = getResources().getDimensionPixelOffset(R.dimen.view_circle_popupaddwindow_arrow_height);

        arrowView.SetData(new Point(0, arrowHeight), new Point(arrowWidth, arrowHeight), new Point(arrowWidth / 2, 0), getResources().getColor(R.color.black));
        this.setOnClickListener(onFinish);

        tv1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish.onClick(null);
                onTV1Click.onClick(null);
            }
        });

        tv2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish.onClick(null);
                onTV2Click.onClick(null);
            }
        });

        tv3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish.onClick(null);
                onTV3Click.onClick(null);
            }
        });
    }
}
