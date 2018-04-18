package com.runit.neljutisecovece.render;


import android.graphics.Canvas;

import com.runit.neljutisecovece.model.Cell;
import com.runit.neljutisecovece.render.cells.CellsGameRender;
import com.runit.neljutisecovece.render.dice.DiceRender;

import java.util.ArrayList;
import java.util.List;

public class MainGameRender {
    private static final int RENDERS_NUM = 1;

    private List<GameRender> renders;
    private DiceRender diceRender;
    private CellPositionGenerator positionGenerator;
    // indicating if x,y coordinates of cells has been measured
    private boolean measured;
    private final int canvasSize;

    /**
     * Construct main game render. Sets {@link Cell#CELL_SIZE}, {@link Cell#x} and {@link Cell#y} based on canvas size.
     *
     * @param canvasSize canvas size used to set cell size.
     */
    public MainGameRender(int canvasSize) {
        this.canvasSize = canvasSize;
        Cell.CELL_SIZE = canvasSize / 11;
        renders = new ArrayList<>(RENDERS_NUM);
        renders.add(new CellsGameRender());
        diceRender = new DiceRender();
        measured = false;
        positionGenerator = new CellPositionGenerator();
    }

    /**
     * Renders the game screen.
     *
     * @param canvas    {@link Canvas} object to render the screen on.
     * @param gameCells list of cells to be rendered with all its list of {@link com.runit.neljutisecovece.render.attributes.CellAttribute}.
     */
    public void renderGameScreen(Canvas canvas, List<Cell> gameCells, List<Cell> endCells) {
        if (!measured) {
            measured = true;
            positionGenerator.generatePosition(this.canvasSize, gameCells, endCells);
        }
        for (GameRender render :
                renders) {
            render.render(canvas, gameCells, endCells);
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