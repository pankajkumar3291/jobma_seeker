package com.smartit.jobSeeker.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.handShakeForDashboard.HandshakeForDashboard;
import com.smartit.jobSeeker.apiResponse.mobDetail.MobDetail;
import com.smartit.jobSeeker.apiResponse.uploadProfilePicture.UploadProfile;
import com.smartit.jobSeeker.eventbus.MessageEventProfile;
import com.smartit.jobSeeker.fragments.ApplicationHistoryFragment;
import com.smartit.jobSeeker.fragments.HomeFragment;
import com.smartit.jobSeeker.fragments.InvitationFragment;
import com.smartit.jobSeeker.fragments.MyProfileFragment;
import com.smartit.jobSeeker.fragments.ProfileVideoFragment;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.smartit.jobSeeker.sharedPreferenceUtils.SharedPrefsHelper;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
public class ActivityDashboard extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = ActivityDashboard.class.getSimpleName();
    private ReactiveLocationProvider locationProvider;
    private CompositeDisposable compositeDisposable;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private LinearLayout linearDashboard, linearInvitations, linearApplicationHistory, linearProfileVideo, linearLogout;
    private TextView tvDashboard, tvInvitations, tvApplicationHistory, tvProfileVideo, tvLogout, userName;
    private FrameLayout frameContainer;
    private boolean doubleBackToExitPressedOnce = false;
    private TextView tvTitleToolbar;

    private CircleImageView userProfileImage;
    private ProgressDialog mProgressDialog;

    private SharedPrefsHelper sharedPrefsHelper;
    private NoInternetDialog noInternetDialog;

    private RelativeLayout linearSettingsLayout, linearContactUs;
    private LinearLayout linearForCPQR, linearMainContact, liJobAlert;
    private ImageView imageMinus, imagePlus, imagePlusCont, imageMinusCont;
    private boolean isSettingsClicked = true;
    private boolean isContactUsClicked = true;
    private TextView tvChangePassword, tvQRCode, tvPrivacySettings;

    private LinearLayout liReportAnIssue, liTrackReportedIssue, liProfileAudi, liMyProfile, linearJobSearch;

    private GoogleSignInClient mGoogleSignInClient;
    private boolean isLoggedIn;
    private String token;

    private TextView tvJobSearch, tvJobAlert, tvMyProfile, tvProfileAudio, tvSettings, tvContactUs;

    private boolean permissionChecked;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        compositeDisposable = new CompositeDisposable();
        locationProvider = new ReactiveLocationProvider(this);
        token = getIntent().getStringExtra("token");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();

        ActivityDashboardPermissionsDispatcher.needPersmissionsToUpdateDeviceWithPermissionCheck(this);

        Hawk.init(this).build();
        noInternetDialog = new NoInternetDialog.Builder(this).build();

        findTheIdsFromHere();
        eventListnerGoesHere();


        handshakeApiForProfilePicAndName();

        addHomeFragmentsFromHere(new HomeFragment());


    }


    public void refreshFragment() {

        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("token", token);
        homeFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, homeFragment).commit();
//        Toast.makeText(ActivityDashboard.this, "Refresh", Toast.LENGTH_SHORT).show();
    }


    public void refreshFragmentFromInvitation() {

        InvitationFragment invitationFragment = new InvitationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("token", token);
        invitationFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, invitationFragment).commit();
