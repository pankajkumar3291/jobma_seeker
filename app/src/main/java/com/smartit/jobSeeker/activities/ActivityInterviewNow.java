package com.smartit.jobSeeker.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.applyInterviewQuestions.ApplyInterviewQuestion;
import com.smartit.jobSeeker.apiResponse.mobDetail.MobDetail;
import com.smartit.jobSeeker.apiResponse.preRecordedInterviewKit.PreRecordedInterviewKit;
import com.smartit.jobSeeker.atsApiResponses.ats_total_question.AtsTotalInteviewQuestion;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.smartit.jobSeeker.internal_deeplinkingApiResponse.InternalDeeplinking;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

@RuntimePermissions
public class ActivityInterviewNow extends AppCompatActivity implements View.OnClickListener {


    private TextView tvInterviwerName, tvInterviwerJob, tvTotalQuestion, tvRemainingQuestion,
            tvPreRecordedQuestion, tvPreRecordedTimes, tvMultipleChoiceQuestion,
            tvMultipleChoiceQuesTimes, tvSubjectiveQuestion, tvSubjectiveQuesTimes, tvTimesTotal;

    private CheckBox checkBoxMand;
    private Button btnContinue;
    private ImageView backarr;
    private NoInternetDialog noInternetDialog;
    private ProgressDialog mProgressDialog;


    private Integer getInterviewId, getKitId, getCatcherId;
    private String getAtsValue, getSourceId;

    private String token;
    private int jobId, jobIdForDL;

    private Intent mIntent;
    private NestedScrollView scrlView;
    private TextView tvInterviewInclude;


    // RxLocation
    private ReactiveLocationProvider locationProvider;
    private CompositeDisposable compositeDisposable;

    private TextView tvAppTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_now);

        compositeDisposable = new CompositeDisposable();
        locationProvider = new ReactiveLocationProvider(this);
        ActivityInterviewNowPermissionsDispatcher.needPersmissionsToUpdateDeviceWithPermissionCheck(this);


        this.mIntent = getIntent();
        // getData(mIntent);


        noInternetDialog = new NoInternetDialog.Builder(this).build();

