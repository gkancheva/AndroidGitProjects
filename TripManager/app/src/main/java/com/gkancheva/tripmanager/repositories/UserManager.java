package com.gkancheva.tripmanager.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.gkancheva.tripmanager.model.user.User;

import java.util.HashMap;

public class UserManager {
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mPrefEditor;
    private Context _context;
    private int PRIVATE_MODE = 0;
    public static final String PREF_NAME = "TripManager";
    public static final String LOGIN_KEY = "Login";
    public static final String USERNAME_KEY = "Username";
    public static final String PASSWORD_KEY = "Password";
    public static final String FIRST_NAME = "First_Name";
    public static final String LAST_NAME = "Last_Name";

    public UserManager(Context ctx) {
        this._context = ctx;
        this.mPreferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        this.mPrefEditor = mPreferences.edit();
    }

    public HashMap<String,String> getUserDetails() {
        HashMap<String, String> dictionary = new HashMap<>();
        dictionary.put(LOGIN_KEY, String.valueOf(mPreferences.getBoolean(LOGIN_KEY, false)));
        dictionary.put(USERNAME_KEY, String.valueOf(mPreferences.getString(USERNAME_KEY, null)));
        dictionary.put(PASSWORD_KEY, String.valueOf(mPreferences.getString(PASSWORD_KEY, null)));
        return dictionary;
    }

    public boolean isLoggedIn() {
        if(mPreferences != null) {
            return mPreferences.getBoolean(LOGIN_KEY, false);
        }
        return false;
    }

    public boolean registerUser(User user) {
        mPrefEditor.putString(USERNAME_KEY, user.getUsername());
        mPrefEditor.putString(PASSWORD_KEY, user.getPassword());
        mPrefEditor.putString(FIRST_NAME, user.getFirstName());
        mPrefEditor.putString(LAST_NAME, user.getLastName());
        return mPrefEditor.commit();
    }

    public boolean loginUser(String username, String password) {
        String currentUsername = "", currentPass = "";
        if(mPreferences.contains(USERNAME_KEY)) {
            currentUsername = mPreferences.getString(USERNAME_KEY, "");
            currentPass = mPreferences.getString(PASSWORD_KEY, "");
            if(currentUsername.equals(username) && currentPass.equals(password)) {
                return createSession(username, password, true);
            }
            return false;
        }
        return false;
    }

    public boolean logOut(){
        if(isLoggedIn()) {
            mPrefEditor.putBoolean(LOGIN_KEY, false);
            return mPrefEditor.commit();
        }
        return false;
    }

    private boolean createSession(String username, String password, boolean isLogged) {
        if(mPreferences != null && mPrefEditor != null){
            mPrefEditor.putString(USERNAME_KEY, username);
            mPrefEditor.putString(PASSWORD_KEY, password);
            mPrefEditor.putBoolean(LOGIN_KEY, isLogged);
            return mPrefEditor.commit();
        }
        return false;
    }
}
