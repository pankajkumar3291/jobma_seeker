package com.smartit.jobSeeker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.job_search.SearchJobRequest;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.smartit.jobSeeker.httpRetrofit.HttpModuleForJobSearch;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ActivitySearch extends AppCompatActivity {

    private Button btnSubmit;
    private EditText etskills, etLocation, etExperience;
    private HashMap<String, String> hashMap = new HashMap<>();
//    private SendSearchRequest sendSearchRequest;


    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private ImageView backArrowImage;
    private ProgressDialog mProgressDialog;

    private ImageView imageCancel, imageCancelSalary, imageCancelForAddress;
    private EditText edProfileSummary;
    private Button btnApply;
    private TextView tvJS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Hawk.init(ActivitySearch.this).build();
        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        findidhere();
    }


    private void findidhere() {


        btnSubmit = findViewById(R.id.button);
        etskills = findViewById(R.id.et_skills);
        etLocation = findViewById(R.id.et_location);
        etExperience = findViewById(R.id.et_experience);
        tvJS = findViewById(R.id.tvJS);
        tvJS.setTypeface(tvJS.getTypeface(), Typeface.BOLD);

        backArrowImage = findViewById(R.id.backArrowImage);

        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(etskills.getText().toString()) && TextUtils.isEmpty(etLocation.getText().toString()) && TextUtils.isEmpty(etExperience.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please Fill At least one field", Toast.LENGTH_SHORT).show();
                } else {

                    loadingProgress();
                    JobSearchApi();


                }
            }
        });


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


    private void JobSearchApi() {

//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        VolleyLog.DEBUG = true;
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://dev.jobma.com/api/v1/jobseeker/jobs-search", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> hashMap = new HashMap<>();
//                hashMap.put("skills", etskills.getText().toString());
//                hashMap.put("location", "");
//                hashMap.put("experience", "");
//
//                return hashMap;
//
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
//                headers.put("Authorization", "00BV8Sauj1NZIuDvuIb01P1qBOOjEJ1510294263");
//                return headers;
//            }
//        };
//
//
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        stringRequest.setShouldCache(false);
//
//        System.out.println("ActivitySearch.JobSearchApi " + stringRequest);
//        requestQueue.add(stringRequest);


        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("skills", etskills.getText().toString());
        jsonParams.put("location", etLocation.getText().toString());
        jsonParams.put("experience", etExperience.getText().toString());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());


        HttpModuleForJobSearch.provideRepositoryService().searchJobs(Hawk.get("GET_TOKEN"), body).enqueue(new Callback<SearchJobRequest>() {
            @Override
            public void onResponse(Call<SearchJobRequest> call, Response<SearchJobRequest> response) {

                if (response.body() != null) {

                    if (response.body().getError() == 0 && response.body().getData() != null) {

                        mProgressDialog.dismiss();


                        if (response.body().getData().size() > 0) {

                            Intent intent = new Intent(ActivitySearch.this, ActivitySwappingLikeTinder.class);
                            intent.putExtra("list", (Serializable) response.body().getData());
                            startActivity(intent);
                            finish();
                        } else {
                            noDataFoundDialog();
                        }


                    } else {
                        mProgressDialog.dismiss();
                        Toast.makeText(ActivitySearch.this, "There is no data", Toast.LENGTH_LONG).show();
                    }

                } else {
                    mProgressDialog.dismiss();
                    System.out.println("ActivitySearch.onResponse");
                }


            }

            @Override
            public void onFailure(Call<SearchJobRequest> call, Throwable t) {
                mProgressDialog.dismiss();
                System.out.println("ActivitySearch.onFailure " + t.toString());
                Toast.makeText(ActivitySearch.this, t.toString(), Toast.LENGTH_LONG).show();

            }
        });

    }

    private void noDataFoundDialog() {


        LayoutInflater li = LayoutInflater.from(ActivitySearch.this);
        View dialogView = li.inflate(R.layout.dialog_no_data_found, null);

        findIdsNoDataFound(dialogView);

        alertDialogBuilder = new AlertDialog.Builder(ActivitySearch.this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void findIdsNoDataFound(View dialogView) {


        imageCancel = dialogView.findViewById(R.id.imageCancel);
        edProfileSummary = dialogView.findViewById(R.id.edProfileSummary);
        btnApply = dialogView.findViewById(R.id.btnApply);

        imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });


        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });


    }


}
