package com.catira.opencvdemo.activities;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by ck on 05.01.2017.
 */

public class ZeichnenView extends View{
    private int offsetX;
    private int offsetY;
    private float xpos = -1;
    private float ypos = -1;
    private float xxpos = -1;
    private float yypos = -1;
    private Bitmap kratzbild;
    private Paint transparentPaint;
    private Bitmap kratzSchicht;
    private Bitmap layerHPS;
    private Canvas kratzSchichtCanvas;
    private Canvas layerHPSCanvas;
    private static int currentPoint = 0;
    private static boolean init = true;

    Paint highest_point_saddle;
    Paint top_link; //Oberlenker

    private Canvas canvas2;
    private Bitmap backingBitmap;
    private Canvas canvasBluePoint;
    private Bitmap bluePointBitmap;

    public ZeichnenView(Context c) {
        super(c);
        //kratzbild = ladeBild(R.drawable.kratzbild);
        //transparentPaint = new Paint();
        //transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));




        // Oberlenker - top link
        /*
        top_link = new Paint(Paint.ANTI_ALIAS_FLAG);
        top_link.setColor(Color.BLUE);
        top_link.setStrokeWidth(20);
        currentPoint = point;
        */
        //highest_point_saddle = new Paint(Paint.ANTI_ALIAS_FLAG);
        //highest_point_saddle.setColor(Color.YELLOW);
        //highest_point_saddle.setStrokeWidth(20);
        //currentPoint = point;

        //Ereignisse empfängbar
        setFocusable(true);

    }

    public void updateMouse(int x, int y) {
        kratzSchichtCanvas.drawRect(x, y, x + 75, y + 75, transparentPaint);
        invalidate();
    }

    public void reset() {
        if (currentPoint==1) {
            currentPoint=2;
        }else {
            currentPoint = 1;
        }

        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*
        if(kratzSchicht == null) {
            kratzschichtErstellen(canvas);
            offsetX = (getWidth() - kratzbild.getWidth()) / 2; // zur zentrierten Darstellung
            offsetY = (getHeight() - kratzbild.getHeight()) /2;
        }
        */

        //offsetX = (getWidth() - kratzbild.getWidth()) / 2; // zur zentrierten Darstellung
        //offsetY = (getHeight() - kratzbild.getHeight()) /2;

        //canvas.drawBitmap(kratzbild, offsetX, offsetY, null);

        //canvas.drawBitmap(kratzSchicht, 0, 0, null);
// Startposition getWidth() funktioniert nicht in onCreate() oder
        // onStart()
        if (xpos == -1 && ypos == -1) {
            xpos = getWidth() / 2;
            ypos = getHeight() / 2;

            xxpos = getWidth() / 3;
            yypos = getHeight() / 3;
        }
        if (top_link != null) {
            //canvas.drawCircle(xxpos, yypos, 15, top_link);
        }

        if(highest_point_saddle != null) {
            /*
            highest_point_saddle = new Paint(Paint.ANTI_ALIAS_FLAG);
            highest_point_saddle.setColor(Color.YELLOW);
            highest_point_saddle.setStrokeWidth(20);
            */
            //canvas.drawCircle(xpos, ypos, 15, highest_point_saddle);
            //createLayerHighestPointSaddle(canvas);
        }
        switch (currentPoint) {
            case 1:
                createLayerTopLink(canvas, xpos, ypos);
                break;
            case 2:
                createLayerHighestPointSaddle(canvas, xpos, ypos);
                break;
        }
        if (init==true) {
            createLayerHighestPointSaddle(canvas, xpos, ypos);
            xpos = 300;
            ypos = 300;
            createLayerTopLink(canvas, xpos, ypos);

            // Set Initialisation to false
            init = false;
        }




    }

    private void kratzschichtErstellen(Canvas canvas) {
        kratzSchicht =  Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        kratzSchichtCanvas = new Canvas(kratzSchicht);
        Rect r = new Rect(0,0, kratzSchicht.getWidth(), kratzSchicht.getHeight());
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        kratzSchichtCanvas.drawRect(r, paint);
    }
    private void createLayerTopLink(Canvas canvas, float xxpos, float yypos) {

        //canvasBluePoint = new Canvas(backingBitmap);
        top_link = new Paint(Paint.ANTI_ALIAS_FLAG);
        top_link.setColor(Color.BLUE);
        top_link.setStrokeWidth(20);
        canvas.drawCircle(xxpos, yypos, 15, top_link);
        //canvasBluePoint.drawBitmap(bluePointBitmap, 200, 90, top_link);
    }
    private void createLayerHighestPointSaddle(Canvas canvas, float xpos, float ypos) {

        //layerHPS = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.RGB_565);
        //layerHPSCanvas = new Canvas(layerHPS);

        //highest_point_saddle = new Paint(Paint.ANTI_ALIAS_FLAG);
        //highest_point_saddle.setColor(Color.YELLOW);
        //highest_point_saddle.setStrokeWidth(20);
        //layerHPSCanvas.drawCircle(xpos, ypos, 15, highest_point_saddle);
        //Rect r = new Rect(0,0, layerHPS.getWidth(), layerHPS.getHeight());
        //Paint paint = new Paint();
        //paint.setColor(Color.YELLOW);
        //layerHPSCanvas.drawRect(r, paint);
        //Bitmap test = new Canvas();
        highest_point_saddle = new Paint(Paint.ANTI_ALIAS_FLAG);
        highest_point_saddle.setColor(Color.YELLOW);
        highest_point_saddle.setStrokeWidth(20);

        canvas.drawCircle(xpos, ypos, 15, highest_point_saddle);

        //layerHPSCanvas.drawBitmap(layerHPS, 0, 0, null);

    }

    private Bitmap ladeBild(int id) {
        // nur Bildmaße laden
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), id, options);

        // Ladefaktor bestimmen
        int breite    = getResources().getDisplayMetrics().widthPixels;
        int hoehe     = getResources().getDisplayMetrics().heightPixels;
        options.inSampleSize = berechneLadefaktor(options, breite, hoehe);

        // jetzt richtig laden
        options.inJustDecodeBounds = false;
        return    BitmapFactory.decodeResource(getResources(), id, options);
    }

    private int berechneLadefaktor(BitmapFactory.Options options,
                                   int breiteAnzeige, int hoeheAnzeige) {
        final int w = options.outWidth;
        final int h = options.outHeight;
        int faktor = 2;

        if (w > breiteAnzeige || h > hoeheAnzeige) {
            int w_halbe = w / 2;
            int h_halbe = h / 1;

            while ((w_halbe / faktor) > breiteAnzeige
                    && ((h_halbe / faktor)) > hoeheAnzeige) {
                    faktor = faktor * 3;
            }
        }

        return faktor;
    }

    public void onTouch(MotionEvent event) {
        int action = event.getAction();

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

        invalidate();
    }
}
