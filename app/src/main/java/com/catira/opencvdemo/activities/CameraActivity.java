package com.catira.opencvdemo.activities;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import org.opencv.core.Point;

import java.io.File;
import java.io.IOException;


public class CameraActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    //Fünf Variable für Kamera
    ImageButton picture;
    ImageView bikecycleImageView, fa_button_camera, fa_button_check;
    Intent i;
    final static int cameraData = 0;
    final static int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1, PERMISSIONS_REQUEST_CAMERA = 2;
    private int permissionFirst = 0, permissionSecond = 0;

    private BikeImageIdentifier mBikeImageIdentifier;
    private BikePartPositions mBikePrediction;

    Bitmap bmp;
    private File bildDatei;

    ImageButton weiter;

    //Drawing View
    private FrameLayout frameLayout;
    private RelativeLayout seekBarWrapper;
    private MoveableBikeComponentsView zview;
    private static SeekBar seekbar_BackTypre;
    private static SeekBar seekbar_FrontTypre;
    private ZoomFragment mZoomFragment;
    private ZoomFragment mZoomFragmentSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRunCam", true);
        if (isFirstRun) {
            // Place your dialog code here to display the dialog
            Fragment_Camera fragment = new Fragment_Camera();
            FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(fragment, "Kamera");
            transaction.commit();


            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRunCam", false)
                    .apply();
        }

        weiter = (ImageButton) findViewById(R.id.fa_button_check);
        weiter.setOnClickListener(new View.OnClickListener() {

            public void onClick (View v) {
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                startActivity(intent);
            }
        });


        // Default help picture
        bikecycleImageView = (ImageView) findViewById(R.id.bikecycleImageView);
        bikecycleImageView.setImageDrawable(getDrawable(R.drawable.handy_orientation_picture));
        //Kamera einbinden, Bild aufnehmen und auch anzeigen
        picture = (ImageButton) findViewById(R.id.fa_button_camera);
        picture.setOnClickListener(this);

        //create framelayout and zview
        frameLayout = (FrameLayout) findViewById(R.id.framelayout1);
        seekBarWrapper = (RelativeLayout) findViewById(R.id.seekBarWrapper);


        mZoomFragment = ZoomFragment.newInstance(R.id.bikecycleImageView);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.framelayout1, mZoomFragment).commit();

        mZoomFragmentSeekbar = ZoomFragment.newInstance(R.id.bikecycleImageView);
        FragmentTransaction ftSeekbar = getFragmentManager().beginTransaction();
        ftSeekbar.add(R.id.framelayout1, mZoomFragmentSeekbar).commit();

        frameLayout.setOnTouchListener(this);
        //frameLayout.setVisibility(View.GONE);
        seekBarWrapper.setVisibility(View.GONE);

        fa_button_check = (ImageView) findViewById(R.id.fa_button_check);
        fa_button_check.setVisibility(View.GONE);
    }

    public void firstStep() {
        // hint: Click on front tyre
    }

    public void secondStep() {
        zview.hideFrame(true);
        zview.disableTyres(false);

        //frameLayout.setVisibility(View.VISIBLE);
        seekBarWrapper.setVisibility(View.VISIBLE);
        fa_button_check.setVisibility(View.GONE);

        fa_button_camera = (ImageView) findViewById(R.id.fa_button_camera);
        fa_button_camera.setVisibility(View.GONE);

        Button buttonBacktoCycle = (Button) findViewById(R.id.buttonBacktoCycle);
        buttonBacktoCycle.setVisibility(View.GONE);

        final Button setPointsButton = (Button) findViewById(R.id.buttonNextStepSetPoints);
        setPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //zview.updateMouse();
                thirdStep();
            }
        });

        // set seekbar
        seekbar_BackTypre = (SeekBar) findViewById(R.id.seekBarBackTypre);
        seekbar_BackTypre.setProgress(70);
        seekbar_BackTypre.setMax(200);
        seekbar_BackTypre.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int prozess, boolean fromUser) {
                        zview.setSeekbar_BackTyre(prozess);
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
                        zview.setSeekbar_FrontTyre(prozess);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }

                });

        calSecondWheelPos();
    }

    private void initZView(View v, MotionEvent event) {
        if(zview == null && mBikePrediction == null && MeasurementContext.currentWheelSize != 0 && MeasurementContext.currentPersDimen != null) {
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

            BikeSize bikeSize = new BikeSizeCalculator().calculateBikeSize(MeasurementContext.currentPersDimen, MeasurementContext.currentCyclingPosition);
            mBikePrediction = mBikeImageIdentifier.createPrediction(m, center, bikeSize, MeasurementContext.currentWheelSize,  MeasurementContext.currentCyclingPosition);
            MeasurementContext.currentBikeDimen = mBikePrediction;
            zview = new MoveableBikeComponentsView(this, mBikePrediction, bikeSize);
            frameLayout.addView(zview);
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
    }

    public void calSecondWheelPos() {
        //Zweites Rad berechnen
        Log.d("FahrradApp", "Zoll: " + getIntent().getStringExtra("wheelSize") );
        double mmPixelFaktor = 2.5;
        int wheelDistance = 1000; // mm
        double resultFakWhDis;
        int firstBicycleCenterY;
        int firstBicycleCenterX;
        // Mit der WheelSize in mm wird der Faktor zwischen Bildpixel und realem Millimeter dargestellt
        resultFakWhDis = mmPixelFaktor * wheelDistance;
        Log.d("FahrradApp", "Punkt: " + resultFakWhDis);
        // das resultFakWhDis plus oder minus in der x-Achse zum Mittelpunkt eines Rades
    }
    public void thirdStep(){
        zview.hideFrame(false);
        zview.disableTyres(true);

        seekBarWrapper.setVisibility(View.GONE);
        fa_button_check.setVisibility(View.VISIBLE);

        final Button backToCycButton = (Button) findViewById(R.id.buttonBacktoCycle);
        backToCycButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //zview.updateMouse();
                secondStep();
            }
        });

        backToCycButton.setVisibility(View.VISIBLE);
    }
    public void onClick(View v) {

        int check = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int check2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        if(check != PackageManager.PERMISSION_GRANTED ) {
            setPermissionsRequestWriteExternalStorage();
            return;
        }
        if(check == 0 && check2 != PackageManager.PERMISSION_GRANTED) {
            setPermissionsRequestCamera();
            return;
        }

        cameraIntentSenden();

        /*
        switch(v.getId()) {
            case R.id.fa_button_camera:
                i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, cameraData);
                break;
        }
        */
    }

    private void setPermissionsRequestWriteExternalStorage() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }
    private void setPermissionsRequestCamera() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA}, 2);
    }
    private void cameraIntentSenden()  {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            bildDatei = bildDateiErzeugen();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(bildDatei));
            startActivityForResult(intent, 1); // 1 = beliebige ID
        } catch (IOException e) {
            Log.d("FahrradApp", "Problem beim Intent-Senden", e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) throws SecurityException {

        switch (requestCode) {
            case PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    permissionFirst = 1;

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;

                // other 'case' lines to check for other
                // permissions this app might request
                case PERMISSIONS_REQUEST_CAMERA:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    permissionSecond = 1;

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
        }

        if(permissionFirst == 1 && permissionSecond == 1) {
            cameraIntentSenden();
        } else {
            if(permissionSecond == 0) {
                setPermissionsRequestCamera();
            }
            if (permissionFirst == 0) {
                setPermissionsRequestWriteExternalStorage();
            }
        }

    }
    private File bildDateiErzeugen() throws IOException {
        String dateiName = "CameraSimple_" + System.currentTimeMillis() + ".jpg";

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            return new File(dir, dateiName);
        }
        else {
            return null;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        boolean tryAgain = false;

        if (requestCode == 1 && resultCode == RESULT_OK) {

            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(bildDatei.getAbsolutePath(), options);

                Matrix mat = new Matrix();

                ExifInterface exif = new ExifInterface(bildDatei.getAbsolutePath());
                String ori = exif.getAttribute(ExifInterface.TAG_ORIENTATION).toString();

                switch (ori) {
                    case "0":
                        mat.postRotate((float) 0);
                        break;
                    case "6":
                        mat.postRotate((float) 90);

                        break;
                    case "3":
                        mat.postRotate((float) 180);
                        break;
                    case "8":
                        mat.postRotate((float) 270);
                        break;
                    default:
                        bikecycleImageView.setImageBitmap(bitmap);
                }

                if (ori.equals("6") || ori.equals("8")) {
                    bikecycleImageView.setImageDrawable(getDrawable(R.drawable.handy_orientation_picture));
                    tryAgain = true;
                } else {
                    Bitmap bitmapRotate = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
                    bikecycleImageView.setImageBitmap(bitmapRotate);
                }

            } catch (Exception e) {
                System.out.println("onActivityResult");
            }

            if (tryAgain == false) {
                secondStep();
            }
        }
        /*
        if(resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            bmp = (Bitmap) extras.get("data");
            bikecycleImageView.setImageBitmap(bmp);

            //show WheelCycle
            secondStep();
            //stateSetWheelCycle = true;
        }
        */
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(zview == null) {
            initZView(v, event);
        } else {
            zview.onTouch(event);
        }
        mZoomFragment.onTouch(v, event);
        return true;
    }

    // Laden der Menuressource
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_camera, menu);
        return true;
    }

    //Mit Help Menü Button Dialog starten mit Fragment Manager
    public void doCam(MenuItem menuItem) {
        Fragment_Camera fragment = new Fragment_Camera();
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(fragment, "Kamera");
        transaction.commit();
    }
}
