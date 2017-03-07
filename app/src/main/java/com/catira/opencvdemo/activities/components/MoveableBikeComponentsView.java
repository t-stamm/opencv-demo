package com.catira.opencvdemo.activities.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.catira.opencvdemo.model.BikePartPositions;
import com.catira.opencvdemo.model.BikeSize;

import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ck on 10.01.2017.
 */

public class MoveableBikeComponentsView extends View implements Runnable {

    private final BikeSize mBikeSize;
    private int mStrikeWidth = 10;
    private int mPointRadius = 12;

    private BikePartPositions mBikePartPositions;

    private DrawableCircle mPaddles;
    private DrawableCircle mPaddleLength;
    private DrawableCircle mSaddle;
    private DrawableCircle mFrameFront;
    private DrawableCircle mSteeringLength;

    private DrawableCircle mBackWheel;
    private DrawableCircle mFrontWheel;

    private List<DrawableCircle> mDrawableCircles;

    private Path mPath;
    private Paint mTextColor;
    private Paint mTextBorder;
    private ScaleGestureDetector SGD;

    Paint mPaintRed, mPaintYellow;
    private DrawableCircle mNearestPressedPoint;
    private boolean mHideFrame = false;
    private boolean mTyresDisabled = false;
    private int mDefaultTyreSize = 0;
    /*
    Initialisierung mit allen Elementen die in der View verschoben werden können.
     D. h. die Komponente wird maximal einmal pro View instanziiert.
    Hört an der Parent-View der einzelnen Elemente auf das Touch Event und
    berechnet selber welches View Element bewegt werden soll.
     */

    //  view = new ZeichnenView(this);
    public MoveableBikeComponentsView(Context c, BikePartPositions bikePartPositions, BikeSize bikeSize) {
        super(c);
        this.mBikeSize = bikeSize;
        this.mBikePartPositions = bikePartPositions;
        mDefaultTyreSize = this.mBikePartPositions.getFrontWheel().getRadius();
        SGD = new ScaleGestureDetector(c, new touchZoomListener());
    }

