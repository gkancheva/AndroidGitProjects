package com.company.jokedisplaylib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class JokeDisplayActivity extends AppCompatActivity {

    private static final String JOKE_KEY = "JOKE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_display);
        TextView tvJoke = findViewById(R.id.tv_joke_text);
        if(getIntent().getStringExtra(JOKE_KEY) != null) {
           String joke = getIntent().getStringExtra(JOKE_KEY);
           tvJoke.setText(joke);
           return;
        }

        closeOnError();

    }

    private void closeOnError() {
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
        this.finish();
    }
}
