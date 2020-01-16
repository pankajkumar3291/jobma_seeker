package com.smartit.jobSeeker.activities.link;

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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smartit.jobSeeker.MessageEvent;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.utilsCamera.CameraPreview;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import am.appwise.components.ni.NoInternetDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ActivityVideoInterview2 extends AppCompatActivity implements View.OnClickListener {


    private TextView tvTotalQuestionCountVideo, tvQuestionVideo, tvDurationTimeVideo, tvThinkTimeVideo, tvAttemptsVideo, tvNumberOfQuestionVideo;
    private String invitationId, postId;


    private Camera mCamera;
    private CameraPreview mPreview;
    private MediaRecorder mediaRecorder;
    private Context myContext;
    private LinearLayout cameraPreview;
    private boolean cameraFront = false;


    private ImageButton imageSwitch, imageRecord, imageStop, imageSkip;

    private double timeConsumed = 0.0;
    boolean recording = false;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private Button btnPublishAndContiJust;
    private CountDownTimer countDownTimerThinkTime, countDownTimerDuration;


    private File file;
    private NoInternetDialog noInternetDialog;
    private ProgressDialog mProgressDialog;


    private long durationTimer, thinkTimer;


    private TextView btnSeeMore, tvTimer;
    private TextView tvSuccessMessage;
    private Button buttonOk;
    private Intent mIntent;

    private boolean isFront;


    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_interview2);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myContext = this;
        noInternetDialog = new NoInternetDialog.Builder(this).build();


        findingIdsHere();
        eventlistner();
        initialize();


        imageStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if ((timeConsumed) < 5) {

                        Toast.makeText(getApplicationContext(), "Minimum recording should be 5 seconds", Toast.LENGTH_SHORT).show();
                        return;

                    } else {

                        mediaRecorder.stop();
                        mediaRecorder.release();
                        mediaRecorder = null;

                        Toast.makeText(getApplicationContext(), "Stop recording..", Toast.LENGTH_SHORT).show();

                        if (countDownTimerDuration != null) {
                            countDownTimerDuration.cancel();
                        }

                        EventBus.getDefault().post(new MessageEvent(file.getPath()));
                        onBackPressed();

                    }


                } catch (IllegalStateException e) {
                    //  it is called before start()
                    e.printStackTrace();
                } catch (RuntimeException e) {
                    // no valid audio/video data has been received
                    System.out.println("ActivityVideoInterview.onClick exception     " + e);
                    e.printStackTrace();
                }


            }
        });


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


    private void eventlistner() {
        imageSkip.setOnClickListener(this);
    }


    private void startRecordingAgain() {

        imageSwitch.setVisibility(View.GONE);
        imageRecord.setVisibility(View.GONE);
        imageStop.setVisibility(View.VISIBLE);


        if (countDownTimerThinkTime != null) {
            countDownTimerThinkTime.cancel();
        }


        if (recording) {
            // stop recording and release camera
            mediaRecorder.stop(); // stop the recording
            releaseMediaRecorder(); // release the MediaRecorder object
            Toast.makeText(ActivityVideoInterview2.this, "Video captured!", Toast.LENGTH_LONG).show();

            recording = false;

        } else {

            if (!prepareMediaRecorder()) {
                Toast.makeText(ActivityVideoInterview2.this, "Fail in prepareMediaRecorder()!\n - Ended -", Toast.LENGTH_LONG).show();
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
            Toast.makeText(ActivityVideoInterview2.this, "Video captured!", Toast.LENGTH_LONG).show();

            recording = false;
        } else {
            if (!prepareMediaRecorder()) {
                Toast.makeText(ActivityVideoInterview2.this, "Fail in prepareMediaRecorder()!\n - Ended -", Toast.LENGTH_LONG).show();
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


        countDownTimerDuration = new CountDownTimer(durationTimer, 1000) {

            public void onTick(long millisUntilFinished) {


                long millisElapsed = 500000 - millisUntilFinished;
                long minutes = (millisElapsed / 1000) / 60;
                long seconds = (millisElapsed / 1000) % 60;

//                tvDurationTimeVideo.setText(String.format("00:%d:%d", minutes, seconds));
//                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
//                tvDurationTimeVideo.setText("00:" + millisUntilFinished / 1000);
                tvTimer.setText(String.format("%02d:%02d:%02d", 0L, minutes, seconds));
                timeConsumed++;

                DecimalFormat df = new DecimalFormat("#.##");
                df.setRoundingMode(RoundingMode.CEILING);
                System.out.println("ActivityVideoInterview.onTick " + df.format(timeConsumed / 60.0));


            }

            public void onFinish() {

                tvDurationTimeVideo.setText("No");
                tvDurationTimeVideo.setTextColor(Color.parseColor("#2DBFD9"));


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

        isFront = false;
        int cameraId = -1;
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


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

        if (countDownTimerThinkTime != null) {
            countDownTimerThinkTime.cancel();
        }

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
        tvTimer = findViewById(R.id.tvTimer);


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

                mCamera.setDisplayOrientation(90);
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
                mCamera.setDisplayOrientation(90);
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        // when on Pause, release camera in order to be used from other
        // applications
        releaseCamera();
        System.out.println("ActivityVideoInterview2.onPause");


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


            // timerForDuration();

            if (recording) {
                // stop recording and release camera
                mediaRecorder.stop(); // stop the recording
                releaseMediaRecorder(); // release the MediaRecorder object
                Toast.makeText(ActivityVideoInterview2.this, "Video captured!", Toast.LENGTH_LONG).show();
                recording = false;
                countDownTimerThinkTime.cancel();

            } else {

                if (!prepareMediaRecorder()) {

                    Toast.makeText(ActivityVideoInterview2.this, "Fail in prepareMediaRecorder()!\n - Ended -", Toast.LENGTH_LONG).show();
                    finish();
                }
                // work on UiThread for better performance
                runOnUiThread(new Runnable() {
                    public void run() {

                        // If there are stories, add them to the table

                        try {

                            mediaRecorder.start();

                            if (countDownTimerThinkTime != null) {
                                countDownTimerThinkTime.cancel();
                            } else {

                                countDownTimerThinkTime = new CountDownTimer(500000, 1000) {

                                    public void onTick(long millisUntilFinished) {

                                        long millisElapsed = 500000 - millisUntilFinished;
                                        long minutes = (millisElapsed / 1000) / 60;
                                        long seconds = (millisElapsed / 1000) % 60;
                                        tvTimer.setText(String.format("%02d:%02d:%02d", 0L, minutes, seconds));
                                        timeConsumed++;

                                    }

                                    public void onFinish() {

                                        tvThinkTimeVideo.setText("No");
                                        tvThinkTimeVideo.setTextColor(Color.parseColor("#2DBFD9"));

                                        imageRecord.setVisibility(View.GONE);
                                        imageStop.setVisibility(View.VISIBLE);
                                        imageSwitch.setVisibility(View.GONE);

                                        timerForDuration();
                                        videoAutomaticallyStartsRecording();


                                    }

                                }.start();
                            }

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

      /*  if (mOrientation != OrientationEventListener.ORIENTATION_UNKNOWN) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                rotation = (info.orientation - mOrientation + 360) % 360;
            } else {  // back-facing camera
                rotation = (info.orientation + mOrientation) % 360;
            }
        }*/

        mediaRecorder.setOrientationHint(isFront ? 270 : 90);
        mediaRecorder.setCamera(mCamera);


        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));

        mediaRecorder.setMaxDuration(120000);  // Set max duration 60 sec.
//        mediaRecorder.setMaxFileSize(50000000); // Set max file size 50M
        mediaRecorder.setVideoSize(640, 480);
        file = new File(Environment.getExternalStorageDirectory(), "videocapture_profile.mp4");
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

                break;
        }


    }


}
