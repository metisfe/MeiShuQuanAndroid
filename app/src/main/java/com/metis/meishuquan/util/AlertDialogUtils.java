package com.metis.meishuquan.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.metis.meishuquan.R;

/**
 * Created by wangjin on 15/6/24.
 */
public class AlertDialogUtils {

    public static void showMenuDialog(Context context, boolean isMe, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (isMe) {
            builder.setItems(context.getResources().getStringArray(R.array.moment_comment_item), onClickListener);
        } else {
            builder.setItems(context.getResources().getStringArray(R.array.moment_comment_item_not_me), onClickListener);
        }
        builder.show();
    }
}
