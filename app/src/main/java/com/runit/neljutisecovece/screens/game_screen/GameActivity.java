package com.runit.neljutisecovece.screens.game_screen;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.runit.neljutisecovece.R;
import com.runit.neljutisecovece.model.Player;
import com.runit.neljutisecovece.util.UIUtil;

public class GameActivity extends AppCompatActivity implements GameScreenContract.View {

    private TextView mTvText;
    private GameView mGameScreen;
    private FrameLayout mContainer, mFlPlayerColor;
    private GameScreenContract.Presenter mPresenter;
    private GestureDetector mGestureDetector;
    // Game size in order for all 11 field to be set up correctly. Must be a number that has x % 20 == 0.
    private int gameScreenSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameScreenSize = (int) Math.round(Math.floor(getResources().getDisplayMetrics().widthPixels / 11) * 11);
        initViews();
        mPresenter = ViewModelProviders.of(this).get(GamePresenter.class);
        if (!mPresenter.isInitialized()) {
            mPresenter.init(gameScreenSize, new String[]{"crveni", "zeleni"});
        }
        mPresenter.setView(this);
    }

    @Override
    public void startUpdateGameScreen() {
        if (mGameScreen != null) {
            mGameScreen.invalidate();
        }
    }

    @Override
    public void endUpdateGameScreen() {
        if (mGameScreen != null) {
            mGameScreen.invalidate();
        }
    }

    @Override
    public void showGameEndDialog(Player winner) {

    }

    @Override
    public void showMessage(String message) {
        UIUtil.showShortToast(GameActivity.this, message);
    }

    @Override
    public void showCurrentPlayer(Player player) {
        mTvText.setText(player.getPlayerName());
        mFlPlayerColor.setBackgroundColor(player.getPlayerColor());
    }

    @Override
    public void updateGameScreen() {
        if (mGameScreen != null)
            mGameScreen.invalidate();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        mTvText = findViewById(R.id.tv_text);
        mFlPlayerColor = findViewById(R.id.fl_current_player_color);
        mContainer = findViewById(R.id.container);
        mGameScreen = new GameView(this) {
            @Override
            protected void onDraw(Canvas canvas) {
                mPresenter.drawGameScreen(canvas);
                super.onDraw(canvas);
            }
        };
        mGameScreen.setOnTouchListener((v, event) -> {
            mGestureDetector.onTouchEvent(event);
            return true;
        });
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                mPresenter.onScreenClicked(Math.round(e.getX()), Math.round(e.getY()));
                return true;
            }
        });
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(gameScreenSize, gameScreenSize);
        mGameScreen.setLayoutParams(params);
        mContainer.addView(mGameScreen);
    }
}
