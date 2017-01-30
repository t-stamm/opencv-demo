package com.catira.opencvdemo.activities.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * Created by ck on 10.01.2017.
 */

public class MoveViewComponent extends View {
    private float xpos = -1;
    private float ypos = -1;
    private float xRed = -1;
    private float yRed = -1;
    private float xGreen = -1;
    private float yGreen = -1;
    private float xCyan = -1;
    private float yCyan = -1;
    private float xBlack = -1;
    private float yBlack = -1;
    private float xGray = -1;
    private float yGray = -1;
    private float xMagenta = -1;
    private float yMagenta = -1;
    private float xBackTyre = -1;
    private float yBackTyre= -1;
    private float xFrontTyre = -1;
    private float yFrontTyre= -1;
    private Path triangle;
    private ScaleGestureDetector SGD;
    private float circleBackTyreSize = 70;
    private float circleFrontTyreSize = 70;
    private boolean circleBackTyre = false;
    //temporär
    private float xDir = -1;
    private float yDir = -1;

    private String farbe;

    private Canvas canvasRed, canvasBlue, canvasGreen, canvasCyan, canvasBlack, canvasGray, canvasMagenta;
    private Bitmap pointBlueBitmap, pointRedBitmap, pointGreenBitmap, pointCyanBitmap,
            pointBlackBitmap, pointGrayBitmap, pointMagentaBitmap;
    private Paint paint, paintRed, paintBlue, paintGreen, paintCyan, paintBlack, paintGray, paintMagenta;
    private boolean init = true;

    Paint red_paintbrush_fill, blue_paintbrush_fill, green_paintbrush_fill, cyan_paintbrush_fill,
            black_paintbrush_fill;
    Paint red_paintbrush_stroke, blue_paintbrush_stroke, green_paintbrush_stroke, yellow_paintbrush_stroke,
            cyan_paintbrush_stroke, black_paintbrush_stroke;

    private int touch = 0;

    private boolean drawingPoints = false;

    /*
    Initialisierung mit allen Elementen die in der View verschoben werden können.
     D. h. die Komponente wird maximal einmal pro View instanziiert.
    Hört an der Parent-View der einzelnen Elemente auf das Touch Event und
    berechnet selber welches View Element bewegt werden soll.
     */

    //  view = new ZeichnenView(this);
    public MoveViewComponent(Context c) {
        super(c);
        if (init = true) {
            //Ereignisse empfängbar
            //Paint Objekte vorbereiten
            prePaintBrushes();
            //set multitouch
            SGD = new ScaleGestureDetector(c, new touchZoomListener());

            setFocusable(true);
        }

    }

