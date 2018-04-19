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
         * Called by presenter when dice update is needed.
         *
         * @param diceNumber dice number to be drawn.
         */
        void updateDice(int diceNumber);

        /**
         * Called when message needs to be shown.
         *
         * @param message String message.
         */
        void showMessage(String message);

        /**
         * Called wheen current player playing the game has changed.
         *
         * @param player {@link Player} object from the game who is currently playing the game.
         */
        void showCurrentPlayer(Player player);
    }

    interface Presenter {
        /**
         * Initialize method for presenter.
         *
         * @param gameScreenSize size of the game screen on which the game will be rendered.
         * @param players        string array holding player names.
         */
        void init(int gameScreenSize, String[] players);

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


        /**
         * Called by the view when dice roll is being drawn.
         *
         * @param isRolling indicating is dice is still rolling, is dice is still being drawn. False if dice has finished drawing.
         */
        void onDiceRolling(boolean isRolling);

        /**
         * Sets view reference into presenter.
         *
         * @param view {@link View} object.
         */
        void setView(View view);
    }
}
