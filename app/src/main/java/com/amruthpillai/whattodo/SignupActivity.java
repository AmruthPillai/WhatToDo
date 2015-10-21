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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    EditText _usernameText;
    EditText _passwordText;
    Button _signupButton;
    TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        _usernameText = (EditText) findViewById(R.id.input_username);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _signupButton = (Button) findViewById(R.id.btn_signup);
        _loginLink = (TextView) findViewById(R.id.link_login);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup(v);
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup(final View view) {
        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);
        view.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this, R.style.AppTheme_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(username);
        parseUser.setPassword(password);

        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(SignupActivity.this, TodoActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
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
                    }
                    view.setEnabled(true);
                }
            }
        });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onSignupSuccess();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
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

        if (password.isEmpty() || password.length() < 4 || password.length() > 16) {
            _passwordText.setError("between 4 and 16 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}