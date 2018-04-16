package com.runit.neljutisecovece.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;

import com.runit.neljutisecovece.Cell;

import java.util.List;

/**
 * Created by Sarma on 4/16/2018.
 */

public class CellsGameRender implements GameRender {
    private static final int STROKE_SIZE = 5;
    private static final int PLAYER_FILL_SIZE = 10;
    private int cellWidth = -1;
    private int halfCell;
    private int strokeSize;
    private int fillSize;
    Paint fill = new Paint();
    Paint stroke = new Paint();

    public CellsGameRender() {
        fill.setColor(Color.WHITE);
        stroke.setColor(Color.BLACK);
        stroke.setTextSize(48);
    }

    @Override
    public void render(Canvas canvas, List<Cell> cells) {
        if (cellWidth == -1) {
            cellWidth = canvas.getWidth() / 11;
            strokeSize = cellWidth / 2;
            fillSize = cellWidth / 2 - STROKE_SIZE;
            halfCell = cellWidth / 2;
        }
        int canvasH = canvas.getHeight();
        int canvasHalfH = canvas.getHeight() / 2;
        int canvasW = canvas.getWidth();
        int canvasHalfW = canvas.getWidth() / 2;

        for (int i = 0; i < 11; i++) {
            if (i < 5) {
                // right vertical rows
                drawVertical(canvas, canvasHalfW + cellWidth, 0, i, cells.get(i));
                // left vertical rows
                drawVertical(canvas, canvasHalfW - cellWidth, 0, i, cells.get(38 - i));
                // top horizontal rows
                drawHorizontal(canvas, 0, canvasHalfH - cellWidth, i, cells.get(i + 30));
                // bottom horizontal rows
                drawHorizontal(canvas, 0, canvasHalfH + cellWidth, i, cells.get(28 - i));
            } else if (i > 5) {
                // right vertical rows
                drawVertical(canvas, canvasHalfW + cellWidth, canvasH / 2 + halfCell,
                        i - 6, cells.get(i + 8));
                // left vertical rows
                drawVertical(canvas, canvasHalfW - cellWidth, canvasH / 2 + halfCell,
                        i - 6, cells.get(30 - i));
                // top horizontal rows
                drawHorizontal(canvas, canvasHalfW + cellWidth / 2, canvasHalfH - cellWidth,
                        i - 6, cells.get(i - 2));
                // bottom horizontal rows
                drawHorizontal(canvas, canvasHalfW + cellWidth / 2, canvasHalfH + cellWidth,
                        i - 6, cells.get(20 - i));
            }
        }

        drawVertical(canvas, canvasHalfW, 0, 0, cells.get(39));
        drawVertical(canvas, canvasW - halfCell, canvasHalfH - halfCell, 0, cells.get(9));
        drawVertical(canvas, canvasHalfW, canvasH - cellWidth, 0, cells.get(19));
        drawVertical(canvas, halfCell, canvasHalfH - halfCell, 0, cells.get(29));
    }

    Paint playerPaint = new Paint();

    private void drawVertical(Canvas canvas, int x, int yStart, int indexInLine, Cell c) {
        c.x = x;
        c.y = yStart + (cellWidth * indexInLine) + halfCell;
        if (c.player != null) {
            playerPaint.setColor(c.player.team.color);
            drawCircle(canvas, c.x, c.y, playerPaint);
        } else {
            drawCircle(canvas, c.x, c.y);
        }
    }

    private void drawHorizontal(Canvas canvas, int xStart, int y, int indexInLine, Cell c) {
        c.y = y;
        c.x = xStart + (cellWidth * indexInLine) + halfCell;
        if (c.player != null) {
            playerPaint.setColor(c.player.team.color);
            drawCircle(canvas, c.x, c.y, playerPaint);
        } else {
            drawCircle(canvas, c.x, c.y);
        }
    }

    private void drawCircle(Canvas canvas, int x, int y) {
        drawCircle(canvas, x, y, null);
    }

    private void drawCircle(Canvas canvas, int x, int y, @Nullable Paint playerPaint) {
        canvas.drawCircle(x, y, strokeSize, stroke);
        canvas.drawCircle(x, y, fillSize, fill);
        if (playerPaint != null)
            canvas.drawCircle(x, y, fillSize - PLAYER_FILL_SIZE, playerPaint);
    }
}
