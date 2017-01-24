package com.catira.opencvdemo.activities;

import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.catira.opencvdemo.R;
import com.catira.opencvdemo.activities.components.MoveViewComponent;

/**
 * Created by ck on 05.01.2017.
 */

public class ZeichnenActivity extends AppCompatActivity implements View.OnTouchListener{
    private ZeichnenView zview;
    private FrameLayout frameLayout;
    private ZeichnenView highest_point_saddle_view;
    private MoveViewComponent testview, Blueview, Redview;
    private static SeekBar seekbar_BackTypre;
    private static SeekBar seekbar_FrontTypre;
    private ZoomFragment mZoomFragment;

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
        //zview = new ZeichnenView(this);
        //frameLayout.addView(zview);

        /*
        FrameLayout.LayoutParams zviewLayoutParams =
                new FrameLayout.LayoutParams();
        zview.setLayoutParams(zviewLayoutParams);
        */
        mZoomFragment = ZoomFragment.newInstance(R.id.bikecycleImageView);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.framelayout1, mZoomFragment).commit();



        Blueview = new MoveViewComponent(this);
        frameLayout.addView(Blueview);

        // set seekbar
        seekbar_BackTypre = (SeekBar) findViewById(R.id.seekBarBackTypre);
        seekbar_BackTypre.setProgress(70);
        seekbar_BackTypre.setMax(200);

        seekbar_BackTypre.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int prozess, boolean fromUser) {
                        Blueview.setSeekbar_BackTypre(prozess);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }

                });
        seekbar_FrontTypre = (SeekBar) findViewById(R.id.seekBarFrontTypre);
        seekbar_FrontTypre.setProgress(70);
        seekbar_FrontTypre.setMax(200);

        seekbar_FrontTypre.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int prozess, boolean fromUser) {
                        Blueview.setSeekbar_FrontTypre(prozess);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }

                });

/*
        setFarbe = "red";
        setX = 400;
        setY = 400;
        Redview = new MoveViewComponent(this, setX, setY, setFarbe);
        frameLayout.addView(Redview);
*/
        /*        Redview.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String LOG_TAG = MoveViewComponent.class.getSimpleName();

                Log.i(LOG_TAG, "information - Redview");
                return false;
            }
        });
        Blueview.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String LOG_TAG = MoveViewComponent.class.getSimpleName();

                Log.i(LOG_TAG, "information blueview " + v);
                Blueview.updateMouse((int)event.getX(), (int)event.getY());
                return false;
            }
        });
*/


        //testview.setLayoutParams();
        /*testview.setLayoutParams(new FrameLayout.LayoutParams(40, 40));
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(0xFF00FF00); // Changes this drawbale to use a single color instead of a gradient
        gd.setCornerRadius(5);
        gd.setStroke(1, 0xFF000000);
        testview.setBackgroundDrawable(gd);
        */
/*
        imageView.setOnTouchListener(new View.OnTouchListener() {

             @Override
             public boolean onTouch(View v, MotionEvent event) {
                testview.setLeft((int)event.getX());
                testview.setTop((int)event.getY());
                 return false;
             }
         });

        //zview.setLayoutParams(new FrameLayout.LayoutParams(200, 200));
        //point = 2;
        highest_point_saddle_view = new ZeichnenView(this);
        frameLayout.addView(highest_point_saddle_view);
        //highest_point_saddle_view.setLayoutParams(new FrameLayout.LayoutParams(200,200));

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
                Blueview.updateMouse();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Blueview.onTouch(event);
        //zview.onTouch(event);
        mZoomFragment.onTouch(v, event);
        return true;
        
        //!!!Erkennung eines Punktes geht nicht, wenn die View nicht so groß ist wie ein Punkt
        /*
        if (Blueview.getX() > event.getX()) {
            Blueview.updateMouse("blue", (int)event.getX(), (int)event.getY());
        }
        if (Redview.getX() < event.getX()) {
            Redview.updateMouse("red", (int)event.getX(), (int)event.getY());
        }
        */
        /*
        testview.setLeft((int)event.getX());
        testview.setTop((int)event.getY());
        testview.setRight((int)event.getX()+40);
        testview.setBottom((int)event.getY()+40);
        */
        //testview.updateMouse((int) event.getX(), (int) event.getY());
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

        //return false;
    }

}
