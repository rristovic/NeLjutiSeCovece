package com.runit.neljutisecovece.render;


import android.graphics.Canvas;

import com.runit.neljutisecovece.Cell;

import java.util.ArrayList;
import java.util.List;

public class MainGameRender {
    private static final int RENDERS_NUM = 1;

    private List<GameRender> renders;

    public MainGameRender(int canvasSize) {
        Cell.CELL_SIZE = canvasSize / 11;
        renders = new ArrayList<>(RENDERS_NUM);
        renders.add(new CellsGameRender());
    }

    public void renderGameScreen(Canvas canvas, List<Cell> cells) {
        for (GameRender render :
                renders) {
            render.render(canvas, cells);
        }
    }
}