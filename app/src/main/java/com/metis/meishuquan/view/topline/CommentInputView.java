package com.metis.meishuquan.view.topline;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.metis.meishuquan.R;
import com.metis.meishuquan.util.Utils;

/**
 * Created by wj on 15/3/29.
 */
public class CommentInputView extends RelativeLayout {
    public EditText editText;
    public Button btnSend;
    private Context context;

    public CommentInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        LayoutInflater.from(context).inflate(R.layout.popupwindow_dialog_comment_input,this);
        initView(this);
    }

    private void initView(ViewGroup viewGroup) {
        editText= (EditText) viewGroup.findViewById(R.id.id_comment_edittext);
        btnSend= (Button) viewGroup.findViewById(R.id.id_btn_comment_send);
        Utils.showInputMethod(context,editText);
    }
}
