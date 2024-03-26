package com.example.yp01;

import androidx.annotation.ContentView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
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

    public void onLogin(View view) {
        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);


        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();


        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showErrorDialog("Заполните все поля");
            return;
        }


        if (!isValidEmail(email)) {
            showErrorDialog("Некорректный email");
            return;
        }


        onStepMain(view);
    }
    public void onReg(View view) {
        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);
        EditText phoneEditText = findViewById(R.id.phone);

        EditText nameEditText = findViewById(R.id.full_name);



        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String name = nameEditText.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(name)) {
            showErrorDialog("Заполните все поля");
            return;
        }


        if (!isValidEmail(email)) {
            showErrorDialog("Некорректный email");
            return;
        }


        onStepMain(view);
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                email.matches("^[a-z0-9]+@[a-z0-9]+\\.[a-z]{2,3}$");
    }

   private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Ошибка")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
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