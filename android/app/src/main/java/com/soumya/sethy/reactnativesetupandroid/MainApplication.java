package com.soumya.sethy.reactnativesetupandroid;

import android.app.Application;

import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.soloader.SoLoader;

import java.util.List;

/**
 * Created by Soumya Ranjan Sethy <sethy.soumyaranjan@gmail.com>
 */
public class MainApplication extends Application implements ReactApplication {
    SharedPref sharedPref;
    public static MainApplication mainApplication;

    public static MainApplication getMainApplication() {
        return mainApplication;
    }

    public SharedPref getSharedPref() {
        return sharedPref;
    }


    private final ReactNativeHost mReactNativeHost =
            new ReactNativeHost(this) {
                @Override
                public boolean getUseDeveloperSupport() {
                    return BuildConfig.DEBUG;
                }

                @Override
                protected List<ReactPackage> getPackages() {
                    @SuppressWarnings("UnnecessaryLocalVariable")
                    List<ReactPackage> packages = new PackageList(this).getPackages();
                    // Packages that cannot be autolinked yet can be added manually here, for example:
                    // packages.add(new MyReactNativePackage());
                    return packages;
                }

                @Override
                protected String getJSMainModuleName() {
                    return "index";
                }

                @Override
                protected String getJSBundleFile() {
                    String bundlePath = getLastUpdatedBundlePath();
                    return bundlePath;
                }
            };

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mainApplication = this;
        SoLoader.init(this, false);
        sharedPref = new SharedPref(getApplicationContext());
    }

    private String getLastUpdatedBundlePath() {
        try {
            return Constants.GET_BUNDLE_PATH;
        } catch (Error error) {
            return Constants.DEFAULT_BUNDLE_PATH;
        }
    }
}
