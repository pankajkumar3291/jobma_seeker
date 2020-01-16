package com.smartit.jobSeeker.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.getResumeInfo.GetResumeInfo;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityResumeDisplay extends AppCompatActivity {


    private ImageView backArrowImage;
    private WebView mWebView;
    ProgressBar progressbar;
    private ProgressDialog mProgressDialog;
    private TextView tvResumeT;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_display);
        Hawk.init(this).build();
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        initViewsHere();
        loadingProgress();
        getResumeInfoApi();


    }

    private void loadingProgress() {


        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();


    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void getResumeInfoApi() {


        HttpModule.provideRepositoryService().getResumeInfoApi(Hawk.get("GET_TOKEN")).enqueue(new Callback<GetResumeInfo>() {
            @Override
            public void onResponse(Call<GetResumeInfo> call, Response<GetResumeInfo> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();
                    mWebView.getSettings().setJavaScriptEnabled(true);
                    String filename = "http://www3.nd.edu/~cpoellab/teaching/cse40816/android_tutorial.pdf";
                    mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + response.body().getData().getResumeUrl());

                    mWebView.setWebViewClient(new WebViewClient() {

                        public void onPageFinished(WebView view, String url) {

                            progressbar.setVisibility(View.GONE);
                        }
                    });


                } else {
                    mProgressDialog.dismiss();
                }


            }

            @Override
            public void onFailure(Call<GetResumeInfo> call, Throwable t) {
                mProgressDialog.dismiss();
                System.out.println("ActivityResumeDisplay.onFailure " + t.toString());
            }
        });

    }

    private void initViewsHere() {

        backArrowImage = findViewById(R.id.backArrowImage);
        mWebView = findViewById(R.id.mWebView);
        progressbar = findViewById(R.id.progressbar);

        tvResumeT = findViewById(R.id.tvResumeT);
        tvResumeT.setTypeface(tvResumeT.getTypeface(), Typeface.BOLD);


        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
