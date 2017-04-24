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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.catira.opencvdemo.R;
import com.catira.opencvdemo.activities.components.MoveableBikeComponentsView;
import com.catira.opencvdemo.model.BikePartPositions;
import com.catira.opencvdemo.model.BikeSize;
import com.catira.opencvdemo.model.MeasurementContext;
import com.catira.opencvdemo.services.BikeImageIdentifier;
import com.catira.opencvdemo.services.BikeSizeCalculator;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * Created by ck on 05.01.2017.
 */

public class MoveableBikeComponentsActivity extends AppCompatActivity implements View.OnTouchListener{
    private FrameLayout frameLayout;
    private MoveableBikeComponentsView mBikeComponents;
    private static SeekBar seekbar_BackTyre;
    private static SeekBar seekbar_FrontTyre;
    private ZoomFragment mZoomFragment;
    private BikeImageIdentifier mBikeImageIdentifier;
    private BikePartPositions mBikePrediction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zeichnen);
        mBikeImageIdentifier = new BikeImageIdentifier(getApplicationContext());


        frameLayout = (FrameLayout) findViewById(R.id.framelayout1);

        ImageView imageView = (ImageView) frameLayout.findViewById(R.id.bicycleImageView);
        int imageResource = R.drawable.kratzbild;
        Drawable image2 = getResources().getDrawable(imageResource);
        //imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
        imageView.setImageDrawable(image2);

        mZoomFragment = ZoomFragment.newInstance(R.id.bicycleImageView);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.framelayout1, mZoomFragment).commit();


        // set seekbar
        seekbar_BackTyre = (SeekBar) findViewById(R.id.seekBarBackTyre);
        seekbar_BackTyre.setProgress(70);
        seekbar_BackTyre.setMax(200);

        seekbar_BackTyre.setOnSeekBarChangeListener(
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
        seekbar_FrontTyre = (SeekBar) findViewById(R.id.seekBarFrontTyre);
        seekbar_FrontTyre.setProgress(70);
        seekbar_FrontTyre.setMax(200);

        seekbar_FrontTyre.setOnSeekBarChangeListener(
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
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // TODO remove for production
        /*MeasurementContext.currentPersDimen = new PersonDimensions(60,90,80);
        MeasurementContext.currentCyclingPosition = CyclingPosition.SPORT;
*/
        if(mBikePrediction == null && MeasurementContext.currentWheelSize != 0 && MeasurementContext.currentPersDimen != null) {
            ImageView image = (ImageView)v.findViewById(R.id.bicycleImageView);

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

            BikeSize bikeSize = new BikeSizeCalculator().calculateBikeSize(MeasurementContext.currentPersDimen, MeasurementContext.currentCyclingPosition);
            mBikePrediction = mBikeImageIdentifier.createPrediction(m, center, bikeSize, MeasurementContext.currentWheelSize,  MeasurementContext.currentCyclingPosition);
            MeasurementContext.currentBikeDimen = mBikePrediction;
            //mBikeComponents = new MoveableBikeComponentsView(this, mBikePrediction, bikeSize);
            //frameLayout.addView(mBikeComponents);
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
