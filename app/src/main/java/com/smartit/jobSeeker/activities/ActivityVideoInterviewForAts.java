package com.smartit.jobSeeker.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.atsApiResponses.atsRetake.AtsRetake;
import com.smartit.jobSeeker.atsApiResponses.ats_total_question.AtsTotalInteviewQuestion;
import com.smartit.jobSeeker.atsApiResponses.submit_answer_video_for_ats.VideoSubmitAnsForAts;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.smartit.jobSeeker.upload.ProgressRequestBody;
import com.smartit.jobSeeker.utilsCamera.CameraPreview;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityVideoInterviewForAts extends AppCompatActivity implements View.OnClickListener, ProgressRequestBody.UploadCallbacks {


    private TextView tvTotalQuestionCountVideo, tvQuestionVideo, tvDurationTimeVideo, tvThinkTimeVideo, tvAttemptsVideo, tvNumberOfQuestionVideo;
    private String invitationId, postId;


    private Camera mCamera;
    private CameraPreview mPreview;
    private MediaRecorder mediaRecorder;
    private Context myContext;
    private LinearLayout cameraPreview;
    private boolean cameraFront = false;
    private boolean isFront;


    private ImageButton imageSwitch, imageRecord, imageStop, imageSkip;
    private Float thinktime, durationOfQuestions;
    boolean recording = false;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private Button btnRetakeAvailable, btnPublishAndContiAvailable, btnPublishAndContiJust;
    private CountDownTimer countDownTimerThinkTime, countDownTimerDuration;

    private int attempts = 1;
    private File file;
    private NoInternetDialog noInternetDialog;
    private ProgressDialog mProgressDialog;

    private String COMINGALLTHEWAYFROMATS;

    private boolean recordingCheckForSkip;

    // FOR ATS CASE


    private ArrayList<com.smartit.jobSeeker.atsApiResponses.ats_total_question.Question> questionsAllForAts;
    private Integer totalQuestionsForAts;
    private String optionsForAts;
    private String invitationIdForAts;
    private Integer kitidForAts;
    private Integer interviewIdForAts;
    private Integer increamentedValueForAts;


    private Integer getCatcherId;

    long l1ThinkTimer, l1DurationTimer;
    float f1ThinkTimer, f1DurationFloat;

    private String getSourceId;


    private TextView btnSeeMore;
    private TextView tvSuccessMessage;
    private Button buttonOk;

    private double timeConsumed = 0;
    DecimalFormat df;


    private Intent mIntent;
    private ProgressBar tvProgress;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getData(intent);


    }

    private void getData(Intent intent) {

        this.mIntent = intent;
        COMINGALLTHEWAYFROMATS = intent.getStringExtra("COMING_ALL_THE_WAY_FROM_ATS");

        HttpModule.provideRepositoryService().totalInterviewQuestionForAts(mIntent.getIntExtra("KITID_FOR_ATS", 0), mIntent.getIntExtra("INTERVIEWID_FOR_ATS", 0)).enqueue(new Callback<AtsTotalInteviewQuestion>() {
            @Override
            public void onResponse(Call<AtsTotalInteviewQuestion> call, Response<AtsTotalInteviewQuestion> response) {

                if (response.body() != null && response.body().getError() == 0) {


                    Intent intent = new Intent(ActivityVideoInterviewForAts.this, ActivityVideoInterviewForAts.class);
                    intent.putExtra("COMING_ALL_THE_WAY_FROM_ATS", COMINGALLTHEWAYFROMATS);
                    intent.putExtra("PRE_RECORDED_KIT_LIST_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion());
                    intent.putExtra("OPTIONS_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions());
                    intent.putExtra("TOTAL_QUESTIONS_FOR_ATS", mIntent.getIntExtra("TOTAL_QUESTIONS_FOR_ATS", 0));
                    intent.putExtra("INVITATIONID_FOR_ATS", response.body().getData().getInvitationId());
                    intent.putExtra("KITID_FOR_ATS", mIntent.getIntExtra("KITID_FOR_ATS", 0));
                    intent.putExtra("INTERVIEWID_FOR_ATS", mIntent.getIntExtra("INTERVIEWID_FOR_ATS", 0));
                    intent.putExtra("CATCHER_ID_FOR_ATS", mIntent.getIntExtra("CATCHER_ID_FOR_ATS", 0));
                    intent.putExtra("GET_SOURCEID", mIntent.getStringExtra("GET_SOURCEID"));
                    intent.putExtra("INCREAMENTED_VALUE_FOR_ATS", mIntent.getIntExtra("INCREAMENTED_VALUE_FOR_ATS", 0));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Animatoo.animateFade(ActivityVideoInterviewForAts.this);
                    finish();


                } else {

                    System.out.println("ActivityVideoInterview.onResponse");

                }


            }

            @Override
            public void onFailure(Call<AtsTotalInteviewQuestion> call, Throwable t) {

            }
        });


    }


    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_interview);


        this.mIntent = getIntent();


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myContext = this;
        noInternetDialog = new NoInternetDialog.Builder(this).build();

        COMINGALLTHEWAYFROMATS = getIntent().getStringExtra("COMING_ALL_THE_WAY_FROM_ATS");


        findingIdsHere();
        eventlistner();
        initialize();


        questionsAllForAts = (ArrayList<com.smartit.jobSeeker.atsApiResponses.ats_total_question.Question>) getIntent().getSerializableExtra("PRE_RECORDED_KIT_LIST_FOR_ATS");
        totalQuestionsForAts = getIntent().getIntExtra("TOTAL_QUESTIONS_FOR_ATS", 0);
//        optionsForAts = getIntent().getStringExtra("OPTIONS_FOR_ATS");
        invitationIdForAts = getIntent().getStringExtra("INVITATIONID_FOR_ATS");
        kitidForAts = getIntent().getIntExtra("KITID_FOR_ATS", 0);
        interviewIdForAts = getIntent().getIntExtra("INTERVIEWID_FOR_ATS", 0);
        increamentedValueForAts = getIntent().getIntExtra("INCREAMENTED_VALUE_FOR_ATS", 0);
        getCatcherId = getIntent().getIntExtra("CATCHER_ID_FOR_ATS", 0);
        getSourceId = getIntent().getStringExtra("GET_SOURCEID");


        thinktime = Float.valueOf(questionsAllForAts.get(0).getThinktime());
        durationOfQuestions = Float.valueOf(questionsAllForAts.get(0).getDuration());


        tvTotalQuestionCountVideo.setText("/" + getIntent().getIntExtra("TOTAL_QUESTIONS_FOR_ATS", 0));
        tvQuestionVideo.setText(questionsAllForAts.get(0).getQuesTitle());
        tvDurationTimeVideo.setText(questionsAllForAts.get(0).getDuration());
//            tvAttemptsVideo.setText(questionsAllForAts.get(0).getAttempts().toString());
        tvNumberOfQuestionVideo.setText("" + increamentedValueForAts);


        tvQuestionVideo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (tvQuestionVideo.getLineCount() > 3) {
                    btnSeeMore.setVisibility(View.VISIBLE);

                }

            }
        });

        btnSeeMore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialogForLongQuestion(questionsAllForAts.get(0).getQuesTitle());


            }
        });


        // FOR THINK TIMER
        f1ThinkTimer = thinktime * 60000f;
        l1ThinkTimer = (long) f1ThinkTimer;


        // FOR DURATION TIMER
        f1DurationFloat = durationOfQuestions * 60000f;
        l1DurationTimer = (long) f1DurationFloat;

        if (questionsAllForAts.get(0).getOptional().equalsIgnoreCase("1")) {
            imageSkip.setVisibility(View.VISIBLE);
        }


