package com.catira.opencvdemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by timos on 12.02.2017.
 */

public class App extends Application {

        private static App instance;

        public App() {
            instance = this;
        }

        public static Context getContext() {
            return instance;
        }

}
