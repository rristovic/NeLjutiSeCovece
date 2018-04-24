package com.runit.neljutisecovece.render.attributes;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.runit.neljutisecovece.model.Cell;


/**
 * Attribute indicating that this is player's closing cell.
 */
public class StartCellAttribute extends CellAttribute {
    private final int color;
    private final Paint cellPaint;

    public StartCellAttribute(int playerColor) {
        this.color = playerColor;
        this.cellPaint = new Paint();
        this.cellPaint.setAntiAlias(true);
        this.cellPaint.setColor(color);
    }

    @Override
    public void render(Canvas canvas, Cell cell) {
        canvas.drawCircle(cell.x, cell.y, (Cell.CELL_SIZE / 2), this.cellPaint);
        canvas.drawCircle(cell.x, cell.y, (Cell.CELL_SIZE / 2) - (CellAttribute.STROKE_SIZE), CellAttribute.CELL_FILL);
        if (cell.getOccupyingPlayer() != null) {
            Paint p = new Paint();
            p.setColor(cell.getOccupyingPlayer().getPlayerColor());
            p.setAntiAlias(true);
            canvas.drawCircle(cell.x, cell.y, (Cell.CELL_SIZE / 2) - (CellAttribute.STROKE_SIZE * 2), p);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof StartCellAttribute;
    }
}
