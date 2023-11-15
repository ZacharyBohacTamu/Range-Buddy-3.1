package com.example.mainpage_rangebuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.mainpage_rangebuddy.models.LoginRequest

class MainActivity : AppCompatActivity() {
    //variables called
    private lateinit var ToInformationPage: ImageButton
    private lateinit var StartSession: Button
    private lateinit var PastSessionsPage: Button
    private lateinit var ToLogin: Button
    private lateinit var TextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cameraPermission = android.Manifest.permission.CAMERA
        val storagePermissions = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val requestCode = 1

        if (ContextCompat.checkSelfPermission(this, cameraPermission) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(storagePermissions, requestCode)
        }

        //hiding the title bar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)


        //button to information page
        ToInformationPage = findViewById(R.id.Information)
        ToInformationPage.setOnClickListener {
            val ToHelp = Intent(this, HelpPage::class.java)
            startActivity(ToHelp)
        }
        //button to PicturePage
        StartSession = findViewById(R.id.StartSession)
        StartSession.setOnClickListener {
            val Auto = Intent(this, PhotoPage::class.java)
            startActivity(Auto)
        }
        //button to PastSessionsPage
        PastSessionsPage = findViewById(R.id.ToPastSessionsPage)
        PastSessionsPage.setOnClickListener {
            val Past = Intent(this, PastSessions::class.java)
            startActivity(Past)
        }
        //button to Login Page
        ToLogin = findViewById(R.id.Login_button)
        ToLogin.setOnClickListener {
            val Login = Intent(this, LoginPage::class.java)
            startActivity(Login)
        }

    }
}