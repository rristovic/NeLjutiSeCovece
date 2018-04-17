package com.runit.neljutisecovece.model.attributes;

import android.graphics.Canvas;

import com.runit.neljutisecovece.model.Cell;


/**
 * Interface representing single cell attribute, e.g. CellHighlighted, CellDisabled etc. Cell attributes is responsible for drawing itself onto the canvas.
 */
public abstract class CellAttribute {
    /**
     * Draw itself onto the provided canvas.
     *
     * @param canvas {@link Canvas} to draw itself on.
     * @param cell   {@link Cell} to which this attribute is attached to.
     */
    abstract void render(Canvas canvas, Cell cell);

    @Override
    public abstract boolean equals(Object obj);
}
