package com.metis.meishuquan.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.metis.meishuquan.activity.info.ImagePreviewActivity;
import com.metis.meishuquan.activity.info.homepage.StudioActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/5/18.
 */
public class ActivityUtils {
    public static void startNameCardActivity (Context context, int userId) {
        Intent it = new Intent (context, StudioActivity.class);
        it.putExtra(StudioActivity.KEY_USER_ID, userId);
        context.startActivity(it);
    }

    public static void startImagePreviewActivity (Context context, String url) {
        ArrayList<String> list = new ArrayList<String>();
        list.add(url);
        startImagePreviewActivity(context, list);
    }

    public static void startImagePreviewActivity (Context context, ArrayList<String> list) {
        startImagePreviewActivity(context, list, 0);
    }

    public static void startImagePreviewActivity (Context context, ArrayList<String> list, int index) {
        Intent it = new Intent(context, ImagePreviewActivity.class);
        it.putStringArrayListExtra(ImagePreviewActivity.KEY_IMAGE_URL_ARRAY, list);
        it.putExtra(ImagePreviewActivity.KEY_START_INDEX, index);
        context.startActivity(it);
    }
}
