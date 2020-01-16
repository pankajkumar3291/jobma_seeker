package com.smartit.jobSeeker.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.sdsmdg.tastytoast.TastyToast;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.activities.ActivityDashboard;
import com.smartit.jobSeeker.activities.ActivityInterviewNow;
import com.smartit.jobSeeker.activities.ActivityRescheduled;
import com.smartit.jobSeeker.apiResponse.inviationModule.InvitationList;
import com.smartit.jobSeeker.apiResponse.invitationAccept.InvitationAccept;
import com.smartit.jobSeeker.apiResponse.invitationReject.InvitationReject;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.ViewHolder> {


    private Context mCtx;
    private View mView;
    private List<InvitationList> invitelist;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private String AuthorizationToekn;


    private CardView cardInterviewNow, cardCancel, cardAccept, cardReject, cardRescheduled, cardCancelForAccept;

    public InvitationAdapter(FragmentActivity activity, List<InvitationList> invitaionList, String toekn) {

        this.mCtx = activity;
        this.invitelist = invitaionList;
        this.AuthorizationToekn = toekn;

        setHasStableIds(true);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        mView = inflater.inflate(R.layout.row_inviation_adapter, viewGroup, false);
        return new InvitationAdapter.ViewHolder(mView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {


        final InvitationList invitationList = invitelist.get(position);


        if (invitationList.getInterviewStatus().equalsIgnoreCase("0")) {

            viewHolder.imageThreeDots.setVisibility(View.VISIBLE);
            viewHolder.tvJobTitle.setText(invitationList.getJobTitle());
            viewHolder.tvJobTitle.setTypeface(viewHolder.tvJobTitle.getTypeface(), Typeface.BOLD);


            if (invitationList.getCompanyName().equalsIgnoreCase("")) {
                viewHolder.tvCompany.setText("N/A");
            } else {
                viewHolder.tvCompany.setText(invitationList.getCompanyName());
            }


            viewHolder.tvInterviewMode.setText(invitationList.getInterviewMode());
            viewHolder.tvDate.setText(invitationList.getInvitationDate());

            viewHolder.tvJobTitleText.setTypeface(viewHolder.tvJobTitleText.getTypeface(), Typeface.BOLD);
            viewHolder.tvCompanyText.setTypeface(viewHolder.tvCompanyText.getTypeface(), Typeface.BOLD);
            viewHolder.textInterviewMode.setTypeface(viewHolder.textInterviewMode.getTypeface(), Typeface.BOLD);
            viewHolder.textDate.setTypeface(viewHolder.textDate.getTypeface(), Typeface.BOLD);
            viewHolder.textTimeZone.setTypeface(viewHolder.textTimeZone.getTypeface(), Typeface.BOLD);
            viewHolder.textTime.setTypeface(viewHolder.textTime.getTypeface(), Typeface.BOLD);


            if (invitationList.getTimezone().equalsIgnoreCase("")) {
//                viewHolder.tvTimeZone.setText("Not Available");
                viewHolder.tvTimeZone.setVisibility(View.GONE);
                viewHolder.textTimeZone.setVisibility(View.GONE);

                viewHolder.reFive.setVisibility(View.GONE);
                viewHolder.reSix.setVisibility(View.GONE);

            } else {
                viewHolder.tvTimeZone.setText(invitationList.getTimezone());
                viewHolder.textTimeZone.setVisibility(View.VISIBLE);
                viewHolder.textTimeZone.setTypeface(viewHolder.textTimeZone.getTypeface(), Typeface.BOLD);

            }
            if (invitationList.getTime().equalsIgnoreCase("")) {
//                viewHolder.tvTime.setText("Not Available");
                viewHolder.tvTime.setVisibility(View.GONE);
                viewHolder.textTime.setVisibility(View.GONE);
            } else {
                viewHolder.tvTime.setText(invitationList.getTime());
                viewHolder.textTime.setVisibility(View.VISIBLE);
                viewHolder.textTime.setTypeface(viewHolder.textTime.getTypeface(), Typeface.BOLD);
                viewHolder.textTimeZone.setTypeface(viewHolder.textTimeZone.getTypeface(), Typeface.BOLD);
            }


        } else if (invitationList.getInterviewStatus().equalsIgnoreCase("1")) {


            viewHolder.tvJobTitle.setText(invitationList.getJobTitle());
            viewHolder.tvJobTitle.setTypeface(viewHolder.tvJobTitle.getTypeface(), Typeface.BOLD);

            if (invitationList.getCompanyName().equalsIgnoreCase("")) {
                viewHolder.tvCompany.setText("N/A");
            } else {
                viewHolder.tvCompany.setText(invitationList.getCompanyName());
            }


            viewHolder.tvInterviewMode.setText(invitationList.getInterviewMode());
            viewHolder.tvDate.setText(invitationList.getInvitationDate());
//            viewHolder.tvTimeZone.setText(invitationList.getTimezone());
//            viewHolder.tvTime.setText(invitationList.getTime());
            viewHolder.imageAction.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.new_in_progress)); // new_in_progress
            viewHolder.imageThreeDots.setVisibility(View.GONE);
            viewHolder.reFive.setVisibility(View.GONE);
            viewHolder.reSix.setVisibility(View.GONE);

            viewHolder.tvJobTitleText.setTypeface(viewHolder.tvJobTitleText.getTypeface(), Typeface.BOLD);
            viewHolder.tvCompanyText.setTypeface(viewHolder.tvCompanyText.getTypeface(), Typeface.BOLD);
            viewHolder.textInterviewMode.setTypeface(viewHolder.textInterviewMode.getTypeface(), Typeface.BOLD);
            viewHolder.textDate.setTypeface(viewHolder.textDate.getTypeface(), Typeface.BOLD);
            viewHolder.textTimeZone.setTypeface(viewHolder.textTimeZone.getTypeface(), Typeface.BOLD);

            if (invitationList.getTimezone().equalsIgnoreCase("")) {
//                viewHolder.tvTimeZone.setText("Not Available");
                viewHolder.tvTimeZone.setVisibility(View.GONE);
                viewHolder.textTimeZone.setVisibility(View.GONE);
                viewHolder.reFive.setVisibility(View.GONE);
                viewHolder.reSix.setVisibility(View.GONE);
            } else {
                viewHolder.tvTimeZone.setText(invitationList.getTimezone());
                viewHolder.textTimeZone.setVisibility(View.VISIBLE);
                viewHolder.textTimeZone.setTypeface(viewHolder.textTimeZone.getTypeface(), Typeface.BOLD);
            }
            if (invitationList.getTime().equalsIgnoreCase("")) {
//                viewHolder.tvTime.setText("Not Available");
                viewHolder.tvTime.setVisibility(View.GONE);
                viewHolder.textTime.setVisibility(View.GONE);
            } else {
                viewHolder.tvTime.setText(invitationList.getTime());
                viewHolder.textTime.setVisibility(View.VISIBLE);
                viewHolder.textTime.setTypeface(viewHolder.textTime.getTypeface(), Typeface.BOLD);
                viewHolder.textTimeZone.setTypeface(viewHolder.textTimeZone.getTypeface(), Typeface.BOLD);
            }


        } else if (invitationList.getInterviewStatus().equalsIgnoreCase("2")) {


            viewHolder.tvJobTitle.setText(invitationList.getJobTitle());
            viewHolder.tvJobTitle.setTypeface(viewHolder.tvJobTitle.getTypeface(), Typeface.BOLD);

            if (invitationList.getCompanyName().equalsIgnoreCase("")) {
                viewHolder.tvCompany.setText("N/A");
            } else {
                viewHolder.tvCompany.setText(invitationList.getCompanyName());
            }


            viewHolder.tvInterviewMode.setText(invitationList.getInterviewMode());
            viewHolder.tvDate.setText(invitationList.getInvitationDate());
//            viewHolder.tvTimeZone.setText(invitationList.getTimezone());
//            viewHolder.tvTime.setText(invitationList.getTime());
            viewHolder.imageAction.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.new_applied)); //new_applied
            viewHolder.imageThreeDots.setVisibility(View.GONE);
            viewHolder.reFive.setVisibility(View.GONE);
            viewHolder.reSix.setVisibility(View.GONE);

            viewHolder.tvJobTitleText.setTypeface(viewHolder.tvJobTitleText.getTypeface(), Typeface.BOLD);
            viewHolder.tvCompanyText.setTypeface(viewHolder.tvCompanyText.getTypeface(), Typeface.BOLD);
            viewHolder.textInterviewMode.setTypeface(viewHolder.textInterviewMode.getTypeface(), Typeface.BOLD);
            viewHolder.textDate.setTypeface(viewHolder.textDate.getTypeface(), Typeface.BOLD);
            viewHolder.textTimeZone.setTypeface(viewHolder.textTimeZone.getTypeface(), Typeface.BOLD);


            if (invitationList.getTimezone().equalsIgnoreCase("")) {
//                viewHolder.tvTimeZone.setText("Not Available");
                viewHolder.tvTimeZone.setVisibility(View.GONE);
                viewHolder.textTimeZone.setVisibility(View.GONE);
                viewHolder.reFive.setVisibility(View.GONE);
                viewHolder.reSix.setVisibility(View.GONE);
            } else {
                viewHolder.tvTimeZone.setText(invitationList.getTimezone());
                viewHolder.textTimeZone.setVisibility(View.VISIBLE);
                viewHolder.textTime.setTypeface(viewHolder.textTime.getTypeface(), Typeface.BOLD);
                viewHolder.textTimeZone.setTypeface(viewHolder.textTimeZone.getTypeface(), Typeface.BOLD);
            }
            if (invitationList.getTime().equalsIgnoreCase("")) {
//                viewHolder.tvTime.setText("Not Available");
                viewHolder.tvTime.setVisibility(View.GONE);
                viewHolder.textTime.setVisibility(View.GONE);
            } else {
                viewHolder.tvTime.setText(invitationList.getTime());
                viewHolder.textTime.setVisibility(View.VISIBLE);
                viewHolder.textTime.setTypeface(viewHolder.textTime.getTypeface(), Typeface.BOLD);
                viewHolder.textTimeZone.setTypeface(viewHolder.textTimeZone.getTypeface(), Typeface.BOLD);
            }


        } else if (invitationList.getInterviewStatus().equalsIgnoreCase("3")) {


            viewHolder.tvJobTitle.setText(invitationList.getJobTitle());
            viewHolder.tvJobTitle.setTypeface(viewHolder.tvJobTitle.getTypeface(), Typeface.BOLD);


            if (invitationList.getCompanyName().equalsIgnoreCase("")) {
                viewHolder.tvCompany.setText("N/A");
            } else {
                viewHolder.tvCompany.setText(invitationList.getCompanyName());
            }

            viewHolder.tvInterviewMode.setText(invitationList.getInterviewMode());
            viewHolder.tvDate.setText(invitationList.getInvitationDate());
//            viewHolder.tvTimeZone.setText(invitationList.getTimezone());
//            viewHolder.tvTime.setText(invitationList.getTime());
            viewHolder.imageAction.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.expired));
            viewHolder.imageThreeDots.setVisibility(View.GONE);
            viewHolder.reFive.setVisibility(View.GONE);
            viewHolder.reSix.setVisibility(View.GONE);

            viewHolder.tvJobTitleText.setTypeface(viewHolder.tvJobTitleText.getTypeface(), Typeface.BOLD);
            viewHolder.tvCompanyText.setTypeface(viewHolder.tvCompanyText.getTypeface(), Typeface.BOLD);
            viewHolder.textInterviewMode.setTypeface(viewHolder.textInterviewMode.getTypeface(), Typeface.BOLD);
            viewHolder.textDate.setTypeface(viewHolder.textDate.getTypeface(), Typeface.BOLD);
            viewHolder.textTimeZone.setTypeface(viewHolder.textTimeZone.getTypeface(), Typeface.BOLD);


            if (invitationList.getTimezone().equalsIgnoreCase("")) {
//                viewHolder.tvTimeZone.setText("Not Available");
                viewHolder.tvTimeZone.setVisibility(View.GONE);
                viewHolder.textTimeZone.setVisibility(View.GONE);
                viewHolder.reFive.setVisibility(View.GONE);
                viewHolder.reSix.setVisibility(View.GONE);
            } else {
                viewHolder.tvTimeZone.setText(invitationList.getTimezone());
                viewHolder.textTimeZone.setVisibility(View.VISIBLE);
                viewHolder.textTimeZone.setTypeface(viewHolder.textTimeZone.getTypeface(), Typeface.BOLD);
                viewHolder.textTimeZone.setTypeface(viewHolder.textTimeZone.getTypeface(), Typeface.BOLD);
            }
            if (invitationList.getTime().equalsIgnoreCase("")) {
//                viewHolder.tvTime.setText("Not Available");
                viewHolder.tvTime.setVisibility(View.GONE);
                viewHolder.textTime.setVisibility(View.GONE);
            } else {
                viewHolder.tvTime.setText(invitationList.getTime());
                viewHolder.textTime.setVisibility(View.VISIBLE);
                viewHolder.textTime.setTypeface(viewHolder.textTime.getTypeface(), Typeface.BOLD);
                viewHolder.textTimeZone.setTypeface(viewHolder.textTimeZone.getTypeface(), Typeface.BOLD);
            }


        } else if (invitationList.getInterviewStatus().equalsIgnoreCase("4")) {

            viewHolder.tvJobTitle.setText(invitationList.getJobTitle());
            viewHolder.tvJobTitle.setTypeface(viewHolder.tvJobTitle.getTypeface(), Typeface.BOLD);


            if (invitationList.getCompanyName().equalsIgnoreCase("")) {
                viewHolder.tvCompany.setText("N/A");
            } else {
                viewHolder.tvCompany.setText(invitationList.getCompanyName());
            }

            viewHolder.tvInterviewMode.setText(invitationList.getInterviewMode());
            viewHolder.tvDate.setText(invitationList.getInvitationDate());
//            viewHolder.tvTimeZone.setText(invitationList.getTimezone());
//            viewHolder.tvTime.setText(invitationList.getTime());
            viewHolder.imageAction.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.new_rejected)); //new_rejected
            viewHolder.imageThreeDots.setVisibility(View.GONE);
            viewHolder.reFive.setVisibility(View.GONE);
            viewHolder.reSix.setVisibility(View.GONE);


            viewHolder.tvJobTitleText.setTypeface(viewHolder.tvJobTitleText.getTypeface(), Typeface.BOLD);
            viewHolder.tvCompanyText.setTypeface(viewHolder.tvCompanyText.getTypeface(), Typeface.BOLD);
            viewHolder.textInterviewMode.setTypeface(viewHolder.textInterviewMode.getTypeface(), Typeface.BOLD);
            viewHolder.textDate.setTypeface(viewHolder.textDate.getTypeface(), Typeface.BOLD);
            viewHolder.textTimeZone.setTypeface(viewHolder.textTimeZone.getTypeface(), Typeface.BOLD);


            if (invitationList.getTimezone().equalsIgnoreCase("")) {
//                viewHolder.tvTimeZone.setText("Not Available");
                viewHolder.tvTimeZone.setVisibility(View.GONE);
                viewHolder.textTimeZone.setVisibility(View.GONE);
                viewHolder.reFive.setVisibility(View.GONE);
                viewHolder.reSix.setVisibility(View.GONE);
            } else {
                viewHolder.tvTimeZone.setText(invitationList.getTimezone());
                viewHolder.textTimeZone.setVisibility(View.VISIBLE);
                viewHolder.textTimeZone.setTypeface(viewHolder.textTimeZone.getTypeface(), Typeface.BOLD);
                viewHolder.textTimeZone.setTypeface(viewHolder.textTimeZone.getTypeface(), Typeface.BOLD);
            }
            if (invitationList.getTime().equalsIgnoreCase("")) {
//                viewHolder.tvTime.setText("Not Available");
                viewHolder.tvTime.setVisibility(View.GONE);
                viewHolder.textTime.setVisibility(View.GONE);
            } else {
                viewHolder.tvTime.setText(invitationList.getTime());
                viewHolder.textTime.setVisibility(View.VISIBLE);
                viewHolder.textTime.setTypeface(viewHolder.textTime.getTypeface(), Typeface.BOLD);
                viewHolder.textTimeZone.setTypeface(viewHolder.textTimeZone.getTypeface(), Typeface.BOLD);

            }


        } else if (invitationList.getInterviewStatus().equalsIgnoreCase("5")) {

            viewHolder.tvJobTitle.setText(invitationList.getJobTitle());
            viewHolder.tvJobTitle.setTypeface(viewHolder.tvJobTitle.getTypeface(), Typeface.BOLD);

            if (invitationList.getCompanyName().equalsIgnoreCase("")) {
                viewHolder.tvCompany.setText("N/A");
            } else {
                viewHolder.tvCompany.setText(invitationList.getCompanyName());
            }

            viewHolder.tvInterviewMode.setText(invitationList.getInterviewMode());
            viewHolder.tvDate.setText(invitationList.getInvitationDate());
//            viewHolder.tvTimeZone.setText(invitationList.getTimezone());
//            viewHolder.tvTime.setText(invitationList.getTime());
            viewHolder.imageAction.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.new_rescheduled)); //new_rescheduled
            viewHolder.imageThreeDots.setVisibility(View.GONE);
            viewHolder.reFive.setVisibility(View.GONE);
            viewHolder.reSix.setVisibility(View.GONE);


            viewHolder.tvJobTitleText.setTypeface(viewHolder.tvJobTitleText.getTypeface(), Typeface.BOLD);
            viewHolder.tvCompanyText.setTypeface(viewHolder.tvCompanyText.getTypeface(), Typeface.BOLD);
            viewHolder.textInterviewMode.setTypeface(viewHolder.textInterviewMode.getTypeface(), Typeface.BOLD);
            viewHolder.textDate.setTypeface(viewHolder.textDate.getTypeface(), Typeface.BOLD);
            viewHolder.textTimeZone.setTypeface(viewHolder.textTimeZone.getTypeface(), Typeface.BOLD);


            if (invitationList.getTimezone().equalsIgnoreCase("")) {
//                viewHolder.tvTimeZone.setText("Not Available");
                viewHolder.tvTimeZone.setVisibility(View.GONE);
                viewHolder.textTimeZone.setVisibility(View.GONE);
                viewHolder.reFive.setVisibility(View.GONE);
                viewHolder.reSix.setVisibility(View.GONE);
            } else {
                viewHolder.tvTimeZone.setText(invitationList.getTimezone());
                viewHolder.textTimeZone.setVisibility(View.VISIBLE);
                viewHolder.textTimeZone.setTypeface(viewHolder.textTimeZone.getTypeface(), Typeface.BOLD);
                viewHolder.textTimeZone.setTypeface(viewHolder.textTimeZone.getTypeface(), Typeface.BOLD);
            }
            if (invitationList.getTime().equalsIgnoreCase("")) {
//                viewHolder.tvTime.setText("Not Available");
                viewHolder.tvTime.setVisibility(View.GONE);
                viewHolder.textTime.setVisibility(View.GONE);
            } else {
                viewHolder.tvTime.setText(invitationList.getTime());
                viewHolder.textTime.setVisibility(View.VISIBLE);
                viewHolder.textTime.setTypeface(viewHolder.textTime.getTypeface(), Typeface.BOLD);
                viewHolder.textTimeZone.setTypeface(viewHolder.textTimeZone.getTypeface(), Typeface.BOLD);
            }

        }


        viewHolder.imageThreeDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (invitationList.getMode().equalsIgnoreCase("2")) {

                    dialogForAcceptRejectReschedule(position, invitationList.getId()); //getJobId()

                } else if (invitationList.getMode().equalsIgnoreCase("1")) {

                    dialogForInterviewNow(position, invitationList.getJobId()); //getJobId()
                }


            }
        });


    }

    private void dialogForInterviewNow(int position, Integer id) {


        LayoutInflater li = LayoutInflater.from(mCtx);
        View dialogView = li.inflate(R.layout.dialog_interview_now, null);

        findingIdsForInterviewNow(dialogView, id);

        alertDialogBuilder = new AlertDialog.Builder(mCtx);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void findingIdsForInterviewNow(View dialogView, final Integer id) {

        cardInterviewNow = dialogView.findViewById(R.id.cardInterviewNow);
        cardCancel = dialogView.findViewById(R.id.cardCancel);


        cardInterviewNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mCtx, ActivityInterviewNow.class);
                intent.putExtra("job_id", id);
                mCtx.startActivity(intent);
                alertDialog.dismiss();

                System.out.println("InterviewModeAdapter.onClick Shaikh The dhimkana---" + id);


            }
        });


        cardCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });


    }

    private void dialogForAcceptRejectReschedule(int position, Integer id) {


        LayoutInflater li = LayoutInflater.from(mCtx);
        View dialogView = li.inflate(R.layout.dialog_accep_reject_reschedule, null);

        findingIdsForAcceptRejectRescheduled(dialogView, id, position);

        alertDialogBuilder = new AlertDialog.Builder(mCtx);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder
                .setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void findingIdsForAcceptRejectRescheduled(View dialogView, final Integer id, final int position) {

        cardAccept = dialogView.findViewById(R.id.cardAccept);
        cardReject = dialogView.findViewById(R.id.cardReject);
        cardRescheduled = dialogView.findViewById(R.id.cardRescheduled);
        cardCancelForAccept = dialogView.findViewById(R.id.cardCancelForAccept);


        // CLICK GOES HERE

        cardAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                HttpModule.provideRepositoryService().invitationAcceptForJobSeeker(AuthorizationToekn, id, 0).enqueue(new Callback<InvitationAccept>() {
                    @Override
                    public void onResponse(Call<InvitationAccept> call, Response<InvitationAccept> response) {


                        if (response.body() != null && response.body().getError() == 0) {

                            new AwesomeSuccessDialog(mCtx)
                                    .setTitle("")
                                    .setMessage(response.body().getMessage())
                                    .setColoredCircle(R.color.colorlogin)
                                    .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
                                    .setCancelable(false)
                                    .setPositiveButtonText("Ok")
                                    .setPositiveButtonbackgroundColor(R.color.colorlogin)
                                    .setPositiveButtonTextColor(R.color.white)
                                    .setPositiveButtonClick(new Closure() {
                                        @Override
                                        public void exec() {


                                        }
                                    })
                                    .show();


                        } else {


                            new AwesomeErrorDialog(mCtx)
                                    .setTitle("Oops")
                                    .setMessage(Objects.requireNonNull(response.body()).getMessage())
                                    .setColoredCircle(R.color.dialogErrorBackgroundColor)
                                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                                    .setCancelable(true).setButtonText(mCtx.getString(R.string.dialog_ok_button))
                                    .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                                    .setButtonText(mCtx.getString(R.string.dialog_ok_button))
                                    .setErrorButtonClick(new Closure() {
                                        @Override
                                        public void exec() {


                                        }
                                    })
                                    .show();
                        }


                    }

                    @Override
                    public void onFailure(Call<InvitationAccept> call, Throwable t) {

                        TastyToast.makeText(mCtx, t.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }
                });


            }
        });


        cardReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                HttpModule.provideRepositoryService().invitationRejectForJobSeeker(AuthorizationToekn, id, 1).enqueue(new Callback<InvitationReject>() {
                    @Override
                    public void onResponse(Call<InvitationReject> call, Response<InvitationReject> response) {


                        if (response.body() != null && response.body().getError() == 0) {

                            alertDialog.dismiss();
                            new AwesomeSuccessDialog(mCtx)
                                    .setTitle("")
                                    .setMessage(response.body().getMessage())
                                    .setColoredCircle(R.color.colorlogin)
                                    .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
                                    .setCancelable(false)
                                    .setPositiveButtonText("Ok")
                                    .setPositiveButtonbackgroundColor(R.color.colorlogin)
                                    .setPositiveButtonTextColor(R.color.white)
                                    .setPositiveButtonClick(new Closure() {
                                        @Override
                                        public void exec() {
                                            alertDialog.dismiss();
                                            ((ActivityDashboard) mCtx).refreshFragmentFromInvitation();

                                        }
                                    })
                                    .show();

                        } else {

                            new AwesomeErrorDialog(mCtx)
                                    .setTitle("Oops")
                                    .setMessage(Objects.requireNonNull(response.body()).getMessage())
                                    .setColoredCircle(R.color.dialogErrorBackgroundColor)
                                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                                    .setCancelable(true).setButtonText(mCtx.getString(R.string.dialog_ok_button))
                                    .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                                    .setButtonText(mCtx.getString(R.string.dialog_ok_button))
                                    .setErrorButtonClick(new Closure() {
                                        @Override
                                        public void exec() {


                                        }
                                    })
                                    .show();
                        }


                    }

                    @Override
                    public void onFailure(Call<InvitationReject> call, Throwable t) {

                        TastyToast.makeText(mCtx, t.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }
                });


            }
        });


        cardRescheduled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callTheRescheduleActivityFromHere(invitelist.get(position).getId());

            }
        });


        cardCancelForAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

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

        return invitelist.size();
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


        private TextView tvJobTitle, tvCompany, textInterviewMode, tvInterviewMode,
                textDate, tvDate, textTimeZone, tvTimeZone, textTime, tvTime, tvJobTitleText, tvCompanyText;


        private ImageView imageAction, imageThreeDots;
        private RelativeLayout reFive, reSix;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvCompany = itemView.findViewById(R.id.tvCompany);

            textInterviewMode = itemView.findViewById(R.id.textInterviewMode);
            tvInterviewMode = itemView.findViewById(R.id.tvInterviewMode);

            textDate = itemView.findViewById(R.id.textDate);
            tvDate = itemView.findViewById(R.id.tvDate);

            textTimeZone = itemView.findViewById(R.id.textTimeZone);
            tvTimeZone = itemView.findViewById(R.id.tvTimeZone);

            textTime = itemView.findViewById(R.id.textTime);
            tvTime = itemView.findViewById(R.id.tvTime);

            imageAction = itemView.findViewById(R.id.imageAction);
            imageThreeDots = itemView.findViewById(R.id.imageThreeDots);


            tvJobTitleText = itemView.findViewById(R.id.tvJobTitleText);
            tvCompanyText = itemView.findViewById(R.id.tvCompanyText);
            reFive = itemView.findViewById(R.id.reFive);
            reSix = itemView.findViewById(R.id.reSix);


        }


    }
}
