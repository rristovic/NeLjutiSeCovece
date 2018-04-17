package com.runit.neljutisecovece.screens.game_screen;

import android.support.annotation.Nullable;

import com.runit.neljutisecovece.model.Cell;
import com.runit.neljutisecovece.model.Player;

import java.util.List;


/**
 * Inspects if one of the cells with player on it has been clicked.
 */
public class CellTouchHandler {

    /**
     * Returns clicked cell which has player on it, based on provided x,y coordinates.
     * If player isn't on the cell, it will return a cell in case it's a starting cell for provided player.
     *
     * @param cells     list of cells that needs to be checked with click event.
     * @param y         Y coordinate of click event.
     * @param x         X coordinate of click event.
     * @param forPlayer {@link Player} object that cell lookup is call for.
     * @return {@link Cell} object that has been clicked, NULL if no cell has been clicked.
     */
    public @Nullable
    Cell getClickedCell(List<Cell> cells, int x, int y, Player forPlayer) {
        for (Cell c : cells) {
            if (c.getOccupyingPlayer() != null && c.isInCellBounds(x, y) ||
                    (forPlayer.getStartingCell().equals(c)) && c.isInCellBounds(x, y)) {
                return c;
            }
        }

        return null;
    }
}