    public void updateMouse() {
        if(circleBackTyre == false) {
            circleBackTyre = true;
            drawingPoints = false;
        } else {
            circleBackTyre = false;
            drawingPoints = true;
        }
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (init==true) {
            //Initialization Points
            xpos = (getWidth() / 2) - (getWidth() / 13);
            ypos = (getHeight() / 2) - (getHeight() / 12);
            xRed = (getWidth() / 2) - (getWidth() / 5);
            yRed = (getHeight() / 2) - (getHeight() / (float)2.4);
            xBlack = (getWidth() / 2) - (getWidth() / 5);
            yBlack = (getHeight() / 2) - (getHeight() / (float)2.2);
            xGreen = (getWidth() / 2) + (getWidth() / 6);
            yGreen = (getHeight() / 2) - (getHeight() / 3);
            xCyan = (getWidth() / 2) + (getWidth() / 14);
            yCyan = (getHeight() / 2) - (getHeight() / 14);
            xGray = (getWidth() / 2) + (getWidth() / 6);
            yGray = (getHeight() / 2) - (getHeight() / (float)2.4);
            xMagenta = (getWidth() / 2) + (getWidth() / 4);
            yMagenta = (getHeight() / 2) - (getHeight() / (float)2.2);
            xBackTyre = (getWidth() / 2) - (getWidth() / 4);
            yBackTyre = (getHeight() / 2);
            xFrontTyre = (getWidth() / 2) + (getWidth() / 4);
            yFrontTyre = (getHeight() / 2);
            if(drawingPoints == true) {
                CreatePoints(canvas, xpos, ypos, xRed, yRed, xGreen, yGreen, xCyan, yCyan,
                        xBlack, yBlack, xGray, yGray, xMagenta, yMagenta);
                //Drawing line from point to  point
                linefromPointToPoint(canvas, xpos, ypos, xRed, yRed, xGreen, yGreen, xCyan, yCyan,
                        xBlack, yBlack,xGray, yGray, xMagenta, yMagenta);
            }

            // Change allow circleBackTyre
            circleBackTyre = true;
            //Drawing bicycle circle
            bicycle_tyre_circle(canvas, xBackTyre, yBackTyre,
                                        xFrontTyre, yFrontTyre,
                                        circleBackTyreSize, circleFrontTyreSize);
        }


        //update
        if ( init != true ) {
            //Drawing Points
            if(drawingPoints == true) {
                CreatePoints(canvas, xpos, ypos, xRed, yRed, xGreen, yGreen, xCyan, yCyan, xBlack, yBlack,
                        xGray, yGray, xMagenta, yMagenta);
                //Drawing line from point to  point
                linefromPointToPoint(canvas, xpos, ypos, xRed, yRed, xGreen, yGreen, xCyan, yCyan,
                        xBlack, yBlack, xGray, yGray, xMagenta, yMagenta);
            }
            else {
                //Drawing bicycle circle
                bicycle_tyre_circle(canvas, xBackTyre, yBackTyre,
                        xFrontTyre, yFrontTyre,
                        circleBackTyreSize, circleFrontTyreSize);
            }
        }



        init=false;
    }

    private void CreatePoints(Canvas canvas, float xpos, float ypos, float xRed, float yRed,
                              float xGreen, float yGreen, float xCyan, float yCyan,
                              float xBlack, float yBlack, float xGray, float yGray,
                              float xMagenta, float yMagenta) {


        paintRed = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointRedBitmap  = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        canvasRed = new Canvas(pointRedBitmap);
        paintRed.setColor(Color.RED);
        paintRed.setStrokeWidth(20);
        canvasRed.drawCircle(xRed, yRed, 17, paintRed);
        canvas.drawBitmap(pointRedBitmap, 0, 0, paintRed);

        paintBlue = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointBlueBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        canvasBlue = new Canvas(pointBlueBitmap);
        paintBlue.setColor(Color.BLUE);
        paintBlue.setStrokeWidth(20);
        canvasBlue.drawCircle(xpos, ypos, 17, paintBlue);
        //canvasBlue.drawCircle(xpos, ypos, 17, paintBlue);
        //canvas.drawCircle(xpos, ypos, 17, paintBlue);
        canvas.drawBitmap(pointBlueBitmap, 0, 0, paintBlue);

        paintGreen = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointGreenBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        canvasGreen = new Canvas(pointGreenBitmap);
        paintGreen.setColor(Color.GREEN);
        paintGreen.setStrokeWidth(20);
        canvasGreen.drawCircle(xGreen, yGreen, 17, paintGreen);
        //canvasBlue.drawCircle(xpos, ypos, 17, paintBlue);
        //canvas.drawCircle(xpos, ypos, 17, paintBlue);
        canvas.drawBitmap(pointGreenBitmap, 0, 0, paintGreen);

        paintCyan = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointCyanBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        canvasCyan = new Canvas(pointCyanBitmap);
        paintCyan.setColor(Color.CYAN);
        paintCyan.setStrokeWidth(20);
        canvasCyan.drawCircle(xCyan, yCyan, 17, paintCyan);
        //canvasBlue.drawCircle(xpos, ypos, 17, paintBlue);
        //canvas.drawCircle(xpos, ypos, 17, paintBlue);
        canvas.drawBitmap(pointCyanBitmap, 0, 0, paintCyan);

        paintBlack = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointBlackBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        canvasBlack = new Canvas(pointBlackBitmap);
        paintBlack.setColor(Color.BLACK);
        paintBlack.setStrokeWidth(20);
        canvasBlack.drawCircle(xBlack, yBlack, 17, paintBlack);
        //canvasBlue.drawCircle(xpos, ypos, 17, paintBlue);
        //canvas.drawCircle(xpos, ypos, 17, paintBlue);
        canvas.drawBitmap(pointBlackBitmap, 0, 0, paintBlack);

        paintGray = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointGrayBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        canvasGray = new Canvas(pointGrayBitmap);
        paintGray.setColor(Color.GRAY);
        paintGray.setStrokeWidth(20);
        canvasGray.drawCircle(xGray, yGray, 17, paintGray);
        //canvasBlue.drawCircle(xpos, ypos, 17, paintBlue);
        //canvas.drawCircle(xpos, ypos, 17, paintBlue);
        canvas.drawBitmap(pointGrayBitmap, 0, 0, paintGray);

        paintMagenta = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointMagentaBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        canvasMagenta = new Canvas(pointBlackBitmap);
        paintMagenta.setColor(Color.MAGENTA);
        paintMagenta.setStrokeWidth(20);
        canvasMagenta.drawCircle(xMagenta, yMagenta, 17, paintMagenta);
        //canvasBlue.drawCircle(xpos, ypos, 17, paintBlue);
        //canvas.drawCircle(xpos, ypos, 17, paintBlue);
        canvas.drawBitmap(pointMagentaBitmap, 0, 0, paintMagenta);
    }

