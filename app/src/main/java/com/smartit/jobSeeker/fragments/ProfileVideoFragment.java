package com.smartit.jobSeeker.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.exoplayer2.util.Assertions;
import com.karan.churi.PermissionManager.PermissionManager;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import com.smartit.jobSeeker.MessageEvent;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.activities.link.ActivityVideoInterview2;
import com.smartit.jobSeeker.apiResponse.profileVideoForFirstTime.ProfileVideo;
import com.smartit.jobSeeker.apiResponse.recordedVideo.RecordedVideo;
import com.smartit.jobSeeker.demo.PlayerActivity;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.smartit.jobSeeker.upload.ProgressRequestBody;
import com.squareup.picasso.Picasso;

import net.alhazmy13.mediapicker.Video.VideoPicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tcking.github.com.giraffeplayer2.GiraffePlayer;
import tcking.github.com.giraffeplayer2.VideoInfo;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class ProfileVideoFragment extends Fragment implements View.OnClickListener, ProgressRequestBody.UploadCallbacks {

    private ImageView imageView, imageView2;
    private Context mContext;
    private View mView;
    private ProgressBar progressBar;
    //    private WebView mWebView;
    private LinearLayout linearRecordVideo, linearUploadVideo;
    private String VIDEO_URL;
    private ProgressDialog mProgressDialog;

    private TextView tvUploadVideo, tvRecordVideo;


    private static final int VIDEO_CAPTURE_OR_RECORDED = 101;
    private static final int VIDEO_UPLOADED_FROM_GALLERY = 222;
    private PermissionManager permission;

    private NoInternetDialog noInternetDialog;


    private VideoView simpleVideoView;
    private MediaController mediaControls;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mView = inflater.inflate(R.layout.profile_video_fragment, container, false);

        mProgressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        progressBar = mView.findViewById(R.id.tvProgress);

        // GiraffeCompressor.init(getActivity());

        if (mediaControls == null) {
            // create an object of media controller class
            mediaControls = new MediaController(getActivity());
            mediaControls.setAnchorView(simpleVideoView);
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        permission = new PermissionManager() {
        };
        permission.checkAndRequestPermissions(getActivity());

        findTheIdsFromHere(mView);
        eventListenerGoesHere();

//        loadingProgress();
        profileVideoForFirstTime();


        return mView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(CalligraphyContextWrapper.wrap(context));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }


    private void eventListenerGoesHere() {

        linearRecordVideo.setOnClickListener(this);
        linearUploadVideo.setOnClickListener(this);

    }

    private void loadingProgress() {

        mProgressDialog.setMessage("Uploading..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();


    }

    private void profileVideoForFirstTime() {


        // NOTE IF YOU ARE HITTING THE API THEN NEED TO VISIBLE THESE IMAGES

//        imageView.setVisibility(View.VISIBLE);
//        imageView2.setVisibility(View.VISIBLE);

        HttpModule.provideRepositoryService().profileVideoForJobSeeker(getArguments().getString("token")).enqueue(new Callback<ProfileVideo>() {
            @Override
            public void onResponse(Call<ProfileVideo> call, Response<ProfileVideo> response) {


                if (response.body() != null && response.body().getError() == 0) {


                    mProgressDialog.dismiss();

//                    mWebView.setWebViewClient(new WebViewClient());
//                    mWebView.getSettings().setJavaScriptEnabled(true);
//                    mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//                    mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
//                    mWebView.getSettings().setMediaPlaybackRequiresUserGesture(true);
//                    mWebView.setWebChromeClient(new WebChromeClient());

                    Picasso.get().load(response.body().getData().getPoster()).into(imageView2);

                    if (response.body().getData().getHls().equalsIgnoreCase("")) { // TODO HLS BALANK THEN FILE URL

                        VIDEO_URL = response.body().getData().getFileurl();  // FILE URL
                    } else if (!response.body().getData().getHls().equalsIgnoreCase("")) { // TODO NOT BLANK THEN HLS ITSELF
                        VIDEO_URL = response.body().getData().getHls();// HLS URL
                    } else if (response.body().getData().getHls().equalsIgnoreCase("") && response.body().getData().getFileurl().equalsIgnoreCase("")) {

                        VIDEO_URL = "";
                    }


//                    VIDEO_URL = response.body().getData().getFileurl();

//                    VIDEO_URL = response.body().getData().getHls();


                } else {

                    mProgressDialog.dismiss();
                    Toast.makeText(getActivity(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();


                }

            }

            @Override
            public void onFailure(Call<ProfileVideo> call, Throwable t) {

                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private void findTheIdsFromHere(View mView) {

//        mWebView = mView.findViewById(R.id.mWebView);
        simpleVideoView = mView.findViewById(R.id.mWebView);

        linearRecordVideo = mView.findViewById(R.id.linearRecordVideo);
        linearUploadVideo = mView.findViewById(R.id.linearUploadVideo);

        imageView = mView.findViewById(R.id.imageView);
        imageView2 = mView.findViewById(R.id.imageView2);


        tvRecordVideo = mView.findViewById(R.id.tvRecordVideo);
        tvUploadVideo = mView.findViewById(R.id.tvUploadVideo);

        tvUploadVideo.setTypeface(tvUploadVideo.getTypeface(), Typeface.BOLD);
        tvRecordVideo.setTypeface(tvRecordVideo.getTypeface(), Typeface.BOLD);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mWebView.loadUrl(VIDEO_URL);


                // set the media controller for video view
//                simpleVideoView.setMediaController(mediaControls);
//                // set the uri for the video view
//                simpleVideoView.setVideoURI(Uri.parse(VIDEO_URL));
//                // start a video
//                simpleVideoView.start();

                if (VIDEO_URL != null) {

                    if (VIDEO_URL.equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "No Video Found", Toast.LENGTH_LONG).show();
                    } else {

//                    if (VIDEO_URL != null) {

                        Sample sample = new UriSample(
                                "Super speed (PlayReady)",
                                null,
                                Uri.parse(VIDEO_URL),
                                null,
                                null,
                                null);

                        startActivity(
                                sample.buildIntent(
                                        getActivity(),
                                        false,
                                        PlayerActivity.ABR_ALGORITHM_DEFAULT));


//                    } else {
//
//                        Toast.makeText(getActivity(), "Please upload the video first", Toast.LENGTH_LONG).show();
//                    }

                    }
                } else {
                    Toast.makeText(getActivity(), "Please upload the video first", Toast.LENGTH_LONG).show();
                }

//                if (VIDEO_URL != null) {
//
//
////
////                    VideoInfo videoInfo = new VideoInfo(Uri.parse(VIDEO_URL))
////                            .setTitle("Profile Video") //config title
////                            //  .setFullScreenOnly(true)
////                            // .setFullScreenAnimation(true)
////                            .setAspectRatio(VideoInfo.AR_ASPECT_FILL_PARENT) //aspectRatio
////                            .setShowTopBar(false) //s
////                            //how mediacontroller top bar
////                            .setPortraitWhenFullScreen(false);//portrait when full screen
////
////
////                    GiraffePlayer.play(getActivity(), videoInfo);
//
//
//                    Sample sample = new UriSample(
//                            "Super speed (PlayReady)",
//                            null,
//                            Uri.parse(VIDEO_URL),
//                            null,
//                            null,
//                            null);
//
//                    startActivity(
//                            sample.buildIntent(
//                                    getActivity(),
//                                    false,
//                                    PlayerActivity.ABR_ALGORITHM_DEFAULT));
//
//
//                } else {
//
//                    Toast.makeText(getActivity(), "Please upload the video first", Toast.LENGTH_LONG).show();
//                }


//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(VIDEO_URL));
//                startActivity(intent);

//               imageView.setVisibility(View.GONE);
//                imageView2.setVisibility(View.GONE);


            }
        });


        // implement on completion listener on video view
        simpleVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(getActivity(), "Thank You...!!!", Toast.LENGTH_LONG).show(); // display a toast when an video is completed
            }
        });
        simpleVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(getActivity(), "Oops An Error Occur While Playing Video...!!!", Toast.LENGTH_LONG).show(); // display a toast when an error is occured while playing an video
                return false;
            }
        });


    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.linearRecordVideo:


                Intent intent = new Intent(getActivity(), ActivityVideoInterview2.class);
                startActivity(intent);
                Animatoo.animateFade(getActivity());

