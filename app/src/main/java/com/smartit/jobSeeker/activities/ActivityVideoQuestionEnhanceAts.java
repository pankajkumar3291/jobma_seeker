package com.smartit.jobSeeker.activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.exoplayer2.util.Assertions;
import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.atsApiResponses.ats_total_question.AtsTotalInteviewQuestion;
import com.smartit.jobSeeker.demo.PlayerActivity;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ActivityVideoQuestionEnhanceAts extends AppCompatActivity implements View.OnClickListener {


    private String FILE_PATH_NAME, FILE_THUMB;
    private String FILE_PATH_HLS;


    private ImageView imageThumb, imagePlayThumb;
    private TextView tvQuestionTitle, btnSeeMore, tvOutOfQuestion, tvTotalDisplayQuestion;
    private Button btnContinuee;
    private int interviewJobId;
    private int increamentedQuestionValue;
    private int totalQuestions;
    private String questionTitle;
    private String comingAllTheWayFromAts;


    private int kitId, interviewId, catcherId;
    private String getSourceId;
    private String VIDEO_URL;


    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private TextView tvSuccessMessage;
    private Button buttonOk;


    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_question_enhance);

        Hawk.init(this).build();

        if (getIntent() != null && getIntent().hasExtra("FILE_PATH")) {
            FILE_PATH_NAME = getIntent().getStringExtra("FILE_PATH");
        }

        if (getIntent() != null && getIntent().hasExtra("FILE_PATH_HLS")) {
            FILE_PATH_HLS = getIntent().getStringExtra("FILE_PATH_HLS");
        }


        if (FILE_PATH_NAME != null && FILE_PATH_HLS != null) {

            if (FILE_PATH_HLS.equalsIgnoreCase("")) { // HLS BALANK THEN FILE URL

                VIDEO_URL = FILE_PATH_NAME;  // FILE URL

            } else if (!FILE_PATH_HLS.equalsIgnoreCase("")) { // NOT BLANK THEN HLS ITSELF

                VIDEO_URL = FILE_PATH_HLS;// HLS URL

            } else if (FILE_PATH_HLS.equalsIgnoreCase("") && FILE_PATH_NAME.equalsIgnoreCase("")) {

                VIDEO_URL = "";
            }
        } else {

            System.out.println("ActivityVideoQuestionEnhance.onCreate ");
        }


        if (getIntent() != null && getIntent().hasExtra("FILE_THUMB")) {
            FILE_THUMB = getIntent().getStringExtra("FILE_THUMB");
        }

        if (getIntent() != null && getIntent().hasExtra("INCREAMENTED_VALUE")) {
            increamentedQuestionValue = getIntent().getIntExtra("INCREAMENTED_VALUE", 0);

        }

        if (getIntent() != null && getIntent().hasExtra("TOTAL_QUESTIONS_FOR_ATS")) {
            totalQuestions = getIntent().getIntExtra("TOTAL_QUESTIONS_FOR_ATS", 0);
        }

        if (getIntent() != null && getIntent().hasExtra("QUESTION_TITLE")) {
            questionTitle = getIntent().getStringExtra("QUESTION_TITLE");
        }

        if (getIntent() != null && getIntent().hasExtra("KITID_FOR_ATS")) {
            kitId = getIntent().getIntExtra("KITID_FOR_ATS", 0);
        }

        if (getIntent() != null && getIntent().hasExtra("INTERVIEWID_FOR_ATS")) {
            interviewId = getIntent().getIntExtra("INTERVIEWID_FOR_ATS", 0);
        }

        if (getIntent() != null && getIntent().hasExtra("CATCHER_ID_FOR_ATS")) {
            catcherId = getIntent().getIntExtra("CATCHER_ID_FOR_ATS", 0);
        }

        if (getIntent() != null && getIntent().hasExtra("GET_SOURCEID")) {
            getSourceId = getIntent().getStringExtra("GET_SOURCEID");
        }


        if (getIntent() != null && getIntent().hasExtra("COMING_ALL_THE_WAY_FROM_ATS")) {
            comingAllTheWayFromAts = getIntent().getStringExtra("COMING_ALL_THE_WAY_FROM_ATS");
        }

        initViewsHere();


    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void initViewsHere() {

        imageThumb = findViewById(R.id.imageThumb);
        imagePlayThumb = findViewById(R.id.imagePlayThumb);

        tvQuestionTitle = findViewById(R.id.tvQuestionTitle);
        btnSeeMore = findViewById(R.id.btnSeeMore);
        tvOutOfQuestion = findViewById(R.id.tvOutOfQuestion);
        tvTotalDisplayQuestion = findViewById(R.id.tvTotalDisplayQuestion);

        btnContinuee = findViewById(R.id.btnContinuee);
        btnContinuee.setOnClickListener(this);

        Picasso.get().load(FILE_THUMB).into(imageThumb);


        tvOutOfQuestion.setText("" + getIntent().getIntExtra("INCREAMENTED_VALUE", 0));
        tvTotalDisplayQuestion.setText("" + "/" + totalQuestions);
        tvQuestionTitle.setText(questionTitle);


        tvQuestionTitle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (tvQuestionTitle.getLineCount() > 3) {
                    btnSeeMore.setVisibility(View.VISIBLE);

                } else {
                    System.out.println("ActivityVideoQuestionEnhance.onGlobalLayout");
                }

            }
        });

        btnSeeMore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialogForLongQuestion(questionTitle); // questionsAll.get(0).getQuesTitle()


            }
        });


        imagePlayThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (VIDEO_URL != null) {

                    if (VIDEO_URL.equalsIgnoreCase("")) {
                        Toast.makeText(ActivityVideoQuestionEnhanceAts.this, "No Video Found", Toast.LENGTH_LONG).show();
                    } else {

                        Sample sample = new UriSample(
                                "Super speed (PlayReady)",
                                null,
                                Uri.parse(VIDEO_URL),
                                null,
                                null,
                                null);

                        startActivity(
                                sample.buildIntent(
                                        ActivityVideoQuestionEnhanceAts.this,
                                        false,
                                        PlayerActivity.ABR_ALGORITHM_DEFAULT));


                    }
                } else {
                    Toast.makeText(ActivityVideoQuestionEnhanceAts.this, "Please upload the video first", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    private void dialogForLongQuestion(String questionTitle) {


        LayoutInflater li = LayoutInflater.from(ActivityVideoQuestionEnhanceAts.this);
        View dialogView = li.inflate(R.layout.dialog_for_long_question, null);

        findTheIdsHere(dialogView, questionTitle);// quesTitle

        alertDialogBuilder = new AlertDialog.Builder(ActivityVideoQuestionEnhanceAts.this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void findTheIdsHere(View dialogView, String questionTitle) {

        tvSuccessMessage = dialogView.findViewById(R.id.tvSuccessMessage);
        buttonOk = dialogView.findViewById(R.id.buttonOk);

        tvSuccessMessage.setMovementMethod(new ScrollingMovementMethod());
        tvSuccessMessage.setText(questionTitle);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnContinuee:


                HttpModule.provideRepositoryService().totalInterviewQuestionForAts(kitId, interviewId).enqueue(new Callback<AtsTotalInteviewQuestion>() {
                    @Override
                    public void onResponse(Call<AtsTotalInteviewQuestion> call, Response<AtsTotalInteviewQuestion> response) {


                        Intent intent = new Intent(ActivityVideoQuestionEnhanceAts.this, ActivityVideoInterviewForAts.class);

                        intent.putExtra("PRE_RECORDED_KIT_LIST_FOR_ATS", (Serializable) Objects.requireNonNull(response.body()).getData().getTotalQuestion().getQuestion());
                        intent.putExtra("OPTIONS_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions());
                        intent.putExtra("TOTAL_QUESTIONS_FOR_ATS", response.body().getData().getTotalQustion());
                        intent.putExtra("INVITATIONID_FOR_ATS", response.body().getData().getInvitationId());
                        intent.putExtra("KITID_FOR_ATS", kitId);
                        intent.putExtra("INTERVIEWID_FOR_ATS", interviewId);
                        intent.putExtra("CATCHER_ID_FOR_ATS", catcherId);
                        intent.putExtra("GET_SOURCEID", getSourceId);
                        intent.putExtra("COMING_ALL_THE_WAY_FROM_ATS", comingAllTheWayFromAts);
                        intent.putExtra("INCREAMENTED_VALUE_FOR_ATS", response.body().getData().getTotalQustion() - response.body().getData().getRemainQustion() + 1);
                        // finishing all activities just using this to check flag
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Animatoo.animateFade(ActivityVideoQuestionEnhanceAts.this);
                        finish();


                    }

                    @Override
                    public void onFailure(Call<AtsTotalInteviewQuestion> call, Throwable t) {
                        System.out.println("ActivityVideoQuestionEnhanceAts.onFailure " + t.getMessage());
                    }
                });


                break;

        }


    }


    private abstract static class Sample {
        public final String name;
        public final DrmInfo drmInfo;

        public Sample(String name, DrmInfo drmInfo) {
            this.name = name;
            this.drmInfo = drmInfo;
        }

        public Intent buildIntent(
                Context context, boolean preferExtensionDecoders, String abrAlgorithm) {
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.putExtra(PlayerActivity.PREFER_EXTENSION_DECODERS_EXTRA, preferExtensionDecoders);
            intent.putExtra(PlayerActivity.ABR_ALGORITHM_EXTRA, abrAlgorithm);
            if (drmInfo != null) {
                drmInfo.updateIntent(intent);
            }
            return intent;
        }

    }


    private static final class UriSample extends Sample {

        public final Uri uri;
        public final String extension;
        public final String adTagUri;
        public final String sphericalStereoMode;

        public UriSample(
                String name,
                DrmInfo drmInfo,
                Uri uri,
                String extension,
                String adTagUri,
                String sphericalStereoMode) {
            super(name, drmInfo);
            this.uri = uri;
            this.extension = extension;
            this.adTagUri = adTagUri;
            this.sphericalStereoMode = sphericalStereoMode;
        }

        @Override
        public Intent buildIntent(
                Context context, boolean preferExtensionDecoders, String abrAlgorithm) {
            return super.buildIntent(context, preferExtensionDecoders, abrAlgorithm)
                    .setData(uri)
                    .putExtra(PlayerActivity.EXTENSION_EXTRA, extension)
                    .putExtra(PlayerActivity.AD_TAG_URI_EXTRA, adTagUri)
                    .putExtra(PlayerActivity.SPHERICAL_STEREO_MODE_EXTRA, sphericalStereoMode)
                    .setAction(PlayerActivity.ACTION_VIEW);
        }

    }


    private static final class DrmInfo {
        public final String drmScheme;
        public final String drmLicenseUrl;
        public final String[] drmKeyRequestProperties;
        public final boolean drmMultiSession;

        public DrmInfo(
                String drmScheme,
                String drmLicenseUrl,
                String[] drmKeyRequestProperties,
                boolean drmMultiSession) {
            this.drmScheme = drmScheme;
            this.drmLicenseUrl = drmLicenseUrl;
            this.drmKeyRequestProperties = drmKeyRequestProperties;
            this.drmMultiSession = drmMultiSession;
        }

        public void updateIntent(Intent intent) {
            Assertions.checkNotNull(intent);
            intent.putExtra(PlayerActivity.DRM_SCHEME_EXTRA, drmScheme);
            intent.putExtra(PlayerActivity.DRM_LICENSE_URL_EXTRA, drmLicenseUrl);
            intent.putExtra(PlayerActivity.DRM_KEY_REQUEST_PROPERTIES_EXTRA, drmKeyRequestProperties);
            intent.putExtra(PlayerActivity.DRM_MULTI_SESSION_EXTRA, drmMultiSession);
        }
    }


    @Override
    public void onBackPressed() {
    }
}

