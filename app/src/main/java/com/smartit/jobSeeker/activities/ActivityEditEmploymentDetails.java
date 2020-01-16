package com.smartit.jobSeeker.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.smartit.jobSeeker.apiResponse.getEmploymentDetailsForParticularJob.GetEmpDetailsParticular;
import com.smartit.jobSeeker.apiResponse.getState.State;
import com.smartit.jobSeeker.apiResponse.industry.Industry;
import com.smartit.jobSeeker.apiResponse.updateEmployment.UpdateEmployment;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.smartit.jobSeeker.multiSelectSpinnerWithLimit.MultipleSelectionSpinnerIndustryOnly;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityEditEmploymentDetails extends AppCompatActivity {


    private EditText edTitle, edCompany, edStartDate, edEndDate, edProgramDescription;
    private ImageView backArrowImage;
    private TextView tvEmpDetailsTitle;

    private Spinner spCountry, spState, spCity;
    private Button btnSubmit;

    private NoInternetDialog noInternetDialog;


    private MultipleSelectionSpinnerIndustryOnly spIndustry;


    private ArrayList<String> countryIdList = new ArrayList<>();
    private String holderCountry;

    private ArrayList<String> stateIdList = new ArrayList<>();
    private String holderState;


    private ArrayList<String> cityIdList = new ArrayList<>();
    private String holderCity;


    private ArrayList<com.smartit.jobSeeker.apiResponse.getCountry.Datum> countryList = new ArrayList<>();
    private ArrayList<com.smartit.jobSeeker.apiResponse.getState.Datum> stateList = new ArrayList<>();
    private ArrayList<com.smartit.jobSeeker.apiResponse.getCity.Datum> cityList = new ArrayList<>();

    private int countryId, stateId, cityId;
    private TextView tvState, tvCity;

    private boolean stateChecked = true;
    private boolean cityChecked = true;


    private int mYear, mMonth, mDay;
    private String d1, d2;
    private String m1, m2;
    private String y1, y2;


    private String mStartDate, mEndDate;
    private Date date1, date2;


    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private TextView tvWarning;
    private Button btnOk;


    private ArrayList<com.smartit.jobSeeker.apiResponse.industry.Datum> industrylist = new ArrayList<>();
    private ArrayList<String> getIdIndustryList = new ArrayList<>();

    private ProgressDialog mProgressDialog;

    private String id;
    private String matchCountry, matchState, matchCity;
    private ArrayList<String> ind = new ArrayList<>();


    private boolean isCountry = false;
    private boolean isState = false;
    private boolean isCity = false;
    private String startdate="";
    private boolean isStartdate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employment_details);
        Hawk.init(this).build();

        noInternetDialog = new NoInternetDialog.Builder(this).build();
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        initViews();


        if (getIntent() != null && getIntent().hasExtra("ID")) {
            id = getIntent().getStringExtra("ID");
        }

        loadingProgress();
        getEmploymentDetailsForParticular(id);
