package com.runit.neljutisecovece.model;

import java.util.Random;


public class Dice {
    private Random random;

    public Dice() {
        random = new Random();
    }

    public int rollDice() {
        return random.nextInt(6) + 1;
    }
}
