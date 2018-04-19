package com.runit.neljutisecovece.render;


import android.graphics.Canvas;

import com.runit.neljutisecovece.model.Cell;
import com.runit.neljutisecovece.model.Game;
import com.runit.neljutisecovece.render.attributes.CellAttribute;
import com.runit.neljutisecovece.render.attributes.EndCellAttribute;
import com.runit.neljutisecovece.render.cells.CellsGameRender;
import com.runit.neljutisecovece.render.dice.DiceRender;

import java.util.ArrayList;
import java.util.List;

public class MainGameRender {
    private static final int RENDERS_NUM = 1;

    private List<GameRender> renders;
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
            addEndCellAttributes(endCells);
        }
        for (GameRender render :
                renders) {
            render.render(canvas, gameCells, endCells);
        }
    }

    private void addEndCellAttributes(List<Cell> endCells) {
        CellAttribute player1 = new EndCellAttribute(Game.COLORS[0]);
        CellAttribute player2 = new EndCellAttribute(Game.COLORS[1]);
        CellAttribute player3 = new EndCellAttribute(Game.COLORS[2]);
        CellAttribute player4 = new EndCellAttribute(Game.COLORS[3]);
        for (int i = 0; i < endCells.size(); i++) {
            if (i < 4) {
                endCells.get(i).addAttribute(player1);
            } else if (i < 8) {
                endCells.get(i).addAttribute(player2);
            } else if (i < 12) {
                endCells.get(i).addAttribute(player3);
            } else {
                endCells.get(i).addAttribute(player4);
            }
        }
    }
}