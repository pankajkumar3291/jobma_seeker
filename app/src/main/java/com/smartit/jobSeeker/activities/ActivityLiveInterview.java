package com.smartit.jobSeeker.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.liveInterview.EOEncodedResponse;
import com.smartit.jobSeeker.apiResponse.liveInterview.EOLiveInterview;
import com.smartit.jobSeeker.httpRetrofit.APIClientLiveInterview;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.smartit.jobSeeker.webRtc.WebRtcActivity;

import java.nio.charset.StandardCharsets;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLiveInterview extends AppCompatActivity implements View.OnClickListener {

    private EditText edAccessToken;
    private Button btnLogin;
    private NoInternetDialog noInternetDialog;
    private String apiKey;
    private ProgressDialog mProgressDialog;
    private int userId;
    private int invitedId;
    private APIClientLiveInterview.APIInterfaceLiveInterview apiInterfaceLiveInterview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_interview);

        if (this.getIntent().hasExtra("invitedId")) {
            this.invitedId = this.getIntent().getIntExtra("invitedId", 0);
        }

        this.initViewsHere();
    }

    private void initViewsHere() {
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        this.apiInterfaceLiveInterview = APIClientLiveInterview.getClient();
        this.apiKey = Hawk.get("GET_TOKEN");
        this.userId = Hawk.get("USER_ID");
        edAccessToken = findViewById(R.id.edAccessToken);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            if (TextUtils.isEmpty(edAccessToken.getText().toString().trim())) {
                Toast.makeText(ActivityLiveInterview.this, "Please enter your access token", Toast.LENGTH_SHORT).show();
            } else {
                this.liveInterviewCheckApi();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    private void loadingProgress() {
        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }

    private void liveInterviewCheckApi() {
        if (this.apiKey != null) {
            loadingProgress();
            HttpModule.provideRepositoryService().liveInterviewCheck(apiKey, this.invitedId, edAccessToken.getText().toString().trim()).enqueue(new Callback<EOLiveInterview>() {
                @Override
                public void onResponse(@NonNull Call<EOLiveInterview> call, @NonNull Response<EOLiveInterview> response) {
                    mProgressDialog.dismiss();
                    if (response.body() != null) {
                        EOLiveInterview liveInterview = response.body();
                        if (liveInterview.getError() == 0) {
                            Toast.makeText(ActivityLiveInterview.this, "" + liveInterview.getMessage(), Toast.LENGTH_SHORT).show();
                            if (userId != 0 && invitedId != 0) {
                                StringBuilder builder = new StringBuilder();
                                builder.append("[").append(userId).append(",").append(invitedId).append(",").append(4).append("]");
                                byte[] data = builder.toString().getBytes(StandardCharsets.UTF_8);
                                String encodedIntoBase64 = Base64.encodeToString(data, Base64.DEFAULT);
                                getJsonResponseFromEncodedData(encodedIntoBase64);
                            }
                        } else {
                            Toast.makeText(ActivityLiveInterview.this, "" + liveInterview.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<EOLiveInterview> call, Throwable t) {
                    if (t.getMessage() != null) {
                        mProgressDialog.dismiss();
                        Toast.makeText(ActivityLiveInterview.this, "Failed Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void getJsonResponseFromEncodedData(String encodedValue) {
        if (this.apiKey != null) {
            loadingProgress();
            apiInterfaceLiveInterview.getResponseFromEncodedData("1", encodedValue).enqueue(new Callback<EOEncodedResponse>() {
                @Override
                public void onResponse(Call<EOEncodedResponse> call, Response<EOEncodedResponse> response) {
                    mProgressDialog.dismiss();
                    if (response.body() != null) {
                        EOEncodedResponse encodedResponse = response.body();

                        if (encodedResponse.getError() == 0) {
                            Intent webRtcIntent = new Intent(ActivityLiveInterview.this, WebRtcActivity.class);
                            webRtcIntent.putExtra("encodedData", encodedResponse.getData());
                            ActivityLiveInterview.this.startActivity(webRtcIntent);
                        }
                    } else {
                        Toast.makeText(ActivityLiveInterview.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<EOEncodedResponse> call, Throwable t) {
                    if (t.getMessage() != null) {
                        mProgressDialog.dismiss();
                        Toast.makeText(ActivityLiveInterview.this, "Failed Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


}
