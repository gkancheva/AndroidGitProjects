package com.notesapp.services;

import com.notesapp.models.UserDto;
import com.notesapp.services.listeners.OnUserListener;

/**
 * Created by gery on 07Sep2017.
 */

public interface UserService {
    void signUp(UserDto user);
    void login(UserDto user);
    void setUserListener(OnUserListener userLoggedListener);
    void logout();
}
