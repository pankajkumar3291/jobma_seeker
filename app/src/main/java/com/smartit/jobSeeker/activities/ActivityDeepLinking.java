package com.smartit.jobSeeker.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.atsApiResponses.ats_deeplink.deeplinking_url.AtsDeeplink;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityDeepLinking extends AppCompatActivity {

    private ImageView imageNext;
    private TextView tvName, tvEmailId, tvApplyingFor, tvDate;
    private NoInternetDialog noInternetDialog;
    private CircleImageView imageProfile;
    private ProgressDialog mProgressDialog;
    private String jobTitle, firstName, lastName;
    private Integer interviewId, kitId, cathcerId, getInvitationId;
    private String sourceId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_linking);

        findingTheIdsHere();
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        loadingProgress();
        AtsDeeplinkApisGoesHere(getIntent().getData().getPath());
    }

    private void loadingProgress() {
        mProgressDialog.setMessage("Loading..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }


    private void AtsDeeplinkApisGoesHere(String path) {
        String newUrl = getIntent().getData().getPath().replaceAll("//", "");
        System.out.println("ActivityDeepLinking.AtsDeeplinkApisGoesHere " + newUrl);
        HttpModule.provideRepositoryService().deeplinkATSURL(newUrl).enqueue(new Callback<AtsDeeplink>(){
            @Override
            public void onResponse(Call<AtsDeeplink> call, Response<AtsDeeplink> response) {
                if (response.body() != null && response.body().getError() == 0) {
                    mProgressDialog.dismiss();
                    tvName.setText(response.body().getData().getInterview().getFirstName() + " " + response.body().getData().getInterview().getLastName());
                    tvEmailId.setText(response.body().getData().getInterview().getEmail());
                    tvApplyingFor.setText(response.body().getData().getInterview().getJobTitle());
                    tvDate.setText(response.body().getData().getInterview().getDate());
                    Picasso.get().load(response.body().getData().getCatcherPic()).into(imageProfile);
                    jobTitle = response.body().getData().getInterview().getJobTitle();
                    firstName = response.body().getData().getInterview().getFirstName();
                    lastName = response.body().getData().getInterview().getLastName();
                    cathcerId = response.body().getData().getInterview().getCatcherId();
                    interviewId = response.body().getData().getInterview().getInterviewId();
                    kitId = response.body().getData().getInterview().getKitId();
                    sourceId = response.body().getData().getInterview().getSource();
                    getInvitationId = response.body().getData().getInterview().getInterviewId();
                    Hawk.put("SAVED_SOURCEID", sourceId);
                    Hawk.put("SAVED_INVITATIONID", getInvitationId);
                } else {
                    mProgressDialog.dismiss();

                    new AwesomeErrorDialog(ActivityDeepLinking.this)
                            .setTitle("Oops")
                            .setMessage(Objects.requireNonNull(response.body()).getMessage())
                            .setColoredCircle(R.color.dialogErrorBackgroundColor)
                            .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                            .setCancelable(false).setButtonText(getString(R.string.dialog_ok_button))
                            .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                            .setButtonText(getString(R.string.dialog_ok_button))
                            .setErrorButtonClick(new Closure(){
                                @Override
                                public void exec() {
                                    moveTaskToBack(true);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(1);
                                }
                            })
                            .show();
                }
            }
            @Override
            public void onFailure(Call<AtsDeeplink> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("ActivityDeepLinking.onFailure " + t.getMessage());
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

    private void findingTheIdsHere() {
        imageProfile = findViewById(R.id.imageProfile);
        imageNext = findViewById(R.id.imageNext);
        tvName = findViewById(R.id.tvName);
        tvEmailId = findViewById(R.id.tvEmailId);
        tvApplyingFor = findViewById(R.id.tvApplyingFor);
        tvDate = findViewById(R.id.tvDate);
        imageNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityDeepLinking.this, ActivityInterviewNow.class);
                intent.putExtra("INTERVIEW_ID", interviewId);
                intent.putExtra("SOURCE_ID", sourceId);
                intent.putExtra("KIT_ID", kitId);
                intent.putExtra("CATCHER_ID", cathcerId);
                intent.putExtra("ATS_VALUE", "0");
                intent.putExtra("JTitle", jobTitle);
                intent.putExtra("FName", firstName);
                intent.putExtra("LName", lastName);
                startActivity(intent);
                finish();
            }
        });
    }


}
