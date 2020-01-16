package com.smartit.jobSeeker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.smartit.jobSeeker.R;

import am.appwise.components.ni.NoInternetDialog;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityChoosenLoginRegister extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mainRelative;
    private Button tvLogin, tvRegister;
    private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosen_login_register);

        noInternetDialog = new NoInternetDialog.Builder(this).build();

        findTheIdsFromHere();
        slideUpAnimationGoesHere();
        changingStatusBarColorFromHere();
        eventListenerGoeshere();

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



    private void eventListenerGoeshere() {

        tvLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);


    }

    private void changingStatusBarColorFromHere() {
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityChoosenLoginRegister.this, R.color.colorStatusBar));

    }

    private void slideUpAnimationGoesHere() {
        mainRelative.startAnimation(AnimationUtils.loadAnimation(ActivityChoosenLoginRegister.this, R.anim.slide_up));
    }

    private void findTheIdsFromHere() {

        mainRelative = findViewById(R.id.mainRelative);
        tvLogin = findViewById(R.id.tvLogin);
        tvRegister = findViewById(R.id.tvRegister);
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.tvLogin:


                callTheLoginActivityFromHere();


                break;


            case R.id.tvRegister:

                callTheRegisterActivityFromHere();

                break;
        }

    }

    private void callTheRegisterActivityFromHere() {

        Intent intent = new Intent(ActivityChoosenLoginRegister.this, ActivityRegister.class);
        startActivity(intent);

    }

    private void callTheLoginActivityFromHere() {

        Intent intent = new Intent(ActivityChoosenLoginRegister.this, ActivityLogin.class);
        startActivity(intent);


    }
}