//        getIndustryApi();
//        getCountries();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void getEmploymentDetailsForParticular(String id) {

        HttpModule.provideRepositoryService().getEmploymentDetailsForParticularJob(Hawk.get("GET_TOKEN"), id).enqueue(new Callback<GetEmpDetailsParticular>() {
            @Override
            public void onResponse(Call<GetEmpDetailsParticular> call, Response<GetEmpDetailsParticular> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    edTitle.setText(response.body().getData().getTitle());
                    edCompany.setText(response.body().getData().getCompany());
                    edStartDate.setText(response.body().getData().getStartDate());
                    edEndDate.setText(response.body().getData().getEndDate());
                    edProgramDescription.setText(response.body().getData().getDesc());

                    matchCountry = response.body().getData().getCountry();
                    matchState = response.body().getData().getState();
                    matchCity = response.body().getData().getCity();

                    for (int i = 0; i < response.body().getData().getIndustry().size(); i++) {
                        ind.add(response.body().getData().getIndustry().get(i).getValue());
                    }


                    getIndustryApi();
                    getCountries();


                } else {

                    System.out.println("ActivityEditEmploymentDetails.onResponse " + Objects.requireNonNull(response.body()).getMessage());

                }


            }

            @Override
            public void onFailure(Call<GetEmpDetailsParticular> call, Throwable t) {
                System.out.println("ActivityEditEmploymentDetails.onFailure " + t.toString());


            }
        });


    }

    private void getCountries() {


        HttpModule.provideRepositoryService().getCountry(Hawk.get("GET_TOKEN")).enqueue(new Callback<Country>() {
            @Override
            public void onResponse(Call<Country> call, Response<Country> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    countryList.addAll(response.body().getData());
                    setupCountry(response.body().getData());


                } else {

                    System.out.println("ActivityRegister.accept " + Objects.requireNonNull(response.body()).getMessage());

                }
            }

            @Override
            public void onFailure(Call<Country> call, Throwable t) {

                System.out.println("ActivityEditEmploymentDetails.onFailure " + t.toString());
            }
        });


    }

    private void setupCountry(List<Datum> data) {


        ArrayList<String> countriesName = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            countriesName.add(data.get(i).getName());
            countryIdList.add(String.valueOf(data.get(i).getId()));


        }

        String compareValue = matchCountry;

        HintAdapter hintAdapter = new HintAdapter(this, R.layout.spinner_item_others_details, countriesName);
        spCountry.setDropDownWidth(1000);
        spCountry.setAdapter(hintAdapter);
        spCountry.setSelection(hintAdapter.getCount());
//

//        SpinnerAdapter arrayAdapter = new SpinnerAdapter(this, R.layout.spinner_item_others_details);
//        arrayAdapter.addAll(countriesName);
//        arrayAdapter.add("Country");
//        spCountry.setDropDownWidth(1000);
//        spCountry.setAdapter(arrayAdapter);
//        spCountry.setSelection(arrayAdapter.getCount());


        if (compareValue != null && !compareValue.trim().equalsIgnoreCase("")) {

            int spinnerPosition = hintAdapter.getPosition(compareValue);
            spCountry.setSelection(spinnerPosition);
        }

        itemSelectionFromCountrySpinner();


    }

    private void itemSelectionFromCountrySpinner() {


        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                holderCountry = (String) parent.getItemAtPosition(position);

                if (holderCountry.trim().equalsIgnoreCase("Country")) {
                    mProgressDialog.dismiss();

                } else {

                    for (com.smartit.jobSeeker.apiResponse.getCountry.Datum countryData : countryList) {
                        if (countryData.getName().equalsIgnoreCase(holderCountry)) {
                            countryId = countryData.getId();
                            tvState.setVisibility(View.INVISIBLE);
                        }
                    }
                    getStates(countryId);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getStates(int countryId) {

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
                System.out.println("ActivityEditEmploymentDetails.onFailure " + t.toString());


            }
        });


    }

    private void setupState(List<com.smartit.jobSeeker.apiResponse.getState.Datum> data) {


        ArrayList<String> listState = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            listState.add(data.get(i).getName());
            stateIdList.add(String.valueOf(data.get(i).getId()));

        }

        String compareValue = matchState;

//        HintAdapter hintAdapter = new HintAdapter(this, R.layout.spinner_item_others_details, listState);
//        spState.setDropDownWidth(1000);
//        spState.setAdapter(hintAdapter);
//        spState.setSelection(hintAdapter.getCount());


        SpinnerAdapter arrayAdapter = new SpinnerAdapter(this, R.layout.spinner_item_others_details);
        arrayAdapter.addAll(listState);
        arrayAdapter.add("State");
        spState.setDropDownWidth(1000);
        spState.setAdapter(arrayAdapter);
        spState.setSelection(arrayAdapter.getCount());


        if (compareValue != null && !compareValue.trim().equalsIgnoreCase("")) {

            if (!isState) {
                isState = true;
                int spinnerPosition = arrayAdapter.getPosition(compareValue);
                spState.setSelection(spinnerPosition);
            }

        }

        itemSelectionFromStateSpinner();


    }

    private void itemSelectionFromStateSpinner() {


        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                holderState = (String) parent.getItemAtPosition(position);
                stateChecked = false;

                for (com.smartit.jobSeeker.apiResponse.getState.Datum datumState : stateList) {

                    if (datumState.getName().equalsIgnoreCase(holderState)) {
                        stateId = datumState.getId();
                        tvCity.setVisibility(View.INVISIBLE);

                    }
                }

                getCities(stateId);
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
            cityIdList.add(String.valueOf(data.get(i).getId()));

        }


        String compareValue = matchCity;

