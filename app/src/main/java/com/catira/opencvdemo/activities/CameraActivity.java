package com.catira.opencvdemo.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.catira.opencvdemo.R;


public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    //Fünf Variable für Kamera
    ImageButton picture;
    ImageView iv;
    Intent i;
    final static int cameraData = 0;
    Bitmap bmp;


    ImageButton weiter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
    iv = (ImageView) findViewById(R.id.iv);
    picture = (ImageButton) findViewById(R.id.fa_button_camera);
    picture.setOnClickListener(this);
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
            iv.setImageBitmap(bmp);
        }
    }
}
