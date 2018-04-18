package com.runit.neljutisecovece.screens.game_screen;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.graphics.Canvas;
import android.support.annotation.NonNull;

import com.runit.neljutisecovece.model.Cell;
import com.runit.neljutisecovece.model.Game;
import com.runit.neljutisecovece.model.Player;
import com.runit.neljutisecovece.render.MainGameRender;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;


public class GamePresenter extends AndroidViewModel implements GameScreenContract.Presenter {

    private WeakReference<GameScreenContract.View> mGameScreen;
    private MainGameRender mGameRender;
    private Game mGame;
    private boolean mDiceRolling = false;
    private int mCurDiceNumber = 1;

    public GamePresenter(@NonNull Application application) {
        super(application);
    }

    @Override
    public void init(int gameScreenSize, String[] players) {
        // TODO set in weak ref
        this.mGameRender = new MainGameRender(gameScreenSize);
        this.mGame = new Game(players);
        this.mGame.setGameFieldsChangedListener(new Game.GameChangedListener() {
            @Override
            public void onGameEnd(Player winner) {
                updateScreen();
            }

            @Override
            public void onGameFieldChanged(List<Cell> fields) {
                updateScreen();
            }

            @Override
            public void onEndGameFieldChanged(Map<Long, List<Cell>> endCells) {
                updateScreen();
            }

            @Override
            public void onDiceRoll(int number) {
                mDiceRolling = true;
                mCurDiceNumber = number;
                startScreenUpdate();
            }
        });
    }

    @Override
    public void setView(GameScreenContract.View view) {
        this.mGameScreen = new WeakReference<>(view);
        updateCurrentPlayer();
    }


    @Override
    public void drawGameScreen(Canvas canvas) {
        if (mDiceRolling) {
            // TODO cache the board
            mGameRender.renderGameScreen(canvas, mGame.getGameFields(), mGame.getEndCellsAsList());
            mGameRender.renderDice(canvas, mCurDiceNumber);
            mDiceRolling = mGameRender.isDiceRolling();
            if (!mDiceRolling) {
               endScreenUpdate();
            }
        } else {
            mGameRender.renderGameScreen(canvas, mGame.getGameFields(), mGame.getEndCellsAsList());
            mGameRender.renderDice(canvas, mCurDiceNumber);
        }
    }


    @Override
    public void onScreenClicked(int x, int y) {
        mGame.onNextClick(x, y);
        updateCurrentPlayer();
    }

    @Override
    public boolean isInitialized() {
        return this.mGame != null && this.mGameRender != null && this.mGameScreen != null;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.mGameRender = null;
        this.mGame = null;
        this.mGameScreen.clear();
        this.mGameScreen = null;
    }

    private void updateCurrentPlayer() {
        if (mGameScreen.get() != null) {
            mGameScreen.get().showCurrentPlayer(mGame.getCurrentPlayer());
        }
    }

    private void updateScreen() {
        if (mGameScreen.get() != null) {
            mGameScreen.get().updateGameScreen();
        }
    }

    private void startScreenUpdate() {
        if (mGameScreen.get() != null) {
            mGameScreen.get().startUpdateGameScreen();
        }
    }

    private void endScreenUpdate() {
        if (mGameScreen.get() != null) {
            mGameScreen.get().endUpdateGameScreen();
        }
    }
}