//        if (!questionsAllForAts.get(0).getThinktime().equalsIgnoreCase("0.0") && questionsAllForAts.get(0).getAttempts() < 0) {
//            tvAttemptsVideo.setText("Unlimited");
//        }else {
//            tvAttemptsVideo.setText(questionsAllForAts.get(0).getAttempts().toString());
//        }


//        if (questionsAllForAts.get(0).getAttempts() < 0) {
//
//            tvAttemptsVideo.setText("Unlimited");
//
//        } else {
//
//            tvAttemptsVideo.setText(questionsAllForAts.get(0).getAttempts().toString());
//        }


        if (questionsAllForAts.get(0).getThinktime().equalsIgnoreCase("0.0")) {

            l1ThinkTimer = 120 * 60000;
            tvAttemptsVideo.setText("Unlimited");

        } else {

            tvAttemptsVideo.setText(questionsAllForAts.get(0).getAttempts().toString());
        }

        countDownTimerThinkTime = new CountDownTimer(l1ThinkTimer, 1000) {

            public void onTick(long millisUntilFinished) {


                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;

                if (questionsAllForAts.get(0).getThinktime().equalsIgnoreCase("0.0")) {

                    tvAttemptsVideo.setText("Unlimited");
                    tvThinkTimeVideo.setText("Unlimited");

                } else if (!questionsAllForAts.get(0).getThinktime().equalsIgnoreCase("0.0") && questionsAllForAts.get(0).getAttempts() < 0) {

                    tvAttemptsVideo.setText("Unlimited");
                    tvThinkTimeVideo.setText(String.format("%02d:%02d", minutes, seconds));

                } else {

                    tvThinkTimeVideo.setText(String.format("%02d:%02d", minutes, seconds));
                }

                thinktime--;
            }

            public void onFinish() {

                tvThinkTimeVideo.setText("No");
                tvThinkTimeVideo.setTextColor(Color.parseColor("#2DBFD9"));

                imageRecord.setVisibility(View.GONE);
                imageStop.setVisibility(View.VISIBLE);
                imageSwitch.setVisibility(View.GONE);


                if (questionsAllForAts.get(0).getOptional().equalsIgnoreCase("1")) {
                    imageSkip.setVisibility(View.VISIBLE);
                }

                timerForDuration();
                videoAutomaticallyStartsRecording();


            }

        }.start();


        imageStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if ((timeConsumed) < 5) {
                        Toast.makeText(getApplicationContext(), "Minimum recording should be 5 seconds", Toast.LENGTH_LONG).show();
                        return;

                    } else {

                        mediaRecorder.stop();
                        mediaRecorder.release();
                        mediaRecorder = null;

                        Toast.makeText(getApplicationContext(), "Stop recording..", Toast.LENGTH_SHORT).show();
                        countDownTimerDuration.cancel();


                        if (questionsAllForAts.get(0).getAttempts() < 0) {

                            dialogPublishAndContinueWithRetake();

                        } else if (questionsAllForAts.get(0).getAttempts() > 0) {

                            dialogPublishAndContinueWithRetake();

                        } else {

                            dialogPublishAndContinue();
                        }


                    }


                } catch (IllegalStateException e) {
                    //  it is called before start()
                    e.printStackTrace();
                } catch (RuntimeException e) {
                    // no valid audio/video data has been received
                    System.out.println("ActivityVideoInterview.onClick exception  " + e);
                    e.printStackTrace();
                }


            }
        });


    }


    private void dialogForLongQuestion(String quesTitle) {


        LayoutInflater li = LayoutInflater.from(ActivityVideoInterviewForAts.this);
        View dialogView = li.inflate(R.layout.dialog_for_long_question, null);

        findTheIdsHere(dialogView, quesTitle);

        alertDialogBuilder = new AlertDialog.Builder(ActivityVideoInterviewForAts.this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void findTheIdsHere(View dialogView, String quesTitle) {

        tvSuccessMessage = dialogView.findViewById(R.id.tvSuccessMessage);

        buttonOk = dialogView.findViewById(R.id.buttonOk);

        tvSuccessMessage.setMovementMethod(new ScrollingMovementMethod());
        tvSuccessMessage.setText(quesTitle);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        noInternetDialog.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void dialogPublishAndContinue() {


        LayoutInflater li = LayoutInflater.from(ActivityVideoInterviewForAts.this);
        View dialogView = li.inflate(R.layout.dialog_publish_and_continue, null);

        findIdsForDialogForPublish(dialogView);

        alertDialogBuilder = new AlertDialog.Builder(ActivityVideoInterviewForAts.this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder
                .setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void findIdsForDialogForPublish(View dialogView) {

        btnPublishAndContiJust = dialogView.findViewById(R.id.btnPublishAndContiJust);
        btnPublishAndContiJust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                loadingProgress();
                submitAnswerApiVideoForAts();

            }
        });


    }

    private void submitAnswerApiVideoForAts() {


        final RequestBody reqFile = RequestBody.create(MediaType.parse("video/*"), file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("video_answer", file.getName(), reqFile); // NOTE: upload here is the name which is the file name


        ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
        MultipartBody.Part body1 = MultipartBody.Part.createFormData("video_answer", file.getName(), fileBody);


        HttpModule.provideRepositoryService().videoForAts(Integer.parseInt(questionsAllForAts.get(0).getQtype()), (float) (timeConsumed / 60.0), totalQuestionsForAts, body1, questionsAllForAts.get(0).getAttempts(), getIntent().getIntExtra("CATCHER_ID_FOR_ATS", 0), questionsAllForAts.get(0).getQues(), interviewIdForAts).enqueue(new Callback<VideoSubmitAnsForAts>() {
            @Override
            public void onResponse(Call<VideoSubmitAnsForAts> call, Response<VideoSubmitAnsForAts> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    callTotalInterviewQuestionsForAts();

                } else {

                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<VideoSubmitAnsForAts> call, Throwable t) {

                mProgressDialog.dismiss();
                System.out.println("ActivityVideoInterviewForAts.onFailure " + t.getMessage());

            }
        });


    }

    private void callTotalInterviewQuestionsForAts() {

        HttpModule.provideRepositoryService().totalInterviewQuestionForAts(kitidForAts, interviewIdForAts).enqueue(new Callback<AtsTotalInteviewQuestion>() {
            @Override
            public void onResponse(Call<AtsTotalInteviewQuestion> call, Response<AtsTotalInteviewQuestion> response) {


                if (response.body() != null && response.body().getError() == 0) {


                    if (response.body().getData().getTotalQuestion().getQuestion().get(0).getQtype().equalsIgnoreCase("1")) {


                        if (response.body().getData().getTotalQuestion().getQuestion().get(0).getFile_status() == 1) {

                            Intent intent = new Intent(ActivityVideoInterviewForAts.this, ActivityVideoQuestionEnhanceAts.class);
                            intent.putExtra("FILE_PATH", response.body().getData().getTotalQuestion().getQuestion().get(0).getFile_name());
                            intent.putExtra("FILE_PATH_HLS", response.body().getData().getTotalQuestion().getQuestion().get(0).getHls());
                            intent.putExtra("FILE_THUMB", response.body().getData().getTotalQuestion().getQuestion().get(0).getFile_thumb());
                            intent.putExtra("INCREAMENTED_VALUE", response.body().getData().getTotalQustion() - response.body().getData().getRemainQustion() + 1);
                            intent.putExtra("TOTAL_QUESTIONS_FOR_ATS", response.body().getData().getTotalQustion());
                            intent.putExtra("QUESTION_TITLE", response.body().getData().getTotalQuestion().getQuestion().get(0).getQuesTitle());
                            intent.putExtra("KITID_FOR_ATS", kitidForAts);
                            intent.putExtra("INTERVIEWID_FOR_ATS", interviewIdForAts);
                            intent.putExtra("CATCHER_ID_FOR_ATS", getCatcherId);
                            intent.putExtra("GET_SOURCEID", getSourceId);
                            intent.putExtra("COMING_ALL_THE_WAY_FROM_ATS", mIntent.getStringExtra("COMING_ALL_THE_WAY_FROM_ATS"));
                            startActivity(intent);
                            finish();


                        } else {

                            finish();
                            startActivity(getIntent().
                                    putExtra("PRE_RECORDED_KIT_LIST_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion()).
                                    putExtra("OPTIONS_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions()).
                                    putExtra("TOTAL_QUESTIONS_FOR_ATS", response.body().getData().getTotalQustion()).
                                    putExtra("INVITATIONID_FOR_ATS", response.body().getData().getInvitationId()).
                                    putExtra("KITID_FOR_ATS", mIntent.getIntExtra("KITID_FOR_ATS", 0)).
                                    putExtra("INTERVIEWID_FOR_ATS", mIntent.getIntExtra("INTERVIEWID_FOR_ATS", 0)).
                                    putExtra("CATCHER_ID_FOR_ATS", mIntent.getIntExtra("CATCHER_ID_FOR_ATS", 0)).
                                    putExtra("COMING_ALL_THE_WAY_FROM_ATS", mIntent.getStringExtra("COMING_ALL_THE_WAY_FROM_ATS")).
                                    putExtra("GET_SOURCEID_FOR_ATS", mIntent.getStringExtra("GET_SOURCEID")).
                                    setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
                                    putExtra("INCREAMENTED_VALUE_FOR_ATS", increamentedValueForAts + 1));

                            Animatoo.animateFade(ActivityVideoInterviewForAts.this);

                        }


                    } else if (response.body().getData().getTotalQuestion().getQuestion().get(0).getQtype().equalsIgnoreCase("2")) {

                        Intent intent = new Intent(ActivityVideoInterviewForAts.this, ActivityMultipleChoiceQuestion.class);
                        intent.putExtra("PRE_RECORDED_KIT_LIST_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion());
                        intent.putExtra("OPTIONS_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions());
                        intent.putExtra("TOTAL_QUESTIONS_FOR_ATS", response.body().getData().getTotalQustion());
                        intent.putExtra("INVITATIONID_FOR_ATS", response.body().getData().getInvitationId());
                        intent.putExtra("KITID_FOR_ATS", mIntent.getIntExtra("KITID_FOR_ATS", 0));
                        intent.putExtra("INTERVIEWID_FOR_ATS", mIntent.getIntExtra("INTERVIEWID_FOR_ATS", 0));
                        intent.putExtra("CATCHER_ID_FOR_ATS", mIntent.getIntExtra("CATCHER_ID_FOR_ATS", 0));
                        intent.putExtra("COMING_ALL_THE_WAY_FROM_ATS", mIntent.getStringExtra("COMING_ALL_THE_WAY_FROM_ATS"));
                        intent.putExtra("INCREAMENTED_VALUE_FOR_ATS", increamentedValueForAts + 1);
                        intent.putExtra("GET_SOURCEID_FOR_ATS", mIntent.getStringExtra("GET_SOURCEID"));
                        // finishing all activities just using this to check flag
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Animatoo.animateFade(ActivityVideoInterviewForAts.this);
                        finish();// todo charan


                    } else if (response.body().getData().getTotalQuestion().getQuestion().get(0).getQtype().equalsIgnoreCase("3")) {


                        Intent intent = new Intent(ActivityVideoInterviewForAts.this, ActivitySubjectiveInterview.class);
                        intent.putExtra("PRE_RECORDED_KIT_LIST_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion());
                        intent.putExtra("OPTIONS_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions());
                        intent.putExtra("TOTAL_QUESTIONS_FOR_ATS", response.body().getData().getTotalQustion());
                        intent.putExtra("INVITATIONID_FOR_ATS", response.body().getData().getInvitationId());
                        intent.putExtra("KITID_FOR_ATS", mIntent.getIntExtra("KITID_FOR_ATS", 0));
                        intent.putExtra("INTERVIEWID_FOR_ATS", mIntent.getIntExtra("INTERVIEWID_FOR_ATS", 0));
                        intent.putExtra("CATCHER_ID_FOR_ATS", mIntent.getIntExtra("CATCHER_ID_FOR_ATS", 0));
                        intent.putExtra("COMING_ALL_THE_WAY_FROM_ATS", mIntent.getStringExtra("COMING_ALL_THE_WAY_FROM_ATS"));
                        intent.putExtra("INCREAMENTED_VALUE_FOR_ATS", increamentedValueForAts + 1);
                        intent.putExtra("GET_SOURCEID_FOR_ATS", mIntent.getStringExtra("GET_SOURCEID"));
                        // finishing all activities just using this to check flag
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Animatoo.animateFade(ActivityVideoInterviewForAts.this);
                        finish();//todo charan

                    }


                } else {

                    Intent intent = new Intent(ActivityVideoInterviewForAts.this, ActivityFinished.class);
                    intent.putExtra("GET_SOURCEID_FOR_ATS", mIntent.getStringExtra("GET_SOURCEID"));
                    intent.putExtra("INTERVIEWID_FOR_ATS", mIntent.getIntExtra("INTERVIEWID_FOR_ATS", 0));
                    intent.putExtra("COMING_ALL_THE_WAY_FROM_ATS", COMINGALLTHEWAYFROMATS);
                    intent.putExtra("INVITATIONID_FOR_ATS", invitationIdForAts);
                    // finishing all activities just using this to check flag
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();


                }

            }

            @Override
            public void onFailure(Call<AtsTotalInteviewQuestion> call, Throwable t) {

                System.out.println("ActivityVideoInterviewForAts.onFailure " + t.getMessage());


            }
        });


    }

    private void eventlistner() {
        imageSkip.setOnClickListener(this);
    }

    private void dialogPublishAndContinueWithRetake() {

        LayoutInflater li = LayoutInflater.from(ActivityVideoInterviewForAts.this);
        View dialogView = li.inflate(R.layout.dialog_publish_and_continue_retake, null);

        findIdsForDialog(dialogView);

        alertDialogBuilder = new AlertDialog.Builder(ActivityVideoInterviewForAts.this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void findIdsForDialog(final View dialogView) {

        btnRetakeAvailable = dialogView.findViewById(R.id.btnRetakeAvailable);
        btnPublishAndContiAvailable = dialogView.findViewById(R.id.btnPublishAndContiAvailable);

        btnRetakeAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                loadingProgressForAts();
                ratekeForAts();


            }
        });

        btnPublishAndContiAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                loadingProgress();
                submitAnswerApiVideoForAts();


            }
        });

    }

    private void loadingProgressForAts() {

        mProgressDialog.setMessage("Loading..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        if (!isFinishing()) {
            mProgressDialog.show();
        }

    }

    private void ratekeForAts() {


        HttpModule.provideRepositoryService().retakeForAts(questionsAllForAts.get(0).getQtype(), invitationIdForAts, questionsAllForAts.get(0).getQues(), attempts).enqueue(new Callback<AtsRetake>() {
            @Override
            public void onResponse(Call<AtsRetake> call, Response<AtsRetake> response) {


                if (response.body() != null && response.body().getError() == 0) {


                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    attempts = attempts + 1;
                    timeConsumed = 0;

                    recording = false;


                    startRecordingAgain();

                    if (questionsAllForAts.get(0).getAttempts() < 0) {

                        tvAttemptsVideo.setText("Unlimited");

                    } else {

                        tvAttemptsVideo.setText("" + (questionsAllForAts.get(0).getAttempts() - 1));
                        questionsAllForAts.get(0).setAttempts(questionsAllForAts.get(0).getAttempts() - 1);
                    }


                } else {

                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<AtsRetake> call, Throwable t) {

                mProgressDialog.dismiss();
                System.out.println("ActivityVideoInterview.onFailure " + t.getMessage());


            }
        });


    }


    private void loadingProgress() {

        mProgressDialog.setMessage("Uploading..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        if (!isFinishing()) {
            mProgressDialog.show();
        }


    }

    private void startRecordingAgain() {

        imageSwitch.setVisibility(View.GONE);
        imageRecord.setVisibility(View.GONE);
        imageStop.setVisibility(View.VISIBLE);


        countDownTimerThinkTime.cancel();
        timerForDuration();

        if (recording) {
            // stop recording and release camera
            mediaRecorder.stop(); // stop the recording
            releaseMediaRecorder(); // release the MediaRecorder object
            Toast.makeText(ActivityVideoInterviewForAts.this, "Video captured!", Toast.LENGTH_LONG).show();

            recording = false;

        } else {

            if (!prepareMediaRecorder()) {
                Toast.makeText(ActivityVideoInterviewForAts.this, "Fail in prepareMediaRecorder()!\n - Ended -", Toast.LENGTH_LONG).show();
                finish();
            }
            // work on UiThread for better performance
            runOnUiThread(new Runnable() {
                public void run() {
                    // If there are stories, add them to the table

                    try {
                        mediaRecorder.start();
                    } catch (final Exception ex) {
                        // Log.i("---","Exception in thread");
                    }
                }
            });

            recording = true;
        }


    }

    private void videoAutomaticallyStartsRecording() {

        if (recording) {
            // stop recording and release camera
            mediaRecorder.stop(); // stop the recording
            releaseMediaRecorder(); // release the MediaRecorder object
            Toast.makeText(ActivityVideoInterviewForAts.this, "Video captured!", Toast.LENGTH_LONG).show();

            recording = false;
        } else {
            if (!prepareMediaRecorder()) {
                Toast.makeText(ActivityVideoInterviewForAts.this, "Fail in prepareMediaRecorder()!\n - Ended -", Toast.LENGTH_LONG).show();
                finish();
            }
            // work on UiThread for better performance
            runOnUiThread(new Runnable() {
                public void run() {
                    // If there are stories, add them to the table

                    try {
                        mediaRecorder.start();
                    } catch (final Exception ex) {
                        // Log.i("---","Exception in thread");
                    }
                }
            });

            recording = true;
        }

    }

    private void timerForDuration() {


        countDownTimerDuration = new CountDownTimer(l1DurationTimer, 1000) {

            public void onTick(long millisUntilFinished) {


                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;

//                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
//                tvDurationTimeVideo.setText("00:" + millisUntilFinished / 1000);
                tvDurationTimeVideo.setText(String.format("%02d:%02d", minutes, seconds));
                timeConsumed++;

                df = new DecimalFormat("#.##");
                df.setRoundingMode(RoundingMode.CEILING);
                System.out.println("ActivityVideoInterview.onTick " + df.format(timeConsumed / 60.0));
                durationOfQuestions--;
            }

            public void onFinish() {

                tvDurationTimeVideo.setText("No");
//                tvDurationTimeVideo.setTextColor(Color.parseColor("#2DBFD9"));

                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;


                if (countDownTimerDuration != null) {
                    countDownTimerDuration.cancel();
                }


                if (questionsAllForAts.get(0).getAttempts() < 0) {

                    dialogPublishAndContinueWithRetake();

                } else if (questionsAllForAts.get(0).getAttempts() > 0) {

                    dialogPublishAndContinueWithRetake();

                } else {

                    dialogPublishAndContinue();
                }

            }

        }.start();


    }

    private int findFrontFacingCamera() {
        int cameraId = -1;

        isFront = true;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {
        int cameraId = -1;

        isFront = false;
        // Search for the back facing camera
        // get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        // for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

    public void onResume() {
        super.onResume();
        if (!hasCamera(myContext)) {
            Toast toast = Toast.makeText(myContext, "Sorry, your phone does not have a camera!", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        if (mCamera == null) {
            // if the front facing camera does not exist
            if (findFrontFacingCamera() < 0) {
                Toast.makeText(this, "No front facing camera found.", Toast.LENGTH_LONG).show();

                imageSwitch.setVisibility(View.GONE);
            }
            mCamera = Camera.open(findBackFacingCamera());
            mPreview.refreshCamera(mCamera);
            mCamera.setDisplayOrientation(90); // ORIENTATION SET BY SHAHZEB IT WAS NOT
        }
    }

    private void initialize() {


        cameraPreview = findViewById(R.id.camera_preview);


        mPreview = new CameraPreview(myContext, mCamera);
        cameraPreview.addView(mPreview);


        imageRecord = findViewById(R.id.imageRecord);
        imageRecord.setOnClickListener(captrureListener);

        imageSwitch = findViewById(R.id.imageSwitch);
        imageSwitch.setOnClickListener(switchCameraListener);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) ==
                    PackageManager.PERMISSION_GRANTED) {
                // put your code for Version>=Marshmallow
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                    Toast.makeText(this,
                            "App required access to audio", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO
                }, 100);
            }

        } else {
            // put your code for Version < Marshmallow
        }


    }


    private void findingIdsHere() {

        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        tvTotalQuestionCountVideo = findViewById(R.id.tvTotalQuestionCountVideo);
        tvQuestionVideo = findViewById(R.id.tvQuestionVideo);
        tvDurationTimeVideo = findViewById(R.id.tvDurationTimeVideo);
        tvThinkTimeVideo = findViewById(R.id.tvThinkTimeVideo);
        tvAttemptsVideo = findViewById(R.id.tvAttemptsVideo);

        tvNumberOfQuestionVideo = findViewById(R.id.tvNumberOfQuestionVideo);

        imageSwitch = findViewById(R.id.imageSwitch);
        imageRecord = findViewById(R.id.imageRecord);
        imageStop = findViewById(R.id.imageStop);
        imageSkip = findViewById(R.id.imageSkip);

        btnSeeMore = findViewById(R.id.btnSeeMore);
        tvProgress = findViewById(R.id.tvProgress);


    }


    View.OnClickListener switchCameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // get the number of cameras
            if (!recording) {
                int camerasNumber = Camera.getNumberOfCameras();
                if (camerasNumber > 1) {
                    // release the old camera instance
                    // switch camera, from the front and the back and vice versa

                    releaseCamera();
                    chooseCamera();
                } else {
                    Toast toast = Toast.makeText(myContext, "Sorry, your phone has only one camera!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }
    };


    public void chooseCamera() {
        // if the camera preview is the front
        if (cameraFront) {
            int cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                // open the backFacingCamera
                // set a picture callback
                // refresh the preview

                mCamera = Camera.open(cameraId);
                // mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);

                mCamera.setDisplayOrientation(90); // ORIENTATION SET BY SHAHZEB IT WAS NOT
            }
        } else {
            int cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                // open the backFacingCamera
                // set a picture callback
                // refresh the preview

                mCamera = Camera.open(cameraId);
                // mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
                mCamera.setDisplayOrientation(90); // ORIENTATION SET BY SHAHZEB IT WAS NOT
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // when on Pause, release camera in order to be used from other
        // applications
        releaseCamera();
    }

    private boolean hasCamera(Context context) {
        // check if the device has camera
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }


    View.OnClickListener captrureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            imageSwitch.setVisibility(View.GONE);
            imageRecord.setVisibility(View.GONE);
            imageStop.setVisibility(View.VISIBLE);

            recordingCheckForSkip = true;

            countDownTimerThinkTime.cancel();
            timerForDuration();

            if (recording) {
                // stop recording and release camera
                mediaRecorder.stop(); // stop the recording
                releaseMediaRecorder(); // release the MediaRecorder object
                Toast.makeText(ActivityVideoInterviewForAts.this, "Video captured!", Toast.LENGTH_LONG).show();
                recording = false;

            } else {

                if (!prepareMediaRecorder()) {

                    Toast.makeText(ActivityVideoInterviewForAts.this, "Fail in prepareMediaRecorder()!\n - Ended -", Toast.LENGTH_LONG).show();
                    finish();
                }
                // work on UiThread for better performance
                runOnUiThread(new Runnable() {
                    public void run() {

                        // If there are stories, add them to the table

                        try {

                            mediaRecorder.start();

                        } catch (final Exception ex) {
                            // Log.i("---","Exception in thread");
                        }
                    }
                });

                recording = true;
            }
        }
    };


    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset(); // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            mCamera.lock(); // lock camera for later use
        }
    }


    private boolean prepareMediaRecorder() {

        mediaRecorder = new MediaRecorder();
        mCamera.unlock();

        mediaRecorder.setOrientationHint(isFront ? 270 : 90);
        mediaRecorder.setCamera(mCamera);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));

        mediaRecorder.setMaxDuration(600000); // Set max duration 60 sec.
//        mediaRecorder.setMaxFileSize(50000000); // Set max file size 50M
        mediaRecorder.setVideoSize(640, 480);
        file = new File(Environment.getExternalStorageDirectory(), "videocapture_example.mp4");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mediaRecorder.setOutputFile(file);
        } else {
            mediaRecorder.setOutputFile(file.getPath());
        }


        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;

    }


    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Application will not have audio on record", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.imageSkip:
                submitAnswerApiVideoForAtsSkip();
                break;
        }


    }

    private void submitAnswerApiVideoForAtsSkip() {


        if (recordingCheckForSkip) {

            if (countDownTimerThinkTime != null && countDownTimerDuration != null) {
                countDownTimerThinkTime.cancel();
                countDownTimerDuration.cancel();

                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;

                recordingCheckForSkip = false;

            }


        } else {

            if (countDownTimerThinkTime != null) {
                countDownTimerThinkTime.cancel();
            }

            if (mediaRecorder != null) {

                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;
            }

        }


        HttpModule.provideRepositoryService().videoForAtsForSkip(Integer.parseInt(questionsAllForAts.get(0).getQtype()), (float) 0.0, totalQuestionsForAts, "", questionsAllForAts.get(0).getAttempts(), getIntent().getIntExtra("CATCHER_ID_FOR_ATS", 0), questionsAllForAts.get(0).getQues(), interviewIdForAts).enqueue(new Callback<VideoSubmitAnsForAts>() {
            @Override
            public void onResponse(Call<VideoSubmitAnsForAts> call, Response<VideoSubmitAnsForAts> response) {


//                System.out.println("ActivityVideoInterviewForAts.onResponse " + Integer.parseInt(questionsAllForAts.get(0).getQtype() + " " + Float.valueOf(questionsAllForAts.get(0).getThinktime() + " " + totalQuestionsForAts + " " + questionsAllForAts.get(0).getAttempts() + " " + getIntent().getIntExtra("CATCHER_ID_FOR_ATS", 0) + " " + questionsAllForAts.get(0).getQues() + " " + interviewIdForAts)));


                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    callTotalInterviewQuestionsForAts();

                } else {

                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<VideoSubmitAnsForAts> call, Throwable t) {

                mProgressDialog.dismiss();
                System.out.println("ActivityVideoInterviewForAts.onFailure " + t.getMessage());

            }
        });


    }


    @Override
    public void onProgressUpdate(int percentage) {
        mProgressDialog.setMessage("Uploading.. " + percentage + "%");
        mProgressDialog.setProgress(percentage);
        tvProgress.setProgress(percentage);

    }

    @Override
    public void onError() {

        tvProgress.setVisibility(View.GONE);
    }

    @Override
    public void onFinish() {
        tvProgress.setVisibility(View.GONE);
//        Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uploadStart() {

        tvProgress.setVisibility(View.VISIBLE);

        mProgressDialog.setMessage("Uploading.. " + 0 + "%");
        //textView.setText("0%");
        tvProgress.setProgress(0);

    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
