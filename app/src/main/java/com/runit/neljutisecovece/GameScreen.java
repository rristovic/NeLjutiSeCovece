package com.runit.neljutisecovece;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

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
        this.gameRender = new MainGameRender(context.getResources().getDisplayMetrics().widthPixels);
        initCells();
        this.touchHandler = new TouchHandler(cells);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        gameRender.renderGameScreen(canvas, cells);
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
                touchHandler.onActionUp();
            }
            if (handled) {
                this.invalidate();
            }
            return handled;
        } else
            return false;
    }

    private void initCells() {
        for (int i = 0; i < 16; i++) {
            Team t = new Team();
            if (i < 4) {
                t.color = Color.RED;
            } else if (i < 8) {
                t.color = Color.BLUE;
            } else if (i < 12) {
                t.color = Color.GREEN;
            } else {
                t.color = Color.YELLOW;
            }
            Player p = new Player();
            p.id = i;
            p.team = t;
            players.add(p);
        }
        cells = new ArrayList<>(40);

        for (int i = 0; i < 40; i++) {
            Cell c = new Cell();
            c.id = String.valueOf(i);
            cells.add(c);
        }
    }
}
