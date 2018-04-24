package com.runit.neljutisecovece.render.attributes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.runit.neljutisecovece.model.Cell;


/**
 * Class representing single cell attribute, e.g. CellHighlighted, CellDisabled etc.
 * Cell attribute is responsible for drawing itself onto the canvas.
 */
public abstract class CellAttribute {
    public static final int STROKE_SIZE = 5;
    public static final int STROKE_COLOR = Color.BLACK;
    public static final Paint STROKE_PAINT;
    public static final Paint CELL_FILL;

    static {
        STROKE_PAINT = new Paint();
        STROKE_PAINT.setAntiAlias(true);
        STROKE_PAINT.setColor(STROKE_COLOR);
        CELL_FILL = new Paint();
        CELL_FILL.setColor(Color.WHITE);
        CELL_FILL.setAntiAlias(true);
    }

    /**
     * Draw itself onto the provided canvas.
     *
     * @param canvas {@link Canvas} to draw itself on.
     * @param cell   {@link Cell} to which this attribute is attached to.
     */
    public abstract void render(Canvas canvas, Cell cell);

    @Override
    public abstract boolean equals(Object obj);
}
