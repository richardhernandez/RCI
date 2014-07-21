package com.example.rci.rci;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.TextView;

import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.*;
import com.facebook.model.GraphUser;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogInActivity extends Activity {
    private EditText email;
    private EditText password;
    private Button login;
    private String APP_ID;
    private Facebook fb;
    private String at = "@";
    //private LogInFragment logInFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_log_in);
        email = (EditText)findViewById(R.id.emailInputText);
        password = (EditText)findViewById(R.id.passwordInputText);
        login = (Button)findViewById(R.id.button1);
        //facebook.setVisibility(View.INVISIBLE);

        // start Facebook Login
        Session.openActiveSession(this, true, new Session.StatusCallback() {

            // callback when session changes state
            @Override
            public void call(Session session, SessionState state, Exception exception) {

                if(session.isOpened()){
                    // make request to the /me API
                    Request.newMeRequest(session, new Request.GraphUserCallback() {

                        // callback after Graph API response with user object
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            if (user != null) {
                                email.setText(user.getUsername());
                                Toast.makeText(getApplicationContext(), "Welcome " + user.getName(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).executeAsync();
                }
            }
        });
    }

    // Function to make login work
    public void login(View view) {
        new Access().execute(0);
    }

    public void register(View view) {
        new Access().execute(1);
    }

    private void launch() {
        if(isLoggedIn()) {
            Intent i = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(), "Log in with Facebook or your RCI credentials", Toast.LENGTH_SHORT).show();
        }

    }

    private void failure() {
        Log.e(this.getClass().getName(), "!!! Login or registration failed !!!");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    //Function to make facebook login work
    public void PullFacebookData(View view) {
        email.setText("gburdell@gatech.edu");
        password.setText("buzz");
        Toast.makeText(getApplicationContext(), "Click Log in to Proceed", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isLoggedIn() {
        Session session = Session.getActiveSession();
        return (session != null && session.isOpened());
    }

    private class Access extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... mode) {
            Map<String, String> values = new HashMap<String, String>(2);
            Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
            Matcher mat = pattern.matcher(email.getText().toString());
            if(mat.matches())
            {
                values.put("email", email.getText().toString());
                values.put("password", password.getText().toString());
            }
            else
            {

            }
            String url = "http://54.210.12.229/api/";
            HttpResponse response;
            Boolean success = false;
            switch (mode[0]) {
                case 0:
                    url += "login";
                    break;
                case 1:
                    url += "user";
                    break;
                default:
                    break;
            }

            try {
                response = HttpManager.makeRequest(url, values);
                int code = response.getStatusLine().getStatusCode();
                if (code == 200 || code == 201) {
                    success = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                launch();
            }
            else {
                failure();
            }
        }
    }
}