//        getInterviewId = getIntent().getIntExtra("INTERVIEW_ID", 0);
//        getKitId = getIntent().getIntExtra("KIT_ID", 0);
//        getCatcherId = getIntent().getIntExtra("CATCHER_ID", 0);
//        getSourceId = getIntent().getStringExtra("SOURCE_ID");
//
//
//        getAtsValue = getIntent().getStringExtra("ATS_VALUE");
//
//        jobId = getIntent().getIntExtra("job_id", 0);


        getInterviewId = mIntent.getIntExtra("INTERVIEW_ID", 0);
        getKitId = mIntent.getIntExtra("KIT_ID", 0);
        getCatcherId = mIntent.getIntExtra("CATCHER_ID", 0);
        getSourceId = mIntent.getStringExtra("SOURCE_ID");
        getAtsValue = mIntent.getStringExtra("ATS_VALUE");
        jobId = mIntent.getIntExtra("job_id", 0);

        findingTheIdsHere();
        checkBoxMand.setChecked(false);

        eventListnerGoesHere();
        loadingProgress();
        checkCleartest();

    }

    private void checkCleartest() {


        if (mIntent.hasExtra("ATS_VALUE") && mIntent.getStringExtra("ATS_VALUE").equalsIgnoreCase(getAtsValue)) {

            getTotalQuestionForAts();

        } else {


            if (jobId == 0) {


                String action = mIntent.getAction();
                Uri data = mIntent.getData();

                internalDeeplinking();

            } else {

                applyInterviewNowApiGoesHere();
            }


        }
    }

    private void internalDeeplinking() {

        final String newUrl = mIntent.getData().getPath().replaceAll("//", "");

        HttpModule.provideRepositoryService().internalDeeplinking(newUrl).enqueue(new Callback<InternalDeeplinking>() { //newUrl
            @Override
            public void onResponse(Call<InternalDeeplinking> call, Response<InternalDeeplinking> response) {

                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if (response.body() != null && response.body().getError() == 0) {


                    token = response.body().getData().getToken();
                    jobIdForDL = response.body().getData().getJobId();

                    Hawk.put("GET_TOKEN", token);

                    applyInterviewNowApiGoesHereForDL(token, jobIdForDL);


                } else {


                    new AwesomeErrorDialog(ActivityInterviewNow.this)
                            .setTitle("Oops")
                            .setMessage(Objects.requireNonNull(response.body()).getMessage())
                            .setColoredCircle(R.color.dialogErrorBackgroundColor)
                            .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                            .setCancelable(true).setButtonText(ActivityInterviewNow.this.getString(R.string.dialog_ok_button))
                            .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                            .setButtonText(ActivityInterviewNow.this.getString(R.string.dialog_ok_button))
                            .setErrorButtonClick(new Closure() {
                                @Override
                                public void exec() {

                                    finishAffinity();

                                }
                            })
                            .show();


                }

            }

            @Override
            public void onFailure(Call<InternalDeeplinking> call, Throwable t) {

                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                System.out.println("ActivityInterviewNow.onFailure");

            }
        });


    }

    private void applyInterviewNowApiGoesHereForDL(final String token, Integer jobId) {


        HttpModule.provideRepositoryService().applyInterviewNowForJobSeeker(token, jobId).enqueue(new Callback<ApplyInterviewQuestion>() {
            @Override
            public void onResponse(Call<ApplyInterviewQuestion> call, Response<ApplyInterviewQuestion> response) {


                if (response.body() != null && response.body().getError() == 0) {

//                    Toast.makeText(ActivityInterviewNow.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                    mProgressDialog.dismiss();


//                    tvTotalQuestion.setText(response.body().getData().getTotalQustion().toString() + "- Total question");
//                    tvRemainingQuestion.setText(response.body().getData().getRemainQustion().toString() + "- Remaining question");

                    tvTotalQuestion.setText("Total questions : " + response.body().getData().getTotalQustion().toString());
                    tvRemainingQuestion.setText("Remaining questions : " + response.body().getData().getRemainQustion().toString());

                    tvPreRecordedQuestion.setText(response.body().getData().getTotalVideo() + "- Pre-recorded questions");
                    tvPreRecordedTimes.setText(response.body().getData().getVideoDuration() + " minutes");


                    Hawk.put("PRE_RECORDE_QUESTION", response.body().getData().getTotalVideo());


                    tvMultipleChoiceQuestion.setText(response.body().getData().getTotalMcq() + "- Multiple choice question");
                    tvMultipleChoiceQuesTimes.setText(response.body().getData().getMcqDuration() + " minutes");

                    Hawk.put("MULTIPLE_CHOICE_QUESTION", response.body().getData().getTotalMcq());


                    tvSubjectiveQuestion.setText(response.body().getData().getTotalEssay() + "- Subjective questions");
                    tvSubjectiveQuesTimes.setText(response.body().getData().getEssayDuration() + " minutes");

                    Hawk.put("SUBJECTIVE_QUESTION", response.body().getData().getTotalEssay());

                    tvTimesTotal.setText(response.body().getData().getTotalDuration() + " minutes");

                    totalInterviewQuestionsForDl(token, jobIdForDL);


                } else {

                    mProgressDialog.dismiss();
                    Toast.makeText(ActivityInterviewNow.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<ApplyInterviewQuestion> call, Throwable t) {

                mProgressDialog.dismiss();
                Toast.makeText(ActivityInterviewNow.this, t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("ActivityInterviewNow.onFailure " + t.toString());

            }
        });


    }

    private void totalInterviewQuestionsForDl(String token, Integer jobIdForDL) {


        HttpModule.provideRepositoryService().applyInterviewNowUsedForJobSeeker(token, jobIdForDL).enqueue(new Callback<PreRecordedInterviewKit>() {
            @Override
            public void onResponse(Call<PreRecordedInterviewKit> call, Response<PreRecordedInterviewKit> response) {


                if (response.body() != null && response.body().getError() == 0) {


                    mProgressDialog.dismiss();
                    tvInterviwerName.setText("Hello " + response.body().getData().getPitcherName() + ",");
//                    tvInterviwerJob.setText("Welcome " + response.body().getData().getJobTitle());

                    Hawk.put("PithcerName", response.body().getData().getPitcherName());
                    Hawk.put("JobTitle", response.body().getData().getJobTitle());


                } else {

                    mProgressDialog.dismiss();
                    System.out.println("ActivityInterviewNow.onResponse");
                }

            }

            @Override
            public void onFailure(Call<PreRecordedInterviewKit> call, Throwable t) {

                mProgressDialog.dismiss();
                System.out.println("ActivityCheckInternetCameraMicrophone.onFailure " + t.getMessage());
            }
        });


    }


    private void getTotalQuestionForAts() {

        HttpModule.provideRepositoryService().totalInterviewQuestionForAts(getKitId, getInterviewId).enqueue(new Callback<AtsTotalInteviewQuestion>() {
            @Override
            public void onResponse(Call<AtsTotalInteviewQuestion> call, Response<AtsTotalInteviewQuestion> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();

//                    tvTotalQuestion.setText(response.body().getData().getTotalQustion().toString() + "- Total question");
//                    tvRemainingQuestion.setText(response.body().getData().getRemainQustion().toString() + "- Remaining question");

                    tvTotalQuestion.setText("Total questions : " + response.body().getData().getTotalQustion().toString());
                    tvRemainingQuestion.setText("Remaining questions : " + response.body().getData().getRemainQustion().toString());


                    // MCQ
                    tvMultipleChoiceQuestion.setText(response.body().getData().getTotalMcq() + "- Multiple choice questions");
                    tvMultipleChoiceQuesTimes.setText(response.body().getData().getMcqDuration() + " minutes");

                    Hawk.put("MCQ_QUESTIONS_FROM_ATS", response.body().getData().getTotalMcq());


                    // PRE-RECORDED
                    tvPreRecordedQuestion.setText(response.body().getData().getTotalVideo() + "- Pre-recorded questions");
                    tvPreRecordedTimes.setText(response.body().getData().getVideoDuration() + " minutes");

                    Hawk.put("PRE_RECORDED_QUESTIONS_FROM_ATS", response.body().getData().getTotalVideo());

                    // ESSAY

                    tvSubjectiveQuestion.setText(response.body().getData().getTotalEssay() + "- Subjective questions");
                    tvSubjectiveQuesTimes.setText(response.body().getData().getEssayDuration() + " minutes");

                    Hawk.put("ESSAY_QUESTIONS_FROM_ATS", response.body().getData().getTotalEssay());


                    // TOTAL MINUTES
                    tvTimesTotal.setText(response.body().getData().getTotalDuration() + " minutes");


                    tvInterviwerName.setText("Hello " + mIntent.getStringExtra("FName") + " " + mIntent.getStringExtra("LName") + ",");
//                    tvInterviwerJob.setText("Welcome " + mIntent.getStringExtra("JTitle"));


                } else {

                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<AtsTotalInteviewQuestion> call, Throwable t) {

                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("ActivityInterviewNow.onFailure " + t.getMessage());
            }
        });


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void loadingProgress() {

        mProgressDialog.setMessage("Please wait..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        noInternetDialog.onDestroy();
    }

    private void eventListnerGoesHere() {

        btnContinue.setOnClickListener(this);
        backarr.setOnClickListener(this);
    }

    private void applyInterviewNowApiGoesHere() {

        HttpModule.provideRepositoryService().applyInterviewNowForJobSeeker((String) Hawk.get("GET_TOKEN"), mIntent.getIntExtra("job_id", 0)).enqueue(new Callback<ApplyInterviewQuestion>() {
            @Override
            public void onResponse(Call<ApplyInterviewQuestion> call, Response<ApplyInterviewQuestion> response) {


                if (response.body() != null && response.body().getError() == 0) {

//                    Toast.makeText(ActivityInterviewNow.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                    mProgressDialog.dismiss();


//                    tvTotalQuestion.setText(response.body().getData().getTotalQustion().toString() + "- Total question");
//                    tvRemainingQuestion.setText(response.body().getData().getRemainQustion().toString() + "- Remaining question");


                    tvTotalQuestion.setText("Total questions : " + response.body().getData().getTotalQustion().toString());
                    tvRemainingQuestion.setText("Remaining questions : " + response.body().getData().getRemainQustion().toString());

                    tvPreRecordedQuestion.setText(response.body().getData().getTotalVideo() + "- Pre-recorded questions");
                    tvPreRecordedTimes.setText(response.body().getData().getVideoDuration() + " minutes");

//                    if (response.body().getData().getEssayDuration().equalsIgnoreCase("0.0")) {
//                        tvPreRecordedTimes.setText("Unlimited");
//                    } else {
//
//                    }


                    Hawk.put("PRE_RECORDE_QUESTION", response.body().getData().getTotalVideo());


                    tvMultipleChoiceQuestion.setText(response.body().getData().getTotalMcq() + "- Multiple choice questions");
                    tvMultipleChoiceQuesTimes.setText(response.body().getData().getMcqDuration() + " minutes");
//                    if (response.body().getData().getEssayDuration().equalsIgnoreCase("0.0")) {
//                        tvMultipleChoiceQuesTimes.setText("Unlimited");
//                    } else {
//
//                    }


                    Hawk.put("MULTIPLE_CHOICE_QUESTION", response.body().getData().getTotalMcq());


                    tvSubjectiveQuestion.setText(response.body().getData().getTotalEssay() + "- Subjective questions");

                    if (response.body().getData().getEssayDuration().equalsIgnoreCase("0.0")) {

                        tvSubjectiveQuesTimes.setText("Unlimited");
                    } else {
                        tvSubjectiveQuesTimes.setText(response.body().getData().getEssayDuration() + " minutes");
                    }


                    Hawk.put("SUBJECTIVE_QUESTION", response.body().getData().getTotalEssay());

                    tvTimesTotal.setText(response.body().getData().getTotalDuration() + " minutes");


                    totalInterviewQuestions(mIntent.getIntExtra("job_id", 0));


                } else {

                    mProgressDialog.dismiss();

                    new AwesomeErrorDialog(ActivityInterviewNow.this)
                            .setTitle("Oops")
                            .setMessage(Objects.requireNonNull(response.body()).getMessage())
                            .setColoredCircle(R.color.dialogErrorBackgroundColor)
                            .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                            .setCancelable(true).setButtonText(ActivityInterviewNow.this.getString(R.string.dialog_ok_button))
                            .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                            .setButtonText(ActivityInterviewNow.this.getString(R.string.dialog_ok_button))
                            .setErrorButtonClick(new Closure() {
                                @Override
                                public void exec() {


                                    Intent intent = new Intent(ActivityInterviewNow.this, ActivityDashboard.class);
                                    intent.putExtra("token", (String) Hawk.get("GET_TOKEN"));
                                    startActivity(intent);
                                    finish();


                                }
                            })
                            .show();


                }

            }

            @Override
            public void onFailure(Call<ApplyInterviewQuestion> call, Throwable t) {

                mProgressDialog.dismiss();
                Toast.makeText(ActivityInterviewNow.this, t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("ActivityInterviewNow.onFailure " + t.toString());

            }
        });
    }


    private void totalInterviewQuestions(int job_id) {


        HttpModule.provideRepositoryService().applyInterviewNowUsedForJobSeeker((String) Hawk.get("GET_TOKEN"), job_id).enqueue(new Callback<PreRecordedInterviewKit>() {
            @Override
            public void onResponse(Call<PreRecordedInterviewKit> call, Response<PreRecordedInterviewKit> response) {


                if (response.body() != null && response.body().getError() == 0) {


                    mProgressDialog.dismiss();
                    tvInterviwerName.setText("Hello " + response.body().getData().getPitcherName() + ",");
//                    tvInterviwerJob.setText("Welcome " + response.body().getData().getJobTitle());

                    Hawk.put("PithcerName", response.body().getData().getPitcherName());
                    Hawk.put("JobTitle", response.body().getData().getJobTitle());


                } else {

                    mProgressDialog.dismiss();
                    System.out.println("ActivityInterviewNow.onResponse");
                }

            }

            @Override
            public void onFailure(Call<PreRecordedInterviewKit> call, Throwable t) {

                mProgressDialog.dismiss();
                System.out.println("ActivityCheckInternetCameraMicrophone.onFailure " + t.getMessage());
            }
        });


    }


    private void findingTheIdsHere() {

        scrlView = findViewById(R.id.scrlView);
        tvInterviwerName = findViewById(R.id.tvInterviwerName);

        tvInterviewInclude = findViewById(R.id.tvInterviewInclude);
        tvInterviewInclude.setTypeface(tvInterviewInclude.getTypeface(), Typeface.BOLD);

        tvInterviwerJob = findViewById(R.id.tvInterviwerJob);

        tvTotalQuestion = findViewById(R.id.tvTotalQuestion);

        tvRemainingQuestion = findViewById(R.id.tvRemainingQuestion);

        tvPreRecordedQuestion = findViewById(R.id.tvPreRecordedQuestion);

        tvPreRecordedTimes = findViewById(R.id.tvPreRecordedTimes);

        tvMultipleChoiceQuestion = findViewById(R.id.tvMultipleChoiceQuestion);

        tvMultipleChoiceQuesTimes = findViewById(R.id.tvMultipleChoiceQuesTimes);

        tvSubjectiveQuestion = findViewById(R.id.tvSubjectiveQuestion);

        tvSubjectiveQuesTimes = findViewById(R.id.tvSubjectiveQuesTimes);

        tvTimesTotal = findViewById(R.id.tvTimesTotal);
        backarr = findViewById(R.id.backarr);

        checkBoxMand = findViewById(R.id.checkBoxMand);
        btnContinue = findViewById(R.id.btnContinue);

        tvAppTitle = findViewById(R.id.tvAppTitle);
        tvAppTitle.setTypeface(tvAppTitle.getTypeface(), Typeface.BOLD);

        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        permissionAllow();


        Hawk.put("INTERVIEW_JOB_ID", mIntent.getIntExtra("job_id", 0));


    }

    private void permissionAllow() {


        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.RECORD_AUDIO};
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                // do your task.
            }


            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                showSettingsDialog();

            }
        });


    }

    private void showSettingsDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityInterviewNow.this);
        builder.setTitle("Need Permissions");
        builder.setCancelable(false);
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();


    }

    private void openSettings() {

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.mIntent = intent;
        try {
            checkBoxMand.setChecked(false);
            scrlView.smoothScrollTo(0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getData(intent);


    }

    public void getData(Intent intent) {


        getInterviewId = intent.getIntExtra("INTERVIEW_ID", 0);
        getKitId = intent.getIntExtra("KIT_ID", 0);
        getCatcherId = intent.getIntExtra("CATCHER_ID", 0);
        getSourceId = intent.getStringExtra("SOURCE_ID");


        getAtsValue = intent.getStringExtra("ATS_VALUE");

        jobId = intent.getIntExtra("job_id", 0);

        checkCleartest();
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.btnContinue:
                if (!checkBoxMand.isChecked()) {

                    Toast.makeText(ActivityInterviewNow.this, "Please accept Terms and Policy", Toast.LENGTH_LONG).show();

                } else {

                    if (jobId == 0) {

                        callTheActivityFromHereForDL();

                    } else {

                        callTheActivityFromHere();
                    }


                }
                break;

            case R.id.backarr:
                finish();
                break;

        }


    }

    private void callTheActivityFromHereForDL() {


        Intent intent = new Intent(ActivityInterviewNow.this, ActivityCheckInternetCameraMicrophone.class);
        intent.putExtra("interview_job_id", jobIdForDL);
        intent.putExtra("PName", Hawk.get("PithcerName", ""));
        intent.putExtra("JTitle", Hawk.get("JobTitle", ""));

        intent.putExtra("P_RECORDED_QUESTION", Hawk.get("PRE_RECORDE_QUESTION", ""));
        intent.putExtra("M_CHOICE_QUESTION", Hawk.get("MULTIPLE_CHOICE_QUESTION", ""));
        intent.putExtra("S_QUESTION", Hawk.get("SUBJECTIVE_QUESTION", ""));


        intent.putExtra("F_Name", mIntent.getStringExtra("FName"));
        intent.putExtra("L_Name", mIntent.getStringExtra("LName"));
        intent.putExtra("J_Title", mIntent.getStringExtra("JTitle"));
        intent.putExtra("ComingFromATS", mIntent.getStringExtra("ATS_VALUE"));

        intent.putExtra("GET_KITID", getKitId);
        intent.putExtra("GET_INTERVIEWID", getInterviewId);
        intent.putExtra("GET_CATCHERID", getCatcherId);
        intent.putExtra("GET_SOURCEID", getSourceId);


        startActivity(intent);


    }

    private void callTheActivityFromHere() {


        Intent intent = new Intent(ActivityInterviewNow.this, ActivityCheckInternetCameraMicrophone.class);
        intent.putExtra("interview_job_id", mIntent.getIntExtra("job_id", 0));
        intent.putExtra("PName", Hawk.get("PithcerName", ""));
        intent.putExtra("JTitle", Hawk.get("JobTitle", ""));

        intent.putExtra("P_RECORDED_QUESTION", Hawk.get("PRE_RECORDE_QUESTION", ""));
        intent.putExtra("M_CHOICE_QUESTION", Hawk.get("MULTIPLE_CHOICE_QUESTION", ""));
        intent.putExtra("S_QUESTION", Hawk.get("SUBJECTIVE_QUESTION", ""));


        intent.putExtra("F_Name", mIntent.getStringExtra("FName"));
        intent.putExtra("L_Name", mIntent.getStringExtra("LName"));
        intent.putExtra("J_Title", mIntent.getStringExtra("JTitle"));
        intent.putExtra("ComingFromATS", mIntent.getStringExtra("ATS_VALUE"));

        intent.putExtra("GET_KITID", getKitId);
        intent.putExtra("GET_INTERVIEWID", getInterviewId);
        intent.putExtra("GET_CATCHERID", getCatcherId);
        intent.putExtra("GET_SOURCEID", getSourceId);
        // finishing all activities just using this to check flag
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        startActivity(intent);


    }


    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void needPersmissionsToUpdateDevice() {

        compositeDisposable.add(locationProvider.getLastKnownLocation()
                .subscribe(new Consumer<Location>() {
                    @Override
                    public void accept(Location location) throws Exception {

                        Observable<List<Address>> reverseGeocodeObservable = locationProvider
                                .getReverseGeocodeObservable(location.getLatitude(), location.getLongitude(), 1);

                        compositeDisposable.add(reverseGeocodeObservable
                                .subscribeOn(Schedulers.io())               // use I/O thread to query for addresses
                                .observeOn(AndroidSchedulers.mainThread())  // return result in main android thread to manipulate UI
                                .subscribe(new Consumer<List<Address>>() {
                                    @Override
                                    public void accept(List<Address> addresses) throws Exception {
                                        if (addresses.size() > 0) {

                                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(ActivityInterviewNow.this, instanceIdResult -> {
                                                String newToken = instanceIdResult.getToken();
                                                Log.e("newToken", newToken);

                                                System.out.println("Activity.accept " + addresses);

                                                mobDetailApi(newToken,
                                                        addresses.get(0).getAdminArea(),
                                                        addresses.get(0).getCountryName(),
                                                        addresses.get(0).getPostalCode(),
                                                        addresses.get(0).getSubAdminArea(),
                                                        location.getLatitude() + "",
                                                        location.getLongitude() + "");


                                            });

                                        }


                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        throwable.printStackTrace();
                                        Crashlytics.logException(throwable);
                                    }
                                }));


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        Crashlytics.logException(throwable);
                    }
                }));

    }

    private void mobDetailApi(String token, String state, String country, String zipcode, String city, String lat, String lng) {

        Field[] fields = Build.VERSION_CODES.class.getFields();

//         String osName = fields[Build.VERSION.SDK_INT + 1].getName();
//         Log.d("Android OsName:", osName);
//         System.out.println("ActivityDashboard.mobDetailApi " + osName);

        String model = Build.MODEL;
        String reqString = Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();


        HttpModule.provideRepositoryService().mobDetail(Hawk.get("GET_TOKEN"), model, reqString, state, country, zipcode, city, lat, "Android", lng, String.valueOf(Build.VERSION.SDK_INT), token).enqueue(new Callback<MobDetail>() {
            @Override
            public void onResponse(Call<MobDetail> call, Response<MobDetail> response) {

                System.out.println("ActivityDashboard.onResponse " + response.body().getMessage());

//                Hawk.delete("GET_TOKEN");

            }

            @Override
            public void onFailure(Call<MobDetail> call, Throwable t) {

                System.out.println("ActivityDashboard.onFailure " + t.toString());
            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ActivityInterviewNowPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void showRatPersmissionsToUpdateDevice(final PermissionRequest request) {

    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onDeniedPersmissionsToUpdateDevice() {
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onNeverAskedPersmissionsToUpdateDevice() {
    }

}
