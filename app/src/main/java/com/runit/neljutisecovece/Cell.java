package com.runit.neljutisecovece;

/**
 * Created by Sarma on 4/16/2018.
 */

public class Cell {
    public static int CELL_SIZE = -1;
    public Player player;
    public int x, y;
    public String id;
    public boolean hightlighted = false;

    public boolean isInCellBounds(int x, int y) {
        int realX = this.x - CELL_SIZE / 2;
        int realY = this.y - CELL_SIZE / 2;
        return x > realX && x < realX + CELL_SIZE
                && y > realY && y < realY + CELL_SIZE;
    }
}
