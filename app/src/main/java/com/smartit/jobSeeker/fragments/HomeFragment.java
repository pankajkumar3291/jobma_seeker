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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.sdsmdg.tastytoast.TastyToast;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.activities.ActivityAddSkills;
import com.smartit.jobSeeker.activities.ActivitySkill;
import com.smartit.jobSeeker.adapters.InterviewModeAdapter;
import com.smartit.jobSeeker.adapters.KeySkillAdapeter;
import com.smartit.jobSeeker.apiResponse.handShakeForDashboard.HandshakeForDashboard;
import com.smartit.jobSeeker.apiResponse.interview_list_mode.InterviewList;
import com.smartit.jobSeeker.apiResponse.interview_list_mode.InterviewListMode;
import com.smartit.jobSeeker.eventbus.MessageEventProfile;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.smartit.jobSeeker.skills.Skills;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeFragment extends Fragment {

    private View view;
    private Context context;
    private TextView tvUserName, tvUserExp, tvCompanyWorkingWith, tvAppliedJobs, tvViwed, tvDownloaded, tvJobsNearMe, tvProfilePercentage,
            tvInvitations, tvSetting, tvDataNotFound, tvNoSkill, tvSInter;

    private CircleImageView productImage;
    private ArcProgress arc_progress;
    private ImageView editImage;


    private RecyclerView recylerForLanguagesTags, recylerForInterviewModes;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Integer offsetItesm = 1;

    private List<InterviewList> interviewListsForpagination = new ArrayList<>();
    private InterviewModeAdapter interviewModeAdapter;
    private ProgressBar main_progress;
    private Integer totalCount = 0;
    private NoInternetDialog noInternetDialog;
    private ProgressDialog mProgressDialog;

    private List<Skills> myList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_fragment, container, false);
        EventBus.getDefault().register(this);

        findingTheIdsFromHere(view);
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        mProgressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);


