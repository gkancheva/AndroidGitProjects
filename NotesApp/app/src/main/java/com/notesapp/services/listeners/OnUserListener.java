package com.notesapp.services.listeners;

/**
 * Created by gery on 07Sep2017.
 */

public interface OnUserListener {
    void onUserSuccess(String message);
    void onUserFailure(Throwable t, String message);
}
