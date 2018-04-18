package com.runit.neljutisecovece.render;


import android.graphics.Canvas;

import com.runit.neljutisecovece.model.Cell;

import java.util.List;

public interface GameRender {
    void render(Canvas canvas, List<Cell> cells, List<Cell> endCells);
}
