package com.smartit.jobSeeker.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.invitationRescheduled.InviationRescheduled;
import com.smartit.jobSeeker.apiResponse.timeZone.Datum;
import com.smartit.jobSeeker.apiResponse.timeZone.TimeZone;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityRescheduled extends AppCompatActivity implements View.OnClickListener {


    private TextView tvInterviewDate, tvSelectYourTimeZone, tvInterviewStartTime, tvInterviewEndTime;
    private EditText edMessage;
    private Button btnSubmit;
    private ImageView backarr;

    final Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;
    private String userSelected;
    private String systemDate;
    private int mHour, mMinute;
    private SpinnerDialog spinnerDialog;
    private String selectedKeys;

    private ArrayList<String> timeZoneKeyArray = new ArrayList<>();
    private boolean your_date_is_outdated;
    private String startTime;
    private String endTime;
    private String invitationDate;
    private String timeZoneNames;
    private NoInternetDialog noInternetDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescheduled);


        findTheIdsFromHere();
        eventListner();

        noInternetDialog = new NoInternetDialog.Builder(this).build();
        timeZoneSelectionApi();


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


    private void timeZoneSelectionApi() {


        HttpModule.provideRepositoryService().timeZoneForJobSeeker().enqueue(new Callback<TimeZone>() {
            @Override
            public void onResponse(Call<TimeZone> call, Response<TimeZone> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    selectTimeZone(response.body().getData());


                } else {
                    TastyToast.makeText(ActivityRescheduled.this, response.body().getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                }


            }

            @Override
            public void onFailure(Call<TimeZone> call, Throwable t) {

                TastyToast.makeText(ActivityRescheduled.this, t.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

            }


        });

    }

    private void selectTimeZone(List<Datum> data) {


        final ArrayList<String> timeZoneNamesArray = new ArrayList<>();
        timeZoneKeyArray.clear();

        for (int i = 0; i < data.size(); i++) {

            timeZoneNamesArray.add(data.get(i).getName());
            timeZoneKeyArray.add(data.get(i).getKey());

        }


        spinnerDialog = new SpinnerDialog(ActivityRescheduled.this, timeZoneNamesArray, "Select or Search Time Zone", "Close");// With No Animation
        spinnerDialog = new SpinnerDialog(ActivityRescheduled.this, timeZoneNamesArray, "Select or Search Time Zone", R.style.DialogAnimations_SmileWindow, "Close");// With 	Animation


        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                String timeZoneKeys = timeZoneKeyArray.get(position);
                timeZoneNames = timeZoneNamesArray.get(position);


                tvSelectYourTimeZone.setText(timeZoneNames);

                selectedKeys = timeZoneKeys;
                System.out.println("ActivityRescheduled.onClick KEYS " + selectedKeys);
                System.out.println("ActivityRescheduled.onClick NAMES " + timeZoneNames);


            }
        });


    }

    private void eventListner() {

        tvInterviewDate.setOnClickListener(this);
        tvSelectYourTimeZone.setOnClickListener(this);
        tvInterviewStartTime.setOnClickListener(this);
        tvInterviewEndTime.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);


    }

    private void findTheIdsFromHere() {


        tvInterviewDate = findViewById(R.id.tvInterviewDate);
        tvSelectYourTimeZone = findViewById(R.id.tvSelectYourTimeZone);
        tvInterviewStartTime = findViewById(R.id.tvInterviewStartTime);
        tvInterviewEndTime = findViewById(R.id.tvInterviewEndTime);
        edMessage = findViewById(R.id.edMessage);
        btnSubmit = findViewById(R.id.btnSubmit);
        backarr = findViewById(R.id.backarr);
        backarr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


    }

    private void updateLabel() {


        String myFormat = "yyyy-MM-dd";     //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        userSelected = sdf.format(myCalendar.getTime());
//        tvInterviewDate.setText(sdf.format(myCalendar.getTime()));

        System.out.println("ActivityRescheduled.updateLabel SELECTED DATE " + userSelected);
        systemDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = new Date();
        Date convertedDate2 = new Date();
        try {
            convertedDate = dateFormat.parse(systemDate);
            convertedDate2 = dateFormat.parse(userSelected);

            if (convertedDate2.after(convertedDate) || convertedDate2.equals(convertedDate)) {

//                tvInterviewDate.setText("true");

//                new AwesomeSuccessDialog(ActivityRescheduled.this)
//                        .setTitle("Awesome")
//                        .setMessage("Your date has been selected successfully")
//                        .setColoredCircle(R.color.colorlogin)
//                        .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
//                        .setCancelable(false)
//                        .setPositiveButtonText("Ok")
//                        .setPositiveButtonbackgroundColor(R.color.colorlogin)
//                        .setPositiveButtonTextColor(R.color.white)
//                        .setPositiveButtonClick(new Closure() {
//                            @Override
//                            public void exec() {
//
//
//                            }
//                        })
//                        .show();

                tvInterviewDate.setText(sdf.format(myCalendar.getTime()));

                invitationDate = sdf.format(myCalendar.getTime());


            } else {

                Toast.makeText(ActivityRescheduled.this, "You can not select the previous date", Toast.LENGTH_SHORT).show();

//                new AwesomeErrorDialog(ActivityRescheduled.this)
//                        .setTitle("Oops")
//                        .setMessage("You can not select the previous date")
//                        .setColoredCircle(R.color.dialogErrorBackgroundColor)
//                        .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
//                        .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
//                        .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
//                        .setButtonText(getString(R.string.dialog_ok_button))
//                        .setErrorButtonClick(new Closure() {
//                            @Override
//                            public void exec() {
//
//
//                            }
//                        })
//                        .show();
//                tvInterviewDate.setText("false");

                tvInterviewDate.setText("Interview Date");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("ActivityRescheduled.updateLabel " + e);
        }

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {


            case R.id.tvInterviewDate:

                pickingUpDate();

                break;

            case R.id.tvSelectYourTimeZone:

                timeZoneSelection();

                break;

            case R.id.tvInterviewStartTime:

                pickingUpStartTime();

                break;

            case R.id.tvInterviewEndTime:

                pickingUpEndTime();

                break;

            case R.id.btnSubmit:

                if (TextUtils.isEmpty(invitationDate)) {

                    Toast.makeText(ActivityRescheduled.this, "Date field is required", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(timeZoneNames)) {

                    Toast.makeText(ActivityRescheduled.this, "Time zone field is required", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(startTime)) {

                    Toast.makeText(ActivityRescheduled.this, "Start time is required", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(endTime)) {

                    Toast.makeText(ActivityRescheduled.this, "End time required ", Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(edMessage.getText().toString())) {

                    Toast.makeText(ActivityRescheduled.this, "Message field is required", Toast.LENGTH_LONG).show();
                } else if (!checktimings(startTime, endTime)) {

//                    new AwesomeErrorDialog(ActivityRescheduled.this)
//                            .setTitle("Oops")
//                            .setMessage("End time must be greater then Start time")
//                            .setColoredCircle(R.color.dialogErrorBackgroundColor)
//                            .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
//                            .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
//                            .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
//                            .setButtonText(getString(R.string.dialog_ok_button))
//                            .setErrorButtonClick(new Closure() {
//                                @Override
//                                public void exec() {
//
//
//                                }
//                            })
//                            .show();

                    Toast.makeText(ActivityRescheduled.this, "End time must be greater than Start time", Toast.LENGTH_SHORT).show();


                } else {

                    callTheRescheduledInvitationApi();
                }


                break;

        }

    }

    private boolean checktimings(String starttime, String endtime) {

        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(starttime);
            Date date2 = sdf.parse(endtime);

            if (date1.before(date2)) {
                return true;

            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void callTheRescheduledInvitationApi() {


        HttpModule.provideRepositoryService().invitationRescheduledForJobSeeker((String) Hawk.get("GET_TOKEN"), getIntent().getIntExtra("invitationId", 0), 2, invitationDate, startTime, endTime, selectedKeys, edMessage.getText().toString()).enqueue(new Callback<InviationRescheduled>() {
            @Override
            public void onResponse(Call<InviationRescheduled> call, Response<InviationRescheduled> response) {


                if (response.body() != null && response.body().getError() == 0) {

                    new AwesomeSuccessDialog(ActivityRescheduled.this)
                            .setTitle("Awesome")
                            .setMessage(response.body().getMessage())
                            .setColoredCircle(R.color.colorlogin)
                            .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
                            .setCancelable(false)
                            .setPositiveButtonText("Ok")
                            .setPositiveButtonbackgroundColor(R.color.colorlogin)
                            .setPositiveButtonTextColor(R.color.white)
                            .setPositiveButtonClick(new Closure() {
                                @Override
                                public void exec() {

                                    Intent intent = new Intent(ActivityRescheduled.this, ActivityDashboard.class);
                                    intent.putExtra("token", (String) Hawk.get("GET_TOKEN"));
                                    startActivity(intent);
                                    finish();


                                }
                            })
                            .show();


                } else {


                    new AwesomeErrorDialog(ActivityRescheduled.this)
                            .setTitle("Oops")
                            .setMessage(Objects.requireNonNull(response.body()).getMessage())
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

            @Override
            public void onFailure(Call<InviationRescheduled> call, Throwable t) {

                TastyToast.makeText(ActivityRescheduled.this, t.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                System.out.println("ActivityRescheduled.onFailure " + t.toString());

            }
        });


    }

    private void timeZoneSelection() {


        if (spinnerDialog != null) {
            spinnerDialog.showSpinerDialog();
        }


    }

    private void pickingUpEndTime() {

        mHour = myCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = myCalendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        tvInterviewEndTime.setText(hourOfDay + ":" + (minute < 10 ? "0" + minute : minute));
                        endTime = hourOfDay + ":" + minute;

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();


    }

    private void pickingUpStartTime() {


        // Get Current Time
//        final Calendar c = Calendar.getInstance();
        mHour = myCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = myCalendar.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                        tvInterviewStartTime.setText(hourOfDay + ":" + (minute < 10 ? "0" + minute : minute));

                        startTime = hourOfDay + ":" + minute;

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

    }

    private void pickingUpDate() {

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {


                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel();
            }

        };

        new DatePickerDialog(ActivityRescheduled.this, date, myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }


}