    private void init(Canvas c) {
        mPaddleLength = new DrawableCircle(Color.GREEN, mStrikeWidth);
        mPaddles = new DrawableCircle(Color.CYAN, mStrikeWidth);
        mSaddle = new DrawableCircle(Color.BLACK, mStrikeWidth);
        mSteeringLength = new DrawableCircle(Color.MAGENTA, mStrikeWidth);
        mFrameFront = new DrawableCircle(Color.GRAY, mStrikeWidth);
        mFrontWheel = new DrawableCircle(Color.YELLOW, mStrikeWidth);
        mBackWheel = new DrawableCircle(Color.YELLOW, mStrikeWidth);

        mDrawableCircles = new ArrayList<>();
        mDrawableCircles.add(mPaddleLength);
        mDrawableCircles.add(mPaddles);
        mDrawableCircles.add(mSaddle);
        mDrawableCircles.add(mSteeringLength);
        mDrawableCircles.add(mFrameFront);
        mDrawableCircles.add(mFrontWheel);
        mDrawableCircles.add(mBackWheel);

        mPaintRed = new Paint();
        mPaintRed.setColor(Color.RED);
        mPaintRed.setStyle(Paint.Style.STROKE);
        mPaintRed.setStrokeWidth(mStrikeWidth);

        mPaintYellow = new Paint();
        mPaintYellow.setColor(Color.YELLOW);
        mPaintYellow.setStyle(Paint.Style.STROKE);
        mPaintYellow.setStrokeWidth(mStrikeWidth);

        mTextColor = new Paint();
        mTextColor.setAntiAlias(true);
        mTextColor.setTextSize(36);
        mTextColor.setColor(Color.BLACK);
        mTextColor.setStyle(Paint.Style.FILL);

        mTextBorder = new Paint();
        mTextBorder.setAntiAlias(true);
        mTextBorder.setTextSize(36);
        mTextBorder.setColor(Color.WHITE);
        mTextBorder.setStyle(Paint.Style.STROKE);
        mTextBorder.setStrokeWidth(9);

        setFocusable(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mSaddle == null) {
            init(canvas);
        }

        if(!mHideFrame) {
            mPath = new Path();
            mPath.moveTo((float) mBikePartPositions.getFrontWheel().getCenter().x, (float) mBikePartPositions.getFrontWheel().getCenter().y);
            mPath.lineTo((float) mBikePartPositions.getFrameFront().x, (float) mBikePartPositions.getFrameFront().y);
            mPath.lineTo((float) mBikePartPositions.getSteering().x, (float) mBikePartPositions.getSteering().y);
            mPath.lineTo((float) mBikePartPositions.getSteeringLength().x, (float) mBikePartPositions.getSteeringLength().y);
            mPath.moveTo((float) mBikePartPositions.getFrameFront().x, (float) mBikePartPositions.getFrameFront().y);
            mPath.lineTo((float) mBikePartPositions.getPaddles().x, (float) mBikePartPositions.getPaddles().y);
            mPath.moveTo((float) mBikePartPositions.getFrameFront().x, (float) mBikePartPositions.getFrameFront().y);
            mPath.lineTo((float) mBikePartPositions.getFrameBack().x, (float) mBikePartPositions.getFrameBack().y);
            mPath.lineTo((float) mBikePartPositions.getSaddle().x, (float) mBikePartPositions.getSaddle().y);
            mPath.moveTo((float) mBikePartPositions.getFrameBack().x, (float) mBikePartPositions.getFrameBack().y);
            mPath.lineTo((float) mBikePartPositions.getPaddles().x, (float) mBikePartPositions.getPaddles().y);
            mPath.lineTo((float) mBikePartPositions.getBackWheel().getCenter().x, (float) mBikePartPositions.getBackWheel().getCenter().y);
            canvas.drawPath(mPath, mPaintRed);

            mPaddles.draw(canvas, mBikePartPositions.getPaddles(), mPointRadius);
            mPaddleLength.draw(canvas, mBikePartPositions.getPaddlesLength(), mPointRadius);
            mSaddle.draw(canvas, mBikePartPositions.getSaddle(), mPointRadius);
            mFrameFront.draw(canvas, mBikePartPositions.getFrameFront(), mPointRadius);
            mSteeringLength.draw(canvas, mBikePartPositions.getSteeringLength(), mPointRadius);
        }
        mFrontWheel.draw(canvas, mBikePartPositions.getFrontWheel().getCenter(), mPointRadius, mBikePartPositions.getFrontWheel().getRadius());
        mBackWheel.draw(canvas, mBikePartPositions.getBackWheel().getCenter(), mPointRadius, mBikePartPositions.getBackWheel().getRadius());

        double pixelToCm = mBikePartPositions.getFrontWheel().getRadius() * 2 / ((double) mBikePartPositions.getWheelSize() / 10);

        int frameLength = (int)(getDistance(mBikePartPositions.getFrameFront(), mBikePartPositions.getFrameBack()) / pixelToCm);
        int frameHeight = (int)(getDistance(mBikePartPositions.getFrameBack(), mBikePartPositions.getPaddles()) / pixelToCm);
        int saddleHeight = (int)(getDistance(mBikePartPositions.getSaddle(), mBikePartPositions.getFrameBack()) / pixelToCm);
        int steeringLength = (int)(getDistance(mBikePartPositions.getSteering(), mBikePartPositions.getSteeringLength()) / pixelToCm);

        if(!mHideFrame) {
            drawMeasurement(canvas, frameLength, mBikeSize.getFrameLength(), mBikePartPositions.getFrameFront(), mBikePartPositions.getFrameBack());
            drawMeasurement(canvas, frameHeight, mBikeSize.getFrameHeight(), mBikePartPositions.getFrameBack(), mBikePartPositions.getPaddles());
            drawMeasurement(canvas, saddleHeight, mBikeSize.getSaddleHeight() - mBikeSize.getFrameHeight(), mBikePartPositions.getSaddle(), mBikePartPositions.getFrameBack());
            drawMeasurement(canvas, steeringLength, mBikeSize.getSteeringLength(), mBikePartPositions.getSteering(), mBikePartPositions.getSteeringLength());
        }
    }

    private double getDistance(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }


