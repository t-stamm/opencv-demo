package com.catira.opencvdemo.activities.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
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
    private Path triangle;

    private String farbe;

    private Canvas canvasRed, canvasBlue, canvasGreen;
    private Bitmap pointBlueBitmap, pointRedBitmap, pointGreenBitmap;
    private Paint paint, paintRed, paintBlue, paintGreen;
    private boolean init = true;

    Paint red_paintbrush_fill, blue_paintbrush_fill, green_paintbrush_fill;
    Paint red_paintbrush_stroke, blue_paintbrush_stroke, green_paintbrush_stroke;

    private int touch = 0;

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
            prePaintBrushes();
            setFocusable(true);
        }

    }

    public void updateMouse(String setFarbe, int x, int y) {
        farbe = setFarbe;
        //canvasRed.drawCircle(x, y, 17, paintRed);
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
            xGreen = (getWidth() / 2) + (getWidth() / 6);;
            yGreen = (getHeight() / 2) - (getHeight() / 3);
            CreatePoints(canvas, xpos, ypos, xRed, yRed, xGreen, yGreen);
        }

        if(farbe == "blue" && init == false && touch == 0) {
            //CreatePoint(canvas, xpos, ypos);
            //canvas.drawCircle(xpos, ypos, 17, paintBlue);
        }

        if(touch == 1) {
            //canvas.restore();
            //canvasRed.restore();
            //CreatePoint(canvas, xpos, ypos);

        }

        if(farbe == "red" && init == false && touch == 0) {
            //CreatePoint(canvas, xpos, ypos);
            //Wie speichert man x und y für ein Punkt
            //xRed = xpos;
            //yRed = ypos;
            //canvas.drawCircle(xpos, ypos, 17, paintRed);
        }

        if (canvasBlue != null  && canvasRed != null && init != true) {
            CreatePoints(canvas, xpos, ypos, xRed, yRed, xGreen, yGreen);
        }



        init=false;
    }

    private void CreatePoints(Canvas canvas, float xpos, float ypos, float xRed, float yRed, float xGreen, float yGreen) {

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

        triangle = new Path();
        triangle.moveTo(xRed, yRed);
        triangle.lineTo(xGreen, yGreen);
        triangle.moveTo(xGreen, yGreen);
        triangle.lineTo(xpos, ypos);
        triangle.moveTo(xpos, ypos);
        triangle.lineTo(xRed, yRed);

        canvas.drawPath(triangle, red_paintbrush_stroke);
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

        double cRed = Math.sqrt(Math.pow(aRed,2)+ Math.pow(bRed,2));
        double cBlue = Math.sqrt(Math.pow(aBlue,2)+ Math.pow(bBlue,2));
        double cGreen = Math.sqrt(Math.pow(aGreen,2)+ Math.pow(bGreen,2));

        //for (int i=0;i<countPoint;i++)
        if (cRed < cBlue && cRed < cGreen) {
            circle = "red";
        }
        if (cBlue < cRed && cBlue < cGreen){
            circle = "blue";
        }

        if(cGreen < cRed && cGreen < cBlue) {
            circle = "green";
        }

        return circle;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
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

        red_paintbrush_stroke = new Paint();
        red_paintbrush_stroke.setColor(Color.RED);
        red_paintbrush_stroke.setStyle(Paint.Style.STROKE);
        red_paintbrush_stroke.setStrokeWidth(10);

        blue_paintbrush_stroke = new Paint();
        blue_paintbrush_stroke.setColor(Color.RED);
        blue_paintbrush_stroke.setStyle(Paint.Style.STROKE);
        blue_paintbrush_stroke.setStrokeWidth(10);

        green_paintbrush_stroke = new Paint();
        green_paintbrush_stroke.setColor(Color.RED);
        green_paintbrush_stroke.setStyle(Paint.Style.STROKE);
        green_paintbrush_stroke.setStrokeWidth(10);
    }

}
