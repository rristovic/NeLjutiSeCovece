package com.runit.neljutisecovece;


import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

public class TouchHandler implements View.OnTouchListener {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

//    private final List<Cell> cells;
//    private Cell lastHightLightedCell;
//
//    public TouchHandler(List<Cell> cells) {
//        this.cells = cells;
//    }
//
//    @Override
//    public boolean onTouch(View view, MotionEvent event) {
//        if (lastHightLightedCell != null) {
//            lastHightLightedCell.hightlighted = false;
//        }
//        lastHightLightedCell = getCellAt(Math.round(event.getX()), Math.round(event.getY()));
//        if (lastHightLightedCell != null) {
//            lastHightLightedCell.hightlighted = true;
//        }
//        return lastHightLightedCell != null;
//    }
//
//    public void onActionUp() {
//        if (lastHightLightedCell != null)
//            lastHightLightedCell.hightlighted = false;
//        lastHightLightedCell = null;
//    }
//
//    private @Nullable
//    Cell getCellAt(int x, int y) {
//        for (Cell cell :
//                cells) {
//            if (cell.player != null) {
//                if (cell.isInCellBounds(x, y)) {
//                    return cell;
//                }
//            }
//        }
//
//        return null;
//    }


}
