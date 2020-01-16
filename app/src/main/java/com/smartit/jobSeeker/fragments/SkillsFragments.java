package com.smartit.jobSeeker.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
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
import com.smartit.jobSeeker.activities.ActivityAddSkills;
import com.smartit.jobSeeker.addUpdateSkills.AddSkill;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.adapters.SkillFragmentAdapter;
import com.smartit.jobSeeker.apiResponse.gerProfileSkills.GetProfileSkills;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SkillsFragments extends Fragment {


    private View mView;
    private RecyclerView mSkillsRecyclerView;
    private SkillFragmentAdapter skillFragmentAdapter;
    private LinearLayout linAdd;
    List<AddSkill> skillList = new ArrayList<>();

    private ProgressDialog mProgressDialog;
    private TextView tvNoDataFound;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        mView = inflater.inflate(R.layout.skill_fragment, container, false);
        Hawk.init(Objects.requireNonNull(getActivity())).build();
        mProgressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
        initViewsHere(mView);

        return mView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(CalligraphyContextWrapper.wrap(context));

    }

    @Override
    public void onResume() {
        super.onResume();

        loadingProgress();
        skillList.clear();
        getProfileSkills();

    }

    private void loadingProgress() {

        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

    }

    private void getProfileSkills() {


        HttpModule.provideRepositoryService().getProfileSkillsApi(Hawk.get("GET_TOKEN")).enqueue(new Callback<GetProfileSkills>() {
            @Override
            public void onResponse(Call<GetProfileSkills> call, Response<GetProfileSkills> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();


                    if (!TextUtils.isEmpty(response.body().getData().getValue())) {

                        String value_split = response.body().getData().getValue().replace("|", ",");
                        List<String> myList = new ArrayList<String>(Arrays.asList(value_split.split(",")));
                        System.out.println("MyList  " + myList.size());

                        for (String s : myList) {

                            skillList.add(new AddSkill(s));
                        }

                        tvNoDataFound.setVisibility(View.GONE);
                        skillFragmentAdapter = new SkillFragmentAdapter(getActivity(), skillList);
                        mSkillsRecyclerView.setHasFixedSize(true);
                        mSkillsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mSkillsRecyclerView.setAdapter(skillFragmentAdapter);

                    } else {

                        tvNoDataFound.setVisibility(View.VISIBLE);
                    }


                } else {
                    tvNoDataFound.setVisibility(View.VISIBLE);
                    System.out.println("SkillsFragments.onResponse " + Objects.requireNonNull(response.body()).getMessage());
                    mProgressDialog.dismiss();
                }


            }

            @Override
            public void onFailure(Call<GetProfileSkills> call, Throwable t) {

                mProgressDialog.dismiss();

            }
        });


    }

    private void initViewsHere(View mView) {

        mSkillsRecyclerView = mView.findViewById(R.id.mSkillsRecyclerView);
        linAdd = mView.findViewById(R.id.linAdd);
        tvNoDataFound = mView.findViewById(R.id.tvNoDataFound);


        linAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ActivityAddSkills.class);
                intent.putExtra("ADD_SKIlL_LIST", (Serializable) skillList);
                Objects.requireNonNull(getActivity()).startActivity(intent);


            }
        });

    }
}
