package com.smartit.jobSeeker.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.activities.ActivityEditEducation;
import com.smartit.jobSeeker.activities.ActivityInteraction;
import com.smartit.jobSeeker.apiResponse.delete_education.DeleteEducationDetail;
import com.smartit.jobSeeker.apiResponse.getEducationDetails.Datum;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.ViewHolder> {


    private Context mCtx;
    private View mView;
    private List<Datum> list;


    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private Button btnOkay, btnCancel;


    public EducationAdapter(FragmentActivity activity, List<Datum> datumList) {

        this.mCtx = activity;
        this.list = datumList;


        Hawk.init(mCtx).build();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        mView = inflater.inflate(R.layout.row_education, parent, false);
        return new EducationAdapter.ViewHolder(mView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Datum datum = list.get(position);

        if (datum.getDegree().trim().equalsIgnoreCase("")) {
            holder.tvTitle.setText("N/A");
            holder.tvTitle.setTypeface(holder.tvTitle.getTypeface(), Typeface.BOLD);
        } else {
            holder.tvTitle.setText(datum.getDegree());
            holder.tvTitle.setTypeface(holder.tvTitle.getTypeface(), Typeface.BOLD);
        }

        if (datum.getInstitute().trim().equalsIgnoreCase("")) {
            holder.tvInstitution.setText("N/A");
        } else {
            holder.tvInstitution.setText(datum.getInstitute());
        }

        if (datum.getCity().trim().equalsIgnoreCase("") && datum.getState().trim().equalsIgnoreCase("") && datum.getCountry().trim().equalsIgnoreCase("")) {
            holder.tvLocation.setText("N/A");
        } else {
            holder.tvLocation.setText(datum.getCity() + ", " + datum.getState() + ", " + datum.getCountry());
        }

        if (datum.getStartdate().trim().equalsIgnoreCase("") && datum.getEnddate().trim().equalsIgnoreCase("")) {
            holder.tvFromDate.setText("N/A");
        } else {
            holder.tvFromDate.setText(datum.getStartdate() + " To " + datum.getEnddate());
        }

        if (datum.getSummary().trim().equalsIgnoreCase("")) {
            holder.tvProgramDescription.setText("Program Description : " + "N/A");
        } else {
            holder.tvProgramDescription.setText("Program Description : " + datum.getSummary());
            Spannable wordtoSpan = new SpannableString("Program Description : " + datum.getSummary());
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 0, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvProgramDescription.setText(wordtoSpan);
        }


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                warningDilaog(datum);


//                list.remove(datum);
//
//                HttpModule.provideRepositoryService().deleteEducationApi(Hawk.get("GET_TOKEN"), datum.getId().toString()).enqueue(new Callback<DeleteEducationDetail>() {
//                    @Override
//                    public void onResponse(Call<DeleteEducationDetail> call, Response<DeleteEducationDetail> response) {
//
//
//                        if (response.body() != null && response.body().getError() == 0) {
//                            Toast.makeText(mCtx, response.body().getMessage(), Toast.LENGTH_LONG).show();
//                            notifyDataSetChanged();
//
//                        } else {
//                            Toast.makeText(mCtx, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
//                            System.out.println("EducationAdapter.onResponse ");
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<DeleteEducationDetail> call, Throwable t) {
//
//                        System.out.println("EducationAdapter.onFailure " + t.toString());
//
//                    }
//                });


            }
        });


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mCtx, ActivityEditEducation.class);
                intent.putExtra("Edi_Click", true);
                intent.putExtra("ID", datum.getId().toString());
                mCtx.startActivity(intent);


            }
        });


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
                list.remove(datum);

                HttpModule.provideRepositoryService().deleteEducationApi(Hawk.get("GET_TOKEN"), datum.getId().toString()).enqueue(new Callback<DeleteEducationDetail>() {
                    @Override
                    public void onResponse(Call<DeleteEducationDetail> call, Response<DeleteEducationDetail> response) {


                        if (response.body() != null && response.body().getError() == 0) {
                            Toast.makeText(mCtx, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            notifyDataSetChanged();

                        } else {
                            Toast.makeText(mCtx, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                            System.out.println("EducationAdapter.onResponse ");
                        }


                    }

                    @Override
                    public void onFailure(Call<DeleteEducationDetail> call, Throwable t) {

                        System.out.println("EducationAdapter.onFailure " + t.toString());

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

        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvInstitution, tvLocation, tvFromDate, tvToDate, tvProgramDescription;


        private ImageView delete, edit;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvInstitution = itemView.findViewById(R.id.tvInstitution);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvFromDate = itemView.findViewById(R.id.tvFromDate);
            tvToDate = itemView.findViewById(R.id.tvToDate);
            tvProgramDescription = itemView.findViewById(R.id.tvProgramDescription);


            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);


        }
    }


}
