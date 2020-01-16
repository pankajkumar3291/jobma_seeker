package com.smartit.jobSeeker.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.common.StringUtils;
import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.adapters.AddSkillAdapter;
import com.smartit.jobSeeker.addUpdateSkills.AddSkill;
import com.smartit.jobSeeker.apiResponse.EditKeySkillsAndUpdate.EditKeySkillAndUpdate;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.smartit.jobSeeker.skills.Skills;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityAddSkills extends AppCompatActivity {

    private ImageView backArrowImage, addSkillImage;
    private EditText edAddSkills;
    private RecyclerView mRecyclerview;
    private Button btnSave;
    private TextView tvSkills;
    private AddSkillAdapter addSkillAdapter;
    private ArrayList<AddSkill> addSkillArrayList = new ArrayList<>();
    private String Skills;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private ProgressDialog mProgressDialog;
    boolean fromHomeFragment;
    private ArrayList<com.smartit.jobSeeker.skills.Skills> arrayList = new ArrayList<>();
    private List<String> skillTemp;
    private List<String> templist;
    private NoInternetDialog noInternetDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_skills);
        Hawk.init(ActivityAddSkills.this).build();

        noInternetDialog = new NoInternetDialog.Builder(this).build();
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        if (getIntent() != null && getIntent().hasExtra("FROM_HOME_FRAGMENT")) {
            fromHomeFragment = getIntent().getBooleanExtra("FROM_HOME_FRAGMENT", true);
        }

        if (Objects.requireNonNull(getIntent()).hasExtra("ADD_SKIlL_LIST")) {
            arrayList = (ArrayList<com.smartit.jobSeeker.skills.Skills>) getIntent().getSerializableExtra("ADD_SKIlL_LIST");
        }


        if (getIntent() != null && getIntent() != null && getIntent().hasExtra("ADD_SKIlL_LIST")) {
            addSkillArrayList = (ArrayList<AddSkill>) getIntent().getSerializableExtra("ADD_SKIlL_LIST");
        }


        initViews();
        initAdapters();

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    private void initAdapters() {


        if (fromHomeFragment) {

            addSkillAdapter = new AddSkillAdapter(ActivityAddSkills.this, arrayList, fromHomeFragment);
            mRecyclerview.setHasFixedSize(true);
            mRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
            mRecyclerview.setAdapter(addSkillAdapter);


        } else {

            addSkillAdapter = new AddSkillAdapter(ActivityAddSkills.this, addSkillArrayList);
            mRecyclerview.setHasFixedSize(true);
            mRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
            mRecyclerview.setAdapter(addSkillAdapter);

        }


    }

    private void initViews() {

        backArrowImage = findViewById(R.id.backArrowImage);
        edAddSkills = findViewById(R.id.edAddSkills);
        mRecyclerview = findViewById(R.id.mRecyclerview);
        addSkillImage = findViewById(R.id.addSkillImage);
        btnSave = findViewById(R.id.btnSave);
        tvSkills = findViewById(R.id.tvSkills);
        tvSkills.setTypeface(tvSkills.getTypeface(), Typeface.BOLD);


        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addSkillImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edAddSkills.getText().toString().trim())) {

                    Toast.makeText(ActivityAddSkills.this, "Please add skills", Toast.LENGTH_SHORT).show();

                } else {


                    if (fromHomeFragment) {


                        String ed = edAddSkills.getText().toString();
                        arrayList.add(new Skills(ed));
                        addSkillAdapter.notifyDataSetChanged();
                        mRecyclerview.smoothScrollToPosition(addSkillAdapter.getItemCount() - 1);
                        edAddSkills.setText("");


                    } else {

                        String ed = edAddSkills.getText().toString();
                        addSkillArrayList.add(new AddSkill(ed));
                        addSkillAdapter.notifyDataSetChanged();
                        mRecyclerview.smoothScrollToPosition(addSkillAdapter.getItemCount() - 1);

                        edAddSkills.setText("");
                    }


                }
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingProgress();


                // TODO SHAHZEB HERE WE ARE CONVERTING A OBJECT TYPE LIST INTO STRING SO THAT WE CAN ADD SEPARATOR IN IT AND PASS AS STING


                if (fromHomeFragment) {
                    skillTemp = new ArrayList<>();

                    for (Skills skills : arrayList) {
                        skillTemp.add(skills.getUpdateSkills());
                    }
                    Skills = TextUtils.join("|", skillTemp);

                } else {

                    templist = new ArrayList<>();

                    for (AddSkill addSkill : addSkillArrayList) {
                        templist.add(addSkill.getUpdateSkills());
                    }
                    Skills = TextUtils.join("|", templist);
                }


                HttpModule.provideRepositoryService().editKeySkillUpdate(Hawk.get("GET_TOKEN"), Skills).enqueue(new Callback<EditKeySkillAndUpdate>() {
                    @Override
                    public void onResponse(Call<EditKeySkillAndUpdate> call, Response<EditKeySkillAndUpdate> response) {


                        if (response.body() != null && response.body().getError() == 0) {

                            mProgressDialog.dismiss();
                            Toast.makeText(ActivityAddSkills.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            finish();

                        } else {

                            mProgressDialog.dismiss();
                            Toast.makeText(ActivityAddSkills.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<EditKeySkillAndUpdate> call, Throwable t) {

                        mProgressDialog.dismiss();
                        System.out.println("ActivityAddSkills.onFailure " + t.toString());

                    }
                });


            }
        });


    }

    private void loadingProgress() {


        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();


    }
}
