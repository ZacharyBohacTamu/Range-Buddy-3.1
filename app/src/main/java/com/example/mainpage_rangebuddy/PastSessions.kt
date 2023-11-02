package com.example.mainpage_rangebuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.Window
import android.widget.Button

class PastSessions : AppCompatActivity() {
    //button variables called
    private lateinit var ToMainPage: Button
    private lateinit var ToSessionData1: Button
    private lateinit var ToSessionData2: Button
    private lateinit var ToSessionData3: Button
    private lateinit var ToSessionData4: Button
    private lateinit var ToSessionData5: Button
    private lateinit var ToSessionData6: Button
    private lateinit var ToSessionData7: Button
    private lateinit var ToSessionData8: Button
    private lateinit var ToSessionData9: Button
    private lateinit var ToSessionData10: Button
    private lateinit var ToSessionData11: Button
    private lateinit var ToSessionData12: Button
    private lateinit var ToSessionData13: Button
    private lateinit var ToSessionData14: Button
    private lateinit var ToSessionData15: Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //hiding the title bar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        setContentView(R.layout.activity_past_sessions)

        //session 1
        ToSessionData1 = findViewById(R.id.session_1)
        ToSessionData1.setOnClickListener {
            val Session = Intent(this, SessionsData::class.java)
            startActivity(Session)
        }
        //session 2
        ToSessionData2 = findViewById(R.id.session_2)
        ToSessionData2.setOnClickListener {
            val Session = Intent(this, SessionsData::class.java)
            startActivity(Session)
        }
        //session 3
        ToSessionData3 = findViewById(R.id.session_3)
        ToSessionData3.setOnClickListener {
            val Session = Intent(this, SessionsData::class.java)
            startActivity(Session)
        }
        //session 4
        ToSessionData4 = findViewById(R.id.session_4)
        ToSessionData4.setOnClickListener {
            val Session = Intent(this, SessionsData::class.java)
            startActivity(Session)
        }
        //session 5
        ToSessionData5 = findViewById(R.id.session_5)
        ToSessionData5.setOnClickListener {
            val Session = Intent(this, SessionsData::class.java)
            startActivity(Session)
        }
        //session 6
        ToSessionData6 = findViewById(R.id.session_6)
        ToSessionData6.setOnClickListener {
            val Session = Intent(this, SessionsData::class.java)
            startActivity(Session)
        }
        //session 7
        ToSessionData7 = findViewById(R.id.session_7)
        ToSessionData7.setOnClickListener {
            val Session = Intent(this, SessionsData::class.java)
            startActivity(Session)
        }
        //session 8
        ToSessionData8 = findViewById(R.id.session_8)
        ToSessionData8.setOnClickListener {
            val Session = Intent(this, SessionsData::class.java)
            startActivity(Session)
        }
        //session 9
        ToSessionData9 = findViewById(R.id.session_9)
        ToSessionData9.setOnClickListener {
            val Session = Intent(this, SessionsData::class.java)
            startActivity(Session)
        }
        //session 10
        ToSessionData10 = findViewById(R.id.session_10)
        ToSessionData10.setOnClickListener {
            val Session = Intent(this, SessionsData::class.java)
            startActivity(Session)
        }
        //session 11
        ToSessionData11 = findViewById(R.id.session_11)
        ToSessionData11.setOnClickListener {
            val Session = Intent(this, SessionsData::class.java)
            startActivity(Session)
        }
        //session 12
        ToSessionData12 = findViewById(R.id.session_12)
        ToSessionData12.setOnClickListener {
            val Session = Intent(this, SessionsData::class.java)
            startActivity(Session)
        }
        //session 13
        ToSessionData13 = findViewById(R.id.session_13)
        ToSessionData13.setOnClickListener {
            val Session = Intent(this, SessionsData::class.java)
            startActivity(Session)
        }
        //session 14
        ToSessionData14 = findViewById(R.id.session_14)
        ToSessionData14.setOnClickListener {
            val Session = Intent(this, SessionsData::class.java)
            startActivity(Session)
        }
        //session 15
        ToSessionData15 = findViewById(R.id.session_15)
        ToSessionData15.setOnClickListener {
            val Session = Intent(this, SessionsData::class.java)
            startActivity(Session)
        }
        //buttons to other pages
        ToMainPage = findViewById(R.id.BackToMainPage1)
        ToMainPage.setOnClickListener {
            val Return = Intent(this, MainActivity::class.java)
            startActivity(Return)
        }
    }
}