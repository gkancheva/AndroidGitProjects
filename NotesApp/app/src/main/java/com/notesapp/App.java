package com.notesapp;

import android.app.Application;
import android.util.Log;

import com.kinvey.android.Client;
import com.kinvey.android.model.User;
import com.kinvey.android.store.UserStore;
import com.kinvey.java.core.KinveyClientCallback;
import com.notesapp.models.UserModel;
import com.notesapp.utils.Const;
import com.notesapp.utils.MessageParser;
import com.notesapp.utils.Session;

/**
 * Created by gery on 07Sep2017.
 */

public class App extends Application {

    private Client sharedClient;
    private static MessageParser messageParser;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedClient = new Client.Builder(Const.APP_KEY, Const.APP_SECRET, this)
                .setBaseUrl(Const.APP_URL)
                .build();
        UserStore.retrieve(sharedClient, new KinveyClientCallback<User>() {
            @Override
            public void onSuccess(User user) {
                String username = user.getUsername();
                UserModel userModel = new UserModel(username);
                Session.setActiveUser(userModel);
            }
            @Override
            public void onFailure(Throwable throwable) {
                Log.i("ERROR APP USER RETRIEVE", throwable.getMessage());
            }
        });
        messageParser = new MessageParser(this);
    }

    public Client getSharedClient(){
        return this.sharedClient;
    }

    public static MessageParser getMessageParser() {
        return messageParser;
    }
}
