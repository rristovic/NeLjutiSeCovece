package com.runit.neljutisecovece.model;


import android.annotation.SuppressLint;
import android.support.annotation.Nullable;

import com.runit.neljutisecovece.model.attributes.CellAttribute;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a single cell unit in the game.
 */
public class Cell {
    // Cell ID
    private final int id;
    // Player that is currency on this cell.
    private Player currentPlayer;
    // Cell special attributes to be drawn onto the screen
    private List<CellAttribute> attributes;
    // X, Y coordinate on the screen
    public int x, y;
    // Cell size on the screen
    public static int CELL_SIZE = -1;

    public Cell(int id) {
        this.id = id;
    }

    /**
     * Checks if provided x,y coordinates are within cell bounds.
     *
     * @param x X coordinate on the screen.
     * @param y Y coordinate on the screen.
     * @return true if these coordinates are within the cell bounds.
     */
    public boolean isInCellBounds(int x, int y) {
        int realX = this.x - CELL_SIZE / 2;
        int realY = this.y - CELL_SIZE / 2;
        return x > realX && x < realX + CELL_SIZE
                && y > realY && y < realY + CELL_SIZE;
    }

    /**
     * Adds a {@link CellAttribute} to be drawn onto the screen.
     *
     * @param attribute attribute to be added.
     */
    public void addAttribute(CellAttribute attribute) {
        if (this.attributes == null) {
            this.attributes = new LinkedList<>();
        }
        if (!this.attributes.contains(attribute)) {
            this.attributes.add(attribute);
        }
    }

    /**
     * Sets a player to this cell indicating that its occupying current cell.
     *
     * @param player {@link Player} to be set.
     * @return player object who has been on this cell before new player, null if there was no player before the new one.
     */
    public @Nullable
    Player setNewPlayer(Player player) {
        Player current = this.currentPlayer;
        this.currentPlayer = player;
        return current;
    }

    /**
     * Remotes a {@link CellAttribute} from the list of attributes ready for drawing.
     *
     * @param attribute to be removed if found.
     */
    public void remoteAttribute(CellAttribute attribute) {
        if (this.attributes != null)
            this.attributes.remove(attribute);
    }

    /**
     * Retrieves current player who is occupying this cell.
     *
     * @return {@link Player} instance, NULL if there is no player on this cell.
     */
    public @Nullable
    Player getOccupyingPlayer() {
        return currentPlayer;
    }

    public int getIndex() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Cell) {
            Cell compareWith = (Cell) obj;
            return compareWith.id == this.id;
        } else
            return false;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("Cell id: %d, Player occupying: %s", this.id, this.currentPlayer);
    }
}