//        Toast.makeText(ActivityDashboard.this, "Refresh", Toast.LENGTH_SHORT).show();
    }


    private void mobDetailApi(String token, String state, String country, String zipcode, String city, String lat, String lng) {


        Field[] fields = Build.VERSION_CODES.class.getFields();
        /*        String osName = fields[Build.VERSION.SDK_INT + 1].getName();*/
        // Log.d("Android OsName:", osName);

        //    System.out.println("ActivityDashboard.mobDetailApi " + osName);

        String model = Build.MODEL;
        String reqString = Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();


        HttpModule.provideRepositoryService().mobDetail(getIntent().getStringExtra("token"), model, reqString, state, country, zipcode, city, lat, "Android", lng, String.valueOf(Build.VERSION.SDK_INT), token).enqueue(new Callback<MobDetail>() {
            @Override
            public void onResponse(Call<MobDetail> call, Response<MobDetail> response) {

                System.out.println("ActivityDashboard.onResponse " + response.body().getMessage());

            }

            @Override
            public void onFailure(Call<MobDetail> call, Throwable t) {

                System.out.println("ActivityDashboard.onFailure " + t.toString());
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


    private void handshakeApiForProfilePicAndName() {


        HttpModule.provideRepositoryService().handShakeForJobSeeker(getIntent().getStringExtra("token")).enqueue(new Callback<HandshakeForDashboard>() {
            @Override
            public void onResponse(Call<HandshakeForDashboard> call, Response<HandshakeForDashboard> response) {


                if (response.body() != null && response.body().getError() == 0) {


                    if (response.body().getData().getImageurl().equalsIgnoreCase("")) {

                        Picasso.get()
                                .load(R.drawable.no_image_available)
                                .placeholder(R.drawable.no_image_available)
                                .error(R.drawable.no_image_available)
                                .into(userProfileImage);

                    } else {

                        EventBus.getDefault().post(new MessageEventProfile(response.body().getData().getImageurl()));
                        Picasso.get().load(response.body().getData().getImageurl()).into(userProfileImage);
                        userName.setText(response.body().getData().getName() + " " + response.body().getData().getLastname());

                    }


                } else {

                    System.out.println("ActivityDashboard.onResponse " + Objects.requireNonNull(response.body()).getMessage());

                }


            }

            @Override
            public void onFailure(Call<HandshakeForDashboard> call, Throwable t) {

                TastyToast.makeText(ActivityDashboard.this, t.getMessage(), TastyToast.LENGTH_SHORT, 3).show();

            }
        });


    }


    private void addHomeFragmentsFromHere(HomeFragment homeFragment) {


        Bundle bundle = new Bundle();
        bundle.putString("token", getIntent().getStringExtra("token"));
        homeFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameContainer, homeFragment, "Home Fragment").commit();


    }

    private void eventListnerGoesHere() {

        linearDashboard.setOnClickListener(this);
        linearInvitations.setOnClickListener(this);
        linearApplicationHistory.setOnClickListener(this);
        linearProfileVideo.setOnClickListener(this);
        linearLogout.setOnClickListener(this);
        userProfileImage.setOnClickListener(this);
        linearSettingsLayout.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);
        tvQRCode.setOnClickListener(this);
        tvPrivacySettings.setOnClickListener(this);
        linearContactUs.setOnClickListener(this);


        liReportAnIssue.setOnClickListener(this);
        liTrackReportedIssue.setOnClickListener(this);
        liJobAlert.setOnClickListener(this);
        liProfileAudi.setOnClickListener(this);
        liMyProfile.setOnClickListener(this);
        linearJobSearch.setOnClickListener(this);

    }

    private void findTheIdsFromHere() {


        mProgressDialog = new ProgressDialog(ActivityDashboard.this, R.style.AppTheme_Dark_Dialog);
        sharedPrefsHelper = new SharedPrefsHelper(ActivityDashboard.this);

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer);

        linearDashboard = findViewById(R.id.linearDashboard);
        linearInvitations = findViewById(R.id.linearInvitations);
        linearApplicationHistory = findViewById(R.id.linearApplicationHistory);
        linearProfileVideo = findViewById(R.id.linearProfileVideo);
        linearLogout = findViewById(R.id.linearLogout);
        liMyProfile = findViewById(R.id.liMyProfile);


        userProfileImage = findViewById(R.id.userProfileImage);
        userName = findViewById(R.id.userName);


        tvDashboard = findViewById(R.id.tvDashboard);
        tvInvitations = findViewById(R.id.tvInvitations);
        tvApplicationHistory = findViewById(R.id.tvApplicationHistory);
        tvProfileVideo = findViewById(R.id.tvProfileVideo);
        tvLogout = findViewById(R.id.tvLogout);

        frameContainer = findViewById(R.id.frameContainer);
        tvTitleToolbar = findViewById(R.id.tvTitleToolbar);
        tvTitleToolbar.setTypeface(tvTitleToolbar.getTypeface(), Typeface.BOLD);

        linearSettingsLayout = findViewById(R.id.linearSettingsLayout);
        linearForCPQR = findViewById(R.id.linearForCPQR);

        imageMinus = findViewById(R.id.imageMinus);
        imagePlus = findViewById(R.id.imagePlus);
        tvChangePassword = findViewById(R.id.tvChangePassword);

        tvQRCode = findViewById(R.id.tvQRCode);
        tvPrivacySettings = findViewById(R.id.tvPrivacySettings);


        linearContactUs = findViewById(R.id.linearContactUs);
        imagePlusCont = findViewById(R.id.imagePlusCont);
        imageMinusCont = findViewById(R.id.imageMinusCont);

        linearMainContact = findViewById(R.id.linearMainContact);

        liReportAnIssue = findViewById(R.id.liReportAnIssue);
        liTrackReportedIssue = findViewById(R.id.liTrackReportedIssue);

        liJobAlert = findViewById(R.id.liJobAlert);
        liProfileAudi = findViewById(R.id.liProfileAudi);

        linearJobSearch = findViewById(R.id.linearJobSearch);

        tvJobSearch = findViewById(R.id.tvJobSearch);
        tvJobAlert = findViewById(R.id.tvJobAlert);
        tvMyProfile = findViewById(R.id.tvMyProfile);
        tvProfileAudio = findViewById(R.id.tvProfileAudio);
        tvSettings = findViewById(R.id.tvSettings);
        tvContactUs = findViewById(R.id.tvContactUs);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
                handshakeApiForProfilePicAndName();
            }
        });


    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.linearDashboard:

                tvDashboard.setTextColor(Color.parseColor("#2DBFD9"));
                tvLogout.setTextColor(Color.parseColor("#000000"));
                tvProfileVideo.setTextColor(Color.parseColor("#000000"));
                tvApplicationHistory.setTextColor(Color.parseColor("#000000"));
                tvInvitations.setTextColor(Color.parseColor("#000000"));

                tvMyProfile.setTextColor(Color.parseColor("#000000"));
                tvJobAlert.setTextColor(Color.parseColor("#000000"));
                tvJobSearch.setTextColor(Color.parseColor("#000000"));
                tvSettings.setTextColor(Color.parseColor("#000000"));
                tvContactUs.setTextColor(Color.parseColor("#000000"));
                tvProfileAudio.setTextColor(Color.parseColor("#000000"));

