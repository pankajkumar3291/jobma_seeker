package com.smartit.jobSeeker.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.adapters.MultipleChoiceQuestionAdapter;
import com.smartit.jobSeeker.apiResponse.preRecordedInterviewKit.PreRecordedInterviewKit;
import com.smartit.jobSeeker.apiResponse.preRecordedInterviewKit.Question;
import com.smartit.jobSeeker.apiResponse.submitInterviewAnswer.InterviewAnswer;
import com.smartit.jobSeeker.atsApiResponses.ats_total_question.AtsTotalInteviewQuestion;
import com.smartit.jobSeeker.atsApiResponses.submit_answer_mcq_for_ats.MCQSubmitAnsForAts;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.io.Serializable;
import java.util.ArrayList;

import am.appwise.components.ni.NoInternetDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityMultipleChoiceQuestion extends AppCompatActivity implements View.OnClickListener {


    private TextView tvQuestionMCQ, tvNumberOfQuestionMCQ, tvTotalQuestionCountMCQ, tvTimeDurationMCQ;
    private RecyclerView recylerRadioButtonMCQ;
    private ProgressBar progressMCQ;
    private Button btnSkipNextMCQ, btnPublishAndConti;
    private ImageView backarr;
    private ArrayList<Question> questionsAll;
    ArrayList<String> optionsAll;

    private MultipleChoiceQuestionAdapter multipleChoiceQuestionAdapter;
    private String invitationId, postId;

    private Float time;
    private Integer increamentedQ;
    private NoInternetDialog noInternetDialog;

    private String appliedId, sourceId;
    private int mSelectedItemPosition;


    private float f1;
    private long l1;

    private CountDownTimer countDownTimer;

    private double timeConsumed = 0;


    // ATS
    private String comingAllTheWayFromAts;

    private ArrayList<com.smartit.jobSeeker.atsApiResponses.ats_total_question.Question> mcqQuestionsAllForAts;
    private Integer mcqTotalQuestionsForAts;
    private String mcqInvitationIdForAts;
    private Integer mcqKitidForAts;
    private Integer mcqInterviewIdForAts;
    private Integer mcqIncreamentedQ;
    private Integer mcqCatcherId;

    ArrayList<String> mcqOptionsAll;

    private Float mcqTime;
    private ProgressDialog mProgressDialog;
    private float f1ForAts;
    private long l1ForAts;
    private TextView btnSeeMore;


    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private TextView tvSuccessMessage;
    private Button buttonOk;
    private Intent mIntent;

    private TextView tvAppTitle;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getData(intent);

    }

    private void getData(Intent intent) {

        this.mIntent = intent;

        checkMCQ();
//        mcqSeetingValuesForAts();

    }

    private void checkMCQ() {


        comingAllTheWayFromAts = mIntent.getStringExtra("COMING_ALL_THE_WAY_FROM_ATS");


        if (mIntent.hasExtra("COMING_ALL_THE_WAY_FROM_ATS") && comingAllTheWayFromAts != null) {


//            code for ats flow
            mcqSeetingValuesForAts();

        } else {

//            code for normal flow

            questionsAll = (ArrayList<Question>) mIntent.getSerializableExtra("pre-recorded_kit_list");
            optionsAll = (ArrayList<String>) mIntent.getSerializableExtra("options");

            tvQuestionMCQ.setText(questionsAll.get(0).getQuesTitle());

            tvQuestionMCQ.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    if (tvQuestionMCQ.getLineCount() > 3) {
                        btnSeeMore.setVisibility(View.VISIBLE);

                    }

                }
            });

            btnSeeMore.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    dialogForLongQuestion(questionsAll.get(0).getQuesTitle());


                }
            });

            System.out.println("ActivityMultipleChoiceQuestion.onCreate " + questionsAll.get(0).getQuesTitle());


            tvTotalQuestionCountMCQ.setText("/" + mIntent.getStringExtra("total_questions"));

            Integer totalQuestion = Integer.valueOf(mIntent.getStringExtra("total_questions"));

            increamentedQ = mIntent.getIntExtra("increamentedValue", 0);
            tvNumberOfQuestionMCQ.setText("" + increamentedQ);

            if (questionsAll.get(0).getOptional().equalsIgnoreCase("1")) {

                btnSkipNextMCQ.setVisibility(View.VISIBLE);
            }


            Float a = Float.valueOf(((increamentedQ * 100) / totalQuestion));

            int xx = Math.round(a);
            progressMCQ.setProgress(xx);

            time = Float.valueOf(questionsAll.get(0).getThinktime());

            if (questionsAll.get(0).getThinktime().equalsIgnoreCase("0.0")) {

                l1 = 120 * 60000;
            }

            f1 = time * 60000f;
            l1 = (long) f1;


            countDownTimer = new CountDownTimer(l1, 1000) {

                public void onTick(long millisUntilFinished) {

                    long minutes = (millisUntilFinished / 1000) / 60;
                    long seconds = (millisUntilFinished / 1000) % 60;

                    tvTimeDurationMCQ.setText(String.format("%02d:%02d", minutes, seconds));
                    timeConsumed++;
                    time--;
                }

                public void onFinish() {

                    tvTimeDurationMCQ.setText("No");
//                    loadingProgress();
                    submitApiSkip();

                }

            }.start();


            tvTimeDurationMCQ.setText(questionsAll.get(0).getThinktime());

            invitationId = mIntent.getStringExtra("invitationId");
            postId = mIntent.getStringExtra("postId");


            initAdapterHere(optionsAll);


            // Register to receive messages.
            // We are registering an observer (mMessageReceiver) to receive Intents
            // with actions named "custom-message".
            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("custom-message"));


        }


    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice_questions);


        this.mIntent = getIntent();

        findingIdsHere();