//        loadingProgress();
//        handshakeApiGoeshere();
        interviewListModeGoesHere();

        return view;

    }

    private void loadingProgress() {

        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();


    }


    @Override
    public void onResume() {
        super.onResume();

        loadingProgress();
        myList.clear();
        handshakeApiGoeshere();
//        interviewListModeGoesHere();


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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEventProfile event) {
        /* Do something */

        System.out.println("HomeFragment.onMessageEvent " + event.getPath());

        Picasso.get()
                .load(event.getPath())
                .placeholder(R.drawable.no_image_available)
                .error(R.drawable.no_image_available)
                .into(productImage);


    }

    public void addHomeFrgamentAgain() {
        Toast.makeText(getActivity(), "Adding", Toast.LENGTH_SHORT).show();

    }


    private void interviewListModeGoesHere() {

        if (interviewListsForpagination.size() < totalCount || totalCount == 0) {

            main_progress.setVisibility(View.VISIBLE);


            HttpModule.provideRepositoryService().interviewListModeForJobSeeker(getArguments().getString("token"), String.valueOf(offsetItesm), 10).enqueue(new Callback<InterviewListMode>() {
                @Override
                public void onResponse(Call<InterviewListMode> call, final Response<InterviewListMode> response) {

                    if (response.body() != null && response.body().getError() == 0) {

                        main_progress.setVisibility(View.GONE);

                        totalCount = response.body().getData().getTotalCount();

                        if (response.body().getData().getInterviewList().size() > 0) {

                            interviewListsForpagination.addAll(response.body().getData().getInterviewList());
                            interviewModeAdapter.notifyDataSetChanged();
                        } else {

                            tvDataNotFound.setVisibility(View.VISIBLE);
                        }


                    }


                }

                @Override
                public void onFailure(Call<InterviewListMode> call, Throwable t) {


                    TastyToast.makeText(getActivity(), t.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                }
            });
        }


    }


    private void handshakeApiGoeshere() {

        HttpModule.provideRepositoryService().handShakeForJobSeeker(getArguments().getString("token")).enqueue(new Callback<HandshakeForDashboard>() {
            @Override
            public void onResponse(Call<HandshakeForDashboard> call, Response<HandshakeForDashboard> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();


                    tvUserName.setText(response.body().getData().getName() + " " + response.body().getData().getLastname());
                    tvUserName.setTypeface(tvUserName.getTypeface(), Typeface.BOLD);

                    tvUserExp.setText(response.body().getData().getExp().toString() + "+ years of experience");
                    tvCompanyWorkingWith.setText(response.body().getData().getComp());
                    tvAppliedJobs.setText(response.body().getData().getApplied().toString());
                    tvAppliedJobs.setTypeface(tvAppliedJobs.getTypeface(), Typeface.BOLD);
                    tvViwed.setText(response.body().getData().getViews().toString());
                    tvViwed.setTypeface(tvViwed.getTypeface(), Typeface.BOLD);
                    tvDownloaded.setText(response.body().getData().getDownloads().toString());
                    tvDownloaded.setTypeface(tvDownloaded.getTypeface(), Typeface.BOLD);


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


                    // HERE, WE ARE REMOVING THE PIPE OPERATOR FROM THE STRING AND PUT THEM ALL INTO LIST


                    System.out.println("MyList  " + myList.size());


                    if (!TextUtils.isEmpty(response.body().getData().getKeyskills())) {

                        tvNoSkill.setVisibility(View.GONE);

                        String value_split = response.body().getData().getKeyskills().replace("|", ",");
                        //myList = new ArrayList<String>(Arrays.asList(value_split.split(",")));
                        String[] newArray = value_split.split(",");
                        for (String str : newArray) {
                            myList.add(new Skills(str));
                        }

                        KeySkillAdapeter keySkillAdapeter = new KeySkillAdapeter(getActivity(), myList);
                        recylerForLanguagesTags.setHasFixedSize(true);
                        recylerForLanguagesTags.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        recylerForLanguagesTags.setAdapter(keySkillAdapeter);


                    } else {
                        tvNoSkill.setVisibility(View.VISIBLE);

                    }


                } else {

                    mProgressDialog.dismiss();
                    TastyToast.makeText(getActivity(), Objects.requireNonNull(response.body()).getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                }

            }

            @Override
            public void onFailure(Call<HandshakeForDashboard> call, Throwable t) {

                mProgressDialog.dismiss();
                TastyToast.makeText(getActivity(), t.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                System.out.println("HomeFragment.onFailure " + t.getMessage());

            }
        });


        // ITS WORKING TOO
//        compositeDisposable.add(HttpModule.provideRepositoryService().handShakeForJobSeeker("TSP9RANfZdO0uVqqGyNBEeFLesMAVY1530530467").
//                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
//                observeOn(AndroidSchedulers.mainThread()).
//                subscribe(new Consumer<HandshakeForDashboard>() {
//
//                    @Override
//                    public void accept(HandshakeForDashboard handshakeForDashboard) throws Exception {
//
//
//                        if (handshakeForDashboard != null && handshakeForDashboard.getError() == 0) {
//                            tvUserName.setText(handshakeForDashboard.getData().getName().toString());
//                            tvUserExp.setText(handshakeForDashboard.getData().getExp().toString());
//                            tvCompanyWorkingWith.setText(handshakeForDashboard.getData().getComp().toString());
//                            tvAppliedJobs.setText(handshakeForDashboard.getData().getApplied().toString());
//                            tvViwed.setText(handshakeForDashboard.getData().getViews().toString());
//                            tvDownloaded.setText(handshakeForDashboard.getData().getDownloads().toString());
//                        } else {
//
//                            Toast.makeText(getActivity(), "Error 1", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//
//                }, new Consumer<Throwable>() {
//
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        Toast.makeText(getActivity(), throwable.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }));

    }

    private void findingTheIdsFromHere(View view) {
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserExp = view.findViewById(R.id.tvUserExp);
        tvCompanyWorkingWith = view.findViewById(R.id.tvCompanyWorkingWith);
        tvAppliedJobs = view.findViewById(R.id.tvAppliedJobs);
        tvViwed = view.findViewById(R.id.tvViwed);
        tvDownloaded = view.findViewById(R.id.tvDownloaded);
        tvJobsNearMe = view.findViewById(R.id.tvJobsNearMe);
        tvInvitations = view.findViewById(R.id.tvInvitations);
        tvSetting = view.findViewById(R.id.tvSetting);

        tvSInter = view.findViewById(R.id.tvSInter);
        tvSInter.setTypeface(tvSInter.getTypeface(), Typeface.BOLD);

        tvProfilePercentage = view.findViewById(R.id.tvProfilePercentage);
        productImage = view.findViewById(R.id.productImage);
        arc_progress = view.findViewById(R.id.arc_progress);


        tvDataNotFound = view.findViewById(R.id.tvDataNotFound);
        tvNoSkill = view.findViewById(R.id.tvNoSkill);


        recylerForLanguagesTags = view.findViewById(R.id.recylerForLanguagesTags);
        recylerForInterviewModes = view.findViewById(R.id.recylerForInterviewModes);

        main_progress = view.findViewById(R.id.main_progress);
        editImage = view.findViewById(R.id.editImage);


        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityAddSkills.class);
                intent.putExtra("ADD_SKIlL_LIST", (Serializable) myList);
                intent.putExtra("FROM_HOME_FRAGMENT", true);
                getActivity().startActivity(intent);
            }
        });

        interviewModeAdapter = new InterviewModeAdapter(getActivity(), interviewListsForpagination);
        recylerForInterviewModes.setHasFixedSize(true);
        recylerForInterviewModes.setLayoutManager(new LinearLayoutManager(getActivity()));
        recylerForInterviewModes.setAdapter(interviewModeAdapter);

        recylerForInterviewModes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == interviewListsForpagination.size() - 1) {
                    offsetItesm += 10;
                    interviewListModeGoesHere();
                }
            }
        });
    }
}
