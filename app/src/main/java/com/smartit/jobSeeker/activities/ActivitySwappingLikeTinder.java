package com.smartit.jobSeeker.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daprlabs.cardstack.SwipeDeck;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.adapters.AdapterKeywords;
import com.smartit.jobSeeker.apiResponse.notInterested.NotInterested;
import com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.job_search.SearchJobData;
import com.smartit.jobSeeker.httpRetrofit.HttpModuleForJobSearch;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ActivitySwappingLikeTinder extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private SwipeDeck cardStack;

    private SwipeDeckAdapter adapter;
    List<SearchJobData> searchJobData = new ArrayList<>();

    private int jobId;
    private ProgressDialog mProgressDialog;

    private TextView tvJobs, btnFilter, tvFinishedAllJobs;
    private RelativeLayout reBtns;


    @Override
    protected void onResume() {
        super.onResume();

        reBtns.setVisibility(View.VISIBLE);
        adapter = new SwipeDeckAdapter(searchJobData, this);
        cardStack.setAdapter(adapter);

        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {


                if (position == searchJobData.size() - 1) {


                    tvFinishedAllJobs.setVisibility(View.VISIBLE);
                    reBtns.setVisibility(View.GONE);

                }
                Log.i("MainActivity", "card was swiped left, position in adapter: " + position);
            }

            @Override
            public void cardSwipedRight(int position) {

                if (position == searchJobData.size() - 1) {

                    tvFinishedAllJobs.setVisibility(View.VISIBLE);
                    reBtns.setVisibility(View.GONE);
                }

                Log.i("MainActivity", "card was swiped right, position in adapter: " + position);
            }

            @Override
            public void cardsDepleted() {
                Log.i("MainActivity", "no more cards");
            }

            @Override
            public void cardActionDown() {
                Log.i(TAG, "cardActionDown");
            }

            @Override
            public void cardActionUp() {
                Log.i(TAG, "cardActionUp");
            }

        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swapping_like_tinder);

        Hawk.init(ActivitySwappingLikeTinder.this).build();

        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        tvJobs = findViewById(R.id.tvJobs);
        tvJobs.setTypeface(tvJobs.getTypeface(), Typeface.BOLD);

        btnFilter = findViewById(R.id.btnFilter);
        btnFilter.setTypeface(btnFilter.getTypeface(), Typeface.BOLD);

        tvFinishedAllJobs = findViewById(R.id.tvFinishedAllJobs);
        tvFinishedAllJobs.setTypeface(tvFinishedAllJobs.getTypeface(), Typeface.BOLD);

        reBtns = findViewById(R.id.reBtns);

        cardStack = findViewById(R.id.swipe_deck);
        cardStack.setHardwareAccelerationEnabled(true);


        if (getIntent() != null && getIntent().hasExtra("list")) {
            searchJobData.addAll((Collection<? extends SearchJobData>) getIntent().getSerializableExtra("list"));
            System.out.println("ActivitySwappingLikeTinder.onCreate " + searchJobData.size());

        }


        ImageView imageCancelLeft = findViewById(R.id.imageCancelLeft);

        imageCancelLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cardStack.swipeTopCardLeft(180);

