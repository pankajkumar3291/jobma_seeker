package com.smartit.jobSeeker.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.preRecordedInterviewKit.PreRecordedInterviewKit;
import com.smartit.jobSeeker.atsApiResponses.ats_total_question.AtsTotalInteviewQuestion;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.smartit.jobSeeker.internetConnection.InternetConnection;
import com.tbruyelle.rxpermissions2.RxPermissions;

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

import static android.content.pm.PackageManager.FEATURE_CAMERA;

public class ActivityCheckInternetCameraMicrophone extends AppCompatActivity {


    private ImageView imageCheckInternet, imageCheckCamera, imageCheckMircophone,
            imageShowStatus, imageShowStatusCamera, imageShowStatusMicro, backarr;
    private TextView tvCheckInternetSpeed, tvInterviwerName, tvInterviwerJob, tvPreRecordInst;
    private WifiManager wifiManger;
    private boolean hasCamera;
    private ProgressDialog mProgressDialog;
    private PermissionListener permissionlistener;
    private Button btnStart;

    private NoInternetDialog noInternetDialog;
    String pre_recorded_questions, multiple_choice_question, subjective_question;


    private Integer getKitId, getInterviewId, getCatcherId;
    private String getSourceId;

    private String c;
    private Intent mIntent;
    private TextView tvAppTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_internet_camera_microphone);


        noInternetDialog = new NoInternetDialog.Builder(this).build();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        mIntent = getIntent();
        getData(mIntent);


        findingIdsHere();
        loadingProgress();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mProgressDialog.dismiss();

