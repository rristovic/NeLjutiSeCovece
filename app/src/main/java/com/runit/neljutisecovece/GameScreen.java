package com.runit.neljutisecovece;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.runit.neljutisecovece.render.MainGameRender;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Sarma on 4/16/2018.
 */

public class GameScreen extends View {

    List<Cell> cels;
    List<Player> players = new ArrayList<>(10);
    private final MainGameRender gameRender;

    public GameScreen(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.gameRender = new MainGameRender();
        initCells();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        gameRender.renderGameScreen(canvas, cels);
        super.onDraw(canvas);
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
        cels = new ArrayList<>(40);

        for (int i = 0; i < 40; i++) {
            Cell c = new Cell();
            c.id = String.valueOf(i);
            cels.add(c);
        }

        int playersInGame = new Random().nextInt(16);
        Random randomIndex = new Random();
        for (int i = playersInGame; i >= 0; i--) {
            cels.get(randomIndex.nextInt(40)).player = players.get(i);
        }
    }
}
