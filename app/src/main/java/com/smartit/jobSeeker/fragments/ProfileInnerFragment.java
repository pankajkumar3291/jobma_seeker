package com.smartit.jobSeeker.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.activities.ActivityChangePassword;
import com.smartit.jobSeeker.activities.ActivityOthersDetails;
import com.smartit.jobSeeker.apiResponse.currencyType.CurrencyType;
import com.smartit.jobSeeker.apiResponse.currencyType.Datum;
import com.smartit.jobSeeker.apiResponse.editAddressDetails.EditAddressDetails;
import com.smartit.jobSeeker.apiResponse.editSalaryDetails.EditSalaryDetails;
import com.smartit.jobSeeker.apiResponse.edit_profile_summary.EditProfileSummary;
import com.smartit.jobSeeker.apiResponse.getProfileDetail.GetProfileDetails;
import com.smartit.jobSeeker.apiResponse.salaryType.SalaryType;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileInnerFragment extends Fragment {


    private View mView;

    private TextView tvProfileSummary, tvReadMore, tvCTC, tvExpectedCTC,
            tvCurrentLocation, tvDesiredLocation, tvWillingToRelocate,
            tvFunctionalArea, tvIndustry, tvEmploymentStatus,
            tvDesiredJobType, tvOpenForContract;


    private TextView tvProSum, tvSalDetail, tvAddDet, tvOthDet;


    private ImageView imageEditProfileSummary, imageSalaryDetails, imageAddressDetails, imageOthresDetails;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private ImageView imageCancel, imageCancelSalary, imageCancelForAddress;
    private EditText edProfileSummary;
    private Button btnApply;
    private TextView tvHeadingProfileSum, tvSalaryDet;


    private EditText edCTC, edExpectedSalary;
    private Spinner spinnerCurrenyType, spinnerAnnual;
    private Button btnSubmit;

    private EditText edCurrentLocation, edDesiredLocation;
    private Spinner spinnerWillingToRelocate;
    private Button btnSubmitForAddress;
    private TextView tvAddressDet;

    private boolean isCheck = true;

    private ArrayList<String> funcList = new ArrayList<>();
    private ArrayList<String> funcListID = new ArrayList<>();
    private ArrayList<String> induList = new ArrayList<>();
    private ArrayList<String> induListID = new ArrayList<>();

    private String currencyType, salaryType;


    private ArrayList<String> currencykeyList = new ArrayList<>();
    private String CURRENCY_KEY;
    private String hintSalary, hintAnnual, hintWillingToRelocate;


    private ArrayList<String> salarykeyList = new ArrayList<>();
    private String SALARY_KEY;
    private String mWillingToRelocate;
    private String mRelocateType;
    private String mCurrentLocation, mDesiredLocation;


    private ArrayList<String> integers = new ArrayList<>();
    private String willingToRelocateValues;

    private String mEmploymentStatus, mDesiredJobType, mOpenForContract;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.profile_inner_fragment, container, false);

        Hawk.init(Objects.requireNonNull(getActivity())).build();
        initViewsHere(mView);
        return mView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(CalligraphyContextWrapper.wrap(context));

    }


    @Override
    public void onResume() {
        super.onResume();

        funcList.clear();
        induList.clear();

        funcListID.clear();
        induListID.clear();

        getProfileApi();

    }

    private void getProfileApi() {


        HttpModule.provideRepositoryService().getProfileDetailsApi(Hawk.get("GET_TOKEN")).enqueue(new Callback<GetProfileDetails>() {
            @Override
            public void onResponse(Call<GetProfileDetails> call, Response<GetProfileDetails> response) {

                if (response.body() != null && response.body().getError() == 0) {


                    if (TextUtils.isEmpty(response.body().getData().getSummary())) {
                        tvProfileSummary.setText("N/A");
                    } else {
                        tvProfileSummary.setText(response.body().getData().getSummary());
                    }

                    Hawk.put("PROFILE_SUMMARY", response.body().getData().getSummary());

                    currencyType = response.body().getData().getCurrencyType();
                    salaryType = response.body().getData().getPayRate();


                    if (TextUtils.isEmpty(response.body().getData().getCtc())) {
                        tvCTC.setText("N/A");
                    } else {
                        tvCTC.setText(response.body().getData().getCurrencyType() + " " + response.body().getData().getCtc() + " " + response.body().getData().getPayRate());
                    }

                    Hawk.put("CTC", response.body().getData().getCtc());


                    if (TextUtils.isEmpty(response.body().getData().getExpectedCtc())) {
                        tvExpectedCTC.setText("N/A");
                    } else {
                        tvExpectedCTC.setText(response.body().getData().getCurrencyType() + " " + response.body().getData().getExpectedCtc() + " " + response.body().getData().getPayRate());
                    }

                    Hawk.put("EXPECTED_CTC", response.body().getData().getExpectedCtc());

                    if (TextUtils.isEmpty(response.body().getData().getCurrentLocation())) {
                        tvCurrentLocation.setText("N/A");
                    } else {
                        if (response.body().getData().getCurrentLocation().trim().equalsIgnoreCase("")) {
                            tvCurrentLocation.setText("N/A");
                        } else {
                            tvCurrentLocation.setText(response.body().getData().getCurrentLocation());
                        }

                    }

                    mCurrentLocation = response.body().getData().getCurrentLocation();
                    Hawk.put("CURRENT_LOCATION", response.body().getData().getCurrentLocation());


                    if (TextUtils.isEmpty(response.body().getData().getExpectedLocation())) {
                        tvDesiredLocation.setText("N/A");
                    } else {
                        tvDesiredLocation.setText(response.body().getData().getExpectedLocation());
                    }


                    mDesiredLocation = response.body().getData().getExpectedLocation();
                    Hawk.put("DESIRED_LOCATION", response.body().getData().getExpectedLocation());

                    if (response.body().getData().getRelocate().equalsIgnoreCase("1")) {

                        tvWillingToRelocate.setText("Yes");
                        mWillingToRelocate = "Yes";

                    } else if (response.body().getData().getRelocate().equalsIgnoreCase("0")) {

                        tvWillingToRelocate.setText("No");
                        mWillingToRelocate = "No";
                    } else {


                        tvWillingToRelocate.setText("N/A");
//                        mWillingToRelocate = "No";

                    }


                    for (int x = 0; x < response.body().getData().getFunctional().size(); x++) {
                        funcList.add(response.body().getData().getFunctional().get(x).getValue());
                        funcListID.add(response.body().getData().getFunctional().get(x).getValue());

                    }
                    System.out.println("ProfileInnerFragment.onResponse " + funcListID.size());

                    String functionalArea = null;
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    functionalArea = TextUtils.join(",", funcList); // Don't use String.join in these condition
//                    }


                    if (TextUtils.isEmpty(functionalArea)) {
                        tvFunctionalArea.setText("N/A");
                    } else {
                        tvFunctionalArea.setText(functionalArea);
                    }


                    for (int y = 0; y < response.body().getData().getIndustry().size(); y++) {
                        induList.add(response.body().getData().getIndustry().get(y).getValue());
                        induListID.add(response.body().getData().getIndustry().get(y).getValue());
                    }
                    System.out.println("ProfileInnerFragment.onResponse " + induListID.size());

                    String industry = null;
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    industry = TextUtils.join(", ", induList);
//                    }


                    if (TextUtils.isEmpty(industry)) {
                        tvIndustry.setText("N/A");
                    } else {
                        tvIndustry.setText(industry);
                    }


                    if (TextUtils.isEmpty(response.body().getData().getEmpStatus())) {
                        tvEmploymentStatus.setText("N/A");
                    } else {
                        tvEmploymentStatus.setText(response.body().getData().getEmpStatus());
                    }

                    mEmploymentStatus = response.body().getData().getEmpStatus();


                    if (TextUtils.isEmpty(response.body().getData().getDesireJobtype())) {
                        tvDesiredJobType.setText("N/A");
                    } else {
                        tvDesiredJobType.setText(response.body().getData().getDesireJobtype());
                    }


                    mDesiredJobType = response.body().getData().getDesireJobtype();


                    if (response.body().getData().getContract().equalsIgnoreCase("1")) {
                        tvOpenForContract.setText("Yes");
                        mOpenForContract = "Yes";
                    } else if (response.body().getData().getContract().equalsIgnoreCase("0")) {
                        tvOpenForContract.setText("No");
                        mOpenForContract = "No";
                    } else {
                        tvOpenForContract.setText("N/A");
                    }

                    tvProfileSummary.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {

                            if (tvProfileSummary.getLineCount() > 0) {
                                tvReadMore.setVisibility(View.VISIBLE);
//                                tvReadMore.setTypeface(tvReadMore.getTypeface(), Typeface.BOLD);

                            } else {
                                System.out.println("ActivityVideoQuestionEnhance.onGlobalLayout");
                            }

                        }
                    });


                    tvReadMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            if (isCheck) {

                                tvProfileSummary.setMaxLines(500);

                                if (tvReadMore.getText().equals("READ MORE")) {
                                    tvReadMore.setText("READ LESS");
                                    tvReadMore.setTextColor(Color.parseColor("#e50000"));
                                    tvReadMore.setTypeface(tvReadMore.getTypeface(), Typeface.BOLD);
                                }

                                isCheck = false;
                            } else {
                                tvProfileSummary.setMaxLines(2);

                                if (tvReadMore.getText().equals("READ LESS")) {
                                    tvReadMore.setText("READ MORE");
                                    tvReadMore.setTextColor(Color.parseColor("#28c0da"));
                                    tvReadMore.setTypeface(tvReadMore.getTypeface(), Typeface.BOLD);
                                }
                                isCheck = true;
                            }


                        }
                    });

                } else {

                    Toast.makeText(getActivity(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<GetProfileDetails> call, Throwable t) {

                System.out.println("ProfileInnerFragment.onFailure");

            }
        });


    }

    private void initViewsHere(View mView) {

        tvProfileSummary = mView.findViewById(R.id.tvProfileSummary);
        tvProSum = mView.findViewById(R.id.tvProSum);
        tvProSum.setTypeface(tvProSum.getTypeface(), Typeface.BOLD);


        tvSalDetail = mView.findViewById(R.id.tvSalDetail);
        tvSalDetail.setTypeface(tvSalDetail.getTypeface(), Typeface.BOLD);


        tvAddDet = mView.findViewById(R.id.tvAddDet);
        tvAddDet.setTypeface(tvAddDet.getTypeface(), Typeface.BOLD);

        tvOthDet = mView.findViewById(R.id.tvOthDet);
        tvOthDet.setTypeface(tvOthDet.getTypeface(), Typeface.BOLD);


        tvReadMore = mView.findViewById(R.id.tvReadMore);
        tvCTC = mView.findViewById(R.id.tvCTC);
        tvExpectedCTC = mView.findViewById(R.id.tvExpectedCTC);
        tvCurrentLocation = mView.findViewById(R.id.tvCurrentLocation);
        tvDesiredLocation = mView.findViewById(R.id.tvDesiredLocation);
        tvWillingToRelocate = mView.findViewById(R.id.tvWillingToRelocate);
        tvFunctionalArea = mView.findViewById(R.id.tvFunctionalArea);
        tvIndustry = mView.findViewById(R.id.tvIndustry);
        tvEmploymentStatus = mView.findViewById(R.id.tvEmploymentStatus);
        tvDesiredJobType = mView.findViewById(R.id.tvDesiredJobType);
        tvOpenForContract = mView.findViewById(R.id.tvOpenForContract);


        imageEditProfileSummary = mView.findViewById(R.id.imageEditProfileSummary);
        imageSalaryDetails = mView.findViewById(R.id.imageSalaryDetails);
        imageAddressDetails = mView.findViewById(R.id.imageAddressDetails);
        imageOthresDetails = mView.findViewById(R.id.imageOthresDetails);


        imageEditProfileSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogProfileSummary();

            }
        });


        imageSalaryDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogSalaryDetail();

            }
        });


        imageAddressDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogAddressDetails();

            }
        });


        imageOthresDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ActivityOthersDetails.class);
                intent.putExtra("EMPLOYMENT_STATUS", mEmploymentStatus);
                intent.putExtra("DESIRED_JOB_TYPE", mDesiredJobType);
                intent.putExtra("OPEN_FOR_CONTRACT", mOpenForContract);
                intent.putExtra("FUNCTION_ID", funcListID);
                intent.putExtra("INDUSTRY_ID", induListID);

                Objects.requireNonNull(getActivity()).startActivity(intent);


            }
        });


    }

    private void dialogAddressDetails() {


        LayoutInflater li = LayoutInflater.from(getActivity());
        View dialogView = li.inflate(R.layout.dialog_address_details, null);

        findIdsForAddress(dialogView);

        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

    }

    private void findIdsForAddress(View dialogView) {

        edCurrentLocation = dialogView.findViewById(R.id.edCurrentLocation);
        edDesiredLocation = dialogView.findViewById(R.id.edDesiredLocation);
        spinnerWillingToRelocate = dialogView.findViewById(R.id.spinnerWillingToRelocate);
        imageCancelForAddress = dialogView.findViewById(R.id.imageCancelForAddress);
        btnSubmitForAddress = dialogView.findViewById(R.id.btnSubmitForAddress);

        tvAddressDet = dialogView.findViewById(R.id.tvAddressDet);
        tvAddressDet.setTypeface(tvAddressDet.getTypeface(), Typeface.BOLD);

        String currentLocation = Hawk.get("CURRENT_LOCATION");

        if (currentLocation.trim().equalsIgnoreCase("")) {
            edCurrentLocation.setText("N/A");
        } else {
            edCurrentLocation.setText(Hawk.get("CURRENT_LOCATION"));
        }

        if (Hawk.get("DESIRED_LOCATION").equals("")) {
            edDesiredLocation.setText("N/A");
        } else {
            edDesiredLocation.setText(Hawk.get("DESIRED_LOCATION"));
        }


        // SPINNER SELECTION FROM WILLING TO RELOCATE

        willingToRelocateDropDown();


        imageCancelForAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });


        btnSubmitForAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(edCurrentLocation.getText().toString().trim())) {
                    Toast.makeText(getActivity(), "Please enter the current location", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(edDesiredLocation.getText().toString().trim())) {
                    Toast.makeText(getActivity(), "Please enter the desired location", Toast.LENGTH_SHORT).show();
                } else {
                    alertDialog.dismiss();
                    editAddressDetailsApi();
                }


            }
        });

    }

    private void editAddressDetailsApi() {


        HttpModule.provideRepositoryService().editAddresssDetails(Hawk.get("GET_TOKEN"), edCurrentLocation.getText().toString(), edDesiredLocation.getText().toString(), willingToRelocateValues).enqueue(new Callback<EditAddressDetails>() {
            @Override
            public void onResponse(Call<EditAddressDetails> call, Response<EditAddressDetails> response) {

                if (response.body() != null && response.body().getError() == 0) {

                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    getProfileApi();
                } else {
                    Toast.makeText(getActivity(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<EditAddressDetails> call, Throwable t) {

                System.out.println("ProfileInnerFragment.onFailure " + t.toString());

            }
        });


    }

    private void willingToRelocateDropDown() {


        ArrayList<String> willingToRelocateStringArrayList = new ArrayList<>();

        willingToRelocateStringArrayList.add("Yes");
        willingToRelocateStringArrayList.add("No");

        integers.add("1");
        integers.add("0");


        String compareValueForSalary = mWillingToRelocate;

//        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, willingToRelocateStringArrayList);
//        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerWillingToRelocate.setDropDownWidth(700);
//        spinnerWillingToRelocate.setAdapter(dataAdapter1);


        SpinnerAdapter arrayAdapter = new SpinnerAdapter(getActivity(), R.layout.spinner_item);
        arrayAdapter.addAll(willingToRelocateStringArrayList);
        arrayAdapter.add("N/A");
        spinnerWillingToRelocate.setDropDownWidth(700);
        spinnerWillingToRelocate.setAdapter(arrayAdapter);
        spinnerWillingToRelocate.setSelection(arrayAdapter.getCount());


        if (compareValueForSalary != null) {

            int spinnerPosition = arrayAdapter.getPosition(compareValueForSalary);
            spinnerWillingToRelocate.setSelection(spinnerPosition);
        }


        itemSelectionFromWillingToRelocateSpinner();


    }

    private void itemSelectionFromWillingToRelocateSpinner() {


        spinnerWillingToRelocate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                hintWillingToRelocate = (String) parent.getItemAtPosition(position);

                if (hintWillingToRelocate.trim().equalsIgnoreCase("N/A")) {

                    System.out.println("ProfileInnerFragment.onItemSelected " + hintWillingToRelocate);

                } else {

                    willingToRelocateValues = integers.get(position);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void dialogSalaryDetail() {


        LayoutInflater li = LayoutInflater.from(getActivity());
        View dialogView = li.inflate(R.layout.dialog_salary_details, null);

        findIdsForSalary(dialogView);

        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

    }

    private void findIdsForSalary(View dialogView) {

        edCTC = dialogView.findViewById(R.id.edCTC);
        edExpectedSalary = dialogView.findViewById(R.id.edExpectedSalary);
        spinnerCurrenyType = dialogView.findViewById(R.id.spinnerCurrenyType);
        spinnerAnnual = dialogView.findViewById(R.id.spinnerAnnual);
        btnSubmit = dialogView.findViewById(R.id.btnSubmit);
        imageCancelSalary = dialogView.findViewById(R.id.imageCancelSalary);

        tvSalaryDet = dialogView.findViewById(R.id.tvSalaryDet);
        tvSalaryDet.setTypeface(tvSalaryDet.getTypeface(), Typeface.BOLD);


        if (Hawk.get("CTC").equals("")) {
            edCTC.setText("N/A");

        } else {
            edCTC.setText(Hawk.get("CTC"));
        }

        if (Hawk.get("EXPECTED_CTC").equals("")) {
            edExpectedSalary.setText("N/A");

        } else {
            edExpectedSalary.setText(Hawk.get("EXPECTED_CTC"));
        }


        showCurrenyType();
        showSalaryType();


        imageCancelSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edCTC.getText().toString().trim())) {
                    Toast.makeText(getActivity(), "Please enter ctc", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(edExpectedSalary.getText().toString().trim())) {
                    Toast.makeText(getActivity(), "Please enter expected salary", Toast.LENGTH_SHORT).show();
                } else if (spinnerCurrenyType != null && spinnerCurrenyType.getSelectedItem().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter currency type", Toast.LENGTH_SHORT).show();
                } else if (spinnerAnnual != null && spinnerAnnual.getSelectedItem().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter the required field", Toast.LENGTH_SHORT).show();
                } else {
                    alertDialog.dismiss();
                    editSalaryDetails();
                }


            }
        });

    }

    private void editSalaryDetails() {


        try {

            HttpModule.provideRepositoryService().editSalaryDetails(Hawk.get("GET_TOKEN"), SALARY_KEY, edCTC.getText().toString(), edExpectedSalary.getText().toString(), CURRENCY_KEY).enqueue(new Callback<EditSalaryDetails>() {
                @Override
                public void onResponse(Call<EditSalaryDetails> call, Response<EditSalaryDetails> response) {

                    if (response.body() != null && response.body().getError() == 0) {

                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        getProfileApi();

                    } else {
                        Toast.makeText(getActivity(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<EditSalaryDetails> call, Throwable t) {


                    System.out.println("ProfileInnerFragment.onFailure " + t.toString());


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ProfileInnerFragment.editSalaryDetails " + e);
        }


    }

    private void showSalaryType() {


        HttpModule.provideRepositoryService().salaryApi(Hawk.get("GET_TOKEN")).enqueue(new Callback<SalaryType>() {
            @Override
            public void onResponse(Call<SalaryType> call, Response<SalaryType> response) {

                if (response.body() != null && response.body().getError() == 0) {

                    salarySetup(response.body().getData());

                } else {

                    System.out.println("ProfileInnerFragment.onResponse");

                }

            }

            @Override
            public void onFailure(Call<SalaryType> call, Throwable t) {

            }
        });


    }

    private void salarySetup(List<com.smartit.jobSeeker.apiResponse.salaryType.Datum> data) {


        ArrayList<String> salaryList = new ArrayList<>();


        for (int i = 0; i < data.size(); i++) {
            salaryList.add(data.get(i).getKey());
            salarykeyList.add(String.valueOf(data.get(i).getKey()));
        }


        String compareValueForSalary = salaryType;

//        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, salaryList);
//        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerAnnual.setDropDownWidth(700);
//        spinnerAnnual.setAdapter(dataAdapter1);


        SpinnerAdapter arrayAdapter = new SpinnerAdapter(getActivity(), R.layout.spinner_item);
        arrayAdapter.addAll(salaryList);
        arrayAdapter.add("N/A");
        spinnerAnnual.setDropDownWidth(700);
        spinnerAnnual.setAdapter(arrayAdapter);
        spinnerAnnual.setSelection(arrayAdapter.getCount());


        if (compareValueForSalary != null && !compareValueForSalary.equalsIgnoreCase("")) {

            int spinnerPosition = arrayAdapter.getPosition(compareValueForSalary);
            spinnerAnnual.setSelection(spinnerPosition);
        }


        selectionOfSalaryItemFromSpinner();


    }

    private void selectionOfSalaryItemFromSpinner() {


        spinnerAnnual.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                hintAnnual = (String) parent.getItemAtPosition(position);

                if (hintAnnual.trim().equalsIgnoreCase("N/A")) {

                    System.out.println("ProfileInnerFragment.onItemSelected " + hintAnnual);

                } else {

                    SALARY_KEY = salarykeyList.get(position);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void showCurrenyType() {


        HttpModule.provideRepositoryService().currencyApi(Hawk.get("GET_TOKEN")).enqueue(new Callback<CurrencyType>() {
            @Override
            public void onResponse(Call<CurrencyType> call, Response<CurrencyType> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    currencySetup(response.body().getData());

                } else {

                    System.out.println("ProfileInnerFragment.onResponse");
                }

            }

            @Override
            public void onFailure(Call<CurrencyType> call, Throwable t) {

                System.out.println("ProfileInnerFragment.onFailure " + t.toString());
            }
        });


    }

    private void currencySetup(List<Datum> data) {


        ArrayList<String> stateArrList = new ArrayList<>();


        for (int i = 0; i < data.size(); i++) {
            stateArrList.add(data.get(i).getTitle());
            currencykeyList.add(String.valueOf(data.get(i).getKey()));
        }


        String compareValue = currencyType;
//
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, stateArrList); // android.R.layout.simple_spinner_item
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerCurrenyType.setDropDownWidth(700);
//        spinnerCurrenyType.setAdapter(dataAdapter);


        SpinnerAdapter arrayAdapter = new SpinnerAdapter(getActivity(), R.layout.spinner_item);
        arrayAdapter.addAll(stateArrList);
        arrayAdapter.add("N/A");
        spinnerCurrenyType.setDropDownWidth(700);
        spinnerCurrenyType.setAdapter(arrayAdapter);
        spinnerCurrenyType.setSelection(arrayAdapter.getCount());


        if (compareValue != null) {

            int spinnerPosition = arrayAdapter.getPosition(compareValue);
            spinnerCurrenyType.setSelection(spinnerPosition);
        }


        selectionOfCurrencyItemFromSpinner();


    }

    private void selectionOfCurrencyItemFromSpinner() {


        spinnerCurrenyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                hintSalary = (String) parent.getItemAtPosition(position);

                if (hintSalary.equalsIgnoreCase("N/A")) {

                    System.out.println("ProfileInnerFragment.onItemSelected " + hintSalary);
                } else {
                    CURRENCY_KEY = currencykeyList.get(position);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


    }

    private void dialogProfileSummary() {


        LayoutInflater li = LayoutInflater.from(getActivity());
        View dialogView = li.inflate(R.layout.dialog_profile_summary, null);

        findIdsForSummary(dialogView);

        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        alertDialog.show();


    }

    private void findIdsForSummary(View dialogView) {

        imageCancel = dialogView.findViewById(R.id.imageCancel);
        edProfileSummary = dialogView.findViewById(R.id.edProfileSummary);
        btnApply = dialogView.findViewById(R.id.btnApply);
        tvHeadingProfileSum = dialogView.findViewById(R.id.tvHeadingProfileSum);
        tvHeadingProfileSum.setTypeface(tvHeadingProfileSum.getTypeface(), Typeface.BOLD);

        edProfileSummary.setText(Hawk.get("PROFILE_SUMMARY"));


        imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });


        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(edProfileSummary.getText().toString().trim())) {
                    editProfileSummaryApi(edProfileSummary.getText().toString());
                } else {
                    Toast.makeText(getActivity(), "Please enter the summary", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void editProfileSummaryApi(String summary) {


        HttpModule.provideRepositoryService().editProfileSummaryApi(Hawk.get("GET_TOKEN"), summary).enqueue(new Callback<EditProfileSummary>() {
            @Override
            public void onResponse(Call<EditProfileSummary> call, Response<EditProfileSummary> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                    getProfileApi();

                } else {

                    alertDialog.dismiss();
                    Toast.makeText(getActivity(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<EditProfileSummary> call, Throwable t) {
                System.out.println("ProfileInnerFragment.onFailure " + t.toString());
                alertDialog.dismiss();

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
