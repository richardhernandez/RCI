package com.example.rci.rci;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
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


public class LogInActivity extends Activity {
    private EditText email;
    private EditText password;
    private Button login;
    private ImageButton facebook;
    private String APP_ID;
    private Facebook fb;
    //private LogInFragment logInFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_log_in);
        email = (EditText)findViewById(R.id.emailInputText);
        password = (EditText)findViewById(R.id.passwordInputText);
        login = (Button)findViewById(R.id.button1);
        facebook = (ImageButton)findViewById(R.id.imageButton);
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
                            email.setText(user.getUsername().toString());

                            Toast.makeText(getApplicationContext(), "Click Log in to Proceed", Toast.LENGTH_SHORT).show();
                        }
                    }).executeAsync();
                }
            }
        });
    }

    //Function to make login work
    public void login(View view) {
        if (email.getText().toString().equals("gburdell@gatech.edu") &&
                password.getText().toString().equals("buzz")) {
            Toast.makeText(getApplicationContext(), "Logging In...", Toast.LENGTH_SHORT).show();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("loggedIn", true);
            editor.commit();

            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
        }
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
}
