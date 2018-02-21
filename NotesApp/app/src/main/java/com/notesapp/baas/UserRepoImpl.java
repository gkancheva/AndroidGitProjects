package com.notesapp.baas;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.kinvey.android.Client;
import com.kinvey.android.model.User;
import com.kinvey.android.store.UserStore;
import com.kinvey.java.core.KinveyClientCallback;
import com.notesapp.App;
import com.notesapp.R;
import com.notesapp.models.UserModel;
import com.notesapp.services.listeners.OnUserListener;
import com.notesapp.utils.Session;

import java.io.IOException;

/**
 * Created by gery on 24Aug2017.
 */

public class UserRepoImpl implements UserRepo {
    private Client client;
    private OnUserListener userListener;
    private SharedPreferences sharedPref;

    public UserRepoImpl(Client client, Context context) {
        this.client = client;
        sharedPref = context.getSharedPreferences(context.getString(R.string.collection_name), Context.MODE_PRIVATE);
    }

    public void setUserListener(OnUserListener userListener) {
        this.userListener = userListener;
    }

    @Override
    public void signUp(final UserModel user) {
        UserStore.signUp(user.getUsername(), user.getPassword(), this.client, new KinveyClientCallback<User>() {
                @Override
                public void onFailure(Throwable t) {
                    Log.i("ERROR USER SIGN UP", t.getMessage());
                    userListener.onUserFailure(t, App.getMessageParser().getMessage(R.string.sign_up_fail));
                }
                @Override
                public void onSuccess(User u) {
                    storeUserLocally(user);
                    userListener.onUserSuccess(App.getMessageParser().getMessage(R.string.user_created));
                }
            });
    }

    @Override
    public void login(final UserModel user) {
        try {
            UserStore.login(user.getUsername(), user.getPassword(), this.client, new KinveyClientCallback<User>() {
                @Override
                public void onFailure(Throwable t) {
                    Log.i("ERROR USER LOGIN", t.getMessage());
                    userListener.onUserFailure(t, App.getMessageParser().getMessage(R.string.login_fail));
                }
                @Override
                public void onSuccess(User u) {
                    storeUserLocally(user);
                    userListener.onUserSuccess(App.getMessageParser().getMessage(R.string.login_successful));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logout() {
        UserStore.logout(this.client, new KinveyClientCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Session.setActiveUser(null);
                userListener.onUserSuccess(App.getMessageParser().getMessage(R.string.logout_successful));
                removeUserFromLocalStorage();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("ERROR USER LOGOUT", t.getMessage());
                userListener.onUserFailure(t, App.getMessageParser().getMessage(R.string.logout_unsuccessful));
            }
        });
    }

    private void storeUserLocally(UserModel user) {
        this.sharedPref.edit().putString("id", user.getId()).apply();
        this.sharedPref.edit().putString("username", user.getUsername()).apply();
        this.sharedPref.edit().putString("password", user.getPassword()).apply();
        this.sharedPref.edit().putString("email", user.geteMail()).apply();
        Session.setActiveUser(user);
    }

    private void removeUserFromLocalStorage() {
        this.sharedPref.edit().clear().apply();
    }

}
