package com.company.jokeproviderlib;

public class JokeProvider {

    private JokeProvider(){}

    public static String provideJoke() {
        JokeManager jokeManager = new JokeManager();
        return jokeManager.getJoke();
    }

}