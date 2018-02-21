package com.notesapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kinvey.android.Client;
import com.notesapp.App;
import com.notesapp.R;
import com.notesapp.activities.notes.NotesActivity;
import com.notesapp.activities.user.SignUpLoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Client client = ((App)getApplicationContext()).getSharedClient();
        if(client.getActiveUser() != null) {
            redirectToNotesActivity();
        } else {
            redirectToSignUpLoginActivity();
        }
        this.finish();
    }

    private void redirectToSignUpLoginActivity() {
        Intent intent = new Intent(this, SignUpLoginActivity.class);
        startActivity(intent);
    }

    private void redirectToNotesActivity() {
        Intent intent = new Intent(this , NotesActivity.class);
        startActivity(intent);
    }
}
