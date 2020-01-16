package com.smartit.jobSeeker.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.smartit.jobSeeker.apiResponse.reportAnIssue.ReportAnIssue;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityReportAnIssue extends AppCompatActivity implements View.OnClickListener {

    private EditText edEmail, edSubject, edMessage;
    private Button btnSubmit;
    private ImageView backArrowImage;
    private NoInternetDialog noInternetDialog;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private TextView tvSuccessMessage, tvReportT;
    private Button buttonOk;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_an_issue);

        Hawk.init(this).build();
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        loadingProgress();
        findTheIdsHere();
        eventListener();


    }


    private void loadingProgress() {

        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

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

    private void eventListener() {

        backArrowImage.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

    }

    private void findTheIdsHere() {

        edEmail = findViewById(R.id.edEmail);
        edSubject = findViewById(R.id.edSubject);
        edMessage = findViewById(R.id.edMessage);
        btnSubmit = findViewById(R.id.btnSubmit);
        backArrowImage = findViewById(R.id.backArrowImage);
        tvReportT = findViewById(R.id.tvReportT);

        tvReportT.setTypeface(tvReportT.getTypeface(), Typeface.BOLD);

        Hawk.get("USER_EMAIL");
        edEmail.setText(Hawk.get("USER_EMAIL"));
        edEmail.setEnabled(false);

        mProgressDialog.dismiss();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.backArrowImage:
                finish();
                break;

            case R.id.btnSubmit:


                if (TextUtils.isEmpty(edSubject.getText().toString().trim())) {

                    Toast.makeText(ActivityReportAnIssue.this, "Subject field is required", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(edMessage.getText().toString().trim())) {

                    Toast.makeText(ActivityReportAnIssue.this, "Message field is required", Toast.LENGTH_SHORT).show();

                } else {
                    loadingProgress();
                    callTheApi();

                }


                break;
        }

    }

    private void callTheApi() {


        HttpModule.provideRepositoryService().reportAnIssueApi(Hawk.get("GET_TOKEN"), edSubject.getText().toString(), edMessage.getText().toString()).enqueue(new Callback<ReportAnIssue>() {
            @Override
            public void onResponse(Call<ReportAnIssue> call, Response<ReportAnIssue> response) {

                if (response.body() != null && response.body().getError() == 0) {


                    mProgressDialog.dismiss();
                    reportAnIssueDialog(response.body().getMessage());

                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(ActivityReportAnIssue.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ReportAnIssue> call, Throwable t) {
                mProgressDialog.dismiss();
                System.out.println("ActivityReportAnIssue.onFailure " + t.toString());
            }
        });

    }

    private void reportAnIssueDialog(String message) {


        LayoutInflater li = LayoutInflater.from(ActivityReportAnIssue.this);
        View dialogView = li.inflate(R.layout.dialog_feedback, null);

        idGoesHere(dialogView, message);


        alertDialogBuilder = new AlertDialog.Builder(ActivityReportAnIssue.this);
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
