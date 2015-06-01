package com.metis.meishuquan.push;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

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

    public void showNotify () {

    }
}
