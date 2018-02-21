package com.gkancheva.guessthecelebrity.models;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class QuestionManager {
    private int questionCount;
    private int correctAnswers;
    private List<Celebrity> celebrities;
    private int chosenCelebIndex;
    private int indexCorrectAnswer;

    public QuestionManager(List<Celebrity> celebrities) {
        this.questionCount = 0;
        this.correctAnswers = 0;
        this.celebrities = celebrities;
    }

    public List<String> generateNewGuess() {
        Random random = new Random();
        chosenCelebIndex = random.nextInt(celebrities.size());
        String chosenCelebName = celebrities.get(chosenCelebIndex).getName();
        indexCorrectAnswer = random.nextInt(4);
        List<String> options = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if(i == indexCorrectAnswer) {
                options.add(i, chosenCelebName);
            } else {
                String currentName = celebrities.get(random.nextInt(celebrities.size())).getName();
                while(currentName.equals(chosenCelebName) || options.contains(currentName)) {
                    currentName = celebrities.get(random.nextInt(celebrities.size())).getName();
                }
                options.add(i, currentName);
            }
        }
        return options;
    }

    public Bitmap getBitmapOfGeneratedQuestion() {
        ImageDownloadTask imageDownloadTask = new ImageDownloadTask();
        Bitmap celebImage = null;
        try {
            celebImage = imageDownloadTask
                    .execute(celebrities.get(chosenCelebIndex).getUrlPhoto())
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
        return celebImage;
    }

    public String getResultString(int tag) {
        this.questionCount++;
        if(tag == indexCorrectAnswer) {
            correctAnswers++;
            return "Correct!";
        } else {
           return "Wrong! This is " + celebrities.get(chosenCelebIndex).getName();
        }
    }

    public String getResult() {
        return String.format(Locale.getDefault(), "%d / %d",
                this.correctAnswers, this.questionCount);
    }
}