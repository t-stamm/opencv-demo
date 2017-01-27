package com.catira.opencvdemo.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.catira.opencvdemo.R;
import com.catira.opencvdemo.activities.components.MoveViewComponent;


public class CameraActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    //Fünf Variable für Kamera
    ImageButton picture;
    ImageView bikecycleImageView, fa_button_camera, fa_button_check;
    Intent i;
    final static int cameraData = 0;
    Bitmap bmp;

    ImageButton weiter;

    //Drawing View
    private FrameLayout frameLayout;
    private RelativeLayout seekBarWrapper;
    private MoveViewComponent zview;
    private static SeekBar seekbar_BackTypre;
    private static SeekBar seekbar_FrontTypre;
    private ZoomFragment mZoomFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        weiter = (ImageButton) findViewById(R.id.fa_button_check);
        weiter.setOnClickListener(new View.OnClickListener() {

            public void onClick (View v) {
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                startActivity(intent);
            }
        });

        //Kamera einbinden, Bild aufnehmen und auch anzeigen
        bikecycleImageView = (ImageView) findViewById(R.id.bikecycleImageView);
        picture = (ImageButton) findViewById(R.id.fa_button_camera);
        picture.setOnClickListener(this);

        //create framelayout and zview
        frameLayout = (FrameLayout) findViewById(R.id.framelayout1);
        seekBarWrapper = (RelativeLayout) findViewById(R.id.seekBarWrapper);
        zview = new MoveViewComponent(this);
        frameLayout.addView(zview);

        mZoomFragment = ZoomFragment.newInstance(R.id.bikecycleImageView);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.framelayout1, mZoomFragment).commit();

        frameLayout.setOnTouchListener(this);
        frameLayout.setVisibility(View.GONE);
        seekBarWrapper.setVisibility(View.GONE);

        fa_button_check = (ImageView) findViewById(R.id.fa_button_check);
        fa_button_check.setVisibility(View.GONE);
    }

    public void secondStep() {

        frameLayout.setVisibility(View.VISIBLE);
        seekBarWrapper.setVisibility(View.VISIBLE);

        fa_button_camera = (ImageView) findViewById(R.id.fa_button_camera);
        fa_button_camera.setVisibility(View.GONE);

        final Button resetButton = (Button) findViewById(R.id.buttonNextStep);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zview.updateMouse();
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
                        zview.setSeekbar_BackTypre(prozess);
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
                        zview.setSeekbar_FrontTypre(prozess);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }

                });

    }
    public void thirdStep(){
        seekBarWrapper.setVisibility(View.GONE);
        fa_button_check.setVisibility(View.VISIBLE);
    }
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.fa_button_camera:
                i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, cameraData);
                break;
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            bmp = (Bitmap) extras.get("data");
            bikecycleImageView.setImageBitmap(bmp);

            //show WheelCycle
            secondStep();
            //stateSetWheelCycle = true;
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        zview.onTouch(event);
        mZoomFragment.onTouch(v, event);
        return true;
    }
}
