package com.runit.neljutisecovece.render.attributes;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.runit.neljutisecovece.model.Cell;


/**
 * Attribute indicating that this is player's closing cell.
 */
public class EndCellAttribute extends CellAttribute {
    private final int color;
    private final Paint cellPaint;

    public EndCellAttribute(int playerColor) {
        this.color = playerColor;
        this.cellPaint = new Paint();
        this.cellPaint.setAntiAlias(true);
        this.cellPaint.setColor(color);
    }

    @Override
    public void render(Canvas canvas, Cell cell) {
        canvas.drawCircle(cell.x, cell.y, Cell.CELL_SIZE / 2, CellAttribute.strokePaint);
        canvas.drawCircle(cell.x, cell.y, Cell.CELL_SIZE / 2 - CellAttribute.STROKE_SIZE, this.cellPaint);
        if (cell.getOccupyingPlayer() != null)
            canvas.drawCircle(cell.x, cell.y, Cell.CELL_SIZE / 2 - (CellAttribute.STROKE_SIZE * 3), CellAttribute.strokePaint);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof EndCellAttribute;
    }
}
