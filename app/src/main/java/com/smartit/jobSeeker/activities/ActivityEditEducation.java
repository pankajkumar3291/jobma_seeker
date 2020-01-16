package com.smartit.jobSeeker.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
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
import com.smartit.jobSeeker.apiResponse.degreeType.Datum;
import com.smartit.jobSeeker.apiResponse.degreeType.DegreeType;
import com.smartit.jobSeeker.apiResponse.getCity.GetCity;
import com.smartit.jobSeeker.apiResponse.getCountry.Country;
import com.smartit.jobSeeker.apiResponse.getEducationDetailsForParticularJob.GetEduDetailsParticular;
import com.smartit.jobSeeker.apiResponse.getState.State;
import com.smartit.jobSeeker.apiResponse.updateEducation.UpdateEducation;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityEditEducation extends AppCompatActivity {


    private ImageView backArrowImage;
    private Spinner spinnerCountry, spinnerState, spinnerCity, spinnerDegree;
    private EditText edFeildOfStudy, edInstitute, edStartDate, edEndDate, edProgramDescription;
    private Button btnSubmit;


    private ArrayList<String> listDegreeID = new ArrayList<>();
    private String holderDegreeType;


    private ArrayList<String> countryIdList = new ArrayList<>();
    private String holderCountry;


    private ArrayList<String> stateIdList = new ArrayList<>();
    private String holderState;


    private ArrayList<String> cityIdList = new ArrayList<>();
    private String holderCity;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    private ArrayList<com.smartit.jobSeeker.apiResponse.getCountry.Datum> countryList = new ArrayList<>();
    private ArrayList<com.smartit.jobSeeker.apiResponse.getState.Datum> stateList = new ArrayList<>();
    private ArrayList<com.smartit.jobSeeker.apiResponse.getCity.Datum> cityList = new ArrayList<>();

    private int countryId, stateId, cityId;

    private TextView tvState, tvCity;

    private int mYear, mMonth, mDay;

    private String mStartDate, mEndDate;

    private String d1, d2;
    private String m1, m2;
    private String y1, y2;

    private Date date1, date2;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private TextView tvWarning;
    private Button btnOk;

    private boolean stateChecked = true;
    private boolean cityChecked = true;

    private ProgressDialog mProgressDialog;

    private boolean editClickCheck;
    private String id;
    private String matchCountry, matchState, matchCity, matchDegree;

    private NoInternetDialog noInternetDialog;

    private boolean isState = false;
    private boolean isCity = false;
    private String startdate = "";
    private boolean isStartdate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);
        Hawk.init(this).build();

        noInternetDialog = new NoInternetDialog.Builder(this).build();
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        initViewsHere();

        // for differentiating  the  click
        if (getIntent() != null && getIntent().hasExtra("Edi_Click")) {
            editClickCheck = getIntent().getBooleanExtra("Edi_Click", true);
        }

        // id for particular employment detail job

        if (getIntent() != null && getIntent().hasExtra("ID")) {

            id = getIntent().getStringExtra("ID");
        }


        if (editClickCheck) {

            loadingProgress();
            getEducationDetailsForParticularJob(id);
            getDegreeType();
            getCountries();


        } else {

            loadingProgress();
            getDegreeType();
            getCountries();

        }


    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void getEducationDetailsForParticularJob(String id) {


        HttpModule.provideRepositoryService().getEducationDetailsForParticular(Hawk.get("GET_TOKEN"), id).enqueue(new Callback<GetEduDetailsParticular>() {
            @Override
            public void onResponse(Call<GetEduDetailsParticular> call, Response<GetEduDetailsParticular> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    if (response.body().getData().getEduLevel().trim().equalsIgnoreCase("")) {
                        edFeildOfStudy.setText("N/A");
                    } else {
                        edFeildOfStudy.setText(response.body().getData().getEduLevel());
                    }
                    if (response.body().getData().getInstitute().trim().equalsIgnoreCase("")) {
                        edInstitute.setText("N/A");
                    } else {
                        edInstitute.setText(response.body().getData().getInstitute());
                    }

                    if (response.body().getData().getStartdate().trim().equalsIgnoreCase("")) {
                        edStartDate.setText("N/A");
                    } else {
                        edStartDate.setText(response.body().getData().getStartdate());
                    }

                    if (response.body().getData().getEnddate().trim().equalsIgnoreCase("")) {
                        edEndDate.setText("N/A");
                    } else {
                        edEndDate.setText(response.body().getData().getEnddate());
                    }

                    if (response.body().getData().getSummary().trim().equalsIgnoreCase("")) {
                        edProgramDescription.setText("N/A");
                    } else {
                        edProgramDescription.setText(response.body().getData().getSummary());
                    }


                    matchCountry = response.body().getData().getCountry();
                    matchState = response.body().getData().getState();
                    matchCity = response.body().getData().getCity();
                    matchDegree = response.body().getData().getDegree();


                } else {

                    System.out.println("ActivityEditEducation.onResponse " + Objects.requireNonNull(response.body()).getMessage());
                }


            }

            @Override
            public void onFailure(Call<GetEduDetailsParticular> call, Throwable t) {

            }
        });


    }

    private void getCities(int holderState) {


        HttpModule.provideRepositoryService().getCity(Hawk.get("GET_TOKEN"), holderState).enqueue(new Callback<GetCity>() {
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
                System.out.println("ActivityEditEducation.onFailure" + t.toString());
            }
        });


    }

    private void setupCities(List<com.smartit.jobSeeker.apiResponse.getCity.Datum> data) {


        ArrayList<String> cityListNames = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            cityListNames.add(data.get(i).getName());
            cityIdList.add(String.valueOf(data.get(i).getId()));

        }

