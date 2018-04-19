package com.runit.neljutisecovece.model;


import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.graphics.Color;
import android.support.annotation.Nullable;

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

        void onPlayerChanged();
    }

    // Number of times player can roll dice it he's not in the game before moving on to the next player. Must be greater then 0.
    private static final int NUM_OF_RETRIES = 3;
    private static final int STANDARD_CELL_NUM = 40;
    private static final int MAX_PLAYER_NUM = 4;
    private static final int MIN_PLAYER_NUM = 1;
    private static final int END_CELLS_PER_PLAYER = 4;
    public static final int[] COLORS = new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
    // Dice number a player must get in order to occupy a starting cell
    private static final int DICE_NUM_FOR_START = 6;

    // listener for game changed events
    private GameChangedListener mGameChangedListener;

    private ArrayList<Cell> gameFields;
    private List<Player> players;
    private Map<Long, List<Cell>> endCells;
    private List<Cell> endCellsList;
    private CellTouchHandler cellTouchHandler;
    private Dice dice;

    // Game logic vars //
    // Indicating if player should roll the dice
    private boolean shouldRollDice;
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
     * Helper method for setting the next player who should be playing. Changes to next player only if {@link #lastDiceRoll} != {@value #DICE_NUM_FOR_START}.
     * Sets {@link #shouldRollDice} to true.
     */
    private void nextPlayer() {
        if (this.currentPlayer == null) {
            this.currentPlayer = this.players.get(0);
        }

        if (lastDiceRoll != DICE_NUM_FOR_START) {
            // Next player only if dice wasn't 6
            int index = this.players.indexOf(this.currentPlayer);
            if (index == this.players.size() - 1) {
                this.currentPlayer = this.players.get(0);
            } else {
                index++;
                this.currentPlayer = this.players.get(index);
            }
            if (this.mGameChangedListener != null)
                this.mGameChangedListener.onPlayerChanged();
        }
        shouldRollDice(true);
    }

    /**
     * Called when next click has occurred with correct positions.
     *
     * @param x X coordinate on the screen.
     * @param y Y coordinate on the screen.
     */
    public void onNextClick(int x, int y) {
        // Main game logic
        if (shouldRollDice) {
            lastDiceRoll = dice.rollDice();
            // Notify listener
            if (mGameChangedListener != null)
                mGameChangedListener.onDiceRoll(lastDiceRoll);
            if (currentPlayer.canPlay(lastDiceRoll)) {
                // player has active fields
//                if (currentPlayer.canPlayOnlyOneWay(lastDiceRoll) &&
//                        (lastDiceRoll != DICE_NUM_FOR_START ||
//                                currentPlayer.getCurrentlyOccupiedCells().contains(currentPlayer.getStartingCell()))) {
//                    // Move player automatically if only one player on the field, or 6 is rolled and player is on start
//                    movePlayerToNextCell(currentPlayer.getCurrentlyOccupiedCells().get(0));
//                    nextPlayer();
//                } else {
//                    // Wait for next click
//                    waitForClick();
//                }
                waitForClick();
            } else {
                // Player don't have active fields
                if (lastDiceRoll == DICE_NUM_FOR_START) {
                    // If player cannot play and the dice is correct, move him to start position
                    movePlayerToStart();
                } else {
                    if (currentPlayer.numberOfRetry < NUM_OF_RETRIES - 1) {
                        // player can retry 3 times before continuing
                        currentPlayer.numberOfRetry++;
                        shouldRollDice(true);
                    } else {
                        currentPlayer.numberOfRetry = 0;
                        // Failed to start game, move to next player
                        nextPlayer();
                    }
                }
            }
        } else {
            if (currentPlayer.canPlay(lastDiceRoll)) {
                Cell c = cellTouchHandler.getClickedCell(this.getGameFields(), x, y, currentPlayer);
                if (c == null) {
                    c = cellTouchHandler.getClickedCell(this.getEndCells().get(currentPlayer.getPlayerId()), x, y, currentPlayer);
                }
                if (c != null) {
                    if (c.getOccupyingPlayer() != null && c.getOccupyingPlayer().equals(this.currentPlayer)) {
                        // move if clicked on player's own cell
                        boolean moved = movePlayerToNextCell(c);
                        checkForGameEnd();
                        if (moved) {
                            nextPlayer();
                        } else if (currentPlayer.canPlayOnlyOneWay(lastDiceRoll)) {
                            // Failed to move because player is blocking himself
                            nextPlayer();
                        }
                    } else if (currentPlayer.getStartingCell().equals(c) && currentPlayer.hasAvailablePlayersInHouse()) {
                        // move player to start
                        movePlayerToStart();
                    }
                }
            } else if (lastDiceRoll == DICE_NUM_FOR_START) {
                movePlayerToStart();
            }
        }

        updateGame();
    }


    /**
     * Method for placing current player on its starting cell while removes old player if exists.
     */
    private void movePlayerToStart() {
        Cell start = currentPlayer.getStartingCell();
        Player oldPlayer = start.setNewPlayer(currentPlayer);
        if (oldPlayer != null) {
            if (oldPlayer.equals(currentPlayer)) {
                throw new IllegalStateException("Player has been moved to starting cell where the old player was already standing.");
            } else {
                oldPlayer.removePlayerCell(start);
            }
        }
        currentPlayer.addNewPlayerCell(start);
        shouldRollDice(true);
    }

    private void checkForGameEnd() {

    }

    /**
     * Moves current player to the new cell.
     *
     * @param currentCell old cell that player was on.
     * @return true if moving a player was a success, false otherwise.
     */
    private boolean movePlayerToNextCell(Cell currentCell) {
        Cell nextCell = getNextCell(currentCell);
        if (nextCell != null && (nextCell.getOccupyingPlayer() == null || !nextCell.getOccupyingPlayer().equals(currentPlayer))) {
            // Set player new cell
            if (this.endCells.get(currentPlayer.getPlayerId()).contains(nextCell)) {
                // Next cell is end cell
                currentPlayer.moveToEndCell(nextCell);
                currentPlayer.removePlayerCell(currentCell);
                nextCell.setNewPlayer(currentPlayer);
            } else {
                currentPlayer.removePlayerCell(currentCell);
                currentPlayer.addNewPlayerCell(nextCell);
                // Move player to the new cell
                Player player = nextCell.setNewPlayer(currentPlayer);
                if (player != null) {
                    // Remove old cell from old player
                    player.removePlayerCell(nextCell);
                }
            }
            // Remove cell old player
            currentCell.setNewPlayer(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns next cell that a player should moved to. Can be next cell from {@link #gameFields} or from {@link #endCells}.
     *
     * @param currentCell current cell that player is on.
     * @return next cell that a player should move to, NULL if player cannot move to next cell because out of bounds.
     */
    private @Nullable
    Cell getNextCell(Cell currentCell) {
        if (this.endCells.get(currentPlayer.getPlayerId()).contains(currentCell)) {
            // Is in end cells
            return getNextEndCell(currentCell);
        } else {
            int nextCellIndex = getNextCellIndex(currentCell, lastDiceRoll);
            Cell closingCell = currentPlayer.getClosingCell();
            if ((closingCell.getIndex() < nextCellIndex && closingCell.getIndex() >= currentCell.getIndex())
                    || (nextCellIndex < currentCell.getIndex() && closingCell.getIndex() >= currentCell.getIndex())) {
                // Next cell should be end cell
                int index = getEndCellIndexAfterStandard(currentCell);
                if (index != -1) {
                    return endCells.get(currentPlayer.getPlayerId()).get(index);
                } else return null;
            } else {
                return gameFields.get(nextCellIndex);
            }
        }
    }

    /**
     * Returns next cell index a player should moved to disregarding end cells.
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
     * Returns END cell index a player should be moved to from standard cells.
     *
     * @param lastCell the cell player is currently on.
     * @return next cell index, -1 if index is out of bounds.
     */
    private int getEndCellIndexAfterStandard(Cell lastCell) {
        int stepsInEndCells = lastDiceRoll - (currentPlayer.getClosingCell().getIndex() - lastCell.getIndex());
        if (stepsInEndCells <= END_CELLS_PER_PLAYER) {
            return stepsInEndCells - 1;
        } else {
            return -1;
        }
    }

    /**
     * Retrieves next end cell a player should be move to when already in ending cells.
     *
     * @param currentCell the cell player is currently on,
     * @return next end cell, NULL if out of bounds.
     */
    private @Nullable
    Cell getNextEndCell(Cell currentCell) {
        return currentCell.getIndex() + lastDiceRoll > 3 ? null : endCells.get(currentPlayer.getPlayerId()).get(currentCell.getIndex() + lastDiceRoll);
    }

    /**
     * Called when waiting for another player's click.
     */
    private void waitForClick() {
        shouldRollDice(false);
    }

    /**
     * Sets {@link #shouldRollDice} to provided boolean.
     */
    private void shouldRollDice(boolean rollDice) {
        shouldRollDice = rollDice;
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
        endCellsList = new ArrayList<>(playerNum * END_CELLS_PER_PLAYER);
        for (Player player :
                this.players) {
            List<Cell> cells = new ArrayList<>(END_CELLS_PER_PLAYER);
            for (int i = 0; i < END_CELLS_PER_PLAYER; i++) {
                Cell c = new Cell(i, true);
                cells.add(c);
                endCellsList.add(c);
            }
            endCells.put(player.getPlayerId(), cells);
        }
    }

    public void setGameFieldsChangedListener(GameChangedListener listener) {
        this.mGameChangedListener = listener;
    }

    /**
     * Return list of main {@link Cell} object from this game with populated players.
     *
     * @return {@link LiveData} observable emitting unmodifiable list of cells from this game.
     */
    public List<Cell> getGameFields() {
        return Collections.unmodifiableList(gameFields);
    }

    /**
     * Return map of end {@link Cell} object from this game with populated players.
     * Map key is {@link Player#playerId}, map value is list of end cells for each player.
     *
     * @return {@link LiveData} observable emitting map of cells from this game.
     */
    public Map<Long, List<Cell>> getEndCells() {
        return Collections.unmodifiableMap(endCells);
    }


    /**
     * Return list of end {@link Cell} object from this game with populated players.
     *
     * @return {@link LiveData} observable emitting list of cells from this game.
     */
    public List<Cell> getEndCellsAsList() {
        return Collections.unmodifiableList(endCellsList);
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

    /**
     * Retrieves the player who is currently playing the game.
     *
     * @return {@link Player} object who is currently playing.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean getShouldRollDice() {
        return shouldRollDice;
    }
}

