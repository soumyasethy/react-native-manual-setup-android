package com.soumya.sethy.reactnativesetupandroid;

import com.facebook.react.ReactActivity;

/**
 * Created by Soumya Ranjan Sethy <sethy.soumyaranjan@gmail.com>
 */
public class ReactNativeActivity extends ReactActivity {
    @Override
    protected String getMainComponentName() {
        try {
            return Constants.GET_BUNDLE_NAME;
        } catch (Error error) {
            return Constants.DEFAULT_APP_NAME;
        }
    }
}
