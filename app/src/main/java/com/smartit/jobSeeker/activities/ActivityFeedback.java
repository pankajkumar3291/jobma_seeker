package com.smartit.jobSeeker.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.interviewFeedback.InteeviewFeedback;
import com.smartit.jobSeeker.atsApiResponses.feedback_for_ats.FeedbackForAts;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityFeedback extends AppCompatActivity implements View.OnClickListener {


    private EditText edFeedback;
    private Button btnPost;
    private RatingBar ratingBar;
    private NoInternetDialog noInternetDialog;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private TextView tvSuccessMessage;
    private Button buttonOk;
    private float ratingvalue;

    private ProgressDialog mProgressDialog;
    private TextView tvJobTitleForJob;


    String APPLIEDID, SOURCEID;
    String token;
    private TextView tvFeedbackTitle;


    // ATS

    private String getSourceIdForAts;
    private Integer getInterviewIdForAts;
    private String comingAllTheWayFromAts;
    private String getInvitationIdForAts;


    private Intent mIntent;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        getData(intent);


    }

    private void getData(Intent intent) {

        this.mIntent = intent;


    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        this.mIntent = getIntent();
        getData(mIntent);

        noInternetDialog = new NoInternetDialog.Builder(this).build();
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);


        APPLIEDID = mIntent.getStringExtra("APPLIED_ID");
        SOURCEID = mIntent.getStringExtra("SOURCE_ID");


        getSourceIdForAts = mIntent.getStringExtra("SOURCE_ID_FOR_ATS");
        getInterviewIdForAts = mIntent.getIntExtra("INTERVIEW_ID_FOR_ATS", 0);
        comingAllTheWayFromAts = mIntent.getStringExtra("comingAllTheWayFromAts");
        getInvitationIdForAts = mIntent.getStringExtra("invitationIdForAts");


        findingIdsHere();
        eventListenerGoesHere();


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void eventListenerGoesHere() {

        btnPost.setOnClickListener(this);


    }

    private void findingIdsHere() {

        edFeedback = findViewById(R.id.edFeedback);
        btnPost = findViewById(R.id.btnPost);
        ratingBar = findViewById(R.id.ratingBar);
        tvJobTitleForJob = findViewById(R.id.tvJobTitleForJob);
        tvFeedbackTitle = findViewById(R.id.tvFeedbackTitle);
        tvFeedbackTitle.setTypeface(tvFeedbackTitle.getTypeface(), Typeface.BOLD);

        tvJobTitleForJob.setText(Hawk.get("JobTitle"));  //"Your application has been successfully submitted for  " +


//        Spannable word = new SpannableString("Your application has been successfully submitted for " + Hawk.get("JobTitle"));
//        word.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 53, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvJobTitleForJob.setTypeface(tvJobTitleForJob.getTypeface(), Typeface.BOLD);
//        tvJobTitleForJob.setText(word);


        getRatings();


    }

    private void getRatings() {


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

//                Float ratingVal = (Float) rating;
                ratingvalue = ratingBar.getRating();


//                Toast.makeText(getApplicationContext(), " Ratings : " + String.valueOf(ratingVal) + "", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), " Ratings1 : " + ratingvalue, Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        noInternetDialog.onDestroy();
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.btnPost:

                if (ratingBar.getRating() == 0.0) {

                    Toast.makeText(getApplicationContext(), "Your rating is must to submit", Toast.LENGTH_LONG).show();

                } else if (edFeedback.getText().toString().trim().equalsIgnoreCase("")) {

                    Toast.makeText(getApplicationContext(), "Your feedback is must to submit", Toast.LENGTH_LONG).show();

                } else {


                    if (mIntent.hasExtra("comingAllTheWayFromAts") && comingAllTheWayFromAts != null) {

                        feedbackFromAts();

                    } else {

                        feedbackFromNormal();
                    }


                }


                break;
        }
    }

    private void feedbackFromAts() {


        HttpModule.provideRepositoryService().feedbackForAts(Hawk.get("SAVED_INVITATIONID"), (int) ratingvalue, edFeedback.getText().toString(), Hawk.get("SAVED_SOURCEID")).enqueue(new Callback<FeedbackForAts>() {
            @Override
            public void onResponse(Call<FeedbackForAts> call, Response<FeedbackForAts> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                    Hawk.delete("SAVED_SOURCEID");
                    Hawk.delete("SAVED_INVITATIONID");
                    callTheThankyouSubmissionActivity();


                } else {

                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<FeedbackForAts> call, Throwable t) {

                System.out.println("ActivityFeedback.onFailure " + t.getMessage());

            }
        });


    }

    private void callTheThankyouSubmissionActivity() {

        Intent intent = new Intent(ActivityFeedback.this, ActivitySubmissionThanku.class);
        // finishing all activities just using this to check flag
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();


    }

    private void feedbackFromNormal() {

        HttpModule.provideRepositoryService().interviewFeedback((String) Hawk.get("GET_TOKEN"), APPLIEDID, (int) ratingvalue, edFeedback.getText().toString(), SOURCEID).enqueue(new Callback<InteeviewFeedback>() {
            @Override
            public void onResponse(Call<InteeviewFeedback> call, Response<InteeviewFeedback> response) {


                if (response.body() != null && response.body().getError() == 0) {


                    token = (String) Hawk.get("GET_TOKEN");

                    dialogFeedback(response.body().getMessage());

                } else {

                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<InteeviewFeedback> call, Throwable t) {

                System.out.println("ActivityFeedback.onFailure " + t.getMessage());

            }
        });


    }

    private void dialogFeedback(String message) {

        LayoutInflater li = LayoutInflater.from(ActivityFeedback.this);
        View dialogView = li.inflate(R.layout.dialog_feedback, null);

        findTheIdsHere(dialogView, message);

        alertDialogBuilder = new AlertDialog.Builder(ActivityFeedback.this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void findTheIdsHere(View dialogView, String message) {

        tvSuccessMessage = dialogView.findViewById(R.id.tvSuccessMessage);
        buttonOk = dialogView.findViewById(R.id.buttonOk);
        tvSuccessMessage.setText(message);


        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                loadingProgress();
                Intent intent = new Intent(ActivityFeedback.this, ActivityDashboard.class);
                intent.putExtra("token", token);
                startActivity(intent);
                finish();
                mProgressDialog.dismiss();


            }
        });


    }

    private void loadingProgress() {

        mProgressDialog.setMessage("Submitting your feedback");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
