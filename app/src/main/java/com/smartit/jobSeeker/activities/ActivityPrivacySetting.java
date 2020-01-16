package com.smartit.jobSeeker.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.privacySettings.PrivacySettings;
import com.smartit.jobSeeker.apiResponse.updatePrivacySettings.UpdatePrivacySettings;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityPrivacySetting extends AppCompatActivity implements View.OnClickListener {


    private TextView tvPrimaryEmail, tvPrimaryCode, tvTextResume, tvVideoResume, tvAudio,
            tvFaceebookProfile, tvLinkedinProfile, tvTwitterProfile, tvtvQuoraProfile, tvSocialWebsite,tvTitlePST;

    private Switch switch_primary_email, switch_primary_phone, switch_text_resume, switch_video_resume, switch_audio_resume,
            switch_faceebook, switch_linkediin, switch_twittter, switch_quora, switch_social_website;


    private ImageView backArrowImage;
    private NoInternetDialog noInternetDialog;


    private String spEmail, spPhone, stResume, svResume, saResume,
            sFaceebook, sLinkediin, sTwiitter, sQuora, ssWbsite;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_setting);

        Hawk.init(this).build();
        noInternetDialog = new NoInternetDialog.Builder(this).build();

        findTheIdsHere();

        checkSwitch();

        privacySettingApi();


    }




    private void updatePrivacySettingsApi() {


        HttpModule.provideRepositoryService().updatePrivacySettings(Hawk.get("GET_TOKEN"), spPhone, spEmail, saResume, stResume,
                svResume, sFaceebook, sLinkediin, sTwiitter, sQuora, ssWbsite).enqueue(new Callback<UpdatePrivacySettings>() {

            @Override
            public void onResponse(Call<UpdatePrivacySettings> call, Response<UpdatePrivacySettings> response) {

                if (response.body() != null && response.body().getError() == 0) {

                    Toast.makeText(ActivityPrivacySetting.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(ActivityPrivacySetting.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<UpdatePrivacySettings> call, Throwable t) {

                System.out.println("ActivityPrivacySetting.onFailure " + t.toString());
            }

        });

    }

    private void checkSwitch() {

        switch_primary_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (switch_primary_email.isChecked()) {
                    spEmail = "1";
                    updatePrivacySettingsApi();

                } else {
                    spEmail = "0";
                    updatePrivacySettingsApi();

                }
            }
        });

        switch_primary_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (switch_primary_phone.isChecked()) {
                    spPhone = "1";
                    updatePrivacySettingsApi();

                } else {
                    spPhone = "0";
                    updatePrivacySettingsApi();

                }
            }
        });

        switch_text_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (switch_text_resume.isChecked()) {
                    stResume = "1";
                    updatePrivacySettingsApi();

                } else {
                    stResume = "0";
                    updatePrivacySettingsApi();

                }
            }
        });


        switch_video_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (switch_video_resume.isChecked()) {
                    svResume = "1";
                    updatePrivacySettingsApi();

                } else {
                    svResume = "0";
                    updatePrivacySettingsApi();

                }
            }
        });


        switch_audio_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switch_audio_resume.isChecked()) {
                    saResume = "1";
                    updatePrivacySettingsApi();

                } else {
                    saResume = "0";
                    updatePrivacySettingsApi();

                }

            }
        });

        switch_faceebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switch_faceebook.isChecked()) {
                    sFaceebook = "1";
                    updatePrivacySettingsApi();

                } else {
                    sFaceebook = "0";
                    updatePrivacySettingsApi();

                }

            }
        });

        switch_linkediin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (switch_linkediin.isChecked()) {
                    sLinkediin = "1";
                    updatePrivacySettingsApi();

                } else {
                    sLinkediin = "0";
                    updatePrivacySettingsApi();

                }
            }
        });


        switch_twittter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switch_twittter.isChecked()) {
                    sTwiitter = "1";
                    updatePrivacySettingsApi();

                } else {
                    sTwiitter = "0";
                    updatePrivacySettingsApi();

                }

            }
        });

        switch_quora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switch_quora.isChecked()) {
                    sQuora = "1";
                    updatePrivacySettingsApi();

                } else {
                    sQuora = "0";
                    updatePrivacySettingsApi();

                }
            }
        });
        switch_social_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (switch_social_website.isChecked()) {
                    ssWbsite = "1";
                    updatePrivacySettingsApi();

                } else {
                    ssWbsite = "0";
                    updatePrivacySettingsApi();

                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    private void findTheIdsHere() {

        backArrowImage = findViewById(R.id.backArrowImage);
        backArrowImage.setOnClickListener(this);

        tvPrimaryEmail = findViewById(R.id.tvPrimaryEmail);
        tvPrimaryCode = findViewById(R.id.tvPrimaryCode);
        tvTextResume = findViewById(R.id.tvTextResume);
        tvVideoResume = findViewById(R.id.tvVideoResume);
        tvAudio = findViewById(R.id.tvAudio);

        tvFaceebookProfile = findViewById(R.id.tvFaceebookProfile);
        tvLinkedinProfile = findViewById(R.id.tvLinkedinProfile);
        tvTwitterProfile = findViewById(R.id.tvTwitterProfile);
        tvtvQuoraProfile = findViewById(R.id.tvtvQuoraProfile);
        tvSocialWebsite = findViewById(R.id.tvSocialWebsite);
        tvTitlePST = findViewById(R.id.tvTitlePST);
        tvTitlePST.setTypeface(tvTitlePST.getTypeface(), Typeface.BOLD);


        switch_primary_email = findViewById(R.id.switch_primary_email);
        switch_primary_phone = findViewById(R.id.switch_primary_phone);

        switch_text_resume = findViewById(R.id.switch_text_resume);
        switch_video_resume = findViewById(R.id.switch_video_resume);

        switch_audio_resume = findViewById(R.id.switch_audio_resume);

        switch_faceebook = findViewById(R.id.switch_faceebook);
        switch_linkediin = findViewById(R.id.switch_linkediin);

        switch_twittter = findViewById(R.id.switch_twittter);

        switch_quora = findViewById(R.id.switch_quora);
        switch_social_website = findViewById(R.id.switch_social_website);


    }

    private void privacySettingApi() {


        HttpModule.provideRepositoryService().privacySettingsApi(Hawk.get("GET_TOKEN")).enqueue(new Callback<PrivacySettings>() {
            @Override
            public void onResponse(Call<PrivacySettings> call, Response<PrivacySettings> response) {


                if (response.body() != null && response.body().getError() == 0) {


                    tvPrimaryEmail.setText(response.body().getData().getPrimaryEmail().toString());
                    tvPrimaryCode.setText(response.body().getData().getPrimaryPhone().toString());
                    tvTextResume.setText(response.body().getData().getPdfResume().toString());
                    tvVideoResume.setText(response.body().getData().getVideoResume().toString());
                    tvAudio.setText(response.body().getData().getAudioResume().toString());
                    tvFaceebookProfile.setText(response.body().getData().getFacebook().toString());
                    tvLinkedinProfile.setText(response.body().getData().getLinkedin().toString());
                    tvTwitterProfile.setText(response.body().getData().getTwitter().toString());
                    tvtvQuoraProfile.setText(response.body().getData().getQuora().toString());
                    tvSocialWebsite.setText(response.body().getData().getSocialWeb().toString());


                    spEmail = response.body().getData().getPrimaryEmailOption();
                    spPhone = response.body().getData().getPrimaryPhoneOption();
                    stResume = response.body().getData().getPdfResumeOption();
                    svResume = response.body().getData().getVideoResumeOption();
                    saResume = response.body().getData().getAudioResumeOption();
                    sFaceebook = response.body().getData().getFacebookOption();
                    sLinkediin = response.body().getData().getLinkedinOption();
                    sTwiitter = response.body().getData().getTwitterOption();
                    sQuora = response.body().getData().getQuoraOption();
                    ssWbsite = response.body().getData().getSocialWebOption();


                    // MARK

                    if (response.body().getData().getPrimaryEmailOption().equalsIgnoreCase("1")) {
                        switch_primary_email.setChecked(true);

                    } else {
                        switch_primary_email.setChecked(false);
                    }


                    if (response.body().getData().getPrimaryPhoneOption().equalsIgnoreCase("1")) {
                        switch_primary_phone.setChecked(true);
                    } else {
                        switch_primary_phone.setChecked(false);
                    }


                    if (response.body().getData().getPdfResumeOption().equalsIgnoreCase("1")) {
                        switch_text_resume.setChecked(true);
                    } else {
                        switch_text_resume.setChecked(false);
                    }

                    if (response.body().getData().getVideoResumeOption().equalsIgnoreCase("1")) {
                        switch_video_resume.setChecked(true);
                    } else {
                        switch_video_resume.setChecked(false);
                    }

                    if (response.body().getData().getAudioResumeOption().equalsIgnoreCase("1")) {
                        switch_audio_resume.setChecked(true);
                    } else {
                        switch_audio_resume.setChecked(false);
                    }


                    if (response.body().getData().getFacebookOption().equalsIgnoreCase("1")) {
                        switch_faceebook.setChecked(true);
                    } else {
                        switch_faceebook.setChecked(false);
                    }


                    if (response.body().getData().getLinkedinOption().equalsIgnoreCase("1")) {
                        switch_linkediin.setChecked(true);
                    } else {
                        switch_linkediin.setChecked(false);
                    }


                    if (response.body().getData().getTwitterOption().equalsIgnoreCase("1")) {
                        switch_twittter.setChecked(true);
                    } else {
                        switch_twittter.setChecked(false);
                    }

                    if (response.body().getData().getQuoraOption().equalsIgnoreCase("1")) {
                        switch_quora.setChecked(true);
                    } else {
                        switch_quora.setChecked(false);
                    }


                    if (response.body().getData().getSocialWebOption().equalsIgnoreCase("1")) {
                        switch_social_website.setChecked(true);
                    } else {
                        switch_social_website.setChecked(false);
                    }


                } else {


                    Toast.makeText(ActivityPrivacySetting.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<PrivacySettings> call, Throwable t) {

                System.out.println("ActivityPrivacySetting.onFailure " + t.toString());

            }
        });


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.backArrowImage:
                finish();
                break;
        }

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
