package com.soumya.sethy.reactnativesetupandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Soumya Ranjan Sethy <sethy.soumyaranjan@gmail.com>
 */
public class MainActivity extends AppCompatActivity {
    private static MainActivity mainActivity;
    public static MainActivity getActivity() {
        return mainActivity;
    }

    ProgressBar progressBar;
    Utils utils;
    Button skipButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;

        progressBar = findViewById(R.id.progressBar);
        skipButton = findViewById(R.id.skipButton);
        textView = findViewById(R.id.textView);

        //initialize Utils Class
        utils = new Utils();
        //sync with Server
        utils.syncWithServer();

        //skip update manually
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRNPage();
            }
        });
    }


    public void startRNPage() {
        Intent intent = new Intent(this, ReactNativeActivity.class);
        startActivity(intent);
        finish();
    }
}