//        cityListNames.add("City");
        String compareValue = matchCity;


//        HintAdapter hintAdapter = new HintAdapter(this, R.layout.spinner_item_others_details, cityListNames);
//        spinnerCity.setDropDownWidth(1000);
//        spinnerCity.setAdapter(hintAdapter);
//        spinnerCity.setSelection(hintAdapter.getCount());
//


        SpinnerAdapter arrayAdapter = new SpinnerAdapter(this, R.layout.spinner_item_others_details);
        arrayAdapter.addAll(cityListNames);
        arrayAdapter.add("City");
        spinnerCity.setDropDownWidth(1000);
        spinnerCity.setAdapter(arrayAdapter);
        spinnerCity.setSelection(arrayAdapter.getCount());


        if (compareValue != null && !compareValue.trim().equalsIgnoreCase("")) {

            if (!isCity) {
                isCity = true;
                int spinnerPosition = arrayAdapter.getPosition(compareValue);
                spinnerCity.setSelection(spinnerPosition);
            }


        }


        itemSelectionFromCitySpinner();

    }

    private void itemSelectionFromCitySpinner() {

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                holderCity = (String) parent.getItemAtPosition(position);

                if (holderCity.trim().equalsIgnoreCase("City")) {
                    mProgressDialog.dismiss();
                } else {

                    cityChecked = false;
                    for (com.smartit.jobSeeker.apiResponse.getCity.Datum datumCity : cityList) {
                        if (datumCity.getName().equalsIgnoreCase(holderCity)) {
                            cityId = datumCity.getId();
                        }

                    }

                    System.out.println("ActivityEditEducation.onItemSelected " + cityId);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getStates(int holderCountry) {


        HttpModule.provideRepositoryService().getState(Hawk.get("GET_TOKEN"), holderCountry).enqueue(new Callback<State>() {
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
            stateIdList.add(String.valueOf(data.get(i).getId()));

        }
//        listState.add("State");

        String compareValue = matchState;


//        HintAdapter hintAdapter = new HintAdapter(this, R.layout.spinner_item_others_details, listState);
//        spinnerState.setDropDownWidth(1000);
//        spinnerState.setAdapter(hintAdapter);
//        spinnerState.setSelection(hintAdapter.getCount());


        SpinnerAdapter arrayAdapter = new SpinnerAdapter(this, R.layout.spinner_item_others_details);
        arrayAdapter.addAll(listState);
        arrayAdapter.add("State");
        spinnerState.setDropDownWidth(1000);
        spinnerState.setAdapter(arrayAdapter);
        spinnerState.setSelection(arrayAdapter.getCount());


        if (compareValue != null && !compareValue.trim().equalsIgnoreCase("")) {

            if (!isState) {
                isState = true;
                int spinnerPosition = arrayAdapter.getPosition(compareValue);
                spinnerState.setSelection(spinnerPosition);
            }

        }


        itemSelectionFromStateSpinner();


    }

    private void itemSelectionFromStateSpinner() {

        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                holderState = (String) parent.getItemAtPosition(position);

                if (holderState.trim().equalsIgnoreCase("State")) {
                    mProgressDialog.dismiss();
                } else {

                    stateChecked = false;
                    for (com.smartit.jobSeeker.apiResponse.getState.Datum datumState : stateList) {

                        if (datumState.getName().equalsIgnoreCase(holderState)) {
                            stateId = datumState.getId();
                            tvCity.setVisibility(View.INVISIBLE);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();

    }

    private void getCountries() {


        HttpModule.provideRepositoryService().getCountry(Hawk.get("GET_TOKEN")).enqueue(new Callback<Country>() {
            @Override
            public void onResponse(Call<Country> call, Response<Country> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    countryList.addAll(response.body().getData());
                    getCountries(response.body().getData());


                } else {

                    System.out.println("ActivityRegister.accept " + Objects.requireNonNull(response.body()).getMessage());

                }
            }

            @Override
            public void onFailure(Call<Country> call, Throwable t) {

            }
        });

    }

    private void getCountries(List<com.smartit.jobSeeker.apiResponse.getCountry.Datum> data) {


        ArrayList<String> countriesListName = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            countriesListName.add(data.get(i).getName());
            countryIdList.add(String.valueOf(data.get(i).getId()));


        }

        if (!editClickCheck) {
            countriesListName.add("Country");

        }

        String compareValue = matchCountry;

        HintAdapter hintAdapter = new HintAdapter(this, R.layout.spinner_item_others_details, countriesListName);
        spinnerCountry.setDropDownWidth(1000);
        spinnerCountry.setAdapter(hintAdapter);
        spinnerCountry.setSelection(countriesListName.indexOf(0));


        if (editClickCheck) {

            if (compareValue != null && !compareValue.trim().equalsIgnoreCase("")) {

                int spinnerPosition = hintAdapter.getPosition(compareValue);
                spinnerCountry.setSelection(spinnerPosition);
            }

        }


        itemSelectionFromCountrySpinner();


    }

    private void itemSelectionFromCountrySpinner() {


        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


    private void getDegreeType() {


        HttpModule.provideRepositoryService().degreeType(Hawk.get("GET_TOKEN")).enqueue(new Callback<DegreeType>() {
            @Override
            public void onResponse(Call<DegreeType> call, Response<DegreeType> response) {

                if (response.body() != null && response.body().getError() == 0) {
                    setupDegreeType(response.body().getData());

                } else {
                    System.out.println("ActivityEditEducation.onResponse " + Objects.requireNonNull(response.body()).getMessage());
                }


            }

            @Override
            public void onFailure(Call<DegreeType> call, Throwable t) {
                System.out.println("ActivityEditEducation.onFailure " + t.toString());

            }
        });


    }

    private void setupDegreeType(List<Datum> data) {


        ArrayList<String> listDegreeType = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {

            listDegreeType.add(data.get(i).getTitle());
            listDegreeID.add(String.valueOf(data.get(i).getKey()));

        }

        if (!editClickCheck) {
            listDegreeType.add("Degree");

        }


        String compareValue = matchDegree;

        HintAdapter hintAdapter = new HintAdapter(this, R.layout.spinner_item_others_details, listDegreeType);
        spinnerDegree.setDropDownWidth(1000);
        spinnerDegree.setAdapter(hintAdapter);
        // show hint
        spinnerDegree.setSelection(hintAdapter.getCount());

        if (editClickCheck) {
            if (compareValue != null) {
                int spinnerPosition = hintAdapter.getPosition(compareValue);
                spinnerDegree.setSelection(spinnerPosition);

            }

        }


        itemSelectionFromDegreeTypeSpinner();


    }

    private void itemSelectionFromDegreeTypeSpinner() {


        spinnerDegree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void initViewsHere() {

        backArrowImage = findViewById(R.id.backArrowImage);
        tvState = findViewById(R.id.tvState);
        tvCity = findViewById(R.id.tvCity);

        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        spinnerDegree = findViewById(R.id.spinnerDegree);
        spinnerCountry = findViewById(R.id.spinnerCountry);
        spinnerState = findViewById(R.id.spinnerState);
        spinnerCity = findViewById(R.id.spinnerCity);

        edFeildOfStudy = findViewById(R.id.edFeildOfStudy);
        edInstitute = findViewById(R.id.edInstitute);
        edStartDate = findViewById(R.id.edStartDate);
        edEndDate = findViewById(R.id.edEndDate);
        edProgramDescription = findViewById(R.id.edProgramDescription);

        btnSubmit = findViewById(R.id.btnSubmit);

        spinnerState.setOnTouchListener(spinnerOnTouch);
        spinnerState.setOnKeyListener(spinnerOnKey);

        spinnerCity.setOnTouchListener(spinnerOnTouchCity);
        spinnerCity.setOnKeyListener(spinnerOnKeyCity);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mDegree = null;

                if (spinnerDegree != null && spinnerDegree.getSelectedItem() != null) {
                    mDegree = (String) spinnerDegree.getSelectedItem();

                    if (mDegree.equalsIgnoreCase("Degree")) {
                        Toast.makeText(ActivityEditEducation.this, "Please enter degree", Toast.LENGTH_LONG).show();
                    } else {

                        if (TextUtils.isEmpty(edFeildOfStudy.getText().toString().trim())) {
                            Toast.makeText(ActivityEditEducation.this, "Please enter field of study", Toast.LENGTH_LONG).show();

                        } else if (TextUtils.isEmpty(edInstitute.getText().toString().trim())) {
                            Toast.makeText(ActivityEditEducation.this, "Please enter institute", Toast.LENGTH_LONG).show();

                        } else if (spinnerCountry != null && spinnerCountry.getSelectedItem() != null) {

                            String mCountry = null;
                            mCountry = (String) spinnerCountry.getSelectedItem();
                            if (mCountry.equalsIgnoreCase("Country")) {
                                Toast.makeText(ActivityEditEducation.this, "Please enter Country", Toast.LENGTH_LONG).show();

                            } else {
                                if (spinnerState != null && spinnerState.getSelectedItem() != null) {
                                    String mState = null;
                                    mState = (String) spinnerState.getSelectedItem();
                                    if (mState.equalsIgnoreCase("State")) {
                                        Toast.makeText(ActivityEditEducation.this, "Please enter State", Toast.LENGTH_LONG).show();
                                    } else {
                                        if (spinnerCity != null && spinnerCity.getSelectedItem() != null) {
                                            String mCity = null;
                                            mCity = (String) spinnerCity.getSelectedItem();
                                            if (mCity.equalsIgnoreCase("City")) {
                                                Toast.makeText(ActivityEditEducation.this, "Please enter City", Toast.LENGTH_LONG).show();
                                            } else {

                                                if (TextUtils.isEmpty(edStartDate.getText().toString())) {
                                                    Toast.makeText(ActivityEditEducation.this, "Please enter Start date", Toast.LENGTH_LONG).show();

                                                } else if (TextUtils.isEmpty(edEndDate.getText().toString())) {
                                                    Toast.makeText(ActivityEditEducation.this, "Please enter End date", Toast.LENGTH_LONG).show();
                                                } else if (TextUtils.isEmpty(edProgramDescription.getText().toString().trim())) {

//                                                    edProgramDescription.setError("Please enter program description");
//                                                    edProgramDescription.requestFocus();
                                                    Toast.makeText(ActivityEditEducation.this, "Please enter program description", Toast.LENGTH_LONG).show();
                                                } else {


                                                    if (editClickCheck) {


                                                        loadingProgress();
                                                        updateEducationApiEditUpdate(mDegree, mCountry, mState, mCity, edFeildOfStudy.getText().toString(), edInstitute.getText().toString(), edStartDate.getText().toString(), edEndDate.getText().toString(), edProgramDescription.getText().toString());

                                                    } else {

                                                        loadingProgress();
                                                        updateEducationApiGoesHere(mDegree, mCountry, mState, mCity, edFeildOfStudy.getText().toString(), edInstitute.getText().toString(), edStartDate.getText().toString(), edEndDate.getText().toString(), edProgramDescription.getText().toString());

                                                    }
                                                }


                                            }
                                        }


                                    }

                                }

                            }

                        }


                    }

                }


            }
        });

        edStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate("start");
                // Get Current Date
             /*   final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityEditEducation.this, new DatePickerDialog.OnDateSetListener() {
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
                datePickerDialog.show();*/

            }
        });


        edEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isStartdate)
                    setDate("end");
                else
                    Toast.makeText(ActivityEditEducation.this, "Please select start date", Toast.LENGTH_SHORT).show();
               /* if (TextUtils.isEmpty(mStartDate)) {
                    edStartDate.setText(null);
                    edEndDate.setText(null);
                    Toast.makeText(ActivityEditEducation.this, "Please select the start date first", Toast.LENGTH_LONG).show();

                } else {

                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityEditEducation.this, new DatePickerDialog.OnDateSetListener() {
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

    private void updateEducationApiEditUpdate(String mDegree, String mCountry, String mState, String mCity, String fieldOfStudy, String institute, String startDate, String endDate, String programDescription) {

        HttpModule.provideRepositoryService().updateEditEducation(Hawk.get("GET_TOKEN"), mDegree, fieldOfStudy, startDate,
                endDate, institute, String.valueOf(countryId), String.valueOf(stateId), String.valueOf(cityId), programDescription, "1", id).enqueue(new Callback<UpdateEducation>() {
            @Override
            public void onResponse(Call<UpdateEducation> call, Response<UpdateEducation> response) {

                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();
                    finish();
                    Toast.makeText(ActivityEditEducation.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(ActivityEditEducation.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<UpdateEducation> call, Throwable t) {
                mProgressDialog.dismiss();
                System.out.println("ActivityEditEducation.onFailure " + t.toString());
            }
        });


    }

    private void loadingProgress() {

        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();


    }

    private void updateEducationApiGoesHere(String mDegree, String mCountry, String mState, String mCity, String fieldOfStudy, String institute, String startDate, String endDate, String programDescription) {


        HttpModule.provideRepositoryService().updateEducation(Hawk.get("GET_TOKEN"), mDegree, fieldOfStudy, startDate,
                endDate, institute, String.valueOf(countryId), String.valueOf(stateId), String.valueOf(cityId), programDescription, "0").enqueue(new Callback<UpdateEducation>() {
            @Override
            public void onResponse(Call<UpdateEducation> call, Response<UpdateEducation> response) {

                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();
                    finish();
                    Toast.makeText(ActivityEditEducation.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(ActivityEditEducation.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<UpdateEducation> call, Throwable t) {
                mProgressDialog.dismiss();
                System.out.println("ActivityEditEducation.onFailure " + t.toString());
            }
        });

    }

    private void dialogGreaterThan() {


        LayoutInflater li = LayoutInflater.from(ActivityEditEducation.this);
        View dialogView = li.inflate(R.layout.dialog_greater_than, null);

        initGreater(dialogView);

        alertDialogBuilder = new AlertDialog.Builder(ActivityEditEducation.this);
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


        LayoutInflater li = LayoutInflater.from(ActivityEditEducation.this);
        View dialogView = li.inflate(R.layout.dialog_cannot_be_equal, null);

        init(dialogView);

        alertDialogBuilder = new AlertDialog.Builder(ActivityEditEducation.this);
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
                    Toast.makeText(ActivityEditEducation.this, "Please enter the country first", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(ActivityEditEducation.this, "Please enter the state first", Toast.LENGTH_LONG).show();
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
