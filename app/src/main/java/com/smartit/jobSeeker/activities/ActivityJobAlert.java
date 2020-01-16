package com.smartit.jobSeeker.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.adapters.JobAlertAdapter;
import com.smartit.jobSeeker.apiResponse.jobAlert.Datum;
import com.smartit.jobSeeker.apiResponse.jobAlert.JobAlert;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityJobAlert extends AppCompatActivity {


    private ImageView backArrowImage;
    private RecyclerView mRecyclerView;
    private LinearLayout liAdd;

    private JobAlertAdapter jobAlertAdapter;
    private boolean isAddBtnChecked;

    private ProgressDialog mProgressDialog;

    private TextView tvNoDataAvail;

    private ArrayList<Datum> datumArrayList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_alert);
        mProgressDialog = new ProgressDialog(ActivityJobAlert.this, R.style.AppTheme_Dark_Dialog);

        initView();
        loadingProgress();


    }


    @Override
    protected void onResume() {
        super.onResume();
        loadingProgress();
        datumArrayList.clear();
        jobAlertApi();

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void loadingProgress() {

        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();


    }

    private void jobAlertApi() {

        HttpModule.provideRepositoryService().jobAlertApi(Hawk.get("GET_TOKEN")).enqueue(new Callback<JobAlert>() {
            @Override
            public void onResponse(Call<JobAlert> call, Response<JobAlert> response) {


                if (response.body() != null && response.body().getError() == 0) {
                    mProgressDialog.dismiss();
                    datumArrayList.addAll(response.body().getData());

                    if (datumArrayList.size() > 0) {

                        tvNoDataAvail.setVisibility(View.GONE);
                        initAdapter(datumArrayList); // response.body().getData()
                    } else {
                        tvNoDataAvail.setVisibility(View.VISIBLE);
                    }


                } else {

                    mProgressDialog.dismiss();
                    tvNoDataAvail.setVisibility(View.VISIBLE);
                    Toast.makeText(ActivityJobAlert.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JobAlert> call, Throwable t) {
                mProgressDialog.dismiss();
                System.out.println("ActivityJobAlert.onFailure " + t.getMessage());
            }
        });


    }

    private void initAdapter(List<Datum> data) {

        jobAlertAdapter = new JobAlertAdapter(this, data);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(jobAlertAdapter);


    }

    private void initView() {

        backArrowImage = findViewById(R.id.backArrowImage);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        liAdd = findViewById(R.id.liAdd);

        tvNoDataAvail = findViewById(R.id.tvNoDataAvail);

        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        liAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                isAddBtnChecked = true;
                Intent intent = new Intent(ActivityJobAlert.this, ActivityAddEditJobAlert.class);
                intent.putExtra("ADD_BUTTON_CLICKED", true);
                startActivity(intent);


            }
        });


    }


}
