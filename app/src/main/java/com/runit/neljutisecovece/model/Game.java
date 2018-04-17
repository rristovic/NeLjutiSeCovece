package com.runit.neljutisecovece.model;


import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.graphics.Color;

import com.runit.neljutisecovece.model.attributes.EndCellAttribute;
import com.runit.neljutisecovece.screens.game_screen.CellTouchHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class containing game logic.
 */
public class Game {

    public interface GameChangedListener {
        void onGameEnd(Player winner);

        void onGameFieldChanged(List<Cell> fields);

        void onEndGameFieldChanged(Map<Long, List<Cell>> endCells);

        /**
         * Called when dice is rolling and when it's finished.
         *
         * @param number number indicating dice number.
         */
        void onDiceRoll(int number);
    }

    private static final int STANDARD_CELL_NUM = 40;
    private static final int MAX_PLAYER_NUM = 4;
    private static final int MIN_PLAYER_NUM = 1;
    private static final int END_CELLS_PER_PLAYER = 4;
    private static final int[] COLORS = new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
    // Dice number a player must get in order to occupy a starting cell
    private static final int DICE_NUM_FOR_START = 6;

    // listener for game changed events
    private GameChangedListener mGameChangedListener;

    private ArrayList<Cell> gameFields;
    private List<Player> players;
    private Map<Long, List<Cell>> endCells;
    private CellTouchHandler cellTouchHandler;
    private Dice dice;

    // Game logic vars //
    // Indicating if player should roll the dice
    private boolean shouldRoleDice;
    // last number of dice rolled
    private int lastDiceRoll;
    // Current player who is playing its turn
    private Player currentPlayer;


    /**
     * Construct the game logic.
     *
     * @param players array of string containing player names, which length must be from [{@value #MIN_PLAYER_NUM}, {@value #MAX_PLAYER_NUM}].
     */
    public Game(String... players) {
        cellTouchHandler = new CellTouchHandler();
        dice = new Dice();
        initCells();
        initPlayers(players.length, players);
        initEndCells(players.length);
        nextPlayer();
    }

    /**
     * Helper method for setting the next player who should be playing. Sets {@link #shouldRoleDice} to true.
     */
    private void nextPlayer() {
        if (this.currentPlayer == null) {
            this.currentPlayer = this.players.get(0);
        } else {
            int index = this.players.indexOf(this.currentPlayer);
            if (index == this.players.size() - 1) {
                this.currentPlayer = this.players.get(0);
            } else {
                index++;
                this.currentPlayer = this.players.get(index);
            }
        }
        this.shouldRoleDice = true;
    }

    /**
     * Called when next click has occurred with correct positions.
     *
     * @param x X coordinate on the screen.
     * @param y Y coordinate on the screen.
     */
    public void onNextClick(int x, int y) {
        // Main game logic
        if (shouldRoleDice) {
            lastDiceRoll = dice.rollDice();
            if (currentPlayer.canPlay()) {
                // If player can play, wait for the next click
                shouldRoleDice = false;
            } else {
                if (lastDiceRoll == DICE_NUM_FOR_START) {
                    // If player cannot player and the dice is correct, move him to start position
                    movePlayerToStart();
                    updateGame();
                } else {
                    // Failed to start game, move to next player
                    shouldRoleDice = true;
                    nextPlayer();
                }
            }
            if (mGameChangedListener != null)
                mGameChangedListener.onDiceRoll(lastDiceRoll);
        } else {
            if (currentPlayer.canPlay()) {
                Cell c = cellTouchHandler.getClickedCell(this.getGameFields(), x, y);
                if (c != null) {
                    if (c.getOccupyingPlayer() != null && c.getOccupyingPlayer().equals(this.currentPlayer)) {
                        boolean moved = movePlayerToNextCell(c);
                        if (moved) {
                            updateGame();
                        }
                        checkForGameEnd();
                        nextPlayer();
                    }
                }
            } else if (lastDiceRoll == DICE_NUM_FOR_START) {
                movePlayerToStart();
                updateGame();
            }
        }
    }
    /**
     * Method for placing current player on its starting cell.
     */
    private void movePlayerToStart() {
        Cell start = currentPlayer.getStartingCell();
        Player oldPlayer = start.setNewPlayer(currentPlayer);
        if (oldPlayer != null) {
            throw new IllegalStateException("Player has been moved to starting cell where the old player was already standing.");
        }
        currentPlayer.addNewPlayerCell(start);
        nextPlayer();
    }

