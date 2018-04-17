package com.runit.neljutisecovece.model;


import android.annotation.SuppressLint;
import android.graphics.Color;

import com.runit.neljutisecovece.model.attributes.EndCellAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class containing game logic.
 */
public class Game {
    private static final int STANDARD_CELL_NUM = 40;
    private static final int MAX_PLAYER_NUM = 4;
    private static final int MIN_PLAYER_NUM = 1;
    private static final int END_CELLS_PER_PLAYER = 4;
    private static final int[] COLORS = new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};

    private ArrayList<Cell> gameFields;
    private Map<Long, Player> players;
    private Map<Long, List<Cell>> endCells;

    /**
     * Construct the game logic.
     *
     * @param players array of string containing player names, which length must be from [{@value #MIN_PLAYER_NUM}, {@value #MAX_PLAYER_NUM}].
     */
    public Game(String... players) {
        initCells();
        initPlayers(players.length, players);
        initEndCells(players.length);
    }


    /**
     * Construct cells.
     */
    private void initCells() {
        gameFields = new ArrayList<>(STANDARD_CELL_NUM);
        // Setup first cell
        for (int i = 0; i < STANDARD_CELL_NUM; i++) {
            Cell c = new Cell(i);
            gameFields.add(c);
        }
    }

    /**
     * Constructs players from provided names.
     *
     * @param players   player names.
     * @param playerNum number of players, must be from [{@value #MIN_PLAYER_NUM}, {@value #MAX_PLAYER_NUM}];
     * @throws IllegalArgumentException if playerNum is incorrect.
     */
    @SuppressLint({"UseSparseArrays", "DefaultLocale"})
    private void initPlayers(int playerNum, String... players) throws IllegalArgumentException {
        if (playerNum < 1 || playerNum > MAX_PLAYER_NUM) {
            throw new IllegalArgumentException(String.format("Players number is incorrect. Must be a number from %d to %d, inclusive.", MIN_PLAYER_NUM, MAX_PLAYER_NUM));
        }
        this.players = new HashMap<>(playerNum);
        for (int playerIndex = 0; playerIndex < playerNum; playerIndex++) {
            Cell closingCell;
            if (playerIndex == 0) {
                closingCell = gameFields.get(gameFields.size() - 1);
            } else {
                closingCell = gameFields.get(playerIndex * 10 - 1);
            }
            Player player = new Player(playerIndex, players[playerIndex], COLORS[playerIndex], closingCell);
            this.players.put(player.getPlayerId(), player);
        }
    }

    /**
     * Constructs end cells for players.
     *
     * @param playerNum number of players to create cells for.
     */
    @SuppressLint("UseSparseArrays")
    private void initEndCells(int playerNum) {
        endCells = new HashMap<>(playerNum);
        for (Long playerId :
                this.players.keySet()) {
            List<Cell> cells = new ArrayList<>(END_CELLS_PER_PLAYER);
            for (int i = 0; i < END_CELLS_PER_PLAYER; i++) {
                Cell c = new Cell(i);
                c.addAttribute(new EndCellAttribute(this.players.get(playerId).getPlayerColor()));
                cells.add(c);
            }
            endCells.put(playerId, cells);
        }
    }
}
