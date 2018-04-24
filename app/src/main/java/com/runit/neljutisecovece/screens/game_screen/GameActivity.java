package com.runit.neljutisecovece.screens.game_screen;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.runit.neljutisecovece.R;
import com.runit.neljutisecovece.model.Player;
import com.runit.neljutisecovece.render.dice.DiceView;
import com.runit.neljutisecovece.util.UIUtil;

public class GameActivity extends AppCompatActivity implements GameScreenContract.View {

    private TextView mTvText;
    private View mGameScreen;
    private DiceView mDiceView;
    private FrameLayout mGameContainer, mDiceContainer, mFlPlayerColor;
    private GameScreenContract.Presenter mPresenter;
    private GestureDetector mGestureDetector;
    // Game size in order for all 11 field to be set up correctly. Must be a number that has x % 20 == 0.
    private int gameScreenSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
    public void showGameEndDialog(Player winner) {
        AlertDialog ad = new AlertDialog.Builder(this)
                .setTitle(R.string.game_over)
                .setMessage(String.format(getString(R.string.player_won_msg), winner.getPlayerName()))
                .setNeutralButton(R.string.close, (dialog, which) -> setScreenEnable(false))
                .create();
        setScreenEnable(false);
        ad.show();
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
        if (mGameScreen != null) {
            mGameScreen.invalidate();
        }
    }

    @Override
    public void updateDice(int diceNumber) {
        mDiceView.drawDice(diceNumber);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        mTvText = findViewById(R.id.tv_text);
        mFlPlayerColor = findViewById(R.id.fl_current_player_color);
        mGameContainer = findViewById(R.id.fl_game_view);
        mDiceContainer = findViewById(R.id.fl_dice_view);
        mGameScreen = new View(this) {
            @Override
            protected void onDraw(Canvas canvas) {
                mPresenter.drawGameScreen(canvas);
                super.onDraw(canvas);
            }
        };
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(gameScreenSize, gameScreenSize);
        mGameScreen.setLayoutParams(params);
        mGameContainer.addView(mGameScreen);
        mGameScreen.setOnTouchListener((v, event) -> {
            if (mGameScreen.isEnabled())
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
        mDiceView = new DiceView(this, () ->
                mPresenter.onDiceRolling(false)
        );
        mDiceView.setId(R.id.dice_view_id);
        mDiceView.setOnTouchListener((v, event) -> {
            if (mDiceView.isEnabled())
                mGestureDetector.onTouchEvent(event);
            return true;
        });
        FrameLayout.LayoutParams diceParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mDiceView.setLayoutParams(diceParams);
        mDiceContainer.addView(mDiceView);
    }

    private void setScreenEnable(boolean enabled) {
        mGameScreen.setEnabled(enabled);
        mDiceView.setEnabled(enabled);
    }
}
