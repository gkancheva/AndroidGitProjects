package com.company.jokeproviderlib;

public class JokeProvider {

    private static final String JOKE = "This should be a very funny joke!";

    private JokeProvider(){}

    public static String provideJoke() {
        return JOKE;
    }

}