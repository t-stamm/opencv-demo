package com.catira.opencvdemo.activities.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ck on 10.01.2017.
 */

public class MoveViewComponent extends View {
    private float xpos = -1;
    private float ypos = -1;
    private String farbe;

    private Canvas canvasRed, canvasBlue;
    private Bitmap pointBlueBitmap;
    private Bitmap pointRedBitmap;
    private Paint paint, paintRed, paintBlue;
    private boolean init = true;

    private float xRed = -1;
    private float yRed = -1;
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
            xpos = (getWidth() / 2) + 200;
            ypos = (getHeight() / 2) - 200;
            xRed = xpos - 400;
            yRed = ypos;
            CreatePoint(canvas, xpos, ypos, xRed, yRed);

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
/*
        if (canvasRed != null && init != true) {
            farbe = "red";
            CreatePoint(canvas, xpos, ypos, xRed, yRed);

        }
        if (canvasBlue != null  && init != true) {
            farbe = "blue";
            CreatePoint(canvas, xpos, ypos, xRed, yRed);
        }
*/
        if (canvasBlue != null  && canvasRed != null && init != true) {
            CreatePoint(canvas, xpos, ypos, xRed, yRed);
        }
        init=false;
    }

    private void CreatePoint(Canvas canvas, float xpos, float ypos, float xRed, float yRed) {
        /*
        if (farbe == "red") {
            paintRed = new Paint(Paint.ANTI_ALIAS_FLAG);
            pointRedBitmap  = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            canvasRed = new Canvas(pointRedBitmap);
            paintRed.setColor(Color.RED);
            paintRed.setStrokeWidth(20);
            canvasRed.drawCircle(xpos, ypos, 17, paintRed); // Warum geht das nur beim ersten Aufruf?
            canvas.drawBitmap(pointRedBitmap, 0, 0, paintRed);
        }
        if (farbe == "blue") {
            paintBlue = new Paint(Paint.ANTI_ALIAS_FLAG);
            pointBlueBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            canvasBlue = new Canvas(pointBlueBitmap);
            paintBlue.setColor(Color.BLUE);
            paintBlue.setStrokeWidth(20);
            canvasBlue.drawCircle(xpos, ypos, 17, paintBlue);
            //canvasBlue.drawCircle(xpos, ypos, 17, paintBlue);
            //canvas.drawCircle(xpos, ypos, 17, paintBlue);
            canvas.drawBitmap(pointBlueBitmap, 0, 0, paintBlue);
        }*/

        paintRed = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointRedBitmap  = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        canvasRed = new Canvas(pointRedBitmap);
        paintRed.setColor(Color.RED);
        paintRed.setStrokeWidth(20);
        canvasRed.drawCircle(xRed, yRed, 17, paintRed); // Warum geht das nur beim ersten Aufruf?
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
    }

    private String whichCircle(MotionEvent event, int countPoint) {
        String circle = null;
        float touchX = event.getX();
        float touchY = event.getY();

        float touchPoint = touchX + touchY;
        float redPoint = xRed + yRed;
        float bluePoint = xpos + ypos;
        /*
        for(int i=0;i<countPoint;i++) {
            if ((touchPoint - redPoint) < (touchPoint - bluePoint)) {
                circle = "red";
            }
            if ((touchPoint - redPoint) > (touchPoint - bluePoint)) {
                circle = "blue";
            }
        }
        */
        double aRed = touchX - xRed;
        double bRed = touchY - yRed;

        double aBlue = touchX - xpos;
        double bBlue = touchY - ypos;

        double cRed = Math.sqrt(Math.pow(aRed,2)+ Math.pow(bRed,2));
        double cBlue = Math.sqrt(Math.pow(aBlue,2)+ Math.pow(bBlue,2));

        if (cRed < cBlue) {
            circle = "red";
        }else{
            circle = "blue";
        }

        return circle;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        String nearestPoint = null;
        int countPoint = 2;
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

        } else {

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

        invalidate();
        return true;
    }
}