    private void linefromPointToPoint (Canvas canvas, float xpos, float ypos, float xRed, float yRed,
                                       float xGreen, float yGreen, float xCyan, float yCyan,
                                       float xBlack, float yBlack, float xGray, float yGray,
                                       float xMagenta, float yMagenta) {
        triangle = new Path();
        triangle.moveTo(xRed, yRed);
        triangle.lineTo(xGreen, yGreen);
        triangle.moveTo(xGreen, yGreen);
        triangle.lineTo(xGray, yGray);
        triangle.moveTo(xGray, yGray);
        triangle.lineTo(xMagenta, yMagenta);
        triangle.moveTo(xGreen, yGreen);
        triangle.lineTo(xpos, ypos);
        triangle.lineTo(xCyan, yCyan);
        triangle.moveTo(xpos, ypos);
        triangle.lineTo(xRed, yRed);
        triangle.lineTo(xBlack, yBlack);


        canvas.drawPath(triangle, red_paintbrush_stroke);
    }

    private void bicycle_tyre_circle(Canvas canvas,
                                     float xBackTyre, float yBackTyre,
                                     float xFrontTyre, float yFrontTyre,
                                     float circleBackTyreSize, float circleFrontTyreSize){
        canvas.drawCircle(xBackTyre, yBackTyre, circleBackTyreSize, green_paintbrush_stroke);
        canvas.drawCircle(xFrontTyre, yFrontTyre, circleFrontTyreSize, yellow_paintbrush_stroke);
    }

