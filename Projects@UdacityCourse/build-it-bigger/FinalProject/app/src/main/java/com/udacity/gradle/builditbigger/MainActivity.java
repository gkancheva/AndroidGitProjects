package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.company.jokedisplaylib.JokeDisplayActivity;
import com.company.jokeproviderlib.JokeProvider;


public class MainActivity extends AppCompatActivity
        implements EndpointAsyncTaskListener{

    private static final String JOKE_KEY = "JOKE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onJokeSuccess(String joke) {
        this.showJoke(joke);
    }

    public void getJokeFromGCE(View v) {
        new EndpointAsyncTask(this)
                .execute(new Pair<Context, String>(this, "GCE"));
    }

    public void tellJoke(View view) {
        String joke = JokeProvider.provideJoke();
        this.showJoke(joke);
    }

    private void showJoke(String joke) {
        Toast.makeText(this, joke, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, JokeDisplayActivity.class);
        intent.putExtra(JOKE_KEY, joke);
        startActivity(intent);
    }
}
