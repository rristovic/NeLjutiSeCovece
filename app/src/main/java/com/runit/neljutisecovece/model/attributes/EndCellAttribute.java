package com.runit.neljutisecovece.model.attributes;

import android.graphics.Canvas;

import com.runit.neljutisecovece.model.Cell;


/**
 * Attribute indicating that this is player's closing cell.
 */
public class EndCellAttribute extends CellAttribute {

    private final int color;

    public EndCellAttribute(int playerColor) {
        this.color = playerColor;
    }

    @Override
    void render(Canvas canvas, Cell cell) {
        // TODO render closing cell
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof EndCellAttribute;
    }
}