    private String whichCircle(MotionEvent event, int countPoint) {
        String circle = null;
        float touchX = event.getX();
        float touchY = event.getY();

        double aRed = touchX - xRed;
        double bRed = touchY - yRed;

        double aBlue = touchX - xpos;
        double bBlue = touchY - ypos;

        double aGreen = touchX - xGreen;
        double bGreen = touchY - yGreen;

        double aCyan = touchX - xCyan;
        double bCyan = touchY - yCyan;

        double aBlack = touchX - xBlack;
        double bBlack = touchY - yBlack;

        double aGray= touchX - xGray;
        double bGray = touchY - yGray;

        double aMagenta = touchX - xMagenta;
        double bMagenta = touchY - yMagenta;

        double cRed = Math.sqrt(Math.pow(aRed,2)+ Math.pow(bRed,2));
        double cBlue = Math.sqrt(Math.pow(aBlue,2)+ Math.pow(bBlue,2));
        double cGreen = Math.sqrt(Math.pow(aGreen,2)+ Math.pow(bGreen,2));
        double cCyan = Math.sqrt(Math.pow(aCyan,2)+ Math.pow(bCyan,2));
        double cBlack = Math.sqrt(Math.pow(aBlack,2)+ Math.pow(bBlack,2));
        double cGray = Math.sqrt(Math.pow(aGray,2)+ Math.pow(bGray,2));
        double cMagenta = Math.sqrt(Math.pow(aMagenta,2)+ Math.pow(bMagenta,2));

        //for (int i=0;i<countPoint;i++)
        if (cRed < cBlue && cRed < cGreen && cRed < cCyan && cRed < cBlack
                && cRed < cGray && cRed < cMagenta) {
            circle = "red";
        }
        if (cBlue < cRed && cBlue < cGreen && cBlue < cCyan && cRed < cBlack
                && cBlue < cGray && cBlue < cMagenta) {
            circle = "blue";
        }

        if(cGreen < cRed && cGreen < cBlue && cGreen < cCyan && cRed < cBlack
                && cGreen < cGray && cGreen < cMagenta)  {
            circle = "green";
        }

        if(cCyan < cRed && cCyan < cBlue && cCyan < cGreen && cRed < cBlack
                && cCyan < cGray && cCyan < cMagenta)  {
            circle = "cyan";
        }
        if(cBlack < cRed && cBlack < cBlue && cBlack < cGreen && cBlack < cCyan
                && cBlack < cGray && cBlack < cMagenta)  {
            circle = "black";
        }
        if(cGray < cRed && cGray < cBlue && cGray < cBlack && cGray < cCyan
                && cGray < cGreen && cGray < cMagenta)  {
            circle = "gray";
        }
        if(cMagenta < cRed && cMagenta < cBlue && cMagenta < cGreen && cMagenta < cCyan
                && cMagenta < cGray && cMagenta < cBlack)  {
            circle = "magenta";
        }

        return circle;
    }
    private String whichCircleTyre(MotionEvent event) {
        String circleTyre = null;
        float touchX = event.getX();
        float touchY = event.getY();

        double aGreen = touchX - xBackTyre;
        double bGreen = touchY - yBackTyre;

        double aYellow = touchX - xFrontTyre;
        double bYellow = touchY - yFrontTyre;



        double cGreen = Math.sqrt(Math.pow(aGreen,2)+ Math.pow(bGreen,2));
        double cYellow = Math.sqrt(Math.pow(aYellow,2)+ Math.pow(bYellow,2));


        //for (int i=0;i<countPoint;i++)
        if (cGreen < cYellow ) {
            circleTyre = "green";
        }
        if (cYellow < cGreen ){
            circleTyre = "yellow";
        }


        return circleTyre;
    }


