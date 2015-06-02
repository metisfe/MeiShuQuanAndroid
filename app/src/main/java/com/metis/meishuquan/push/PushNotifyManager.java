package com.metis.meishuquan.push;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.act.SelectStudioActivity;
import com.metis.meishuquan.activity.act.StudentListActivity;
import com.metis.meishuquan.model.enums.IdTypeEnum;
import com.umeng.message.entity.UMessage;

/**
 * Created by WJ on 2015/6/1.
 */
public class PushNotifyManager extends AbsManager {

    private static PushNotifyManager sManager = null;

    public static synchronized PushNotifyManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new PushNotifyManager(context.getApplicationContext());
        }
        return sManager;
    }

    private NotificationManagerCompat mNotifyManager = null;

    private PushNotifyManager(Context context) {
        super(context);
        mNotifyManager = NotificationManagerCompat.from(context);
    }

    public void showNotify (UMessage msg, Intent contentIt) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
        builder.setContentTitle(msg.title);
        builder.setContentText(msg.text);
        builder.setSmallIcon(R.drawable.notification_small_icon);
        builder.setAutoCancel(true);
        builder.setLargeIcon(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_launcher));
        Intent it = null;
        /*if (targetActivity != null) {
            it = new Intent(getContext(), targetActivity);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }*/
        /*if (MainApplication.userInfo.getUserRoleEnum() == IdTypeEnum.STUDENT) {
            it = new Intent(getContext(), SelectStudioActivity.class);
        } else if (MainApplication.userInfo.getUserRoleEnum() == IdTypeEnum.STUDIO) {
            it = new Intent(getContext(), StudentListActivity.class);
        }*/
        if (contentIt != null) {
            builder.setContentIntent(PendingIntent.getActivity(getContext(), 100, contentIt, PendingIntent.FLAG_CANCEL_CURRENT));
        }
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        mNotifyManager.notify(100, builder.build());
    }
}
