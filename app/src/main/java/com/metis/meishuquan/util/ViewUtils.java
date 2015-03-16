package com.metis.meishuquan.util;

import android.content.Intent;
import android.widget.Toast;

import com.metis.meishuquan.MainActivity;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.UserGuide;

/**
 * Created by wudi on 3/15/2015.
 */
public class ViewUtils {

    private static final String ToastMessageCreatedAppShortcut = "Shortcut created.";

    public static void delayExecute(Runnable runner, int delayInMS)
    {
        if (delayInMS <= 0)
        {
            runner.run();
        }
        else
        {
            MainApplication.Handler.postDelayed(runner, delayInMS);
        }
    }

    public static void createAppShortcut(final MainActivity mainActivity)
    {
        if (!UserGuide.isFirstTo("meishuquan_shortcut"))
        {
            return;
        }

        UserGuide.setIsFirstTo("meishuquan_shortcut", false);
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra("duplicate", false);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, MainApplication.MainActivity.getString(R.string.app_name));
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(mainActivity, R.drawable.ic_launcher));
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(mainActivity, mainActivity.getClass()).setAction(Intent.ACTION_MAIN));
        mainActivity.sendBroadcast(shortcut);
        Toast.makeText(MainApplication.MainActivity, ToastMessageCreatedAppShortcut, Toast.LENGTH_LONG).show();
    }
}
