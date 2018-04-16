package com.runit.neljutisecovece;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    View gameScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int width = getResources().getDisplayMetrics().widthPixels;
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(width, width);
        gameScreen = findViewById(R.id.game_screen);
        gameScreen.setLayoutParams(params);
        gameScreen.setFocusable(false);
        gameScreen.setEnabled(false);
    }


}