//                linearDashboard.setBackgroundColor(Color.parseColor("#EDEDED"));

                replaceTheDashboardFragment(new HomeFragment());
                closeDrawerFromHere();


                break;

            case R.id.linearInvitations:


                tvInvitations.setTextColor(Color.parseColor("#2DBFD9"));
                tvDashboard.setTextColor(Color.parseColor("#000000"));
                tvApplicationHistory.setTextColor(Color.parseColor("#000000"));
                tvProfileVideo.setTextColor(Color.parseColor("#000000"));
                tvLogout.setTextColor(Color.parseColor("#000000"));

                tvMyProfile.setTextColor(Color.parseColor("#000000"));
                tvJobAlert.setTextColor(Color.parseColor("#000000"));
                tvJobSearch.setTextColor(Color.parseColor("#000000"));
                tvSettings.setTextColor(Color.parseColor("#000000"));
                tvContactUs.setTextColor(Color.parseColor("#000000"));
                tvProfileAudio.setTextColor(Color.parseColor("#000000"));

                replaceTheInviationFragment(new InvitationFragment());
                closeDrawerFromHere();

                break;

            case R.id.linearApplicationHistory:

                tvApplicationHistory.setTextColor(Color.parseColor("#2DBFD9"));
                tvInvitations.setTextColor(Color.parseColor("#000000"));
                tvDashboard.setTextColor(Color.parseColor("#000000"));
                tvProfileVideo.setTextColor(Color.parseColor("#000000"));
                tvLogout.setTextColor(Color.parseColor("#000000"));

                tvMyProfile.setTextColor(Color.parseColor("#000000"));
                tvJobAlert.setTextColor(Color.parseColor("#000000"));
                tvJobSearch.setTextColor(Color.parseColor("#000000"));
                tvSettings.setTextColor(Color.parseColor("#000000"));
                tvContactUs.setTextColor(Color.parseColor("#000000"));
                tvProfileAudio.setTextColor(Color.parseColor("#000000"));

                replaceTheApplicationHistoryFragment(new ApplicationHistoryFragment());
                closeDrawerFromHere();

                break;

            case R.id.linearProfileVideo:

                tvProfileVideo.setTextColor(Color.parseColor("#2DBFD9"));
                tvApplicationHistory.setTextColor(Color.parseColor("#000000"));
                tvInvitations.setTextColor(Color.parseColor("#000000"));
                tvDashboard.setTextColor(Color.parseColor("#000000"));
                tvLogout.setTextColor(Color.parseColor("#000000"));
                tvJobSearch.setTextColor(Color.parseColor("#000000"));

                tvMyProfile.setTextColor(Color.parseColor("#000000"));
                tvJobAlert.setTextColor(Color.parseColor("#000000"));
                tvJobSearch.setTextColor(Color.parseColor("#000000"));
                tvSettings.setTextColor(Color.parseColor("#000000"));
                tvContactUs.setTextColor(Color.parseColor("#000000"));
                tvProfileAudio.setTextColor(Color.parseColor("#000000"));

                replaceTheProfileVideoFragment(new ProfileVideoFragment());
                closeDrawerFromHere();

                break;

            case R.id.linearLogout:


                tvLogout.setTextColor(Color.parseColor("#2DBFD9"));
                tvProfileVideo.setTextColor(Color.parseColor("#000000"));
                tvApplicationHistory.setTextColor(Color.parseColor("#000000"));
                tvInvitations.setTextColor(Color.parseColor("#000000"));
                tvDashboard.setTextColor(Color.parseColor("#000000"));

                tvMyProfile.setTextColor(Color.parseColor("#000000"));
                tvJobAlert.setTextColor(Color.parseColor("#000000"));
                tvJobSearch.setTextColor(Color.parseColor("#000000"));
                tvSettings.setTextColor(Color.parseColor("#000000"));
                tvContactUs.setTextColor(Color.parseColor("#000000"));
                tvProfileAudio.setTextColor(Color.parseColor("#000000"));


                new FancyAlertDialog.Builder(this)
                        .setTitle("Do you really want to Exit ?")
