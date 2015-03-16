package com.metis.meishuquan.framework.runner;

import android.text.TextUtils;
import android.util.Log;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.util.SystemUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by wudi on 3/15/2015.
 */

public abstract class FileWebRunner extends WebRunner<File>
{
    private String savePath;

    public FileWebRunner(String url)
    {
        super(url);

        this.setWebRunnerListener(new FileWebRunnerListener());
    }

    public FileWebRunner(String url, String savePath)
    {
        super(url);

        this.savePath = savePath;
        this.setWebRunnerListener(new FileWebRunnerListener());
    }

    private class FileWebRunnerListener implements WebRunnerListener<File>
    {

        @Override
        public void onCompleted(AsyncResult<File> result)
        {
            File file = result.getResult();
            if (file == null)
            {
                Log.d("FileWebRunner", "onCompleted file is null");
            }
            else
            {
                Log.d("FileWebRunner", "onCompleted file path" + file.getAbsolutePath());
            }

            onFileLoadCompleted(file);
        }

        @Override
        public File onHandleData(BufferedInputStream is, RunnerProgress progress)
        {
            File file = null;
            if (!TextUtils.isEmpty(savePath))
            {
                file = new File(savePath);
                if (file.exists())
                {
                    file.delete();
                }
                else
                {
                    SystemUtil.makeDirs(savePath);
                }
                file = new File(savePath);

            }
            else
            {
                String key = SystemUtil.hashKeyForDisk(getUrl());
                file = SystemUtil.getDiskCacheDir(MainApplication.UIContext, key);
            }

            Log.d("FileWebRunner", "onHandleData file path" + file.getAbsolutePath());
            file.deleteOnExit();
            FileOutputStream os = null;
            try
            {
                file.createNewFile();
                os = new FileOutputStream(file);
                byte[] buffer = new byte[4 * 1024];
                int readSize = 0;
                while ((readSize = is.read(buffer)) > 0)
                {
                    os.write(buffer, 0, readSize);
                    progress.downloadSize += readSize;
                    progress.progress = (progress.totalSize == 0 || progress.downloadSize == 0) ? 0 : (int) (progress.downloadSize / progress.totalSize);
                }
                os.flush();

                return file;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (os != null)
                {
                    try
                    {
                        os.close();
                    }
                    catch (IOException e)
                    {
                    }
                }
            }
            return null;
        }

        @Override
        public File getObjectFromCache(String url)
        {
            return null;
        }

    }

    public abstract void onFileLoadCompleted(File file);
}