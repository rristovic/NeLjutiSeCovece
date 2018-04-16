package com.runit.neljutisecovece.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;

import com.runit.neljutisecovece.Cell;

import java.util.List;

import static com.runit.neljutisecovece.Cell.CELL_SIZE;

/**
 * Created by Sarma on 4/16/2018.
 */

public class CellsGameRender implements GameRender {
    private static final int STROKE_SIZE = 5;
    private static final int PLAYER_FILL_SIZE = 10;
    private int halfCell;
    private int strokeSize;
    private int fillSize;
    Paint fill = new Paint();
    Paint stroke = new Paint();
    Paint highlightStroke = new Paint();

    public CellsGameRender() {
        strokeSize = CELL_SIZE / 2;
        fillSize = CELL_SIZE / 2 - STROKE_SIZE;
        halfCell = CELL_SIZE / 2;
        fill.setColor(Color.WHITE);
        stroke.setColor(Color.BLACK);
        highlightStroke.setColor(Color.RED);
        stroke.setTextSize(48);
    }

    @Override
    public void render(Canvas canvas, List<Cell> cells) {
        int canvasH = canvas.getHeight();
        int canvasHalfH = canvas.getHeight() / 2;
        int canvasW = canvas.getWidth();
        int canvasHalfW = canvas.getWidth() / 2;

        for (int i = 0; i < 11; i++) {
            if (i < 5) {
                // right vertical rows
                drawVertical(canvas, canvasHalfW + CELL_SIZE, 0, i, cells.get(i));
                // left vertical rows
                drawVertical(canvas, canvasHalfW - CELL_SIZE, 0, i, cells.get(38 - i));
                // top horizontal rows
                drawHorizontal(canvas, 0, canvasHalfH - CELL_SIZE, i, cells.get(i + 30));
                // bottom horizontal rows
                drawHorizontal(canvas, 0, canvasHalfH + CELL_SIZE, i, cells.get(28 - i));
            } else if (i > 5) {
                // right vertical rows
                drawVertical(canvas, canvasHalfW + CELL_SIZE, canvasH / 2 + halfCell,
                        i - 6, cells.get(i + 8));
                // left vertical rows
                drawVertical(canvas, canvasHalfW - CELL_SIZE, canvasH / 2 + halfCell,
                        i - 6, cells.get(30 - i));
                // top horizontal rows
                drawHorizontal(canvas, canvasHalfW + CELL_SIZE / 2, canvasHalfH - CELL_SIZE,
                        i - 6, cells.get(i - 2));
                // bottom horizontal rows
                drawHorizontal(canvas, canvasHalfW + CELL_SIZE / 2, canvasHalfH + CELL_SIZE,
                        i - 6, cells.get(20 - i));
            }
        }

        drawVertical(canvas, canvasHalfW, 0, 0, cells.get(39));
        drawVertical(canvas, canvasW - halfCell, canvasHalfH - halfCell, 0, cells.get(9));
        drawVertical(canvas, canvasHalfW, canvasH - CELL_SIZE, 0, cells.get(19));
        drawVertical(canvas, halfCell, canvasHalfH - halfCell, 0, cells.get(29));
    }

    Paint playerPaint = new Paint();

    private void drawVertical(Canvas canvas, int x, int yStart, int indexInLine, Cell c) {
        c.x = x;
        c.y = yStart + (CELL_SIZE * indexInLine) + halfCell;
        if (c.player != null) {
            playerPaint.setColor(c.player.team.color);
            drawCircle(canvas, c, playerPaint);
        } else {
            drawCircle(canvas, c);
        }
    }

    private void drawHorizontal(Canvas canvas, int xStart, int y, int indexInLine, Cell c) {
        c.y = y;
        c.x = xStart + (CELL_SIZE * indexInLine) + halfCell;
        if (c.player != null) {
            playerPaint.setColor(c.player.team.color);
            drawCircle(canvas, c, playerPaint);
        } else {
            drawCircle(canvas, c);
        }
    }

    private void drawCircle(Canvas canvas, Cell c) {
        drawCircle(canvas, c, null);
    }

    private void drawCircle(Canvas canvas, Cell c, @Nullable Paint playerPaint) {
        if (c.hightlighted) {
            canvas.drawCircle(c.x, c.y, strokeSize, highlightStroke);
        } else {
            canvas.drawCircle(c.x, c.y, strokeSize, stroke);
        }
        canvas.drawCircle(c.x, c.y, fillSize, fill);
        if (playerPaint != null)
            canvas.drawCircle(c.x, c.y, fillSize - PLAYER_FILL_SIZE, playerPaint);
    }
}
