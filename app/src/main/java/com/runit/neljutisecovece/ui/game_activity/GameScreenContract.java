package com.runit.neljutisecovece.ui.game_activity;

import android.graphics.Canvas;

import com.runit.neljutisecovece.model.Team;


/**
 * MVP contract for main game screen.
 */
public interface GameScreenContract {
    interface View {
        void onCanvasUpdate(Canvas canvas);

        void onGameEnded(Team winner);
    }

    interface Presenter {
        /**
         * Initialize method for presenter.
         *
         * @param canvas {@link Canvas} object to draw on.
         * @param view   {@link View} object implementation.
         * @param teams  string array holding team names.
         */
        void init(Canvas canvas, View view, String[] teams);

        /**
         * Caled when game screen has been clicked.
         *
         * @param x coordinate on the screen.
         * @param y coordinate on the screen.
         */
        void onScreenClicked(int x, int y);
    }
}
