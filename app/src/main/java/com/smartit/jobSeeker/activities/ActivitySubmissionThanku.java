package com.smartit.jobSeeker.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.smartit.jobSeeker.R;

import am.appwise.components.ni.NoInternetDialog;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivitySubmissionThanku extends AppCompatActivity {

    private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission_thanku);


        noInternetDialog = new NoInternetDialog.Builder(this).build();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


 /*               moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);*/

                finishAffinity();


            }
        }, 2000);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
