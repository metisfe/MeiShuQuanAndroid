package com.metis.meishuquan.view.circle;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.circle.CircleAttentionAdapter;
import com.metis.meishuquan.view.shared.ArrowView;

/**
 * Created by wangjin on 4/12/2015.
 */
public class PopupAttentionWindow extends RelativeLayout {
    private ArrowView arrowView;
    private ListView listView;
    private CircleAttentionAdapter adapter;

    public PopupAttentionWindow(Context context, final View.OnClickListener onFinish, final AdapterView.OnItemClickListener onItemClickListener) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.view_circle_popupattentionwindow, this);
        listView = (ListView) findViewById(R.id.id_listview_attention);

        this.arrowView = (ArrowView) this.findViewById(R.id.view_circle_popupaddwindow_arrow);
        int arrowWidth = getResources().getDimensionPixelOffset(R.dimen.view_circle_popupaddwindow_arrow_width);
        int arrowHeight = getResources().getDimensionPixelOffset(R.dimen.view_circle_popupaddwindow_arrow_height);
        arrowView.SetData(new Point(0, arrowHeight), new Point(arrowWidth, arrowHeight), new Point(arrowWidth / 2, 0), getResources().getColor(R.color.black));
        this.setOnClickListener(onFinish);
        adapter = new CircleAttentionAdapter(getContext());
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onFinish.onClick(null);
                onItemClickListener.onItemClick(adapterView, view, i, l);
            }
        });
    }


    public int getGroupId(int i) {
        return (int) adapter.getItemId(i);
    }

    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View的宽高
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
