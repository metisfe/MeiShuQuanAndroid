package com.metis.meishuquan.view.shared;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by WJ on 2015/4/20.
 */
public class ClipView extends View {

    private Rect mRect = null;
    private Paint mRectPaint = null;
    private int mWidth = 20, mLength = 100;

    public ClipView(Context context) {
        this(context, null);
    }

    public ClipView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //359 334 1559 744
        mRectPaint = new Paint();
        mRectPaint.setColor(0xfffb6d6d);
        mRectPaint.setStrokeWidth(mWidth);
    }

    public void setRect (Rect rect) {
        mRect = rect;
    }

    public Rect getRect () {
        return mRect;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRect != null) {
            canvas.save();
            canvas.clipRect(mRect, Region.Op.XOR);
            canvas.drawColor(0x90ffffff);
            canvas.drawLine(mRect.left - mWidth / 2, mRect.top, mRect.left + mLength, mRect.top, mRectPaint);
            canvas.drawLine(mRect.right - mLength, mRect.top, mRect.right + mWidth / 2, mRect.top, mRectPaint);
            canvas.drawLine(mRect.left, mRect.top - mWidth / 2, mRect.left, mRect.top + mLength, mRectPaint);
            canvas.drawLine(mRect.right, mRect.top - mWidth / 2, mRect.right, mRect.top + mLength, mRectPaint);

            canvas.drawLine(mRect.left - mWidth / 2, mRect.bottom, mRect.left + mLength, mRect.bottom, mRectPaint);
            canvas.drawLine(mRect.right - mLength, mRect.bottom, mRect.right + mWidth / 2, mRect.bottom, mRectPaint);
            canvas.drawLine(mRect.left, mRect.bottom - mWidth / 2, mRect.left, mRect.bottom - mLength, mRectPaint);
            canvas.drawLine(mRect.right, mRect.bottom - mWidth / 2, mRect.right, mRect.bottom - mLength, mRectPaint);
            canvas.restore();
        }

    }
}
