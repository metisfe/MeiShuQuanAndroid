package com.metis.meishuquan.view.circle;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.metis.meishuquan.R;

/**
 * Created by wudi on 4/11/2015.
 */
public class ContactListItemView extends LinearLayout {


    public ContactListItemView(Context context) {
        super(context);
        init();
    }

    private void init()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.view_circle_contactlistitemview, this);
    }


    public enum ContactItemType{
        CHECK, BUTTON, NONE
    }
}
