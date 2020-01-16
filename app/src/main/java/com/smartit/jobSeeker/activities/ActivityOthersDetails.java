package com.smartit.jobSeeker.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.editOtherDetails.EditOthersDetails;
import com.smartit.jobSeeker.apiResponse.getContractType.ContractType;
import com.smartit.jobSeeker.apiResponse.getFunctionalArea.Datum;
import com.smartit.jobSeeker.apiResponse.getFunctionalArea.GetFunctionalArea;
import com.smartit.jobSeeker.apiResponse.industry.Industry;
import com.smartit.jobSeeker.apiResponse.visaStatus.VisaStatus;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.smartit.jobSeeker.multiSelectSpinnerWithLimit.MultipleSelectionSpinnerFunctionalArea;
import com.smartit.jobSeeker.multiSelectSpinnerWithLimit.MultipleSelectionSpinnerIndustries;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityOthersDetails extends AppCompatActivity {


    private ImageView backArrowImage;
    private MultipleSelectionSpinnerFunctionalArea spinnerFunctionalArea;
    private MultipleSelectionSpinnerIndustries spinnerIndustryArea;
    private Spinner spinnerVisaStatus, spinnerContract, spinnerOpenForContract;
    private Button btnSubmit;
    private TextView tvTitleOtherDet;

    private ArrayList<Datum> datumlist = new ArrayList<>();
    private ArrayList<com.smartit.jobSeeker.apiResponse.industry.Datum> industrylist = new ArrayList<>();


    private ArrayList<String> visaStatusKeysList = new ArrayList<>();
    private String mVisaStatusKeyHolder;


    private ArrayList<String> contractTypeKeyList = new ArrayList<>();
    private String mContractTypeKeyHolder;

    private ArrayList<String> listHolderForOpenContract = new ArrayList<>();
    private String mOpenForHolder;


    private ProgressDialog mProgressDialog;


    private String empStatus, desiredJobType, openForContract;

    private ArrayList<String> fun = new ArrayList<>();
    private ArrayList<String> ind = new ArrayList<>();

    private String hintVisa;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_details);
        Hawk.init(this).build();
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        loadingProgress();

        if (getIntent() != null && getIntent().hasExtra("EMPLOYMENT_STATUS")) {
            empStatus = getIntent().getStringExtra("EMPLOYMENT_STATUS");
        }

        if (getIntent() != null && getIntent().hasExtra("DESIRED_JOB_TYPE")) {
            desiredJobType = getIntent().getStringExtra("DESIRED_JOB_TYPE");

        }

        if (getIntent() != null && getIntent().hasExtra("OPEN_FOR_CONTRACT")) {
            openForContract = getIntent().getStringExtra("OPEN_FOR_CONTRACT");

        }


        if (getIntent() != null && getIntent().hasExtra("FUNCTION_ID")) {
            fun = (ArrayList<String>) getIntent().getSerializableExtra("FUNCTION_ID");
        }


        if (getIntent() != null && getIntent().hasExtra("INDUSTRY_ID")) {
            ind = (ArrayList<String>) getIntent().getSerializableExtra("INDUSTRY_ID");
        }

        initViewsHere();
        getFunctionalAreaApi();
        getIndustries();

        getVisaStatus();
        getContract();
        getOpenForContract();


    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void getOpenForContract() {


        ArrayList<String> listOpenForContract = new ArrayList<>();

        listOpenForContract.add("Yes");
        listOpenForContract.add("No");

        listHolderForOpenContract.add("1");
        listHolderForOpenContract.add("0");


        String compareValue = openForContract;

//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_others_details, listOpenForContract); // android.R.layout.simple_spinner_item
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerOpenForContract.setDropDownWidth(900);
//        spinnerOpenForContract.setAdapter(dataAdapter);

        SpinnerAdapter arrayAdapter = new SpinnerAdapter(this, R.layout.spinner_item_others_details);
        arrayAdapter.addAll(listOpenForContract);
        arrayAdapter.add("N/A");
        spinnerOpenForContract.setDropDownWidth(900);
        spinnerOpenForContract.setAdapter(arrayAdapter);
        spinnerOpenForContract.setSelection(arrayAdapter.getCount());


        if (compareValue != null && !compareValue.trim().equalsIgnoreCase("")) {

            int spinnerPosition = arrayAdapter.getPosition(compareValue);
            spinnerOpenForContract.setSelection(spinnerPosition);
        }


        itemSelectionOpenForContractSpinner();


    }

    private void itemSelectionOpenForContractSpinner() {


        spinnerOpenForContract.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String hintOpenForContract = (String) parent.getItemAtPosition(position);

                if (hintOpenForContract.trim().equalsIgnoreCase("N/A")) {
                    System.out.println("ActivityOthersDetails.onItemSelected " + hintOpenForContract);
                } else {
                    mOpenForHolder = listHolderForOpenContract.get(position);
                    Hawk.put("OPEN_FOR_HELPER", mOpenForHolder);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getContract() {

        HttpModule.provideRepositoryService().getContract(Hawk.get("GET_TOKEN")).enqueue(new Callback<ContractType>() {
            @Override
            public void onResponse(Call<ContractType> call, Response<ContractType> response) {

                if (response.body() != null && response.body().getError() == 0) {
                    mProgressDialog.dismiss();
                    setupContractType(response.body().getData());


                } else {
                    mProgressDialog.dismiss();
                    System.out.println("ActivityOthersDetails.onResponse" + Objects.requireNonNull(response.body()).getMessage());
                }


            }

            @Override
            public void onFailure(Call<ContractType> call, Throwable t) {
                mProgressDialog.dismiss();
                System.out.println("ActivityOthersDetails.onFailure " + t.toString());

            }
        });


    }

    private void setupContractType(List<com.smartit.jobSeeker.apiResponse.getContractType.Datum> data) {


        ArrayList<String> listContractType = new ArrayList<>();

        for (int x = 0; x < data.size(); x++) {

            listContractType.add(data.get(x).getTitle());
            contractTypeKeyList.add(String.valueOf(data.get(x).getTitle()));

        }

        String compareValue = desiredJobType;

//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_others_details, listContractType); // android.R.layout.simple_spinner_item
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerContract.setDropDownWidth(900);
//        spinnerContract.setAdapter(dataAdapter);


        SpinnerAdapter arrayAdapter = new SpinnerAdapter(this, R.layout.spinner_item_others_details);
        arrayAdapter.addAll(listContractType);
        arrayAdapter.add("N/A");
        spinnerContract.setDropDownWidth(900);
        spinnerContract.setAdapter(arrayAdapter);
        spinnerContract.setSelection(arrayAdapter.getCount());


        if (compareValue != null && !compareValue.trim().equalsIgnoreCase("")) {

            int spinnerPosition = arrayAdapter.getPosition(compareValue);
            spinnerContract.setSelection(spinnerPosition);
        }


        itemSelectionFromContractType();


    }

    private void itemSelectionFromContractType() {

        spinnerContract.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String hintContractType = (String) parent.getItemAtPosition(position);

                if (hintContractType.trim().equalsIgnoreCase("N/A")) {
                    System.out.println("ActivityOthersDetails.onItemSelected " + hintContractType);
                } else {
                    mContractTypeKeyHolder = contractTypeKeyList.get(position);
                    Hawk.put("CONTRACT_TYPE_KEY_HOLDER", mContractTypeKeyHolder);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getVisaStatus() {


        HttpModule.provideRepositoryService().getVisaStatus().enqueue(new Callback<VisaStatus>() {
            @Override
            public void onResponse(Call<VisaStatus> call, Response<VisaStatus> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();
                    setupVisaStatus(response.body().getData());

                } else {
                    mProgressDialog.dismiss();
                    System.out.println("ActivityOthersDetails.onResponse " + Objects.requireNonNull(response.body()).getMessage());

                }
            }

            @Override
            public void onFailure(Call<VisaStatus> call, Throwable t) {
                mProgressDialog.dismiss();
                System.out.println("ActivityOthersDetails.onFailure " + t.toString());
            }
        });


    }

    private void setupVisaStatus(List<com.smartit.jobSeeker.apiResponse.visaStatus.Datum> data) {


        ArrayList<String> listVisaStatus = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            listVisaStatus.add(data.get(i).getTitle());
            visaStatusKeysList.add(String.valueOf(data.get(i).getTitle()));
        }

        String compareValue = empStatus;

//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_others_details, listVisaStatus); // android.R.layout.simple_spinner_item
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerVisaStatus.setDropDownWidth(900);
//        spinnerVisaStatus.setAdapter(dataAdapter);


        SpinnerAdapter arrayAdapter = new SpinnerAdapter(this, R.layout.spinner_item_others_details);
        arrayAdapter.addAll(listVisaStatus);
        arrayAdapter.add("N/A");
        spinnerVisaStatus.setDropDownWidth(900);
        spinnerVisaStatus.setAdapter(arrayAdapter);
        spinnerVisaStatus.setSelection(arrayAdapter.getCount());


        if (compareValue != null && !compareValue.trim().equalsIgnoreCase("")) {

            int spinnerPosition = arrayAdapter.getPosition(compareValue);
            spinnerVisaStatus.setSelection(spinnerPosition);
        }


        itemSelectionFromVisaStatusSpinner();


    }

    private void itemSelectionFromVisaStatusSpinner() {

        spinnerVisaStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                hintVisa = (String) parent.getItemAtPosition(position);

                if (hintVisa.trim().equalsIgnoreCase("N/A")) {
                    System.out.println("ActivityOthersDetails.onItemSelected " + hintVisa);
                } else {
                    mVisaStatusKeyHolder = visaStatusKeysList.get(position);
                    Hawk.put("VISA_STATUS_HOLDER", mVisaStatusKeyHolder);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getIndustries() {


        HttpModule.provideRepositoryService().industryApi(Hawk.get("GET_TOKEN")).enqueue(new Callback<Industry>() {
            @Override
            public void onResponse(Call<Industry> call, Response<Industry> response) {

                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();
                    industrylist.addAll(response.body().getData());
                    setupIndustry(response.body().getData());
                } else {
                    mProgressDialog.dismiss();
                }


            }

            @Override
            public void onFailure(Call<Industry> call, Throwable t) {

                System.out.println("ActivityOthersDetails.onFailure" + t.toString());
                mProgressDialog.dismiss();
            }
        });
    }

    private void setupIndustry(List<com.smartit.jobSeeker.apiResponse.industry.Datum> data) {


        ArrayList<String> industryList = new ArrayList<>();

        for (com.smartit.jobSeeker.apiResponse.industry.Datum datum : industrylist) {

            industryList.add(datum.getName());
        }
        spinnerIndustryArea.setItems(industryList);


        // MARK DISPLAYING THE SELECTED VALUE

        String[] array = new String[ind.size()];

        for (int i = 0; i < ind.size(); i++) {
            array[i] = ind.get(i);
        }
        spinnerIndustryArea.setSelection(array);


    }


    // RETURNING IDS FOR INDUSTRY  IN ARRAY WILL GIVE WITH COMMA

    private ArrayList<Integer> getIndustryIds() {


        ArrayList<Integer> industryList = new ArrayList<>();

        LinkedList<String> selectedList = (LinkedList<String>) spinnerIndustryArea.getSelectedStrings();

        for (com.smartit.jobSeeker.apiResponse.industry.Datum datum : industrylist) {

            for (String str : selectedList) {
                if (datum.getName().equalsIgnoreCase(str)) {
                    industryList.add(datum.getId());
                }
            }
        }
        return industryList;
    }


    private void getFunctionalAreaApi() {


        HttpModule.provideRepositoryService().getFunctionalArea().enqueue(new Callback<GetFunctionalArea>() {
            @Override
            public void onResponse(Call<GetFunctionalArea> call, Response<GetFunctionalArea> response) {

                if (response.body() != null && response.body().getError() == 0) {

                    mProgressDialog.dismiss();
                    datumlist.addAll(response.body().getData());
                    setupFunctionalArea(response.body().getData());

                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(ActivityOthersDetails.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<GetFunctionalArea> call, Throwable t) {

                mProgressDialog.dismiss();
                System.out.println("ActivityOthersDetails.onFailure " + t.toString());

            }
        });


    }

    private void setupFunctionalArea(List<Datum> data) {


        ArrayList<String> industryList = new ArrayList<>();

        for (Datum datum : datumlist) {

            industryList.add(datum.getName());
        }
        spinnerFunctionalArea.setItems(industryList);


        // MARK DISPLAYING THE SELECTED VALUE

        String[] array = new String[fun.size()];

        for (int i = 0; i < fun.size(); i++) {
            array[i] = fun.get(i);
        }
        spinnerFunctionalArea.setSelection(array);


    }


    // RETURNING IDS FOR FUNCTIONAL AREA IN ARRAY WILL GIVE WITH COMMA

    private ArrayList<Integer> getFunctionalAreaIds() {


        ArrayList<Integer> functiuonalList = new ArrayList<>();

        LinkedList<String> selectedList = (LinkedList<String>) spinnerFunctionalArea.getSelectedStrings();

        for (Datum datum : datumlist) {

            for (String str : selectedList) {
                if (datum.getName().equalsIgnoreCase(str)) {
                    functiuonalList.add(datum.getId());
                }
            }
        }
        return functiuonalList;
    }


    private void initViewsHere() {

        backArrowImage = findViewById(R.id.backArrowImage);
        tvTitleOtherDet = findViewById(R.id.tvTitleOtherDet);
        tvTitleOtherDet.setTypeface(tvTitleOtherDet.getTypeface(), Typeface.BOLD);

        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();
            }
        });

        spinnerFunctionalArea = findViewById(R.id.spinnerFunctionalArea);
        spinnerIndustryArea = findViewById(R.id.spinnerIndustryArea);
        spinnerVisaStatus = findViewById(R.id.spinnerVisaStatus);
        spinnerContract = findViewById(R.id.spinnerContract);
        spinnerOpenForContract = findViewById(R.id.spinnerOpenForContract);

        btnSubmit = findViewById(R.id.btnSubmit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                loadingProgress();

                editOthersDetailsApi();

            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void loadingProgress() {

        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();


    }

    private void editOthersDetailsApi() {


        try {
            HttpModule.provideRepositoryService().editOthersDetails(Hawk.get("GET_TOKEN"), TextUtils.join(",", getFunctionalAreaIds()), TextUtils.join(",", getIndustryIds()), mVisaStatusKeyHolder, mContractTypeKeyHolder, mOpenForHolder).enqueue(new Callback<EditOthersDetails>() {
                @Override
                public void onResponse(Call<EditOthersDetails> call, Response<EditOthersDetails> response) {

                    if (response.body() != null && response.body().getError() == 0) {

                        mProgressDialog.dismiss();
                        Toast.makeText(ActivityOthersDetails.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                        finish();

                    } else {

                        mProgressDialog.dismiss();
                        Toast.makeText(ActivityOthersDetails.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println("ActivityOthersDetails.onResponse " + Objects.requireNonNull(response.body()).getMessage());


                    }


                }

                @Override
                public void onFailure(Call<EditOthersDetails> call, Throwable t) {

                    mProgressDialog.dismiss();
                    System.out.println("ActivityOthersDetails.onFailure " + t.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ActivityOthersDetails.editOthersDetailsApi " + e);
        }


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
