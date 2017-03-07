package com.catira.opencvdemo.activities.components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import org.opencv.core.Point;

/**
 * Created by timos on 15.02.2017.
 */

public class DrawableCircle {
    private Paint mFillPaint;
    private Paint mStrokePaint;

    private int mColor;
    private int mStrokeWidth;
    private Point mCenter;

    public DrawableCircle(int color, int strokeWidth) {
        mColor = color;
        mStrokeWidth = strokeWidth;
        mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFillPaint.setColor(mColor);
        mFillPaint.setStrokeWidth(strokeWidth);
        mFillPaint.setStyle(Paint.Style.FILL);
        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setColor(mColor);
        mStrokePaint.setStrokeWidth(mStrokeWidth);
        mStrokePaint.setStyle(Paint.Style.STROKE);
    }

    public void draw(Canvas parent, Point center, int radius) {
        draw(parent, center, radius, -1);
    }

    public void draw(Canvas parent, Point center, int radius, int outerRadius) {
        mCenter = center;

        parent.drawCircle((float)center.x, (float)center.y, radius, mFillPaint);

        if(outerRadius >= 0) {
            parent.drawCircle((float) center.x, (float) center.y, outerRadius, mStrokePaint);
        }
    }

    public Point getCenter() {
        return mCenter;
    }
}
