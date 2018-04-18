package com.runit.neljutisecovece.render;

import com.runit.neljutisecovece.model.Cell;

import java.util.List;

import static com.runit.neljutisecovece.model.Cell.CELL_SIZE;

/**
 * Generates x,y coordinates for list of cells.
 */
public class CellPositionGenerator {

    private final int halfCell;

    public CellPositionGenerator() {
        halfCell = CELL_SIZE / 2;
    }

    /**
     * Generates {@link Cell#x} and {@link Cell#y} coordinates based on provided canvas size and {@link Cell#CELL_SIZE}.
     *
     * @param canvasSize canvas size to generate coordinates against.
     * @param cells      list of main cells. Cell at index 0 will be measured as top right cell on the field.
     * @param endCells   list of end cells. Cell at index 0 will be measure as top cell on the field.
     */
    public void generatePosition(int canvasSize, List<Cell> cells, List<Cell> endCells) {
        int canvasHalfSize = canvasSize / 2;
        for (int i = 0; i < 11; i++) {
            if (i < 5) {
                // top right vertical rows
                generateVertical(canvasHalfSize + CELL_SIZE, 0, i, cells.get(i));
                // top left vertical rows
                generateVertical(canvasHalfSize - CELL_SIZE, 0, i, cells.get(38 - i));
                //  top left horizontal rows
                generateHorizontal(0, canvasHalfSize - CELL_SIZE, i, cells.get(i + 30));
                // bottom left horizontal rows
                generateHorizontal(0, canvasHalfSize + CELL_SIZE, i, cells.get(28 - i));
            } else if (i > 5) {
                // right bottom vertical rows
                generateVertical(canvasHalfSize + CELL_SIZE, canvasSize / 2 + halfCell,
                        i - 6, cells.get(i + 8));
                // left bottom vertical rows
                generateVertical(canvasHalfSize - CELL_SIZE, canvasSize / 2 + halfCell,
                        i - 6, cells.get(30 - i));
                // top right horizontal rows
                generateHorizontal(canvasHalfSize + CELL_SIZE / 2, canvasHalfSize - CELL_SIZE,
                        i - 6, cells.get(i - 2));
                // bottom right horizontal rows
                generateHorizontal(canvasHalfSize + CELL_SIZE / 2, canvasHalfSize + CELL_SIZE,
                        i - 6, cells.get(20 - i));
            }
        }

        for (int i = 0; i < endCells.size(); i++) {
            if (i < 4) {
                // Top end cells
                generateVertical(canvasHalfSize, CELL_SIZE, i, endCells.get(i));
            } else if (i < 8) {
                // right end cells
                generateHorizontal(canvasHalfSize + halfCell, canvasHalfSize, i - 4, endCells.get(i));
            } else if (i < 12) {
                // bottom end cells
                generateVertical(canvasHalfSize, canvasHalfSize + halfCell,
                        i - 8, endCells.get(i));
            } else {
                // left end cells
                generateHorizontal(CELL_SIZE, canvasHalfSize,
                        i - 12, endCells.get(i - 2));
            }

        }

        generateVertical(canvasHalfSize, 0, 0, cells.get(39));
        generateVertical(canvasSize - halfCell, canvasHalfSize - halfCell, 0, cells.get(9));
        generateVertical(canvasHalfSize, canvasSize - CELL_SIZE, 0, cells.get(19));
        generateVertical(halfCell, canvasHalfSize - halfCell, 0, cells.get(29));
    }


    private void generateVertical(int x, int yStart, int cellIndexInRow, Cell c) {
        c.x = x;
        c.y = yStart + (CELL_SIZE * cellIndexInRow) + halfCell;
    }

    private void generateHorizontal(int xStart, int y, int indexInLine, Cell c) {
        c.y = y;
        c.x = xStart + (CELL_SIZE * indexInLine) + halfCell;
    }
}
