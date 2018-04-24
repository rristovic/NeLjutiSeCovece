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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


public class GamePresenter extends AndroidViewModel implements GameScreenContract.Presenter {

    private WeakReference<GameScreenContract.View> mGameScreen;
    private MainGameRender mGameRender;
    private Game mGame;
    private boolean mDiceRolling = false;
    private int mLastDiceRoll = -1;

    private Queue<Runnable> mTaskQueue = new LinkedList<>();

    public GamePresenter(@NonNull Application application) {
        super(application);
    }

    @Override
    public void init(int gameScreenSize, String[] players) {
        this.mGameRender = new MainGameRender(gameScreenSize);
        this.mGame = new Game(players);
        this.mGame.setGameFieldsChangedListener(new Game.GameChangedListener() {
            @Override
            public void onGameEnd(Player winner) {
                queueTaskAndExecute(() -> {
                    if (mGameScreen.get() != null) mGameScreen.get().showGameEndDialog(winner);
                });
            }

            @Override
            public void onGameFieldChanged(List<Cell> fields) {
                queueTaskAndExecute(GamePresenter.this::updateScreen);
            }

            @Override
            public void onEndGameFieldChanged(Map<Long, List<Cell>> endCells) {
                queueTaskAndExecute(GamePresenter.this::updateScreen);
            }

            @Override
            public void onDiceRoll(int number) {
                if (mGameScreen.get() != null) {
                    mLastDiceRoll = number;
                    setDiceRolling(true);
                    mGameScreen.get().updateDice(number);
                }
            }

            @Override
            public void onPlayerChanged() {
                queueTaskAndExecute(GamePresenter.this::updateCurrentPlayer);
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
        mGameRender.renderGameScreen(canvas, mGame.getGameFields(), mGame.getEndCellsAsList());
    }


    @Override
    public void onScreenClicked(int x, int y) {
        if (!mDiceRolling) {
            mGame.onNextClick(x, y);
        }
    }

    @Override
    public void onDiceRolling(boolean isRolling) {
        if (!isRolling) {
            // Finished rolling
            queueTaskAndExecute(this::updateScreen);
        }
        setDiceRolling(isRolling);
    }


    /**
     * Calls view's {@link GameScreenContract.View#showCurrentPlayer(Player)} method with {@link Game#getCurrentPlayer()} as a method param.
     */
    private void updateCurrentPlayer() {
        if (mGameScreen.get() != null) {
            mGameScreen.get().showCurrentPlayer(mGame.getCurrentPlayer());
        }
    }

    /**
     * Calls view's {@link GameScreenContract.View#updateGameScreen()} method.
     */
    private void updateScreen() {
        if (mGameScreen.get() != null) {
            mGameScreen.get().updateGameScreen();
        }
    }

    /**
     * Adds tasks to task queue. {@link #executePendingTasks()} will be called immediately.
     *
     * @param r {@link Runnable} task to be added to queue.
     */
    private void queueTaskAndExecute(Runnable r) {
        mTaskQueue.add(r);
        executePendingTasks();
    }

    /**
     * Executes all pending tasks on {@link #mTaskQueue} if <code>{@link #mDiceRolling} == false</code>.
     */
    private void executePendingTasks() {
        while (!mDiceRolling && !mTaskQueue.isEmpty()) {
            Runnable r = mTaskQueue.poll();
            r.run();
        }
    }

    /**
     * Sets {@link #mDiceRolling} variable. Is provided param is true, {@link #executePendingTasks()} will be called indicating dice roll is finished and should update game screen.
     *
     * @param rolling boolean indicating if dice is currently in rolling state.
     */
    private void setDiceRolling(boolean rolling) {
        this.mDiceRolling = rolling;
        if (!mDiceRolling)
            executePendingTasks();
    }


    @Override
    public boolean isInitialized() {
        return this.mGame != null && this.mGameRender != null && this.mGameScreen != null;
    }

    public int getLastDiceRoll() {
        return mLastDiceRoll;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.mGameRender = null;
        this.mGame = null;
        this.mGameScreen.clear();
        this.mGameScreen = null;
    }
}
