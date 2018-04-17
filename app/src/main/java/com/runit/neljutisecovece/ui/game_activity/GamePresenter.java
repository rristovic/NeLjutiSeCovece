package com.runit.neljutisecovece.ui.game_activity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.graphics.Canvas;
import android.support.annotation.NonNull;

import com.runit.neljutisecovece.model.Game;
import com.runit.neljutisecovece.render.GameRender;
//import com.runit.neljutisecovece.render.SimpleGameRender;

/**
 * Created by Radovan Ristovic on 4/16/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class GamePresenter extends AndroidViewModel implements GameScreenContract.Presenter {

    private GameScreenContract.View mGameScreen;
    private GameRender mGameRender;
    private Canvas mCanvas;
    private Game mGame;

    public GamePresenter(@NonNull Application application) {
        super(application);
    }

    @Override
    public void init(Canvas canvas, GameScreenContract.View view, String[] teams) {
//        this.mGameRender = new SimpleGameRender(this.getApplication());
        this.mCanvas = canvas;
        this.mGameScreen = view;
        this.mGame = new Game(teams);
    }

    @Override
    public void onScreenClicked(int x, int y) {

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.mGameScreen = null;
        this.mCanvas = null;
        this.mGameRender = null;
    }
}