    public boolean onTouch(MotionEvent event) {
        int action = event.getAction();
        //bicycle circle
        if(circleBackTyre == true){

            String nearestTyre = null;
            nearestTyre = whichCircleTyre(event);

            if(nearestTyre == "green") {
                float dx = event.getX() - xBackTyre;
                float dy = event.getY() - yBackTyre;


                if(action == MotionEvent.ACTION_MOVE) {
                    xBackTyre += dx;
                    yBackTyre += dy;
                }
                else  if(action == MotionEvent.ACTION_DOWN ) {
                    xBackTyre += Math.signum(dx) * 25;
                    yBackTyre += Math.signum(dy) * 25;
                }

                if(xBackTyre < 0) {
                    xBackTyre = 0;
                }
                if(xBackTyre > getWidth()) {
                    xBackTyre = getWidth();
                }
                if(yBackTyre < 0) {
                    yBackTyre = 0;
                }
                if(yBackTyre > getHeight()) {
                    yBackTyre = getHeight();
                }
            }
            if(nearestTyre == "yellow") {
                float dx = event.getX() - xFrontTyre;
                float dy = event.getY() - yFrontTyre;


                if(action == MotionEvent.ACTION_MOVE) {
                    xFrontTyre += dx;
                    yFrontTyre += dy;
                }
                else  if(action == MotionEvent.ACTION_DOWN ) {
                    xFrontTyre += Math.signum(dx) * 25;
                    yFrontTyre += Math.signum(dy) * 25;
                }

                if(xFrontTyre < 0) {
                    xFrontTyre = 0;
                }
                if(xFrontTyre > getWidth()) {
                    xFrontTyre = getWidth();
                }
                if(yFrontTyre < 0) {
                    yFrontTyre = 0;
                }
                if(yFrontTyre > getHeight()) {
                    yFrontTyre = getHeight();
                }
            }
            SGD.onTouchEvent(event);
        }
        if(circleBackTyre == false){

            String nearestPoint = null;
            int countPoint = 3;
            touch = 1;
            //Canvas Position ermitteln!?

            //neareset Circle
            nearestPoint = whichCircle(event, countPoint);
            //Test blau/red erkannt

            if (nearestPoint == "blue") {

                float dx = event.getX() - xpos;
                float dy = event.getY() - ypos;


                if(action == MotionEvent.ACTION_MOVE) {
                    xpos += dx;
                    ypos += dy;
                }
                else  if(action == MotionEvent.ACTION_DOWN ) {
                    xpos += Math.signum(dx) * 25;
                    ypos += Math.signum(dy) * 25;
                }

                if(xpos < 0) {
                    xpos = 0;
                }
                if(xpos > getWidth()) {
                    xpos = getWidth();
                }
                if(ypos < 0) {
                    ypos = 0;
                }
                if(ypos > getHeight()) {
                    ypos = getHeight();
                }

            }

            if (nearestPoint == "red") {

                float dx = event.getX() - xRed;
                float dy = event.getY() - yRed;


                if(action == MotionEvent.ACTION_MOVE) {
                    xRed += dx;
                    yRed += dy;
                }
                else  if(action == MotionEvent.ACTION_DOWN ) {
                    xRed += Math.signum(dx) * 25;
                    yRed += Math.signum(dy) * 25;
                }

                if(xRed < 0) {
                    xRed = 0;
                }
                if(xRed > getWidth()) {
                    xRed = getWidth();
                }
                if(yRed < 0) {
                    yRed = 0;
                }
                if(yRed > getHeight()) {
                    yRed = getHeight();
                }
            }
            if (nearestPoint == "green") {

                float dx = event.getX() - xGreen;
                float dy = event.getY() - yGreen;


                if(action == MotionEvent.ACTION_MOVE) {
                    xGreen += dx;
                    yGreen += dy;
                }
                else  if(action == MotionEvent.ACTION_DOWN ) {
                    xGreen += Math.signum(dx) * 25;
                    yGreen += Math.signum(dy) * 25;
                }

                if(xGreen < 0) {
                    xGreen = 0;
                }
                if(xGreen > getWidth()) {
                    xGreen = getWidth();
                }
                if(yGreen < 0) {
                    yGreen = 0;
                }
                if(yGreen > getHeight()) {
                    yGreen = getHeight();
                }
            }
            if (nearestPoint == "cyan") {

                float dx = event.getX() - xCyan;
                float dy = event.getY() - yCyan;


                if(action == MotionEvent.ACTION_MOVE) {
                    xCyan += dx;
                    yCyan += dy;
                }
                else  if(action == MotionEvent.ACTION_DOWN ) {
                    xCyan += Math.signum(dx) * 25;
                    yCyan += Math.signum(dy) * 25;
                }

                if(xCyan < 0) {
                    xCyan = 0;
                }
                if(xCyan > getWidth()) {
                    xCyan = getWidth();
                }
                if(yCyan < 0) {
                    yCyan = 0;
                }
                if(yCyan > getHeight()) {
                    yCyan = getHeight();
                }
            }
            if (nearestPoint == "black") {

                float dx = event.getX() - xBlack;
                float dy = event.getY() - yBlack;


                if(action == MotionEvent.ACTION_MOVE) {
                    xBlack += dx;
                    yBlack += dy;
                }
                else  if(action == MotionEvent.ACTION_DOWN ) {
                    xBlack += Math.signum(dx) * 25;
                    yBlack += Math.signum(dy) * 25;
                }

                if(xBlack < 0) {
                    xBlack = 0;
                }
                if(xBlack > getWidth()) {
                    xBlack = getWidth();
                }
                if(yBlack < 0) {
                    yBlack = 0;
                }
                if(yBlack > getHeight()) {
                    yBlack = getHeight();
                }
            }
            if (nearestPoint == "gray") {

                float dx = event.getX() - xGray;
                float dy = event.getY() - yGray;


                if(action == MotionEvent.ACTION_MOVE) {
                    xGray += dx;
                    yGray += dy;
                }
                else  if(action == MotionEvent.ACTION_DOWN ) {
                    xGray += Math.signum(dx) * 25;
                    yGray += Math.signum(dy) * 25;
                }

                if(xGray < 0) {
                    xGray = 0;
                }
                if(xGray > getWidth()) {
                    xGray = getWidth();
                }
                if(yGray < 0) {
                    yGray = 0;
                }
                if(yGray > getHeight()) {
                    yGray = getHeight();
                }
            }
            if (nearestPoint == "magenta") {

                float dx = event.getX() - xMagenta;
                float dy = event.getY() - yMagenta;


                if(action == MotionEvent.ACTION_MOVE) {
                    xMagenta += dx;
                    yMagenta += dy;
                }
                else  if(action == MotionEvent.ACTION_DOWN ) {
                    xMagenta += Math.signum(dx) * 25;
                    yMagenta += Math.signum(dy) * 25;
                }

                if(xMagenta < 0) {
                    xMagenta = 0;
                }
                if(xMagenta > getWidth()) {
                    xMagenta = getWidth();
                }
                if(yMagenta < 0) {
                    yMagenta = 0;
                }
                if(yMagenta > getHeight()) {
                    yMagenta = getHeight();
                }
            }
        }
        invalidate();
        return true;
    }

