package com.smartit.jobSeeker.activities.link;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.smartit.jobSeeker.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;

public class LinkedinActivity {

    /****FILL THIS WITH YOUR INFORMATION*********/
//This is the public api key of our application
    private static final String API_KEY = "78bl1923wgdgmy";
    //This is the private api key of our application
    private static final String SECRET_KEY = "Hxhc1Qa2ah1p2r5M";
    //This is any string we want to use. This will be used for avoiding CSRF attacks. You can generate one here: http://strongpasswordgenerator.com/
    private static final String STATE = "123456789";
    //This is the url that LinkedIn Auth process will redirect to. We can put whatever we want that starts with http:// or https:// .
//We use a made up url that we will intercept when redirecting. Avoid Uppercases.
    private static final String REDIRECT_URI = "https://dev.jobma.com";
    /*********************************************/

//These are constants used for build the urls
    private static final String AUTHORIZATION_URL = "https://www.linkedin.com/oauth/v2/authorization";
    private static final String ACCESS_TOKEN_URL = "https://www.linkedin.com/uas/oauth2/accessToken";
    private static final String SECRET_KEY_PARAM = "client_secret";
    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String RESPONSE_TYPE_VALUE = "code";
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String STATE_PARAM = "state";
    private static final String REDIRECT_URI_PARAM = "redirect_uri";
    /*---------------------------------------*/
    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";

    private WebView webView;
    private ImageView close_icon;
    private ProgressDialog pd;

    //private OauthInterface oauthInterface;
    private String accessToken;
    private Context context;
    private Dialog dialog;

    public LinkedinActivity(@NonNull Context context) {
        this.context = context;
    }

    public void showLinkedin() {


        dialog = new Dialog(context, R.style.AppTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.linkedin_activity);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        //  pd = ProgressDialog.show(context, "", "Loading...", true);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    LinkedinData linkedinData = (LinkedinData) context;
                    linkedinData.linkedCancel();
                    dialog.dismiss();
                }
                return true;
            }
        });

        //oauthInterface = new OauthPresenter(this);

        //get the webView from the layout
        webView = (WebView) dialog.findViewById(R.id.activity_web_view);
        close_icon = (ImageView) dialog.findViewById(R.id.close_icon);
        close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkedinData linkedinData = (LinkedinData) context;
                linkedinData.linkedCancel();
                dialog.dismiss();
            }
        });


