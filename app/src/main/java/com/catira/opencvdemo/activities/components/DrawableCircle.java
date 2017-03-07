package com.catira.opencvdemo.activities.components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import org.opencv.core.Point;

/**
 * Created by timos on 15.02.2017.
 */

public class DrawableCircle {
    private Paint mPaint;

    private Bitmap mBitmap;
    private int mColor;
    private int mStrokeWidth;
    private Point mCenter;
    private int mRadius;
    private Canvas mCanvas;

    public DrawableCircle(int color, int strokeWidth) {
        mColor = color;
        mStrokeWidth = strokeWidth;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColor);
    }

    public void draw(Canvas parent, Point center, int radius) {
        mCenter = center;
        if(mRadius != radius) {
            mRadius = radius;
            mBitmap = Bitmap.createBitmap(parent.getWidth(), parent.getHeight(), Bitmap.Config.ARGB_8888);

            mCanvas = new Canvas(mBitmap);
            mPaint.setColor(mColor);
            mPaint.setStrokeWidth(mStrokeWidth);
            mCanvas.drawCircle(0, 0, mRadius, mPaint);
        }

        parent.drawBitmap(mBitmap, (float)center.x, (float)center.y, mPaint);
        //if(radius != mStrokeWidth) {
        parent.drawCircle((float) center.x, (float) center.y, radius, mPaint);
        //}
    }

    public int getRadius() {
        return mRadius;
    }

    public Point getCenter() {
        return mCenter;
    }
}
