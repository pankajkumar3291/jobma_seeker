package com.smartit.jobSeeker.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.TextUtils;
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
import com.smartit.jobSeeker.activities.ActivityEditEmploymentDetails;
import com.smartit.jobSeeker.apiResponse.delete_employment.DeleteEmploymentDetail;
import com.smartit.jobSeeker.apiResponse.getEmploymentDetails.Datum;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmploymentAdapter extends RecyclerView.Adapter<EmploymentAdapter.ViewHolder> {


    private Context mCtx;
    private View mView;
    private List<Datum> datumList;

    private ArrayList<String> indStringList = new ArrayList<>();

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private Button btnOkay, btnCancel;


    public EmploymentAdapter(FragmentActivity activity, List<Datum> data) {

        this.mCtx = activity;
        this.datumList = data;


    }

    @NonNull
    @Override
    public EmploymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        mView = inflater.inflate(R.layout.row_employment, parent, false);

        Hawk.init(mView.getContext()).build();

        return new EmploymentAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmploymentAdapter.ViewHolder holder, int position) {

        Datum datum = datumList.get(position);

//        holder.tvTitle.setText(datum.getTitle());

        holder.tvTitle.setText(datum.getTitle());
        holder.tvTitle.setTypeface(holder.tvTitle.getTypeface(), Typeface.BOLD);

        holder.tvComT.setTypeface(holder.tvComT.getTypeface(), Typeface.BOLD);
        holder.tvIndT.setTypeface(holder.tvIndT.getTypeface(), Typeface.BOLD);
        holder.tvLocT.setTypeface(holder.tvLocT.getTypeface(), Typeface.BOLD);
        holder.tvPosDesT.setTypeface(holder.tvPosDesT.getTypeface(), Typeface.BOLD);

        holder.tvCompany.setText(datum.getCompany());

        holder.tvLocation.setText(datum.getCity() + ", " + datum.getState() + ", " + datum.getCountry());

        holder.tvFromDate.setText(datum.getStartDate() + " To " + datum.getEndDate());
        holder.tvPositionDescription.setText(datum.getDesc());

        indStringList.clear();

        for (int i = 0; i < datum.getIndustry().size(); i++) {
            indStringList.add(datum.getIndustry().get(i).getValue());
        }

        String industry = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        industry = TextUtils.join(", ", indStringList);
//        }

        holder.tvIndustry.setText(industry);


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                warningDilaog(datumList, position, datum);
            }
        });


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mCtx, ActivityEditEmploymentDetails.class);
                intent.putExtra("Edi_Click", "1");
                intent.putExtra("ID", datum.getId().toString());
                Objects.requireNonNull(mCtx).startActivity(intent);


            }
        });


    }

    private void warningDilaog(List<Datum> datumList, int position, Datum datum) {

        LayoutInflater li = LayoutInflater.from(mCtx);
        View dialogView = li.inflate(R.layout.dialog_ask, null);

        ids(dialogView, datumList, position, datum);

        alertDialogBuilder = new AlertDialog.Builder(mCtx);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void ids(View dialogView, List<Datum> datumList, int position, Datum datum) {

        btnOkay = dialogView.findViewById(R.id.btnOkay);
        btnCancel = dialogView.findViewById(R.id.btnCancel);


        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                datumList.remove(datum);

                HttpModule.provideRepositoryService().deleteEmploymentApi(Hawk.get("GET_TOKEN"), datum.getId().toString()).enqueue(new Callback<DeleteEmploymentDetail>() {
                    @Override
                    public void onResponse(Call<DeleteEmploymentDetail> call, Response<DeleteEmploymentDetail> response) {

                        if (response.body() != null && response.body().getError() == 0) {

                            Toast.makeText(mCtx, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            notifyDataSetChanged();


                        } else {
                            Toast.makeText(mCtx, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<DeleteEmploymentDetail> call, Throwable t) {

                        System.out.println("EmploymentAdapter.onFailure " + t.toString());

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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvCompany, tvIndustry, tvLocation, tvFromDate, tvPositionDescription,
                tvComT, tvIndT, tvLocT, tvPosDesT;


        private ImageView delete, edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCompany = itemView.findViewById(R.id.tvCompany);
            tvIndustry = itemView.findViewById(R.id.tvIndustry);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvFromDate = itemView.findViewById(R.id.tvFromDate);
            tvPositionDescription = itemView.findViewById(R.id.tvPositionDescription);

            tvComT = itemView.findViewById(R.id.tvComT);
            tvIndT = itemView.findViewById(R.id.tvIndT);
            tvLocT = itemView.findViewById(R.id.tvLocT);
            tvPosDesT = itemView.findViewById(R.id.tvPosDesT);

            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);

        }


    }
}