//        MARK  LOGOUT FROM LINKEDIN


        //Request focus for the webview

        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        webView.requestFocus(View.FOCUS_DOWN);
        webView.getSettings().setAppCacheEnabled(false);
        webView.clearCache(true);
        //Show a progress dialog to the user


        //Set a custom web view client
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                //This method will be executed each time a page finished loading.
                //The only we do is dismiss the progressDialog, in case we are showing any.
                if (pd != null) {
                    if (pd.isShowing())
                        pd.dismiss();
                }
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String authorizationUrl) {
                //This method will be called when the Auth proccess redirect to our RedirectUri.
                //We will check the url looking for our RedirectUri.

                if (authorizationUrl.startsWith(REDIRECT_URI)) {
                    Log.i("Authorize", "");
                    Uri uri = Uri.parse(authorizationUrl);
                    //We take from the url the authorizationToken and the state token. We have to check that the state token returned by the Service is the same we sent.
                    //If not, that means the request may be a result of CSRF and must be rejected.
                    String stateToken = uri.getQueryParameter(STATE_PARAM);

                    if (stateToken == null || !stateToken.equals(STATE)) {
                        Log.e("Authorize", "State token doesn't match");
                        return true;
                    }

                    //If the user doesn't allow authorization to our application, the authorizationToken Will be null.
                    String authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE);
                    if (authorizationToken == null) {
                        Log.i("Authorize", "The user doesn't allow authorization.");
                        return true;
                    }

                    Log.i("Authorize", "Auth token received: " + authorizationToken);

                    //Generate URL for requesting Access Token
                    String accessTokenUrl = getAccessTokenUrl(authorizationToken);
                    //We make the request in a AsyncTask
                    new PostRequestAsyncTask().execute(accessTokenUrl);

                } else {
                    //Default behaviour
                    Log.i("Authorize", "Redirecting to: " + authorizationUrl);
                    webView.loadUrl(authorizationUrl);
                }
                return true;
            }
        });

        //Get the authorization Url
        String authUrl = getAuthorizationUrl();
        Log.i("Authorize", "Loading Auth Url: " + authUrl);
        //Load the authorization URL into the webView
        webView.loadUrl(authUrl);
        dialog.show();

    }

    private static String getAccessTokenUrl(String authorizationToken) {
        return ACCESS_TOKEN_URL
                + QUESTION_MARK
                + GRANT_TYPE_PARAM + EQUALS + GRANT_TYPE
                + AMPERSAND
                + RESPONSE_TYPE_VALUE + EQUALS + authorizationToken
                + AMPERSAND
                + CLIENT_ID_PARAM + EQUALS + API_KEY
                + AMPERSAND
                + REDIRECT_URI_PARAM + EQUALS + REDIRECT_URI
                + AMPERSAND
                + SECRET_KEY_PARAM + EQUALS + SECRET_KEY;
    }

    /**
     * Method that generates the url for get the authorization token from the Service
     *
     * @return Url
     */
    private static String getAuthorizationUrl() {
        return AUTHORIZATION_URL
                + QUESTION_MARK + RESPONSE_TYPE_PARAM + EQUALS + RESPONSE_TYPE_VALUE
                + AMPERSAND + CLIENT_ID_PARAM + EQUALS + API_KEY
                + AMPERSAND + REDIRECT_URI_PARAM + EQUALS + REDIRECT_URI
                + AMPERSAND + STATE_PARAM + EQUALS + STATE
                + AMPERSAND + "scope=r_emailaddress,r_liteprofile";
    }

    private class PostRequestAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(context, "", "loading", true);
        }

        @Override
        protected String doInBackground(String... urls) {
            String responseString = "";
            if (urls.length > 0) {
                String url = urls[0];

                URL url1 = null;
                HttpURLConnection conn = null;
                try {
                    url1 = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    conn = (HttpURLConnection) url1.openConnection();
                    if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {


                        InputStream in = new BufferedInputStream(conn.getInputStream());
                        if (in != null) {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                            String line = "";

                            while ((line = bufferedReader.readLine()) != null)
                                responseString += line;
                        }
                        in.close();
                    } else {
                        responseString = "FAILED"; // See documentation for more info on response handling
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    conn.disconnect();
                }

                try {
                    //Convert the string result to a JSON Object
                    JSONObject resultJson = new JSONObject(responseString);
                    //Extract data from JSON Response
                    int expiresIn = resultJson.has("expires_in") ? resultJson.getInt("expires_in") : 0;

                    accessToken = resultJson.has("access_token") ? resultJson.getString("access_token") : null;
                    Log.e("Tokenm", "" + accessToken);
                    if (expiresIn > 0 && accessToken != null) {
                        Log.i("Authorize", "This is the access Token: " + accessToken + ". It will expires in " + expiresIn + " secs");

                        //Calculate date of expiration
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.SECOND, expiresIn);
                        long expireDate = calendar.getTimeInMillis();

                        ////Store both expires in and access token in shared preferences
                        SharedPreferences preferences = context.getSharedPreferences("user_info", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putLong("expires", expireDate);
                        editor.putString("accessToken", accessToken);
                        //oauthInterface.oauthAuthentication(accessToken, "linkedin", new HackedPrefence(getApplicationContext()).getDevice_token());
                        editor.apply();

                        return accessToken;


                    }
                } catch (JSONException e) {
                    Log.e("Authorize", "Error Parsing Http response " + e.getLocalizedMessage());
                }

            }
            return accessToken;
        }

        @Override
        protected void onPostExecute(String status) {
            if (pd != null) {
                if (pd.isShowing())
                    pd.dismiss();
            }

            LinkedinData linkedinData = (LinkedinData) context;
            linkedinData.LinkedinSuccess(status);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }

    }

    public interface LinkedinData {

        void linkedCancel();

        void LinkedinSuccess(String Token);
    }

}
