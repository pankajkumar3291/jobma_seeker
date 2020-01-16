package com.smartit.jobSeeker.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import am.appwise.components.ni.NoInternetDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivitySplash extends AppCompatActivity {

    public static String token;
    private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Hawk.init(this).build();
        noInternetDialog = new NoInternetDialog.Builder(this).build();

        token = Hawk.get("GET_TOKEN");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Hawk.get("LOGIN_SESSION", false)) {
                    Intent intent = new Intent(ActivitySplash.this, ActivityDashboard.class);
                    intent.putExtra("token", token);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(ActivitySplash.this, ActivityChoosenLoginRegister.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);

//        // Add code to print out the key hash
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.jobma.ssi.seeker",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }


//throw new RuntimeException("rrttrtrtrtrrrt");

    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent in = getIntent();
        Uri data = in.getData();
        System.out.println("deeplinkingcallback   :- " + data);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }
}
