package com.smartit.jobSeeker.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.HintAdapter;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.getCity.GetCity;
import com.smartit.jobSeeker.apiResponse.getCountry.Country;
import com.smartit.jobSeeker.apiResponse.getCountry.Datum;
import com.smartit.jobSeeker.apiResponse.getState.State;
import com.smartit.jobSeeker.apiResponse.updateBasicProfile.UpdateBasicProfile;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityUpdateBasicInformation extends AppCompatActivity {


    private ImageView backArrowImage;
    private EditText edCurrentJobTitle, edCurrentCompany, edZipcode;
    private Spinner spinnerYear, spinnerMonths, spCountry, spState, spCity;
    private Button btnSubmit;

    private String expInYears, expInMonths;

    private String holderCountry, holderState, holderCity;


    private int countryId, stateId, cityId;
    private ArrayList<com.smartit.jobSeeker.apiResponse.getCountry.Datum> countryList = new ArrayList<>();
    private ArrayList<com.smartit.jobSeeker.apiResponse.getState.Datum> stateList = new ArrayList<>();
    private ArrayList<com.smartit.jobSeeker.apiResponse.getCity.Datum> cityList = new ArrayList<>();

    private ProgressDialog mProgressDialog;


    private String mCountry, mState, mCity, mZipcode, mExpe, mExpeInMonths;
    private String mJobTitle, mJobCompany;
    private TextView tvTitleUBI;
    private boolean isStateChecked = false;
    private boolean isCityChecked = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_basic_information);
        Hawk.init(this).build();

        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        if (getIntent() != null && getIntent().hasExtra("mCountry")) {
            mCountry = getIntent().getStringExtra("mCountry");

        }

        if (getIntent() != null && getIntent().hasExtra("mState")) {
            mState = getIntent().getStringExtra("mState");

        }

        if (getIntent() != null && getIntent().hasExtra("mCity")) {
            mCity = getIntent().getStringExtra("mCity");
        }

        if (getIntent() != null && getIntent().hasExtra("mZipcode")) {
            mZipcode = getIntent().getStringExtra("mZipcode");
        }
        if (getIntent() != null && getIntent().hasExtra("mExperience")) {
            mExpe = getIntent().getStringExtra("mExperience");
        }

        if (getIntent() != null && getIntent().hasExtra("mExperienceInMonths")) {
            mExpeInMonths = getIntent().getStringExtra("mExperienceInMonths");
        }


        if (getIntent() != null && getIntent().hasExtra("mJobTitle")) {
            mJobTitle = getIntent().getStringExtra("mJobTitle");
        }


        if (getIntent() != null && getIntent().hasExtra("mCompanyName")) {
            mJobCompany = getIntent().getStringExtra("mCompanyName");
        }


        initViewsHere();
        loadingProgress();

        getExperienceInYears();
        getMonths();
        getContry();


    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void loadingProgress() {

        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

    }

    private void getContry() {

        HttpModule.provideRepositoryService().getCountry(Hawk.get("GET_TOKEN")).enqueue(new Callback<Country>() {
            @Override
            public void onResponse(Call<Country> call, Response<Country> response) {

                if (response.body() != null && response.body().getError() == 0) {


                    countryList.addAll(response.body().getData());
                    setupCountry(response.body().getData());


                } else {
                    mProgressDialog.dismiss();
                }


            }

            @Override
            public void onFailure(Call<Country> call, Throwable t) {

                mProgressDialog.dismiss();
                System.out.println("ActivityUpdateBasicInformation.onFailure " + t.toString());


            }
        });


    }

    private void setupCountry(List<Datum> data) {


        ArrayList<String> listCountry = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {

            listCountry.add(data.get(i).getName());

        }

        String compareValueForSalary = mCountry;

//        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_item_others_details_for_ubi, listCountry);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spCountry.setDropDownWidth(1000);
//        spCountry.setAdapter(adapter);

        SpinnerAdapter arrayAdapter = new SpinnerAdapter(this, R.layout.spinner_item_others_details_for_ubi);
        arrayAdapter.addAll(listCountry);
        arrayAdapter.add("N/A");
        spCountry.setDropDownWidth(1000);
        spCountry.setAdapter(arrayAdapter);
        spCountry.setSelection(arrayAdapter.getCount());


        if (compareValueForSalary != null && !compareValueForSalary.trim().equalsIgnoreCase("")) {

            int spinnerPosition = arrayAdapter.getPosition(compareValueForSalary);
            spCountry.setSelection(spinnerPosition);
        }


        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                holderCountry = (String) parent.getItemAtPosition(position);

                if (holderCountry.trim().equalsIgnoreCase("N/A")) {
                    System.out.println("ActivityUpdateBasicInformation.onItemSelected " + holderCountry);
                    mProgressDialog.dismiss();

                } else {
                    for (com.smartit.jobSeeker.apiResponse.getCountry.Datum countryData : countryList) {

                        if (countryData.getName().equalsIgnoreCase(holderCountry)) {
                            countryId = countryData.getId();
                        }
                    }
                    getState(countryId);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getState(int countryId) {

        HttpModule.provideRepositoryService().getState(Hawk.get("GET_TOKEN"), countryId).enqueue(new Callback<State>() {
            @Override
            public void onResponse(Call<State> call, Response<State> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    stateList.addAll(response.body().getData());
                    setupState(response.body().getData());

                } else {
                    System.out.println("ActivityEditEducation.onResponse " + Objects.requireNonNull(response.body()).getMessage());

                }


            }

            @Override
            public void onFailure(Call<State> call, Throwable t) {

            }
        });


    }


    private void setupState(List<com.smartit.jobSeeker.apiResponse.getState.Datum> data) {


        ArrayList<String> listState = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            listState.add(data.get(i).getName());

        }

        String compareValueForSalary = mState;

//        ArrayAdapter yearAdapter = new ArrayAdapter(this, R.layout.spinner_item_others_details_for_ubi, listState);
//        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spState.setDropDownWidth(1000);
//        spState.setAdapter(yearAdapter);


        SpinnerAdapter arrayAdapter = new SpinnerAdapter(this, R.layout.spinner_item_others_details_for_ubi);
        arrayAdapter.addAll(listState);
        arrayAdapter.add("N/A");
        spState.setDropDownWidth(1000);
        spState.setAdapter(arrayAdapter);
        spState.setSelection(arrayAdapter.getCount());


        if (compareValueForSalary != null && !compareValueForSalary.trim().equalsIgnoreCase("")) {

            if (!isStateChecked) {
                isStateChecked = true;
                int spinnerPosition = arrayAdapter.getPosition(compareValueForSalary);
                spState.setSelection(spinnerPosition);
            }

        }


        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                holderState = (String) parent.getItemAtPosition(position);

                if (holderState.trim().equalsIgnoreCase("State")) {

                    mProgressDialog.dismiss();

                } else {

                    for (com.smartit.jobSeeker.apiResponse.getState.Datum datumState : stateList) {

                        if (datumState.getName().equalsIgnoreCase(holderState)) {
                            stateId = datumState.getId();
                        }
                    }

                    getCities(stateId);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getCities(int stateId) {


        HttpModule.provideRepositoryService().getCity(Hawk.get("GET_TOKEN"), stateId).enqueue(new Callback<GetCity>() {
            @Override
            public void onResponse(Call<GetCity> call, Response<GetCity> response) {

                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();
                    cityList.addAll(response.body().getData());
                    setupCities(response.body().getData());


                } else {

                    mProgressDialog.dismiss();
                    System.out.println("ActivityEditEducation.onResponse " + Objects.requireNonNull(response.body()).getMessage());
                }


            }

            @Override
            public void onFailure(Call<GetCity> call, Throwable t) {

                mProgressDialog.dismiss();
            }
        });


    }


    private void setupCities(List<com.smartit.jobSeeker.apiResponse.getCity.Datum> data) {


        ArrayList<String> cityListNames = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            cityListNames.add(data.get(i).getName());

        }

        String compareValueForSalary = mCity;

//        ArrayAdapter yearAdapter = new ArrayAdapter(this, R.layout.spinner_item_others_details_for_ubi, cityListNames);
//        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spCity.setDropDownWidth(1000);
//        spCity.setAdapter(yearAdapter);


        SpinnerAdapter arrayAdapter = new SpinnerAdapter(this, R.layout.spinner_item_others_details_for_ubi);
        arrayAdapter.addAll(cityListNames);
        arrayAdapter.add("N/A");
        spCity.setDropDownWidth(1000);
        spCity.setAdapter(arrayAdapter);
        spCity.setSelection(arrayAdapter.getCount());


        if (compareValueForSalary != null && !compareValueForSalary.trim().equalsIgnoreCase("")) {

            if (!isCityChecked) {
                isCityChecked = true;
                int spinnerPosition = arrayAdapter.getPosition(compareValueForSalary);
                spCity.setSelection(spinnerPosition);
            }
        }


        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                holderCity = (String) parent.getItemAtPosition(position);

                for (com.smartit.jobSeeker.apiResponse.getCity.Datum datumCity : cityList) {

                    if (datumCity.getName().equalsIgnoreCase(holderCity)) {
                        cityId = datumCity.getId();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getMonths() {

        ArrayList<String> monthList = new ArrayList<>();

        monthList.add("0");
        monthList.add("1");
        monthList.add("2");
        monthList.add("3");
        monthList.add("4");
        monthList.add("5");
        monthList.add("6");
        monthList.add("7");
        monthList.add("8");
        monthList.add("9");
        monthList.add("10");
        monthList.add("11");
        monthList.add("12");


        String expInMonthValues = mExpeInMonths;


        ArrayAdapter monthsAdapter = new ArrayAdapter(this, R.layout.spinner_item_others_details_for_ubi, monthList);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonths.setAdapter(monthsAdapter);


        if (expInMonthValues != null) {

            int spinnerPosition = monthsAdapter.getPosition(expInMonthValues);
            spinnerMonths.setSelection(spinnerPosition);
        }


        spinnerMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                expInMonths = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getExperienceInYears() {

        ArrayList<String> expInYearsList = new ArrayList<>();

        expInYearsList.add("0");
        expInYearsList.add("1");
        expInYearsList.add("2");
        expInYearsList.add("3");
        expInYearsList.add("4");
        expInYearsList.add("5");
        expInYearsList.add("6");
        expInYearsList.add("7");
        expInYearsList.add("8");
        expInYearsList.add("9");
        expInYearsList.add("10");
        expInYearsList.add("11");
        expInYearsList.add("12");
        expInYearsList.add("13");
        expInYearsList.add("14");
        expInYearsList.add("15");
        expInYearsList.add("16");
        expInYearsList.add("17");
        expInYearsList.add("18");
        expInYearsList.add("19");
        expInYearsList.add("20");
        expInYearsList.add("21");
        expInYearsList.add("22");
        expInYearsList.add("23");
        expInYearsList.add("24");
        expInYearsList.add("25");
        expInYearsList.add("26");
        expInYearsList.add("27");
        expInYearsList.add("28");
        expInYearsList.add("29");
        expInYearsList.add("30");
        expInYearsList.add("31");
        expInYearsList.add("32");
        expInYearsList.add("33");
        expInYearsList.add("34");
        expInYearsList.add("35");
        expInYearsList.add("36");
        expInYearsList.add("37");
        expInYearsList.add("38");
        expInYearsList.add("39");
        expInYearsList.add("40");
        expInYearsList.add("41");
        expInYearsList.add("42");
        expInYearsList.add("43");
        expInYearsList.add("44");
        expInYearsList.add("45");
        expInYearsList.add("46");
        expInYearsList.add("47");
        expInYearsList.add("48");
        expInYearsList.add("49");
        expInYearsList.add("50");
        expInYearsList.add("51");
        expInYearsList.add("52");
        expInYearsList.add("53");
        expInYearsList.add("54");
        expInYearsList.add("55");
        expInYearsList.add("56");
        expInYearsList.add("57");
        expInYearsList.add("58");
        expInYearsList.add("59");
        expInYearsList.add("60");


        String compareValues = mExpe;

        ArrayAdapter yearAdapter = new ArrayAdapter(this, R.layout.spinner_item_others_details_for_ubi, expInYearsList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);


        if (compareValues != null) {

            int spinnerPosition = yearAdapter.getPosition(compareValues);
            spinnerYear.setSelection(spinnerPosition);

        }

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                expInYears = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void initViewsHere() {

        backArrowImage = findViewById(R.id.backArrowImage);
        edCurrentJobTitle = findViewById(R.id.edCurrentJobTitle);
        edCurrentCompany = findViewById(R.id.edCurrentCompany);
        edZipcode = findViewById(R.id.edZipcode);

        spinnerYear = findViewById(R.id.spinnerYear);
        spinnerMonths = findViewById(R.id.spinnerMonths);
        spCountry = findViewById(R.id.spCountry);
        spState = findViewById(R.id.spState);
        spCity = findViewById(R.id.spCity);
        btnSubmit = findViewById(R.id.btnSubmit);

        tvTitleUBI = findViewById(R.id.tvTitleUBI);
        tvTitleUBI.setTypeface(tvTitleUBI.getTypeface(), Typeface.BOLD);

        edZipcode.setText(mZipcode);
        edCurrentJobTitle.setText(mJobTitle);
        edCurrentCompany.setText(mJobCompany);


        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(edCurrentJobTitle.getText().toString().trim())) {

                    Toast.makeText(ActivityUpdateBasicInformation.this, "Please enter title", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(edCurrentCompany.getText().toString().trim())) {

                    Toast.makeText(ActivityUpdateBasicInformation.this, "Please enter current company", Toast.LENGTH_SHORT).show();

                } else if (spState != null && spState.getSelectedItem().equals("N/A")) {

                    Toast.makeText(ActivityUpdateBasicInformation.this, "Please enter state", Toast.LENGTH_SHORT).show();

                } else if (spCity != null && spCity.getSelectedItem().equals("N/A")) {

                    Toast.makeText(ActivityUpdateBasicInformation.this, "Please enter city", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(edZipcode.getText().toString().trim())) {
                    Toast.makeText(ActivityUpdateBasicInformation.this, "Please enter zipcode", Toast.LENGTH_SHORT).show();
                } else {
                    loadingProgress();
                    editBasicProfileApi();
                }

            }
        });


    }

    private void editBasicProfileApi() {


        HttpModule.provideRepositoryService().editBasicProfile(Hawk.get("GET_TOKEN"), edCurrentJobTitle.getText().toString(),
                edCurrentCompany.getText().toString(), expInYears, expInMonths, String.valueOf(cityId), String.valueOf(stateId),
                String.valueOf(countryId), edZipcode.getText().toString()).enqueue(new Callback<UpdateBasicProfile>() {
            @Override
            public void onResponse(Call<UpdateBasicProfile> call, Response<UpdateBasicProfile> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();
                    Toast.makeText(ActivityUpdateBasicInformation.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(ActivityUpdateBasicInformation.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<UpdateBasicProfile> call, Throwable t) {

                mProgressDialog.dismiss();
                System.out.println("ActivityUpdateBasicInformation.onFailure " + t.toString());

            }
        });


    }

    private class SpinnerAdapter extends ArrayAdapter<String> {

        private SpinnerAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            int count = super.getCount();
            return count > 0 ? count - 1 : count;
        }
    }

}