//        HintAdapter hintAdapter = new HintAdapter(this, R.layout.spinner_item_others_details, cityListNames);
//        spCity.setDropDownWidth(1000);
//        spCity.setAdapter(hintAdapter);
//        spCity.setSelection(hintAdapter.getCount());


        SpinnerAdapter arrayAdapter = new SpinnerAdapter(this, R.layout.spinner_item_others_details);
        arrayAdapter.addAll(cityListNames);
        arrayAdapter.add("City");
        spCity.setDropDownWidth(1000);
        spCity.setAdapter(arrayAdapter);
        spCity.setSelection(arrayAdapter.getCount());


        if (compareValue != null && !compareValue.trim().equalsIgnoreCase("")) {

            if (!isCity) {
                isCity = true;

                int spinnerPosition = arrayAdapter.getPosition(compareValue);
                spCity.setSelection(spinnerPosition);
            }

        }

        itemSelectionFromCitySpinner();


    }

    private void itemSelectionFromCitySpinner() {


        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                holderCity = (String) parent.getItemAtPosition(position);
                cityChecked = false;
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

    private void getIndustryApi() {


        HttpModule.provideRepositoryService().industryApi(Hawk.get("GET_TOKEN")).enqueue(new Callback<Industry>() {
            @Override
            public void onResponse(Call<Industry> call, Response<Industry> response) {

                if (response.body() != null && response.body().getError() == 0) {

                    industrylist.addAll(response.body().getData());
                    setupIndustry(response.body().getData());
                } else {
                }


            }

            @Override
            public void onFailure(Call<Industry> call, Throwable t) {

                System.out.println("ActivityOthersDetails.onFailure" + t.toString());
            }
        });


    }

    private void setupIndustry(List<com.smartit.jobSeeker.apiResponse.industry.Datum> data) {

        ArrayList<String> industryList = new ArrayList<>();

        for (com.smartit.jobSeeker.apiResponse.industry.Datum datum : industrylist) {

            industryList.add(datum.getName());
            getIdIndustryList.add(String.valueOf(datum.getId()));
        }
        spIndustry.setItems(industryList);

        String[] array = new String[ind.size()];

        for (int i = 0; i < ind.size(); i++) {
            array[i] = ind.get(i);
        }
        spIndustry.setSelection(array);


    }


    private ArrayList<Integer> getIndustryIds() {


        ArrayList<Integer> industryList = new ArrayList<>();

        LinkedList<String> selectedList = (LinkedList<String>) spIndustry.getSelectedStrings();

        for (com.smartit.jobSeeker.apiResponse.industry.Datum datum : industrylist) {

            for (String str : selectedList) {
                if (datum.getName().equalsIgnoreCase(str)) {
                    industryList.add(datum.getId());
                }
            }
        }
        return industryList;
    }


    private void initViews() {

        edTitle = findViewById(R.id.edTitle);
        edCompany = findViewById(R.id.edCompany);
        edStartDate = findViewById(R.id.edStartDate);
        edEndDate = findViewById(R.id.edEndDate);
        edProgramDescription = findViewById(R.id.edProgramDescription);
        tvEmpDetailsTitle = findViewById(R.id.tvEmpDetailsTitle);
        tvEmpDetailsTitle.setTypeface(tvEmpDetailsTitle.getTypeface(), Typeface.BOLD);


        tvState = findViewById(R.id.tvState);
        tvCity = findViewById(R.id.tvCity);

        backArrowImage = findViewById(R.id.backArrowImage);
        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        spIndustry = findViewById(R.id.spIndustry);
        spCountry = findViewById(R.id.spCountry);
        spState = findViewById(R.id.spState);
        spCity = findViewById(R.id.spCity);

        btnSubmit = findViewById(R.id.btnSubmit);


        spState.setOnTouchListener(spinnerOnTouch);
        spState.setOnKeyListener(spinnerOnKey);


        spCity.setOnTouchListener(spinnerOnTouchCity);
        spCity.setOnKeyListener(spinnerOnKeyCity);


        edStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDate("start");


               /* // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityEditEmploymentDetails.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        edStartDate.setText((month + 1) + "/" + dayOfMonth + "/" + year);

                        d1 = String.valueOf(dayOfMonth);
                        m1 = String.valueOf((month + 1));
                        y1 = String.valueOf(year);

                        System.out.println("ActivityEditEducation.onDateSet " + dayOfMonth + "/" + (month + 1) + "/" + year);

                        mStartDate = dayOfMonth + "/" + (month + 1) + "/" + year;

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();

*/
            }
        });


        edEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isStartdate)
                    setDate("end");
                else
                    Toast.makeText(ActivityEditEmploymentDetails.this, "Please select start date", Toast.LENGTH_SHORT).show();



