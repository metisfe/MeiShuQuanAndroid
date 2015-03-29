package com.metis.meishuquan.view.topline;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.metis.meishuquan.R;

/**
 * News分享视图
 *
 * Created by wj on 15/3/30.
 */
public class NewsShareView extends RelativeLayout {

    private Context context;
    public Button btnWeixin,btnFriends,btnCancel;

    public NewsShareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_share,this);
        initView(this);
    }

    private void initView(ViewGroup viewGroup) {
        this.btnWeixin= (Button) viewGroup.findViewById(R.id.btn_weixin_share);
        this.btnFriends= (Button) viewGroup.findViewById(R.id.btn_weixin_friends_share);
        this.btnCancel= (Button) viewGroup.findViewById(R.id.id_btn_cancle);
    }
}
