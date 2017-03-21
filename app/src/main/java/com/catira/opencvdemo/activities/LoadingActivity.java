package com.catira.opencvdemo.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;

import com.catira.opencvdemo.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

public class LoadingActivity extends AppCompatActivity {
    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    private Handler mHandler = new Handler();


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(getClass().getSimpleName(), "OpenCV loaded successfully.");
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        setTitle(R.string.title_activity_loading);

        mProgress = (ProgressBar) findViewById(R.id.progressbar);

        if (!OpenCVLoader.initDebug()) {
            Log.d(getClass().getSimpleName(), "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(getClass().getSimpleName(), "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }


        new Thread(new Runnable() {
            public void run() {
                while (mProgressStatus < 100) {
                    mProgressStatus = doWork(mProgressStatus);

                    mHandler.post(new Runnable() {
                        public void run() {
                            mProgress.setProgress(mProgressStatus);
                        }
                    });
                }
                KickoffActivity();
            }
        }).start();
    }

    public void KickoffActivity() {
        Intent intent = new Intent(this, KickoffActivity.class);
        startActivity(intent);
    }

    public int doWork(int mProzessStatus) {
        try {
            mProzessStatus = mProzessStatus + 1;
            Thread.sleep(60);
        } catch (Exception e) {
        //Fehlercode ausgeben
        }
        return mProzessStatus;
    }
}




