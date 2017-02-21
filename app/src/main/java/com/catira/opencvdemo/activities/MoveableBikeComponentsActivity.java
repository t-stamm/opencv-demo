package com.catira.opencvdemo.activities;

import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import org.opencv.core.Point;
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
import com.catira.opencvdemo.activities.components.MoveableBikeComponentsView;
import com.catira.opencvdemo.model.BikeDimensions;
import com.catira.opencvdemo.services.BikeImageIdentifier;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * Created by ck on 05.01.2017.
 */

public class MoveableBikeComponentsActivity extends AppCompatActivity implements View.OnTouchListener{
    private FrameLayout frameLayout;
    private MoveableBikeComponentsView testview, mBikeComponents, Redview;
    private static SeekBar seekbar_BackTypre;
    private static SeekBar seekbar_FrontTypre;
    private ZoomFragment mZoomFragment;
    private BikeImageIdentifier mBikeImageIdentifier;
    private BikeDimensions mBikePrediction;

    public static final String EXTRA_IMAGE = "extra_image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zeichnen);
        mBikeImageIdentifier = new BikeImageIdentifier(getApplicationContext());


        frameLayout = (FrameLayout) findViewById(R.id.framelayout1);

        ImageView imageView = (ImageView) frameLayout.findViewById(R.id.bikecycleImageView);
        int imageResource = R.drawable.kratzbild;
        Drawable image2 = getResources().getDrawable(imageResource);
        //imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
        imageView.setImageDrawable(image2);

        mZoomFragment = ZoomFragment.newInstance(R.id.bikecycleImageView);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.framelayout1, mZoomFragment).commit();


        // set seekbar
        seekbar_BackTypre = (SeekBar) findViewById(R.id.seekBarBackTypre);
        seekbar_BackTypre.setProgress(70);
        seekbar_BackTypre.setMax(200);

        seekbar_BackTypre.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int prozess, boolean fromUser) {
                        //mBikeComponents.setSeekbar_BackTypre(prozess);
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
                        //mBikeComponents.setSeekbar_FrontTypre(prozess);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }

                });

        frameLayout.setOnTouchListener(this);

        final Button resetButton = (Button) findViewById(R.id.buttonReset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mBikeComponents.updateMouse();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(mBikePrediction == null) {
            ImageView image = (ImageView)v.findViewById(R.id.bikecycleImageView);

            Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas mDrawableCanvas = new Canvas(bitmap);
            image.draw(mDrawableCanvas);
            Mat m = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1);

            // bitmap might be scaled if it doesn't fit entirely ion the screen
            // make sure the mat reflects that because otherwise the search for
            // elements might be flawed
            float scaleX = (float)image.getHeight() / (float)image.getDrawable().getIntrinsicHeight();
            float scaleY = (float)image.getWidth() / (float)image.getDrawable().getIntrinsicWidth();

            int scaledHeight = (int)Math.ceil(image.getHeight() * scaleY);
            int scaledWidth = (int)Math.ceil(image.getWidth() * scaleX);
            if(scaledHeight != image.getHeight() || scaledWidth != image.getWidth()) {
                int startRow = (image.getHeight() - scaledHeight) / 2;
                int startCol = (image.getWidth() - scaledWidth) / 2;
                m = m.submat(startRow, scaledHeight + startRow, startCol, scaledWidth + startCol);
            }
            Utils.bitmapToMat(bitmap, m);/*

            bitmap = Bitmap.createBitmap(rectWidth, rectHeight, Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(m.submat(y, y + rectHeight, x, x + rectWidth), bitmap);
            image.setImageBitmap(bitmap);*/
            Point center = new Point((int)(event.getX() - image.getX()), (int)(event.getY() - image.getY()));

            mBikePrediction = mBikeImageIdentifier.test(m, center);
            mBikeComponents = new MoveableBikeComponentsView(this, mBikePrediction);
            frameLayout.addView(mBikeComponents);
            if(mBikePrediction != null) {
                System.out.println("::::Found front Wheel at "+ mBikePrediction.getFrontWheel().getCenter().x+" / "+ mBikePrediction.getFrontWheel().getCenter().y+" with r "+ mBikePrediction.getFrontWheel().getRadius());
                /*mBikeComponents.xBackTyre = (float) mBikePrediction.getBackWheel().getCenter().x;
                mBikeComponents.yBackTyre = (float) mBikePrediction.getBackWheel().getCenter().y;
                mBikeComponents.circleBackTyreSize = mBikePrediction.getBackWheel().getRadius();

                mBikeComponents.xFrontTyre = (float) mBikePrediction.getFrontWheel().getCenter().x;
                mBikeComponents.yFrontTyre = (float) mBikePrediction.getFrontWheel().getCenter().y;
                mBikeComponents.circleFrontTyreSize = mBikePrediction.getFrontWheel().getRadius();*/
            }

        }
        mBikeComponents.onTouch(event);
        //zview.onTouch(event);
        mZoomFragment.onTouch(v, event);
        return true;

    }

}
