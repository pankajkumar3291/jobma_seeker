package com.smartit.jobSeeker.fragments;

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
import com.smartit.jobSeeker.activities.ActivityAddEmploymentDetails;
import com.smartit.jobSeeker.adapters.EmploymentAdapter;
import com.smartit.jobSeeker.apiResponse.getEmploymentDetails.Datum;
import com.smartit.jobSeeker.apiResponse.getEmploymentDetails.GetEmploymentDetails;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EmploymentFragment extends Fragment {


    private View mView;
    private LinearLayout linAdd;
    private RecyclerView mEmpRecyclerView;
    private EmploymentAdapter employmentAdapter;

    private List<Datum> datumList = new ArrayList<>();
    private TextView tvNoDataFound;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.employment_fragment, container, false);
        Hawk.init(Objects.requireNonNull(getActivity())).build();
        findIdsHere(mView);

        return mView;


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(CalligraphyContextWrapper.wrap(context));

    }


    @Override
    public void onResume() {
        super.onResume();
        datumList.clear();
        getEmploymentDetails();

    }

    private void getEmploymentDetails() {

        HttpModule.provideRepositoryService().getEmploymentDetailsApi(Hawk.get("GET_TOKEN")).enqueue(new Callback<GetEmploymentDetails>() {
            @Override
            public void onResponse(Call<GetEmploymentDetails> call, Response<GetEmploymentDetails> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    datumList.addAll(response.body().getData());


                    if (datumList.size() > 0) {

                        tvNoDataFound.setVisibility(View.GONE);
                        employmentAdapter = new EmploymentAdapter(getActivity(), datumList); //response.body().getData()
                        mEmpRecyclerView.setHasFixedSize(true);
                        mEmpRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mEmpRecyclerView.setAdapter(employmentAdapter);
                    } else {

                        tvNoDataFound.setVisibility(View.VISIBLE);
                    }


                } else {

                    System.out.println("EducationFragment.onResponse" + Objects.requireNonNull(response.body()).getMessage());
                }


            }

            @Override
            public void onFailure(Call<GetEmploymentDetails> call, Throwable t) {


            }
        });

    }


    private void findIdsHere(View mView) {

        linAdd = mView.findViewById(R.id.linAdd);
        mEmpRecyclerView = mView.findViewById(R.id.mEmpRecyclerView);
        tvNoDataFound = mView.findViewById(R.id.tvNoDataFound);


        linAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ActivityAddEmploymentDetails.class);
                Objects.requireNonNull(getActivity()).startActivity(intent);


            }
        });
    }
}
