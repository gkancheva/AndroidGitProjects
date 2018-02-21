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
import com.gkancheva.tripmanager.model.user.User;
import com.gkancheva.tripmanager.repositories.UserManager;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private UserManager mUserManager;
    private User mUser;
    private EditText mTxtFirstName, mTxtLastName, mTxtUsername, mTxtPassword, mTxtRepeatPassword;
    private String mFirstName, mLastName, mUsername, mPassword, mRepeatPass;
    private Button mBtnRegister, mBtnBackToLogin;

    public RegisterFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_register, container, false);
        setViews(view);
        mBtnRegister.setOnClickListener(this);
        mBtnBackToLogin.setOnClickListener(this);
        return view;
    }

    private boolean validateEditTexts() {
        if(mTxtFirstName.getText() != null) {
            mFirstName = mTxtFirstName.getText().toString();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "First name should not be not null.", Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if(mTxtLastName.getText() != null) {
            mLastName = mTxtLastName.getText().toString();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Last name should not be not null.", Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if(mTxtUsername.getText() != null) {
            mUsername = mTxtUsername.getText().toString();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Username should not be not null.", Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if(mTxtPassword.getText().toString().equals(mTxtRepeatPassword.getText().toString()) && mTxtPassword.getText() != null) {
            mPassword = mTxtPassword.getText().toString();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Password does not match.", Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        return true;
    }

    private void setViews(View v) {
        mTxtFirstName = (EditText)v.findViewById(R.id.firstName);
        mTxtLastName = (EditText)v.findViewById(R.id.lastName);
        mTxtUsername = (EditText)v.findViewById(R.id.reg_username);
        mTxtPassword = (EditText)v.findViewById(R.id.reg_password);
        mTxtRepeatPassword = (EditText)v.findViewById(R.id.reg_repeat_password);
        mBtnRegister = (Button)v.findViewById(R.id.btn_reg_register);
        mBtnBackToLogin = (Button)v.findViewById(R.id.btn_back_to_login);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_reg_register) {
            if(validateEditTexts()) {
                mUserManager = new UserManager(getActivity().getApplicationContext());
                mUser = new User(mFirstName, mLastName, mPassword, mUsername);
                boolean isRegistered = mUserManager.registerUser(mUser);
                if(isRegistered) {
                    Toast.makeText(getActivity().getApplicationContext(), "Register successful!", Toast.LENGTH_LONG)
                            .show();
                    ((MainActivity)getActivity()).onUserRegistered();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Failed to register!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }
        if(v.getId() == R.id.btn_back_to_login) {
            ((MainActivity)getActivity()).showLogin();
        }
    }
}