//                new VideoPicker.Builder(getActivity())
//                        .mode(VideoPicker.Mode.CAMERA)
//                        .directory(VideoPicker.Directory.DEFAULT)
//                        .extension(VideoPicker.Extension.MP4)
//                        .enableDebuggingMode(true)
//                        .build();


//                recordVideoFromHere();
                break;

            case R.id.linearUploadVideo:

//                uploadVideoFromGallery();
                new VideoPicker.Builder(getActivity())
                        .mode(VideoPicker.Mode.GALLERY)
                        .directory(VideoPicker.Directory.DEFAULT)
                        .extension(VideoPicker.Extension.MP4)
                        .enableDebuggingMode(true)
                        .build();


                break;


        }


    }

    private void uploadVideoFromGallery() {


        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, VIDEO_UPLOADED_FROM_GALLERY);

    }

    private void recordVideoFromHere() {


        Intent camra_intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        File videofile = getpath();
        Uri videouri = Uri.fromFile(videofile);

//        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
//        mediaRecorder.setVideoEncodingBitRate(690000);

        camra_intent.putExtra(MediaStore.EXTRA_OUTPUT, videouri);
        camra_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        camra_intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 120);
        startActivityForResult(camra_intent, VIDEO_CAPTURE_OR_RECORDED);


    }


    public File getpath() {

        File folder = new File("storage/emulated/0/JOB_SEEKER");

        // Check that the SDCard is mounted
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "MyCameraVideo");


        if (!folder.exists()) {
            folder.mkdir();
        }

        File videofile = new File(folder, "JOB_SEEKER_VIDEO.mp4");
        return videofile;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {


//        if (resultCode == RESULT_OK) {
//
//            if (requestCode == VIDEO_CAPTURE_OR_RECORDED) {
//
//                System.out.println("ProfileVideoFragment.onActivityResult " + Objects.requireNonNull(data.getData()).getPath());
//
//                loadingProgress();
//                recordedVideo(data.getData().getPath());
//            }
//
//            if (requestCode == VIDEO_UPLOADED_FROM_GALLERY) {
//
//                Uri selectedImage = data.getData();
//                String[] filePath = {MediaStore.Video.Media.DATA};
//                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
//                c.moveToFirst();
//
//                int columnIndex = c.getColumnIndex(filePath[0]);
//                String videoPath = c.getString(columnIndex);
//                c.close();
//
//                System.out.println("ProfileVideoFragment.onActivityResult " + videoPath);
//
//                try {
//                    loadingProgress();
//                    recordedVideo(videoPath);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//        } else {
//
//            TastyToast.makeText(getActivity(), "Cancelled", TastyToast.LENGTH_SHORT, 3).show();
//        }


        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {

            List<String> mPaths = data.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH);
            loadingProgress();
            recordedVideo(mPaths.get(0));

        } else if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {

            List<String> mPaths = data.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH);

            // compressVideo(mPaths.get(0));
