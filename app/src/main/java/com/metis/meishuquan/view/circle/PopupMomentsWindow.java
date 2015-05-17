package com.metis.meishuquan.view.circle;

import android.content.Context;
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
public class PopupMomentsWindow extends RelativeLayout {
    private ArrowView arrowView;
    private TextView tv1, tv2, tv3, tv4, tv5, tv6;

    public PopupMomentsWindow(Context context, final OnClickListener onFinish, final OnClickListener onTV1Click, final OnClickListener onTV2Click, final OnClickListener onTV3Click, final OnClickListener onTV4Click, final OnClickListener onTV5Click, final OnClickListener onTV6Click) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.view_circle_popupmomentswindow, this);
        this.tv1 = (TextView) this.findViewById(R.id.view_circle_popupaddwindow_tv1);
        this.tv2 = (TextView) this.findViewById(R.id.view_circle_popupaddwindow_tv2);
        this.tv3 = (TextView) this.findViewById(R.id.view_circle_popupaddwindow_tv3);
        this.tv4 = (TextView) this.findViewById(R.id.view_circle_popupaddwindow_tv4);
        this.tv5 = (TextView) this.findViewById(R.id.view_circle_popupaddwindow_tv5);
        this.tv6 = (TextView) this.findViewById(R.id.view_circle_popupaddwindow_tv6);
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

        tv4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish.onClick(null);
                onTV4Click.onClick(null);
            }
        });

        tv5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish.onClick(null);
                onTV5Click.onClick(null);
            }
        });

        tv5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish.onClick(null);
                onTV5Click.onClick(null);
            }
        });

        tv6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish.onClick(null);
                onTV5Click.onClick(null);
            }
        });
    }
}
