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
    // The last cell that player can move to before entering finishing cells.
    private Cell closingCell;
    // Starting cell for a player
    private Cell startingCell;

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
     * Removes a {@link Cell} from currently occupied cell list.
     *
     * @throws IllegalStateException if the player is not occupying provided cell.
     */
    public void removePlayerCell(Cell cell) throws IllegalStateException {
        boolean success = this.currentlyOccupiedCells.remove(cell);
        if (!success)
            throw new IllegalStateException("Cannot remove cell from player: Player hasn't been occupying provided cell: " + cell.toString());
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

    public List<Cell> getCurrentlyOccupiedCells() {
        return Collections.unmodifiableList(this.currentlyOccupiedCells);
    }

    /**
     * Checks if this player is currently occupying some cells on the game field meaning that he has a figure to move onto the next cell.
     *
     * @return true if player has cells occupied.
     */
    public boolean canPlay() {
        return this.currentlyOccupiedCells.size() > 0;
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
        return String.format("Player: %s %s active players: %d", this.playerName, this.playerColor, this.getCurrentlyOccupiedCells().size());
    }
}
