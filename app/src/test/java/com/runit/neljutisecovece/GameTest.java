package com.runit.neljutisecovece;

import com.runit.neljutisecovece.model.Game;

import org.junit.Test;

import static com.runit.neljutisecovece.PlayersMock.generatePlayers;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Radovan Ristovic on 4/17/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class GameTest {


    @Test
    public void initGameTest() {
        Game g = new Game(generatePlayers(1));
        g = new Game(generatePlayers(2));
        g = new Game(generatePlayers(3));
        g = new Game(generatePlayers(4));
        boolean failed = false;
        try {
            g = new Game(generatePlayers(5));
        } catch (IllegalArgumentException ex) {
            failed = true;
        }
        assertTrue(failed);
        failed = false;
        try {
            g = new Game(generatePlayers(0));
        } catch (IllegalArgumentException ex) {
            failed = true;
        }
        assertTrue(failed);

        String[] players = generatePlayers();
        g = new Game(players);
        assertEquals(players.length * 4, g.getEndCellsAsList().size());
    }

}
