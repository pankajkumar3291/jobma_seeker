package com.smartit.jobSeeker.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.activities.ActivityAddEducation;
import com.smartit.jobSeeker.adapters.EducationAdapter;
import com.smartit.jobSeeker.apiResponse.getEducationDetails.Datum;
import com.smartit.jobSeeker.apiResponse.getEducationDetails.GetEducationDetails;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EducationFragment extends Fragment {


    private View mView;
    private LinearLayout linAdd;
    private RecyclerView mEduRecyclerView;
    private EducationAdapter educationAdapter;


    private ArrayList<Datum> datumArrayList = new ArrayList<>();
    private ProgressDialog mProgressDialog;


    private TextView tvNoDataFound;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.education_fragment, container, false);
        Hawk.init(Objects.requireNonNull(getActivity())).build();
        mProgressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
        findIdsHere(mView);

//        getEducationApi();

        return mView;


    }


    @Override
    public void onResume() {
        super.onResume();
        loadingProgress();
        datumArrayList.clear();
        getEducationApi();


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

    private void getEducationApi() {


        HttpModule.provideRepositoryService().getEducationDetailsApi(Hawk.get("GET_TOKEN")).enqueue(new Callback<GetEducationDetails>() {
            @Override
            public void onResponse(Call<GetEducationDetails> call, Response<GetEducationDetails> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();
                    datumArrayList.addAll(response.body().getData());

                    if (datumArrayList.size() > 0) {

                        tvNoDataFound.setVisibility(View.GONE);
                        educationAdapter = new EducationAdapter(getActivity(), datumArrayList);
                        mEduRecyclerView.setHasFixedSize(true);
                        mEduRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mEduRecyclerView.setAdapter(educationAdapter);
                    } else {

                        tvNoDataFound.setVisibility(View.VISIBLE);
                    }


                } else {

                    mProgressDialog.dismiss();
                    System.out.println("EducationFragment.onResponse" + Objects.requireNonNull(response.body()).getMessage());
                }


            }

            @Override
            public void onFailure(Call<GetEducationDetails> call, Throwable t) {
                mProgressDialog.dismiss();

                System.out.println("EducationFragment.onFailure " + t.getMessage());

            }
        });


    }


    private void findIdsHere(View mView) {

        mEduRecyclerView = mView.findViewById(R.id.mEduRecyclerView);
        linAdd = mView.findViewById(R.id.linAdd);

        tvNoDataFound = mView.findViewById(R.id.tvNoDataFound);

        linAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ActivityAddEducation.class);
                Objects.requireNonNull(getActivity()).startActivity(intent);


            }
        });
    }
}
