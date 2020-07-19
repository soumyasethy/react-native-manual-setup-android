package com.soumya.sethy.reactnativesetupandroid;

import android.os.Environment;

import androidx.core.content.ContextCompat;

import java.io.File;

/**
 * Created by Soumya Ranjan Sethy <sethy.soumyaranjan@gmail.com>
 */
public final class Constants {

    public static final String DEFAULT_APP_NAME = "MyReactNativeApp";
    public static final String DEFAULT_BUNDLE_PATH = "assets://index.android.bundle";

    public static final String FIREBASE_ACTIVE_APP_NAME = "active_app_name";
    public static final String FIREBASE_ACTIVE_BUNDLE_NAME = "active_bundle_name";

    public static final String BUNDLE_NAME = "BUNDLE_NAME";
    public static final String BUNDLE_URL = "BUNDLE_URL";
    public static final String BUNDLE_PATH = "BUNDLE_PATH";

    public static final String GET_BUNDLE_NAME = MainApplication.getMainApplication().getSharedPref().getString(Constants.BUNDLE_NAME);
    public static final String GET_BUNDLE_URL = MainApplication.getMainApplication().getSharedPref().getString(Constants.BUNDLE_URL);
    public static final String GET_BUNDLE_PATH = MainApplication.getMainApplication().getSharedPref().getString(Constants.BUNDLE_PATH);

    public static final void SET_BUNDLE_NAME(String bundleName) {
        MainApplication.getMainApplication().getSharedPref().putString(Constants.BUNDLE_NAME, bundleName);
    }

    public static final void SET_BUNDLE_URL(String bundleURL) {
        MainApplication.getMainApplication().getSharedPref().putString(Constants.BUNDLE_URL, bundleURL);
    }

    public static final void SET_BUNDLE_PATH(String bundlePath) {
        MainApplication.getMainApplication().getSharedPref().putString(Constants.BUNDLE_PATH, bundlePath);
    }

    public static final String INDEX_ANDROID_BUNDLE = "index.android.bundle";

    public static String GET_ROOT_DIR_PATH() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(MainApplication.getMainApplication(), null)[0];
            return file.getAbsolutePath();
        } else {
            return MainApplication.getMainApplication().getFilesDir().getAbsolutePath();
        }
    }
}

