package com.metis.meishuquan.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.util.SystemUtil;

/**
 * Created by wudi on 3/15/2015.
 */

public class BaseFragment extends Fragment
{
    public static String Bundle_TitleKey = "Title";
    public static String Bundle_UrlKey = "Url";
    public static String Bundle_IsProfileTimelineKey = "IsProfileTimeline";
    public static String Bundle_IdKey = "Id";

    protected void showRefreshErrorTip(Exception e)
    {
        String errorMessage;
        if (SystemUtil.isNetworkAvailable(MainApplication.MainActivity))
        {
            errorMessage = "error, try again later";
        }
        else
        {
            errorMessage = "network not available";
        }

        Toast.makeText(MainApplication.MainActivity, errorMessage, Toast.LENGTH_SHORT).show();
    }

    protected void showLoadEmptyTip(View loadedMessageLayout, TextView loadedMessage, String errorInfo)
    {
        // if(isGallery)
        // loadedMessage.setText(this.getString(R.string.loaded_gallery_empty));
        // else
        // loadedMessage.setText(this.getString(R.string.loaded_moment_empty));
        loadedMessage.setText(errorInfo);
        loadedMessage.setVisibility(View.VISIBLE);
        loadedMessageLayout.setVisibility(View.VISIBLE);
    }

    protected void showLoadErrorTip(View loadedMessageLayout, TextView loadedMessage, Exception e)
    {
        String errorMessage = "";
        if (SystemUtil.isNetworkAvailable(MainApplication.MainActivity))
        {
            errorMessage = "error, try again later";
        }
        else
        {
            errorMessage = "network not available";
        }

        loadedMessage.setText(errorMessage);
        loadedMessage.setVisibility(View.VISIBLE);
        loadedMessageLayout.setVisibility(View.VISIBLE);
    }
}