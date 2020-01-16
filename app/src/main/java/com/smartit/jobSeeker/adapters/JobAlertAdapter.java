package com.smartit.jobSeeker.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.activities.ActivityAddEditJobAlert;
import com.smartit.jobSeeker.activities.ActivityJobAlert;
import com.smartit.jobSeeker.apiResponse.delete_education.DeleteEducationDetail;
import com.smartit.jobSeeker.apiResponse.jobAlert.Datum;
import com.smartit.jobSeeker.apiResponse.reportAnIssue.ReportAnIssue;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class JobAlertAdapter extends RecyclerView.Adapter<JobAlertAdapter.ViewHolder> {

    private Context mCtx;
    private View mView;
    private List<Datum> datumList;

    private String zero1, one1, two1;
    private String zero2, one2;
    private String zero3;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private Button btnOkay, btnCancel;


    public JobAlertAdapter(ActivityJobAlert activityJobAlert, List<Datum> datum) {

        this.mCtx = activityJobAlert;
        this.datumList = datum;
        setHasStableIds(true);

    }

    @NonNull
    @Override
    public JobAlertAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        mView = inflater.inflate(R.layout.row_job_alert, parent, false);
        return new JobAlertAdapter.ViewHolder(mView);

    }

    @Override
    public void onBindViewHolder(@NonNull JobAlertAdapter.ViewHolder holder, int position) {


        Datum datum = datumList.get(position);

        holder.tvJobName.setText("Job for " + datum.getName());
        holder.tvJobName.setTypeface(holder.tvJobName.getTypeface(), Typeface.BOLD);
        holder.tvExperience.setText(datum.getMinexp().toString() + " to " + datum.getMaxexp().toString() + " years ");
        holder.tvExpText.setTypeface(holder.tvExpText.getTypeface(), Typeface.BOLD);
        holder.tvNotiText.setTypeface(holder.tvNotiText.getTypeface(), Typeface.BOLD);


        if (datum.getNotification().equalsIgnoreCase("1")) {
            holder.tvNotification.setText("Daily");
        } else if (datum.getNotification().equalsIgnoreCase("2")) {
            holder.tvNotification.setText("Weekly");
        } else if (datum.getNotification().equalsIgnoreCase("3")) {
            holder.tvNotification.setText("Monthly");
        }


        if (datum.getIndustry().size() == 3) {

            zero1 = datum.getIndustry().get(0).getValue();
            one1 = datum.getIndustry().get(1).getValue();
            two1 = datum.getIndustry().get(2).getValue();

            holder.tvIndustry.setText(zero1 + "," + one1 + "," + two1);

        }
        else if (datum.getIndustry().size() == 2) {

            zero2 = datum.getIndustry().get(0).getValue();
            one2 = datum.getIndustry().get(1).getValue();

            holder.tvIndustry.setText(zero2 + "," + one2 + ",");
}

     else if (datum.getIndustry().size() == 1) {
            zero3 = datum.getIndustry().get(0).getValue();

            holder.tvIndustry.setText(zero3);
        }


        holder.tvSalary.setText(datum.getCurrencyType() + " " + datum.getMinsalary().toString() + " to " + datum.getMaxsalary().toString());


        holder.imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mCtx, ActivityAddEditJobAlert.class);
                intent.putExtra("BoolName", true);
                intent.putExtra("JOB_ALERT_ID", datum.getJobAlertId());

                mCtx.startActivity(intent);

            }
        });


        holder.imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                warningDilaog(datum);

            }
        });


        // PLAYING WITH INNER ADAPTER

        String[] elements = datum.getSkills().split(",");
        List<String> fixedLenghtList = Arrays.asList(elements);
        System.out.println("JobAlertAdapter.onBindViewHolder " + fixedLenghtList);

        SkillsJobAlertAdapter skillsJobAlertAdapter = new SkillsJobAlertAdapter(mCtx, fixedLenghtList);
        holder.mRecyclerView.setHasFixedSize(true);
        holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(mCtx, LinearLayoutManager.HORIZONTAL, false));
        holder.mRecyclerView.setAdapter(skillsJobAlertAdapter);


    }

    private void warningDilaog(Datum datum) {

        LayoutInflater li = LayoutInflater.from(mCtx);
        View dialogView = li.inflate(R.layout.dialog_ask, null);

        ids(dialogView, datum);

        alertDialogBuilder = new AlertDialog.Builder(mCtx);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void ids(View dialogView, Datum datum) {

        btnOkay = dialogView.findViewById(R.id.btnOkay);
        btnCancel = dialogView.findViewById(R.id.btnCancel);


        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

                datumList.remove(datum);
                HttpModule.provideRepositoryService().deleteJobAlertsApi(Hawk.get("GET_TOKEN"), String.valueOf(datum.getJobAlertId())).enqueue(new Callback<ReportAnIssue>() {
                    @Override
                    public void onResponse(Call<ReportAnIssue> call, Response<ReportAnIssue> response) {

                        if (response.body() != null && response.body().getError() == 0) {

                            notifyDataSetChanged();

                        } else {
                            Toast.makeText(mCtx, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<ReportAnIssue> call, Throwable t) {

                        System.out.println("JobAlertAdapter.onFailure " + t.getMessage());
                    }
                });


            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }

    @Override
    public int getItemCount() {

        return datumList.size();
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView tvJobName, tvExperience, tvNotification, tvIndustry, tvSalary;
        private RecyclerView mRecyclerView;
        private ImageView imageEdit, imageCancel;
        private boolean isEditBtnClicked = false;
        private TextView tvExpText, tvNotiText;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvJobName = itemView.findViewById(R.id.tvJobName);
            tvExperience = itemView.findViewById(R.id.tvExperience);
            tvNotification = itemView.findViewById(R.id.tvNotification);

            tvIndustry = itemView.findViewById(R.id.tvIndustry);
            tvSalary = itemView.findViewById(R.id.tvSalary);


            imageEdit = itemView.findViewById(R.id.imageEdit);
            imageCancel = itemView.findViewById(R.id.imageCancel);

            tvExpText = itemView.findViewById(R.id.tvExpText);
            tvNotiText = itemView.findViewById(R.id.tvNotiText);


            // INITIALIZE INNER ADAPTER HERE
            mRecyclerView = itemView.findViewById(R.id.mRecyclerView);


        }


    }


}