//        getData(mIntent);
        eventListnerGoesHere();

        noInternetDialog = new NoInternetDialog.Builder(this).build();
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        comingAllTheWayFromAts = mIntent.getStringExtra("COMING_ALL_THE_WAY_FROM_ATS");


        if (mIntent.hasExtra("COMING_ALL_THE_WAY_FROM_ATS") && comingAllTheWayFromAts != null) {


//            code for ats flow
            mcqSeetingValuesForAts();

        } else {

//            code for normal flow

            questionsAll = (ArrayList<Question>) mIntent.getSerializableExtra("pre-recorded_kit_list");
            optionsAll = (ArrayList<String>) mIntent.getSerializableExtra("options");

            tvQuestionMCQ.setText(questionsAll.get(0).getQuesTitle());

            tvQuestionMCQ.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    if (tvQuestionMCQ.getLineCount() > 3) {
                        btnSeeMore.setVisibility(View.VISIBLE);

                    }

                }
            });

            btnSeeMore.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    dialogForLongQuestion(questionsAll.get(0).getQuesTitle());


                }
            });

            System.out.println("ActivityMultipleChoiceQuestion.onCreate " + questionsAll.get(0).getQuesTitle());


            tvTotalQuestionCountMCQ.setText("/" + mIntent.getStringExtra("total_questions"));

            Integer totalQuestion = Integer.valueOf(mIntent.getStringExtra("total_questions"));

            increamentedQ = mIntent.getIntExtra("increamentedValue", 0);
            tvNumberOfQuestionMCQ.setText("" + increamentedQ);

            if (questionsAll.get(0).getOptional().equalsIgnoreCase("1")) {

                btnSkipNextMCQ.setVisibility(View.VISIBLE);
            }


            Float a = Float.valueOf(((increamentedQ * 100) / totalQuestion));

            int xx = Math.round(a);
            progressMCQ.setProgress(xx);

            time = Float.valueOf(questionsAll.get(0).getThinktime());


            if (questionsAll.get(0).getThinktime().equalsIgnoreCase("0.0")) {

                l1 = 120 * 60000;
            }

            f1 = time * 60000f;
            l1 = (long) f1;


            countDownTimer = new CountDownTimer(l1, 1000) {

                public void onTick(long millisUntilFinished) {

                    long minutes = (millisUntilFinished / 1000) / 60;
                    long seconds = (millisUntilFinished / 1000) % 60;
                    tvTimeDurationMCQ.setText(String.format("%02d:%02d", minutes, seconds));
                    timeConsumed++;
                    time--;
                }

                public void onFinish() {

                    tvTimeDurationMCQ.setText("No");
//                    loadingProgress();
                    submitApiSkip();

                }

            }.start();


            tvTimeDurationMCQ.setText(questionsAll.get(0).getThinktime());

            invitationId = mIntent.getStringExtra("invitationId");
            postId = mIntent.getStringExtra("postId");


            initAdapterHere(optionsAll);


            // Register to receive messages.
            // We are registering an observer (mMessageReceiver) to receive Intents
            // with actions named "custom-message".
            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("custom-message"));


        }


    }

    private void dialogForLongQuestion(String quesTitle) {


        LayoutInflater li = LayoutInflater.from(ActivityMultipleChoiceQuestion.this);
        View dialogView = li.inflate(R.layout.dialog_for_long_question, null);

        findTheIdsHere(dialogView, quesTitle);

        alertDialogBuilder = new AlertDialog.Builder(ActivityMultipleChoiceQuestion.this);
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

    private void mcqSeetingValuesForAts() {

        mcqQuestionsAllForAts = (ArrayList<com.smartit.jobSeeker.atsApiResponses.ats_total_question.Question>) mIntent.getSerializableExtra("PRE_RECORDED_KIT_LIST_FOR_ATS");
        mcqTotalQuestionsForAts = mIntent.getIntExtra("TOTAL_QUESTIONS_FOR_ATS", 0);
        mcqOptionsAll = (ArrayList<String>) mIntent.getSerializableExtra("OPTIONS_FOR_ATS");
        mcqInvitationIdForAts = mIntent.getStringExtra("INVITATIONID_FOR_ATS");
        mcqKitidForAts = mIntent.getIntExtra("KITID_FOR_ATS", 0);
        mcqInterviewIdForAts = mIntent.getIntExtra("INTERVIEWID_FOR_ATS", 0);
        mcqIncreamentedQ = mIntent.getIntExtra("INCREAMENTED_VALUE_FOR_ATS", 0);
        mcqCatcherId = mIntent.getIntExtra("CATCHER_ID_FOR_ATS", 0);


        tvQuestionMCQ.setText(mcqQuestionsAllForAts.get(0).getQuesTitle());
        tvTotalQuestionCountMCQ.setText("/" + mcqTotalQuestionsForAts);
        tvNumberOfQuestionMCQ.setText("" + mcqIncreamentedQ);


        tvTimeDurationMCQ.setText(mcqQuestionsAllForAts.get(0).getThinktime());

        Integer mcqTotalQuestion = mIntent.getIntExtra("TOTAL_QUESTIONS_FOR_ATS", 0);


        tvQuestionMCQ.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (tvQuestionMCQ.getLineCount() > 3) {
                    btnSeeMore.setVisibility(View.VISIBLE);

                }

            }
        });

        btnSeeMore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialogForLongQuestion(mcqQuestionsAllForAts.get(0).getQuesTitle());


            }
        });


        if (mcqQuestionsAllForAts.get(0).getOptional().equalsIgnoreCase("1")) {

            btnSkipNextMCQ.setVisibility(View.VISIBLE);
        }


        Float aaa = Float.valueOf(((mcqIncreamentedQ * 100) / mcqTotalQuestion));

        mcqTime = Float.valueOf(mcqQuestionsAllForAts.get(0).getThinktime());

        int mcqXX = Math.round(aaa);
        progressMCQ.setProgress(mcqXX);

        f1ForAts = mcqTime * 60000f;
        l1ForAts = (long) f1ForAts;

        countDownTimer = new CountDownTimer(l1ForAts, 1000) {

            public void onTick(long millisUntilFinished) {

                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
//                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
//                tvTimeDurationMCQ.setText("00:" + millisUntilFinished / 1000);
                tvTimeDurationMCQ.setText(String.format("%02d:%02d", minutes, seconds));
                mcqTime--;
            }

            public void onFinish() {

                tvTimeDurationMCQ.setText("No");
//                loadingProgress();
                mcqSubmitAnswerForAts();

            }

        }.start();


        initAdapterHere(mcqOptionsAll);

        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-message".
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("custom-message"));


    }

    private void mcqSubmitAnswerForAts() {


        if (countDownTimer != null) {

            countDownTimer.cancel();
        }

        HttpModule.provideRepositoryService().mcqForAts(mcqQuestionsAllForAts.get(0).getQtype(), String.valueOf(mcqInterviewIdForAts), mSelectedItemPosition, mcqQuestionsAllForAts.get(0).getQues(), mcqQuestionsAllForAts.get(0).getThinktime(), mcqCatcherId, mcqTotalQuestionsForAts).enqueue(new Callback<MCQSubmitAnsForAts>() {
            @Override
            public void onResponse(Call<MCQSubmitAnsForAts> call, Response<MCQSubmitAnsForAts> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();
//                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();


                    totalInterviewQuestionsForAts();


                } else {

                    mProgressDialog.dismiss();
//                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();


                }


            }

            @Override
            public void onFailure(Call<MCQSubmitAnsForAts> call, Throwable t) {

                mProgressDialog.dismiss();
                System.out.println("ActivityMultipleChoiceQuestion.onFailure " + t.getMessage());
            }
        });


    }

    private void totalInterviewQuestionsForAts() {


        HttpModule.provideRepositoryService().totalInterviewQuestionForAts(Integer.valueOf(mcqKitidForAts), mcqInterviewIdForAts).enqueue(new Callback<AtsTotalInteviewQuestion>() {
            @Override
            public void onResponse(Call<AtsTotalInteviewQuestion> call, Response<AtsTotalInteviewQuestion> response) {


                if (response.body() != null && response.body().getError() == 0) {


                    if (response.body().getData().getTotalQuestion().getQuestion().get(0).getQtype().equalsIgnoreCase("1")) {


                        if (response.body().getData().getTotalQuestion().getQuestion().get(0).getFile_status() == 1) {

                            Intent intent = new Intent(ActivityMultipleChoiceQuestion.this, ActivityVideoQuestionEnhanceAts.class);
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

                            Intent intent = new Intent(ActivityMultipleChoiceQuestion.this, ActivityVideoInterview.class);
                            intent.putExtra("PRE_RECORDED_KIT_LIST_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion());
                            intent.putExtra("OPTIONS_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions());
                            intent.putExtra("TOTAL_QUESTIONS_FOR_ATS", response.body().getData().getTotalQustion());
                            intent.putExtra("INVITATIONID_FOR_ATS", response.body().getData().getInvitationId());
                            intent.putExtra("KITID_FOR_ATS", mIntent.getIntExtra("KITID_FOR_ATS", 0));
                            intent.putExtra("INTERVIEWID_FOR_ATS", mIntent.getIntExtra("INTERVIEWID_FOR_ATS", 0));
                            intent.putExtra("CATCHER_ID_FOR_ATS", mIntent.getIntExtra("CATCHER_ID_FOR_ATS", 0));
                            intent.putExtra("COMING_ALL_THE_WAY_FROM_ATS", mIntent.getStringExtra("COMING_ALL_THE_WAY_FROM_ATS"));
                            intent.putExtra("INCREAMENTED_VALUE_FOR_ATS", mcqIncreamentedQ + 1);
                            // finishing all activities just using this to check flag
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            Animatoo.animateFade(ActivityMultipleChoiceQuestion.this);  //fire the zoom animation animateFade //animateSwipeRight

                        }


                    } else if (response.body().getData().getTotalQuestion().getQuestion().get(0).getQtype().equalsIgnoreCase("2")) {


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
                                // finishing all activities just using this to check flag
                                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
                                        putExtra("INCREAMENTED_VALUE_FOR_ATS", mcqIncreamentedQ + 1));

                        Animatoo.animateFade(ActivityMultipleChoiceQuestion.this);


                    } else if (response.body().getData().getTotalQuestion().getQuestion().get(0).getQtype().equalsIgnoreCase("3")) {


                        Intent intent = new Intent(ActivityMultipleChoiceQuestion.this, ActivitySubjectiveInterview.class);
                        intent.putExtra("PRE_RECORDED_KIT_LIST_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion());
                        intent.putExtra("OPTIONS_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions());
                        intent.putExtra("TOTAL_QUESTIONS_FOR_ATS", response.body().getData().getTotalQustion());
                        intent.putExtra("INVITATIONID_FOR_ATS", response.body().getData().getInvitationId());
                        intent.putExtra("KITID_FOR_ATS", mIntent.getIntExtra("KITID_FOR_ATS", 0));
                        intent.putExtra("INTERVIEWID_FOR_ATS", mIntent.getIntExtra("INTERVIEWID_FOR_ATS", 0));
                        intent.putExtra("CATCHER_ID_FOR_ATS", mIntent.getIntExtra("CATCHER_ID_FOR_ATS", 0));
                        intent.putExtra("COMING_ALL_THE_WAY_FROM_ATS", mIntent.getStringExtra("COMING_ALL_THE_WAY_FROM_ATS"));
                        intent.putExtra("INCREAMENTED_VALUE_FOR_ATS", mcqIncreamentedQ + 1);
                        // finishing all activities just using this to check flag
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Animatoo.animateFade(ActivityMultipleChoiceQuestion.this);  //fire the zoom animation animateFade //animateSwipeRight


                    }


                } else {

                    Intent intent = new Intent(ActivityMultipleChoiceQuestion.this, ActivityFinished.class);
                    intent.putExtra("GET_SOURCEID_FOR_ATS", mIntent.getStringExtra("GET_SOURCEID"));
                    intent.putExtra("INTERVIEWID_FOR_ATS", mIntent.getIntExtra("INTERVIEWID_FOR_ATS", 0));
                    intent.putExtra("COMING_ALL_THE_WAY_FROM_ATS", mIntent.getStringExtra("COMING_ALL_THE_WAY_FROM_ATS"));
                    intent.putExtra("INVITATIONID_FOR_ATS", mcqInvitationIdForAts);
                    // finishing all activities just using this to check flag
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }


            }

            @Override
            public void onFailure(Call<AtsTotalInteviewQuestion> call, Throwable t) {

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }


    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            mSelectedItemPosition = intent.getIntExtra("selectedValue", 0);
            System.out.println("ActivityMultipleChoiceQuestion.onReceive " + mSelectedItemPosition);


            if (mSelectedItemPosition >= 0) {

                btnPublishAndConti.setVisibility(View.VISIBLE);
            }

        }
    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    public String checkDigit(Float number) {

        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    private void initAdapterHere(ArrayList<String> optionsAll) {


        multipleChoiceQuestionAdapter = new MultipleChoiceQuestionAdapter(this, optionsAll);
        recylerRadioButtonMCQ.setHasFixedSize(true);
        recylerRadioButtonMCQ.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recylerRadioButtonMCQ.setAdapter(multipleChoiceQuestionAdapter);
    }

    private void eventListnerGoesHere() {

        backarr.setOnClickListener(this);
        btnSkipNextMCQ.setOnClickListener(this);
        btnPublishAndConti.setOnClickListener(this);


    }

    private void findingIdsHere() {

        backarr = findViewById(R.id.backarr);

        tvQuestionMCQ = findViewById(R.id.tvQuestionMCQ);
        tvNumberOfQuestionMCQ = findViewById(R.id.tvNumberOfQuestionMCQ);
        tvTotalQuestionCountMCQ = findViewById(R.id.tvTotalQuestionCountMCQ);
        tvTimeDurationMCQ = findViewById(R.id.tvTimeDurationMCQ);
        recylerRadioButtonMCQ = findViewById(R.id.recylerRadioButtonMCQ);

        progressMCQ = findViewById(R.id.progressMCQ);
        btnSkipNextMCQ = findViewById(R.id.btnSkipNextMCQ);
        btnPublishAndConti = findViewById(R.id.btnPublishAndConti);
        btnSeeMore = findViewById(R.id.btnSeeMore);
        tvAppTitle = findViewById(R.id.tvAppTitle);
        tvAppTitle.setTypeface(tvAppTitle.getTypeface(), Typeface.BOLD);


    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.backarr:
                finish();
                break;


            case R.id.btnPublishAndConti:


                if (mIntent.hasExtra("COMING_ALL_THE_WAY_FROM_ATS") && comingAllTheWayFromAts != null) {

                    loadingProgress();
                    mcqSubmitAnswerForAts();

                } else {

                    loadingProgress();
                    submitApiSkip();
                }

                if (countDownTimer != null) {

                    countDownTimer.cancel();
                }


                break;


            case R.id.btnSkipNextMCQ:

                if (mIntent.hasExtra("COMING_ALL_THE_WAY_FROM_ATS") && comingAllTheWayFromAts != null) {

                    loadingProgress();
                    mcqSubmitAnswerForAts();

                } else {
                    loadingProgress();
                    submitApiSkip();
                }

                if (countDownTimer != null) {

                    countDownTimer.cancel();
                }

                break;
        }


    }

    private void submitApiSkip() {


        HttpModule.provideRepositoryService().submitInterviewAnswerForJobSeeker((String) Hawk.get("GET_TOKEN"), questionsAll.get(0).getQtype(), questionsAll.get(0).getQues(), (float) (timeConsumed / 60.0), String.valueOf(mSelectedItemPosition == 0 ? "" : mSelectedItemPosition), 0, invitationId, postId).enqueue(new Callback<InterviewAnswer>() {
            @Override
            public void onResponse(Call<InterviewAnswer> call, Response<InterviewAnswer> response) {


                if (response.body() != null && response.body().getError() == 0) {


                    mProgressDialog.dismiss();
                    Toast.makeText(ActivityMultipleChoiceQuestion.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                    appliedId = response.body().getData().getAppliedDetail().getAppliedId();
                    sourceId = response.body().getData().getAppliedDetail().getSource();


                    callTheApplyInterviewApiFromHere();


                } else {
                    mProgressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<InterviewAnswer> call, Throwable t) {

                mProgressDialog.dismiss();
                Toast.makeText(ActivityMultipleChoiceQuestion.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }

    private void callTheApplyInterviewApiFromHere() {


        HttpModule.provideRepositoryService().applyInterviewNowUsedForJobSeeker((String) Hawk.get("GET_TOKEN"), mIntent.getIntExtra("interview_job_id", 0)).enqueue(new Callback<PreRecordedInterviewKit>() {
            @Override
            public void onResponse(Call<PreRecordedInterviewKit> call, Response<PreRecordedInterviewKit> response) {


                if (response.body() != null && response.body().getError() == 0) {


                    if (response.body().getData().getTotalQuestion().getQuestion().get(0).getQtype().equalsIgnoreCase("1")) {


                        if (response.body().getData().getTotalQuestion().getQuestion().get(0).getFile_status() == 1) {

                            Intent intent = new Intent(ActivityMultipleChoiceQuestion.this, ActivityVideoQuestionEnhance.class);
                            intent.putExtra("File_Path", response.body().getData().getTotalQuestion().getQuestion().get(0).getFile_name());
                            intent.putExtra("File_Path_Hls", response.body().getData().getTotalQuestion().getQuestion().get(0).getHls());
                            intent.putExtra("File_Thumb", response.body().getData().getTotalQuestion().getQuestion().get(0).getFile_thumb());
                            intent.putExtra("increamentedValue", increamentedQ + 1);
                            intent.putExtra("questionTitle", response.body().getData().getTotalQuestion().getQuestion().get(0).getQuesTitle());
                            intent.putExtra("total_questions", response.body().getData().getTotalQustion().toString());
                            startActivity(intent);


                        } else {


                            Intent intent = new Intent(ActivityMultipleChoiceQuestion.this, ActivityVideoInterview.class);
                            intent.putExtra("pre-recorded_kit_list", (Serializable) response.body().getData().getTotalQuestion().getQuestion());
                            intent.putExtra("options", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions());
                            intent.putExtra("total_questions", response.body().getData().getTotalQustion().toString());
                            intent.putExtra("invitationId", response.body().getData().getInvitationId());
                            intent.putExtra("postId", response.body().getData().getPostId());
                            intent.putExtra("interview_job_id", mIntent.getIntExtra("interview_job_id", 0));
                            intent.putExtra("increamentedValue", increamentedQ + 1);
                            startActivity(intent);
                            Animatoo.animateFade(ActivityMultipleChoiceQuestion.this);  //fire the zoom animation animateFade //animateSwipeRight


                        }


                    } else if (response.body().getData().getTotalQuestion().getQuestion().get(0).getQtype().equalsIgnoreCase("2")) {

                        finish();
                        startActivity(mIntent
                                .putExtra("pre-recorded_kit_list", (Serializable) response.body().getData().getTotalQuestion().getQuestion()).
                                        putExtra("options", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions()).
                                        putExtra("total_questions", response.body().getData().getTotalQustion().toString()).
                                        putExtra("invitationId", response.body().getData().getInvitationId()).
                                        putExtra("postId", response.body().getData().getPostId()).
                                        putExtra("interview_job_id", mIntent.getIntExtra("interview_job_id", 0)).
                                        putExtra("increamentedValue", increamentedQ + 1)


                        );
                        Animatoo.animateFade(ActivityMultipleChoiceQuestion.this);

                    } else if (response.body().getData().getTotalQuestion().getQuestion().get(0).getQtype().equalsIgnoreCase("3")) {

                        Intent intent = new Intent(ActivityMultipleChoiceQuestion.this, ActivitySubjectiveInterview.class);
                        intent.putExtra("pre-recorded_kit_list", (Serializable) response.body().getData().getTotalQuestion().getQuestion());
                        intent.putExtra("options", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions());
                        intent.putExtra("total_questions", response.body().getData().getTotalQustion().toString());
                        intent.putExtra("invitationId", response.body().getData().getInvitationId());
                        intent.putExtra("postId", response.body().getData().getPostId());
                        intent.putExtra("interview_job_id", mIntent.getIntExtra("interview_job_id", 0));
                        intent.putExtra("increamentedValue", increamentedQ + 1);
                        startActivity(intent);
                        Animatoo.animateFade(ActivityMultipleChoiceQuestion.this);  //fire the zoom animation animateFade //animateSwipeRight
                    }


                } else {


                    System.out.println("ActivityMultipleChoiceQuestion.onResponse  ERROR 1");
                    Intent intent = new Intent(ActivityMultipleChoiceQuestion.this, ActivityFinished.class);
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

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Animatoo.animateSlideLeft(ActivityMultipleChoiceQuestion.this); //fire the slide left animation
//    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

}
