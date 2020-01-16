package com.smartit.jobSeeker.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.google.android.material.textfield.TextInputLayout;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.getPhoneCode.Datum;
import com.smartit.jobSeeker.apiResponse.getPhoneCode.GetPhoneCode;
import com.smartit.jobSeeker.apiResponse.signUp.SignUp;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import am.appwise.components.ni.NoInternetDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityRegister extends AppCompatActivity implements View.OnClickListener {


    private EditText edFirstName, edLastName, edEmail, edConfirmEmail, edMobileNo, edPassword, edConfirmPassword;
    private CheckBox checkBoxOne, checkBoxTwo;
    private TextView tvCheckOne, tvCheckTwo, tvPhoneCode, tvTerms, tvPrivacy, tvSignUpT;
    private Button btnSubmit;
    private ImageView backArrowImage;

    private SpinnerDialog spinnerDialog;
    private String selectedZipcode;


    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ArrayList<String> countryPhoneCodeArray = new ArrayList<>();
    private NoInternetDialog noInternetDialog;
    private ProgressDialog mProgressDialog;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private TextView tvSuccessMessage;
    private Button buttonOk;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        findTheIdsFromHere();
        eventListner();
//        tvPhoneCode.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);


        TextInputLayout usernameTextObj = (TextInputLayout) findViewById(R.id.tillPass);
        usernameTextObj.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));


        TextInputLayout usernameTextObj11 = (TextInputLayout) findViewById(R.id.tllConPass);
        usernameTextObj11.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));


