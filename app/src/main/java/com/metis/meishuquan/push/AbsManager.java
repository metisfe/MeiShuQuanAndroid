package com.metis.meishuquan.push;

import android.content.Context;

/**
 * Created by WJ on 2015/6/1.
 */
public abstract class AbsManager {

    private Context mContext = null;

    public AbsManager (Context context) {
        mContext = context;
    }

    public Context getContext () {
        return mContext;
    }
}
