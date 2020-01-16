package com.smartit.jobSeeker.adapters;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.activities.ActivityCompanyDetails;
import com.smartit.jobSeeker.activities.ActivityDashboard;
import com.smartit.jobSeeker.activities.ActivityInterviewNow;
import com.smartit.jobSeeker.activities.ActivityLiveInterview;
import com.smartit.jobSeeker.activities.ActivityRescheduled;
import com.smartit.jobSeeker.apiResponse.interview_list_mode.InterviewList;
import com.smartit.jobSeeker.apiResponse.invitationAccept.InvitationAccept;
import com.smartit.jobSeeker.apiResponse.invitationReject.InvitationReject;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class InterviewModeAdapter extends RecyclerView.Adapter<InterviewModeAdapter.ViewHolder> {

    private Context mCtx;
    private View view;
    private List<InterviewList> interviewLists;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private CardView cardInterviewNow, cardCancel, cardAccept, cardReject, cardRescheduled, cardCancelForAccept;
    private TextView tvJobma, tvJobmaAccept;

    public InterviewModeAdapter(FragmentActivity activity, List<InterviewList> interviewList) {
        this.mCtx = activity;
        this.interviewLists = interviewList;
    }

    @NonNull
    @Override
    public InterviewModeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        view = inflater.inflate(R.layout.row_interview_mode_adapter, viewGroup, false);
        return new InterviewModeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterviewModeAdapter.ViewHolder viewHolder, final int position) {
        final InterviewList interviewList = interviewLists.get(position);

//        viewHolder.tvDesig.setTypeface(viewHolder.tvDesig.getTypeface(), Typeface.BOLD);
//        viewHolder.tvUserDesignation.setTypeface(viewHolder.tvUserDesignation.getTypeface(), Typeface.BOLD);

        viewHolder.interviewModetextForLive.setTypeface(viewHolder.interviewModetextForLive.getTypeface(), Typeface.BOLD);
        viewHolder.interviewModeTextForPreR.setTypeface(viewHolder.interviewModeTextForPreR.getTypeface(), Typeface.BOLD);
        viewHolder.tvDateAndTime.setTypeface(viewHolder.tvDateAndTime.getTypeface(), Typeface.BOLD);
        viewHolder.tvToken.setText(interviewList.getJobmaInterviewToken());

        if (interviewList.getInterviewMode().equalsIgnoreCase("Live Interview")) {
            viewHolder.tvCompany.setText(interviewList.getCompanyName());
            viewHolder.tvUserDesignation.setText(interviewList.getJobTitle());
            viewHolder.tvInterviewModeForLive.setText(interviewList.getInterviewMode());
            viewHolder.tvDateTime.setText(interviewList.getInvitationDate() + " - " + interviewList.getTime());
            viewHolder.tvTimeZone.setText(interviewList.getTimezone());
            viewHolder.tvPreRecordedInterview.setVisibility(View.GONE);
            viewHolder.interviewModeTextForPreR.setVisibility(View.GONE);
        } else {
            viewHolder.tvInterviewModeForLive.setVisibility(View.GONE);
            viewHolder.interviewModetextForLive.setVisibility(View.GONE);
            viewHolder.tvTimeZone.setVisibility(View.GONE);
            viewHolder.timeZonetext.setVisibility(View.GONE);
            viewHolder.tvCompany.setText(interviewList.getCompanyName());
            viewHolder.tvUserDesignation.setText(interviewList.getJobTitle());
            viewHolder.tvPreRecordedInterview.setText(interviewList.getInterviewMode());
            viewHolder.tvDateTime.setText(interviewList.getInvitationDate());
        }

        viewHolder.imageThreeDotsDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interviewList.getJobmaInterviewMode().equalsIgnoreCase("2")) {
                    dialogForAcceptRejectReschedule(position, interviewList.getId(),viewHolder.frameLayout);
//                    dialogLiveInterview();
                    viewHolder.frameLayout.setVisibility(View.VISIBLE);
                } else if (interviewList.getJobmaInterviewMode().equalsIgnoreCase("1")) {
                    dialogForInterviewNow(position, interviewList.getJobId());
                }
            }
        });

        viewHolder.linearWhole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.frameLayout.setVisibility(View.GONE);
                Intent intent = new Intent(mCtx, ActivityCompanyDetails.class);
                intent.putExtra("jobId", interviewList.getJobId());
                intent.putExtra("invitationId", interviewList.getId());
                mCtx.startActivity(intent);
            }
        });

        viewHolder.tvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = viewHolder.tvToken.getText().toString();
                viewHolder.myClip = ClipData.newPlainText("text", text);
                viewHolder.myClipboard.setPrimaryClip(viewHolder.myClip);
                Toast.makeText(getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.tvGoToLiveInterview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent liveInterview = new Intent(mCtx, ActivityLiveInterview.class);
                liveInterview.putExtra("invitedId", interviewList.getId());
                mCtx.startActivity(liveInterview);
            }
        });
    }

    private void dialogLiveInterview() {
        LayoutInflater li = LayoutInflater.from(mCtx);
        View dialogView = li.inflate(R.layout.pop_up_for_live_interview, null);
        findingIdForLiveInterview(dialogView);
        alertDialogBuilder = new AlertDialog.Builder(mCtx);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void findingIdForLiveInterview(View dialogView) {

    }

    private void dialogForInterviewNow(int position, Integer jobId) {
        LayoutInflater li = LayoutInflater.from(mCtx);
        View dialogView = li.inflate(R.layout.dialog_interview_now, null);
        findingIdsForInterviewNow(dialogView, jobId);
        alertDialogBuilder = new AlertDialog.Builder(mCtx);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void findingIdsForInterviewNow(View dialogView, final Integer jobId) {
        cardInterviewNow = dialogView.findViewById(R.id.cardInterviewNow);
        cardCancel = dialogView.findViewById(R.id.cardCancel);
        tvJobma = dialogView.findViewById(R.id.tvJobma);
        tvJobma.setTypeface(tvJobma.getTypeface(), Typeface.BOLD);

        cardInterviewNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, ActivityInterviewNow.class);
                intent.putExtra("job_id", jobId);
                mCtx.startActivity(intent);
                alertDialog.dismiss();
            }
        });

        cardCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void dialogForAcceptRejectReschedule(int position, Integer id,FrameLayout liveInterviewLayout) {
        LayoutInflater li = LayoutInflater.from(mCtx);
        View dialogView = li.inflate(R.layout.dialog_accep_reject_reschedule_for_interview_mode, null);
        findingIdsForAcceptRejectRescheduled(dialogView, id, position,liveInterviewLayout);
        alertDialogBuilder = new AlertDialog.Builder(mCtx);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void findingIdsForAcceptRejectRescheduled(View dialogView, final Integer id, final int position,FrameLayout liveInterviewLayout) {
        cardAccept = dialogView.findViewById(R.id.cardAccept);
        cardReject = dialogView.findViewById(R.id.cardReject);
        cardRescheduled = dialogView.findViewById(R.id.cardRescheduled);
        cardCancelForAccept = dialogView.findViewById(R.id.cardCancelForAccept);
        tvJobmaAccept = dialogView.findViewById(R.id.tvJobmaAccept);
        tvJobmaAccept.setTypeface(tvJobmaAccept.getTypeface(), Typeface.BOLD);
        // CLICK GOES HERE

        cardAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HttpModule.provideRepositoryService().invitationAcceptForJobSeeker(Hawk.get("GET_TOKEN"), id, 0).enqueue(new Callback<InvitationAccept>() {
                    @Override
                    public void onResponse(Call<InvitationAccept> call, Response<InvitationAccept> response) {
                        if (response.body() != null && response.body().getError() == 0) {
                            alertDialog.dismiss();
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(mCtx, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    }
                    @Override
                    public void onFailure(Call<InvitationAccept> call, Throwable t) {
                        alertDialog.dismiss();
                        liveInterviewLayout.setVisibility(View.GONE);
                        TastyToast.makeText(mCtx, t.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }
                });

            }
        });

        cardReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HttpModule.provideRepositoryService().invitationRejectForJobSeeker((String) Hawk.get("GET_TOKEN"), id, 1).enqueue(new Callback<InvitationReject>() {
                    @Override
                    public void onResponse(Call<InvitationReject> call, Response<InvitationReject> response) {
                        if (response.body() != null && response.body().getError() == 0) {
                            alertDialog.dismiss();
                            liveInterviewLayout.setVisibility(View.GONE);
                            ((ActivityDashboard) mCtx).refreshFragment();
                        } else {
                            liveInterviewLayout.setVisibility(View.GONE);
                            Toast.makeText(mCtx, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<InvitationReject> call, Throwable t) {
                        alertDialog.dismiss();
                        liveInterviewLayout.setVisibility(View.GONE);
                        TastyToast.makeText(mCtx, t.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }
                });
            }
        });

        cardRescheduled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callTheRescheduleActivityFromHere(interviewLists.get(position).getId());
                alertDialog.dismiss();
                liveInterviewLayout.setVisibility(View.GONE);
            }
        });

        cardCancelForAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                liveInterviewLayout.setVisibility(View.GONE);
