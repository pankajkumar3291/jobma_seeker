package com.smartit.jobSeeker.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.adapters.InvitationAdapter;
import com.smartit.jobSeeker.apiResponse.inviationModule.InvitationList;
import com.smartit.jobSeeker.apiResponse.inviationModule.InvitationModule;
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

public class InvitationFragment extends Fragment  {


    private Context context;
    private View view;

    private InvitationAdapter invitationAdapter;

    private RecyclerView recyclerInvitation;
    private ProgressBar progressInvitation;

    private Integer offsetItesm = 1;
    private Integer totalCount = 0;

    private List<InvitationList> invitaionList = new ArrayList<>();

    private TextView tvNoDataAvailable;
    private NoInternetDialog noInternetDialog;
    private ProgressDialog mProgressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.invitation_fragment, container, false);


        findTheIdsFromHere(view);
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        mProgressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);

        loadingProgress();
        inviationApiGoesHere();


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

    private void inviationApiGoesHere() {

        if (invitaionList.size() < totalCount || totalCount == 0) {

            progressInvitation.setVisibility(View.VISIBLE);
            if (invitaionList.size() == 0) {

//                tvNoDataAvailable.setVisibility(View.VISIBLE);
            }


            HttpModule.provideRepositoryService().inviationForJobSeeker(getArguments().getString("token"), String.valueOf(offsetItesm), 10).enqueue(new Callback<InvitationModule>() {
                @Override
                public void onResponse(Call<InvitationModule> call, Response<InvitationModule> response) {


                    if (response.body() != null && response.body().getError() == 0) {

                        mProgressDialog.dismiss();
                        progressInvitation.setVisibility(View.GONE);

                        if (response.body().getData().getInvitationList().size() == 0) {

                            tvNoDataAvailable.setVisibility(View.VISIBLE);

                        } else {
                            mProgressDialog.dismiss();
                            totalCount = response.body().getData().getTotalCount();
                            invitaionList.addAll(response.body().getData().getInvitationList());

                            if (invitaionList.size() == 0) {
                                tvNoDataAvailable.setVisibility(View.VISIBLE);
                            }

                            invitationAdapter.notifyDataSetChanged();
                        }


                    } else {

                        mProgressDialog.dismiss();
                        Toast.makeText(getActivity(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();

                    }


                }

                @Override
                public void onFailure(Call<InvitationModule> call, Throwable t) {

                    mProgressDialog.dismiss();
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

                }
            });


        }
    }

    private void inviationAdapterInitialization() {

        invitationAdapter = new InvitationAdapter(getActivity(), invitaionList, getArguments().getString("token"));
        recyclerInvitation.setHasFixedSize(true);
        recyclerInvitation.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerInvitation.setAdapter(invitationAdapter);


        recyclerInvitation.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == invitaionList.size() - 1) {

                    offsetItesm += 10;

                    inviationApiGoesHere();
                }

            }
        });


    }


    private void findTheIdsFromHere(View view) {

        recyclerInvitation = view.findViewById(R.id.recyclerInvitation);
        progressInvitation = view.findViewById(R.id.progressInvitation);
        tvNoDataAvailable = view.findViewById(R.id.tvNoDataAvailable);

        inviationAdapterInitialization();


    }



}
