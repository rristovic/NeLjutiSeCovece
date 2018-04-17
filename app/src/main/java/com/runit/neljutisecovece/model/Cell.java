package com.runit.neljutisecovece.model;


import android.support.annotation.Nullable;

/**
 * Represents a single cell unit in the game.
 */
public class Cell {
    // Current player that is in this cell.
    private Player currentPlayer;
    // Next cell that player can move to.
    // Owner of the cell indicating that this cell is either an end cell in the game or a start cell for player.
    private @Nullable
    Team cellOwner;
    // Next closing cell that certain player can move to.
    private @Nullable
    Cell nextClosingCell;
    private boolean isEndCell;

    public Cell() {
        currentPlayer = null;
        cellOwner = null;
        nextClosingCell = null;
        isEndCell = false;
    }

    public Cell(boolean isEndCell) {
        this();
        this.isEndCell = isEndCell;
    }

    public boolean isEndCell() {
        return isEndCell;
    }

    @Nullable
    public Cell getNextClosingCell() {
        return nextClosingCell;
    }

    /**
     * Sets the next closing sell a certain player can move to. Player will move to this cell if it's from cell's team owned set by {@link #setCurrentCellTeamOwner(Team)}.
     *
     * @param nextClosingCell {@link Cell} next cell players will move to if from the cell's team owner.
     */
    public void setNextClosingCell(Cell nextClosingCell) {
        this.nextClosingCell = nextClosingCell;
    }

    /**
     * Sets current cell's team owner. If {@link #setNextClosingCell(Cell)} isn't called, this cell will be marked as starting cell for provided team.
     *
     * @param cellOwner {@link Team} object to be set as a cell owner.
     */
    public void setCurrentCellTeamOwner(Team cellOwner) {
        this.cellOwner = cellOwner;
    }

    /**
     * Indicates if this cell is a starting point for provided player.
     *
     * @param player {@link Player} to check the cell against.
     * @return true if provided player can start the game from this cell.
     */
    public boolean isStartingCellForPlayer(Player player) {
        return this.cellOwner != null && this.nextClosingCell == null && player.belongsToTeam(this.cellOwner);
    }

    /**
     * Method to call when new player has stepped on this cell. Sets the cell as occupied and removes any current player in this cell if present.
     *
     * @param newPlayer {@link Player} object to be set as currently occupying player.
     * @return {@link Player} object who has been occupying this cell until now, NULL if the cell was empty.
     */
    public @Nullable
    Player newPlayerOnTheCell(Player newPlayer) {
        Player oldPlayer = this.currentPlayer;
        this.currentPlayer = newPlayer;
        return oldPlayer;
    }

    /**
     * Method to call when a player has moved from this cell.
     */
    public void playerMovedFromCell() {
        this.currentPlayer = null;
    }

    /**
     * Indicates if provided player can move to this cell. Player cannot be set to this cell if the currently occupying player is from the same team as a provided player.
     *
     * @param player {@link Player} that is trying to move to this cell.
     * @return true if player can move to this cell.
     */
    public boolean canMoveToThisCell(Player player) {
        return this.currentPlayer == null || !this.currentPlayer.isFromSameTeam(player);
    }

    /**
     * Returns current player that is occupying this cell.
     * @return {@link Player} instance that is on this cell.
     */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }
}
