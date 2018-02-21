package com.notesapp.services;

import android.content.Context;

import com.kinvey.android.Client;
import com.notesapp.baas.UserRepo;
import com.notesapp.baas.UserRepoImpl;
import com.notesapp.models.UserDto;
import com.notesapp.services.listeners.OnUserListener;
import com.notesapp.validators.UserValidator;

/**
 * Created by gery on 07Sep2017.
 */

public class UserServiceImpl implements UserService {
    private UserRepo userRepo;

    public UserServiceImpl(Client client, Context context) {
        this.userRepo = new UserRepoImpl(client, context);
    }

    @Override
    public void signUp(UserDto userDto) {
        UserValidator validator = new UserValidator(userDto);
        this.userRepo.signUp(validator.getUser());
    }

    @Override
    public void login(UserDto userDto) {
        UserValidator validator = new UserValidator(userDto.getEmail(), userDto.getPassword());
        this.userRepo.login(validator.getUser());
    }

    @Override
    public void setUserListener(OnUserListener userListener) {
        this.userRepo.setUserListener(userListener);
    }

    @Override
    public void logout() {
        this.userRepo.logout();
    }

}
