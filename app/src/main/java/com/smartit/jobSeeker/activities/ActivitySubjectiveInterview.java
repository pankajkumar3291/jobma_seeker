package com.smartit.jobSeeker.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.preRecordedInterviewKit.PreRecordedInterviewKit;
import com.smartit.jobSeeker.apiResponse.preRecordedInterviewKit.Question;
import com.smartit.jobSeeker.apiResponse.submitInterviewAnswer.InterviewAnswer;
import com.smartit.jobSeeker.atsApiResponses.ats_total_question.AtsTotalInteviewQuestion;
import com.smartit.jobSeeker.atsApiResponses.submit_answer_essay_for_ats.EssaySubmitAnsForAts;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivitySubjectiveInterview extends AppCompatActivity implements View.OnClickListener {


    private TextView tvQuestion, tvNumberOfQuestion, tvTotalQuestionCount, tvTimeDuration, tvCharactersLimit;
    private EditText edWriteAnswer;
    private ProgressBar progress;
    private Button btnSkipNext, btnPublishAndContinue;
    private ImageView backarr;
    private String invitationId, postId;
    private ArrayList<Question> questionsAll;

    private Float time;
    private Integer increamentedQ;
    private NoInternetDialog noInternetDialog;

    private String appliedId, sourceId;
    private double timeConsumed = 0;


    // ATS
    private String comingAllTheWayFromAts;

    private ArrayList<com.smartit.jobSeeker.atsApiResponses.ats_total_question.Question> essayQuestionsAllForAts;
    private Integer essayTotalQuestionsForAts;
    private String essayInvitationIdForAts;
    private Integer essayKitidForAts;
    private Integer essayInterviewIdForAts;
    private Integer essayIncreamentedQ;
    private Integer essayCatcherId;
    private String essaySourceIdForAts;

    ArrayList<String> essayOptionsAll;

    private Float essayTime;


    private String invitationIdForAts;

    private float f1, f1ForAts;
    private long l1, l1ForAts;

    private ProgressDialog mProgressDialog;

    private CountDownTimer countDownTimer;
    private TextView btnSeeMore;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private TextView tvSuccessMessage;
    private Button buttonOk;


    private Intent mIntent;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        getData(intent);


    }

    private void getData(Intent intent) {

        this.mIntent = intent;
        checkEssay();

    }

    private void checkEssay() {
        comingAllTheWayFromAts = mIntent.getStringExtra("COMING_ALL_THE_WAY_FROM_ATS");

        if (mIntent.hasExtra("COMING_ALL_THE_WAY_FROM_ATS") && comingAllTheWayFromAts != null) {

            essaySeetingValuesForAts();


        } else {


            questionsAll = (ArrayList<Question>) mIntent.getSerializableExtra("pre-recorded_kit_list");
            increamentedQ = mIntent.getIntExtra("increamentedValue", 0);

            tvTotalQuestionCount.setText("/" + mIntent.getStringExtra("total_questions"));


            tvQuestion.setText(questionsAll.get(0).getQuesTitle());

            tvQuestion.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    if (tvQuestion.getLineCount() > 3) {

                        btnSeeMore.setVisibility(View.VISIBLE);

                    }

                }
            });

            btnSeeMore.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    dialogForLongQuestion(questionsAll.get(0).getQuesTitle());


                }
            });


            Integer totalQuestion = Integer.valueOf(mIntent.getStringExtra("total_questions"));


            invitationId = mIntent.getStringExtra("invitationId");
            postId = mIntent.getStringExtra("postId");

            tvNumberOfQuestion.setText("" + increamentedQ);


            if (questionsAll.get(0).getOptional().equalsIgnoreCase("1")) {

                btnSkipNext.setVisibility(View.VISIBLE);
            }


            Float a = Float.valueOf(((increamentedQ * 100) / totalQuestion));
            int xx = Math.round(a);
            progress.setProgress(xx);


            time = Float.valueOf(questionsAll.get(0).getThinktime());

            f1 = time * 60000f;
            l1 = (long) f1;

            if (questionsAll.get(0).getThinktime().equalsIgnoreCase("0.0")) {

                l1 = 120 * 60000;
            }


            countDownTimer = new CountDownTimer(l1, 1000) {

                public void onTick(long millisUntilFinished) {

                    long minutes = (millisUntilFinished / 1000) / 60;
                    long seconds = (millisUntilFinished / 1000) % 60;

//                  tvTimeDuration.setText("00:" + millisUntilFinished / 1000);
//                  tvTimeDuration.setText(String.format("%d:%d", minutes, seconds));
//                    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                    if (questionsAll.get(0).getThinktime().equalsIgnoreCase("0.0")) {
                        tvTimeDuration.setText("Unlimited");
                    } else {
                        tvTimeDuration.setText(String.format("%02d:%02d", minutes, seconds));
                    }
                    timeConsumed++;
                    time--;
                }

                public void onFinish() {

                    tvTimeDuration.setText("No");
                    tvTimeDuration.setTextColor(Color.parseColor("#2DBFD9"));

//                    loadingProgress();
                    callTheSubmitAnswerApi();

                }

            }.start();

        }


    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjective_interview);

        this.mIntent = getIntent();


