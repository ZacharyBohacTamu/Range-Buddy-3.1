package com.example.mainpage_rangebuddy

import android.annotation.SuppressLint
import android.content.ContentResolver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.mainpage_rangebuddy.models.Data
import com.example.mainpage_rangebuddy.models.LoginRequest
import java.io.File

class SessionsData : AppCompatActivity() {

    private lateinit var toMainPage: Button
    private lateinit var pastSessionsPage: Button
    private lateinit var comparison: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //hiding the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        //set content view
        setContentView(R.layout.activity_sessions_data)
        val viewModel = ViewModelProvider(this)[APIViewModel::class.java]

        //viewModel.postData(Profile(0,"zahra.mehedi12@gmail.com", "Zahra Test", "pasword123"))
        viewModel.datapoint(Data(5.0f, 5f))


        //comparison button
        comparison = findViewById(R.id.Test_button)
        comparison.setOnClickListener {
            Image_Processing()
        }

        // Code to update the image view with the most recent image
        val imageView = findViewById<ImageView>(R.id.imageView)
        val dP = "/storage/emulated/0/Android/media/com.example.mainpage_rangebuddy/Main Page-Range Buddy/"
        val iC = 2

        val recentImages = contentResolver.imagegrabber(dP, iC)
        if (recentImages.isNotEmpty()) {
            Glide.with(this).load(File(recentImages[1])).into(imageView) // add what ins build.gradle
        } else {
            imageView.setImageResource(R.drawable.target)
        }

        //save session
        pastSessionsPage = findViewById(R.id.save_Session)
        pastSessionsPage.setOnClickListener {
            Toast.makeText(this, "Choose which session to save on", Toast.LENGTH_SHORT).show()
            val session = Intent(this, PastSessions::class.java)
            startActivity(session)
        }
        //back to main page
        toMainPage = findViewById(R.id.BacktoMainPage)
        toMainPage.setOnClickListener {
            val home = Intent(this, MainActivity::class.java)
            startActivity(home)
        }


    }
    //
    fun ContentResolver.imagegrabber(
        directoryPath: String,
        imageCount: Int,
    ): List<String> {
        val images = mutableListOf<String>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_ADDED
        )
        val selection = "${MediaStore.Images.Media.DATA} like ? "
        val selectionArgs = arrayOf("%$directoryPath%")
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val cursor = query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
        // Add this before the cursor:
        Log.d("DirectoryPath", directoryPath)

        cursor?.use { c ->
            val dataIndex = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (dataIndex >= 0) {
                var count = 0
                while (c.moveToNext() && count < imageCount) {
                    val imagePath = c.getString(dataIndex)
                    images.add(imagePath)
                    count++
                }
            }
        }
        // After the cursor, check if any images were found:
        Log.d("ImageCount", images.size.toString())
        return images
    }

    //python calling functions
    @SuppressLint("SetTextI18n")
    private fun Image_Processing() {
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        //starts up python interpreter
        val py = Python.getInstance()
        //gets the python file
        val pyobj = py.getModule("Comparison")
        val pyobj1 = py.getModule("bullet_coords")
        val pyobj2 = py.getModule("Grouping")
        val pyobj3 = py.getModule("Point_Calcs")
        val pyobj4 = py.getModule("Actual_points")
        //sets variables  for image grabbing function
        val directoryPath = "/storage/emulated/0/Android/media/com.example.mainpage_rangebuddy/Main Page-Range Buddy/"
        val output_image = "/storage/emulated/0/Android/media/com.example.mainpage_rangebuddy/Main Page-Range Buddy/Bullet_Holes.jpg"

        // Check if the directory exists
        val directory = File(directoryPath)
        if (directory.exists()) {
            // Directory exists, log a message
            Log.d("DirectoryCheck", "Directory exists at path: $directoryPath")
        } else {
            // Directory does not exist, log an error message
            Log.e("DirectoryCheck", "Directory does not exist at path: $directoryPath")
        }

        // sets the amount of images its grabbing
        val imageCount = 2
        val images = contentResolver.imagegrabber(directoryPath, imageCount)
        //calls the python function

        Toast.makeText(this, "comparison is beginning to run", Toast.LENGTH_SHORT).show()

        val bullet_image = pyobj.callAttr("comparison", images[0], images[1], output_image) // image[0], image[1], output_image
        //Log.d("Python function comparison is being ran", bullet_image.toString())
        Toast.makeText(this, "comparison is finished running & bullet_coords is beginning to run", Toast.LENGTH_LONG).show()

        val bullets_XnY = pyobj1.callAttr("bullet_coords", bullet_image)
        Log.d("Python function bullet_coords is being ran", bullets_XnY.toString())
        Toast.makeText(this, "bullet_coords is finished running", Toast.LENGTH_LONG).show()

        val point_calc = pyobj3.callAttr("Point_Calcs", bullets_XnY, bullet_image) //data to send to back end
        Log.d("Python function Point_Calcs is being ran", point_calc.toString())
        Toast.makeText(this, "Point_Calcs is finished running", Toast.LENGTH_LONG).show()
        val pointcalcTextView = findViewById<TextView>(R.id.Number_of_points)
        pointcalcTextView.text = "Point Calculation: $point_calc"

        val actual_points = pyobj3.callAttr("Actual_points", bullets_XnY, bullet_image)
        Log.d("Python function Actual_points is being ran", actual_points.toString())
        val bullet_group = pyobj2.callAttr("Grouping", actual_points) // data to send to back end
        val bullet_groupValue = bullet_group.toDouble()
        Log.d("Python function Grouping is being ran", bullet_group.toString())
        Toast.makeText(this, "Grouping is finished running", Toast.LENGTH_LONG).show()
        val formatcals = String.format("Bullet Group: %.3f in", bullet_groupValue)
        val bulletgroupTextView = findViewById<TextView>(R.id.Group_Size)
        bulletgroupTextView.text = formatcals




    }

}