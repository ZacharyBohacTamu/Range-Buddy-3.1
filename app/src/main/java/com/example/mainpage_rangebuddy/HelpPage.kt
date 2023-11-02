package com.example.mainpage_rangebuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.Window
import android.widget.Button

class HelpPage : AppCompatActivity() {

    private lateinit var toMainPage: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //hiding the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        setContentView(R.layout.activity_help_page)

        //back to main page
        toMainPage = findViewById(R.id.BackToMainPage)
        toMainPage.setOnClickListener {
            val main = Intent(this, MainActivity::class.java)
            startActivity(main)
        }
    }
}