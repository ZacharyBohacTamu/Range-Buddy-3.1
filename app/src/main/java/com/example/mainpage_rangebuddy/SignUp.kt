package com.example.mainpage_rangebuddy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class SignUp : AppCompatActivity(){
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //button to go back to main page
        val toMainPage = findViewById<Button>(R.id.ToMainPage)
        toMainPage.setOnClickListener {
            val back = Intent(this, MainActivity::class.java)
            startActivity(back)
        }

        val signUp = findViewById<Button>(R.id.signup)


    }
}