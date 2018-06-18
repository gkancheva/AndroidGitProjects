package com.company.jokeproviderlib;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class JokeManager {

    private Random random = new Random();
    private List<String> jokes = Arrays.asList(
            "Programmer: A person who fixed a problem that you don`t know you have, in a way you don`t understand.",
            "Q: Why do Java programmers have to wear glasses? A: Because they don`t C!",
            "Real programmers count from 0.",
            "A foo walks into a bar, takes a look around and says: 'Hello World!'",
            "3 Database SQL walked into a NoSQL bar. A little while later... they walked out. They couldn`t find a table.",
            "The programmer`s wife sent him to the grocery store. Her instructions were: 'Buy butter. See if they have eggs. If they do, buy 10.'. So, he bought 10 butters.",
            "Programmer got stuck in the shower because the instructions on the shampoo bottle said... 'Lather, rinse, repeat.'.",
            "A SQL query goes into a bar, walks up to two tables and asks: 'Can I join you?'",
            "Q: How many programmers does it take to screw in a light bulb? A: None. It's a hardware problem.",
            "An optimist says: 'the glass is half full.' A pessimist says: 'the glass is half empty.' A programmer says: 'the glass is twice as large as necessary.'"
    );

    public String getJoke() {
        int index = this.random.nextInt(jokes.size());
        return this.jokes.get(index);
    }


}