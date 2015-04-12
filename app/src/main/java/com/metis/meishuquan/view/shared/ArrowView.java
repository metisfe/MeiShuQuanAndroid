package com.metis.meishuquan.view.shared;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wudi on 4/12/2015.
 */

public class ArrowView extends View
{
    private Point head;
    private Point tailLeft;
    private Point tailRight;
    private int fillColor;
    private Path path;
    private Paint paint;

    public ArrowView(Context context)
    {
        super(context);
    }

    public ArrowView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ArrowView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public void SetData(Point head,Point tailLeft,Point tailRight,int fillColor)
    {
        this.head = head;
        this.tailLeft = tailLeft;
        this.tailRight = tailRight;
        this.fillColor = fillColor;

        initPaint();
        generatePath();
    }

    private void initPaint()
    {
        paint = new Paint();
        paint.setColor(fillColor);
        paint.setStyle(Paint.Style.FILL);
    }

    private void generatePath()
    {
        path = new Path();
        path.moveTo(head.x, head.y);
        path.lineTo(tailLeft.x,tailLeft.y);
        path.lineTo(tailRight.x, tailRight.y);
        path.close();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(path!=null)
        {
            canvas.drawPath(path, paint);
        }
    }

}
