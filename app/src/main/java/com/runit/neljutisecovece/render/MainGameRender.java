package com.runit.neljutisecovece.render;


import android.graphics.Canvas;

import com.runit.neljutisecovece.model.Cell;

import java.util.ArrayList;
import java.util.List;

public class MainGameRender {
    private static final int RENDERS_NUM = 1;

    private List<GameRender> renders;
    private DiceRender diceRender;

    public MainGameRender(int canvasSize) {
        Cell.CELL_SIZE = canvasSize / 11;
        renders = new ArrayList<>(RENDERS_NUM);
        diceRender = new DiceRender();
        renders.add(new CellsGameRender());
    }

    /**
     * Renders the game screen.
     *
     * @param canvas {@link Canvas} object to render the screen on.
     * @param cells  list of cells to be rendered with all its list of {@link com.runit.neljutisecovece.model.attributes.CellAttribute}.
     */
    public void renderGameScreen(Canvas canvas, List<Cell> cells) {
        for (GameRender render :
                renders) {
            render.render(canvas, cells);
        }
    }

    /**
     * Renders a dice on the game screen.
     *
     * @param canvas     {@link Canvas} object to render the dice on.
     * @param diceNumber number representing current dice state.
     */
    public void renderDice(Canvas canvas, int diceNumber) {
        this.diceRender.renderDice(canvas, diceNumber);
    }

    /**
     * Checks if dice is still rolling.
     */
    public boolean isDiceRolling() {
        return this.diceRender.isDiceRolling();
    }
}