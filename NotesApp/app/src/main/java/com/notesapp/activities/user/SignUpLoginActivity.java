package com.notesapp.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kinvey.android.Client;
import com.notesapp.App;
import com.notesapp.IO.Toaster;
import com.notesapp.R;
import com.notesapp.activities.notes.NotesActivity;
import com.notesapp.models.UserDto;
import com.notesapp.services.UserService;
import com.notesapp.services.UserServiceImpl;
import com.notesapp.services.listeners.OnUserListener;
import com.notesapp.utils.ProgrBar;

public class SignUpLoginActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnKeyListener, OnUserListener {
    private EditText etPassword, etConfirmPass, etMail;
    private TextView txtLinkLogin;
    private Button btnSignUpLogin;
    private ProgressBar progressBar;
    private ProgrBar progrBar;
    private UserService userService;
    private boolean signUpModeActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_login);
        setLayoutElements();
        Client client = ((App)getApplicationContext()).getSharedClient();
        this.userService = new UserServiceImpl(client, this);
        this.userService.setUserListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.linkLogin) {
            changeView();
        } else if(v.getId() == R.id.btnSignUpLogin) {
            executeSignUpOrLogin();
        } else {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            executeSignUpOrLogin();
        }
        return false;
    }

    @Override
    public void onUserSuccess(String message) {
        Toaster.toastShort(this, message);
        redirectToNotesActivity();
    }

    @Override
    public void onUserFailure(Throwable t, String message) {
        Log.i("ERROR", t.getMessage());
        Toaster.toastLong(this, message);
    }

    private void executeSignUpOrLogin() {
        try {
            UserDto userDto = new UserDto(etMail, etPassword, etConfirmPass);
            progrBar.start();
            if (signUpModeActive) {
                userService.signUp(userDto);
            } else {
                userService.login(userDto);
            }
        }catch (Exception e) {
            Toaster.toastShort(this, e.getMessage());
        }
    }

    private void changeView() {
        if(signUpModeActive) {
            signUpModeActive = false;
            btnSignUpLogin.setText(R.string.login);
            txtLinkLogin.setText(R.string.sign_up);
            etConfirmPass.setVisibility(View.INVISIBLE);
        } else {
            signUpModeActive = true;
            btnSignUpLogin.setText(R.string.sign_up);
            txtLinkLogin.setText(R.string.login);
            etConfirmPass.setVisibility(View.VISIBLE);
        }
    }

    private void redirectToNotesActivity() {
        Intent intent = new Intent(this , NotesActivity.class);
        startActivity(intent);
    }

    private void setLayoutElements() {
        etPassword = (EditText)findViewById(R.id.etPassword);
        etPassword.setOnKeyListener(this);
        etConfirmPass = (EditText)findViewById(R.id.etConfirmPass);
        etMail = (EditText)findViewById(R.id.etMail);
        btnSignUpLogin = (Button)findViewById(R.id.btnSignUpLogin);
        btnSignUpLogin.setOnClickListener(this);
        txtLinkLogin = (TextView)findViewById(R.id.linkLogin);
        txtLinkLogin.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        progrBar = new ProgrBar(progressBar);
    }
}
