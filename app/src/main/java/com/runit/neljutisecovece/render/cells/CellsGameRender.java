package com.runit.neljutisecovece.render.cells;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.runit.neljutisecovece.model.Cell;
import com.runit.neljutisecovece.render.GameRender;
import com.runit.neljutisecovece.render.attributes.CellAttribute;

import java.util.List;

import static com.runit.neljutisecovece.model.Cell.CELL_SIZE;


/**
 * Renders {@link Cell} fields in the game with populated players.
 */
public class CellsGameRender implements GameRender {
    private static final int STROKE_SIZE = 5;
    private static final int PLAYER_FILL_SIZE = 10;
    private int strokeSize;
    private int fillSize;
    Paint fill = new Paint();
    Paint playerPaint = new Paint();
    Paint stroke = new Paint();
    Paint highlightStroke = new Paint();

    public CellsGameRender() {
        strokeSize = CELL_SIZE / 2;
        fillSize = CELL_SIZE / 2 - STROKE_SIZE;
        fill.setColor(Color.WHITE);
        fill.setAntiAlias(true);
        playerPaint.setAntiAlias(true);
        stroke.setColor(Color.BLACK);
        stroke.setAntiAlias(true);
        stroke.setTextSize(48);
        highlightStroke.setColor(Color.RED);
    }

    @Override
    public void render(Canvas canvas, List<Cell> cells, List<Cell> endCells) {
        for (Cell c : cells) {
            if (c.getOccupyingPlayer() != null) {
                drawCircle(canvas, c, c.getOccupyingPlayer().getPlayerColor());
            } else {
                drawCircle(canvas, c);
            }
        }
        for (Cell c : endCells) {
            if (c.getOccupyingPlayer() != null) {
                drawCircle(canvas, c, c.getOccupyingPlayer().getPlayerColor());
            } else {
                drawCircle(canvas, c);
            }
        }
    }


    private void drawCircle(Canvas canvas, Cell c) {
        drawCircle(canvas, c, null);
    }

    private void drawCircle(Canvas canvas, Cell c, Integer playerColor) {
        canvas.drawCircle(c.x, c.y, strokeSize, stroke);
        canvas.drawCircle(c.x, c.y, fillSize, fill);

        if (playerColor != null) {
            playerPaint.setColor(playerColor);
            canvas.drawCircle(c.x, c.y, fillSize - PLAYER_FILL_SIZE, playerPaint);
        }

        for (CellAttribute attr : c.getAttributes()) {
            attr.render(canvas, c);
        }
    }
}