//                        .setTitle("Jobma lets you record and upload your video resume and share it across social media")
                        .setBackgroundColor(Color.parseColor("#2DBFD9"))  //Don't pass R.color.colorvalue
//                        .setMessage("Do you really want to Exit ?")
                        .setNegativeBtnText("Cancel")
                        .setPositiveBtnBackground(Color.parseColor("#2DBFD9"))  //Don't pass R.color.colorvalue
                        .setPositiveBtnText("Yes")
                        .setNegativeBtnBackground(Color.parseColor("#808080"))  //Don't pass R.color.colorvalue
                        .setAnimation(Animation.SLIDE)
                        .isCancellable(true)
                        .setIcon(R.drawable.logout_logo, Icon.Visible)
                        .OnPositiveClicked(new FancyAlertDialogListener() {
                            @Override
                            public void OnClick() {

                                Hawk.delete("LOGIN_SESSION");
                                Hawk.delete("GET_TOKEN");


                                if (isLoggedIn) {

                                    if (AccessToken.getCurrentAccessToken() == null) {
                                        return; // already logged out
                                    } else {


//                                        GraphRequest delPermRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), "/{user-id}/permissions/", null, HttpMethod.DELETE, new GraphRequest.Callback() {
//                                            @Override
//                                            public void onCompleted(GraphResponse graphResponse) {
//                                                if(graphResponse!=null){
//                                                    FacebookRequestError error =graphResponse.getError();
//                                                    if(error!=null){
//                                                        Log.e(TAG, error.toString());
//                                                    }else {
//                                                        finish();
//                                                    }
//                                                }
//                                            }
//                                        });
//                                        Log.d(TAG,"Executing revoke permissions with graph path" + delPermRequest.getGraphPath());
//                                        delPermRequest.executeAsync();
                                    }


                                } else {

                                    googleSignOut();
                                }


                                ActivityDashboard.this.finish();

                                Intent intent = new Intent(ActivityDashboard.this, ActivityLogin.class);
                                startActivity(intent);


                            }
                        })
                        .OnNegativeClicked(new FancyAlertDialogListener() {
                            @Override
                            public void OnClick() {

                            }
                        })
                        .build();


