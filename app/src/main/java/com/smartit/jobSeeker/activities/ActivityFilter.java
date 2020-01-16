package com.smartit.jobSeeker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.adapters.filterAdapters.FunctionalAreaAdapter;
import com.smartit.jobSeeker.adapters.filterAdapters.IndustryAdapter;
import com.smartit.jobSeeker.adapters.filterAdapters.SkillAdapter;
import com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.SkillName;
import com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.functional_area.FunctionalAreaData;
import com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.functional_area.GetFunctionalAreaRequest;
import com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.job_search.SearchJobRequest;
import com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.job_type.Datum;
import com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.job_type.GetJobTypeRequest;
import com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.salary_type.GetSalaryRequest;
import com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.salary_type.SalaryTypeData;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.smartit.jobSeeker.httpRetrofit.HttpModuleForJobSearch;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityFilter extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recSkills, recIndustryType, recFundctionalArea;
    private EditText etSkills, etLocation, etMinimumExp, etMaximumExp, etMinSalary, etMaxSalary;
    private TextInputLayout tvLocation;
    private ImageView imgAdd;
    private List<SalaryTypeData> salaryTypeList = new ArrayList<>();
    private List<Datum> jobTypeList = new ArrayList<>();
    private List<SkillName> skillList = new ArrayList<>();
    private SkillAdapter skillAdapter;
    private TextView btnApply;
    private TextView tvSkillKeywords, tvbtnLocation, tvbtnExp, tvbtnIndustry, tvbtnFunctionalArea, tvbtnJobType, tvbtnSalary, tvbtnJobPosting, btnClear;
    private ConstraintLayout skilLayout, expLayout, jobTypeLayout, salaryLayout, jobPostingLayout;
    private List<FunctionalAreaData> industryList = new ArrayList<>();
    private List<FunctionalAreaData> functionalAreaList = new ArrayList<>();
    private Spinner spJobType, spSalaryType;
    private IndustryAdapter industryAdapter;
    private FunctionalAreaAdapter functionalAreaAdapter;
    private RadioGroup rgJobPostingDate;
    private ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Hawk.init(ActivityFilter.this).build();

        skillAdapter = new SkillAdapter(skillList, ActivityFilter.this);
        callAllApis();
        findViewId();
        tvSkillKeywords.setBackgroundColor(getResources().getColor(R.color.colorReetuBlue));
        tvSkillKeywords.setTextColor(getResources().getColor(R.color.white));
        skilLayout.setVisibility(View.VISIBLE);
    }

    private void callAllApis() {
        callJobTypeApi();
        callSalaryTypeApi();
        callFunctionalAreaApi();
        callIndustryTypeApi();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void callIndustryTypeApi() {
        try {
            HttpModule.provideRepositoryService().getIndustryType().enqueue(new Callback<GetFunctionalAreaRequest>() {
                @Override
                public void onResponse(Call<GetFunctionalAreaRequest> call, Response<GetFunctionalAreaRequest> response) {
                    if (response.body() != null) {
                        if (response.body().getError() == 0 && response.body().getData().size() > 0) {
                            industryList.addAll(response.body().getData());
                            industryAdapter = new IndustryAdapter(industryList, ActivityFilter.this);
                            recIndustryType.setAdapter(industryAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetFunctionalAreaRequest> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callFunctionalAreaApi() {

        try {
            HttpModule.provideRepositoryService().getFunctionalAreaFilter().enqueue(new Callback<GetFunctionalAreaRequest>() {
                @Override
                public void onResponse(Call<GetFunctionalAreaRequest> call, Response<GetFunctionalAreaRequest> response) {
                    if (response.body() != null) {
                        if (response.body().getError() == 0 && response.body().getData().size() > 0) {
                            functionalAreaList.addAll(response.body().getData());
                            functionalAreaAdapter = new FunctionalAreaAdapter(functionalAreaList, ActivityFilter.this);
                            recFundctionalArea.setAdapter(functionalAreaAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetFunctionalAreaRequest> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callSalaryTypeApi() {
        try {
            HttpModule.provideRepositoryService().getSalaryType().enqueue(new Callback<GetSalaryRequest>() {
                @Override
                public void onResponse(Call<GetSalaryRequest> call, Response<GetSalaryRequest> response) {
                    if (response.body() != null) {
                        if (response.body().getError() == 0 && response.body().getData().size() > 0) {
                            salaryTypeList.add(new SalaryTypeData("00", "Select Salary Type"));
                            salaryTypeList.addAll(response.body().getData());
                            ArrayAdapter<SalaryTypeData> adapter = new ArrayAdapter<SalaryTypeData>(getApplicationContext(), R.layout.spinner_item_for_select_job_type, salaryTypeList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spSalaryType.setAdapter(adapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetSalaryRequest> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callJobTypeApi() {


        HttpModule.provideRepositoryService().getFilterJobType().enqueue(new Callback<GetJobTypeRequest>() {
            @Override
            public void onResponse(Call<GetJobTypeRequest> call, Response<GetJobTypeRequest> response) {
                if (response.body().getError() == 0 && response.body().getData().size() > 0) {

                    jobTypeList.add(new Datum(00, "Select Job Type"));
                    jobTypeList.addAll(response.body().getData());
                    ArrayAdapter<Datum> adapter = new ArrayAdapter<Datum>(getApplicationContext(), R.layout.spinner_item_for_select_job_type, jobTypeList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spJobType.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<GetJobTypeRequest> call, Throwable t) {
            }
        });
    }


    private void findViewId() {
        btnClear = findViewById(R.id.textView2);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllData();
            }
        });
        btnApply = findViewById(R.id.textView13);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                gettingAllData();

            }
        });


        imageView2 = findViewById(R.id.imageView2);

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        rgJobPostingDate = findViewById(R.id.rg_radio_group);
        etMaxSalary = findViewById(R.id.max_salary);
        etMinSalary = findViewById(R.id.min_salary);
        spSalaryType = findViewById(R.id.spinner);
        spJobType = findViewById(R.id.editText2);
        etMaximumExp = findViewById(R.id.maximum_exprience);
        etMinimumExp = findViewById(R.id.minimum_exprience);
        etLocation = findViewById(R.id.etLocation);
        jobPostingLayout = findViewById(R.id.job_posting_layout);
        tvbtnJobPosting = findViewById(R.id.textView11);
        tvbtnJobPosting.setOnClickListener(this);
        tvbtnSalary = findViewById(R.id.textView9);
        tvbtnSalary.setOnClickListener(this);
        salaryLayout = findViewById(R.id.layout_salary);
        jobTypeLayout = findViewById(R.id.layout_job_type);
        imgAdd = findViewById(R.id.imageView3);
        tvbtnJobType = findViewById(R.id.textView10);
        tvbtnJobType.setOnClickListener(this);
        tvbtnFunctionalArea = findViewById(R.id.textView5);
        tvbtnFunctionalArea.setOnClickListener(this);
        etSkills = findViewById(R.id.etSkills);
        recSkills = findViewById(R.id.recSkills);
        tvLocation = findViewById(R.id.textInputLayout5);
        tvSkillKeywords = findViewById(R.id.textView4);
        tvSkillKeywords.setOnClickListener(this);
        tvbtnLocation = findViewById(R.id.textView3);
        skilLayout = findViewById(R.id.skill_layout);
        expLayout = findViewById(R.id.layout_expriences);
        tvbtnExp = findViewById(R.id.textView6);
        recIndustryType = findViewById(R.id.rec_industry_type);
        recIndustryType.setHasFixedSize(true);
//        recIndustryType.setAdapter(new IndustryAdapter(industryList, ActivityFilter.this));
        tvbtnIndustry = findViewById(R.id.textView7);
        recFundctionalArea = findViewById(R.id.rec_functional_area);
        tvbtnIndustry.setOnClickListener(this);
        tvbtnExp.setOnClickListener(this);
        tvbtnLocation.setOnClickListener(this);
        recSkills.setHasFixedSize(true);
        recSkills.setAdapter(skillAdapter);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!TextUtils.isEmpty(etSkills.getText().toString())) {
                    skillList.add(new SkillName(etSkills.getText().toString()));
                    skillAdapter.notifyDataSetChanged();
                    etSkills.setText("");

                } else {
                    etSkills.setError("Add skills");
                    etSkills.requestFocus();
                }

            }
        });
    }

    private void gettingAllData() {

        String allSkills = "";
        String functionalArea = "";
        String industry = "";
        String jobPostingDate = "";

        RadioButton spec1 = findViewById(rgJobPostingDate.getCheckedRadioButtonId());

        if (skillList.size() > 0) {
            for (SkillName skillName : skillList) {
                if (skillName.isChecked()) {
                    if (allSkills.equalsIgnoreCase(""))
                        allSkills = skillName.getSkillName();
                    else
                        allSkills = allSkills + "," + skillName.getSkillName();
                }

            }
        }
        //industryList
        if (functionalAreaList.size() > 0) {
            for (FunctionalAreaData functionalAreaData : functionalAreaList) {
                if (functionalAreaData.isIschecked()) {
                    if (functionalArea.equalsIgnoreCase(""))
                        functionalArea = String.valueOf(functionalAreaData.getId());
                    else
                        functionalArea = functionalArea + "," + String.valueOf(functionalAreaData.getId());
                }
            }

        }

        if (industryList.size() > 0) {
            for (FunctionalAreaData functionalAreaData : industryList) {
                if (functionalAreaData.isIschecked()) {
                    if (industry.equalsIgnoreCase(""))
                        industry = String.valueOf(functionalAreaData.getId());
                    else
                        industry = industry + "," + String.valueOf(functionalAreaData.getId());
                }
            }

        }

        if (spec1 != null) {

            if (spec1.isChecked()) {


                if (spec1.getText().toString().equalsIgnoreCase("1 Day Old"))

                    jobPostingDate = "1";


                else if (spec1.getText().toString().equalsIgnoreCase("3 Days Old"))
                    jobPostingDate = "3";

                else if (spec1.getText().toString().equalsIgnoreCase("7 Days Old"))

                    jobPostingDate = "7";

                else if (spec1.getText().toString().equalsIgnoreCase("15 Days Old"))

                    jobPostingDate = "15";

                else if (spec1.getText().toString().equalsIgnoreCase("30 Days Old"))

                    jobPostingDate = "30";


            }
        }


        Map<String, Object> jsonParams = new ArrayMap<>();
        //put something inside the map, could be null
        jsonParams.put("salary_type", spSalaryType.getSelectedItem().toString().equalsIgnoreCase("Select Salary Type") ? "" : spSalaryType.getSelectedItem().toString());
        jsonParams.put("location", etLocation.getText().toString());
        jsonParams.put("functional", functionalArea);
        jsonParams.put("posting_date", jobPostingDate);
        jsonParams.put("experience", etMaximumExp.getText().toString());
        jsonParams.put("industry", industry);
        jsonParams.put("skills", allSkills);
        jsonParams.put("offset", "1");
        jsonParams.put("salary_min", etMinSalary.getText().toString());
        jsonParams.put("salary_max", etMaxSalary.getText().toString());
        jsonParams.put("limit", "800");
        jsonParams.put("job_type", spJobType.getSelectedItem().toString().equalsIgnoreCase("Select Job Type") ? "" : spJobType.getSelectedItem().toString());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());


        HttpModuleForJobSearch.provideRepositoryService().searchJobs(Hawk.get("GET_TOKEN"), body).enqueue(new Callback<SearchJobRequest>() {
            @Override
            public void onResponse(Call<SearchJobRequest> call, Response<SearchJobRequest> response) {


                if (response.body() != null) {
                    if (response.body().getError() == 0) {


                        if (response.body().getData().size() > 0) {

                            Toast.makeText(ActivityFilter.this, "" + response.body().getData().size(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("list", (Serializable) response.body().getData());
                            setResult(RESULT_OK, intent);
                            finish();

                        } else {

                            Toast.makeText(ActivityFilter.this, "No data found", Toast.LENGTH_SHORT).show();
                        }


                    }
                } else {
                    Toast.makeText(ActivityFilter.this, "" + Objects.requireNonNull(response.body()).getData().size(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<SearchJobRequest> call, Throwable t) {
                Toast.makeText(ActivityFilter.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void clearAllData() {
        skillList.clear();
        skillAdapter.notifyDataSetChanged();
        etLocation.setText("");
        etMinimumExp.setText("");
        etMaximumExp.setText("");
        spJobType.setSelection(0);
        etMaxSalary.setText("");
        etMinSalary.setText("");
        etSkills.setText("");
        spSalaryType.setSelection(0);
        if (industryAdapter != null) {
            for (FunctionalAreaData functionalAreaData : industryList)
                if (functionalAreaData.isIschecked())
                    functionalAreaData.setIschecked(false);
            industryAdapter.notifyDataSetChanged();
        }
        if (functionalAreaAdapter != null)
            for (FunctionalAreaData functionalAreaData : functionalAreaList)
                if (functionalAreaData.isIschecked())
                    functionalAreaData.setIschecked(false);
        functionalAreaAdapter.notifyDataSetChanged();

        RadioButton spec1 = findViewById(rgJobPostingDate.getCheckedRadioButtonId());
        if (spec1 != null) {
            if (spec1.isChecked()) {
                spec1.setChecked(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView4://skill/keywords
                hideKeyboard();
                tvSkillKeywords.setBackgroundColor(getResources().getColor(R.color.colorReetuBlue));
                tvSkillKeywords.setTextColor(getResources().getColor(R.color.white));
                skilLayout.setVisibility(View.VISIBLE);
                tvLocation.setVisibility(View.GONE);
                tvbtnLocation.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnLocation.setTextColor(getResources().getColor(R.color.black));
                tvbtnExp.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnExp.setTextColor(getResources().getColor(R.color.black));
                expLayout.setVisibility(View.GONE);
                tvbtnIndustry.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnIndustry.setTextColor(getResources().getColor(R.color.black));
                recIndustryType.setVisibility(View.GONE);
                tvbtnFunctionalArea.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnFunctionalArea.setTextColor(getResources().getColor(R.color.black));
                recFundctionalArea.setVisibility(View.GONE);
                tvbtnJobType.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnJobType.setTextColor(getResources().getColor(R.color.black));
                jobTypeLayout.setVisibility(View.GONE);
                tvbtnSalary.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnSalary.setTextColor(getResources().getColor(R.color.black));
                salaryLayout.setVisibility(View.GONE);
                tvbtnJobPosting.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnJobPosting.setTextColor(getResources().getColor(R.color.black));
                jobPostingLayout.setVisibility(View.GONE);
                break;
            case R.id.textView3://Location
                hideKeyboard();
                tvSkillKeywords.setBackgroundColor(getResources().getColor(R.color.white));
                tvSkillKeywords.setTextColor(getResources().getColor(R.color.black));
                skilLayout.setVisibility(View.GONE);
                tvLocation.setVisibility(View.VISIBLE);
                tvbtnLocation.setBackgroundColor(getResources().getColor(R.color.colorReetuBlue));
                tvbtnLocation.setTextColor(getResources().getColor(R.color.white));
                tvbtnExp.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnExp.setTextColor(getResources().getColor(R.color.black));
                expLayout.setVisibility(View.GONE);
                tvbtnIndustry.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnIndustry.setTextColor(getResources().getColor(R.color.black));
                recIndustryType.setVisibility(View.GONE);
                tvbtnFunctionalArea.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnFunctionalArea.setTextColor(getResources().getColor(R.color.black));
                recFundctionalArea.setVisibility(View.GONE);
                tvbtnJobType.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnJobType.setTextColor(getResources().getColor(R.color.black));
                jobTypeLayout.setVisibility(View.GONE);
                tvbtnSalary.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnSalary.setTextColor(getResources().getColor(R.color.black));
                salaryLayout.setVisibility(View.GONE);
                tvbtnJobPosting.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnJobPosting.setTextColor(getResources().getColor(R.color.black));
                jobPostingLayout.setVisibility(View.GONE);
                break;
            case R.id.textView6:// Exprience
                hideKeyboard();
                tvbtnExp.setBackgroundColor(getResources().getColor(R.color.colorReetuBlue));
                tvbtnExp.setTextColor(getResources().getColor(R.color.white));
                expLayout.setVisibility(View.VISIBLE);
                tvSkillKeywords.setBackgroundColor(getResources().getColor(R.color.white));
                tvSkillKeywords.setTextColor(getResources().getColor(R.color.black));
                skilLayout.setVisibility(View.GONE);
                tvLocation.setVisibility(View.GONE);
                tvbtnLocation.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnLocation.setTextColor(getResources().getColor(R.color.black));
                tvbtnIndustry.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnIndustry.setTextColor(getResources().getColor(R.color.black));
                recIndustryType.setVisibility(View.GONE);
                tvbtnFunctionalArea.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnFunctionalArea.setTextColor(getResources().getColor(R.color.black));
                recFundctionalArea.setVisibility(View.GONE);
                tvbtnJobType.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnJobType.setTextColor(getResources().getColor(R.color.black));
                jobTypeLayout.setVisibility(View.GONE);
                tvbtnSalary.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnSalary.setTextColor(getResources().getColor(R.color.black));
                salaryLayout.setVisibility(View.GONE);
                tvbtnJobPosting.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnJobPosting.setTextColor(getResources().getColor(R.color.black));
                jobPostingLayout.setVisibility(View.GONE);
                break;
            case R.id.textView7:// Industry
                hideKeyboard();
                tvbtnExp.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnExp.setTextColor(getResources().getColor(R.color.black));
                expLayout.setVisibility(View.GONE);
                tvSkillKeywords.setBackgroundColor(getResources().getColor(R.color.white));
                tvSkillKeywords.setTextColor(getResources().getColor(R.color.black));
                skilLayout.setVisibility(View.GONE);
                tvLocation.setVisibility(View.GONE);
                tvbtnLocation.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnLocation.setTextColor(getResources().getColor(R.color.black));
                tvbtnFunctionalArea.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnFunctionalArea.setTextColor(getResources().getColor(R.color.black));
                recFundctionalArea.setVisibility(View.GONE);
                tvbtnJobType.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnJobType.setTextColor(getResources().getColor(R.color.black));
                jobTypeLayout.setVisibility(View.GONE);
                tvbtnIndustry.setBackgroundColor(getResources().getColor(R.color.colorReetuBlue));
                tvbtnIndustry.setTextColor(getResources().getColor(R.color.white));
                recIndustryType.setVisibility(View.VISIBLE);
                tvbtnSalary.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnSalary.setTextColor(getResources().getColor(R.color.black));
                salaryLayout.setVisibility(View.GONE);
                tvbtnJobPosting.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnJobPosting.setTextColor(getResources().getColor(R.color.black));
                jobPostingLayout.setVisibility(View.GONE);
                break;
            case R.id.textView5:// Functional Area
                hideKeyboard();
                tvbtnExp.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnExp.setTextColor(getResources().getColor(R.color.black));
                expLayout.setVisibility(View.GONE);
                tvSkillKeywords.setBackgroundColor(getResources().getColor(R.color.white));
                tvSkillKeywords.setTextColor(getResources().getColor(R.color.black));
                skilLayout.setVisibility(View.GONE);
                tvLocation.setVisibility(View.GONE);
                tvbtnLocation.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnLocation.setTextColor(getResources().getColor(R.color.black));
                tvbtnIndustry.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnIndustry.setTextColor(getResources().getColor(R.color.black));
                recIndustryType.setVisibility(View.GONE);
                tvbtnJobType.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnJobType.setTextColor(getResources().getColor(R.color.black));
                jobTypeLayout.setVisibility(View.GONE);
                tvbtnFunctionalArea.setBackgroundColor(getResources().getColor(R.color.colorReetuBlue));
                tvbtnFunctionalArea.setTextColor(getResources().getColor(R.color.white));
                recFundctionalArea.setVisibility(View.VISIBLE);
                tvbtnSalary.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnSalary.setTextColor(getResources().getColor(R.color.black));
                salaryLayout.setVisibility(View.GONE);
                tvbtnJobPosting.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnJobPosting.setTextColor(getResources().getColor(R.color.black));
                jobPostingLayout.setVisibility(View.GONE);
                break;
            case R.id.textView10:// Job Type
                hideKeyboard();
                tvbtnExp.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnExp.setTextColor(getResources().getColor(R.color.black));
                expLayout.setVisibility(View.GONE);
                tvSkillKeywords.setBackgroundColor(getResources().getColor(R.color.white));
                tvSkillKeywords.setTextColor(getResources().getColor(R.color.black));
                skilLayout.setVisibility(View.GONE);
                tvLocation.setVisibility(View.GONE);
                tvbtnLocation.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnLocation.setTextColor(getResources().getColor(R.color.black));
                tvbtnIndustry.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnIndustry.setTextColor(getResources().getColor(R.color.black));
                recIndustryType.setVisibility(View.GONE);
                tvbtnFunctionalArea.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnFunctionalArea.setTextColor(getResources().getColor(R.color.black));
                recFundctionalArea.setVisibility(View.GONE);
                tvbtnJobType.setBackgroundColor(getResources().getColor(R.color.colorReetuBlue));
                tvbtnJobType.setTextColor(getResources().getColor(R.color.white));
                jobTypeLayout.setVisibility(View.VISIBLE);
                tvbtnSalary.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnSalary.setTextColor(getResources().getColor(R.color.black));
                salaryLayout.setVisibility(View.GONE);
                tvbtnJobPosting.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnJobPosting.setTextColor(getResources().getColor(R.color.black));
                jobPostingLayout.setVisibility(View.GONE);
                break;
            case R.id.textView9:// Salary
                hideKeyboard();
                tvbtnExp.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnExp.setTextColor(getResources().getColor(R.color.black));
                expLayout.setVisibility(View.GONE);
                tvSkillKeywords.setBackgroundColor(getResources().getColor(R.color.white));
                tvSkillKeywords.setTextColor(getResources().getColor(R.color.black));
                skilLayout.setVisibility(View.GONE);
                tvLocation.setVisibility(View.GONE);
                tvbtnLocation.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnLocation.setTextColor(getResources().getColor(R.color.black));
                tvbtnIndustry.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnIndustry.setTextColor(getResources().getColor(R.color.black));
                recIndustryType.setVisibility(View.GONE);
                tvbtnFunctionalArea.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnFunctionalArea.setTextColor(getResources().getColor(R.color.black));
                recFundctionalArea.setVisibility(View.GONE);
                tvbtnJobType.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnJobType.setTextColor(getResources().getColor(R.color.black));
                jobTypeLayout.setVisibility(View.GONE);
                tvbtnJobPosting.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnJobPosting.setTextColor(getResources().getColor(R.color.black));
                jobPostingLayout.setVisibility(View.GONE);
                tvbtnSalary.setBackgroundColor(getResources().getColor(R.color.colorReetuBlue));
                tvbtnSalary.setTextColor(getResources().getColor(R.color.white));
                salaryLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.textView11:// Job Posting
                hideKeyboard();
                tvbtnExp.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnExp.setTextColor(getResources().getColor(R.color.black));
                expLayout.setVisibility(View.GONE);
                tvSkillKeywords.setBackgroundColor(getResources().getColor(R.color.white));
                tvSkillKeywords.setTextColor(getResources().getColor(R.color.black));
                skilLayout.setVisibility(View.GONE);
                tvLocation.setVisibility(View.GONE);
                tvbtnLocation.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnLocation.setTextColor(getResources().getColor(R.color.black));
                tvbtnIndustry.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnIndustry.setTextColor(getResources().getColor(R.color.black));
                recIndustryType.setVisibility(View.GONE);
                tvbtnFunctionalArea.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnFunctionalArea.setTextColor(getResources().getColor(R.color.black));
                recFundctionalArea.setVisibility(View.GONE);
                tvbtnJobType.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnJobType.setTextColor(getResources().getColor(R.color.black));
                jobTypeLayout.setVisibility(View.GONE);
                tvbtnSalary.setBackgroundColor(getResources().getColor(R.color.white));
                tvbtnSalary.setTextColor(getResources().getColor(R.color.black));
                salaryLayout.setVisibility(View.GONE);
                tvbtnJobPosting.setBackgroundColor(getResources().getColor(R.color.colorReetuBlue));
                tvbtnJobPosting.setTextColor(getResources().getColor(R.color.white));
                jobPostingLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}