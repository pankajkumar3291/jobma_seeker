package com.smartit.jobSeeker.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.adapters.ApplicationHistoryAdapter;
import com.smartit.jobSeeker.apiResponse.applicationHistory.appliedJobs.AppliedJobsForAppHistory;
import com.smartit.jobSeeker.apiResponse.applicationHistory.appliedJobs.AppliedList;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ApplicationHistoryFragment extends Fragment implements View.OnClickListener {


    private Context mContext;
    private View view;

    private RecyclerView recyclerApplicationHistory;
    private ProgressBar progressApplicationHistory;
    private LinearLayout filterLinear;
    private ApplicationHistoryAdapter applicationHistoryAdapter;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private int offsetItesm = 1;
    private int totalCount;


    private List<AppliedList> appliedJobsApplicationHistory = new ArrayList<>();


    private ImageView imageCancel;
    private CheckBox checkBoxApplied, checkBoxOnHold, checkBoxSelected, checkBoxRejected;
    private Button btnApply, btnCancel;

    private String statusCheckApplied, statusCheckOnHold, statusCheckSelected, statusCheckRejected;
    private ArrayList<String> statusArray = new ArrayList<>();

    private String STATUS = "";
    private boolean isStatucChecked = false;

    private TextView tvNoDataAvailable;
    private NoInternetDialog noInternetDialog;
    private ProgressDialog mProgressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.application_history_fragment, container, false);

        findTheIdsFromHere(view);
        eventListener();

        noInternetDialog = new NoInternetDialog.Builder(this).build();
        mProgressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);

        loadingProgress();
        appliedJobsForApplicationHistoryGoesHere();


        return view;
    }

    private void loadingProgress() {

        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(CalligraphyContextWrapper.wrap(context));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }


    private void eventListener() {

        filterLinear.setOnClickListener(this);

    }

    private void appliedJobsForApplicationHistoryGoesHere() {


        if (appliedJobsApplicationHistory.size() < totalCount || totalCount == 0) {

            progressApplicationHistory.setVisibility(View.VISIBLE);

            HttpModule.provideRepositoryService().appliedJobsForJobSeeker(getArguments().getString("token"), offsetItesm, 10, STATUS).enqueue(new Callback<AppliedJobsForAppHistory>() {
                @Override
                public void onResponse(Call<AppliedJobsForAppHistory> call, Response<AppliedJobsForAppHistory> response) {


                    if (response.body() != null && response.body().getError() == 0) {

                        mProgressDialog.dismiss();
                        progressApplicationHistory.setVisibility(View.GONE);

                        if (response.body().getData().getAppliedList().size() == 0) {
                            tvNoDataAvailable.setVisibility(View.VISIBLE);

                        } else {
                            mProgressDialog.dismiss();
                            totalCount = Objects.requireNonNull(response.body()).getData().getTotalCount();
                            appliedJobsApplicationHistory.addAll(response.body().getData().getAppliedList());

                            if (appliedJobsApplicationHistory.size() == 0) {
                                tvNoDataAvailable.setVisibility(View.VISIBLE);
                            } else {
                                tvNoDataAvailable.setVisibility(View.GONE);
                            }

                            applicationHistoryAdapter.notifyDataSetChanged();
                        }


                    } else {

                        mProgressDialog.dismiss();
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                    }


                }

                @Override
                public void onFailure(Call<AppliedJobsForAppHistory> call, Throwable t) {

                    mProgressDialog.dismiss();
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    System.out.println("ApplicationHistoryFragment.onFailure " + t.toString());


                }
            });

        }


    }


    private void findTheIdsFromHere(View view) {

        recyclerApplicationHistory = view.findViewById(R.id.recyclerApplicationHistory);
        progressApplicationHistory = view.findViewById(R.id.progressApplicationHistory);
        filterLinear = view.findViewById(R.id.filterLinear);

        tvNoDataAvailable = view.findViewById(R.id.tvNoDataAvailable);

        applicationHistoryAdapterInitialization();


    }

    private void applicationHistoryAdapterInitialization() {


        applicationHistoryAdapter = new ApplicationHistoryAdapter(getActivity(), appliedJobsApplicationHistory);
        recyclerApplicationHistory.setHasFixedSize(true);
        recyclerApplicationHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerApplicationHistory.setAdapter(applicationHistoryAdapter);


        recyclerApplicationHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == appliedJobsApplicationHistory.size() - 1) {

                    offsetItesm += 10;

                    if (!isStatucChecked) {
                        appliedJobsForApplicationHistoryGoesHere();
                    }

                }
            }
        });


    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.filterLinear:
                statusArray.clear();
                filterDialog();
                break;


        }


    }

    private void filterDialog() {


        LayoutInflater li = LayoutInflater.from(getActivity());
        View dialogView = li.inflate(R.layout.dialog_filter, null);

        findIdsFroFilterDialog(dialogView);

        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);

        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();


    }

    private void findIdsFroFilterDialog(View dialogView) {

        imageCancel = dialogView.findViewById(R.id.imageCancel);

        checkBoxApplied = dialogView.findViewById(R.id.checkBoxApplied);
        checkBoxOnHold = dialogView.findViewById(R.id.checkBoxOnHold);
        checkBoxSelected = dialogView.findViewById(R.id.checkBoxSelected);
        checkBoxRejected = dialogView.findViewById(R.id.checkBoxRejected);

        btnApply = dialogView.findViewById(R.id.btnApply);
        btnCancel = dialogView.findViewById(R.id.btnCancel);


        checkBoxApplied.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    statusCheckApplied = "0";
                    statusArray.add(statusCheckApplied);
                } else {
                    statusArray.remove(statusCheckApplied);

                }


            }
        });

        checkBoxOnHold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    statusCheckOnHold = "1";

                    statusArray.add(statusCheckOnHold);
                } else {
                    statusArray.remove(statusCheckOnHold);
                }


            }
        });

        checkBoxSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    statusCheckSelected = "2";
                    statusArray.add(statusCheckSelected);
                } else {
                    statusArray.remove(statusCheckSelected);
                }


            }
        });

        checkBoxRejected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    statusCheckRejected = "3";
                    statusArray.add(statusCheckRejected);
                } else {
                    statusArray.remove(statusCheckRejected);
                }


            }
        });


        imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });


        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!checkBoxApplied.isChecked() && !checkBoxOnHold.isChecked() && !checkBoxSelected.isChecked() && !checkBoxRejected.isChecked()) {

                    new AwesomeErrorDialog(getActivity())
                            .setTitle("Oops")
                            .setMessage("Please select options")
                            .setColoredCircle(R.color.dialogErrorBackgroundColor)
                            .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                            .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
                            .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                            .setButtonText(getString(R.string.dialog_ok_button))
                            .setErrorButtonClick(new Closure() {
                                @Override
                                public void exec() {


                                }
                            })
                            .show();


                } else {


                    STATUS = statusArray.toString().replace("[", "").replace("]", "");


                    new AwesomeSuccessDialog(getActivity())
                            .setTitle("Success")
                            .setMessage("")
                            .setColoredCircle(R.color.colorlogin)
                            .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
                            .setCancelable(false)
                            .setPositiveButtonText("Ok")
                            .setPositiveButtonbackgroundColor(R.color.colorlogin)
                            .setPositiveButtonTextColor(R.color.white)
                            .setPositiveButtonClick(new Closure() {
                                @Override
                                public void exec() {

                                    if (STATUS != null) {
                                        offsetItesm = 1;
                                        loadingProgress();
//                                        callTheFilterApi(STATUS);
                                        appliedJobsApplicationHistory.clear();
                                        appliedJobsForApplicationHistoryGoesHere();

                                        alertDialog.dismiss();

                                    }

                                }
                            })
                            .show();

                }


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });


    }

    private void callTheFilterApi(final String STATUSNEW) {


        HttpModule.provideRepositoryService().applyFilterToRefreshDataForJobSeeker((String) Hawk.get("GET_TOKEN"), offsetItesm, 10, STATUSNEW).enqueue(new Callback<AppliedJobsForAppHistory>() {
            @Override
            public void onResponse(Call<AppliedJobsForAppHistory> call, Response<AppliedJobsForAppHistory> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();
                    String temp = null;

                    appliedJobsApplicationHistory.clear();
                    appliedJobsApplicationHistory.addAll(response.body().getData().getAppliedList());

                    if (appliedJobsApplicationHistory.size() == 0) {

                        tvNoDataAvailable.setVisibility(View.VISIBLE);
                    } else {
                        tvNoDataAvailable.setVisibility(View.GONE);
                    }

                    applicationHistoryAdapter.notifyDataSetChanged();
                    STATUS = temp;
                    statusArray.clear();
                    isStatucChecked = true;


                } else {

                    mProgressDialog.dismiss();
                    new AwesomeErrorDialog(getActivity())
                            .setTitle("Oops")
                            .setMessage(Objects.requireNonNull(response.body()).getMessage())
                            .setColoredCircle(R.color.dialogErrorBackgroundColor)
                            .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                            .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
                            .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                            .setButtonText(getString(R.string.dialog_ok_button))
                            .setErrorButtonClick(new Closure() {
                                @Override
                                public void exec() {


                                }
                            })
                            .show();


                }


            }

            @Override
            public void onFailure(Call<AppliedJobsForAppHistory> call, Throwable t) {

                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }


}
