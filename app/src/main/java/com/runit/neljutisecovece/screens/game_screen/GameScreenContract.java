package com.runit.neljutisecovece.screens.game_screen;

import android.graphics.Canvas;

import com.runit.neljutisecovece.model.Player;


/**
 * MVP contract for main game screen.
 */
public interface GameScreenContract {
    interface View {
        /**
         * Called when game has ended to show winner dialog and score board.
         *
         * @param winner {@link Player} who won the game.
         */
        void showGameEndDialog(Player winner);

        /**
         * Called when requested a game screen update.
         */
        void updateGameScreen();

        /**
         * Called when interval update of the screen is requested by presenter.
         */
        void startUpdateGameScreen();

        /**
         * Called when stopping interval update of the screen is requested by presenter.
         */
        void endUpdateGameScreen();

        /**
         * Called when message needs to be shown.
         *
         * @param message String message.
         */
        void showMessage(String message);
    }

    interface Presenter {
        /**
         * Initialize method for presenter.
         *
         * @param gameScreenSize size of the game screen on which the game will be rendered.
         * @param view           {@link View} object implementation.
         * @param players        string array holding player names.
         */
        void init(int gameScreenSize, View view, String[] players);

        /**
         * Checks if presenter has already been initialized.
         */
        boolean isInitialized();

        /**
         * Called when game screen has been clicked.
         *
         * @param x coordinate on the screen.
         * @param y coordinate on the screen.
         */
        void onScreenClicked(int x, int y);

        /**
         * Called when screen drawing is requested from the view.
         *
         * @param canvas {@link Canvas} to draw the game screen on.
         */
        void drawGameScreen(Canvas canvas);
    }
}
