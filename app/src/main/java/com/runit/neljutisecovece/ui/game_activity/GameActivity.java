package com.runit.neljutisecovece.ui.game_activity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.runit.neljutisecovece.R;
import com.runit.neljutisecovece.model.Game;
import com.runit.neljutisecovece.model.Team;

public class GameActivity extends AppCompatActivity implements GameScreenContract.View{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Game g = new Game("Radovan", "rale");
    }

    @Override
    public void onCanvasUpdate(Canvas canvas) {

    }

    @Override
    public void onGameEnded(Team winner) {

    }
}
