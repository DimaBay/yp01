package com.example.yp01;

import androidx.annotation.ContentView;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView textView;
    private static final int LOADING_DELAY = 3000;
int step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launchscreen);

        progressBar = findViewById(R.id.progressBar);





        if (isNetworkAvailable()) {
            progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    openScreenOnline();
                }
            }, LOADING_DELAY);
        } else {
            progressBar.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    openScreenOffline();
                }
            }, LOADING_DELAY);
        }
    }
    int start_x = 0;
    int end_x = 0;

            @Override
            public boolean onTouchEvent(MotionEvent event)
            {

                switch (event.getAction()) {
                        case MotionEvent.ACTION_UP:
                               start_x = (int) event.getX();
                                break;
                        case MotionEvent.ACTION_DOWN:
                            end_x = (int) event.getX();
                                break;
                    }
                        if (start_x != 0 && end_x != 0 ){
                        if (start_x > end_x){
if (step==2)
{
    setContentView(R.layout.onboardingscreen);
    step=1;
}

                            } else if (start_x < end_x) {
                            if (step==1)
                            {
                                setContentView(R.layout.onboadringscreen2);
                                step=2;
                            }

                            if (isNetworkAvailable())
                            {
                                textView = findViewById(R.id.skip);
                                textView.setVisibility(View.GONE);
                            }
                            }
                        start_x = 0;
                        end_x = 0;
                    }
                return false;
            }
    private void openScreenOffline() {

        setContentView(R.layout.onboardingscreen);
        step=1;

    }
    private void openScreenOnline() {

        setContentView(R.layout.onboardingscreen);
        step=1;
    }
    public void onStepScreen1(View view)
    {
        setContentView(R.layout.onboardingscreen);
        step=1;

    }
    public void onStepScreen2(View view)
    {
        setContentView(R.layout.onboadringscreen2);
        if (isNetworkAvailable())
        {
            textView = findViewById(R.id.skip);
            textView.setVisibility(View.GONE);
        }
        step=2;
    }
    public void onStepLogin(View view)
    {
        setContentView(R.layout.signinscreen);
        step=3;
    }
    public void onStepReg(View view)
    {
        setContentView(R.layout.signupscreen);
        step=3;
    }
    public void onStepMain(View view)
    {
        setContentView(R.layout.mainscreen);
        step=3;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}