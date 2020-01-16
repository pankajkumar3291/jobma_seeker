package com.smartit.jobSeeker.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.applicationHistory.appliedJobs.AppliedList;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ApplicationHistoryAdapter extends RecyclerView.Adapter<ApplicationHistoryAdapter.ViewHolder> {


    private Context mCtx;
    private View view;

    private List<AppliedList> appliedLists;

    public ApplicationHistoryAdapter(FragmentActivity activity, List<AppliedList> appliedJobsApplicationHistory) {

        this.mCtx = activity;
        this.appliedLists = appliedJobsApplicationHistory;

        setHasStableIds(true);

    }


    @NonNull
    @Override
    public ApplicationHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        LayoutInflater inflater = LayoutInflater.from(mCtx);
        view = inflater.inflate(R.layout.row_application_history_adapter, viewGroup , false);
        return new ApplicationHistoryAdapter.ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ApplicationHistoryAdapter.ViewHolder viewHolder, int position) {


        AppliedList list = appliedLists.get(position);

        viewHolder.tvJobTitle.setText(list.getTitle());
        viewHolder.tvCompany.setText(list.getCompanyname());
        viewHolder.tvPostedJobs.setText(list.getPosteddate());

        viewHolder.tvJobTitleText.setTypeface(viewHolder.tvJobTitleText.getTypeface(), Typeface.BOLD);
        viewHolder.tvJobTitle.setTypeface(viewHolder.tvJobTitle.getTypeface(), Typeface.BOLD);

        if (list.getAppliedMode().equalsIgnoreCase("0")) {

            viewHolder.imgText.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.video));

        } else if (list.getAppliedMode().equalsIgnoreCase("1")) {

            viewHolder.imgText.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.live_interview));
        }


        if (list.getStatus().equalsIgnoreCase("0")) {

            viewHolder.imageApplied.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.active));

            viewHolder.imageRejected.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.inactive));
            viewHolder.imageSelected.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.inactive));
            viewHolder.imageOnHold.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.inactive));

        }

        if (list.getStatus().equalsIgnoreCase("1")) {
            viewHolder.imageOnHold.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.active));

            viewHolder.imageApplied.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.inactive));
            viewHolder.imageSelected.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.inactive));
            viewHolder.imageRejected.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.inactive));

        }
        if (list.getStatus().equalsIgnoreCase("2")) {

            viewHolder.imageSelected.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.active));

            viewHolder.imageOnHold.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.inactive));
            viewHolder.imageApplied.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.inactive));
            viewHolder.imageRejected.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.inactive));

        }
        if (list.getStatus().equalsIgnoreCase("3")) {

            viewHolder.imageRejected.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.active));

            viewHolder.imageSelected.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.inactive));
            viewHolder.imageOnHold.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.inactive));
            viewHolder.imageApplied.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.inactive));

        }


    }


    @Override
    public int getItemCount() {

        return appliedLists.size();
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

        private TextView tvJobTitle, tvCompany, tvPostedJobs, tvJobTitleText;
        private ImageView imgText, imageApplied, imageOnHold, imageSelected, imageRejected;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvCompany = itemView.findViewById(R.id.tvCompany);
            tvPostedJobs = itemView.findViewById(R.id.tvPostedJobs);

            tvJobTitleText = itemView.findViewById(R.id.tvJobTitleText);


            imgText = itemView.findViewById(R.id.imgText);

            imageApplied = itemView.findViewById(R.id.imageApplied);
            imageOnHold = itemView.findViewById(R.id.imageOnHold);
            imageSelected = itemView.findViewById(R.id.imageSelected);
            imageRejected = itemView.findViewById(R.id.imageRejected);


        }


    }


}
