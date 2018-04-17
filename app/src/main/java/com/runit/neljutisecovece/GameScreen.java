package com.runit.neljutisecovece;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.runit.neljutisecovece.model.Cell;
import com.runit.neljutisecovece.model.Player;
import com.runit.neljutisecovece.render.MainGameRender;

import java.util.ArrayList;
import java.util.List;


public class GameScreen extends View {

    private TouchHandler touchHandler;
    List<Cell> cells;
    List<Player> players = new ArrayList<>(16);
    private MainGameRender gameRender;

    public GameScreen(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            boolean handled = false;
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                handled = touchHandler.onTouch(this, event);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                handled = true;
//                touchHandler.onActionUp();
            }
            if (handled) {
                this.invalidate();
            }
            return handled;
        } else
            return false;
    }

}
