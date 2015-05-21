package com.metis.meishuquan.model.assess;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Bimp {

    private static final String TAG = Bimp.class.getSimpleName();

    public static int max = 0;
    public static boolean act_bool = true;
    public static Bimp bimp = null;
    public List<Bitmap> bmp = new ArrayList<Bitmap>();

    //图片sd地址  上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小于100KB，失真度不明显
    public List<String> drr = new ArrayList<String>();

    public static Bimp getInstance() {
        if (bimp == null) {
            bimp = new Bimp();
        }
        return bimp;
    }

    private Bimp() {
    }

    public Bitmap revitionImageSize(String path) throws IOException {
        if (path == null) {
            return null;
        }
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1000)
                    && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }
}
