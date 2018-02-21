package com.gkancheva.braintrainer;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Game {
    private Random random;
    private Map<Integer, Integer> answers;
    private int correctIndex;
    private int score;
    private int questionCount;

    public Game() {
        this.answers = new LinkedHashMap<>();
        this.random = new Random();
    }

    public String startGame() {
        this.questionCount = 0;
        this.score = 0;
        return this.generateQuestion();
    }

    public String generateQuestion() {
        answers.clear();
        int num1 = random.nextInt(21);
        int num2 = random.nextInt(21);
        correctIndex = random.nextInt(4);
        for (int i = 0; i < 4; i++) {
            if(correctIndex == i) {
                answers.put(i, num1 + num2);
            } else {
                int incorrectAnswer = random.nextInt(50);
                while(incorrectAnswer == num1 + num2) {
                    incorrectAnswer = random.nextInt(50);
                }
                answers.put(i, random.nextInt(50));
            }
        }
        return String.format(Locale.getDefault(), "%d + %d = ", num1, num2);
    }

    public boolean isCorrectAnswer(int tag) {
        boolean res = false;
        this.questionCount++;
        if(tag == this.correctIndex) {
            this.score++;
            res = true;
        }
        return res;
    }

    public Map<Integer, Integer> getAnswers() {
        return Collections.unmodifiableMap(this.answers);
    }

    public String getCurrentScore() {
        return String.format(Locale.getDefault(),
                "%d / %d", this.score, this.questionCount);
    }

    public String getFinalScore() {
        return String.format(Locale.getDefault(),
                "Your score is: %d / %d", this.score, this.questionCount);
    }
}
