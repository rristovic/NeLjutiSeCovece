package com.runit.neljutisecovece.render;


import android.graphics.Canvas;
import android.graphics.Color;

import com.runit.neljutisecovece.model.Cell;
import com.runit.neljutisecovece.model.Game;
import com.runit.neljutisecovece.render.attributes.CellAttribute;
import com.runit.neljutisecovece.render.attributes.EndCellAttribute;
import com.runit.neljutisecovece.render.attributes.StartCellAttribute;
import com.runit.neljutisecovece.render.cells.CellsGameRender;

import java.util.ArrayList;
import java.util.List;

public class MainGameRender {
    private static final int RENDERS_NUM = 1;
    public static final int FIRST_PLAYER_START_POSITION = 0;
    public static final int SECOND_PLAYER_START_POSITION = 10;
    public static final int THIRD_PLAYER_START_POSITION = 20;
    public static final int FORTH_PLAYER_START_POSITION = 30;
    public static final int[] COLORS = new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};

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
            // Order is important for drawing!
            addEndCellAttributes(endCells);
            addStartCellAttributes(gameCells);
        }
        for (GameRender render :
                renders) {
            render.render(canvas, gameCells, endCells);
        }
    }

    private void addEndCellAttributes(List<Cell> endCells) {
        CellAttribute player1 = new EndCellAttribute(COLORS[0]);
        CellAttribute player2 = new EndCellAttribute(COLORS[1]);
        CellAttribute player3 = new EndCellAttribute(COLORS[2]);
        CellAttribute player4 = new EndCellAttribute(COLORS[3]);
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

    private void addStartCellAttributes(List<Cell> gameFields) {
        CellAttribute player1 = new StartCellAttribute(COLORS[0]);
        CellAttribute player2 = new StartCellAttribute(COLORS[1]);
        CellAttribute player3 = new StartCellAttribute(COLORS[2]);
        CellAttribute player4 = new StartCellAttribute(COLORS[3 ]);
        if (gameFields.get(FIRST_PLAYER_START_POSITION) != null) {
            gameFields.get(FIRST_PLAYER_START_POSITION).addAttribute(player1);
        }
        if (gameFields.get(SECOND_PLAYER_START_POSITION) != null) {
            gameFields.get(SECOND_PLAYER_START_POSITION).addAttribute(player2);
        }
        if (gameFields.get(THIRD_PLAYER_START_POSITION) != null) {
            gameFields.get(THIRD_PLAYER_START_POSITION).addAttribute(player3);
        }
        if (gameFields.get(FORTH_PLAYER_START_POSITION) != null) {
            gameFields.get(FORTH_PLAYER_START_POSITION).addAttribute(player4);
        }
    }
}