//                checkInternetConnection();
            }
        }, 1000);


    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getData(intent);

    }

    private void getData(Intent intent) {

        this.mIntent = intent;
        pre_recorded_questions = intent.getStringExtra("P_RECORDED_QUESTION");
        multiple_choice_question = intent.getStringExtra("M_CHOICE_QUESTION");
        subjective_question = intent.getStringExtra("S_QUESTION");


        getKitId = intent.getIntExtra("GET_KITID", 0);
        getInterviewId = intent.getIntExtra("GET_INTERVIEWID", 0);
        getCatcherId = intent.getIntExtra("GET_CATCHERID", 0);
        getSourceId = intent.getStringExtra("GET_SOURCEID");


        c = intent.getStringExtra("ComingFromATS");


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


    private void loadingProgress() {

        mProgressDialog.setMessage("Loading..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

    }

    private void checkInternetConnection() {

        if (InternetConnection.checkConnection(ActivityCheckInternetCameraMicrophone.this)) {

            // Its Available

            wifiManger = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManger.getConnectionInfo();
            int speedMbps = wifiInfo.getLinkSpeed();

            tvCheckInternetSpeed.setText("(your connection speed is " + String.valueOf(speedMbps) + "Mbps)");


            Drawable res = getResources().getDrawable(R.drawable.done);
            imageCheckInternet.setImageDrawable(res);
            imageShowStatus.setBackgroundColor(Color.parseColor("#2DBFD9"));


            new AwesomeSuccessDialog(ActivityCheckInternetCameraMicrophone.this)
                    .setTitle("")
                    .setMessage("you are connected with the internet")
                    .setColoredCircle(R.color.colorlogin)
                    .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
                    .setCancelable(false)
                    .setPositiveButtonText("Ok")
                    .setPositiveButtonbackgroundColor(R.color.colorlogin)
                    .setPositiveButtonTextColor(R.color.white)
                    .setPositiveButtonClick(new Closure() {
                        @Override
                        public void exec() {

//                            checkCameraHasCameraHasPermission();
                        }
                    })
                    .show();


        } else {

            Toast.makeText(ActivityCheckInternetCameraMicrophone.this, "There is no internet connection", Toast.LENGTH_LONG).show();

        }


    }

    private void checkCameraHasCameraHasPermission() {


        // CHECKING CAMERA IS AVAILABLE IN THE DEVICE
        final boolean isCameraAvailable = getPackageManager().hasSystemFeature(FEATURE_CAMERA);
        System.out.println("ActivityCheckInternetCameraMicrophone.checkInternetConnection " + isCameraAvailable);

        // CHECK HOW MANY CAMERAS ARE THERE IN DEVICE

        int numCameras = Camera.getNumberOfCameras();
        if (numCameras > 0) {
            hasCamera = true;
            System.out.println("ActivityCheckInternetCameraMicrophone.checkInternetConnection " + numCameras);

        }

        permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

                imageShowStatusCamera.setBackgroundColor(Color.parseColor("#2DBFD9"));
                Drawable res = getResources().getDrawable(R.drawable.done);
                imageCheckCamera.setImageDrawable(res);

                Toast.makeText(ActivityCheckInternetCameraMicrophone.this, "Permission Granted", Toast.LENGTH_LONG).show();

//                checkHeadsetConnect();


            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                Toast.makeText(ActivityCheckInternetCameraMicrophone.this, "Permission Denied\n", Toast.LENGTH_LONG).show();
            }
        };


        TedPermission.with(ActivityCheckInternetCameraMicrophone.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();


    }

    private void checkHeadsetConnect() {

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        Objects.requireNonNull(audioManager).isWiredHeadsetOn();

    }


    private void findingIdsHere() {

        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        imageCheckInternet = findViewById(R.id.imageCheckInternet);
        imageCheckCamera = findViewById(R.id.imageCheckCamera);
        imageCheckMircophone = findViewById(R.id.imageCheckMircophone);


        tvAppTitle = findViewById(R.id.tvAppTitle);
        tvAppTitle.setTypeface(tvAppTitle.getTypeface(), Typeface.BOLD);


        tvPreRecordInst = findViewById(R.id.tvPreRecordInst);
        tvPreRecordInst.setTypeface(tvPreRecordInst.getTypeface(), Typeface.BOLD);

        imageShowStatus = findViewById(R.id.imageShowStatus);
        imageShowStatusCamera = findViewById(R.id.imageShowStatusCamera);
        imageShowStatusMicro = findViewById(R.id.imageShowStatusMicro);

        backarr = findViewById(R.id.backarr);

        tvInterviwerName = findViewById(R.id.tvInterviwerName);
        tvInterviwerJob = findViewById(R.id.tvInterviwerJob);


        if (mIntent.hasExtra("ComingFromATS") && c != null) {

            tvInterviwerName.setText("Hello " + mIntent.getStringExtra("F_Name") + " " + mIntent.getStringExtra("L_Name") + ",");
            tvInterviwerJob.setText("Welcome " + mIntent.getStringExtra("J_Title"));


        } else {

            tvInterviwerName.setText("Hello " + mIntent.getStringExtra("PName") + ",");
            tvInterviwerJob.setText("Welcome " + mIntent.getStringExtra("JTitle"));
        }


        tvCheckInternetSpeed = findViewById(R.id.tvCheckInternetSpeed);
        btnStart = findViewById(R.id.btnStart);


        backarr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RxPermissions rxPermissions = new RxPermissions(ActivityCheckInternetCameraMicrophone.this);
                rxPermissions
                        .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO) // ask single or multiple permission once
                        .subscribe(granted -> {
                            if (granted) {

                                if (mIntent.hasExtra("ComingFromATS") && c != null) {

                                    callTheTotalInterviewQuestionApiForAts();

                                } else {

                                    callTheApplyInterviewApiFromHere();
                                }

                                // All requested permissions are granted
                            } else {

                                Toast.makeText(ActivityCheckInternetCameraMicrophone.this, "Please provide the permissions", Toast.LENGTH_SHORT).show();

                                // At least one permission is denied
                            }
                        });


                // todo Shahzeb commented this and write the same code above with asking all the permission
//                if (mIntent.hasExtra("ComingFromATS") && c != null) {
//
//                    callTheTotalInterviewQuestionApiForAts();
//
//                } else {
//
//                    callTheApplyInterviewApiFromHere();
//                }


            }
        });


    }

    private void callTheTotalInterviewQuestionApiForAts() {


        System.out.println("ActivityCheckInternetCameraMicrophone.callTheTotalInterviewQuestionApiForAts " + getKitId + " " + getInterviewId);

        HttpModule.provideRepositoryService().totalInterviewQuestionForAts(getKitId, getInterviewId).enqueue(new Callback<AtsTotalInteviewQuestion>() {
            @Override
            public void onResponse(Call<AtsTotalInteviewQuestion> call, Response<AtsTotalInteviewQuestion> response) {


                if (response.body() != null && response.body().getError() == 0) {


                    if (response.body().getData().getTotalQuestion().getQuestion().get(0).getQtype().equalsIgnoreCase("1")) {

                        Intent intent = new Intent(ActivityCheckInternetCameraMicrophone.this, ActivityVideoInterview.class);
                        intent.putExtra("COMING_ALL_THE_WAY_FROM_ATS", mIntent.getStringExtra("ComingFromATS"));
                        intent.putExtra("PRE_RECORDED_KIT_LIST_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion());
                        intent.putExtra("OPTIONS_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions());
                        intent.putExtra("TOTAL_QUESTIONS_FOR_ATS", response.body().getData().getTotalQustion());
                        intent.putExtra("INVITATIONID_FOR_ATS", response.body().getData().getInvitationId());
                        intent.putExtra("KITID_FOR_ATS", getKitId);
                        intent.putExtra("INTERVIEWID_FOR_ATS", getInterviewId);
                        intent.putExtra("CATCHER_ID_FOR_ATS", getCatcherId);
                        intent.putExtra("GET_SOURCEID", getSourceId);
                        intent.putExtra("INCREAMENTED_VALUE_FOR_ATS", response.body().getData().getTotalQustion() - response.body().getData().getRemainQustion() + 1);
                        // finishing all activities just using this to check flag
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();


                    } else if (response.body().getData().getTotalQuestion().getQuestion().get(0).getQtype().equalsIgnoreCase("2")) {

                        Intent intent = new Intent(ActivityCheckInternetCameraMicrophone.this, ActivityMultipleChoiceQuestion.class);

                        intent.putExtra("COMING_ALL_THE_WAY_FROM_ATS", mIntent.getStringExtra("ComingFromATS"));

                        intent.putExtra("PRE_RECORDED_KIT_LIST_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion());
                        intent.putExtra("OPTIONS_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions());
                        intent.putExtra("TOTAL_QUESTIONS_FOR_ATS", response.body().getData().getTotalQustion());
                        intent.putExtra("INVITATIONID_FOR_ATS", response.body().getData().getInvitationId());
                        intent.putExtra("KITID_FOR_ATS", getKitId);
                        intent.putExtra("INTERVIEWID_FOR_ATS", getInterviewId);
                        intent.putExtra("CATCHER_ID_FOR_ATS", getCatcherId);
                        intent.putExtra("GET_SOURCEID", getSourceId);
                        intent.putExtra("INCREAMENTED_VALUE_FOR_ATS", response.body().getData().getTotalQustion() - response.body().getData().getRemainQustion() + 1);
                        // finishing all activities just using this to check flag
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    } else if (response.body().getData().getTotalQuestion().getQuestion().get(0).getQtype().equalsIgnoreCase("3")) {

                        Intent intent = new Intent(ActivityCheckInternetCameraMicrophone.this, ActivitySubjectiveInterview.class);
                        intent.putExtra("COMING_ALL_THE_WAY_FROM_ATS", mIntent.getStringExtra("ComingFromATS"));
                        intent.putExtra("PRE_RECORDED_KIT_LIST_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion());
                        intent.putExtra("OPTIONS_FOR_ATS", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions());
                        intent.putExtra("TOTAL_QUESTIONS_FOR_ATS", response.body().getData().getTotalQustion());
                        intent.putExtra("INVITATIONID_FOR_ATS", response.body().getData().getInvitationId());
                        intent.putExtra("KITID_FOR_ATS", getKitId);
                        intent.putExtra("INTERVIEWID_FOR_ATS", getInterviewId);
                        intent.putExtra("CATCHER_ID_FOR_ATS", getCatcherId);
                        intent.putExtra("GET_SOURCEID", getSourceId);
                        intent.putExtra("INCREAMENTED_VALUE_FOR_ATS", response.body().getData().getTotalQustion() - response.body().getData().getRemainQustion() + 1);
                        // finishing all activities just using this to check flag
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }


                } else {

                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<AtsTotalInteviewQuestion> call, Throwable t) {

                System.out.println("ActivityCheckInternetCameraMicrophone.onFailure " + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void callTheApplyInterviewApiFromHere() {


        Hawk.put("INTERVIEW_JOB_ID", mIntent.getIntExtra("interview_job_id", 0));


        HttpModule.provideRepositoryService().applyInterviewNowUsedForJobSeeker((String) Hawk.get("GET_TOKEN"), mIntent.getIntExtra("interview_job_id", 0)).enqueue(new Callback<PreRecordedInterviewKit>() {
            @Override
            public void onResponse(Call<PreRecordedInterviewKit> call, Response<PreRecordedInterviewKit> response) {


                if (response.body() != null && response.body().getError() == 0) {


                    mProgressDialog.dismiss();


                    if (response.body().getData().getTotalQuestion().getQuestion().get(0).getQtype().equalsIgnoreCase("1")) {


                        if (response.body().getData().getTotalQuestion().getQuestion().get(0).getFile_status() == 1) {

                            Intent intent = new Intent(ActivityCheckInternetCameraMicrophone.this, ActivityVideoQuestionEnhance.class);
                            intent.putExtra("File_Path", response.body().getData().getTotalQuestion().getQuestion().get(0).getFile_name());
                            intent.putExtra("File_Path_Hls", response.body().getData().getTotalQuestion().getQuestion().get(0).getHls());
                            intent.putExtra("File_Thumb", response.body().getData().getTotalQuestion().getQuestion().get(0).getFile_thumb());
                            intent.putExtra("increamentedValue", response.body().getData().getTotalQustion() - response.body().getData().getRemainQustion() + 1);
                            intent.putExtra("total_questions", response.body().getData().getTotalQustion().toString());

                            intent.putExtra("questionTitle", response.body().getData().getTotalQuestion().getQuestion().get(0).getQuesTitle());
                            intent.putExtra("P_Recorded", pre_recorded_questions);
                            intent.putExtra("Multiple_Choice", multiple_choice_question);
                            intent.putExtra("Subjective", subjective_question);

                            startActivity(intent);


                        } else {

                            Intent intent = new Intent(ActivityCheckInternetCameraMicrophone.this, ActivityVideoInterview.class);
                            intent.putExtra("pre-recorded_kit_list", (Serializable) response.body().getData().getTotalQuestion().getQuestion());
                            intent.putExtra("options", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions());
                            intent.putExtra("total_questions", response.body().getData().getTotalQustion().toString());
                            intent.putExtra("invitationId", response.body().getData().getInvitationId());
                            intent.putExtra("postId", response.body().getData().getPostId());
                            intent.putExtra("interview_job_id", mIntent.getIntExtra("interview_job_id", 0));
                            intent.putExtra("increamentedValue", response.body().getData().getTotalQustion() - response.body().getData().getRemainQustion() + 1);

                            intent.putExtra("P_Recorded", pre_recorded_questions);
                            intent.putExtra("Multiple_Choice", multiple_choice_question);
                            intent.putExtra("Subjective", subjective_question);

                            startActivity(intent);
                            finish();
                        }


                    } else if (response.body().getData().getTotalQuestion().getQuestion().get(0).getQtype().equalsIgnoreCase("2")) {

                        Intent intent = new Intent(ActivityCheckInternetCameraMicrophone.this, ActivityMultipleChoiceQuestion.class);
                        intent.putExtra("pre-recorded_kit_list", (Serializable) response.body().getData().getTotalQuestion().getQuestion());
                        intent.putExtra("options", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions());
                        intent.putExtra("total_questions", response.body().getData().getTotalQustion().toString());
                        intent.putExtra("invitationId", response.body().getData().getInvitationId());
                        intent.putExtra("postId", response.body().getData().getPostId());
                        intent.putExtra("interview_job_id", mIntent.getIntExtra("interview_job_id", 0));
                        intent.putExtra("increamentedValue", response.body().getData().getTotalQustion() - response.body().getData().getRemainQustion() + 1);

                        intent.putExtra("P_Recorded", pre_recorded_questions);
                        intent.putExtra("Multiple_Choice", multiple_choice_question);
                        intent.putExtra("Subjective", subjective_question);

                        startActivity(intent);
                        finish();

                    } else if (response.body().getData().getTotalQuestion().getQuestion().get(0).getQtype().equalsIgnoreCase("3")) {

                        Intent intent = new Intent(ActivityCheckInternetCameraMicrophone.this, ActivitySubjectiveInterview.class);
                        intent.putExtra("pre-recorded_kit_list", (Serializable) response.body().getData().getTotalQuestion().getQuestion());
                        intent.putExtra("options", (Serializable) response.body().getData().getTotalQuestion().getQuestion().get(0).getOptions());
                        intent.putExtra("total_questions", response.body().getData().getTotalQustion().toString());
                        intent.putExtra("invitationId", response.body().getData().getInvitationId());
                        intent.putExtra("postId", response.body().getData().getPostId());
                        intent.putExtra("interview_job_id", mIntent.getIntExtra("interview_job_id", 0));
                        intent.putExtra("increamentedValue", response.body().getData().getTotalQustion() - response.body().getData().getRemainQustion() + 1);

                        intent.putExtra("P_Recorded", pre_recorded_questions);
                        intent.putExtra("Multiple_Choice", multiple_choice_question);
                        intent.putExtra("Subjective", subjective_question);

                        startActivity(intent);
                        finish();
                    }

                } else {

                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();


                }

            }

            @Override
            public void onFailure(Call<PreRecordedInterviewKit> call, Throwable t) {

                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}