//                loadingProgress();
//                HttpModuleForJobSearch.provideRepositoryService().notInterested(Hawk.get("GET_TOKEN"), String.valueOf(jobId)).enqueue(new Callback<NotInterested>() {
//                    @Override
//                    public void onResponse(Call<NotInterested> call, Response<NotInterested> response) {
//
//                        if (response.body() != null && response.body().getError() == 0) {
//
//                            mProgressDialog.dismiss();
//                            cardStack.swipeTopCardLeft(180);
//
//                        } else {
//                            mProgressDialog.dismiss();
//                            System.out.println("ActivitySwappingLikeTinder.onResponse " + response);
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<NotInterested> call, Throwable t) {
//
//                        mProgressDialog.dismiss();
//                        System.out.println("ActivitySwappingLikeTinder.onFailure " + t.toString());
//
//                    }
//                });


            }
        });


        ImageView imageAcceptRight = findViewById(R.id.imageAcceptRight);

        imageAcceptRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cardStack.swipeTopCardRight(180);
                Intent intent = new Intent(ActivitySwappingLikeTinder.this, ActivityInterviewNow.class);
                intent.putExtra("job_id", jobId);
                startActivity(intent);
                finish();

            }
        });


        LinearLayout linFilter = findViewById(R.id.linFilter);

        linFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ActivitySwappingLikeTinder.this, ActivityFilter.class);
                startActivityForResult(intent, 000);

            }
        });

        ImageView backArrowImage = findViewById(R.id.backArrowImage);

        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 000) {

            if (searchJobData.size() > 0) {
                searchJobData.clear();
                searchJobData = (List<SearchJobData>) data.getSerializableExtra("list");
            }


        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public class SwipeDeckAdapter extends BaseAdapter {

        private List<SearchJobData> data;
        private Context context;

        public SwipeDeckAdapter(List<SearchJobData> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = getLayoutInflater();
                v = inflater.inflate(R.layout.row_swapping_like_tinder, parent, false);
            }


            TextView tvJobTitle = (TextView) v.findViewById(R.id.tvJobTitle);
            TextView tvContract = v.findViewById(R.id.tvContract);
            TextView tvCompanyName = v.findViewById(R.id.tvCompanyName);
            TextView tvFunctionalArea = v.findViewById(R.id.tvFunctionalArea);
            TextView tvIndustry = v.findViewById(R.id.tvIndustry);
            TextView tvMinMaxExp = v.findViewById(R.id.tvMinMaxExp);
            TextView tvCity = v.findViewById(R.id.tvCity);
            TextView tvMinMaxSal = v.findViewById(R.id.tvMinMaxSal);
            TextView tvNoKeyFound = v.findViewById(R.id.tvNoKeyFound);

            ImageView imagefb = v.findViewById(R.id.imagefb);
            ImageView imageTw = v.findViewById(R.id.imageTw);
            ImageView imageLin = v.findViewById(R.id.imageLin);
            ConstraintLayout wholeConstCard = v.findViewById(R.id.wholeConstCard);
            ImageView circleImageView = v.findViewById(R.id.circleImageView);

            if (!data.get(position).getJobmaCatcherPhoto().equalsIgnoreCase("")) {
//                Picasso.get().load(data.get(position).getJobmaCatcherPhoto()).into(circleImageView);


                Picasso.get()
                        .load(data.get(position).getJobmaCatcherPhoto())
                        .placeholder(R.drawable.no_image_available)
                        .error(R.drawable.no_image_available)
                        .into(circleImageView);


            } else {

                Picasso.get()
                        .load(R.drawable.no_image_available)
                        .placeholder(R.drawable.no_image_available)
                        .error(R.drawable.no_image_available)
                        .into(circleImageView);
//                Picasso.get().load(R.drawable.no_image_available).into(circleImageView);
            }


            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ActivitySwappingLikeTinder.this, ActivityCompanyDetails.class);
                    intent.putExtra("JOB_DESCRIPTION", (Serializable) searchJobData);
                    intent.putExtra("CHECKED_FROM_WHERE_ITS_COMING", "0");
                    intent.putExtra("jobId", searchJobData.get(0).getId());
                    startActivity(intent);


                }
            });


            imagefb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(data.get(position).getShareUrl()))
                            .build();

                    ShareDialog.show(ActivitySwappingLikeTinder.this, content);


                }
            });


            imageTw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    shareTwitter(data.get(position).getShareUrl());
                }
            });

            imageLin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://www.linkedin.com/sharing/share-offsite/?url=" + data.get(position).getShareUrl()));
                    startActivity(i);

                }
            });


            jobId = data.get(position).getId();

            tvJobTitle.setText(data.get(position).getJobmaJobTitle());
            tvContract.setText("(" + data.get(position).getJobmaJobType() + ")");
            tvCompanyName.setText(data.get(position).getJobmaCatcherCompany());

            if (!data.get(position).getFunctionalArea().equalsIgnoreCase("")) {
                tvFunctionalArea.setText(data.get(position).getFunctionalArea());
            } else {
                tvFunctionalArea.setText("N/A");
            }
            if (!data.get(position).getIndustry().equalsIgnoreCase("")) {
                tvIndustry.setText(data.get(position).getIndustry());
            } else {
                tvIndustry.setText("N/A");
            }


            tvMinMaxExp.setText(data.get(position).getJobmaJobMinExp() + " to " + data.get(position).getJobmaJobMaxExp() + " years");
            tvCity.setText(data.get(position).getJobmaJobCity() + " , " + data.get(position).getJobmaJobState() + " " + data.get(position).getJobmaJobCountry());
            tvMinMaxSal.setText(data.get(position).getJobmaJobMinSalary() + " to " + data.get(position).getJobmaJobMaxSalary());

            String keywords = data.get(position).getJobmaJobKeywords();
            List<String> result = Arrays.asList(keywords.split("\\s*,\\s*"));

            System.out.println("SwipeDeckAdapter.getView " + result);

            if (!result.contains("")) {

                AdapterKeywords adapterKeywords = new AdapterKeywords(ActivitySwappingLikeTinder.this, result);
                RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(ActivitySwappingLikeTinder.this, LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(adapterKeywords);

            } else {

                tvNoKeyFound.setVisibility(View.VISIBLE);
                tvNoKeyFound.setTypeface(tvNoKeyFound.getTypeface(), Typeface.BOLD);
            }


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Layer type: ", Integer.toString(v.getLayerType()));
                    Log.i("Hwardware Accel type:", Integer.toString(View.LAYER_TYPE_HARDWARE));
//                    Intent i = new Intent(v.getContext(), BlankActivity.class);
//                    v.getContext().startActivity(i);
                }
            });

            return v;
        }
    }


    private void shareTwitter(String message) {

        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, "This is a Test.");
        tweetIntent.setType("text/plain");

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            startActivity(tweetIntent);
        } else {
            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_TEXT, message);
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode(message)));
            startActivity(i);
            Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }
    }

    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf(TAG, "UTF-8 should always be supported", e);
            return "";
        }
    }


}

