package com.gkancheva.guessthecelebrity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gkancheva.guessthecelebrity.models.Celebrity;
import com.gkancheva.guessthecelebrity.models.DownloadTask;
import com.gkancheva.guessthecelebrity.models.QuestionManager;
import com.gkancheva.guessthecelebrity.views.QuestionManagerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final String URL = "http://www.posh24.se/kandisar";
    private QuestionManagerView questionManagerView;
    private QuestionManager questionManager;

    public void guess(View view) {
        int tag = Integer.parseInt((String) view.getTag());
        Toast.makeText(this, questionManager.getResultString(tag),
                Toast.LENGTH_SHORT).show();
        questionManagerView.updatePointsTxtView(questionManager.getResult());
        generateQuestion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionManagerView = new QuestionManagerView(this);
        DownloadTask task = new DownloadTask();
        String result;
        try {
            result = task.execute(URL).get();
            String[] splitResult = result.split("<div class=\"sidebarContainer\">");
            List<Celebrity> celebrities = getCelebrities(splitResult);
            questionManager = new QuestionManager(celebrities);
            generateQuestion();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("Error", e.getMessage());
        }
    }

    private List<Celebrity> getCelebrities(String[] splitResult) {
        List<Celebrity> celebrities = new ArrayList<>();
        Pattern pattern = Pattern.compile("img src=\"(.*?)\" alt=\"(.*?)\"");
        Matcher matcher = pattern.matcher(splitResult[0]);
        while(matcher.find()) {
            String name = matcher.group(2);
            String photo = matcher.group(1);
            celebrities.add(new Celebrity(name, photo));
        }
        return celebrities;
    }

    private void generateQuestion() {
        List<String> options = questionManager.generateNewGuess();
        Bitmap bitmap = questionManager.getBitmapOfGeneratedQuestion();
        questionManagerView.updateButtons(options);
        questionManagerView.updateImage(bitmap);
    }
}