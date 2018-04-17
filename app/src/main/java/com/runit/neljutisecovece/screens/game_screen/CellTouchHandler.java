package com.runit.neljutisecovece.screens.game_screen;

import android.support.annotation.Nullable;

import com.runit.neljutisecovece.model.Cell;

import java.util.List;


/**
 * Inspects if one of the cells with player on it has been clicked.
 */
public class CellTouchHandler {

    /**
     * Returns clicked cell which has player on it, based on provided x,y coordinates.
     *
     * @param cells list of cells that needs to be checked with click event.
     * @param y     Y coordinate of click event.
     * @param x     X coordinate of click event.
     * @return {@link Cell} object that has been clicked, NULL if no cell has been clicked.
     */
    public @Nullable
    Cell getClickedCell(List<Cell> cells, int x, int y) {
        for (Cell c :
                cells) {
            if (c.getOccupyingPlayer() != null && c.isInCellBounds(x, y)) {
                return c;
            }
        }

        return null;
    }
}