    private void prePaintBrushes() {
        red_paintbrush_fill = new Paint();
        red_paintbrush_fill.setColor(Color.RED);
        red_paintbrush_fill.setStyle(Paint.Style.FILL);

        blue_paintbrush_fill = new Paint();
        blue_paintbrush_fill.setColor(Color.RED);
        blue_paintbrush_fill.setStyle(Paint.Style.FILL);

        green_paintbrush_fill = new Paint();
        green_paintbrush_fill.setColor(Color.RED);
        green_paintbrush_fill.setStyle(Paint.Style.FILL);

        cyan_paintbrush_fill = new Paint();
        cyan_paintbrush_fill.setColor(Color.CYAN);
        cyan_paintbrush_fill.setStyle(Paint.Style.FILL);

        red_paintbrush_stroke = new Paint();
        red_paintbrush_stroke.setColor(Color.RED);
        red_paintbrush_stroke.setStyle(Paint.Style.STROKE);
        red_paintbrush_stroke.setStrokeWidth(10);

        blue_paintbrush_stroke = new Paint();
        blue_paintbrush_stroke.setColor(Color.RED);
        blue_paintbrush_stroke.setStyle(Paint.Style.STROKE);
        blue_paintbrush_stroke.setStrokeWidth(10);

        green_paintbrush_stroke = new Paint();
        green_paintbrush_stroke.setColor(Color.GREEN);
        green_paintbrush_stroke.setStyle(Paint.Style.STROKE);
        green_paintbrush_stroke.setStrokeWidth(10);

        yellow_paintbrush_stroke = new Paint();
        yellow_paintbrush_stroke.setColor(Color.YELLOW);
        yellow_paintbrush_stroke.setStyle(Paint.Style.STROKE);
        yellow_paintbrush_stroke.setStrokeWidth(10);

        cyan_paintbrush_stroke = new Paint();
        cyan_paintbrush_stroke.setColor(Color.CYAN);
        cyan_paintbrush_stroke.setStyle(Paint.Style.STROKE);
        cyan_paintbrush_stroke.setStrokeWidth(10);

        black_paintbrush_stroke = new Paint();
        black_paintbrush_stroke.setColor(Color.BLACK);
        black_paintbrush_stroke.setStyle(Paint.Style.STROKE);
        black_paintbrush_stroke.setStrokeWidth(10);
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

    public void setSeekbar_BackTypre(int prozess) {
        circleBackTyreSize = (float)prozess;
        invalidate();
    }
    public void setSeekbar_FrontTypre(int prozess) {
        circleFrontTyreSize = (float)prozess;
        invalidate();
    }
}
