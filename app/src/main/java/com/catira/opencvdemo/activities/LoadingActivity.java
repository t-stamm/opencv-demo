package com.catira.opencvdemo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.catira.opencvdemo.R;

public class LoadingActivity extends AppCompatActivity {
    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    private Handler mHandler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        mProgress = (ProgressBar) findViewById(R.id.progressbar);


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
            Thread.sleep(50);
        } catch (Exception e) {
        //Fehlercode ausgeben
        }
        return mProzessStatus;
    }
}




