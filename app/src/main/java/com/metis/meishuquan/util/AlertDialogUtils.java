package com.metis.meishuquan.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.metis.meishuquan.R;

/**
 * Created by wangjin on 15/6/24.
 */
public class AlertDialogUtils {

    public static void showMenuDialog(Context context, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(context.getResources().getStringArray(R.array.moment_comment_item), onClickListener);
        builder.show();
    }
}
