package com.notesapp.validators;

import android.widget.EditText;

import com.notesapp.App;
import com.notesapp.R;
import com.notesapp.models.UserDto;
import com.notesapp.models.UserModel;
import com.notesapp.utils.NoteAppException;

/**
 * Created by gery on 24Aug2017.
 */

public class UserValidator {
    private UserModel user;

    public UserValidator(UserDto userDto) {
        this.user = new UserModel();
        this.setConfirmPassword(userDto.getPassword(), userDto.getConfirmPass());
        this.setEmailAndUsername(userDto.getEmail());
    }

    public UserValidator(EditText email, EditText password) {
        this.user = new UserModel();
        this.setEmailAndUsername(email);
        this.setPassword(password);
    }

    public UserModel getUser() {
        return this.user;
    }

    private void setEmailAndUsername(EditText email) {
        if(email == null || email.getText().toString().isEmpty()) {
            throw new NoteAppException(App.getMessageParser().getMessage(R.string.email_required));
        }
        String mail = email.getText().toString();
        if(!mail.matches("\\b[a-zA-Z0-9_\\-.+%]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\\b")) {
            throw new NoteAppException(App.getMessageParser().getMessage(R.string.invalid_email_address));
        }
        this.user.setUsername(mail.substring(0, mail.indexOf("@")));
        this.user.setMail(mail);
    }

    private void setPassword(EditText password) {
        if(password == null || password.getText().toString().isEmpty()) {
            throw new NoteAppException(App.getMessageParser().getMessage(R.string.pass_required));
        }
        this.user.setPassword(password.getText().toString());
    }

    private void setConfirmPassword(EditText pass, EditText confPass) {
        this.setPassword(pass);
        if(confPass == null || confPass.getText().toString().isEmpty()) {
            throw new NoteAppException(App.getMessageParser().getMessage(R.string.confirm_pass_required));
        }
        if(!confPass.getText().toString().equals(pass.getText().toString())) {
            throw new NoteAppException(App.getMessageParser().getMessage(R.string.pass_does_not_match));
        }
        this.user.setPassword(pass.getText().toString());
    }
}
