package com.example.rci.rci;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class LogInActivity extends Activity {
    private EditText email;
    private EditText password;
    private Button login;
    private ImageButton facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        email = (EditText)findViewById(R.id.emailInputText);
        password = (EditText)findViewById(R.id.passwordInputText);
        login = (Button)findViewById(R.id.button1);
        facebook = (ImageButton)findViewById(R.id.imageButton);
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
