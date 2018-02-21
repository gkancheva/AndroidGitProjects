package com.notesapp.baas;

import com.notesapp.models.UserModel;
import com.notesapp.services.listeners.OnUserListener;

/**
 * Created by gery on 24Aug2017.
 */

public interface UserRepo {
    void signUp(UserModel user);
    void login(UserModel user);
    void setUserListener(OnUserListener userLoggedListener);
    void logout();

}