//                closeDrawerFromHere();

                break;


            case R.id.userProfileImage:

                askingPermissionForCamera();
                uploadImageCodeGoesHere();

                break;


            case R.id.linearSettingsLayout:

                tvSettings.setTextColor(Color.parseColor("#2DBFD9"));
                tvApplicationHistory.setTextColor(Color.parseColor("#000000"));
                tvInvitations.setTextColor(Color.parseColor("#000000"));
                tvDashboard.setTextColor(Color.parseColor("#000000"));
                tvLogout.setTextColor(Color.parseColor("#000000"));

                tvMyProfile.setTextColor(Color.parseColor("#000000"));
                tvJobAlert.setTextColor(Color.parseColor("#000000"));
                tvJobSearch.setTextColor(Color.parseColor("#000000"));
                tvContactUs.setTextColor(Color.parseColor("#000000"));
                tvProfileVideo.setTextColor(Color.parseColor("#000000"));
                tvProfileAudio.setTextColor(Color.parseColor("#000000"));


                if (linearMainContact.getVisibility() == View.VISIBLE) {
                    linearMainContact.setVisibility(View.GONE);
                    imagePlusCont.setVisibility(View.VISIBLE);
                    imageMinusCont.setVisibility(View.GONE);
                    isSettingsClicked = true;
                }


                if (isSettingsClicked) {

                    linearForCPQR.setVisibility(View.VISIBLE);
                    imagePlus.setVisibility(View.GONE);
                    imageMinus.setVisibility(View.VISIBLE);
                    isSettingsClicked = false;

                } else {
                    linearForCPQR.setVisibility(View.GONE);
                    imagePlus.setVisibility(View.VISIBLE);
                    imageMinus.setVisibility(View.GONE);
                    isSettingsClicked = true;

                }

                break;


            case R.id.tvChangePassword:
                changePasswordActivity();

                break;


            case R.id.tvQRCode:

                QRCodeActivity();

                break;


            case R.id.tvPrivacySettings:

                privacySettingActivity();

                break;


            case R.id.linearContactUs:

                tvContactUs.setTextColor(Color.parseColor("#2DBFD9"));
                tvApplicationHistory.setTextColor(Color.parseColor("#000000"));
                tvInvitations.setTextColor(Color.parseColor("#000000"));
                tvDashboard.setTextColor(Color.parseColor("#000000"));
                tvLogout.setTextColor(Color.parseColor("#000000"));

                tvMyProfile.setTextColor(Color.parseColor("#000000"));
                tvJobAlert.setTextColor(Color.parseColor("#000000"));
                tvJobSearch.setTextColor(Color.parseColor("#000000"));
                tvSettings.setTextColor(Color.parseColor("#000000"));
                tvProfileVideo.setTextColor(Color.parseColor("#000000"));
                tvProfileAudio.setTextColor(Color.parseColor("#000000"));


                if (linearForCPQR.getVisibility() == View.VISIBLE) {
                    linearForCPQR.setVisibility(View.GONE);
                    imagePlus.setVisibility(View.VISIBLE);
                    imageMinus.setVisibility(View.GONE);
                    isContactUsClicked = true;
                }

                if (isContactUsClicked) {

                    linearMainContact.setVisibility(View.VISIBLE);
                    imagePlusCont.setVisibility(View.GONE);
                    imageMinusCont.setVisibility(View.VISIBLE);
                    isContactUsClicked = false;

                } else {

                    linearMainContact.setVisibility(View.GONE);
                    imagePlusCont.setVisibility(View.VISIBLE);
                    imageMinusCont.setVisibility(View.GONE);
                    isContactUsClicked = true;

                }

                break;


            case R.id.liReportAnIssue:
                reportAnActivity();

                break;


            case R.id.liTrackReportedIssue:

                trackReportedIssuesActivity();
                break;

            case R.id.liJobAlert:


                tvJobAlert.setTextColor(Color.parseColor("#2DBFD9"));
                tvJobSearch.setTextColor(Color.parseColor("#000000"));
                tvApplicationHistory.setTextColor(Color.parseColor("#000000"));
                tvInvitations.setTextColor(Color.parseColor("#000000"));
                tvDashboard.setTextColor(Color.parseColor("#000000"));
                tvLogout.setTextColor(Color.parseColor("#000000"));

                tvMyProfile.setTextColor(Color.parseColor("#000000"));
                tvSettings.setTextColor(Color.parseColor("#000000"));
                tvContactUs.setTextColor(Color.parseColor("#000000"));
                tvProfileVideo.setTextColor(Color.parseColor("#000000"));
                tvProfileAudio.setTextColor(Color.parseColor("#000000"));

                jobAlertActivity();
                break;


            case R.id.liProfileAudi:

                tvProfileAudio.setTextColor(Color.parseColor("#2DBFD9"));
                tvJobSearch.setTextColor(Color.parseColor("#000000"));
                tvApplicationHistory.setTextColor(Color.parseColor("#000000"));
                tvInvitations.setTextColor(Color.parseColor("#000000"));
                tvDashboard.setTextColor(Color.parseColor("#000000"));
                tvLogout.setTextColor(Color.parseColor("#000000"));

                tvMyProfile.setTextColor(Color.parseColor("#000000"));
                tvSettings.setTextColor(Color.parseColor("#000000"));
                tvContactUs.setTextColor(Color.parseColor("#000000"));
                tvJobAlert.setTextColor(Color.parseColor("#000000"));
                tvProfileVideo.setTextColor(Color.parseColor("#000000"));


                profileAudioActivity();
                break;


            case R.id.liMyProfile:

                tvMyProfile.setTextColor(Color.parseColor("#2DBFD9"));
                tvJobAlert.setTextColor(Color.parseColor("#000000"));
                tvJobSearch.setTextColor(Color.parseColor("#000000"));
                tvApplicationHistory.setTextColor(Color.parseColor("#000000"));
                tvInvitations.setTextColor(Color.parseColor("#000000"));
                tvDashboard.setTextColor(Color.parseColor("#000000"));
                tvLogout.setTextColor(Color.parseColor("#000000"));
                tvSettings.setTextColor(Color.parseColor("#000000"));
                tvContactUs.setTextColor(Color.parseColor("#000000"));
                tvProfileVideo.setTextColor(Color.parseColor("#000000"));
                tvProfileAudio.setTextColor(Color.parseColor("#000000"));

                replaceTheMyProfileFragment(new MyProfileFragment());
                closeDrawerFromHere();
                break;

            case R.id.linearJobSearch:


                tvJobSearch.setTextColor(Color.parseColor("#2DBFD9"));
                tvApplicationHistory.setTextColor(Color.parseColor("#000000"));
                tvInvitations.setTextColor(Color.parseColor("#000000"));
                tvDashboard.setTextColor(Color.parseColor("#000000"));
                tvLogout.setTextColor(Color.parseColor("#000000"));
                tvJobAlert.setTextColor(Color.parseColor("#000000"));
                tvMyProfile.setTextColor(Color.parseColor("#000000"));
                tvSettings.setTextColor(Color.parseColor("#000000"));
                tvContactUs.setTextColor(Color.parseColor("#000000"));
                tvSettings.setTextColor(Color.parseColor("#000000"));
                tvProfileVideo.setTextColor(Color.parseColor("#000000"));
                tvProfileAudio.setTextColor(Color.parseColor("#000000"));

                Intent intent = new Intent(ActivityDashboard.this, ActivitySearch.class);
                startActivity(intent);


                break;

        }


    }

    private void askingPermissionForCamera() {

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.CAMERA) // ask single or multiple permission once
                .subscribe(granted -> {
                    if (granted) {
                        // All requested permissions are granted
                    } else {
                        // At least one permission is denied
                    }
                });


    }

    private void replaceTheMyProfileFragment(MyProfileFragment myProfileFragment) {


        tvTitleToolbar.setText("My Profile");

//        Bundle bundle = new Bundle();
//        bundle.putString("token", getIntent().getStringExtra("token"));
//        myProfileFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, myProfileFragment, "My Profile Fragment").commit();


    }

    private void profileAudioActivity() {

        Intent intent = new Intent(ActivityDashboard.this, ActivityProfileAudio.class);
        startActivity(intent);

    }

    private void loadingProgress() {

        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();


    }

    private void jobAlertActivity() {

        Intent intent = new Intent(ActivityDashboard.this, ActivityJobAlert.class);
        startActivity(intent);

    }

    private void trackReportedIssuesActivity() {

        Intent intent = new Intent(ActivityDashboard.this, ActivityTrackReportedIssue.class);
        startActivity(intent);


    }

    private void reportAnActivity() {
        Intent intent = new Intent(ActivityDashboard.this, ActivityReportAnIssue.class);
        startActivity(intent);


    }

    private void googleSignOut() {

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        System.out.println("ActivityDashboard.onComplete");

                    }
                });


    }

    private void privacySettingActivity() {

        Intent intent = new Intent(ActivityDashboard.this, ActivityPrivacySetting.class);
        startActivity(intent);

    }

    private void QRCodeActivity() {

        Intent intent = new Intent(ActivityDashboard.this, ActivityQRCode.class);
        startActivity(intent);


    }

    private void changePasswordActivity() {
        Intent intent = new Intent(ActivityDashboard.this, ActivityChangePassword.class);
        startActivity(intent);


    }

    private void uploadImageCodeGoesHere() {


        new ImagePicker.Builder(ActivityDashboard.this)
                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.PNG)
                .scale(600, 600)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build();

