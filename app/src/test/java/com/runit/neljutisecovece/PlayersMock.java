package com.runit.neljutisecovece;

import java.util.Random;

/**
 * Created by Sarma on 4/18/2018.
 */

public class PlayersMock {
    public static String[] generatePlayers(int playerNum) {
        String[] players = new String[playerNum];
        for (int i = 0; i < playerNum; i++) {
            players[i] = "Igrac " + (i + 1);
        }
        return players;
    }

    public static String[] generatePlayers() {
        return generatePlayers(new Random().nextInt(4) + 1);
    }
}
