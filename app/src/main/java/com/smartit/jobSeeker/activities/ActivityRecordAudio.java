package com.smartit.jobSeeker.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.uploadAudioResume.UploadAudioResume;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class ActivityRecordAudio extends AppCompatActivity implements View.OnClickListener {


    private ImageView imageMic, imageMicAnim, backArrowImage, stopRecordingImage, playRecordingImage, stopPlayingRecording;
    private TextView tvInstText, tvRecordAT;
    private Button btnRecord, btnUpload;


    private String AudioSavePathInDevice = null;
    private MediaRecorder mediaRecorder;
    private Random random;
    private String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    private MediaPlayer mediaPlayer;
    private CountDownTimer countDownTimer, countDownTimerStopWala;
    private int counter;
    private double timeConsumed = 0;
    private ProgressDialog mProgressDialog;

    private String totalTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_audio);

        Hawk.init(ActivityRecordAudio.this).build();

        random = new Random();
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        initViewsHere();
        listener();


        stopRecordingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter < 30) {

                    Toast.makeText(ActivityRecordAudio.this, "Minimum recording should be 30 seconds", Toast.LENGTH_LONG).show();
                } else {

                    if (mediaRecorder != null) {
                        mediaRecorder.stop();
                        mediaRecorder.release();
                        mediaRecorder = null;
                    }

                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }

                    stopRecordingImage.setVisibility(View.GONE);
                    btnRecord.setEnabled(true);
                    playRecordingImage.setVisibility(View.VISIBLE);
                    imageMicAnim.setVisibility(View.GONE);
                    imageMic.setVisibility(View.VISIBLE);
                    tvInstText.setText("You can listen your recording by clicking on the play button");

                }


            }
        });


        playRecordingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                stopPlayingRecording.setVisibility(View.VISIBLE);
                tvInstText.setText("You can stop your recording by clicking on the pause button");
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                Toast.makeText(ActivityRecordAudio.this, "Recording Playing",
                        Toast.LENGTH_LONG).show();


            }
        });


        stopPlayingRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnRecord.setVisibility(View.VISIBLE);
                btnUpload.setVisibility(View.VISIBLE);
                playRecordingImage.setVisibility(View.VISIBLE);
                stopPlayingRecording.setVisibility(View.GONE);
                tvInstText.setText("You can listen your recording by clicking on the play button or you can upload");

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }


            }
        });

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void listener() {

        backArrowImage.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnRecord.setOnClickListener(this);

    }

    private void initViewsHere() {

        imageMic = findViewById(R.id.imageMic);
        imageMicAnim = findViewById(R.id.imageMicAnim);
        backArrowImage = findViewById(R.id.backArrowImage);

        stopRecordingImage = findViewById(R.id.stopRecordingImage);
        playRecordingImage = findViewById(R.id.playRecordingImage);
        stopPlayingRecording = findViewById(R.id.stopPlayingRecording);

        tvInstText = findViewById(R.id.tvInstText);

        btnRecord = findViewById(R.id.btnRecord);
        btnUpload = findViewById(R.id.btnUpload);
        tvRecordAT = findViewById(R.id.tvRecordAT);
        tvRecordAT.setTypeface(tvRecordAT.getTypeface(), Typeface.BOLD);

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.RECORD_AUDIO, WRITE_EXTERNAL_STORAGE) // ask single or multiple permission once
                .subscribe(granted -> {
                    if (granted) {
                        // All requested permissions are granted
                    } else {
                        // At least one permission is denied
                    }
                });


    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.backArrowImage:

                if (mediaRecorder != null) {
                    mediaRecorder.stop();
                }

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }

                finish();
                break;


            case R.id.btnRecord:

                counter = 0;

                btnRecord.setVisibility(View.GONE);
                btnUpload.setVisibility(View.GONE);
                stopRecordingImage.setVisibility(View.VISIBLE);
                playRecordingImage.setVisibility(View.GONE);
                imageMic.setVisibility(View.GONE);
                imageMicAnim.setVisibility(View.VISIBLE);
                stopPlayingRecording.setVisibility(View.GONE);
                captureAudio();

                break;


            case R.id.btnUpload:


                loadingProgress();
                uploadAudioApi(AudioSavePathInDevice);


                break;
        }

    }

    private void uploadAudioApi(String audioSavePathInDevice) {


        if (audioSavePathInDevice != null) {


            File file = new File(audioSavePathInDevice);
            final RequestBody reqFile = RequestBody.create(MediaType.parse("audio/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile); // NOTE: upload here is the name which is the file name


            HttpModule.provideRepositoryService().uploadAudio(Hawk.get("GET_TOKEN"), body).enqueue(new Callback<UploadAudioResume>() {
                @Override
                public void onResponse(Call<UploadAudioResume> call, Response<UploadAudioResume> response) {


                    if (response.body() != null && response.body().getError() == 0) {

                        mProgressDialog.dismiss();
                        Toast.makeText(ActivityRecordAudio.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        mProgressDialog.dismiss();
                        Toast.makeText(ActivityRecordAudio.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();

                    }


                }

                @Override
                public void onFailure(Call<UploadAudioResume> call, Throwable t) {
                    mProgressDialog.dismiss();
                    System.out.println("ActivityRecordAudio.onFailure " + t.toString());

                }
            });
        } else {
            mProgressDialog.dismiss();
            Toast.makeText(ActivityRecordAudio.this, "Please record the audio first and then try to upload", Toast.LENGTH_LONG).show();

        }


    }

    private void loadingProgress() {

        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();


    }

    private void captureAudio() {


        if (checkPermission()) {

            AudioSavePathInDevice =
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                            CreateRandomAudioFileName(5) + "AudioRecording.mp3";

            MediaRecorderReady();

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            countTimer();

            Toast.makeText(ActivityRecordAudio.this, "Recording started",
                    Toast.LENGTH_LONG).show();
        } else {
            requestPermission();
        }


    }

    private void countTimer() {


        countDownTimer = new CountDownTimer(1200000, 1000) {
            public void onTick(long millisUntilFinished) {

                long millisElapsed = 1200000 - millisUntilFinished;
                long minutes = (millisElapsed / 1000) / 60;
                long seconds = (millisElapsed / 1000) % 60;

                tvInstText.setText(String.format("%02d:%02d", minutes, seconds));


                String totalTime = String.format("%02d:%02d", minutes, seconds);
                System.out.println("ActivityRecordAudio.onTick " + totalTime);

                if (totalTime.equalsIgnoreCase("01:59")) {

                    if (mediaRecorder != null) {
                        mediaRecorder.stop();
                    }

                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    imageMicAnim.setVisibility(View.GONE);
                    imageMic.setVisibility(View.VISIBLE);
                    stopRecordingImage.setVisibility(View.GONE);
                    playRecordingImage.setVisibility(View.VISIBLE);
                    tvInstText.setText("You can listen your recording by clicking on the play button");
                }

                timeConsumed++;
                counter++;

                System.out.println("ActivityRecordAudio.onTick " + counter + "weeeeeeeewewewewewewewew" + timeConsumed);
            }

            public void onFinish() {

                countDownTimer.cancel();
                stopRecordingImage.setVisibility(View.GONE);
                playRecordingImage.setVisibility(View.VISIBLE);


            }
        }.start();


    }


    public void MediaRecorderReady() {

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setMaxDuration(1200000);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);


        System.out.println("ActivityRecordAudio.MediaRecorderReady " + AudioSavePathInDevice);
    }


    public String CreateRandomAudioFileName(int string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < string) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++;
        }
        return stringBuilder.toString();
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(ActivityRecordAudio.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);


        AudioSavePathInDevice =
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                        CreateRandomAudioFileName(5) + "AudioRecording.mp3";

        MediaRecorderReady();

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        countTimer();

        Toast.makeText(ActivityRecordAudio.this, "Recording started",
                Toast.LENGTH_LONG).show();


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(ActivityRecordAudio.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ActivityRecordAudio.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }


    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }


}
