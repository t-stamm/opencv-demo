package com.catira.opencvdemo.activities;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class CameraActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private static final String TAG = "CameraActivity";
    //Fünf Variable für Kamera
    ImageButton picture;
    ImageView bikecycleImageView, fa_button_camera, fa_button_check;
    Intent i;
    final static int cameraData = 0;
    final static int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1, PERMISSIONS_REQUEST_CAMERA = 2;
    private int permissionFirst = 0, permissionSecond = 0;

    private BikePartPositions mBikePrediction;

    Bitmap bmp;
    private File bildDatei;

    ImageButton weiter;

    //Drawing View
    private FrameLayout mFrameLayout;
    private RelativeLayout seekBarWrapper;
    private MoveableBikeComponentsView mBikeComponentsView;
    private static SeekBar mSeekbarBackTyre;
    private static SeekBar mSeekbarFrontTyre;
    private ZoomFragment mZoomFragment;
    private ZoomFragment mZoomFragmentSeekbar;
    private TextView mTip;

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
        bikecycleImageView = (ImageView) findViewById(R.id.bicycleImageView);
        bikecycleImageView.setImageDrawable(getDrawable(R.drawable.handy_orientation_picture));
        //Kamera einbinden, Bild aufnehmen und auch anzeigen
        picture = (ImageButton) findViewById(R.id.fa_button_camera);
        picture.setOnClickListener(this);

        mTip = (TextView) findViewById(R.id.textViewFrontTyreTip);
        mTip.setVisibility(View.GONE);

        //create framelayout and mBikeComponentsView
        mFrameLayout = (FrameLayout) findViewById(R.id.framelayout1);

        mBikeComponentsView = new MoveableBikeComponentsView(this);
        mFrameLayout.addView(mBikeComponentsView);
        mBikeComponentsView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        seekBarWrapper = (RelativeLayout) findViewById(R.id.seekBarWrapper);

        mZoomFragment = ZoomFragment.newInstance(R.id.bicycleImageView);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.framelayout1, mZoomFragment).commit();

        mZoomFragmentSeekbar = ZoomFragment.newInstance(R.id.bicycleImageView);
        FragmentTransaction ftSeekbar = getFragmentManager().beginTransaction();
        ftSeekbar.add(R.id.framelayout1, mZoomFragmentSeekbar).commit();

        mFrameLayout.setOnTouchListener(this);
        seekBarWrapper.setVisibility(View.GONE);

        fa_button_check = (ImageView) findViewById(R.id.fa_button_check);
        fa_button_check.setVisibility(View.GONE);
    }

    private void firstStep() {
        mBikeComponentsView.hideFrame(true);
        mBikeComponentsView.disableTyres(false);
        mFrameLayout.setVisibility(View.VISIBLE);
        mTip.setVisibility(View.VISIBLE);
        seekBarWrapper.setVisibility(View.GONE);
    }

    public void secondStep() {
        mTip.setVisibility(View.GONE);

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
                //mBikeComponentsView.updateMouse();
                thirdStep();
            }
        });
        //get CurrentProzess for Seekbar
        int defaultCurrentProzess = (int) ((float)mBikePrediction.getBackWheel().getRadius() * 2 / (float)mBikePrediction.getReferenceWidth() * 100);
        // set seekbar
        mSeekbarBackTyre = (SeekBar) findViewById(R.id.seekBarBackTypre);
        mSeekbarBackTyre.setProgress(defaultCurrentProzess);
        mSeekbarBackTyre.setMax(100);
        mSeekbarBackTyre.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int prozess, boolean fromUser) {
                        if(prozess < 20 ) prozess = 20;
                        mBikeComponentsView.setSeekbar_BackTyre(prozess);
                        double RadiusBackWheelY =  mBikePrediction.getBackWheel().getCenter().y - mBikePrediction.getBackWheel().getRadius();
                        mZoomFragmentSeekbar.updateImageSeekbar(mFrameLayout, mBikePrediction.getBackWheel().getCenter().x, RadiusBackWheelY);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mZoomFragmentSeekbar.hide();
                    }

                });

        mSeekbarFrontTyre = (SeekBar) findViewById(R.id.seekBarFrontTypre);
        mSeekbarFrontTyre.setProgress(defaultCurrentProzess);
        mSeekbarFrontTyre.setMax(100);
        mSeekbarFrontTyre.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int prozess, boolean fromUser) {
                        if(prozess < 20 ) prozess = 20;
                        mBikeComponentsView.setSeekbar_FrontTyre(prozess);
                        double RadiusFrontWheelY =  mBikePrediction.getFrontWheel().getCenter().y - mBikePrediction.getFrontWheel().getRadius();
                        mZoomFragmentSeekbar.updateImageSeekbar(mFrameLayout, mBikePrediction.getFrontWheel().getCenter().x, RadiusFrontWheelY);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mZoomFragmentSeekbar.hide();
                    }

                });

        calSecondWheelPos();
    }


    private void initZView(View v, MotionEvent event) {
        if(mBikeComponentsView != null && !mBikeComponentsView.isInitialized() && mBikePrediction == null && MeasurementContext.currentWheelSize != 0 && MeasurementContext.currentPersDimen != null) {
            ImageView image = (ImageView)v.findViewById(R.id.bicycleImageView);

            Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
            /*Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas mDrawableCanvas = new Canvas(bitmap);
            image.draw(mDrawableCanvas);*/

            mBikeComponentsView.setSource(bitmap.copy(bitmap.getConfig(), false));
            Mat m = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1);

            // bitmap might be scaled if it doesn't fit entirely on the screen
            // make sure the mat reflects that because otherwise the search for
            // elements might be flawed
            //float scaleX = Math.min(1, (float)image.getHeight() / (float)image.getDrawable().getIntrinsicHeight());
            //float scaleY = Math.min(1, (float)image.getWidth() / (float)image.getDrawable().getIntrinsicWidth());

            /*int scaledHeight = (int)Math.ceil(image.getHeight() * scale);
            int scaledWidth = (int)Math.ceil(image.getWidth() * scale);
            int startRow = Math.max(1, (image.getHeight() - scaledHeight) / 2);
            int startCol = Math.max(1, (image.getWidth() - scaledWidth) / 2);*/
            Utils.bitmapToMat(bitmap, m);
            /*if(scaledHeight != image.getHeight() || scaledWidth != image.getWidth()) {
                m = m.submat(startRow, Math.min(m.rows(), scaledHeight + startRow), startCol, (Math.min(m.cols(), scaledWidth + startCol)));
            }*/
            /*

            bitmap = Bitmap.createBitmap(rectWidth, rectHeight, Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(m.submat(y, y + rectHeight, x, x + rectWidth), bitmap);
            image.setImageBitmap(bitmap);*/
            Point center = new Point((int)(event.getX() - image.getX()), (int)(event.getY() - image.getY()));

            BikeSize bikeSize = new BikeSizeCalculator().calculateBikeSize(MeasurementContext.currentPersDimen, MeasurementContext.currentCyclingPosition);
            BikeImageIdentifier identifier = new BikeImageIdentifier(this);
            mBikePrediction = identifier.createPrediction(m, center, bikeSize, MeasurementContext.currentWheelSize,  MeasurementContext.currentCyclingPosition);

            if(mBikePrediction == null) {
                mBikePrediction = identifier.createDefault(m, center, bikeSize, MeasurementContext.currentWheelSize, MeasurementContext.currentCyclingPosition);
            }

            MeasurementContext.currentBikeDimen = mBikePrediction;
            mBikeComponentsView.setBikePartPositions(mBikePrediction);
            mBikeComponentsView.setBikeSize(bikeSize);
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
        mBikeComponentsView.hideFrame(false);
        mBikeComponentsView.disableTyres(true);

        seekBarWrapper.setVisibility(View.GONE);
        fa_button_check.setVisibility(View.VISIBLE);

        final Button backToCycButton = (Button) findViewById(R.id.buttonBacktoCycle);
        backToCycButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mBikeComponentsView.updateMouse();
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

            try {/*
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                Bitmap bitmap = BitmapFactory.decodeFile(bildDatei.getAbsolutePath(), options);

                // max 4096 px on the bigger side
                int scale = Math.max(bitmap.getWidth(), bitmap.getHeight()) / 2096;
                if(scale > 1) {
                    scale--;
                } else {
                    scale = 1;
                }

                if(scale != 1) {
                    options = new BitmapFactory.Options();
                    options.inSampleSize = scale;
                    bitmap = BitmapFactory.decodeFile(bildDatei.getAbsolutePath(), options);
                }*/

                Bitmap bitmap = getScaledBitmap(bildDatei);

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
                firstStep();
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

    private Bitmap getScaledBitmap(File path) {

        InputStream in;
        try {
            final int IMAGE_MAX_SIZE = 1000000; // 1 MP
            in = new FileInputStream(path);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();

            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d(TAG, "scale = " + scale + ", original width: " + o.outWidth + ", original height: " + o.outHeight);

            Bitmap b;
            in = new FileInputStream(path);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d(TAG, "scale operation dimensions width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;
            } else {
                b = BitmapFactory.decodeStream(in);
            }

            Log.d(TAG, "bitmap size width: " + b.getWidth() + ", height: " +
                    b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(mBikeComponentsView != null && !mBikeComponentsView.isInitialized()) {
            initZView(v, event);
            secondStep();
        } else {
            mBikeComponentsView.onTouch(event);
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