    private void checkForGameEnd() {

    }

    /**
     * Moves current player to the new cell.
     * @param currentCell old cell that player was on.
     * @return true if moving a player was a success, false otherwise.
     */
    private boolean movePlayerToNextCell(Cell currentCell) {
        int nextCellIndex = getNextCellIndex(currentCell, lastDiceRoll);
        Cell nextCell = this.gameFields.get(nextCellIndex);
        if (nextCell.getOccupyingPlayer() == null || !nextCell.getOccupyingPlayer().equals(currentPlayer)) {
            // Remove player old cell
            currentPlayer.removePlayerCell(currentCell);
            // Set player new cell
            currentPlayer.addNewPlayerCell(nextCell);
             // Move player to the new cell
            Player player = nextCell.setNewPlayer(currentPlayer);
            if (player != null) {
                // Remove old cell from old player
                player.removePlayerCell(nextCell);
            }
            // Remove cell old player
            currentCell.setNewPlayer(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns next cell index recursive.
     *
     * @param lastCell the cell player is currently on.
     * @param counter  dice roll of the player.
     * @return next cell index.
     */
    private int getNextCellIndex(Cell lastCell, int counter) {
        if (counter == 0) {
            return lastCell.getIndex();
        } else {
            int index = lastCell.getIndex();
            if (index == this.gameFields.size() - 1) {
                index = 0;
            } else {
                index++;
            }
            return getNextCellIndex(this.gameFields.get(index), --counter);
        }
    }

    /**
     * Helper method for notifying observer that the game has changed.
     */
    private void updateGame() {
        mGameChangedListener.onGameFieldChanged(this.getGameFields());
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
        this.players = new ArrayList<>(playerNum);
        for (int playerIndex = 0; playerIndex < playerNum; playerIndex++) {
            Cell closingCell;
            Cell startingCell;
            if (playerIndex == 0) {
                closingCell = gameFields.get(gameFields.size() - 1);
            } else {
                closingCell = gameFields.get(playerIndex * 10 - 1);
            }
            startingCell = gameFields.get(playerIndex * 10);
            Player player = new Player(playerIndex, players[playerIndex], COLORS[playerIndex], closingCell, startingCell);
            this.players.add(player);
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
        for (Player player :
                this.players) {
            List<Cell> cells = new ArrayList<>(END_CELLS_PER_PLAYER);
            for (int i = 0; i < END_CELLS_PER_PLAYER; i++) {
                Cell c = new Cell(i);
                c.addAttribute(new EndCellAttribute(player.getPlayerColor()));
                cells.add(c);
            }
            endCells.put(player.getPlayerId(), cells);
        }
    }

    public void setGameFieldsChangedListener(GameChangedListener listener) {
        this.mGameChangedListener = listener;
    }

    /**
     * Return unmodifiable list of {@link Cell} objects.
     *
     * @return {@link LiveData} observable emitting list of cells from this game.
     */
    public List<Cell> getGameFields() {
        return Collections.unmodifiableList(gameFields);
    }

    /**
     * Return unmodifiable list of {@link Player} objects.
     *
     * @return players list from this game.
     */
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    /**
     * Retrieves last rolled dice bby the player.
     *
     * @return number of dice rolled.
     */
    public int getLastDiceRoll() {
        return lastDiceRoll;
    }
}
