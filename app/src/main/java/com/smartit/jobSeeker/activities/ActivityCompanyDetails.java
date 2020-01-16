package com.smartit.jobSeeker.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.jobDescription.JobDescription;
import com.smartit.jobSeeker.apiResponse.jobDescriptionNew.JobDescriptionNew;
import com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.job_search.SearchJobData;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityCompanyDetails extends AppCompatActivity {


    private ImageView backArrowImage, imagePosterThumbnail, imageThumbPlay;
    private CircleImageView companyImage;
    private TextView tvTitle, tvJobType, tvLocation,
            tvSalary, tvPostedOn, tvExpirationDate,
            tvExperience, tvAvailAfter, tvFunctionalArea,
            tvIndustry, tvSkills, tvDescription, tvReadMore;

    private List<SearchJobData> searchJobData = new ArrayList<>();
    private boolean isCheck = true;
    private String containValue;
    private int jobId, invitationId;
    private LinearLayout liShare, liApply;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private String shareUrl;
    private TextView tvJobma, tvSoftTitle;
    private CardView cardInterviewNow, cardCancel;
    private NoInternetDialog noInternetDialog;

    private String VIDEO_URL;
    private String HLS_URL;

    private ArrayList<String> funcList = new ArrayList<>();
    private ArrayList<String> indList = new ArrayList<>();
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_detail);
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        loadingProgress();


        Hawk.init(ActivityCompanyDetails.this).build();

        noInternetDialog = new NoInternetDialog.Builder(this).build();

        if (getIntent() != null && getIntent().hasExtra("CHECKED_FROM_WHERE_ITS_COMING")) {
            containValue = getIntent().getStringExtra("CHECKED_FROM_WHERE_ITS_COMING");
        }


        if (getIntent() != null && getIntent().hasExtra("jobId")) {
            jobId = getIntent().getIntExtra("jobId", 0);
        }

        if (getIntent() != null && getIntent().hasExtra("invitationId")) {
            invitationId = getIntent().getIntExtra("invitationId", 0);
        }


        if (containValue != null && Objects.requireNonNull(getIntent()).getStringExtra("CHECKED_FROM_WHERE_ITS_COMING").equalsIgnoreCase(containValue)) {

            if (getIntent() != null && getIntent().hasExtra("JOB_DESCRIPTION")) {

                mProgressDialog.dismiss();

                searchJobData.addAll((Collection<? extends SearchJobData>) getIntent().getSerializableExtra("JOB_DESCRIPTION"));
                System.out.println("ActivitySwappingLikeTinder.onCreate " + searchJobData.size());
            }
        } else {

            jobDescription(jobId, invitationId);
        }

        findingIdsHere();


    }

    private void loadingProgress() {

        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }


    private void jobDescription(int jobId, int invitationId) {

        HttpModule.provideRepositoryService().jobDescription(Hawk.get("GET_TOKEN"), String.valueOf(jobId), String.valueOf(invitationId)).enqueue(new Callback<JobDescriptionNew>() {
            @Override
            public void onResponse(Call<JobDescriptionNew> call, Response<JobDescriptionNew> response) {

                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();


                    if (response.body().getData().getJobDetail().getJobmaJobTitle().equalsIgnoreCase("")) {
                        tvSoftTitle.setText("N/A");
                    } else {
                        tvSoftTitle.setText(response.body().getData().getJobDetail().getJobmaJobTitle());
                    }
//


                    if (response.body().getData().getJobDetail().getJobmaJobCompanyName().equalsIgnoreCase("")) {
                        tvTitle.setText("Title - N/A");
                    } else {
                        tvTitle.setText(response.body().getData().getJobDetail().getJobmaJobCompanyName());
                    }

                    if (response.body().getData().getJobDetail().getJobmaJobType().equalsIgnoreCase("")) {
                        tvJobType.setText("Job Type - (N/A)");
                    } else {
                        tvJobType.setText("( " + response.body().getData().getJobDetail().getJobmaJobType() + " )");
                    }


                    String value_split = response.body().getData().getJobDetail().getJobmaJobLocations().replace("|", ",");

                    if (value_split.equalsIgnoreCase("")) {
                        tvLocation.setText("Location - N/A");
                    } else {
                        tvLocation.setText(value_split);
                    }


                    if (response.body().getData().getJobDetail().getJobmaJobMinSalary().equalsIgnoreCase("") && response.body().getData().getJobDetail().getJobmaJobMaxSalary().equalsIgnoreCase("")) {
                        tvSalary.setText("Salary - N/A");
                    } else {
                        tvSalary.setText(response.body().getData().getJobDetail().getJobmaJobMinSalary() + " To " + response.body().getData().getJobDetail().getJobmaJobMaxSalary() + " Per Month");
                    }

                    if (response.body().getData().getJobDetail().getCreatedAt().equalsIgnoreCase("")) {
                        tvPostedOn.setText("Posted On - N/A");
                    } else {
                        tvPostedOn.setText("(Posted On - " + response.body().getData().getJobDetail().getCreatedAt());
                    }


                    if (response.body().getData().getJobDetail().getJobmaJobExpiryDate().equalsIgnoreCase("")) {
                        tvExpirationDate.setText("Job Expiration Date - N/A");
                    } else {
                        tvExpirationDate.setText("Job Expiration Date - " + response.body().getData().getJobDetail().getJobmaJobExpiryDate() + ")");
                    }


                    if (response.body().getData().getJobDetail().getJobmaJobMinExp().equalsIgnoreCase("") && response.body().getData().getJobDetail().getJobmaJobMaxExp().equalsIgnoreCase("")) {
                        tvExperience.setText("N/A");
                    } else {
                        tvExperience.setText(response.body().getData().getJobDetail().getJobmaJobMinExp() + " To " + response.body().getData().getJobDetail().getJobmaJobMaxExp());
                    }


                    shareUrl = response.body().getData().getJobDetail().getJobLink();

                    if (!response.body().getData().getJobDetail().getJobmaJobNoticePeriod().equalsIgnoreCase("")) {
                        tvAvailAfter.setText(response.body().getData().getJobDetail().getJobmaJobNoticePeriod());
                    } else {
                        tvAvailAfter.setText("Notice Period - N/A");
                    }


                    for (int x = 0; x < response.body().getData().getJobDetail().getFunctional().size(); x++) {
                        funcList.add(String.valueOf(response.body().getData().getJobDetail().getFunctional().get(x).getValue()));

                    }
                    System.out.println("ProfileInnerFragment.onResponse " + funcList.size());

                    String functionalArea = null;
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    functionalArea = TextUtils.join(",", funcList); // Don't use String.join in these condition
//                    }


                    if (TextUtils.isEmpty(functionalArea)) {
                        tvFunctionalArea.setText("N/A");
                    } else {
                        tvFunctionalArea.setText(functionalArea);
                    }


                    for (int x = 0; x < response.body().getData().getJobDetail().getIndustry().size(); x++) {
                        indList.add(String.valueOf(response.body().getData().getJobDetail().getIndustry().get(x).getValue()));

                    }
                    System.out.println("ProfileInnerFragment.onResponse " + indList.size());

                    String industryArea = null;
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    industryArea = TextUtils.join(",", indList); // Don't use String.join in these condition
//                    }


                    if (TextUtils.isEmpty(industryArea)) {
                        tvIndustry.setText("N/A");
                    } else {
                        tvIndustry.setText(industryArea);
                    }


                    if (!response.body().getData().getJobDetail().getJobmaJobKeywords().equalsIgnoreCase("")) {
                        tvSkills.setText(response.body().getData().getJobDetail().getJobmaJobKeywords());
                    } else {
                        tvSkills.setText("N/A");
                    }

                    tvDescription.setText(response.body().getData().getJobDetail().getJobmaJobDescription());


                    HLS_URL = response.body().getData().getJobDetail().getJobHls();


                    if (response.body().getData().getJobDetail().getJobPoster().equalsIgnoreCase("")) {

                        Picasso.get()
                                .load(R.drawable.no_thumbail)
                                .placeholder(R.drawable.no_thumbail)
                                .error(R.drawable.no_thumbail)
                                .into(imagePosterThumbnail);


                    } else {

                        Picasso.get()
                                .load(response.body().getData().getJobDetail().getJobPoster())
                                .placeholder(R.drawable.no_thumbail)
                                .error(R.drawable.no_thumbail)
                                .into(imagePosterThumbnail);
                    }


                    if (!response.body().getData().getJobDetail().getJobmaCatcherPhoto().equalsIgnoreCase("")) {
                        Picasso.get()
                                .load(response.body().getData().getJobDetail().getJobmaCatcherPhoto())
                                .placeholder(R.drawable.no_image_available)
                                .error(R.drawable.no_image_available)
                                .into(companyImage);
                    } else {

                        Picasso.get()
                                .load(response.body().getData().getJobDetail().getJobmaCatcherPhoto())
                                .placeholder(R.drawable.no_image_available)
                                .error(R.drawable.no_image_available)
                                .into(companyImage);
                    }


                    tvReadMore.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {

                            if (tvDescription.getLineCount() > 0) {
                                tvReadMore.setVisibility(View.VISIBLE);
                            }

                        }
                    });

                    tvReadMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            if (isCheck) {
                                tvDescription.setMaxLines(20);
                                if (tvReadMore.getText().equals("READ MORE")) {
                                    tvReadMore.setText("READ LESS");
                                    tvReadMore.setTextColor(Color.parseColor("#e50000"));
                                    tvReadMore.setTypeface(tvReadMore.getTypeface(), Typeface.BOLD);
                                }

                                isCheck = false;
                            } else {
                                tvDescription.setMaxLines(3);

                                if (tvReadMore.getText().equals("READ LESS")) {
                                    tvReadMore.setText("READ MORE");
                                    tvReadMore.setTextColor(Color.parseColor("#28c0da"));
                                    tvReadMore.setTypeface(tvReadMore.getTypeface(), Typeface.BOLD);
                                }
                                isCheck = true;
                            }


                        }
                    });


                } else {

                    mProgressDialog.dismiss();
                    System.out.println("ActivityCompanyDetails.onResponse " + Objects.requireNonNull(response.body()).getMessage());

                }


            }

            @Override
            public void onFailure(Call<JobDescriptionNew> call, Throwable t) {

                mProgressDialog.dismiss();
                System.out.println("ActivityCompanyDetails.onFailure " + t.toString());


            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void findingIdsHere() {

        backArrowImage = findViewById(R.id.backArrowImage);

        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imagePosterThumbnail = findViewById(R.id.imagePosterThumbnail);
        imageThumbPlay = findViewById(R.id.imageThumbPlay);

        imageThumbPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                if (HLS_URL.equalsIgnoreCase("")) {
//
//                }


            }
        });

        tvSoftTitle = findViewById(R.id.tvSoftTitle);
        tvSoftTitle.setTypeface(tvSoftTitle.getTypeface(), Typeface.BOLD);

        companyImage = findViewById(R.id.companyImage);
        tvTitle = findViewById(R.id.tvTitle);
        tvJobType = findViewById(R.id.tvJobType);
        tvLocation = findViewById(R.id.tvLocation);
        tvSalary = findViewById(R.id.tvSalary);
        tvPostedOn = findViewById(R.id.tvPostedOn);
        tvExpirationDate = findViewById(R.id.tvExpirationDate);
        tvExperience = findViewById(R.id.tvExperience);

        tvAvailAfter = findViewById(R.id.tvAvailAfter);
        tvFunctionalArea = findViewById(R.id.tvFunctionalArea);
        tvIndustry = findViewById(R.id.tvIndustry);
        tvSkills = findViewById(R.id.tvSkills);
        tvDescription = findViewById(R.id.tvDescription);
        tvReadMore = findViewById(R.id.tvReadMore);

        liShare = findViewById(R.id.liShare);
        liApply = findViewById(R.id.liApply);


        if (containValue != null && Objects.requireNonNull(getIntent()).getStringExtra("CHECKED_FROM_WHERE_ITS_COMING").equalsIgnoreCase(containValue)) {


            if (searchJobData.get(0).getJobmaJobTitle().equalsIgnoreCase("")) {
                tvTitle.setText("N/A");
            } else {
                tvTitle.setText(searchJobData.get(0).getJobmaJobTitle());
                tvSoftTitle.setText(searchJobData.get(0).getJobmaJobTitle());
            }

            if (searchJobData.get(0).getJobmaJobType().equalsIgnoreCase("")) {
                tvJobType.setText("N/A");
            } else {
                tvJobType.setText("( " + searchJobData.get(0).getJobmaJobType() + " )");
            }

            if (searchJobData.get(0).getJobmaJobCompanyName().equalsIgnoreCase("") && searchJobData.get(0).getJobmaJobCity().equalsIgnoreCase("")) {
                tvLocation.setText("N/A");
            } else {
                tvLocation.setText(searchJobData.get(0).getJobmaJobCompanyName() + " , " + searchJobData.get(0).getJobmaJobCity());
            }

            if (searchJobData.get(0).getJobmaJobMinSalary().equalsIgnoreCase("") && searchJobData.get(0).getJobmaJobMaxSalary().equalsIgnoreCase("")) {
                tvSalary.setText("N/A");
            } else {
                tvSalary.setText(searchJobData.get(0).getJobmaJobMinSalary() + " To " + searchJobData.get(0).getJobmaJobMaxSalary() + " Per Month");
            }

            if (searchJobData.get(0).getPostDate().equalsIgnoreCase("")) {
                tvPostedOn.setText("N/A");
            } else {
                tvPostedOn.setText("Posted On - " + searchJobData.get(0).getPostDate());
            }

            if (searchJobData.get(0).getJobmaJobExpiryDate().equalsIgnoreCase("")) {
                tvExpirationDate.setText("N/A");
            } else {
                tvExpirationDate.setText("Job Expiration Date - " + searchJobData.get(0).getJobmaJobExpiryDate());
            }


            if (searchJobData.get(0).getJobmaJobMinExp().equalsIgnoreCase("") && searchJobData.get(0).getJobmaJobMaxExp().equalsIgnoreCase("")) {
                tvExperience.setText("N/A");
            } else {
                tvExperience.setText(searchJobData.get(0).getJobmaJobMinExp() + " To " + searchJobData.get(0).getJobmaJobMaxExp());
            }

            if (!searchJobData.get(0).getJobmaJobNoticePeriod().equalsIgnoreCase("")) {
                tvAvailAfter.setText(searchJobData.get(0).getJobmaJobNoticePeriod());
            } else {
                tvAvailAfter.setText("N/A");
            }


            if (!searchJobData.get(0).getFunctionalArea().equalsIgnoreCase("")) {
                tvFunctionalArea.setText(searchJobData.get(0).getFunctionalArea());
            } else {
                tvFunctionalArea.setText("N/A");
            }

            if (!searchJobData.get(0).getIndustry().equalsIgnoreCase("")) {
                tvIndustry.setText(searchJobData.get(0).getIndustry());
            } else {
                tvIndustry.setText("N/A");
            }

            if (!searchJobData.get(0).getJobmaJobKeywords().equalsIgnoreCase("")) {
                tvSkills.setText(searchJobData.get(0).getJobmaJobKeywords());
            } else {
                tvSkills.setText("N/A");
            }
            tvDescription.setText(searchJobData.get(0).getJobmaJobDescription());

            if (!searchJobData.get(0).getJobmaCatcherPhoto().equalsIgnoreCase("")) {

//                Picasso.get().load(searchJobData.get(0).getJobmaCatcherPhoto()).into(companyImage);

                Picasso.get()
                        .load(searchJobData.get(0).getJobmaCatcherPhoto())
                        .placeholder(R.drawable.no_image_available)
                        .error(R.drawable.no_image_available)
                        .into(companyImage);

            } else {

                Picasso.get()
                        .load(R.drawable.no_image_available)
                        .placeholder(R.drawable.no_image_available)
                        .error(R.drawable.no_image_available)
                        .into(companyImage);
            }


            tvReadMore.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    if (tvDescription.getLineCount() > 0) {
                        tvReadMore.setVisibility(View.VISIBLE);
                    }

                }
            });

            tvReadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (isCheck) {
                        tvDescription.setMaxLines(20);
                        if (tvReadMore.getText().equals("READ MORE")) {
                            tvReadMore.setText("READ LESS");
                            tvReadMore.setTextColor(Color.parseColor("#e50000"));
                            tvReadMore.setTypeface(tvReadMore.getTypeface(), Typeface.BOLD);
                        }

                        isCheck = false;
                    } else {
                        tvDescription.setMaxLines(3);

                        if (tvReadMore.getText().equals("READ LESS")) {
                            tvReadMore.setText("READ MORE");
                            tvReadMore.setTextColor(Color.parseColor("#28c0da"));
                            tvReadMore.setTypeface(tvReadMore.getTypeface(), Typeface.BOLD);
                        }
                        isCheck = true;
                    }


                }
            });

        }


        liShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (containValue != null && Objects.requireNonNull(getIntent()).getStringExtra("CHECKED_FROM_WHERE_ITS_COMING").equalsIgnoreCase(containValue)) {

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "The app");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, searchJobData.get(0).getShareUrl());
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));

                } else {

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "The app");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareUrl);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));

                }


            }
        });


        liApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (containValue != null && Objects.requireNonNull(getIntent()).getStringExtra("CHECKED_FROM_WHERE_ITS_COMING").equalsIgnoreCase(containValue)) {

                    dialogInterviewNow();

                } else {

                    dialogInterviewNow();


                }


            }
        });


    }

    private void dialogInterviewNow() {

        LayoutInflater li = LayoutInflater.from(ActivityCompanyDetails.this);
        View dialogView = li.inflate(R.layout.dialog_interview_now, null);

        IdsForInterviewNow(dialogView);

        alertDialogBuilder = new AlertDialog.Builder(ActivityCompanyDetails.this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void IdsForInterviewNow(View dialogView) {

        cardInterviewNow = dialogView.findViewById(R.id.cardInterviewNow);
        cardCancel = dialogView.findViewById(R.id.cardCancel);
        tvJobma = dialogView.findViewById(R.id.tvJobma);
        tvJobma.setTypeface(tvJobma.getTypeface(), Typeface.BOLD);


        cardInterviewNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (containValue != null && Objects.requireNonNull(getIntent()).getStringExtra("CHECKED_FROM_WHERE_ITS_COMING").equalsIgnoreCase(containValue)) {

                    Intent intent = new Intent(ActivityCompanyDetails.this, ActivityInterviewNow.class);
                    intent.putExtra("job_id", jobId);
                    startActivity(intent);
                    alertDialog.dismiss();

                } else {

                    Intent intent = new Intent(ActivityCompanyDetails.this, ActivityInterviewNow.class);
                    intent.putExtra("job_id", jobId);
                    startActivity(intent);
                    alertDialog.dismiss();

                }


            }
        });


        cardCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

    }


}
