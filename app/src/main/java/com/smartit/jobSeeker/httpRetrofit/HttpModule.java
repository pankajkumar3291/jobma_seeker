package com.smartit.jobSeeker.httpRetrofit;


import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.smartit.jobSeeker.App;
import com.smartit.jobSeeker.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpModule {

    private static OkHttpClient getOkkHttpClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.NONE : HttpLoggingInterceptor.Level.NONE);
        return new OkHttpClient.Builder().writeTimeout(3600, TimeUnit.SECONDS).connectTimeout(3600, TimeUnit.SECONDS).sslSocketFactory(createAdapter()).
                addInterceptor(logging).build();
    }

    public static Retrofit getRetroFitClient() {
        return new Retrofit.Builder()
                .baseUrl("https://dev.jobma.com:8090/v4/") // "https://www.jobma.com:9000/v4/"  live url
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                //.client(getUnsafeOkHttpClient().build())  // getOkkHttpClient() before
                .client(getOkkHttpClient())
                .build();

//        https://dev.jobma.com:8090/v4/  testing url
//        https://www.jobma.com:9000/v4/  live url

    }

    public static SSLSocketFactory createAdapter() {
        // loading CAs from an InputStream
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        InputStream cert = App.provideMeCertRes();
        Certificate ca = null;
        try {
            try {
                ca = cf.generateCertificate(cert);
            } catch (CertificateException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                cert.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // creating a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(keyStoreType);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            keyStore.load(null, null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        try {
            keyStore.setCertificateEntry("ca", ca);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        // creating a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = null;
        try {
            tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            tmf.init(keyStore);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        // creating an SSLSocketFactory that uses our TrustManager
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sslContext.init(null, tmf.getTrustManagers(), null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        // creating an OkHttpClient that uses our SSLSocketFactory
        return sslContext.getSocketFactory();


    }


    public static RemoteRepositoryService provideRepositoryService() {


//        OkHttpClient client = new OkHttpClient();
//
//        final Request request = new Request.Builder()
//                .url("https://dev.jobma.com:8090/v4/jobseeker/handshake-request")
//                .get()
//                .addHeader("Authorization", "TSP9RANfZdO0uVqqGyNBEeFLesMAVY1530530467")
//                .addHeader("Cache-Control", "no-cache")
//                .addHeader("Postman-Token", "01b743b4-f218-4266-9489-abb8c0806129")
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//
//                System.out.println("HttpModule.onFailure" + e);
//
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//
//                System.out.println("HttpModule.onResponse " + request);
//
//
//            }
//        });

        return getRetroFitClient().create(RemoteRepositoryService.class);
    }


    public static OkHttpClient.Builder getUnsafeOkHttpClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.NONE : HttpLoggingInterceptor.Level.NONE);

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(logging);

            builder.writeTimeout(10000, TimeUnit.SECONDS).connectTimeout(10000, TimeUnit.SECONDS);
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    System.out.println("HttpModule.verify " + session + "HOST NAME " + hostname);
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
//            System.out.println(e);
            throw new RuntimeException(e);
        }
    }


}













