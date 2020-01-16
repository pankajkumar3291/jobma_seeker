package com.smartit.jobSeeker.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.crashlytics.android.Crashlytics;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.orhanobut.hawk.Hawk;
import com.sdsmdg.tastytoast.TastyToast;
import com.smartit.jobSeeker.BuildConfig;
import com.smartit.jobSeeker.R;
import com.smartit.jobSeeker.activities.link.LinkedinActivity;
import com.smartit.jobSeeker.apiResponse.forgotPassword.ForgotPassword;
import com.smartit.jobSeeker.apiResponse.signIn.SignIn;
import com.smartit.jobSeeker.apiResponse.socialLogin.SocialLoginFB;
import com.smartit.jobSeeker.httpRetrofit.HttpModule;
import com.smartit.jobSeeker.sharedPreferenceUtils.SharedPrefsHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import io.fabric.sdk.android.Fabric;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener, LinkedinActivity.LinkedinData {


    private static final int RC_SIGN_IN = 5556;
    private static final String TAG = ActivityLogin.class.getSimpleName();
    private TextView tvForgetPassword, tvRegisterHere;
    private EditText edEmailPhone, edPassword;
    private Button btnSignIn;
    private ImageView imageFacebook, imageLinkediin, imageGooodle;

    private ProgressDialog mProgressDialog;
    private NoInternetDialog noInternetDialog;


    // FOR FORGOT PASSWORD

    private EditText edEmailAddress;
    private Button btnSubmit;
    private ImageView imageCancel;
    private TextView tvFYP;


    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    private CallbackManager callbackManager;
    private static final String EMAIL = "email";

    private SharedPrefsHelper sharedPrefsHelper;
    private String fName, lName, email;

    private String fbFirstTimeToken, googleSigninToken, linkedinToken;

    public static String token;
    private GoogleSignInClient mGoogleSignInClient;

    private ProgressDialog progress;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    private String emailAddress, lastName, firstName;

    private TextView tvSuccessMessage;
    private Button buttonOk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Hawk.init(this).build();
        noInternetDialog = new NoInternetDialog.Builder(this).build();

        Fabric.with(this, new Crashlytics());

//        facebookInitialization();


        mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        findTheIdsFromHere();
        eventListnerGoesHere();


        tvForgetPassword.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvRegisterHere.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);


        TextInputLayout usernameTextObj = (TextInputLayout) findViewById(R.id.tilTwo);
        usernameTextObj.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));


        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    private void facebookInitialization() {
        FacebookSdk.sdkInitialize(ActivityLogin.this);
        AppEventsLogger.activateApp(this);
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

    private void eventListnerGoesHere() {
        tvForgetPassword.setOnClickListener(this);
        tvRegisterHere.setOnClickListener(this);
        imageFacebook.setOnClickListener(this);
        imageLinkediin.setOnClickListener(this);
        imageGooodle.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }

    private void findTheIdsFromHere() {
        tvForgetPassword = findViewById(R.id.tvForgetPassword);
        tvRegisterHere = findViewById(R.id.tvRegisterHere);
        edEmailPhone = findViewById(R.id.edEmailPhone);
        edPassword = findViewById(R.id.edPassword);
        imageFacebook = findViewById(R.id.imageFacebook);
        imageLinkediin = findViewById(R.id.imageLinkediin);
        imageGooodle = findViewById(R.id.imageGooodle);
        btnSignIn = findViewById(R.id.btnSignIn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvForgetPassword:
                forgotPasswordDialog();
                break;
            case R.id.tvRegisterHere:
                callTheRegisterActivityFromHere();
                break;
            case R.id.btnSignIn:
                if (TextUtils.isEmpty(edEmailPhone.getText().toString())) {
                    Toast.makeText(ActivityLogin.this, "Please enter your email", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(edPassword.getText().toString())) {
                    Toast.makeText(ActivityLogin.this, "Please enter your password", Toast.LENGTH_LONG).show();
                } else {
                    loadingProgress();
                    callTheLoginApiFromHere();
                }
                break;
            case R.id.imageFacebook:
                facebookResponseHandledHere();
                break;
            case R.id.imageLinkediin:
                new LinkedinActivity(this).showLinkedin();
                break;
            case R.id.imageGooodle:
                loginWithGoogle();
                break;
        }
    }

    private void loginWithLinkeIn() {

        HttpModule.provideRepositoryService().socialLoginForJobSeeker((String) Hawk.get("GET_TOKEN"), firstName, lastName, emailAddress).enqueue(new Callback<SocialLoginFB>() {
            @Override
            public void onResponse(Call<SocialLoginFB> call, Response<SocialLoginFB> response) {
                if (response.body() != null && response.body().getError() == 0) {
                    linkedinToken = response.body().getData().getToken();
                    Hawk.put("GET_TOKEN", response.body().getData().getToken());
                    Hawk.put("USER_EMAIL", emailAddress);
                    Intent intent = new Intent(ActivityLogin.this, ActivityDashboard.class);
                    intent.putExtra("token", linkedinToken);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<SocialLoginFB> call, Throwable t) {
                System.out.println("ActivityLogin.onFailure " + t.toString());
            }
        });

    }

    private void loginWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void facebookResponseHandledHere() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        if (LoginManager.getInstance() != null) {
            LoginManager.getInstance().logOut();
        }
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        getUserDetails(loginResult);
                        System.out.println("ActivityLogin.onSuccess " + loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("ActivityLogin.onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("ActivityLogin.onError " + exception);
                    }
                });
    }

    private void getUserDetails(LoginResult loginResult) {

        // NOTE:- YOU CAN USE PROFILE CLASS TO GET THE ALL DETAILS OF CURRENT USER

        // Profile.getCurrentProfile().getFirstName();

        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {

                        System.out.println("ActivityLogin.onCompleted " + response);
                        handlingFBResponse(response);

                    }

                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id, first_name, last_name, name, email, picture.width(120).height(120) ");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }

    private void handlingFBResponse(GraphResponse response) {

        JSONObject jso = response.getJSONObject();

        try {

            if (jso != null) {

                fName = jso.get("first_name").toString();
                lName = jso.get("last_name").toString();

                if (jso.has("email")) {
                    email = jso.get("email").toString();

                    HttpModule.provideRepositoryService().socialLoginForJobSeeker((String) Hawk.get("GET_TOKEN"), fName, lName, email).enqueue(new Callback<SocialLoginFB>() {
                        @Override
                        public void onResponse(Call<SocialLoginFB> call, Response<SocialLoginFB> response) {


                            if (response.body() != null && response.body().getError() == 0) {

                                fbFirstTimeToken = response.body().getData().getToken();
                                Hawk.put("GET_TOKEN", response.body().getData().getToken());


                                Hawk.put("USER_EMAIL", email);

                                Intent intent = new Intent(ActivityLogin.this, ActivityDashboard.class);
                                intent.putExtra("token", fbFirstTimeToken);
                                startActivity(intent);


                                System.out.println("ActivityLogin.onResponse ERROR 0");

                            } else if (Objects.requireNonNull(response.body()).getError() == 1) {

                                Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
                                startActivity(intent);

                                System.out.println("ActivityLogin.onResponse  ERROR 1 ");

                            }

                        }

                        @Override
                        public void onFailure(Call<SocialLoginFB> call, Throwable t) {

                            TastyToast.makeText(ActivityLogin.this, t.getMessage(), TastyToast.LENGTH_SHORT, 3).show();

                        }
                    });


                } else {

                    Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
                    startActivity(intent);
                }


            } else {

                System.out.println("ActivityLogin.handlingFBResponse " + jso);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void loadingProgress() {
        mProgressDialog.setMessage("Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }

    private void callTheLoginApiFromHere() {

        compositeDisposable.add(HttpModule.provideRepositoryService().signInForJobSeeker(edEmailPhone.getText().toString(), edPassword.getText().toString()).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<SignIn>() {

            @Override
            public void accept(final SignIn signIn) throws Exception {

                if (signIn != null && signIn.getError() == 0) {
                    mProgressDialog.dismiss();
                    Hawk.put("USER_EMAIL", edEmailPhone.getText().toString());
                    Hawk.put("GET_TOKEN", signIn.getData().getToken());
                    Hawk.put("USER_ID", signIn.getData().getId());
                    Hawk.put("LOGIN_SESSION", true);
                    token = signIn.getData().getToken();
                    Toast.makeText(ActivityLogin.this, signIn.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActivityLogin.this, ActivityDashboard.class);
                    intent.putExtra("token", token);
                    startActivity(intent);
                    finish();
                } else {
                    mProgressDialog.dismiss();
                    new AwesomeErrorDialog(ActivityLogin.this)
                            .setTitle("Oops")
                            .setMessage(Objects.requireNonNull(signIn).getMessage())
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
                System.out.println("ActivityLogin.accept" + throwable.toString());
                Toast.makeText(ActivityLogin.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }));

    }

    private void dialogSignOk(String message, String token) {
        LayoutInflater li = LayoutInflater.from(ActivityLogin.this);
        View dialogView = li.inflate(R.layout.dialog_feedback, null);
        findTheIdsHere(dialogView, message, token);
        alertDialogBuilder = new AlertDialog.Builder(ActivityLogin.this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void findTheIdsHere(View dialogView, String message, String token) {
        tvSuccessMessage = dialogView.findViewById(R.id.tvSuccessMessage);
        buttonOk = dialogView.findViewById(R.id.buttonOk);
        tvSuccessMessage.setText(message);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(ActivityLogin.this, ActivityDashboard.class);
                intent.putExtra("token", token);
                startActivity(intent);
                finish();
            }
        });

    }

    private void callTheRegisterActivityFromHere() {
        Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
        startActivity(intent);
    }

    private void forgotPasswordDialog() {
        LayoutInflater li = LayoutInflater.from(ActivityLogin.this);
        View dialogView = li.inflate(R.layout.dialog_forgot_password, null);
        findForgotPasswordIds(dialogView);
        alertDialogBuilder = new AlertDialog.Builder(ActivityLogin.this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }

    private void findForgotPasswordIds(View dialogView) {
        edEmailAddress = dialogView.findViewById(R.id.edEmailAddress);
        btnSubmit = dialogView.findViewById(R.id.btnSubmit);
        imageCancel = dialogView.findViewById(R.id.imageCancel);
        tvFYP = dialogView.findViewById(R.id.tvFYP);
        tvFYP.setTypeface(tvFYP.getTypeface(), Typeface.BOLD);
        imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailRegistration = edEmailAddress.getText().toString().trim();
                if (TextUtils.isEmpty(edEmailAddress.getText().toString())) {
                    Toast.makeText(ActivityLogin.this, "Please enter your  email address", Toast.LENGTH_LONG).show();
                } else if (!emailRegistration.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
                    Toast.makeText(ActivityLogin.this, "Please enter your valid email address", Toast.LENGTH_LONG).show();
                } else {
                    loadingProgress();
                    callTheForgetPasswordApiFromHere();
                }
            }
        });
    }

    private void callTheForgetPasswordApiFromHere() {
        compositeDisposable.add(HttpModule.provideRepositoryService().forgotPasswordForJobSeeker(token, edEmailAddress.getText().toString()).
                subscribeOn(io.reactivex.schedulers.Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<ForgotPassword>() {

                    @Override
                    public void accept(ForgotPassword forgotPassword) throws Exception {


                        if (forgotPassword != null && forgotPassword.getError() == 0) {


                            mProgressDialog.dismiss();
                            new AwesomeSuccessDialog(ActivityLogin.this)
                                    .setTitle("")
                                    .setMessage(forgotPassword.getMessage())
                                    .setColoredCircle(R.color.colorlogin)
                                    .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
                                    .setCancelable(false)
                                    .setPositiveButtonText("Ok")
                                    .setPositiveButtonbackgroundColor(R.color.colorlogin)
                                    .setPositiveButtonTextColor(R.color.white)
                                    .setPositiveButtonClick(new Closure() {
                                        @Override
                                        public void exec() {


                                            alertDialog.dismiss();


                                        }
                                    })
                                    .show();


                        } else {

                            mProgressDialog.dismiss();
                            new AwesomeErrorDialog(ActivityLogin.this)
                                    .setTitle("Oops")
                                    .setMessage(Objects.requireNonNull(forgotPassword).getMessage())
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
                        System.out.println("ActivityLogin.accept " + throwable.toString());
                        Toast.makeText(ActivityLogin.this, throwable.toString(), Toast.LENGTH_LONG).show();

                    }

                }));


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        } else if (requestCode == 3672) {
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
//            Toast.makeText(ActivityLogin.this, account.getEmail(), Toast.LENGTH_SHORT).show();     // Email address
//            Toast.makeText(ActivityLogin.this, account.getGivenName(), Toast.LENGTH_SHORT).show(); // First Name
//            Toast.makeText(ActivityLogin.this, account.getFamilyName(), Toast.LENGTH_SHORT).show(); // Last Name
//            Toast.makeText(ActivityLogin.this, account.getDisplayName(), Toast.LENGTH_SHORT).show(); // Full Name

            socialLoginForGoogle(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(ActivityLogin.class.getSimpleName(), "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void socialLoginForGoogle(GoogleSignInAccount account) {

        HttpModule.provideRepositoryService().socialLoginForJobSeeker((String) Hawk.get("GET_TOKEN"), account.getGivenName(), account.getFamilyName(), account.getEmail()).enqueue(new Callback<SocialLoginFB>() {
            @Override
            public void onResponse(Call<SocialLoginFB> call, Response<SocialLoginFB> response) {


                if (response.body() != null && response.body().getError() == 0) {


                    googleSigninToken = response.body().getData().getToken();
                    Hawk.put("GET_TOKEN", response.body().getData().getToken());

                    Hawk.put("USER_EMAIL", account.getEmail());

                    Intent intent = new Intent(ActivityLogin.this, ActivityDashboard.class);
                    intent.putExtra("token", googleSigninToken);
                    startActivity(intent);


                } else {

                    Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
                    startActivity(intent);


                }


            }

            @Override
            public void onFailure(Call<SocialLoginFB> call, Throwable t) {

            }
        });


    }


    @Override
    public void linkedCancel() {
    }

    @Override
    public void LinkedinSuccess(final String Token) {


        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);


        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, "https://api.linkedin.com/v2/me", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(final String response1) {
                Log.d(TAG, "onResponse: " + response1);


                //String Request initialized
                StringRequest mStringRequest = new StringRequest(Request.Method.GET, "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))", new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response + response1);
                        try {
                            JSONObject jsonObjectName = new JSONObject(response1);

                            lastName = jsonObjectName.optString("localizedLastName");
                            firstName = jsonObjectName.optString("localizedFirstName");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {


                            JSONObject jsonObjectEmail = new JSONObject(response);

                            JSONArray jsonArray = jsonObjectEmail.optJSONArray("elements");
                            JSONObject jsonObject = new JSONObject(jsonArray.get(0).toString());
                            JSONObject jsonObject1 = jsonObject.optJSONObject("handle~");
                            emailAddress = jsonObject1.optString("emailAddress");


//                            if (jsonObject1.has("emailAddress")) {
//
//                            } else {
//
//                            }

                            loginWithLinkeIn();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onResponse: " + error);

                    }
                }) {

                    /**
                     * Passing some request headers
                     * */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", "Bearer " + Token);
                        return headers;
                    }

                };

                mStringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                mRequestQueue.add(mStringRequest);


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onResponse: " + error);

            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + Token);
                return headers;
            }

        };
        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(mStringRequest);
    }


}