    private void drawMeasurement(Canvas c, double currentLength, double optimalLength, Point a, Point b) {
        float x = (float) (Math.min(a.x, b.x) + Math.abs(a.x - b.x) / 2 - 50);
        float y = (float) (Math.min(a.y, b.y) + Math.abs(a.y - b.y) / 2 + 20);

        String lengthText = currentLength + " cm";

        c.drawText(lengthText, x, y, mTextBorder);
        c.drawText(lengthText, x, y, mTextColor);

        double diff = optimalLength - currentLength;
        double absDiff = Math.abs(diff);

        int maxDiff = 8;
        // change to color from green over yellow to red according to difference
        // 0 = 00ff00
        // maxDiff / 2 = ffff00
        // maxDiff = ff0000
        int greenValue = 255;
        int redValue = 0;
        if (absDiff > 0) {
            double half = maxDiff / 2d;
            double diffFactor = 255 / half;
            // diff of 0 - half should result in 255. anything half - maxDiff should be substracted from 255 so maxDiff is 0 for green.
            //greenValue = Math.max(0, 255 - (int)(diffFactor * Math.max(0, absDiff - half)));
            greenValue = Math.max(0, 255 - (int) (diffFactor * Math.min(half, absDiff)));
            redValue = Math.min(255, (int) (diffFactor * Math.min(half, absDiff)));
        }

        int diffColor = 255 << 24 | redValue << 16 | greenValue << 8;
        String diffText = (diff > 0 ? "+" : "") + (int) diff;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(36);
        paint.setColor(diffColor);
        paint.setStyle(Paint.Style.FILL);

        c.drawText(diffText, x + mTextBorder.measureText(lengthText) + 5, y, mTextBorder);
        c.drawText(diffText, x + mTextBorder.measureText(lengthText) + 5, y, paint);
    }

    public void setSeekbar_BackTyre(int percent) {
        mBikePartPositions.getBackWheel().setRadius(getScaledTyreSize(percent));
        invalidate();
    }
    public void setSeekbar_FrontTyre(int percent) {
        mBikePartPositions.getFrontWheel().setRadius(getScaledTyreSize(percent));
        invalidate();
    }

    private int getScaledTyreSize(int percent) {
        return (int)(mDefaultTyreSize + (mDefaultTyreSize * (percent - 50 * 0.01)));
    }

    public boolean onTouch(MotionEvent event) {
        int action = event.getAction();
        //bicycle circle

        switch(event.getAction()) {
            case MotionEvent.ACTION_BUTTON_PRESS:
            case MotionEvent.ACTION_DOWN:
                mNearestPressedPoint = getClosestPoint(event);
                if(mTyresDisabled && (mNearestPressedPoint == mFrontWheel || mNearestPressedPoint == mBackWheel)) {
                    mNearestPressedPoint = null;
                }
                break;
            case MotionEvent.ACTION_BUTTON_RELEASE:
            case MotionEvent.ACTION_UP:
                mNearestPressedPoint = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mNearestPressedPoint != null) {
                    float dx = event.getX();
                    float dy = event.getY();


                    if(action == MotionEvent.ACTION_DOWN) {
                        dx =  Math.signum(dx) * 25;
                        dy = Math.signum(dy) * 25;
                    }

                    if (action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_DOWN) {
                        mNearestPressedPoint.getCenter().x = (int)Math.max(0, Math.min(getWidth(), dx));
                        mNearestPressedPoint.getCenter().y = (int)Math.max(0, Math.min(getHeight(), dy));
                    }
                }
                break;
        }

        invalidate();
        return true;
    }

    private DrawableCircle getClosestPoint(MotionEvent event) {
        final float touchX = event.getX();
        final float touchY = event.getY();

        DrawableCircle closestCircle = null;
        double closestDiff = 0d;

        //mBikePartPositions.getClosestComponent(new Point(touchX, touchY));
        if(mDrawableCircles != null) {
            for (DrawableCircle circle : mDrawableCircles) {
                double diff = Math.sqrt(Math.pow(touchX - circle.getCenter().x, 2) + Math.pow(touchY - circle.getCenter().y, 2));
                if (closestCircle == null || diff < closestDiff) {
                    closestCircle = circle;
                    closestDiff = diff;
                }
            }
        }

        return closestCircle;
    }

    @Override
    public void run() {
        invalidate();
    }

    public void hideFrame(boolean b) {
        mHideFrame = b;
    }

    public void disableTyres(boolean b) {
        mTyresDisabled = b;
    }


    private class touchZoomListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            /*
            if(xDir > detector.getCurrentSpanX() && yDir > detector.getCurrentSpanY()) {
                circleBackTyreSize = circleBackTyreSize - 5;
            }
            if(xDir < detector.getCurrentSpanX() && yDir < detector.getCurrentSpanY()) {
                circleBackTyreSize = circleBackTyreSize + 5;
            }

            xDir = detector.getCurrentSpanX();
            yDir = detector.getCurrentSpanY();
            */


            return true;
        }
    }
}
