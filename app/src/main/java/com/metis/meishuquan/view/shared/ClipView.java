package com.metis.meishuquan.view.shared;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by WJ on 2015/4/20.
 */
public class ClipView extends View {

    private Rect mRect = null;
    private Paint mRectPaint = null;

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
        mRectPaint.setColor(Color.BLACK);
        mRectPaint.setStrokeWidth(10);
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
            canvas.clipRect(mRect);
            canvas.drawColor(0x90909090);
        }

    }
}
