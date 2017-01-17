package com.catira.opencvdemo.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.catira.opencvdemo.R;


/**
 * Created by ck on 05.01.2017.
 */

public class ZeichnenActivity extends AppCompatActivity implements View.OnTouchListener{
    private ZeichnenView zview;
    private FrameLayout frameLayout;
    private ZeichnenView highest_point_saddle_view;

    public static final String EXTRA_IMAGE = "extra_image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zeichnen);

        //Bitmap bitmap = BitmapFactory.decodeFile(R.drawable.kratzbild);

        frameLayout = (FrameLayout) findViewById(R.id.framelayout1);

        ImageView imageView = (ImageView) frameLayout.findViewById(R.id.bikecycleImageView);
        int imageResource = R.drawable.kratzbild;
        Drawable image2 = getResources().getDrawable(imageResource);
        //imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
        imageView.setImageDrawable(image2);

        //int point = 1;
        zview = new ZeichnenView(this);
        frameLayout.addView(zview);
        /*
        FrameLayout.LayoutParams zviewLayoutParams =
                new FrameLayout.LayoutParams();
        zview.setLayoutParams(zviewLayoutParams);
        */

        //zview.setLayoutParams(new FrameLayout.LayoutParams(200, 200));
        //point = 2;
        //highest_point_saddle_view = new ZeichnenView(this, point);
        //frameLayout.addView(highest_point_saddle_view);
        //highest_point_saddle_view.setLayoutParams(new FrameLayout.LayoutParams(200,200));
/*
        imageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String LOG_TAG = ZeichnenActivity.class.getSimpleName();

                Log.i(LOG_TAG, "information - BikeImageView");
                return false;
            }
        });

        zview.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                String LOG_TAG = ZeichnenActivity.class.getSimpleName();
                zview.layout((int)event.getX(),
                                (int)event.getY(),
                                (int)event.getX() + zview.getWidth(),
                                (int)event.getY() + zview.getHeight());
                Log.i(LOG_TAG, "information - zview" + zview.getLayoutParams().width + " event.getX: " + (int)event.getX());
                return false;
            }
        });
        highest_point_saddle_view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                highest_point_saddle_view.layout((int)event.getX(),
                        (int)event.getY(),
                        (int)event.getX() + highest_point_saddle_view.getWidth(),
                        (int)event.getY() + highest_point_saddle_view.getHeight());
                String LOG_TAG = ZeichnenActivity.class.getSimpleName();

                Log.i(LOG_TAG, "information - highest_point_saddle_view");
                return false;
            }
        });
        */




        frameLayout.setOnTouchListener(this);


        final Button resetButton = (Button) findViewById(R.id.buttonReset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zview.reset();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        /*
        if(v == frameLayout && event.getAction() == MotionEvent.ACTION_DOWN) {
            zview.updateMouse((int) event.getX(), (int) event.getY());
            return true;
        }

        if(v == frameLayout && event.getAction() == MotionEvent.ACTION_MOVE) {
            zview.updateMouse((int) event.getX(), (int) event.getY());
            return true;
        }
        */

        return false;
    }

}