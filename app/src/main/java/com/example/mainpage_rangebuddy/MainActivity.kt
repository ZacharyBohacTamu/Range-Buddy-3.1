package com.example.mainpage_rangebuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.util.Log
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.mainpage_rangebuddy.models.LoginRequest

class MainActivity : AppCompatActivity() {
    //variables called
    private lateinit var ToInformationPage: ImageButton
    private lateinit var StartSession: Button
    private lateinit var PastSessionsPage: Button

    private lateinit var TextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //hiding the title bar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        setContentView(R.layout.activity_main)

        //Python Code testing
        TextView = findViewById(R.id.textView)

        //code to run Python scripts sometimes. to call them
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }

        val py = Python.getInstance()
        val pyobj = py.getModule("TestPy") //name of python file
        val obj = pyobj.callAttr("main") //name of function in python file

        TextView.text = obj.toString() //prints out the output of the python file


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

    }
}