/*

                if (TextUtils.isEmpty(mStartDate)) {
                    edStartDate.setText(null);
                    edEndDate.setText(null);
                    Toast.makeText(ActivityEditEmploymentDetails.this, "Please select the start date first", Toast.LENGTH_LONG).show();
                } else {

                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityEditEmploymentDetails.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                            d2 = String.valueOf(dayOfMonth);
                            m2 = String.valueOf((month + 1));
                            y2 = String.valueOf(year);


                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                            try {
                                date1 = simpleDateFormat.parse(y1 + "/" + m1 + "/" + d1);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            try {
                                date2 = simpleDateFormat.parse(y2 + "/" + m2 + "/" + d2);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            if (date1.equals(date2)) {

                                dialogCannotBeEqual();
                                edEndDate.setText(null);

                            } else if (date1.before(date2)) {

                                edEndDate.setText(null);
                                edEndDate.setText((month + 1) + "/" + dayOfMonth + "/" + year);

                            } else if (date1.after(date2)) {

                                edEndDate.setText(null);
                                dialogGreaterThan();
                            }


                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();


                }
*/

            }

        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(edTitle.getText().toString())) {

                    Toast.makeText(ActivityEditEmploymentDetails.this, "Please enter title", Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(edCompany.getText().toString())) {

                    Toast.makeText(ActivityEditEmploymentDetails.this, "Please company name", Toast.LENGTH_LONG).show();

                } else if (spIndustry != null && spIndustry.getSelectedItem() != null) {

                    String mCountry = null;
                    mCountry = (String) spIndustry.getSelectedItem();

                    if (mCountry.equalsIgnoreCase("Industry")) {
                        Toast.makeText(ActivityEditEmploymentDetails.this, "Please enter industry", Toast.LENGTH_LONG).show();

                    } else {

                        if (spCity != null && spCity.getSelectedItem() != null) {

                            String mCity = null;
                            mCity = (String) spCity.getSelectedItem();
                            if (mCity.equalsIgnoreCase("City")) {
                                Toast.makeText(ActivityEditEmploymentDetails.this, "Please enter City", Toast.LENGTH_LONG).show();
                            } else {

                                if (TextUtils.isEmpty(edStartDate.getText().toString())) {
                                    Toast.makeText(ActivityEditEmploymentDetails.this, "Please enter Start date", Toast.LENGTH_LONG).show();

                                } else if (TextUtils.isEmpty(edEndDate.getText().toString())) {
                                    Toast.makeText(ActivityEditEmploymentDetails.this, "Please enter End date", Toast.LENGTH_LONG).show();
                                } else if (TextUtils.isEmpty(edProgramDescription.getText().toString())) {

                                    edProgramDescription.setError("Please enter program description");
//                                    edProgramDescription.requestFocus();
                                    Toast.makeText(ActivityEditEmploymentDetails.this, "Please enter program description", Toast.LENGTH_LONG).show();
                                } else {

                                    loadingProgress();
                                    updateEditEmploymentApiGoesHere(edTitle.getText().toString(), edCompany.getText().toString(), edStartDate.getText().toString(), edEndDate.getText().toString()
                                            , cityId, stateId, countryId, edProgramDescription.getText().toString(), TextUtils.join(",", getIndustryIds()), "1");


                                }


                            }
                        }


                    }

                }

            }
        });


    }

    private void setDate(String start) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthofyear, int dayofmonth) {

                if (start.equalsIgnoreCase("start")) {
                    edStartDate.setText((monthofyear + 1) + "/" + dayofmonth + "/" + year);
                    startdate = (monthofyear + 1) + "/" + dayofmonth + "/" + year;
//                    edEndDate.setText("Select End Date");
                    isStartdate = true;
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    Date strDate = null;
                    Date endDate = null;
                    try {
                        strDate = sdf.parse(startdate);
                        endDate = sdf.parse((monthofyear + 1) + "/" + dayofmonth + "/" + year);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (endDate.before(strDate)) {
                        dialogGreaterThan();
                    } else if (endDate.equals(strDate)) {
                        dialogCannotBeEqual();
                    } else {
                        edEndDate.setText((monthofyear + 1) + "/" + dayofmonth + "/" + year);
                    }

                }
            }
        }, year, month, day);
        dpd.show();
    }

    private void updateEditEmploymentApiGoesHere(String title, String company, String sDate, String eDate, int cityId, int stateId, int countryId, String description, String industryId, String mode) {


        HttpModule.provideRepositoryService().updateEditEmployment(Hawk.get("GET_TOKEN"), title, company, sDate, eDate,
                String.valueOf(cityId), String.valueOf(stateId), String.valueOf(countryId), description, industryId, "1", id).enqueue(new Callback<UpdateEmployment>() {
            @Override
            public void onResponse(Call<UpdateEmployment> call, Response<UpdateEmployment> response) {

                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();
                    Toast.makeText(ActivityEditEmploymentDetails.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(ActivityEditEmploymentDetails.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<UpdateEmployment> call, Throwable t) {

                mProgressDialog.dismiss();
                System.out.println("ActivityEditEmploymentDetails.onFailure " + t.toString());

            }
        });


    }


    private void loadingProgress() {

        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();


    }

    private void dialogGreaterThan() {

        LayoutInflater li = LayoutInflater.from(ActivityEditEmploymentDetails.this);
        View dialogView = li.inflate(R.layout.dialog_greater_than, null);

        initGreater(dialogView);

        alertDialogBuilder = new AlertDialog.Builder(ActivityEditEmploymentDetails.this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void initGreater(View dialogView) {

        tvWarning = dialogView.findViewById(R.id.tvWarning);
        tvWarning.setText("End date must be greater than start date");

        btnOk = dialogView.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });


    }

    private void dialogCannotBeEqual() {


        LayoutInflater li = LayoutInflater.from(ActivityEditEmploymentDetails.this);
        View dialogView = li.inflate(R.layout.dialog_cannot_be_equal, null);

        init(dialogView);

        alertDialogBuilder = new AlertDialog.Builder(ActivityEditEmploymentDetails.this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void init(View dialogView) {


        tvWarning = dialogView.findViewById(R.id.tvWarning);
        tvWarning.setText("Can not be equal");


        btnOk = dialogView.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });


    }


    private View.OnTouchListener spinnerOnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                //Your code

                if (stateChecked) {
                    Toast.makeText(ActivityEditEmploymentDetails.this, "Please enter the country first", Toast.LENGTH_LONG).show();
                }
                {
                    System.out.println("ActivityEditEducation.onTouch");
                }


            }
            return false;
        }
    };
    private static View.OnKeyListener spinnerOnKey = new View.OnKeyListener() {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                //your code

                return true;
            } else {
                return false;
            }
        }
    };


    private View.OnTouchListener spinnerOnTouchCity = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                //Your code

                if (cityChecked) {
                    Toast.makeText(ActivityEditEmploymentDetails.this, "Please enter the state first", Toast.LENGTH_LONG).show();
                }
                {
                    System.out.println("ActivityEditEducation.onTouch");
                }


            }
            return false;
        }
    };
    private static View.OnKeyListener spinnerOnKeyCity = new View.OnKeyListener() {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                //your code

                return true;
            } else {
                return false;
            }
        }
    };


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