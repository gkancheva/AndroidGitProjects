package com.notesapp.models;

import android.widget.EditText;

/**
 * Created by gery on 07Sep2017.
 */

public class UserDto {
    private EditText password;
    private EditText confirmPass;
    private EditText email;

    public UserDto(EditText email, EditText password, EditText confirmPass) {
        this.password = password;
        this.confirmPass = confirmPass;
        this.email = email;
    }

    public EditText getPassword() {
        return password;
    }

    public EditText getConfirmPass() {
        return confirmPass;
    }

    public EditText getEmail() {
        return email;
    }
}
