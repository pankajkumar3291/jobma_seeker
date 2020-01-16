package com.smartit.jobSeeker.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.android.material.tabs.TabLayout;
import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.activities.ActivityDashboard;
import com.smartit.jobSeeker.activities.ActivityUpdateBasicInformation;
import com.smartit.jobSeeker.adapters.ViewPagerAdapter;
import com.smartit.jobSeeker.apiResponse.getProfileDetail.GetProfileDetails;
import com.smartit.jobSeeker.apiResponse.handShakeForDashboard.HandshakeForDashboard;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyProfileFragment extends Fragment {


    private View mView;
    private CircleImageView productImage;
    private ArcProgress arc_progress;
    private TextView tvUserName, tvUserExp, tvCompanyWorkingWith,
            tvProfilePercentage, tvUserEmail, tvUserPhoneNumber, tvUserAddress;

    private ProgressDialog mProgressDialog;

    private ImageView editMyProfileImage;

    private String mCountry, mState, mCity, mZipcode;

    private String experienceInYears, experienceInMonths;
    private String mJobTitle, mCompanyName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.my_profile_fragment, container, false);

        Hawk.init(Objects.requireNonNull(getActivity())).build();
        mProgressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
        initViewsHere(mView);

        loadingProgress();
//        getProfileApi();
//        handshakeApi();

        return mView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(CalligraphyContextWrapper.wrap(context));

    }


    @Override
    public void onResume() {
        super.onResume();
        getProfileApi();
        handshakeApi();

    }

    private void loadingProgress() {


        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();


    }

    private void handshakeApi() {


        HttpModule.provideRepositoryService().handShakeForJobSeeker(Hawk.get("GET_TOKEN")).enqueue(new Callback<HandshakeForDashboard>() {
            @Override
            public void onResponse(Call<HandshakeForDashboard> call, Response<HandshakeForDashboard> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();
                    tvUserName.setText(response.body().getData().getName() + " " + response.body().getData().getLastname());
                    tvUserName.setTypeface(tvUserName.getTypeface(), Typeface.BOLD);

                    tvUserExp.setText(response.body().getData().getExp().toString() + "+ years of experience");
                    experienceInYears = response.body().getData().getExp().toString();
                    experienceInMonths = response.body().getData().getExpmm().toString();

                    mJobTitle = response.body().getData().getJobtitle();
                    mCompanyName = response.body().getData().getComp();


                    tvCompanyWorkingWith.setText(response.body().getData().getComp());

                    tvProfilePercentage.setText(response.body().getData().getPmeter().toString() + "%");
                    arc_progress.setProgress(response.body().getData().getPmeter());


                    if (response.body().getData().getImageurl().isEmpty()) {

                        productImage.setImageResource(R.drawable.no_image_available);

                    } else {

                        Picasso.get()
                                .load(response.body().getData().getImageurl())
                                .placeholder(R.drawable.no_image_available)
                                .error(R.drawable.no_image_available)
                                .into(productImage);
                    }
                }
            }

            @Override
            public void onFailure(Call<HandshakeForDashboard> call, Throwable t) {
                mProgressDialog.dismiss();
                System.out.println("MyProfileFragment.onFailure " + t.getMessage());

            }
        });

    }

    private void getProfileApi() {


        HttpModule.provideRepositoryService().getProfileDetailsApi(Hawk.get("GET_TOKEN")).enqueue(new Callback<GetProfileDetails>() {
            @Override
            public void onResponse(Call<GetProfileDetails> call, Response<GetProfileDetails> response) {


                if (response.body() != null && response.body().getError() == 0) {
                    mProgressDialog.dismiss();

                    if (TextUtils.isEmpty(response.body().getData().getEmail())) {
                        tvUserEmail.setText("N/A");
                    } else {
                        tvUserEmail.setText(response.body().getData().getEmail());
                    }

                    if (TextUtils.isEmpty(response.body().getData().getCity() + response.body().getData().getState() + response.body().getData().getCountry() + response.body().getData().getZipcode().toString())) {
                        tvUserAddress.setText("N/A");
                    } else {
                        tvUserAddress.setText(response.body().getData().getCity() + "," + response.body().getData().getState() + "," + response.body().getData().getCountry() + "," + response.body().getData().getZipcode().toString());
                    }

                    if (TextUtils.isEmpty(response.body().getData().getPhone())) {
                        tvUserPhoneNumber.setText("N/A");
                    } else {
                        tvUserPhoneNumber.setText(response.body().getData().getPhone());
                    }

                    mCountry = response.body().getData().getCountry();
                    mState = response.body().getData().getState();
                    mCity = response.body().getData().getCity();
                    mZipcode = response.body().getData().getZipcode();


                } else {
                    mProgressDialog.dismiss();
                    System.out.println("MyProfileFragment.onResponse" + Objects.requireNonNull(response.body()).getMessage());
                }


            }

            @Override
            public void onFailure(Call<GetProfileDetails> call, Throwable t) {
                mProgressDialog.dismiss();
                System.out.println("MyProfileFragment.onFailure " + t.getMessage());

            }
        });


    }

    private void initViewsHere(View mView) {

        tvUserName = mView.findViewById(R.id.tvUserName);
        tvUserExp = mView.findViewById(R.id.tvUserExp);
        tvCompanyWorkingWith = mView.findViewById(R.id.tvCompanyWorkingWith);
        tvProfilePercentage = mView.findViewById(R.id.tvProfilePercentage);
        productImage = mView.findViewById(R.id.productImage);
        arc_progress = mView.findViewById(R.id.arc_progress);

        tvUserEmail = mView.findViewById(R.id.tvUserEmail);
        tvUserPhoneNumber = mView.findViewById(R.id.tvUserPhoneNumber);
        tvUserAddress = mView.findViewById(R.id.tvUserAddress);

        editMyProfileImage = mView.findViewById(R.id.editMyProfileImage);


        editMyProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ActivityUpdateBasicInformation.class);
                intent.putExtra("mCountry", mCountry);
                intent.putExtra("mState", mState);
                intent.putExtra("mCity", mCity);
                intent.putExtra("mZipcode", mZipcode);
                intent.putExtra("mExperience", experienceInYears);
                intent.putExtra("mExperienceInMonths", experienceInMonths);
                intent.putExtra("mJobTitle", mJobTitle);
                intent.putExtra("mCompanyName", mCompanyName);

                Objects.requireNonNull(getActivity()).startActivity(intent);


            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // find views by id
        ViewPager viewPager = view.findViewById(R.id.viewpager);
        TabLayout tabLayout = view.findViewById(R.id.tablayout);

        // attach tablayout with viewpager
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        // add your fragments
        adapter.addFrag(new ProfileInnerFragment(), "PROFILE");
        adapter.addFrag(new EducationFragment(), "EDUCATION");
        adapter.addFrag(new SkillsFragments(), "SKILLS");
        adapter.addFrag(new EmploymentFragment(), "EMPLOYMENT");
        adapter.addFrag(new ResumeFragment(), "RESUME");

        // set adapter on viewpager
        viewPager.setAdapter(adapter);
    }
}
