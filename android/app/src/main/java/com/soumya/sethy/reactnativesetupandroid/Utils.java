package com.soumya.sethy.reactnativesetupandroid;

import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

/**
 * Created by Soumya Ranjan Sethy <sethy.soumyaranjan@gmail.com>
 */

public class Utils {
    private FirebaseRemoteConfig firebaseRemoteConfig;
    private Context ctx;
    private ProgressBar progressBar;
    private TextView textView;
    private String firebaseActiveBundle, firebaseAppName, downloadUrl;

    Utils() {
        this.ctx = MainActivity.getActivity();
        this.progressBar = MainActivity.getActivity().progressBar;
        this.textView = MainActivity.getActivity().textView;
        setupConstants();
    }

    private void setupConstants() {
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseAppName = firebaseRemoteConfig.getString(Constants.FIREBASE_ACTIVE_APP_NAME);
        firebaseActiveBundle = firebaseRemoteConfig.getString(Constants.FIREBASE_ACTIVE_BUNDLE_NAME);
        //fetch activeBundle and it's url from firebase at runtime
        downloadUrl = firebaseRemoteConfig.getString(firebaseActiveBundle);
        downloadUrl = downloadUrl.replaceAll("\"", "");
    }

    public void syncWithServer() {
        firebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(MainActivity.getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseRemoteConfig.activateFetched();
                            if (isUpdateRequired()) {
                                triggerDownloadBundle();
                            } else {
                                MainActivity.getActivity().startRNPage();
                            }
                        }
                    }
                });
    }

    private boolean isUpdateRequired() {
        return !downloadUrl.equalsIgnoreCase(Constants.GET_BUNDLE_URL);
    }

    private void triggerDownloadBundle() {
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true) // Enabling database for resume support even after the application is killed:
                .build();
        PRDownloader.initialize(ctx, config);

        PRDownloader.download(downloadUrl, Constants.GET_ROOT_DIR_PATH(), Constants.INDEX_ANDROID_BUNDLE)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        textView.setText("Downloading...");
                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {
                        textView.setText("Downloading paused");
                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {
                        textView.setText("Downloading cancelled");
                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                        progressBar.setProgress((int) progressPercent);
                        textView.setText("Downloading " + (int) progressPercent + "%");
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        textView.setText("Starting...");
                        updateBundleDetails(firebaseAppName, Constants.GET_ROOT_DIR_PATH() + "/" + Constants.INDEX_ANDROID_BUNDLE, downloadUrl);
                        MainActivity.getActivity().startRNPage();
                    }

                    @Override
                    public void onError(Error error) {
                        textView.setText("Downloading Failed");
                        MainActivity.getActivity().startRNPage();
                    }
                });
    }

    private void updateBundleDetails(String newBundleName, String newBundlePath, String newBundleUrl) {
        // Storing data into SharedPreferences
        Constants.SET_BUNDLE_NAME(newBundleName);
        Constants.SET_BUNDLE_PATH(newBundlePath);
        Constants.SET_BUNDLE_URL(newBundleUrl);
    }
}
