package com.metis.meishuquan.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.metis.meishuquan.activity.info.homepage.StudioActivity;

/**
 * Created by WJ on 2015/5/18.
 */
public class ActivityUtils {
    public static void startNameCardActivity (Context context, int userId) {
        Intent it = new Intent (context, StudioActivity.class);
        it.putExtra(StudioActivity.KEY_USER_ID, userId);
        context.startActivity(it);
    }
}