//            recordedVideo(mPaths.get(0));
//            recordedVideo(mPaths.get(0));
        }


    }

//    private void compressVideo(String s) {
//
//
//        GiraffeCompressor.create(GiraffeCompressor.TYPE_FFMPEG) //two implementations: mediacodec and ffmpeg,default is mediacodec
//                .input(s) //set video to be compressed
//                .output("/storage/emulated/0/mediapicker/videos/temp.mp4") //set compressed video output
//                .bitRate(2073600)//set bitrate 码率
//                .resizeFactor(1.0f)//set video resize factor 分辨率缩放,默认保持原分辨率
////                    .watermark(Environment.getExternalStorageDirectory().toString() + "/myApp/")//add watermark(take a long time) 水印图片(需要长时间处理)
//                .ready()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<GiraffeCompressor.Result>() {
//                    @Override
//                    public void onCompleted() {
//
//                        System.out.println("ProfileVideoFragment.onCompleted ");
//
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        System.out.println("ProfileVideoFragment.onError " + e.getMessage());
//
//                    }
//
//                    @Override
//                    public void onNext(GiraffeCompressor.Result s) {
//
//
//                        System.out.println("ProfileVideoFragment.onNext FIle" + s.getOutput());
//                        System.out.println("ProfileVideoFragment.onNext Compresssede taken time " + String.format("" + s.getCostTime(), s.getOutput()));
//                        String compressedSize = s.getOutput();
//                        System.out.println("ProfileVideoFragment.onNext " + compressedSize);
//
//                        mProgressDialog.dismiss();
//                        System.out.println("ProfileVideoFragment.onNext " + Formatter.formatFileSize(getActivity(), new File(s.getOutput()).length()));
//                        loadingProgress();
//                        recordedVideo(s.getOutput());
//
//                    }
//                });
//
//
//    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        loadingProgress();
        recordedVideo(event.getPath());
        /* Do something */
    }


    private void recordedVideo(String path) {

        File file = new File(path);
        // final RequestBody reqFile = RequestBody.create(MediaType.parse("video/*"), file);
        // MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);


        ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
        MultipartBody.Part body1 = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
        // RequestBody token = RequestBody.create(MediaType.parse("text/plain"), (String) Hawk.get("GET_TOKEN"));
        HttpModule.provideRepositoryService().recordedVideo((String) Hawk.get("GET_TOKEN"), body1).enqueue(new Callback<RecordedVideo>() {
            @Override
            public void onResponse(Call<RecordedVideo> call, Response<RecordedVideo> response) {


                if (response.body() != null && response.body().getError() == 0) {

//                    profileVideoForFirstTime(); // YOU WILL HIT THIS API TO GET THE SAME DATA
                    mProgressDialog.dismiss();

                    Picasso.get().load(response.body().getPath().getPoster()).into(imageView2);
                    VIDEO_URL = response.body().getPath().getUploadedFileUrl();


                    imageView.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.VISIBLE);

                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                } else {

                    mProgressDialog.dismiss();
                    Toast.makeText(getActivity(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RecordedVideo> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("ProfileVideoFragment.onFailure " + t.toString());
            }
        });

//        HttpModule.provideRepositoryService().recordedVideo((String) Hawk.get("GET_TOKEN"), body).enqueue(new Callback<RecordedVideo>() {
//            @Override
//            public void onResponse(Call<RecordedVideo> call, Response<RecordedVideo> response) {
//
//
//                if (response.body() != null && response.body().getError() == 0) {
//
////                    profileVideoForFirstTime(); // YOU WILL HIT THIS API TO GET THE SAME DATA
//                    mProgressDialog.dismiss();
//
//                    Picasso.get().load(response.body().getPath().getPoster()).into(imageView2);
//                    VIDEO_URL = response.body().getPath().getUploadedFileUrl();
//
//                    imageView.setVisibility(View.VISIBLE);
//                    imageView2.setVisibility(View.VISIBLE);
//
//                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
//
//                } else {
//
//                    mProgressDialog.dismiss();
//                    Toast.makeText(getActivity(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<RecordedVideo> call, Throwable t) {
//
//                mProgressDialog.dismiss();
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
//                System.out.println("ProfileVideoFragment.onFailure " + t.toString());
//            }
//        });


    }


    @Override
    public void onProgressUpdate(int percentage) {

        // progressBar.setProgress(progressStatus);
        mProgressDialog.setMessage("Uploading.. " + percentage + "%");
        mProgressDialog.setProgress(percentage);
        progressBar.setProgress(percentage);
    }

    @Override
    public void onError() {
        // progressBar.setText("Uploaded Failed!");
        // progressBar.setTextColor(Color.RED);

        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFinish() {
        progressBar.setVisibility(View.GONE);
        // Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
        // progressBar.setText("Uploaded Successfully");
    }

    @Override
    public void uploadStart() {
        progressBar.setVisibility(View.VISIBLE);

        mProgressDialog.setMessage("Uploading.. " + 0 + "%");
        //progressBar.setText("0%");
        progressBar.setProgress(0);

        Toast.makeText(getApplicationContext(), "Upload started", Toast.LENGTH_SHORT).show();
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

}
