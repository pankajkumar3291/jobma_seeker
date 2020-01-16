package com.smartit.jobSeeker.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.changePassword.ChangePassword;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ActivityChangePassword extends AppCompatActivity {


    private EditText edCurrentPassword, edNewPassword, edConfirmPassword;
    private Button btnSave;
    private ImageView backArrowImage;
    private TextView tvChangePass;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private TextView tvSuccessMessage;
    private Button buttonOk;
    private NoInternetDialog noInternetDialog;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Hawk.init(this).build();
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        findTheIdsHere();

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


    private void findTheIdsHere() {

        edCurrentPassword = findViewById(R.id.edCurrentPassword);
        edNewPassword = findViewById(R.id.edNewPassword);
        edConfirmPassword = findViewById(R.id.edConfirmPassword);
        btnSave = findViewById(R.id.btnSave);
        backArrowImage = findViewById(R.id.backArrowImage);

        tvChangePass = findViewById(R.id.tvChangePass);
        tvChangePass.setTypeface(tvChangePass.getTypeface(), Typeface.BOLD);

        backArrowImage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                if (TextUtils.isEmpty(edCurrentPassword.getText().toString().trim())) {

                    edCurrentPassword.setError("Current password required");
                    edCurrentPassword.requestFocus();

                } else if (edCurrentPassword.getText().toString().length() < 6) {
                    Toast.makeText(ActivityChangePassword.this, "Minimum 6 characters needed", Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(edNewPassword.getText().toString().trim())) {

                    edNewPassword.setError("New password required");
                    edNewPassword.requestFocus();

                } else if (TextUtils.isEmpty(edConfirmPassword.getText().toString().trim())) {

                    edConfirmPassword.setError("Confirm password required");
                    edConfirmPassword.requestFocus();

                } else if (!edNewPassword.getText().toString().matches(edConfirmPassword.getText().toString())) {

                    Toast.makeText(ActivityChangePassword.this, "Match your password", Toast.LENGTH_LONG).show();

                } else {

                    loadingProgress();
                    changePasswordApi();


                }


            }
        });


    }

    private void loadingProgress() {

        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();


    }

    private void changePasswordApi() {


        HttpModule.provideRepositoryService().changePasswordApi(Hawk.get("GET_TOKEN"), edCurrentPassword.getText().toString(), edNewPassword.getText().toString(), edConfirmPassword.getText().toString()).enqueue(new Callback<ChangePassword>() {
            @Override
            public void onResponse(Call<ChangePassword> call, Response<ChangePassword> response) {

                if (response.body() != null && response.body().getError() == 0) {


                    mProgressDialog.dismiss();
                    changepasswordMessageDialog(response.body().getMessage());

                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(ActivityChangePassword.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<ChangePassword> call, Throwable t) {
                mProgressDialog.dismiss();
                System.out.println("ActivityChangePassword.onFailure " + t.getMessage());
            }
        });


    }

    private void changepasswordMessageDialog(String message) {


        LayoutInflater li = LayoutInflater.from(ActivityChangePassword.this);
        View dialogView = li.inflate(R.layout.dialog_feedback, null);

        idGoesHere(dialogView, message);

        alertDialogBuilder = new AlertDialog.Builder(ActivityChangePassword.this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void idGoesHere(View dialogView, String message) {

        tvSuccessMessage = dialogView.findViewById(R.id.tvSuccessMessage);
        buttonOk = dialogView.findViewById(R.id.buttonOk);

        tvSuccessMessage.setText(message);


        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                finish();


            }
        });

    }


}
