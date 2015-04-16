package com.metis.meishuquan.activity.info;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by WJ on 2015/4/16.
 */
public class ScanCoverView extends View {

    private Rect mRect = null;
    private Paint mPaint = null;

    public ScanCoverView(Context context) {
        this(context, null);
    }

    public ScanCoverView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScanCoverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init (Context context) {
        mPaint = new Paint();
        mPaint.setColor(0x50000000);
        mPaint.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRect != null) {
            canvas.save();
            canvas.rotate(90);
            canvas.drawRect(mRect, mPaint);
            canvas.restore();
        }
    }

    public void setRect (Rect rect) {
        mRect = rect;
        this.postInvalidate();
    }
}
