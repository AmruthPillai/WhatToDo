package com.amruthpillai.whattodo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText _usernameText;
    EditText _passwordText;
    Button _loginButton;
    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _usernameText = (EditText) findViewById(R.id.input_username);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _loginButton = (Button) findViewById(R.id.btn_login);
        _signupLink = (TextView) findViewById(R.id.link_signup);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login(v);
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login(final View view) {
        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);
        view.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, TodoActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    switch (e.getCode()) {
                        case ParseException.USERNAME_TAKEN:
                            Toast.makeText(getApplicationContext(),
                                    "Sorry, this username has already been taken.",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            break;
                        case ParseException.USERNAME_MISSING:
                            Toast.makeText(getApplicationContext(),
                                    "Sorry, you must supply a username to register.",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            break;
                        case ParseException.PASSWORD_MISSING:
                            Toast.makeText(getApplicationContext(),
                                    "Sorry, you must supply a password to register.",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            break;
                        case ParseException.OBJECT_NOT_FOUND:
                            Toast.makeText(getApplicationContext(),
                                    "Sorry, those credentials were invalid.",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            break;
                    }
                    view.setEnabled(true);
                }
            }
        });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onLoginSuccess();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (username.isEmpty() || username.length() < 3) {
            _usernameText.setError("at least 3 characters");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
