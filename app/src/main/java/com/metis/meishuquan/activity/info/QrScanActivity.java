package com.metis.meishuquan.activity.info;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.client.android.camera.CameraConfigurationUtils;
import com.google.zxing.common.HybridBinarizer;
import com.metis.meishuquan.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QrScanActivity extends BaseActivity implements
        SurfaceHolder.Callback,
        Camera.PreviewCallback,
        View.OnClickListener{

    private static final int MIN_FRAME_WIDTH = 240;
    private static final int MIN_FRAME_HEIGHT = 240;
    private static final int MAX_FRAME_WIDTH = 1200; // = 5/8 * 1920
    private static final int MAX_FRAME_HEIGHT = 675; // = 5/8 * 1080

    private static final String TAG = QrScanActivity.class.getSimpleName();

    public static final String BARCODE_BITMAP = "barcode_bitmap";
    public static final String BARCODE_SCALED_FACTOR = "barcode_scaled_factor";

    private static final int STATE_DECODING = 1,
                            STATE_DECODE_FAILED = 2,
                            STATE_DECODE_SUCCESS = 3;

    private SurfaceView mSurfaceView = null;
    private SurfaceHolder mHolder = null;

    private Camera mCamera = null;

    private Button reScanBtn = null;

    private ScanCoverView mCoverView = null;

    private int mState = STATE_DECODE_FAILED;

    private int mDisplayWidth, mDisplayHeight;

    private Point cameraResolution = null;
    private Point theScreenResolution = null;
    private Rect framingRectInPreview = null;
    private Rect framingRect = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        reScanBtn = (Button)findViewById(R.id.qr_scan_redo);
        reScanBtn.setOnClickListener(this);
        reScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QrScanActivity.this, ImagePreviewActivity.class));
            }
        });

        mCoverView = (ScanCoverView)this.findViewById(R.id.qr_scan_cover);

        mDisplayWidth = getWindowManager().getDefaultDisplay().getWidth();
        mDisplayHeight = getWindowManager().getDefaultDisplay().getHeight();
        Log.v(TAG, "onCreate " + mDisplayWidth + " " + mDisplayHeight);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
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
            //mCamera.setDisplayOrientation(90);
            Log.v(TAG, "point.x=" + point.x + " point.y=" + point.y);
            mCamera.getParameters().setPictureSize(point.x, point.y);
            //mCamera.autoFocus(this);
            mCamera.setOneShotPreviewCallback(this);
            mCamera.startPreview();
            initFromCameraParameters(mCamera);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.v(TAG, TAG + " surfaceChanged ");
        if (mState == STATE_DECODE_FAILED) {
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCamera.release();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        if (mState == STATE_DECODE_FAILED) {
            Log.v(TAG, TAG + " onPreviewFrame " + data.length);
            new DecodeTask().execute(data);
            /*String result = scanningImage(data);
            Toast.makeText(this, "result=" + result, Toast.LENGTH_SHORT).show();*/
        }
    }

    @Override
    public void onClick(View v) {

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
        //mCoverView.setRect(rect);
        // Go ahead and assume it's YUV rather than die.
        Log.v(TAG, "buildLuminanceSource "
                + rect.left + " "
                + rect.top + " "
                + rect.right + " "
                + rect.bottom);
        //359 334 1559 744
        //359 202 1559 877
        return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top,
                rect.width(), rect.height(), false);
    }

    public synchronized Rect getFramingRectInPreview() {
        if (framingRectInPreview == null) {
            Rect framingRect = getFramingRect();
            if (framingRect == null) {
                return null;
            }
            Rect rect = new Rect(framingRect);
            //Point cameraResolution = configManager.getCameraResolution();
            //Point screenResolution = configManager.getScreenResolution();
            if (cameraResolution == null || theScreenResolution == null) {
                // Called early, before init even finished
                return null;
            }
            rect.left = rect.left * cameraResolution.x / theScreenResolution.x;
            rect.right = rect.right * cameraResolution.x / theScreenResolution.x;
            rect.top = rect.top * cameraResolution.y / theScreenResolution.y;
            rect.bottom = rect.bottom * cameraResolution.y / theScreenResolution.y;
            framingRectInPreview = rect;
        }
        return framingRectInPreview;
    }

    public synchronized Rect getFramingRect() {
        if (framingRect == null) {
            if (mCamera == null) {
                return null;
            }
            Point screenResolution = theScreenResolution;
            if (screenResolution == null) {
                // Called early, before init even finished
                return null;
            }

            int width = findDesiredDimensionInRange(screenResolution.x, MIN_FRAME_WIDTH, MAX_FRAME_WIDTH);
            int height = findDesiredDimensionInRange(screenResolution.y, MIN_FRAME_HEIGHT, MAX_FRAME_HEIGHT);

            int leftOffset = (screenResolution.x - width) / 2;
            int topOffset = (screenResolution.y - height) / 2;
            framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
            Log.d(TAG, "Calculated framing rect: " + framingRect);
        }
        return framingRect;
    }

    private static int findDesiredDimensionInRange(int resolution, int hardMin, int hardMax) {
        int dim = 5 * resolution / 8; // Target 5/8 of each dimension
        if (dim < hardMin) {
            return hardMin;
        }
        if (dim > hardMax) {
            return hardMax;
        }
        return dim;
    }

    private void initFromCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        theScreenResolution = new Point();
        display.getSize(theScreenResolution);
        Log.v(TAG, "initFromCameraParameters " + theScreenResolution);
        //screenResolution = theScreenResolution;
        cameraResolution = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, theScreenResolution);
    }

    private class DecodeTask extends AsyncTask<byte[], Integer, String> {

        @Override
        protected String doInBackground(byte[]... params) {
            mState = STATE_DECODING;
            byte[] data = params[0];

            if (cameraResolution != null) {
                return decode(data, cameraResolution.x, cameraResolution.y);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.v(TAG, TAG + " decode end " + s);
            reScanBtn.setText("result=" + s);
            if (s == null) {
                mState = STATE_DECODE_FAILED;
            } else {
                mState = STATE_DECODE_SUCCESS;
            }
        }
    }

}
