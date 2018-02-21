package com.notesapp.utils;

import com.notesapp.models.UserModel;

/**
 * Created by gery on 07Sep2017.
 */

public class Session {
    private static UserModel activeUser;

    protected Session() {
    }

    public static UserModel getActiveUser() {
        return activeUser;
    }

    public static void setActiveUser(UserModel user) {
        activeUser = user;
    }
}
