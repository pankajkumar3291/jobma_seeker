package com.smartit.jobSeeker.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.adapters.IndustryAdapter;
import com.smartit.jobSeeker.apiResponse.industry.Datum;
import com.smartit.jobSeeker.apiResponse.industry.Industry;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityIndustryType extends Activity {


    private RecyclerView mRecyclerView;
    private IndustryAdapter industryAdapter;

    private List<Integer> checkedId = new ArrayList<>();
    private List<Datum> list = new ArrayList<>();


    private Button btnOk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_industry_type);
        Hawk.init(this).build();

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        initViews();
        industryApi();


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void industryApi() {

        HttpModule.provideRepositoryService().industryApi(Hawk.get("GET_TOKEN")).enqueue(new Callback<Industry>() {
            @Override
            public void onResponse(Call<Industry> call, Response<Industry> response) {


                if (response.body() != null && response.body().getError() == 0) {


                    list.addAll(response.body().getData());
                    industryAdapter = new IndustryAdapter(ActivityIndustryType.this, list);
                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(ActivityIndustryType.this));
                    mRecyclerView.setAdapter(industryAdapter);


                } else {


                }

            }

            @Override
            public void onFailure(Call<Industry> call, Throwable t) {

            }
        });


    }


    private void initViews() {


        mRecyclerView = findViewById(R.id.mRecyclerView);

        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (Datum datum : list) {
                    if (datum.isIschecked()) {
                        checkedId.add(datum.getId());
                    }
                }


                Toast.makeText(ActivityIndustryType.this, "" + checkedId, Toast.LENGTH_LONG).show();

            }
        });

    }
}
