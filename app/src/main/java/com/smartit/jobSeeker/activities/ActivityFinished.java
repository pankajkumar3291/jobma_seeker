package com.smartit.jobSeeker.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;

import am.appwise.components.ni.NoInternetDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityFinished extends AppCompatActivity implements View.OnClickListener {


    private ImageView backarr, imagePRQ, imageSQ;
    private TextView tvPreRecordedQues, tvMCQ, tvSubjectiveQues;
    private Button btnFinished;

    private NoInternetDialog noInternetDialog;


    private String applied_id, source_id;

    private String getSourceId;
    private Integer getInterviewId;
    private String comingAllTheWayFromAts;
    private String invitationIdForAts;


    private Intent mIntent;
    private TextView tvAppTitle;


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
        setContentView(R.layout.activity_finished);

        this.mIntent = getIntent();
        getData(mIntent);

        noInternetDialog = new NoInternetDialog.Builder(this).build();

        applied_id = mIntent.getStringExtra("appliedId");
        source_id = mIntent.getStringExtra("sourceId");


//        for ats

        getSourceId = mIntent.getStringExtra("GET_SOURCEID_FOR_ATS");
        getInterviewId = mIntent.getIntExtra("INTERVIEWID_FOR_ATS", 0);
        comingAllTheWayFromAts = mIntent.getStringExtra("COMING_ALL_THE_WAY_FROM_ATS");
        invitationIdForAts = mIntent.getStringExtra("INVITATIONID_FOR_ATS");


        findingIdsHere();
        eventListenerGoesHere();


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


    private void eventListenerGoesHere() {

        backarr.setOnClickListener(this);
        btnFinished.setOnClickListener(this);
    }

    private void findingIdsHere() {

        backarr = findViewById(R.id.backarr);
        imagePRQ = findViewById(R.id.imagePRQ);
        imageSQ = findViewById(R.id.imageSQ);

        tvPreRecordedQues = findViewById(R.id.tvPreRecordedQues);
        tvMCQ = findViewById(R.id.tvMCQ);
        tvSubjectiveQues = findViewById(R.id.tvSubjectiveQues);

        btnFinished = findViewById(R.id.btnFinished);

        tvAppTitle = findViewById(R.id.tvAppTitle);
        tvAppTitle.setTypeface(tvAppTitle.getTypeface(), Typeface.BOLD);


        if (mIntent.hasExtra("COMING_ALL_THE_WAY_FROM_ATS") && comingAllTheWayFromAts != null) {


            tvPreRecordedQues.setText(Hawk.get("PRE_RECORDED_QUESTIONS_FROM_ATS") + " Pre-recorded questions");
            tvMCQ.setText(Hawk.get("MCQ_QUESTIONS_FROM_ATS") + " Multiple choice question");
            tvSubjectiveQues.setText(Hawk.get("ESSAY_QUESTIONS_FROM_ATS") + " Subjective question");


        } else {


//            tvPreRecordedQues.setText(intent.getStringExtra("PR") + "- Pre-recorded questions");
            tvPreRecordedQues.setText(Hawk.get("PRE_RECORDE_QUESTION") + " Pre-recorded questions");
//            tvMCQ.setText(intent.getStringExtra("MCQ") + "- Multiple choice question");
            tvMCQ.setText(Hawk.get("MULTIPLE_CHOICE_QUESTION") + " Multiple choice question");
//            tvSubjectiveQues.setText(intent.getStringExtra("SUB") + "- Subjective question");
            tvSubjectiveQues.setText(Hawk.get("SUBJECTIVE_QUESTION") + " Subjective question");


        }


    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {


            case R.id.backarr:
                finish();
                break;

            case R.id.btnFinished:


                Intent intent = new Intent(ActivityFinished.this, ActivityFeedback.class);
                intent.putExtra("APPLIED_ID", applied_id);
                intent.putExtra("SOURCE_ID", source_id);
                intent.putExtra("SOURCE_ID_FOR_ATS", getSourceId);
                intent.putExtra("INTERVIEW_ID_FOR_ATS", getInterviewId);
                intent.putExtra("comingAllTheWayFromAts", comingAllTheWayFromAts);
                intent.putExtra("invitationIdForAts", invitationIdForAts);
                // finishing all activities just using this to check flag
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                Toast.makeText(getApplicationContext(), "Job applied successfully", Toast.LENGTH_LONG).show();

                break;
        }

    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