//        getData(mIntent);

        findingIdsHere();
        eventListenerGoesHere();
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        comingAllTheWayFromAts = mIntent.getStringExtra("COMING_ALL_THE_WAY_FROM_ATS");

        if (mIntent.hasExtra("COMING_ALL_THE_WAY_FROM_ATS") && comingAllTheWayFromAts != null) {

            essaySeetingValuesForAts();


        } else {


            questionsAll = (ArrayList<Question>) mIntent.getSerializableExtra("pre-recorded_kit_list");
            increamentedQ = mIntent.getIntExtra("increamentedValue", 0);

            tvTotalQuestionCount.setText("/" + mIntent.getStringExtra("total_questions"));


            tvQuestion.setText(questionsAll.get(0).getQuesTitle());

            tvQuestion.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    if (tvQuestion.getLineCount() > 3) {

                        btnSeeMore.setVisibility(View.VISIBLE);

                    }

                }
            });

            btnSeeMore.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    dialogForLongQuestion(questionsAll.get(0).getQuesTitle());


                }
            });


            Integer totalQuestion = Integer.valueOf(mIntent.getStringExtra("total_questions"));


            invitationId = mIntent.getStringExtra("invitationId");
            postId = mIntent.getStringExtra("postId");

            tvNumberOfQuestion.setText("" + increamentedQ);


            if (questionsAll.get(0).getOptional().equalsIgnoreCase("1")) {

                btnSkipNext.setVisibility(View.VISIBLE);
            }


            Float a = Float.valueOf(((increamentedQ * 100) / totalQuestion));
            int xx = Math.round(a);
            progress.setProgress(xx);


            time = Float.valueOf(questionsAll.get(0).getThinktime());

            f1 = time * 60000f;
            l1 = (long) f1;

            if (questionsAll.get(0).getThinktime().equalsIgnoreCase("0.0")) {

                l1 = 120 * 60000;
            }


            countDownTimer = new CountDownTimer(l1, 1000) {

                public void onTick(long millisUntilFinished) {

                    long minutes = (millisUntilFinished / 1000) / 60;
                    long seconds = (millisUntilFinished / 1000) % 60;

//                  tvTimeDuration.setText("00:" + millisUntilFinished / 1000);
//                  tvTimeDuration.setText(String.format("%d:%d", minutes, seconds));
//                    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                    if (questionsAll.get(0).getThinktime().equalsIgnoreCase("0.0")) {
                        tvTimeDuration.setText("Unlimited");
                    } else {
                        tvTimeDuration.setText(String.format("%02d:%02d", minutes, seconds));
                    }
                    timeConsumed++;
                    time--;
                }

                public void onFinish() {

                    tvTimeDuration.setText("No");
                    tvTimeDuration.setTextColor(Color.parseColor("#2DBFD9"));

//                    loadingProgress();
                    callTheSubmitAnswerApi();

                }

            }.start();


        }


    }

    private void dialogForLongQuestion(String quesTitle) {


        LayoutInflater li = LayoutInflater.from(ActivitySubjectiveInterview.this);
        View dialogView = li.inflate(R.layout.dialog_for_long_question, null);

        findTheIdsHere(dialogView, quesTitle);

        alertDialogBuilder = new AlertDialog.Builder(ActivitySubjectiveInterview.this);
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

    private void loadingProgress() {

        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

    }

    private void essaySeetingValuesForAts() {


        essayQuestionsAllForAts = (ArrayList<com.smartit.jobSeeker.atsApiResponses.ats_total_question.Question>) mIntent.getSerializableExtra("PRE_RECORDED_KIT_LIST_FOR_ATS");
        essayTotalQuestionsForAts = mIntent.getIntExtra("TOTAL_QUESTIONS_FOR_ATS", 0);
        essayOptionsAll = (ArrayList<String>) mIntent.getSerializableExtra("OPTIONS_FOR_ATS");
        essayInvitationIdForAts = mIntent.getStringExtra("INVITATIONID_FOR_ATS");
        essayKitidForAts = mIntent.getIntExtra("KITID_FOR_ATS", 0);
        essayInterviewIdForAts = mIntent.getIntExtra("INTERVIEWID_FOR_ATS", 0);
        essayIncreamentedQ = mIntent.getIntExtra("INCREAMENTED_VALUE_FOR_ATS", 0);
        essayCatcherId = mIntent.getIntExtra("CATCHER_ID_FOR_ATS", 0);
        essaySourceIdForAts = mIntent.getStringExtra("GET_SOURCEID");


        tvQuestion.setText(essayQuestionsAllForAts.get(0).getQuesTitle());
        tvTotalQuestionCount.setText("/" + mIntent.getIntExtra("TOTAL_QUESTIONS_FOR_ATS", 0));
        tvNumberOfQuestion.setText("" + essayIncreamentedQ);

        Integer essaytotalQuestion = mIntent.getIntExtra("TOTAL_QUESTIONS_FOR_ATS", 0);


        tvQuestion.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (tvQuestion.getLineCount() > 3) {

                    btnSeeMore.setVisibility(View.VISIBLE);

                }

            }
        });

        btnSeeMore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialogForLongQuestion(essayQuestionsAllForAts.get(0).getQuesTitle());


            }
        });


        if (essayQuestionsAllForAts.get(0).getOptional().equalsIgnoreCase("1")) {

            btnSkipNext.setVisibility(View.VISIBLE);
        }

        Float a = Float.valueOf(((essayIncreamentedQ * 100) / essaytotalQuestion));
        int xx = Math.round(a);
        progress.setProgress(xx);

        essayTime = Float.valueOf(essayQuestionsAllForAts.get(0).getThinktime());

        f1ForAts = essayTime * 60000f;
        l1ForAts = (long) f1ForAts;

        if (essayQuestionsAllForAts.get(0).getThinktime().equalsIgnoreCase("0.0")) {

            l1ForAts = 120 * 60000;
        }


        countDownTimer = new CountDownTimer(l1ForAts, 1000) {

            public void onTick(long millisUntilFinished) {

                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;

//                tvTimeDuration.setText("" + millisUntilFinished / 1000);
//                tvTimeDuration.setText(String.format("%d:%d", minut1es, seconds));

//                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                if (essayQuestionsAllForAts.get(0).getThinktime().equalsIgnoreCase("0.0")) {

                    tvTimeDuration.setText("Unlimited");
                } else {
                    tvTimeDuration.setText(String.format("%02d:%02d", minutes, seconds));
                }
                essayTime--;
            }

            public void onFinish() {

                tvTimeDuration.setText("No");
                tvTimeDuration.setTextColor(Color.parseColor("#2DBFD9"));
//                loadingProgress();
                essaySubmitAnswerForAts();

            }

        }.start();


    }

    private void essaySubmitAnswerForAts() {


        HttpModule.provideRepositoryService().essayForAts(essayQuestionsAllForAts.get(0).getQtype(), String.valueOf(essayInterviewIdForAts), edWriteAnswer.getText().toString(), essayQuestionsAllForAts.get(0).getQues(), essayQuestionsAllForAts.get(0).getThinktime(), essayCatcherId, essayTotalQuestionsForAts).enqueue(new Callback<EssaySubmitAnsForAts>() {
            @Override
            public void onResponse(Call<EssaySubmitAnsForAts> call, Response<EssaySubmitAnsForAts> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();
                    totalInterviewQuestionsForAts();


                } else {

                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<EssaySubmitAnsForAts> call, Throwable t) {
                mProgressDialog.dismiss();
                System.out.println("ActivitySubjectiveInterview.onFailure" + t.getMessage());

            }
        });


    }

    private void totalInterviewQuestionsForAts() {


        HttpModule.provideRepositoryService().totalInterviewQuestionForAts(essayKitidForAts, essayInterviewIdForAts).enqueue(new Callback<AtsTotalInteviewQuestion>() {
            @Override
            public void onResponse(Call<AtsTotalInteviewQuestion> call, Response<AtsTotalInteviewQuestion> response) {


                if (response.body() != null && response.body().getError() == 0) {


                    invitationIdForAts = response.body().getData().getInvitationId();


                    if (response.body().getData().getTotalQuestion().getQuestion().get(0).getQtype().equalsIgnoreCase("1")) {


                        if (response.body().getData().getTotalQuestion().getQuestion().get(0).getFile_status() == 1) {

                            Intent intent = new Intent(ActivitySubjectiveInterview.this, ActivityVideoQuestionEnhanceAts.class);
                            intent.putExtra("FILE_PATH", response.body().getData().getTotalQuestion().getQuestion().get(0).getFile_name());
                            intent.putExtra("FILE_PATH_HLS", response.body().getData().getTotalQuestion().getQuestion().get(0).getHls());
                            intent.putExtra("FILE_THUMB", response.body().getData().getTotalQuestion().getQuestion().get(0).getFile_thumb());
                            intent.putExtra("INCREAMENTED_VALUE", response.body().getData().getTotalQustion() - response.body().getData().getRemainQustion() + 1);
                            intent.putExtra("TOTAL_QUESTIONS_FOR_ATS", response.body().getData().getTotalQustion());
                            intent.putExtra("QUESTION_TITLE", response.body().getData().getTotalQuestion().getQuestion().get(0).getQuesTitle());
                            intent.putExtra("KITID_FOR_ATS", mIntent.getIntExtra("KITID_FOR_ATS", 0));
                            intent.putExtra("INTERVIEWID_FOR_ATS", mIntent.getIntExtra("INTERVIEWID_FOR_ATS", 0));
                            intent.putExtra("CATCHER_ID_FOR_ATS", mIntent.getIntExtra("CATCHER_ID_FOR_ATS", 0));
                            intent.putExtra("GET_SOURCEID", mIntent.getStringExtra("GET_SOURCEID"));
                            intent.putExtra("COMING_ALL_THE_WAY_FROM_ATS", mIntent.getStringExtra("COMING_ALL_THE_WAY_FROM_ATS"));
                            startActivity(intent);


                        } else {

                            Intent intent = new Intent(ActivitySubjectiveInterview.this, ActivityVideoInterview.class);
                            intent.putExtra("PRE_RECORDED_KIT_LIST_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion());
                            intent.putExtra("OPTIONS_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions());
                            intent.putExtra("TOTAL_QUESTIONS_FOR_ATS", response.body().getData().getTotalQustion().toString());
                            intent.putExtra("INVITATIONID_FOR_ATS", response.body().getData().getInvitationId());
                            intent.putExtra("KITID_FOR_ATS", mIntent.getIntExtra("KITID_FOR_ATS", 0));
                            intent.putExtra("INTERVIEWID_FOR_ATS", mIntent.getIntExtra("INTERVIEWID_FOR_ATS", 0));
                            intent.putExtra("CATCHER_ID_FOR_ATS", mIntent.getIntExtra("CATCHER_ID_FOR_ATS", 0));
                            intent.putExtra("GET_SOURCEID_FOR_ATS", mIntent.getStringExtra("GET_SOURCEID"));
                            intent.putExtra("COMING_ALL_THE_WAY_FROM_ATS", mIntent.getStringExtra("COMING_ALL_THE_WAY_FROM_ATS"));
                            intent.putExtra("INCREAMENTED_VALUE_FOR_ATS", essayIncreamentedQ + 1);
                            startActivity(intent);
                            Animatoo.animateFade(ActivitySubjectiveInterview.this);  //fire the zoom animation animateFade //animateSwipeRight
                        }


                    } else if (response.body().getData().getTotalQuestion().getQuestion().get(0).getQtype().equalsIgnoreCase("2")) {


                        Intent intent = new Intent(ActivitySubjectiveInterview.this, ActivityMultipleChoiceQuestion.class);
                        intent.putExtra("PRE_RECORDED_KIT_LIST_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion());
                        intent.putExtra("OPTIONS_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions());
                        intent.putExtra("TOTAL_QUESTIONS_FOR_ATS", response.body().getData().getTotalQustion());
                        intent.putExtra("INVITATIONID_FOR_ATS", response.body().getData().getInvitationId());
                        intent.putExtra("KITID_FOR_ATS", mIntent.getIntExtra("KITID_FOR_ATS", 0));
                        intent.putExtra("INTERVIEWID_FOR_ATS", mIntent.getIntExtra("INTERVIEWID_FOR_ATS", 0));
                        intent.putExtra("CATCHER_ID_FOR_ATS", mIntent.getIntExtra("CATCHER_ID_FOR_ATS", 0));
                        intent.putExtra("GET_SOURCEID_FOR_ATS", mIntent.getStringExtra("GET_SOURCEID"));
                        intent.putExtra("INCREAMENTED_VALUE_FOR_ATS", essayIncreamentedQ + 1);
                        intent.putExtra("COMING_ALL_THE_WAY_FROM_ATS", mIntent.getStringExtra("COMING_ALL_THE_WAY_FROM_ATS"));
                        // finishing all activities just using this to check flag
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Animatoo.animateFade(ActivitySubjectiveInterview.this);
                        //fire the zoom animation animateFade //animateSwipeRight


                    } else if (response.body().getData().getTotalQuestion().getQuestion().get(0).getQtype().equalsIgnoreCase("3")) {

                        finish();
                        startActivity(mIntent.
                                putExtra("PRE_RECORDED_KIT_LIST_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion()).
                                putExtra("OPTIONS_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions()).
                                putExtra("TOTAL_QUESTIONS_FOR_ATS", response.body().getData().getTotalQustion()).
                                putExtra("INVITATIONID_FOR_ATS", response.body().getData().getInvitationId()).
                                putExtra("KITID_FOR_ATS", mIntent.getIntExtra("KITID_FOR_ATS", 0)).
                                putExtra("INTERVIEWID_FOR_ATS", mIntent.getIntExtra("INTERVIEWID_FOR_ATS", 0)).
                                putExtra("CATCHER_ID_FOR_ATS", mIntent.getIntExtra("CATCHER_ID_FOR_ATS", 0)).
                                putExtra("COMING_ALL_THE_WAY_FROM_ATS", mIntent.getStringExtra("COMING_ALL_THE_WAY_FROM_ATS")).
                                putExtra("GET_SOURCEID_FOR_ATS", mIntent.getStringExtra("GET_SOURCEID")).
                                // finishing all activities just using this to check flag
                                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
                                        putExtra("INCREAMENTED_VALUE_FOR_ATS", essayIncreamentedQ + 1));

                        Animatoo.animateFade(ActivitySubjectiveInterview.this);  //fire the zoom animation animateFade //animateSwipeRight


                    }


                } else {


                    Intent intent = new Intent(ActivitySubjectiveInterview.this, ActivityFinished.class);
                    intent.putExtra("GET_SOURCEID_FOR_ATS", mIntent.getStringExtra("GET_SOURCEID"));
                    intent.putExtra("INTERVIEWID_FOR_ATS", mIntent.getIntExtra("INTERVIEWID_FOR_ATS", 0));
                    intent.putExtra("COMING_ALL_THE_WAY_FROM_ATS", mIntent.getStringExtra("COMING_ALL_THE_WAY_FROM_ATS"));
                    intent.putExtra("INVITATIONID_FOR_ATS", invitationIdForAts);
                    // finishing all activities just using this to check flag
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();


                }


            }

            @Override
            public void onFailure(Call<AtsTotalInteviewQuestion> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private void eventListenerGoesHere() {

        btnSkipNext.setOnClickListener(this);
        btnPublishAndContinue.setOnClickListener(this);

        backarr.setOnClickListener(this);


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


    private void findingIdsHere() {

        tvQuestion = findViewById(R.id.tvQuestion);
        tvNumberOfQuestion = findViewById(R.id.tvNumberOfQuestion);
        tvTotalQuestionCount = findViewById(R.id.tvTotalQuestionCount);
        tvTimeDuration = findViewById(R.id.tvTimeDuration);

        btnSeeMore = findViewById(R.id.btnSeeMore);

        tvCharactersLimit = findViewById(R.id.tvCharactersLimit);
        tvCharactersLimit.setTypeface(tvCharactersLimit.getTypeface(), Typeface.ITALIC);

        edWriteAnswer = findViewById(R.id.edWriteAnswer);
        progress = findViewById(R.id.progress);

        btnSkipNext = findViewById(R.id.btnSkipNext);
        btnPublishAndContinue = findViewById(R.id.btnPublishAndContinue);

        backarr = findViewById(R.id.backarr);


        edWriteAnswer.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

                btnPublishAndContinue.setVisibility(View.VISIBLE);

                if (after == 0) {

                    btnPublishAndContinue.setVisibility(View.GONE);
                }

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {


//                int length = 10;
//
//
//                if (count > 10) {
//
//                    TastyToast.makeText(getApplicationContext(), "Crossing the limit", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
//                    tvCharactersLimit.setTextColor(Color.parseColor("#FF0000"));
//                    InputFilter[] FilterArray = new InputFilter[1];
//                    FilterArray[0] = new InputFilter.LengthFilter(length);
//                    edWriteAnswer.setFilters(FilterArray);
//
//                } else {
//                    tvCharactersLimit.setTextColor(Color.parseColor("#000000"));
//                }
            }
        });


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnSkipNext:


                if (mIntent.hasExtra("COMING_ALL_THE_WAY_FROM_ATS") && comingAllTheWayFromAts != null) {

                    loadingProgress();
                    essaySubmitAnswerForAts();

                } else {

                    loadingProgress();
                    callTheSubmitAnswerApi();
                }

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }


                break;


            case R.id.btnPublishAndContinue:


                if (mIntent.hasExtra("COMING_ALL_THE_WAY_FROM_ATS") && comingAllTheWayFromAts != null) {

                    loadingProgress();
                    essaySubmitAnswerForAts();

                } else {
                    loadingProgress();
                    callTheSubmitAnswerApi();
                }
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }

                break;


            case R.id.backarr:
                finish();
                break;
        }
    }

    private void callTheSubmitAnswerApi() {


        HttpModule.provideRepositoryService().submitInterviewAnswerForJobSeeker((String) Hawk.get("GET_TOKEN"), questionsAll.get(0).getQtype(), questionsAll.get(0).getQues(), (float) (timeConsumed / 60.0), edWriteAnswer.getText().toString(), 0, invitationId, postId).enqueue(new Callback<InterviewAnswer>() {
            @Override
            public void onResponse(Call<InterviewAnswer> call, Response<InterviewAnswer> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();
                    Toast.makeText(ActivitySubjectiveInterview.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                    appliedId = response.body().getData().getAppliedDetail().getAppliedId();
                    sourceId = response.body().getData().getAppliedDetail().getSource();


                    callTheApplyInterviewApiFromHere();


                } else {

                    mProgressDialog.dismiss();
//                    Toast.makeText(ActivitySubjectiveInterview.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<InterviewAnswer> call, Throwable t) {

                mProgressDialog.dismiss();
                Toast.makeText(ActivitySubjectiveInterview.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }

    private void callTheApplyInterviewApiFromHere() {


        HttpModule.provideRepositoryService().applyInterviewNowUsedForJobSeeker((String) Hawk.get("GET_TOKEN"), mIntent.getIntExtra("interview_job_id", 0)).enqueue(new Callback<PreRecordedInterviewKit>() {
            @Override
            public void onResponse(Call<PreRecordedInterviewKit> call, Response<PreRecordedInterviewKit> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();

                    // VIDEO CASE
                    if (response.body().getData().getTotalQuestion().getQuestion().get(0).getQtype().equalsIgnoreCase("1")) {


                        if (response.body().getData().getTotalQuestion().getQuestion().get(0).getFile_status() == 1) {

                            Intent intent = new Intent(ActivitySubjectiveInterview.this, ActivityVideoQuestionEnhance.class);
                            intent.putExtra("File_Path", response.body().getData().getTotalQuestion().getQuestion().get(0).getFile_name());
                            intent.putExtra("File_Path_Hls", response.body().getData().getTotalQuestion().getQuestion().get(0).getHls());
                            intent.putExtra("File_Thumb", response.body().getData().getTotalQuestion().getQuestion().get(0).getFile_thumb());
                            intent.putExtra("increamentedValue", increamentedQ + 1);
                            intent.putExtra("questionTitle", response.body().getData().getTotalQuestion().getQuestion().get(0).getQuesTitle());
                            intent.putExtra("total_questions", response.body().getData().getTotalQustion().toString());
                            startActivity(intent);


                        } else {


                            Intent intent = new Intent(ActivitySubjectiveInterview.this, ActivityVideoInterview.class);
                            intent.putExtra("pre-recorded_kit_list", (Serializable) response.body().getData().getTotalQuestion().getQuestion());
                            intent.putExtra("options", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions());
                            intent.putExtra("total_questions", response.body().getData().getTotalQustion().toString());
                            intent.putExtra("invitationId", response.body().getData().getInvitationId());
                            intent.putExtra("interview_job_id", mIntent.getIntExtra("interview_job_id", 0));
                            intent.putExtra("postId", response.body().getData().getPostId());
                            intent.putExtra("increamentedValue", increamentedQ + 1);
                            startActivity(intent);
                            finish();
                        }


                        // MULTIPLE CHOICE OR ESAAY
                    } else if (response.body().getData().getTotalQuestion().getQuestion().get(0).getQtype().equalsIgnoreCase("2")) {

                        Intent intent = new Intent(ActivitySubjectiveInterview.this, ActivityMultipleChoiceQuestion.class);
                        intent.putExtra("pre-recorded_kit_list", (Serializable) response.body().getData().getTotalQuestion().getQuestion());
                        intent.putExtra("options", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions());
                        intent.putExtra("total_questions", response.body().getData().getTotalQustion().toString());
                        intent.putExtra("invitationId", response.body().getData().getInvitationId());
                        intent.putExtra("postId", response.body().getData().getPostId());
                        intent.putExtra("interview_job_id", mIntent.getIntExtra("interview_job_id", 0));
                        intent.putExtra("increamentedValue", increamentedQ + 1);
                        startActivity(intent);
                        finish();

                        tvQuestion.setText(response.body().getData().getTotalQuestion().getQuestion().get(0).getQuesTitle());

                    } else if (response.body().getData().getTotalQuestion().getQuestion().get(0).getQtype().equalsIgnoreCase("3")) {

                        finish();
                        startActivity(mIntent
                                .putExtra("pre-recorded_kit_list", (Serializable) response.body().getData().getTotalQuestion().getQuestion()).
                                        putExtra("options", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions()).
                                        putExtra("total_questions", response.body().getData().getTotalQustion().toString()).
                                        putExtra("invitationId", response.body().getData().getInvitationId()).
                                        putExtra("postId", response.body().getData().getPostId()).
                                        putExtra("interview_job_id", mIntent.getIntExtra("interview_job_id", 0)).
                                        putExtra("increamentedValue", increamentedQ + 1));
                        Animatoo.animateFade(ActivitySubjectiveInterview.this);


                    }


                } else {


                    mProgressDialog.dismiss();
                    Intent intent = new Intent(ActivitySubjectiveInterview.this, ActivityFinished.class);
                    intent.putExtra("PR", mIntent.getStringExtra("P_Recorded"));
                    intent.putExtra("MCQ", mIntent.getStringExtra("Multiple_Choice"));
                    intent.putExtra("SUB", mIntent.getStringExtra("Subjective"));

                    intent.putExtra("appliedId", appliedId);
                    intent.putExtra("sourceId", sourceId);

                    startActivity(intent);
                    finish();


                }

            }

            @Override
            public void onFailure(Call<PreRecordedInterviewKit> call, Throwable t) {

                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }


}
