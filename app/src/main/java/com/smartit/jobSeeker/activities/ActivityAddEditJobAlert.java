package com.smartit.jobSeeker.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.addedJobAlert.AddedJobAlert;
import com.smartit.jobSeeker.apiResponse.currencyType.CurrencyType;
import com.smartit.jobSeeker.apiResponse.displayEditJob.DisplayEditJob;
import com.smartit.jobSeeker.apiResponse.industry.Datum;
import com.smartit.jobSeeker.apiResponse.industry.Industry;
import com.smartit.jobSeeker.apiResponse.salaryType.SalaryType;
import com.smartit.jobSeeker.apiResponse.updatedJobAlert.UpdatedJobAlert;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.smartit.jobSeeker.multiSelectSpinnerWithLimit.MultipleSelectionSpinnerCurrencyType;
import com.smartit.jobSeeker.multiSelectSpinnerWithLimit.MultipleSelectionSpinnerIndustry;
import com.smartit.jobSeeker.multiSelectSpinnerWithLimit.MultipleSelectionSpinnerSalaryType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityAddEditJobAlert extends AppCompatActivity implements View.OnClickListener {


    private EditText edName, edMinExp, edMaxExp, edMinSalary, edMaxSalary, edKeySkills;

    private MultipleSelectionSpinnerIndustry spIndustry;
    private MultipleSelectionSpinnerCurrencyType spCurrencyType;
    private MultipleSelectionSpinnerSalaryType spSalaryType;
    private RadioGroup radioGroup;
    private RadioButton rbDaily, rbWeekly, rbMonthly;
    private Button btnSave;
    private ImageView backArrowImage;
    private String notificationValue;
    private int jobAlertId;
    private ArrayList<Integer> industryIdList = new ArrayList<>();
    private ArrayList<Datum> industryObjList = new ArrayList<>();
    private ArrayList<com.smartit.jobSeeker.apiResponse.currencyType.Datum> currencyObjList = new ArrayList<>();
    private ArrayList<com.smartit.jobSeeker.apiResponse.salaryType.Datum> salaryObjList = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private ArrayList<String> selectedIndustryList = new ArrayList<>();
    private ArrayList<String> selectedCurrencyType = new ArrayList<>();
    private ArrayList<String> selectedSalaryType = new ArrayList<>();
    boolean yourBool;
    private TextView tvItalic;
    private NoInternetDialog noInternetDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_job_alert);

        noInternetDialog = new NoInternetDialog.Builder(this).build();
        mProgressDialog = new ProgressDialog(ActivityAddEditJobAlert.this, R.style.AppTheme_Dark_Dialog);


        findTheIdsHere();
        loadingProgress();

        yourBool = Objects.requireNonNull(getIntent().getExtras()).getBoolean("BoolName");
        jobAlertId = getIntent().getIntExtra("JOB_ALERT_ID", 0);

        if (yourBool) {


            HttpModule.provideRepositoryService().displayEditJobAlertApi(Hawk.get("GET_TOKEN"), jobAlertId).enqueue(new Callback<DisplayEditJob>() {
                @Override
                public void onResponse(Call<DisplayEditJob> call, Response<DisplayEditJob> response) {


                    if (response.body() != null && response.body().getError() == 0) {

                        edName.setText(response.body().getData().getName());
                        edMinExp.setText(response.body().getData().getMinexp().toString());
                        edMaxExp.setText(response.body().getData().getMaxexp().toString());
                        edMinSalary.setText(response.body().getData().getMinsalary().toString());
                        edMaxSalary.setText(response.body().getData().getMaxsalary().toString());
                        edKeySkills.setText(response.body().getData().getSkills().toString());

                        selectedCurrencyType.add(response.body().getData().getCurrencyType());


                        if (response.body().getData().getSalaryType().equalsIgnoreCase("HOURLY")) {

                            selectedSalaryType.add("Hourly");

                        } else if (response.body().getData().getSalaryType().equalsIgnoreCase("PAYPERDAY")) {
                            selectedSalaryType.add("Pay Per Day");

                        } else if (response.body().getData().getSalaryType().equalsIgnoreCase("PAYPERWEEK")) {
                            selectedSalaryType.add("Pay Per Week");

                        } else if (response.body().getData().getSalaryType().equalsIgnoreCase("PAYPERMONTH")) {
                            selectedSalaryType.add("Pay Per Month");

                        } else if (response.body().getData().getSalaryType().equalsIgnoreCase("ANNUALLY")) {
                            selectedSalaryType.add("Annually");
                        }

                        for (int i = 0; i < response.body().getData().getIndustry().size(); i++) {
                            selectedIndustryList.add(response.body().getData().getIndustry().get(i).getValue());
                        }

                        if (response.body().getData().getNotification().equalsIgnoreCase("1")) {
                            rbDaily.setChecked(true);
                        } else if (response.body().getData().getNotification().equalsIgnoreCase("2")) {
                            rbWeekly.setChecked(true);
                        } else if (response.body().getData().getNotification().equalsIgnoreCase("3")) {
                            rbMonthly.setChecked(true);
                        }

                        industryApi();
                        currencyApi();
                        salaryTypeApi();

                    } else {
                        Toast.makeText(ActivityAddEditJobAlert.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                    }


                }

                @Override
                public void onFailure(Call<DisplayEditJob> call, Throwable t) {

                    System.out.println("ActivityAddEditJobAlert.onFailure " + t.getMessage());

                }
            });

        } else {
            loadingProgress();
            industryApi();
            currencyApi();
            salaryTypeApi();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    private void loadingProgress() {
        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void salaryTypeApi() {
        HttpModule.provideRepositoryService().salaryApi(Hawk.get("GET_TOKEN")).enqueue(new Callback<SalaryType>() {
            @Override
            public void onResponse(Call<SalaryType> call, Response<SalaryType> response) {

                if (response.body() != null && response.body().getError() == 0) {
                    mProgressDialog.dismiss();
                    salaryObjList.addAll(response.body().getData());
                    salarySp();
                } else {
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SalaryType> call, Throwable t) {
                mProgressDialog.dismiss();
            }
        });
    }

    private void salarySp() {

        ArrayList<String> salaryList = new ArrayList<>();

        for (com.smartit.jobSeeker.apiResponse.salaryType.Datum datum : salaryObjList) {
            salaryList.add(datum.getTitle());
        }
        spSalaryType.setItems(salaryList);


        // MARK :SELECTED VALUES WILL BE DISPLAYED

        if (yourBool) {
            if (selectedSalaryType != null) {

                String[] array = new String[selectedSalaryType.size()];

                for (int i = 0; i < selectedSalaryType.size(); i++) {
                    array[i] = selectedSalaryType.get(i);
                }

                spSalaryType.setSelection(array);
            }

        }


    }

    private ArrayList<String> getSalaryIds() {

        ArrayList<String> salaryList = new ArrayList<>();
//        LinkedList<String> selectedList = (LinkedList<String>) spSalaryType.getSelectedStrings();

        String selectedItem = String.valueOf(spSalaryType.getSelectedItem());

        for (com.smartit.jobSeeker.apiResponse.salaryType.Datum datum : salaryObjList) {

//            for (String str : selectedList) {

            if (datum.getTitle().equalsIgnoreCase(selectedItem)) {
                salaryList.add(datum.getTitle());
            }
        }
//        }

        return salaryList;

    }


    private void currencyApi() {
        HttpModule.provideRepositoryService().currencyApi(Hawk.get("GET_TOKEN")).enqueue(new Callback<CurrencyType>() {
            @Override
            public void onResponse(Call<CurrencyType> call, Response<CurrencyType> response) {

                if (response.body() != null && response.body().getError() == 0) {

                    currencyObjList.addAll(response.body().getData());
                    currencySp(response.body().getData());

                } else {

                    Toast.makeText(ActivityAddEditJobAlert.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<CurrencyType> call, Throwable t) {
                System.out.println("ActivityAddEditJobAlert.onFailure " + t.getMessage());

            }
        });
    }

    private void currencySp(List<com.smartit.jobSeeker.apiResponse.currencyType.Datum> data) {

        ArrayList<String> currencyList = new ArrayList<>();

        for (com.smartit.jobSeeker.apiResponse.currencyType.Datum datum : currencyObjList) {
            currencyList.add(datum.getTitle());
        }
        spCurrencyType.setItems(currencyList);

        // MARK DISPLAYING THE SELECTED VALUE

        if (yourBool) {

            if (selectedCurrencyType != null) {

                String[] array = new String[selectedCurrencyType.size()];

                for (int i = 0; i < selectedCurrencyType.size(); i++) {
                    array[i] = selectedCurrencyType.get(i);
                }

                spCurrencyType.setSelection(array);
            }


        }

    }

    private void industryApi() {


        HttpModule.provideRepositoryService().industryApi(Hawk.get("GET_TOKEN")).enqueue(new Callback<Industry>() {
            @Override
            public void onResponse(Call<Industry> call, Response<Industry> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    industryObjList.addAll(response.body().getData());
                    industrySp(response.body().getData());


                } else {

                }

            }

            @Override
            public void onFailure(Call<Industry> call, Throwable t) {

                System.out.println("ActivityAddEditJobAlert.onFailure " + t.toString());
            }
        });


    }

    private void industrySp(List<Datum> data) {

        ArrayList<String> industryList = new ArrayList<>();

        for (Datum datum : industryObjList) {

            industryList.add(datum.getName());
        }
        spIndustry.setItems(industryList);


        // MARK DISPLAYING THE SELECTED VALUE

        if (yourBool) {

            if (selectedIndustryList != null) {

                String[] array = new String[selectedIndustryList.size()];

                for (int i = 0; i < selectedIndustryList.size(); i++) {
                    array[i] = selectedIndustryList.get(i);
                }

                spIndustry.setSelection(array);


            }
        }
    }

    private ArrayList<Integer> getIndustryIds() {

        ArrayList<Integer> industryList = new ArrayList<>();
        LinkedList<String> selectedList = (LinkedList<String>) spIndustry.getSelectedStrings();

        for (Datum datum : industryObjList) {
            for (String str : selectedList) {
                if (datum.getName().equalsIgnoreCase(str)) {
                    industryList.add(datum.getId());
                }
            }
        }
        return industryList;
    }


    private ArrayList<Integer> getCurrencyIds() {

        ArrayList<Integer> currencyList = new ArrayList<>();
//        LinkedList<String> selectedList = (LinkedList<String>) spCurrencyType.getSelectedItem();

        String selectedItem = String.valueOf(spCurrencyType.getSelectedItem());


        for (com.smartit.jobSeeker.apiResponse.currencyType.Datum datum : currencyObjList) {

            if (datum.getTitle().equalsIgnoreCase(selectedItem)) {
                currencyList.add(datum.getKey());

            }
        }
        return currencyList;
    }


    private void findTheIdsHere() {

        edName = findViewById(R.id.edName);
        edMinExp = findViewById(R.id.edMinExp);
        edMaxExp = findViewById(R.id.edMaxExp);
        edMinSalary = findViewById(R.id.edMinSalary);
        edMaxSalary = findViewById(R.id.edMaxSalary);
        edKeySkills = findViewById(R.id.edKeySkills);

        spIndustry = findViewById(R.id.spIndustry);
        spCurrencyType = findViewById(R.id.spCurrencyType);
        spSalaryType = findViewById(R.id.spSalaryType);

//        radioGroup = findViewById(R.id.radioGroup);

        tvItalic = findViewById(R.id.tvItalic);
        tvItalic.setTypeface(tvItalic.getTypeface(), Typeface.ITALIC);

        rbDaily = findViewById(R.id.rbDaily);
        rbWeekly = findViewById(R.id.rbWeekly);
        rbMonthly = findViewById(R.id.rbMonthly);

        btnSave = findViewById(R.id.btnSave);
        backArrowImage = findViewById(R.id.backArrowImage);

        btnSave.setOnClickListener(this);
        backArrowImage.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.btnSave:

                if (TextUtils.isEmpty(edName.getText().toString().trim())) {
                    Toast.makeText(ActivityAddEditJobAlert.this, "Name field is required", Toast.LENGTH_LONG).show();

                } else if (spIndustry != null && spIndustry.getSelectedItem().equals("Industry")) {

                    Toast.makeText(ActivityAddEditJobAlert.this, "Industry field is required", Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(edMinExp.getText().toString().trim())) {

                    Toast.makeText(ActivityAddEditJobAlert.this, "Minimum experience is required", Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(edMaxExp.getText().toString().trim())) {
                    Toast.makeText(ActivityAddEditJobAlert.this, "Maximum experience is required", Toast.LENGTH_LONG).show();

                } else if (Integer.parseInt(edMinExp.getText().toString().trim()) > Integer.parseInt(edMaxExp.getText().toString().trim())) {
                    Toast.makeText(ActivityAddEditJobAlert.this, "Minimum experience can not be greater than maximum experience", Toast.LENGTH_LONG).show();

                } else if (Integer.parseInt(edMinExp.getText().toString().trim()) == Integer.parseInt(edMaxExp.getText().toString().trim())) {
                    Toast.makeText(ActivityAddEditJobAlert.this, "Minimum experience and maximum experience can not be equal", Toast.LENGTH_LONG).show();

                } else if (spCurrencyType != null && spCurrencyType.getSelectedItem().equals("Currency Type")) {
                    Toast.makeText(ActivityAddEditJobAlert.this, "Currency field is required", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(edMinSalary.getText().toString().trim())) {
                    Toast.makeText(ActivityAddEditJobAlert.this, "Minimum salary is required", Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(edMaxSalary.getText().toString().trim())) {
                    Toast.makeText(ActivityAddEditJobAlert.this, "Maximum salary is required", Toast.LENGTH_LONG).show();

                } else if (Integer.parseInt(edMinSalary.getText().toString().trim()) > Integer.parseInt(edMaxSalary.getText().toString().trim())) {
                    Toast.makeText(ActivityAddEditJobAlert.this, "Minimum salary can not be greater than maximum salary", Toast.LENGTH_LONG).show();

                } else if (Integer.parseInt(edMinSalary.getText().toString().trim()) == Integer.parseInt(edMaxSalary.getText().toString().trim())) {
                    Toast.makeText(ActivityAddEditJobAlert.this, "Minimum salary and maximum salary can not be equal", Toast.LENGTH_LONG).show();

                } else if (spSalaryType != null && spSalaryType.getSelectedItem().equals("Salary Type")) {
                    Toast.makeText(ActivityAddEditJobAlert.this, "Salary field is required", Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(edKeySkills.getText().toString().trim())) {
                    Toast.makeText(ActivityAddEditJobAlert.this, "Skill is required", Toast.LENGTH_LONG).show();

                } else if (edKeySkills.getText().toString().substring(0, 1).equalsIgnoreCase(",")) {
                    Toast.makeText(ActivityAddEditJobAlert.this, "Skill is required", Toast.LENGTH_LONG).show();

                } else if (!rbDaily.isChecked() && !rbWeekly.isChecked() && !rbMonthly.isChecked()) {
                    Toast.makeText(ActivityAddEditJobAlert.this, "Notification is required", Toast.LENGTH_LONG).show();

                } else {

                    if (yourBool) {


                        if (rbDaily.isChecked()) {
                            notificationValue = "1";
                        } else if (rbWeekly.isChecked()) {
                            notificationValue = "2";
                        } else if (rbMonthly.isChecked()) {
                            notificationValue = "3";
                        }

                        addEditJobAlertApi();


                    } else {

                        if (rbDaily.isChecked()) {
                            notificationValue = "1";
                        } else if (rbWeekly.isChecked()) {
                            notificationValue = "2";
                        } else if (rbMonthly.isChecked()) {
                            notificationValue = "3";
                        }

                        addJobAlertApi();

                    }


                }
                break;

            case R.id.backArrowImage:
                finish();
                break;

        }

    }

    private void addEditJobAlertApi() {


        HttpModule.provideRepositoryService().editJobAlertApi(Hawk.get("GET_TOKEN"), edName.getText().toString(),
                TextUtils.join(",", getCurrencyIds()), edMinSalary.getText().toString(),
                TextUtils.join(",", getIndustryIds()), edMaxSalary.getText().toString(),
                TextUtils.join(",", getSalaryIds()), edKeySkills.getText().toString(), notificationValue, edMinExp.getText().toString(),
                edMaxExp.getText().toString(), String.valueOf(jobAlertId)).enqueue(new Callback<UpdatedJobAlert>() {
            @Override
            public void onResponse(Call<UpdatedJobAlert> call, Response<UpdatedJobAlert> response) {


                if (response.body() != null && response.body().getError() == 0) {
                    finish();
                    Toast.makeText(ActivityAddEditJobAlert.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(ActivityAddEditJobAlert.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<UpdatedJobAlert> call, Throwable t) {

                System.out.println("ActivityAddEditJobAlert.onFailure " + t.toString());
            }
        });


    }

    private void addJobAlertApi() {


        HttpModule.provideRepositoryService().addJobAlertApi(Hawk.get("GET_TOKEN"), edName.getText().toString(),
                TextUtils.join(",", getCurrencyIds()), edMinSalary.getText().toString(),
                TextUtils.join(",", getIndustryIds()), edMaxSalary.getText().toString(),
                TextUtils.join(",", getSalaryIds()), edKeySkills.getText().toString(), notificationValue, edMinExp.getText().toString(),
                edMaxExp.getText().toString()).enqueue(new Callback<AddedJobAlert>() {

            @Override
            public void onResponse(Call<AddedJobAlert> call, Response<AddedJobAlert> response) {

                if (response.body() != null && response.body().getError() == 0) {

                    finish();
                    Toast.makeText(ActivityAddEditJobAlert.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(ActivityAddEditJobAlert.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<AddedJobAlert> call, Throwable t) {

                System.out.println("ActivityAddEditJobAlert.onFailure " + t.toString());


            }
        });

    }


}
