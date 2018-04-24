package com.runit.neljutisecovece.model;


import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a player in the game which has 4 object to the game with.
 */
public class Player {
    // How many actual object a player can play with.
    private static final int OBJECTS_PER_PLAYER = 4;
    private final long playerId;
    private final String playerName;
    private final int playerColor;
    // Holds all currently occupied cells by this player. Size cannot be greater then 4.
    private List<Cell> currentlyOccupiedCells;
    // Holds amount of objects in end cells. Cannot be greater then 4.
    private Cell[] currentlyOccupiedEndCells;
    // The last cell that player can move to before entering finishing cells.
    private Cell closingCell;
    // Starting cell for a player
    private Cell startingCell;
    public int numberOfRetry = 0;

    /**
     * Construct a player with provided player name, id and player color. Closing cell is the last cell the player can move to before entering finishing cells.
     *
     * @param playerId     id of the player.
     * @param playerName   name of the player.
     * @param playerColor  color of the player.
     * @param closingCell  player's closing cell.
     * @param startingCell player's starting cell.
     */
    public Player(long playerId, String playerName, int playerColor, Cell closingCell, Cell startingCell) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.playerColor = playerColor;
        this.closingCell = closingCell;
        this.startingCell = startingCell;
        currentlyOccupiedCells = new ArrayList<>(OBJECTS_PER_PLAYER);
        currentlyOccupiedEndCells = new Cell[OBJECTS_PER_PLAYER];
    }

    /**
     * Ads a new {@link Cell} to currently occupying cells, indicating that this player is now occupying provided cell.
     *
     * @param cell new cell to be occupied by this player.
     * @throws IllegalStateException if player has already occupied 4 cells or the player has already occupied provided cell.
     */
    public void addNewPlayerCell(Cell cell) throws IllegalArgumentException {
        if (currentlyOccupiedCells.size() == OBJECTS_PER_PLAYER)
            throw new IllegalStateException("Cannot add new player cell, reason: Player cannot have more than 4 occupied cells at a time.");
        else {
            if (currentlyOccupiedCells.contains(cell))
                throw new IllegalStateException("Cannot add new player cell, reason: Player is already occupying provided cell: " + cell.toString());
            else
                currentlyOccupiedCells.add(cell);
        }
    }

    /**
     * Removes a {@link Cell} from currently occupied cell list or from end cells.
     *
     * @throws IllegalStateException if the player is not occupying provided cell.
     */
    public void removePlayerCell(Cell cell) throws IllegalStateException {
        boolean success = this.currentlyOccupiedCells.remove(cell);
        if (!success) {
            // try removing it from end cells if it isn't in main game cells
            success = this.currentlyOccupiedEndCells[cell.getIndex()] != null;
            this.currentlyOccupiedEndCells[cell.getIndex()] = null;
        }
        if (!success)
            throw new IllegalStateException("Cannot remove cell from player: Player hasn't been occupying provided cell: " + cell.toString());
    }

    /**
     * Ads a new {@link Cell} to currently occupying end cells.
     *
     * @param newEndCell end cell that player should be move to.
     * @throws IllegalStateException if player has already occupied 4 cells or the player has already occupied provided cell.
     */
    public void moveToEndCell(Cell newEndCell) throws IllegalArgumentException {
        if (this.isEndCellsFull())
            throw new IllegalStateException("Cannot move player to end cell, reason: Player cannot have more than 4 occupied end cells at a time.");
        else {
            if (isCellInEndCells(newEndCell))
                throw new IllegalStateException("Cannot move player to end cell, reason: Player is already occupying provided cell: " + newEndCell.toString());
            else {
                currentlyOccupiedEndCells[newEndCell.getIndex()] = newEndCell;
            }
        }
    }

    /**
     * Retrieves player's closing cell.
     *
     * @return {@link Cell} object from this player.
     */
    public Cell getClosingCell() {
        return this.closingCell;
    }

    /**
     * Retrieves player's starting cell.
     *
     * @return {@link Cell} object from this player.
     */
    public Cell getStartingCell() {
        return startingCell;
    }

    public long getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerColor() {
        return playerColor;
    }

    /**
     * Retrieves unmodifiable list of player's currently occupied game fields.
     * @return list of player's occupied cells.
     */
    public List<Cell> getCurrentlyOccupiedCells() {
        return Collections.unmodifiableList(this.currentlyOccupiedCells);
    }

    /**
     * Indicates if player has available figures in its house available to occupy more cells.
     *
     * @return true if player can occupy more cells.
     */
    public boolean hasAvailablePlayersInHouse() {
        return this.currentlyOccupiedCells.size() + this.getCurrentOccupiedEndCellsNumber() != OBJECTS_PER_PLAYER;
    }

    /**
     * Checks if player has available players to play with. Checks if player can play in the end cells according to rolled number.
     *
     * @param diceRoll last dice number player has rolled.
     * @return true if player has cells occupied.
     */
    public boolean hasAvailablePlayersInGame(int diceRoll) {
        return currentlyOccupiedCells.size() > 0 || isMovementAvailableInEndCells(diceRoll);
    }

    /**
     * Checks if this player has only one way to play the game.
     *
     * @param diceRoll last dice number player has rolled.
     * @return true if only one combination of movement is available.
     */
    public boolean canPlayOnlyOneWay(int diceRoll) {
        // TODO calculate is other player's objects (cells, endCell) can move in order with diceRoll. Also if diceRoll is 6
        return this.currentlyOccupiedCells.size() == 1 && !isMovementAvailableInEndCells(diceRoll);
    }

    /**
     * Indicates if player has available movements in its end cells.
     *
     * @param diceRoll player's current dice role number.
     * @return true if player can play within end cells.
     */
    public boolean isMovementAvailableInEndCells(int diceRoll) {
        if (diceRoll > OBJECTS_PER_PLAYER) {
            return false;
        }
        for (int i = 0; i < this.currentlyOccupiedEndCells.length; i++) {
            if (this.currentlyOccupiedEndCells[i] != null) {
                int index = i + diceRoll;
                if (index < OBJECTS_PER_PLAYER && this.currentlyOccupiedEndCells[index] == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Mehod for checking if player's end cell list is fully occupied.
     *
     * @return true if all end cells are occupied.
     */
    public boolean isEndCellsFull() {
        return getCurrentOccupiedEndCellsNumber() == OBJECTS_PER_PLAYER;
    }

    private int getCurrentOccupiedEndCellsNumber() {
        int count = 0;
        for (int i = 0; i < currentlyOccupiedEndCells.length; i++) {
            if (currentlyOccupiedEndCells[i] != null)
                count++;
        }
        return count;
    }

    private boolean isCellInEndCells(Cell cell) {
        for (int i = 0; i < currentlyOccupiedEndCells.length; i++) {
            if (currentlyOccupiedEndCells[i] != null && currentlyOccupiedEndCells[i].equals(cell))
                return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof Player) {
            Player compareWith = (Player) obj;
            return compareWith.playerId == this.playerId;
        } else {
            return false;
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("%s %s active players: %d", this.playerName, this.playerColor, this.getCurrentlyOccupiedCells().size());
    }
}
