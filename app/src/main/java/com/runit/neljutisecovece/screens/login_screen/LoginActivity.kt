package com.runit.neljutisecovece.screens.login_screen

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.runit.neljutisecovece.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login);

        btn_guest_play.setOnClickListener(View.OnClickListener {
            
        })
    }
}
