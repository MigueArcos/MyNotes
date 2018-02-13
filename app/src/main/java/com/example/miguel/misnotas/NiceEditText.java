package com.example.miguel.misnotas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

/**
 * Created by Valentine on 9/21/2015.
 */
public class NiceEditText extends android.support.v7.widget.AppCompatEditText{

    private Rect rect;
    private Paint paint;

    public NiceEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        rect = new Rect();
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#00897B"));
    }

    public void setLineColor(int color) {
        paint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int lineHeight = getLineHeight();
        int count = height / lineHeight;

        // For long text with scrolling
        if (getLineCount() > count) {
            count = getLineCount();
        }

        // Draw first line
        int baseline = getLineBounds(0, rect);
        for (int i = 0; i < count; i++) {
            canvas.drawLine(rect.left, baseline + 1, rect.right, baseline + 1, paint);
            // Draw next line
            baseline += getLineHeight();
        }
        super.onDraw(canvas);
    }
}