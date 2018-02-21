package com.gkancheva.tripmanager.controller.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gkancheva.tripmanager.MainActivity;
import com.gkancheva.tripmanager.R;
import com.gkancheva.tripmanager.repositories.UserManager;

public class LoginFragment extends Fragment implements View.OnClickListener{

    UserManager mUserManager;
    private Button mBtnLogin, mBtnRegister;
    private EditText mTxtUsername, mTxtPassword;
    private String mUsername, mPassword;

    public LoginFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_login, container, false);
        setViews(view);
        mUserManager = new UserManager(getActivity().getBaseContext());
        mBtnLogin.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        return view;
    }

    private void setViews(View v) {
        mBtnLogin = (Button)v.findViewById(R.id.btn_login);
        mBtnRegister = (Button)v.findViewById(R.id.btn_register);
        mTxtUsername = (EditText)v.findViewById(R.id.username);
        mTxtPassword = (EditText)v.findViewById(R.id.password);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_login) {
            mUsername = mTxtUsername.getText().toString();
            mPassword = mTxtPassword.getText().toString();
            if(mUserManager.loginUser(mUsername, mPassword)){
                Toast.makeText(getActivity().getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                    ((MainActivity)getActivity()).onLoggedIn();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Login failed! Please try again or go to Register.", Toast.LENGTH_LONG)
                    .show();
            }
        }
        if(v.getId() == R.id.btn_register) {
            ((MainActivity)getActivity()).onRegisterClicked();
        }
    }
}