//        Spannable wordtoSpan = new SpannableString("I have read and agree to Terms and privacy.");
//
//        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 25, 42, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        tvCheckOne.setText(wordtoSpan);


        getPhoneCodeApiGoesHere();


    }


    private void getPhoneCodeApiGoesHere() {


        compositeDisposable.add(HttpModule.provideRepositoryService().getPhoneCodeForJobSeeker().
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<GetPhoneCode>() {

                    @Override
                    public void accept(GetPhoneCode getPhoneCode) throws Exception {


                        if (getPhoneCode != null && getPhoneCode.getError() == 0) {

                            phoneCodeSpinner(getPhoneCode.getData());


                        } else {

                            System.out.println("ActivityRegister.accept " + Objects.requireNonNull(getPhoneCode).getMessage());

                        }


                    }


                }, new Consumer<Throwable>() {

                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }


                }));


    }

    private void phoneCodeSpinner(List<Datum> data) {

        ArrayList<String> countryNamesArray = new ArrayList<>();
        countryPhoneCodeArray.clear();

        for (int i = 0; i < data.size(); i++) {

            countryNamesArray.add(data.get(i).getCountryName());
            countryPhoneCodeArray.add(data.get(i).getPhoneCode().toString());

        }


        spinnerDialog = new SpinnerDialog(ActivityRegister.this, (ArrayList<String>) countryNamesArray, "Select country", "Close");// With No Animation
        spinnerDialog = new SpinnerDialog(ActivityRegister.this, (ArrayList<String>) countryNamesArray, "Select country", R.style.DialogAnimations_SmileWindow, "Close");// With 	Animation


        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                String countryCode = countryPhoneCodeArray.get(position);
                tvPhoneCode.setText(countryCode);
                selectedZipcode = countryCode;


            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
        noInternetDialog.onDestroy();

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void eventListner() {

        btnSubmit.setOnClickListener(this);
        backArrowImage.setOnClickListener(this);
        tvPhoneCode.setOnClickListener(this);
        tvTerms.setOnClickListener(this);
        tvPrivacy.setOnClickListener(this);
    }

    private void findTheIdsFromHere() {

        edFirstName = findViewById(R.id.edFirstName);
        edLastName = findViewById(R.id.edLastName);
        edEmail = findViewById(R.id.edEmail);
        edConfirmEmail = findViewById(R.id.edConfirmEmail);
        edMobileNo = findViewById(R.id.edMobileNo);
        edPassword = findViewById(R.id.edPassword);
        edConfirmPassword = findViewById(R.id.edConfirmPassword);
        tvSignUpT = findViewById(R.id.tvSignUpT);
        tvSignUpT.setTypeface(tvSignUpT.getTypeface(), Typeface.BOLD);


        checkBoxOne = findViewById(R.id.checkBoxOne);
        checkBoxTwo = findViewById(R.id.checkBoxTwo);

        tvCheckOne = findViewById(R.id.tvCheckOne);
        tvCheckTwo = findViewById(R.id.tvCheckTwo);

        btnSubmit = findViewById(R.id.btnSubmit);
        backArrowImage = findViewById(R.id.backArrowImage);

        tvTerms = findViewById(R.id.tvTerms);
        tvPrivacy = findViewById(R.id.tvPrivacy);
        tvPhoneCode = findViewById(R.id.tvPhoneCode);

    }


    public static boolean isEditTextContainEmail(EditText argEditText) {

        try {
            Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher matcher = pattern.matcher(argEditText.getText());
            return matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {


            case R.id.btnSubmit:


                if (TextUtils.isEmpty(edFirstName.getText().toString().trim())) {
                    Toast.makeText(ActivityRegister.this, "Please enter your first name", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(edLastName.getText().toString().trim())) {
                    Toast.makeText(ActivityRegister.this, "Please enter your last name", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(edEmail.getText().toString().trim())) {
                    Toast.makeText(ActivityRegister.this, "Please enter your email", Toast.LENGTH_SHORT).show();

                } else if (!edEmail.getText().toString().matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
                    Toast.makeText(ActivityRegister.this, "Enter valid email address", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(edConfirmEmail.getText().toString().trim())) {
                    Toast.makeText(ActivityRegister.this, "Please confirm your email", Toast.LENGTH_SHORT).show();

                } else if (!edConfirmEmail.getText().toString().matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
                    Toast.makeText(ActivityRegister.this, "Enter valid email address", Toast.LENGTH_SHORT).show();

                } else if (!edEmail.getText().toString().matches(edConfirmEmail.getText().toString())) {

                    Toast.makeText(ActivityRegister.this, "Match your email please", Toast.LENGTH_LONG).show();


                } else if (TextUtils.isEmpty(edMobileNo.getText().toString().trim())) {

                    Toast.makeText(ActivityRegister.this, "Please enter your mobile no", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(edPassword.getText().toString().trim())) {
                    Toast.makeText(ActivityRegister.this, "Please enter your password", Toast.LENGTH_SHORT).show();

                } else if (edPassword.getText().toString().length() < 6) {

                    Toast.makeText(getApplicationContext(), "Minimum password should be 6 character", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(edConfirmPassword.getText().toString().trim())) {

                    Toast.makeText(ActivityRegister.this, "Please enter confirm password", Toast.LENGTH_SHORT).show();

                } else if (!edPassword.getText().toString().matches(edConfirmPassword.getText().toString())) {

                    Toast.makeText(ActivityRegister.this, "Match your password please", Toast.LENGTH_LONG).show();

                } else if (!checkBoxOne.isChecked()) {

                    Toast.makeText(ActivityRegister.this, "Please accept terms and privacy", Toast.LENGTH_LONG).show();

                } else if (!checkBoxTwo.isChecked()) {

                    Toast.makeText(ActivityRegister.this, "Please accept terms and privacy", Toast.LENGTH_LONG).show();
                } else {
                    loadingProgress();
                    callTheRegisterApiFromHere();

                }


                break;


            case R.id.backArrowImage:

                finish();

                break;


            case R.id.tvPhoneCode:

                try {

                    if (spinnerDialog != null) {
                        spinnerDialog.showSpinerDialog();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("ActivityRegister.onClick " + e);
                }

                break;


            case R.id.tvTerms:
                callingTermsURL();

                break;


            case R.id.tvPrivacy:
                callingPrivacyURL();
                break;


        }


    }

    private void loadingProgress() {

        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

    }

    private void callingPrivacyURL() {

        String PRIVACY_URL = "https://www.jobma.com/privacy";
        Uri uri_privacy = Uri.parse(PRIVACY_URL); // missing 'http://' will cause crashed
        Intent intent_privacy = new Intent(Intent.ACTION_VIEW, uri_privacy);
        startActivity(intent_privacy);


    }

    private void callingTermsURL() {

        String TERMS_URL = "https://www.jobma.com/terms";
        Uri uri_terms = Uri.parse(TERMS_URL); // missing 'http://' will cause crashed
        Intent intent_terms = new Intent(Intent.ACTION_VIEW, uri_terms);
        startActivity(intent_terms);

    }

    private void callTheRegisterApiFromHere() {


        compositeDisposable.add(HttpModule.provideRepositoryService().signUpForJobSeeker(edFirstName.getText().toString(),
                edLastName.getText().toString(), edEmail.getText().toString(), selectedZipcode, edMobileNo.getText().toString(),
                edPassword.getText().toString()).subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<SignUp>() {

                    @Override
                    public void accept(SignUp signUp) throws Exception {


                        if (signUp != null && signUp.getError() == 0) {

                            mProgressDialog.dismiss();
                            dialogSignUpMessage(signUp.getMessage());


//                            new AwesomeSuccessDialog(ActivityRegister.this)
//                                    .setTitle("Awesome")
//                                    .setMessage(signUp.getMessage())
//                                    .setColoredCircle(R.color.colorlogin)
//                                    .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
//                                    .setCancelable(false)
//                                    .setPositiveButtonText("Ok")
//                                    .setPositiveButtonbackgroundColor(R.color.colorlogin)
//                                    .setPositiveButtonTextColor(R.color.white)
//                                    .setPositiveButtonClick(new Closure() {
//                                        @Override
//                                        public void exec() {
//                                            ActivityRegister.this.finish();
//
//                                            Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
//                                            startActivity(intent);
//                                            finish();
//
//
//                                        }
//                                    })
//                                    .show();

                        } else {

                            mProgressDialog.dismiss();
                            new AwesomeErrorDialog(ActivityRegister.this)
                                    .setTitle("Oops")
                                    .setMessage(Objects.requireNonNull(signUp).getMessage())
                                    .setColoredCircle(R.color.dialogErrorBackgroundColor)
                                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                                    .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
                                    .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                                    .setButtonText(getString(R.string.dialog_ok_button))
                                    .setErrorButtonClick(new Closure() {
                                        @Override
                                        public void exec() {


                                        }
                                    })
                                    .show();

                        }


                    }

                }, new Consumer<Throwable>() {

                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mProgressDialog.dismiss();
                        System.out.println("ActivityRegister.accept" + throwable.toString());
                        Toast.makeText(ActivityRegister.this, throwable.getMessage(), Toast.LENGTH_LONG).show();


                    }

                }));


    }

    private void dialogSignUpMessage(String message) {


        LayoutInflater li = LayoutInflater.from(ActivityRegister.this);
        View dialogView = li.inflate(R.layout.dialog_register_message, null);

        findTheIdsHere(dialogView, message);

        alertDialogBuilder = new AlertDialog.Builder(ActivityRegister.this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void findTheIdsHere(View dialogView, String message) {

        tvSuccessMessage = dialogView.findViewById(R.id.tvSuccessMessage);
        buttonOk = dialogView.findViewById(R.id.buttonOk);
        tvSuccessMessage.setText(message);


        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
                startActivity(intent);
                finish();


            }
        });

    }


}
