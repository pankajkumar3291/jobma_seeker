package com.smartit.jobSeeker.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.adapters.TrackReportedIssueAdapter;
import com.smartit.jobSeeker.apiResponse.trackReportedIssue.ReportList;
import com.smartit.jobSeeker.apiResponse.trackReportedIssue.TrackReportedIssue;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityTrackReportedIssue extends AppCompatActivity {


    private TextView tvReportedList, tvRILT;
    private ImageView backArrowImage;
    private RecyclerView mRecyclerview;
    private ProgressBar mProgressBar;
    private ProgressDialog dialog;

    private int remainingCount, visibleItems;
    private boolean isfirst = false;

    private boolean isLoading = false;


    private LinearLayoutManager layoutManager;
    private TrackReportedIssueAdapter trackReportedIssueAdapter;
    private ArrayList<ReportList> reportLists = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_reported_issue);
        Hawk.init(this).build();
        dialog = new ProgressDialog(this);

        trackReportedIssueAdapter = new TrackReportedIssueAdapter(this, reportLists);

        findTheIdsHere();
//        trackReportedIssueApi();
//        onScrolledPagination();


    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onResume() {
        super.onResume();


        visibleItems = 1;
        isfirst = true;
        if (reportLists.size() > 0)
            reportLists.clear();
        trackReportedIssueApi();
        onScrolledPagination();
    }

    private void onScrolledPagination() {


        mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //check for scroll down
                {
                    if (!isLoading && layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == reportLists.size() - 1) {
                        isLoading = true;
                        visibleItems += 10;
                        trackReportedIssueApi();
                    }
                }
            }
        });

    }

    private void trackReportedIssueApi() {


        //if (!ObjectUtil.isEmpty(this.apiKey))
        if (remainingCount > 0 || isfirst) {

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            } else
                mProgressBar.setVisibility(View.VISIBLE);

            HttpModule.provideRepositoryService().trackReportedIssue(Hawk.get("GET_TOKEN"), visibleItems, 10).enqueue(new Callback<TrackReportedIssue>() {
                @Override
                public void onResponse(Call<TrackReportedIssue> call, Response<TrackReportedIssue> response) {


                    if (response.body() != null && response.body().getError() == 0) {

                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog = null;

                        } else
                            mProgressBar.setVisibility(View.GONE);

                        System.out.println("response====:" + response.body());
                        if (response.body().getError() == 0) {
                            reportLists.addAll(response.body().getData().getReportList());
                            remainingCount = response.body().getData().getRemaining();
                            if (isfirst) {

                                isfirst = false;
                                mRecyclerview.setHasFixedSize(true);
                                mRecyclerview.setLayoutManager(layoutManager);
                                mRecyclerview.setAdapter(trackReportedIssueAdapter);
                            }
                            trackReportedIssueAdapter.notifyDataSetChanged();
                            tvReportedList.setText("Showing " + reportLists.size() + " Reported List");
                            isLoading = false;
                        } else {
                            Toast.makeText(ActivityTrackReportedIssue.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    } else {

                        Toast.makeText(ActivityTrackReportedIssue.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();

                    }

                }

                @Override
                public void onFailure(Call<TrackReportedIssue> call, Throwable t) {

                    mProgressBar.setVisibility(View.GONE);
                    System.out.println("ActivityTrackReportedIssue.onFailure");

                }
            });


        }


    }


    private void findTheIdsHere() {

        tvReportedList = findViewById(R.id.tvReportedList);
        mRecyclerview = findViewById(R.id.mRecyclerview);
        mProgressBar = findViewById(R.id.mProgressBar);
        backArrowImage = findViewById(R.id.backArrowImage);
        tvRILT = findViewById(R.id.tvRILT);
        tvRILT.setTypeface(tvRILT.getTypeface(), Typeface.BOLD);

        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        layoutManager = new LinearLayoutManager(this);


    }
}
