package com.example.mainpage_rangebuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mainpage_rangebuddy.models.LoginRequest

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)


        //button to go back to main page
        val toMainPage = findViewById<Button>(R.id.ToMainPage)
        toMainPage.setOnClickListener {
            val back = Intent(this, MainActivity::class.java)
            startActivity(back)
        }


        //button to go to sign up page
        val toSignUp = findViewById<Button>(R.id.SignUp)
        toSignUp.setOnClickListener {
            val signUp = Intent(this, SignUp::class.java)
            startActivity(signUp)
        }
        val viewModel = ViewModelProvider(this)[APIViewModel::class.java]
        viewModel.getData().observe(this){
            Log.d("Response", "Success")
        }
        //viewModel.postData(Profile(0,"zahra.mehedi12@gmail.com", "Zahra Test", "pasword123"))
        viewModel.login(LoginRequest("test@gmail.com", "password123"))
    }
}