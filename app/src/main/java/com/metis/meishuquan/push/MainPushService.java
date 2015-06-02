package com.metis.meishuquan.push;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.metis.meishuquan.MainActivity;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.activity.act.SelectStudioActivity;
import com.metis.meishuquan.activity.act.StudentListActivity;
import com.metis.meishuquan.activity.topline.NewDetailActivity;
import com.metis.meishuquan.model.enums.IdTypeEnum;
import com.umeng.message.UTrack;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.UmengIntentService;
import com.umeng.message.UmengRegistrar;
import com.umeng.message.entity.UMessage;

import org.android.agoo.client.BaseConstants;
import org.json.JSONObject;

import java.sql.Date;

/**
 * Created by WJ on 2015/6/1.
 */
public class MainPushService extends UmengBaseIntentService {

    private static final String TAG = MainPushService.class.getSimpleName();

    @Override
    protected void onMessage(final Context context, Intent intent) {
        super.onMessage(context, intent);
        try {
            String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
            UMessage msg = new UMessage(new JSONObject(message));
            //UTrack.getInstance(context).trackMsgClick(msg, true);
            runOnUiThread(context, msg);
            // code  to handle message here
            // ...
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void runOnUiThread (final Context context, final UMessage msg) {
        Log.v(TAG, "onMessage msg=" + msg.custom);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                MessageCustom custom = new Gson().fromJson(msg.custom, MessageCustom.class);
                if (custom == null) {
                    custom = MessageCustom.createDefaultOne();
                }
                if (custom.getSendToUserid() != 0) {
                    if (MainApplication.userInfo == null || custom.getSendToUserid() != MainApplication.userInfo.getUserId()) {
                        Log.v(TAG, "onMessage msg not for you");
                        return;
                    }
                }

                PushType type = PushType.getPushType(custom.NotificationType);
                dispatchPushMsg(context, msg, custom, type);
            }
        });
    }

    private void dispatchPushMsg (Context context, UMessage msg, MessageCustom custom, PushType type) {
        Intent it = null;
        switch (type) {
            case ACTIVITY:
                UnReadManager.getInstance(context).notifyByTag(PushType.ACTIVITY.getTag(), 1, true);
                Class clz = null;
                if (MainApplication.userInfo.getUserRoleEnum() == IdTypeEnum.STUDENT) {
                    clz = SelectStudioActivity.class;
                } else if (MainApplication.userInfo.getUserRoleEnum() == IdTypeEnum.STUDIO) {
                    clz = StudentListActivity.class;
                }
                it = new Intent(context, clz);
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PushNotifyManager.getInstance(context).showNotify(msg, it);
                break;
            case APPLY_FRIEND:
                it = new Intent(context, MainActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PushNotifyManager.getInstance(context).showNotify(msg, it);
                break;
            case FRIEND:
                it = new Intent(context, MainActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PushNotifyManager.getInstance(context).showNotify(msg, it);
                break;
            case NEWS:
                it = new Intent(context, NewDetailActivity.class);
                it.putExtra(NewDetailActivity.KEY_NEWS_ID,0);
                it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PushNotifyManager.getInstance(context).showNotify(msg, it);
                break;
            case DEFAULT:
                it = new Intent(context, MainActivity.class);
                PushNotifyManager.getInstance(context).showNotify(msg, it);
                break;
        }
    }

    public static class MessageCustom {
        public int SendToUserRole;
        public int NotificationType;
        public long SendToUserid;
        public String SendToUserName;
        public long SendByUserid;
        public String SendByUserName;
        public String NotificationTime;

        public int getSendToUserRole() {
            return SendToUserRole;
        }

        public void setSendToUserRole(int sendToUserRole) {
            SendToUserRole = sendToUserRole;
        }

        public int getNotificationType() {
            return NotificationType;
        }

        public void setNotificationType(int notificationType) {
            NotificationType = notificationType;
        }

        public long getSendToUserid() {
            return SendToUserid;
        }

        public void setSendToUserid(long sendToUserid) {
            SendToUserid = sendToUserid;
        }

        public String getSendToUserName() {
            return SendToUserName;
        }

        public void setSendToUserName(String sendToUserName) {
            SendToUserName = sendToUserName;
        }

        public long getSendByUserid() {
            return SendByUserid;
        }

        public void setSendByUserid(long sendByUserid) {
            SendByUserid = sendByUserid;
        }

        public String getSendByUserName() {
            return SendByUserName;
        }

        public void setSendByUserName(String sendByUserName) {
            SendByUserName = sendByUserName;
        }

        public String getNotificationTime() {
            return NotificationTime;
        }

        public void setNotificationTime(String notificationTime) {
            NotificationTime = notificationTime;
        }

        public static MessageCustom createDefaultOne () {
            return new MessageCustom();
        }
    }

    public static String getDeviceToken (Context context) {
        UmengRegistrar.setDebug(context, true, true);
        String device_token = UmengRegistrar.getRegistrationId(context);
        UmengRegistrar.setDebug(context, false, false);
        return device_token;
    }
}