//        PickSetup setup = new PickSetup()
//                .setTitle("Choose")
//                .setTitleColor(Color.BLACK)
//                .setBackgroundColor(Color.BLACK)
//                .setCancelText("Close")
//                .setCancelTextColor(Color.BLACK)
//                .setButtonTextColor(Color.BLACK)//todo comment by charan
//                .setMaxSize(500)
//                .setPickTypes(EPickType.GALLERY, EPickType.CAMERA)
//                .setCameraButtonText("Camera")
//                .setGalleryButtonText("Gallery")
//                .setIconGravity(Gravity.LEFT)
//                .setButtonOrientation(LinearLayout.VERTICAL) // LinearLayoutCompat.VERTICAL
//                .setSystemDialog(false)
//                .setGalleryIcon(R.drawable.gallery_picker)
//                .setCameraIcon(R.drawable.camera_picker)
//                .setSystemDialog(false).setBackgroundColor(Color.parseColor("#FFFFFF"));
//
//        PickImageDialog.build(setup).setOnPickResult(new IPickResult() {
//            @Override
//            public void onPickResult(PickResult pickResult) {
//
//                mProgressDialog.setMessage("Loding..");
//                mProgressDialog.setCancelable(false);
//                mProgressDialog.setIndeterminate(true);
//                mProgressDialog.show();
//                pickResult.getUri();
//
//                userUploadProfile(pickResult.getPath());
//
//
//            }
//        }).setOnPickCancel(new IPickCancel() {
//            @Override
//            public void onCancelClick() {
//
//            }
//        }).show(ActivityDashboard.this);


    }


    private void userUploadProfile(String path) {


        File file = new File(path);
        final RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile); // NOTE: upload here is the name which is the file name


        HttpModule.provideRepositoryService().uploadUserProfile(getIntent().getStringExtra("token"), body).enqueue(new Callback<UploadProfile>() {
            @Override
            public void onResponse(Call<UploadProfile> call, final Response<UploadProfile> response) {

                mProgressDialog.dismiss();

                if (response.body() != null && response.body().getError() == 0) {


                    new AwesomeSuccessDialog(ActivityDashboard.this)
                            .setTitle("Success")
                            .setMessage(response.body().getMessage())
                            .setColoredCircle(R.color.colorlogin)
                            .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
                            .setCancelable(false)
                            .setPositiveButtonText("Ok")
                            .setPositiveButtonbackgroundColor(R.color.colorlogin)
                            .setPositiveButtonTextColor(R.color.white)
                            .setPositiveButtonClick(new Closure() {
                                @Override
                                public void exec() {

                                    EventBus.getDefault().post(new MessageEventProfile(response.body().getData().getImage()));
                                    Picasso.get().load(response.body().getData().getImage()).into(userProfileImage);

                                }
                            })
                            .show();


                } else {

                    mProgressDialog.dismiss();

                    new AwesomeErrorDialog(ActivityDashboard.this)
                            .setTitle("Oops")
                            .setMessage(Objects.requireNonNull(response.body()).getMessage())
                            .setColoredCircle(R.color.dialogErrorBackgroundColor)
                            .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                            .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
                            .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                            .setButtonText(getString(R.string.dialog_ok_button))
                            .setErrorButtonClick(new Closure() {
                                @Override
                                public void exec() {


                                }
                            })
                            .show();


                }


            }

            @Override
            public void onFailure(Call<UploadProfile> call, Throwable t) {
                mProgressDialog.dismiss();

                System.out.println("ActivityDashboard.onFailure " + t.getMessage());
                Toast.makeText(ActivityDashboard.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }

    private void replaceTheProfileVideoFragment(ProfileVideoFragment profileVideoFragment) {

        tvTitleToolbar.setText("Video Profile");

        Bundle bundle = new Bundle();
        bundle.putString("token", getIntent().getStringExtra("token"));
        profileVideoFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, profileVideoFragment, "Profile Fragment").commit();

    }

    private void replaceTheApplicationHistoryFragment(ApplicationHistoryFragment applicationHistoryFragment) {

        tvTitleToolbar.setText("Application History");
        Bundle bundle = new Bundle();
        bundle.putString("token", getIntent().getStringExtra("token"));
        applicationHistoryFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, applicationHistoryFragment, "ApplicationHistory Fragment").commit();


    }


    private void replaceTheDashboardFragment(HomeFragment homeFragment) {


        tvTitleToolbar.setText("Dashboard");

        Bundle bundle = new Bundle();
        bundle.putString("token", getIntent().getStringExtra("token"));
        homeFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, homeFragment, "Home Fragment").commit();


    }

    private void replaceTheInviationFragment(InvitationFragment invitationFragment) {

        tvTitleToolbar.setText("Invitations");
        Bundle bundle = new Bundle();
        bundle.putString("token", getIntent().getStringExtra("token"));
        invitationFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, invitationFragment, "Inviation Fragment").commit();


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEventProfile event) {
        /* Do something */

        System.out.println("ActivityDashboard.onMessageEvent " + event.getPath());

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    private void closeDrawerFromHere() {
        drawer.closeDrawer(Gravity.START);
    }

    // PRESS BACK AGAIN TO EXIT FROM THE APP

//    @Override
//    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(ActivityDashboard.this, "Please click again to exit the app", Toast.LENGTH_LONG).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce = false;
//            }
//        }, 2000);
//    }


    @Override
    public void onBackPressed() {


        HomeFragment test = (HomeFragment) getSupportFragmentManager().findFragmentByTag("Home Fragment");

        if (test != null && test.isVisible()) {

        } else {

            replaceTheDashboardFragment(new HomeFragment());

        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment myFragment = getSupportFragmentManager().findFragmentByTag("Profile Fragment");
        if (myFragment != null && myFragment.isVisible() && myFragment instanceof ProfileVideoFragment) {

            myFragment.onActivityResult(requestCode, resultCode, data);
        }


        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            //Your Code


            System.out.println("ActivityDashboard.onActivityResult " + mPaths.get(0));

            mProgressDialog.setMessage("Loading..");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
            userUploadProfile(mPaths.get(0));

        }


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

                                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(ActivityDashboard.this, instanceIdResult -> {
                                                String newToken = instanceIdResult.getToken();
                                                Log.e("newToken", newToken);

                                                System.out.println("ActivityDashboard.accept " + addresses);

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


    @Override
    protected void onResume() {
        super.onResume();

        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (result != PackageManager.PERMISSION_GRANTED && permissionChecked) {

            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", ActivityDashboard.this.getPackageName(), null);
            intent.setData(uri);
            ActivityDashboard.this.startActivity(intent);
        } else {

            System.out.println("ActivityDashboard.onResume");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ActivityDashboardPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void showRatPersmissionsToUpdateDevice(final PermissionRequest request) {

    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onDeniedPersmissionsToUpdateDevice() {

        permissionChecked = true;
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", ActivityDashboard.this.getPackageName(), null);
        intent.setData(uri);
        ActivityDashboard.this.startActivity(intent);


        Field[] fields = Build.VERSION_CODES.class.getFields();
        /*        String osName = fields[Build.VERSION.SDK_INT + 1].getName();*/
        // Log.d("Android OsName:", osName);

        //    System.out.println("ActivityDashboard.mobDetailApi " + osName);

        String model = Build.MODEL;
        String reqString = Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();


        HttpModule.provideRepositoryService().mobDetail(getIntent().getStringExtra("token"), model, reqString, "", "", "", "", "", "Android", "", String.valueOf(Build.VERSION.SDK_INT), token).enqueue(new Callback<MobDetail>() {
            @Override
            public void onResponse(Call<MobDetail> call, Response<MobDetail> response) {

                System.out.println("ActivityDashboard.onResponse " + response.body().getMessage());

            }

            @Override
            public void onFailure(Call<MobDetail> call, Throwable t) {

                System.out.println("ActivityDashboard.onFailure " + t.toString());
            }
        });


    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onNeverAskedPersmissionsToUpdateDevice() {
    }

//    https://github.com/mcharmas/Android-ReactiveLocation


}
