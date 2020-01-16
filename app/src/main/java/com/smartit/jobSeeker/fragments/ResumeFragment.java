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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.activities.ActivityResumeDisplay;
import com.smartit.jobSeeker.apiResponse.getResumeInfo.GetResumeInfo;
import com.smartit.jobSeeker.apiResponse.uploadResume.UploadResume;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.filter.entity.NormalFile;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.app.Activity.RESULT_OK;

public class ResumeFragment extends Fragment {


    private View mView;
    private TextView tvResume, tvLastModifiedDate, tvDefaultStatus, tvNoResumeAvail, tvLast, tvDefau;
    private ImageView imageEdit;
    private LinearLayout linAdd;
    private ProgressDialog mProgressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        mView = inflater.inflate(R.layout.resume_fragment, container, false);
        Hawk.init(Objects.requireNonNull(getActivity())).build();
        mProgressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);

        initViewsHere(mView);

        return mView;
    }


    @Override
    public void onResume() {
        super.onResume();
        resumeInfoApi();


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(CalligraphyContextWrapper.wrap(context));

    }

    private void resumeInfoApi() {

        HttpModule.provideRepositoryService().getResumeInfoApi(Hawk.get("GET_TOKEN")).enqueue(new Callback<GetResumeInfo>() {
            @Override
            public void onResponse(Call<GetResumeInfo> call, Response<GetResumeInfo> response) {


                if (response.body() != null && response.body().getError() == 0) {


                    if (response.body().getData().getResumeName().equalsIgnoreCase("")) {
                        tvResume.setText("N/A");
                    } else {
                        tvResume.setText(response.body().getData().getResumeName());
                        tvResume.setTypeface(tvResume.getTypeface(), Typeface.NORMAL);
                    }


                    tvLastModifiedDate.setTypeface(tvLastModifiedDate.getTypeface(), Typeface.ITALIC);
                    tvLast.setTypeface(tvLast.getTypeface(), Typeface.BOLD_ITALIC);

                    tvDefaultStatus.setTypeface(tvDefaultStatus.getTypeface(), Typeface.ITALIC);
                    tvDefau.setTypeface(tvDefau.getTypeface(), Typeface.BOLD_ITALIC);

                    if (TextUtils.isEmpty(response.body().getData().getUpdatedAt())) {
                        tvLastModifiedDate.setText("N/A");
                    } else {
                        tvLastModifiedDate.setText(response.body().getData().getUpdatedAt());
                    }


                    tvDefaultStatus.setText(response.body().getData().getResumeShow());
                    linAdd.setVisibility(View.GONE);


                    if (tvResume.getText().toString().equalsIgnoreCase("N/A")) {
//                        Toast.makeText(getActivity(), "No resume available", Toast.LENGTH_SHORT).show();

                    } else {

                        tvResume.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(getActivity(), ActivityResumeDisplay.class);
                                startActivity(intent);

                            }
                        });

                    }


                } else {

                    tvNoResumeAvail.setVisibility(View.VISIBLE);
                    linAdd.setVisibility(View.VISIBLE);
                    System.out.println("ResumeFragment.onResponse " + Objects.requireNonNull(response.body()).getMessage());
                }


            }

            @Override
            public void onFailure(Call<GetResumeInfo> call, Throwable t) {


                System.out.println("ResumeFragment.onFailure " + t.toString());

            }
        });
    }

    private void initViewsHere(View mView) {

        tvResume = mView.findViewById(R.id.tvResume);
        tvLastModifiedDate = mView.findViewById(R.id.tvLastModifiedDate);

        tvLast = mView.findViewById(R.id.tvLast);
        tvLast.setTypeface(tvLast.getTypeface(), Typeface.BOLD);
        tvDefau = mView.findViewById(R.id.tvDefau);
        tvDefau.setTypeface(tvDefau.getTypeface(), Typeface.BOLD);

        tvDefaultStatus = mView.findViewById(R.id.tvDefaultStatus);

        tvNoResumeAvail = mView.findViewById(R.id.tvNoResumeAvail);

        imageEdit = mView.findViewById(R.id.imageEdit);
        linAdd = mView.findViewById(R.id.linAdd);


        imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent4 = new Intent(getActivity(), NormalFilePickActivity.class);
                intent4.putExtra(Constant.MAX_NUMBER, 9);
                intent4.putExtra(NormalFilePickActivity.SUFFIX, new String[]{"pdf"});
                startActivityForResult(intent4, Constant.REQUEST_CODE_PICK_FILE);


            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == Constant.REQUEST_CODE_PICK_FILE) {
                ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);

                if (list.size() > 0) {
                    loadingProgress();
                    uploadResumeApi(list.get(0).getPath());
                } else {
                    Toast.makeText(getContext(), "No resume selected", Toast.LENGTH_SHORT).show();
                }

            }

        }

    }

    private void loadingProgress() {

        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }

    private void uploadResumeApi(String path) {


        File file = new File(path);
        final RequestBody reqFile = RequestBody.create(MediaType.parse("pdf/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload_resume", file.getName(), reqFile); // NOTE: upload here is the name which is the file name


        HttpModule.provideRepositoryService().uploadResumeApi(Hawk.get("GET_TOKEN"), body).enqueue(new Callback<UploadResume>() {
            @Override
            public void onResponse(Call<UploadResume> call, Response<UploadResume> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();


                } else {

                    mProgressDialog.dismiss();
                    Toast.makeText(getActivity(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<UploadResume> call, Throwable t) {

                mProgressDialog.dismiss();
                System.out.println("ResumeFragment.onFailure " + t.toString());

            }
        });


    }
}
