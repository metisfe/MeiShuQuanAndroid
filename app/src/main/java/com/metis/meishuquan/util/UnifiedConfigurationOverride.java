package com.metis.meishuquan.util;

import android.content.Context;
import android.util.Log;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.framework.WebAccessManager;
import com.metis.meishuquan.framework.runner.FileWebRunner;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by wudi on 3/15/2015.
 */
public class UnifiedConfigurationOverride {
    public void start()
    {
        String url = Environments.CDNURL + Environments.ConfigurationName;
        File file = SystemUtil.getDiskCacheDir(MainApplication.MainActivity, Environments.ConfigurationName);
        String downloadPath = file.getAbsolutePath();
        FileDownload fileDownload = new FileDownload(url, downloadPath);
        WebAccessManager.getInstance().start(fileDownload);
    }

    private class FileDownload extends FileWebRunner
    {
        public FileDownload(String url)
        {
            super(url);
        }

        public FileDownload(String url, String savePath)
        {
            super(url, savePath);
        }

        @Override
        public void onFileLoadCompleted(File file)
        {
            if (file == null)
            {
                return;
            }

            PullHandler pullHandler = new PullHandler();
            InputStream is;
            try
            {
                is = new FileInputStream(file);
                Properties prop = pullHandler.parse(is);
                is.close();

                if (prop != null)
                {
                    saveConfig(MainApplication.MainActivity, prop);
                }
            }
            catch (FileNotFoundException e)
            {
            }
            catch (IOException e)
            {
            }
        }
    }

    private class PullHandler
    {
        private Boolean start = false;
        Properties prop = null;

        public Properties parse(InputStream inStream)
        {
            try
            {
                XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = pullFactory.newPullParser();
                xmlPullParser.setInput(inStream, "UTF-8");
                int eventType = xmlPullParser.getEventType();
                boolean isDone = false;

                while ((eventType != XmlPullParser.END_DOCUMENT) && (isDone != true))
                {
                    String localName = null;
                    switch (eventType)
                    {
                        case XmlPullParser.START_DOCUMENT:
                        {

                        }
                        break;
                        case XmlPullParser.START_TAG:
                        {
                            localName = xmlPullParser.getName();
                            if ("configurations".equals(localName)
                                    && (new String("android").equals(xmlPullParser.getAttributeValue("", "platform")) || new String("all").equals(xmlPullParser
                                    .getAttributeValue("", "platform"))))
                            {
                                String version = xmlPullParser.getAttributeValue("", "latest");
                                start = true;
                                if (prop == null)
                                    prop = new Properties();
                                prop.setProperty("version", version);
                            }
                            else if (start == true)
                            {
                                if ("configuration".equals(localName))
                                {
                                    String key = xmlPullParser.getAttributeValue("", "key");
                                    String value = xmlPullParser.getAttributeValue("", "value");
                                    prop.setProperty(key, value);
                                }
                            }
                        }
                        break;
                        case XmlPullParser.END_TAG:
                        {
                            localName = xmlPullParser.getName();
                            if ((localName.equalsIgnoreCase("configurationroot")) && (start == true))
                            {
                                isDone = true;
                            }
                        }
                        break;
                        default:
                            break;
                    }
                    eventType = xmlPullParser.next();
                }
            }
            catch (Exception e)
            {
                return null;
            }

            Log.v("lyb", "End");

            return prop;
        }
    }

    public Properties loadConfig(Context context, boolean asset)
    {
        Properties properties = new Properties();

        try
        {
            if (asset)
            {
                InputStream is = context.getAssets().open(Environments.Config);
                properties.load(is);
                is.close();
            }
            else
            {
                File file = SystemUtil.getDiskCacheDir(context, Environments.myConfigurationName);
                if (!file.exists())
                {
                    return null;
                }
                FileInputStream is = new FileInputStream(file);
                properties.load(is);
                is.close();
            }
        }
        catch (Exception e)
        {
            return null;
        }
        return properties;
    }

    public boolean saveConfig(Context context, Properties properties)
    {
        Properties oldProp = loadConfig(context, false);
        if (oldProp == null || oldProp.getProperty("version").compareTo(properties.getProperty("version")) < 0)
        {
            try
            {
                File file = SystemUtil.getDiskCacheDir(context, Environments.myConfigurationName);
                FileOutputStream os = new FileOutputStream(file);
                properties.store(os, "123");
                os.close();
            }
            catch (Exception e)
            {
                return false;
            }
        }
        return true;
    }
}
