package com.metis.meishuquan.activity.info;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.metis.meishuquan.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class QrScanActivity extends BaseActivity implements
        SurfaceHolder.Callback,
        Camera.PreviewCallback,
        View.OnClickListener,
        Camera.AutoFocusCallback{

    private static final String TAG = QrScanActivity.class.getSimpleName();

    public static final String BARCODE_BITMAP = "barcode_bitmap";
    public static final String BARCODE_SCALED_FACTOR = "barcode_scaled_factor";

    private static final int STATE_SCANNING = 0,
                            STATE_DECODING = 1,
                            STATE_DECODE_FAILED = 2,
                            STATE_DECODE_SUCESS = 3;

    private SurfaceView mSurfaceView = null;
    private SurfaceHolder mHolder = null;

    private Camera mCamera = null;

    private Button reScanBtn = null;

    private int mState = STATE_SCANNING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        reScanBtn = (Button)findViewById(R.id.qr_scan_redo);
        reScanBtn.setOnClickListener(this);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (!hasCamera(this)) {
            Toast.makeText(this, "no cameras found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (!canAutoFocus(this)) {
            Toast.makeText(this, "camera can not auto focus", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mCamera = Camera.open();
        mSurfaceView = (SurfaceView)findViewById(R.id.qr_scan_surface);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
    }

    private boolean hasCamera (Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private boolean canAutoFocus (Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            Point point = new Point();
            getWindowManager().getDefaultDisplay().getSize(point);
            mCamera.setPreviewDisplay(holder);
            mCamera.setDisplayOrientation(90);
            Log.v(TAG, "point.x=" + point.x + " point.y=" + point.y);
            mCamera.getParameters().setPictureSize(point.x, point.y);
            //mCamera.autoFocus(this);
            mCamera.setOneShotPreviewCallback(this);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.v(TAG, TAG + " surfaceChanged ");
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        try {
            mCamera.setPreviewCallback(this);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.v(TAG, TAG + " surfaceDestroyed ");
        mCamera.setPreviewCallback(null);
        mCamera.setOneShotPreviewCallback(null);
        try {
            mCamera.setPreviewDisplay(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.release();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        if (mState != STATE_DECODING) {
            Log.v(TAG, TAG + " onPreviewFrame " + data.length);
            new DecodeTask().execute(data);
            /*String result = scanningImage(data);
            Toast.makeText(this, "result=" + result, Toast.LENGTH_SHORT).show();*/
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {

    }

    private String scanningImage(/*byte[] data*/Bitmap bmp) {

        //Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);

        Map<DecodeHintType, String> hints = new HashMap<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");

        final int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        /*final int qrWidth = getResources().getDimensionPixelSize(R.dimen.qr_code_size);
        final int qrHeight = getResources().getDimensionPixelSize(R.dimen.qr_code_size);

        int bmpWidth = mCamera.getParameters().getPictureSize().width;
        int bmpHeight = mCamera.getParameters().getPictureSize().height;

        PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, bmpWidth, bmpHeight,
                (bmpWidth - qrWidth)/2, (bmpHeight - qrHeight)/2,
                (bmpWidth + qrWidth)/2, (bmpHeight + qrHeight)/2, false);*/

        // 获得待解析的图片
        RGBLuminanceSource source = new RGBLuminanceSource(
                bmp.getWidth(), bmp.getHeight(), pixels);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        Result result;
        try {
            result = reader.decode(bitmap1, hints);
            // 得到解析后的文字
            return result.getText();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Decode the data within the viewfinder rectangle, and time how long it took. For efficiency,
     * reuse the same reader objects from one decode to the next.
     *
     * @param data   The YUV preview frame.
     * @param width  The width of the preview frame.
     * @param height The height of the preview frame.
     */
    private String decode(byte[] data, int width, int height) {
        long start = System.currentTimeMillis();
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        Map<DecodeHintType, String> hints = new HashMap<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        multiFormatReader.setHints(hints);
        Result rawResult = null;
        /*int bmpWidth = mCamera.getParameters().getPictureSize().width;
        int bmpHeight = mCamera.getParameters().getPictureSize().height;
        final int qrWidth = getResources().getDimensionPixelSize(R.dimen.qr_code_size);
        final int qrHeight = getResources().getDimensionPixelSize(R.dimen.qr_code_size);*/
        PlanarYUVLuminanceSource source = buildLuminanceSource(data, width, height);
        if (source != null) {
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                rawResult = multiFormatReader.decodeWithState(bitmap);
            } catch (ReaderException re) {
                // continue
            } finally {
                multiFormatReader.reset();
            }
        }
        if (rawResult != null) {
            return rawResult.getText();
        }
        return null;

    }

    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        Rect rect = getFramingRectInPreview();
        if (rect == null) {
            return null;
        }
        // Go ahead and assume it's YUV rather than die.
        return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top,
                rect.width(), rect.height(), false);
    }

    public synchronized Rect getFramingRectInPreview() {
        /*int bmpWidth = mCamera.getParameters().getPictureSize().width;
        int bmpHeight = mCamera.getParameters().getPictureSize().height;
        final int qrWidth = getResources().getDimensionPixelSize(R.dimen.qr_code_size);
        final int qrHeight = getResources().getDimensionPixelSize(R.dimen.qr_code_size);*/
        return new Rect(359, 202, 1559, 877);
        /*if (framingRectInPreview == null) {
            Rect framingRect = getFramingRect();
            if (framingRect == null) {
                return null;
            }
            Rect rect = new Rect(framingRect);
            Point cameraResolution = configManager.getCameraResolution();
            Point screenResolution = configManager.getScreenResolution();
            if (cameraResolution == null || screenResolution == null) {
                // Called early, before init even finished
                return null;
            }
            rect.left = rect.left * cameraResolution.x / screenResolution.x;
            rect.right = rect.right * cameraResolution.x / screenResolution.x;
            rect.top = rect.top * cameraResolution.y / screenResolution.y;
            rect.bottom = rect.bottom * cameraResolution.y / screenResolution.y;
            framingRectInPreview = rect;
        }
        return framingRectInPreview;*/
    }

    private class DecodeTask extends AsyncTask<byte[], Integer, String> {

        @Override
        protected String doInBackground(byte[]... params) {
            mState = STATE_DECODING;
            /*byte[] data = new byte[268 * 270];
            InputStream is = getResources().openRawResource(R.raw.qrcode);

            try {
                is.read(data);
                is.close();
                String result = scanningImage(data);
                Log.v(TAG, TAG + " onCreate data.size=" + data.length);
                Log.v(TAG, TAG + " onCreate result=" + result);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            int bmpWidth = mCamera.getParameters().getPictureSize().width;
            int bmpHeight = mCamera.getParameters().getPictureSize().height;
            byte[] data = params[0];
            final int qrWidth = getResources().getDimensionPixelSize(R.dimen.qr_code_size);
            final int qrHeight = getResources().getDimensionPixelSize(R.dimen.qr_code_size);

            Log.v(TAG, TAG + " decoding ... " + data.length + " " + bmpWidth + " " + bmpHeight);
            //1920 1080 3110400
            return decode(data, 1920, 1080)/*scanningImage(data)*/;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.v(TAG, TAG + " decode end " + s);
            reScanBtn.setText("result=" + s);
            if (s == null) {
                mState = STATE_DECODE_FAILED;
            } else {
                mState = STATE_DECODE_SUCESS;
            }
        }
    }

}
