package com.smartit.jobSeeker.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.getAudioResume.GetAudioResume;
import com.smartit.jobSeeker.apiResponse.uploadAudioResume.UploadAudioResume;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.AudioPickActivity;
import com.vincent.filepicker.filter.entity.AudioFile;
import com.vincent.filepicker.filter.entity.NormalFile;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.vincent.filepicker.Constant.REQUEST_CODE_PICK_AUDIO;
import static com.vincent.filepicker.activity.AudioPickActivity.IS_NEED_RECORDER;


public class ActivityProfileAudio extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener {


    private ImageView imagePlay, imagePause;
    private TextView tvTotalTime, tvTitle, tvInst;
    private SeekBar mSeekBar;


    private MediaPlayer mPlayer;
    private Handler mHandler;
    private Runnable mRunnable;


    private ImageView backArrowImage;
    private Button btnUpload, btnRecord;

    private String AUDIO_RESUME_URL;
    final static int RQS_OPEN_AUDIO_MP3 = 1;

    private ProgressDialog mProgressDialog, mProgressDialogtest;


    private final static int FILE_REQUEST_CODE = 1;
    private ArrayList<MediaFile> mediaFiles = new ArrayList<>();


    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private TextView tvSuccessMessage;
    private Button buttonOk;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_audio);
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        mProgressDialogtest = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
//        mPlayer = new MediaPlayer();
//        getAudioResumeApi();
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        initViewsHere();
        listener();


    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadingProgress();

        imagePause.setVisibility(View.GONE);
        imagePlay.setVisibility(View.VISIBLE);

        getAudioResumeApi();


        imagePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgress();

                if (AUDIO_RESUME_URL != null) {

                    imagePause.setVisibility(View.VISIBLE);
                    imagePlay.setVisibility(View.GONE);

                    // If media player another instance already running then stop it first

//                    mPlayer = new MediaPlayer();
                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


                    if (mPlayer.isPlaying()) {
                        mPlayer.pause();
                    }

                    mPlayer.start();

//                    Get the current audio stats


                    // Initialize the seek bar
                    initializeSeekBar();
                    mProgressDialog.dismiss();

                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(ActivityProfileAudio.this, "No audio available", Toast.LENGTH_SHORT).show();
                }


            }
        });


        imagePause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imagePlay.setVisibility(View.VISIBLE);
                imagePause.setVisibility(View.GONE);
                stopPlaying();

            }
        });

        playWithAudio();

    }

    private void loadingProgress() {

        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

    }

    private void getAudioResumeApi() {


        HttpModule.provideRepositoryService().getAudioResumeApi(Hawk.get("GET_TOKEN")).enqueue(new Callback<GetAudioResume>() {
            @Override
            public void onResponse(Call<GetAudioResume> call, Response<GetAudioResume> response) {

                if (response.body() != null && response.body().getError() == 0) {


                    AUDIO_RESUME_URL = response.body().getData().getAudioResume();
                    Log.e("URL-->", AUDIO_RESUME_URL);

                    if (mPlayer == null) {
                        mPlayer = new MediaPlayer();

                    }

                    try {

                        mPlayer.setDataSource(AUDIO_RESUME_URL);


                    } catch (IllegalArgumentException e) {
                        System.out.println("ActivityProfileAudio.onClick " + e);
                    } catch (SecurityException e) {
                        System.out.println("ActivityProfileAudio.onClick " + e);
                    } catch (IllegalStateException e) {
                        System.out.println("ActivityProfileAudio.onClick " + e);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        mPlayer.setOnPreparedListener(ActivityProfileAudio.this);
                        mPlayer.prepareAsync();

//                        mPlayer.prepare();
                    } catch (IllegalStateException e) {
                        System.out.println("ActivityProfileAudio.onClick " + e);
                    }

                } else {

                    mProgressDialog.dismiss();
                    Toast.makeText(ActivityProfileAudio.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<GetAudioResume> call, Throwable t) {
                mProgressDialog.dismiss();
                System.out.println("ActivityProfileAudio.onFailure " + t.getMessage());
            }
        });


    }

    private void listener() {

        backArrowImage.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnRecord.setOnClickListener(this);


    }

    private void initViewsHere() {

        // Initialize the handler
        mHandler = new Handler();

        backArrowImage = findViewById(R.id.backArrowImage);
        btnUpload = findViewById(R.id.btnUpload);
        btnRecord = findViewById(R.id.btnRecord);

        imagePlay = findViewById(R.id.imagePlay);
        imagePause = findViewById(R.id.imagePause);
        tvTotalTime = findViewById(R.id.tvTotalTime);
        mSeekBar = findViewById(R.id.mSeekBar);
        tvTitle = findViewById(R.id.tvTitle);
        tvInst = findViewById(R.id.tvInst);
        tvTitle.setTypeface(tvTitle.getTypeface(), Typeface.BOLD);
        tvInst.setTypeface(tvInst.getTypeface(), Typeface.BOLD);


    }

    private void playWithAudio() {

 /*
            SeekBar.OnSeekBarChangeListener
                A callback that notifies clients when the progress level has been changed. This
                includes changes that were initiated by the user through a touch gesture or
                arrow key/trackball as well as changes that were initiated programmatically.
        */

        // Set a change listener for seek bar
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /*
                void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser)
                    Notification that the progress level has changed. Clients can use the fromUser
                    parameter to distinguish user-initiated changes from those that occurred programmatically.

                Parameters
                    seekBar SeekBar : The SeekBar whose progress has changed
                    progress int : The current progress level. This will be in the range min..max
                                   where min and max were set by setMin(int) and setMax(int),
                                   respectively. (The default values for min is 0 and max is 100.)
                    fromUser boolean : True if the progress change was initiated by the user.
            */
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mPlayer != null && b) {
                    /*
                        void seekTo (int msec)
                            Seeks to specified time position. Same as seekTo(long, int)
                            with mode = SEEK_PREVIOUS_SYNC.

                        Parameters
                            msec int: the offset in milliseconds from the start to seek to

                        Throws
                            IllegalStateException : if the internal player engine has not been initialized
                    */
                    mPlayer.seekTo(i * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.backArrowImage:
                stopPlaying();
                finish();
                break;

            case R.id.btnUpload:

//                stopPlaying();
                recordOnlyCase();

//                Intent intent = new Intent();
//                intent.setType("audio/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(
//                        intent, "Open Audio (mp3) file"), RQS_OPEN_AUDIO_MP3);


//                Intent intent3 = new Intent(this, AudioPickActivity.class);
//                intent3.putExtra(IS_NEED_RECORDER, true);
//                intent3.putExtra(Constant.MAX_NUMBER, 9);
////                intent3.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                startActivityForResult(intent3, REQUEST_CODE_PICK_AUDIO);


                Intent intent = new Intent(ActivityProfileAudio.this, FilePickerActivity.class);
                MediaFile file = null;

                for (int i = 0; i < mediaFiles.size(); i++) {

                    if (mediaFiles.get(i).getMediaType() == MediaFile.TYPE_AUDIO) {
                        file = mediaFiles.get(i);
                    }
                }

                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                        .setCheckPermission(true)
                        .setShowImages(false)
                        .setShowVideos(false)
                        .setShowAudios(true)
                        .setSingleChoiceMode(true)
                        .setSelectedMediaFile(file)
                        .build());
                startActivityForResult(intent, FILE_REQUEST_CODE);


                break;

            case R.id.btnRecord:

//                stopPlaying();
                recordOnlyCase();
//                askingPermissionForCamera();
                Intent intent11 = new Intent(ActivityProfileAudio.this, ActivityRecordAudio.class);
                startActivity(intent11);

                break;

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopPlaying();
    }

    private void initializeSeekBar() {

        mSeekBar.setMax(mPlayer.getDuration() / 1000);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (mPlayer != null) {
                    int mCurrentPosition = mPlayer.getCurrentPosition() / 1000; // In milliseconds
                    mSeekBar.setProgress(mCurrentPosition);
                    getAudioStats();
                }
                mHandler.postDelayed(mRunnable, 1000);
            }
        };
        mHandler.postDelayed(mRunnable, 1000);


    }



     /*
        int getDuration ()
            Gets the duration of the file.

        Returns
            int the duration in milliseconds, if no duration is available
            (for example, if streaming live content), -1 is returned.
    */

    /*
        int getCurrentPosition ()
            Gets the current playback position.

        Returns
            int the current position in milliseconds
    */

    private void getAudioStats() {

        int duration = mPlayer.getDuration() / 1000; // In milliseconds
        int due = (mPlayer.getDuration() - mPlayer.getCurrentPosition()) / 1000;
        System.out.println("duration   " + mPlayer.getDuration());
        int pass = duration - due;


//        mPass.setText("" + pass + " seconds");
//        mDuration.setText("" + duration + " seconds");

        int mi = due / 60;
        int sec = due - mi * 60;

        if (mi == 0 && sec == 0) {


            mPlayer.stop();
            mPlayer = null;
            imagePause.setVisibility(View.GONE);
            imagePlay.setVisibility(View.VISIBLE);
            tvTotalTime.setText("");
        }
        tvTotalTime.setText(String.format("%02d:%02d", mi, sec));


    }

    private void stopPlaying() {

        // If media player is not null then try to stop it
        if (mPlayer != null) {
            mPlayer.pause();
//            mPlayer.stop();
//            mPlayer.release();
//            mPlayer = null;
//            Toast.makeText(ActivityProfileAudio.this, "Stop playing.", Toast.LENGTH_SHORT).show();
            if (mHandler != null) {
                mHandler.removeCallbacks(mRunnable);
            }
        }


    }


    private void recordOnlyCase() {

        // If media player is not null then try to stop it
        if (mPlayer != null) {
            mPlayer.pause();
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            if (mHandler != null) {
                mHandler.removeCallbacks(mRunnable);
            }
        }

    }


    @Override
    protected void onPause() {
        super.onPause();

        if (mPlayer != null && mPlayer.isPlaying()) {
            stopPlaying();
            imagePause.setVisibility(View.GONE);
            imagePlay.setVisibility(View.VISIBLE);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FILE_REQUEST_CODE) { //RQS_OPEN_AUDIO_MP3

//                Uri audioFileUri = data.getData();

//                String newPath = data.getData().getPath();
//
//                System.out.println("ActivityProfileAudio.onActivityResult " + newPath);
//
//                String currentString = newPath;
//                String[] separated = currentString.split(":");
//                String separated1 = separated[1].trim();
//
//                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + separated1;
//
//
//                final Uri uri = Uri.parse(Environment.getExternalStorageDirectory() + newPath);


//                ArrayList<AudioFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_AUDIO);
//
//                if (list.size() > 0) {
//                    uploadAudioResume(list.get(0));
//                } else {
//                    Toast.makeText(ActivityProfileAudio.this, "No audio selected", Toast.LENGTH_SHORT).show();
//
//                }


//
//                if (requestCode == FILE_REQUEST_CODE
//                        && resultCode == RESULT_OK
//                        && data != null) {
//                    mediaFiles.clear();
//                    mediaFiles.addAll(data.<MediaFile>getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES));
//


                ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);

                //Do something with files

                if (files.size() > 0) {

                    loadingProgressTest();

                    uploadAudioResume(files.get(0).getPath());

                } else {
                    Toast.makeText(ActivityProfileAudio.this, "Audio file not found", Toast.LENGTH_SHORT).show();
                }


            }
        }
    }

    private void loadingProgressTest() {

        mProgressDialogtest.setMessage("Wait");
        mProgressDialogtest.setCancelable(false);
        mProgressDialogtest.show();

    }

    private void uploadAudioResume(String audioPath) {

        File file = new File(audioPath);

        final RequestBody reqFile = RequestBody.create(MediaType.parse("audio/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile); // NOTE: upload here is the name which is the file name

        HttpModule.provideRepositoryService().uploadAudio(Hawk.get("GET_TOKEN"), body).enqueue(new Callback<UploadAudioResume>() {
            @Override
            public void onResponse(Call<UploadAudioResume> call, Response<UploadAudioResume> response) {

                if (response.body() != null && response.body().getError() == 0) {


                    dialogSignOk(response.body().getMessage());
                    mProgressDialogtest.dismiss();

//                    Toast.makeText(ActivityProfileAudio.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                } else {
                    mProgressDialogtest.dismiss();
                    Toast.makeText(ActivityProfileAudio.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<UploadAudioResume> call, Throwable t) {

                mProgressDialogtest.dismiss();
                System.out.println("ActivityProfileAudio.onFailure " + t.toString());

            }
        });


    }

    private void dialogSignOk(String message) {


        LayoutInflater li = LayoutInflater.from(ActivityProfileAudio.this);
        View dialogView = li.inflate(R.layout.dialog_feedback, null);

        findTheIdsHere(dialogView, message);

        alertDialogBuilder = new AlertDialog.Builder(ActivityProfileAudio.this);
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
                finish();
                startActivity(getIntent());

            }
        });


    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        getAudioStats();
        mProgressDialog.dismiss();

    }
}
