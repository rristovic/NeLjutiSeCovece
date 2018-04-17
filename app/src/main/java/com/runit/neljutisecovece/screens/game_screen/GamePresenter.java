package com.runit.neljutisecovece.screens.game_screen;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.graphics.Canvas;
import android.support.annotation.NonNull;

import com.runit.neljutisecovece.model.Cell;
import com.runit.neljutisecovece.model.Game;
import com.runit.neljutisecovece.model.Player;
import com.runit.neljutisecovece.render.MainGameRender;

import java.util.List;
import java.util.Map;


public class GamePresenter extends AndroidViewModel implements GameScreenContract.Presenter {

    private GameScreenContract.View mGameScreen;
    private MainGameRender mGameRender;
    private Game mGame;
    private boolean mDiceRolling = false;
    private int mCurDiceNumber = 1;

    public GamePresenter(@NonNull Application application) {
        super(application);
    }

    @Override
    public void init(int gameScreenSize, GameScreenContract.View view, String[] players) {
        // TODO set in weak ref
        this.mGameScreen = view;
        this.mGameRender = new MainGameRender(gameScreenSize);
        this.mGame = new Game(players);
        this.mGame.setGameFieldsChangedListener(new Game.GameChangedListener() {
            @Override
            public void onGameEnd(Player winner) {
                mGameScreen.updateGameScreen();
            }

            @Override
            public void onGameFieldChanged(List<Cell> fields) {
                mGameScreen.updateGameScreen();
            }

            @Override
            public void onEndGameFieldChanged(Map<Long, List<Cell>> endCells) {
                mGameScreen.updateGameScreen();
            }

            @Override
            public void onDiceRoll(int number) {
                mDiceRolling = true;
                mCurDiceNumber = number;
                mGameScreen.startUpdateGameScreen();
            }
        });
    }


    @Override
    public void drawGameScreen(Canvas canvas) {
        if (mDiceRolling) {
            // TODO cache the board
            mGameRender.renderGameScreen(canvas, mGame.getGameFields());
            mGameRender.renderDice(canvas, mCurDiceNumber);
            mDiceRolling = mGameRender.isDiceRolling();
            if (!mDiceRolling) {
                mGameScreen.endUpdateGameScreen();
            }
        } else {
            mGameRender.renderGameScreen(canvas, mGame.getGameFields());
            mGameRender.renderDice(canvas, mCurDiceNumber);
        }
    }

    @Override
    public void onScreenClicked(int x, int y) {
        mGame.onNextClick(x, y);
    }

    @Override
    public boolean isInitialized() {
        return this.mGame != null && this.mGameRender != null && this.mGameScreen != null;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.mGameScreen = null;
        this.mGameRender = null;
        this.mGame = null;
    }
}