//                ((ActivityDashboard) mCtx).refreshFragment();

            }
        });
    }

    private void callTheRescheduleActivityFromHere(Integer id) {
        Intent intent = new Intent(mCtx, ActivityRescheduled.class);
        intent.putExtra("invitationId", id);
        mCtx.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return interviewLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCompany, tvInterviewModeForLive, tvPreRecordedInterview, tvDateTime, tvTimeZone,
                tvUserDesignation, timeZonetext, interviewModeTextForPreR,
                interviewModetextForLive, tvDesig;
        private ImageView imageThreeDotsDashboard;
        private CardView wholeCard;
        private LinearLayout linearWhole;
        private TextView tvDateAndTime;
        private FrameLayout frameLayout;
        private ClipboardManager myClipboard;
        private ClipData myClip;
        private TextView tvToken, tvCopy, tvGoToLiveInterview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCompany = itemView.findViewById(R.id.tvCompany);
            tvInterviewModeForLive = itemView.findViewById(R.id.tvInterviewModeForLive);
            tvPreRecordedInterview = itemView.findViewById(R.id.tvPreRecordedInterview);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvTimeZone = itemView.findViewById(R.id.tvTimeZone);
            tvUserDesignation = itemView.findViewById(R.id.tvUserDesignation);
            tvDesig = itemView.findViewById(R.id.tvDesig);
            timeZonetext = itemView.findViewById(R.id.timeZonetext);
            interviewModeTextForPreR = itemView.findViewById(R.id.interviewModeTextForPreR);
            interviewModetextForLive = itemView.findViewById(R.id.interviewModetextForLive);
            imageThreeDotsDashboard = itemView.findViewById(R.id.imageThreeDotsDashboard);
            wholeCard = itemView.findViewById(R.id.wholeCard);
            linearWhole = itemView.findViewById(R.id.linearWhole);
            tvDateAndTime = itemView.findViewById(R.id.tvDateAndTime);
            frameLayout = itemView.findViewById(R.id.frameLayout);
            tvToken = itemView.findViewById(R.id.tvToken);
            tvCopy = itemView.findViewById(R.id.tvCopy);
            myClipboard = (ClipboardManager) mCtx.getSystemService(CLIPBOARD_SERVICE);
            tvGoToLiveInterview = itemView.findViewById(R.id.tvGoToLiveInterview);
        }
    }
}
