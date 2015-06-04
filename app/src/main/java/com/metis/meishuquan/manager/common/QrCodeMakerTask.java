package com.metis.meishuquan.manager.common;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

/**
 * Created by gaoyunfei on 15/6/4.
 */
public class QrCodeMakerTask extends AsyncTask<QrCodeMakerTask.Task, Integer, Bitmap> {

    public static class Task {
        private String mTarget = null;
        private int mSize = 0;
        public Task (String target, int size) {
            mTarget = target;
            mSize = size;
        }
    }

    private Callback mCallback = null;

    @Override
    protected Bitmap doInBackground(Task... params) {
        Task task = params[0];
        return makeQRCode(task.mTarget, task.mSize, task.mSize);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (mCallback != null) {
            mCallback.onCallback(bitmap);
            mCallback = null;
        }

    }

    public void makeQrCode (Task task, Callback callback) {
        mCallback = callback;
        execute(task);
    }

    private Bitmap makeQRCode (String str, int width, int height) {
        if (str == null) {
            return null;
        }

        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(str, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xFF000000;
                    } else {
                        pixels[y * width + x] = 0xFFFFFFFF;
                    }
                }
            }
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            bmp.setPixels(pixels, 0, width, 0, 0, width, height);
            return bmp;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static interface Callback {
        public void onCallback (Bitmap bitmap);
    }
}
