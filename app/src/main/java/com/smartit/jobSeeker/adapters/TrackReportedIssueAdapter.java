package com.smartit.jobSeeker.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.activities.ActivityInteraction;
import com.smartit.jobSeeker.activities.ActivityTrackReportedIssue;
import com.smartit.jobSeeker.apiResponse.trackReportedIssue.ReportList;

import java.util.ArrayList;

public class TrackReportedIssueAdapter extends RecyclerView.Adapter<TrackReportedIssueAdapter.ViewHolder> {


    private Context mCtx;
    private View mView;
    private ArrayList<ReportList> reportList;

    public TrackReportedIssueAdapter(ActivityTrackReportedIssue activityTrackReportedIssue, ArrayList<ReportList> reportLists) {

        this.mCtx = activityTrackReportedIssue;
        this.reportList = reportLists;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        mView = inflater.inflate(R.layout.row_track_reported_issue, parent, false);
        return new TrackReportedIssueAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ReportList list = reportList.get(position);

        holder.tvTicket.setText(list.getJobmaTicketId());
        holder.tvReportedOn.setText(list.getCreatedAt());
        holder.tvTitle.setText(list.getSubject());

        if (list.getIssueStatus().equalsIgnoreCase("Pending")) {

            holder.tvStatus.setText(list.getIssueStatus());
            holder.tvStatus.setTextColor(Color.parseColor("#FF0000"));
            holder.roundView.setBackground(ContextCompat.getDrawable(mCtx, R.drawable.pending));

        } else {

            holder.tvStatus.setText(list.getIssueStatus());
            holder.tvStatus.setTextColor(Color.parseColor("#5ec639"));
            holder.roundView.setBackground(ContextCompat.getDrawable(mCtx, R.drawable.complete));
        }


        holder.rowClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(mCtx, ActivityInteraction.class);
                intent.putExtra("contactId", list.getJobmaContactId());
                mCtx.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private View roundView;
        private TextView tvTicket, tvReportedOn, tvStatus, tvTitle;
        private ImageView imageForward;

        private RelativeLayout rowClick;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            roundView = itemView.findViewById(R.id.roundView);
            tvTicket = itemView.findViewById(R.id.tvTicket);
            imageForward = itemView.findViewById(R.id.imageForward);
            tvReportedOn = itemView.findViewById(R.id.tvReportedOn);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTitle = itemView.findViewById(R.id.tvTitle);

            rowClick = itemView.findViewById(R.id.rowClick);

        }
    }
}
