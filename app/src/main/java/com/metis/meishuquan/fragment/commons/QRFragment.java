package com.metis.meishuquan.fragment.commons;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.metis.meishuquan.R;

import java.util.Hashtable;

/**
 * Created by WJ on 2015/4/14.
 */
public class QRFragment extends Fragment {

    private static final String TAG = QRFragment.class.getSimpleName();

    private ImageView mQrIv = null;

    private AsyncTask<String, Integer, Bitmap> mGenerateTask = new AsyncTask<String, Integer, Bitmap>(){

        @Override
        protected Bitmap doInBackground(String... params) {
            return makeQRCode(params[0], mWidth, mHeight);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                mQrIv.setImageBitmap(bitmap);
                mQrBmp = bitmap;
            }
        }
    };

    private int mWidth, mHeight;

    private Bitmap mQrBmp = null;
    private View.OnClickListener mQrClickListener = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWidth = getResources().getDimensionPixelSize(R.dimen.qr_code_size);
        mHeight = getResources().getDimensionPixelSize(R.dimen.qr_code_size);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qr, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mQrIv = (ImageView)view.findViewById(R.id.qr_image);
        setOnImageClickListener(mQrClickListener);
    }

    public void setOnImageClickListener (View.OnClickListener listener) {
        if (mQrIv != null) {
            mQrIv.setOnClickListener(listener);
        }
        mQrClickListener = listener;
    }

    public void showQrCodeWith (String str) {
        mGenerateTask.execute(str);
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

}
