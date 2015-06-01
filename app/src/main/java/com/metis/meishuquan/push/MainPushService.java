package com.metis.meishuquan.push;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.umeng.message.UTrack;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;

import org.android.agoo.client.BaseConstants;
import org.json.JSONObject;

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
            UTrack.getInstance(context).trackMsgClick(msg, true);

            Log.v(TAG, "onMessage msg=" + msg);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    UnReadManager.getInstance(context).notifyByTag(UnReadManager.TAG_NEW_STUDENT, 1, true);
                    Toast.makeText(context, "onMessage", Toast.LENGTH_SHORT).show();
                }
            });
            // code  to handle message here
            // ...
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
