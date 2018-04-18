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
    static final int STROKE_SIZE = 5;
    static final int STROKE_COLOR = Color.BLACK;
    static final Paint strokePaint ;

    static {
        strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setColor(STROKE_COLOR);
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
