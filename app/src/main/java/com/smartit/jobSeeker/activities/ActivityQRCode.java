package com.smartit.jobSeeker.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.orhanobut.hawk.Hawk;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.apiResponse.qrCode.QRCode;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityQRCode extends AppCompatActivity {


    private ImageView backArrowImage, imageQR;
    private TextView tvQRCode, tvQRT;
    private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        Hawk.init(this).build();
        noInternetDialog = new NoInternetDialog.Builder(this).build();

        findTheIdsHere();
        qrCodeApi();


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

    private void qrCodeApi() {

        HttpModule.provideRepositoryService().qrCodeApi(Hawk.get("GET_TOKEN")).enqueue(new Callback<QRCode>() {
            @Override
            public void onResponse(Call<QRCode> call, Response<QRCode> response) {

                if (response.body() != null && response.body().getError() == 0) {

                    tvQRCode.setVisibility(View.VISIBLE);
//                    Toast.makeText(ActivityQRCode.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                    if (response.body().getData().getQrcode() != null) {

                        if (response.body().getData().getQrcode().equalsIgnoreCase("")) {
                            Picasso.get()
                                    .load(R.drawable.no_qr_code)
                                    .placeholder(R.drawable.no_qr_code)
                                    .error(R.drawable.no_qr_code)
                                    .into(imageQR);
                        } else {

                            Picasso.get().load(response.body().getData().getQrcode()).into(imageQR);
                        }
                    } else {

                        Picasso.get()
                                .load(R.drawable.no_qr_code)
                                .placeholder(R.drawable.no_qr_code)
                                .error(R.drawable.no_qr_code)
                                .into(imageQR);
                    }


                } else {

                    tvQRCode.setVisibility(View.GONE);
                    Toast.makeText(ActivityQRCode.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<QRCode> call, Throwable t) {

                System.out.println("ActivityQRCode.onFailure " + t.getMessage());

            }
        });

    }


    private void findTheIdsHere() {

        backArrowImage = findViewById(R.id.backArrowImage);
        imageQR = findViewById(R.id.imageQR);
        tvQRCode = findViewById(R.id.tvQRCode);
        tvQRT = findViewById(R.id.tvQRT);
        tvQRT.setTypeface(tvQRT.getTypeface(), Typeface.BOLD);


        backArrowImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }


//    Picasso.get()
//            .load(url)
//    .placeholder(R.drawable.user_placeholder)
//    .error(R.drawable.user_placeholder_error)
//    .into(imageView);


}
