package com.metis.meishuquan;

import android.content.Context;
import android.os.Environment;

import com.metis.meishuquan.util.Environments;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by WJ on 2015/5/11.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler sHandler = new CrashHandler();

    public static CrashHandler getInstance (Context context) {
        sHandler.setPath(context.getExternalCacheDir().getAbsolutePath());
        return sHandler;
    }

    private String mRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    public void setPath (String path) {
        mRootPath = path;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        writeThrowable(throwable);
    }

    private void writeThrowable (Throwable throwable) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(throwable.getMessage());
            FileWriter fw = new FileWriter(mRootPath + File.separator + System.currentTimeMillis() + ".crash");
            fw.write(sb.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
