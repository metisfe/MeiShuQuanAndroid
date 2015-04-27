package com.metis.meishuquan.util;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by wangjin on 15/4/27.
 */
public class DownloadUtil {
    private URL url = null;
    private InputStream inputStream = null;
    public static final String downloadPath = Environment.getExternalStorageDirectory()
            + "/";

    /**
     * 该函数返回整型     -1：代表下载文件出错     0：代表下载文件成功    1：代表文件已经存在
     *
     * @param urlStr   下载文件的网络地址
     * @param path     想要把下载过来的文件存放到哪一个SDCARD目录下
     * @param fileName 下载的文件的文件名，可以跟原来的名字不同，所以这里加一个fileName
     * @return
     */
    public int downFile(String urlStr, String path, String fileName) {

        try {
            FileUtil fileUtil = new FileUtil();

            if (fileUtil.isFileExist(path + fileName)) {
                return 1;
            } else {
                URL url = new URL(urlStr);
                HttpURLConnection conection = (HttpURLConnection) url
                        .openConnection();
                InputStream input = conection.getInputStream();
                File file = null;
                OutputStream outputstream = null;
                File sdDir = new File(downloadPath + path);
                sdDir.mkdir();
                file = new File(downloadPath + path + fileName);
                file.createNewFile();

                outputstream = new FileOutputStream(file);
                byte data[] = new byte[1024 * 4];
                while (true) {
                    int temp = input.read(data);
                    if (temp == -1) {
                        break;
                    }
                    outputstream.write(data, 0, temp);
                }
                outputstream.flush();
                outputstream.close();
                input.close();

//                inputStream = getInputStreamFromURL(urlStr);
//                File resultFile = fileUtil.writeToSDFromInput(path, fileName, inputStream);
//                if (resultFile == null) {
//                    return -1;
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                //inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 根据URL得到输入流
     *
     * @param urlStr
     * @return
     * @throws IOException
     */
    public InputStream getInputStreamFromURL(String urlStr) {
        HttpURLConnection urlConn = null;

        try {
            url = new URL(urlStr);
            urlConn = (HttpURLConnection) url.openConnection();
            inputStream = urlConn.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputStream;
    }
}
