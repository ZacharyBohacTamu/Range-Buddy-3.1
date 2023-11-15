package com.example.mainpage_rangebuddy


//import android.content.Context

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.OnImageSavedCallback
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mainpage_rangebuddy.Constants.TAG
import com.example.mainpage_rangebuddy.databinding.ActivityPhotoPageBinding
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.speech.v1.RecognitionAudio
import com.google.cloud.speech.v1.RecognitionConfig
import com.google.cloud.speech.v1.RecognizeRequest
import com.google.cloud.speech.v1.SpeechClient
import com.google.cloud.speech.v1.SpeechSettings
import com.google.protobuf.ByteString
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class PhotoPage : AppCompatActivity() {
    //UI Functions Variables
    private lateinit var ToSessionData: Button
    //Picture Function Variables
    lateinit var binding: ActivityPhotoPageBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private val resultTextView: TextView? = null
    // Audio Recording Variables
    private var permissionToRecordAccepted = false
    private val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var recordingThread: Thread? = null
    private var byteArray: ByteArray = ByteArray(0)
    private var mVoiceRecorder: VoiceRecorder? = null

    //private var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaPlayer: MediaPlayer
    private var speechClient: SpeechClient? = null
    companion object {
        const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }


    private val mVoiceCallback: VoiceRecorder.Callback = object : VoiceRecorder.Callback() {
        override fun onVoiceStart() {}
        override fun onVoice(data: ByteArray, size: Int) {
            byteArray = appendByteArrays(byteArray, data)
        }

        override fun onVoiceEnd() {
            runOnUiThread { binding.progressBar!!.visibility = View.VISIBLE }
            Log.e("kya", "" + byteArray)
            transcribeRecording(byteArray)
        }
    }



    //runs most functions, button mapping, etc
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_photo_page)
        ActivityCompat.requestPermissions(this, permissions,
            REQUEST_RECORD_AUDIO_PERMISSION
        )
        // initialize the speech Client
        initializeSpeechClient()

        //hiding the title bar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        //setting variables to use in the xml
        binding = ActivityPhotoPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // permissions to record audio
        binding.startButton.setOnClickListener(View.OnClickListener {
            if (permissionToRecordAccepted) {
                if (binding.startButton.getText().toString() == "Start Recording") {
                    binding.startButton.setText("Stop")
                    startVoiceRecorder()
                } else {
                    stopVoiceRecorder()
                }
            }
        })
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

        //checks if permissions for camera are granted
        if (allPermissionGranted()) {
            startCamera()
        } else {
            //requires perms to be needed
            ActivityCompat.requestPermissions(
                this, Constants.REQUIRED_PERMISSIONS,
                Constants.REQUEST_CODE_PERMISSIONS
            )
        }
        binding.btnTakePicture.setOnClickListener {
            takePhoto()
        }

        //button mapping to other pages
        ToSessionData = findViewById(R.id.ToSessionsPage)
        ToSessionData.setOnClickListener {
            val session = Intent(this, SessionsData::class.java)
            startActivity(session)
        }
    }

    //sends picture to directory will be later changed to server in 404
    private fun getOutputDirectory(): File {
        //gets the directory of the app
        val mediaDir = externalMediaDirs.firstOrNull()?.let { mFile ->
            File(mFile, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }

        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    //takes the photo and saves to file
    private fun takePhoto() {
        //gets the image capture
        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                Constants.FILE_NAME_FORMAT,
                Locale.getDefault()
            )   //creates a file name in terms of time.jpg
                .format(
                    System
                        .currentTimeMillis()
                ) + ".jpg"
        )
        //sets up the output file
        val outputOption = ImageCapture
            .OutputFileOptions
            .Builder(photoFile)
            .build()
        //takes the picture
        imageCapture.takePicture(
            outputOption, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                    val savedUri = Uri.fromFile(photoFile) //maybe other URI
                    val msg = "Photo Saved"

                    Toast.makeText(this@PhotoPage, "$msg $savedUri ", Toast.LENGTH_LONG).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "onError: ${exception.message}", exception)
                }


            }
        )


    }
    private fun startVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder!!.stop()
        }
        mVoiceRecorder = VoiceRecorder(mVoiceCallback)
        mVoiceRecorder!!.start()
    }
    private fun stopVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder!!.stop()
            mVoiceRecorder = null
        }
        if (recordingThread != null) {
            try {
                recordingThread!!.join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            recordingThread = null
        }
    }
    private fun appendByteArrays(array1: ByteArray, array2: ByteArray): ByteArray {
        val outputStream = ByteArrayOutputStream()
        try {
            outputStream.write(array1)
            outputStream.write(array2)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return outputStream.toByteArray()
    }
    private fun initializeSpeechClient() {
        try {
            val credentials =
                GoogleCredentials.fromStream(resources.openRawResource(R.raw.credentials))
            val credentialsProvider = FixedCredentialsProvider.create(credentials)
            speechClient = SpeechClient.create(
                SpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build()
            )
        } catch (e: IOException) {
            Log.e("kya", "InitException" + e.message)
        }
    }
    private fun transcribeRecording(data: ByteArray) {
        try {
            Log.e("API_CALL", "API CALL STARTED...")
            recordingThread = Thread {
                try {
                    val response = speechClient!!.recognize(createRecognizeRequestFromVoice(data))
                    for (result in response.resultsList) {
                        val transcript = result.alternativesList[0].transcript
                        updateResult(transcript)
                    }
                } catch (e: Exception) {
                    Log.e("SEECOLE", "" + e.message)
                }
            }
            recordingThread!!.start()
        } catch (e: Exception) {
            Log.e("SEECOLE", "" + e.message)
        }
    }
    private fun createRecognizeRequestFromVoice(audioData: ByteArray): RecognizeRequest {
        val audioBytes =
            RecognitionAudio.newBuilder().setContent(ByteString.copyFrom(audioData)).build()
        val config = RecognitionConfig.newBuilder()
            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
            .setSampleRateHertz(16000)
            .setLanguageCode("en-US")
            .build()
        return RecognizeRequest.newBuilder()
            .setConfig(config)
            .setAudio(audioBytes)
            .build()
    }
    private fun updateResult(transcript: String) {
        runOnUiThread {
            binding.progressBar!!.visibility = View.GONE
            //binding.resultTextView!!.text = transcript
            clearByteArray(byteArray)
            binding.startButton!!.text = "Start Recording"


            // Check if the recognized text contains your specific keyword(s) or phrase(s)
            if (transcript.lowercase(Locale.getDefault()).contains("start application")) {
                // Perform actions associated with the keyword detection
                stopVoiceRecorder()
                performKeywordAction()
                GlobalScope.launch {
                    delay(timeMillis = 5000) // change this to give more time for demo
                    takePhoto()
                }
            }
        }
    }


    //starts the camera up in app
    private fun startCamera() {

        //starts up camera
        val cameraProviderFuture = ProcessCameraProvider
            .getInstance(this)
        //sets up camera
        cameraProviderFuture.addListener({
            //used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also { mPreview ->

                    mPreview.setSurfaceProvider(
                        binding.cameraView.surfaceProvider // why?
                    )
                }
            imageCapture = ImageCapture.Builder()
                .build()
            //selects back camera as default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            //unbinds all cameras
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector,
                    preview, imageCapture
                )
            } catch (e: Exception) {
                Log.d(TAG, "startCamera Fail:", e)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    //asks for permissions
    private fun allPermissionGranted() =
        Constants.REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
        }
    //checks if permissions are granted and outputs a toast
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE_PERMISSIONS) {
            if (allPermissionGranted()) {
                Toast.makeText(this, "We Have Permission", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
        else {
            Toast.makeText(
                this,
                "Permissions not granted", Toast.LENGTH_LONG
            ).show()
            finish()
        }

        /*if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
        }*/
        if (!permissionToRecordAccepted) {
            binding.startButton!!.isEnabled = false
        }
    }
    private fun clearByteArray(array: ByteArray) {
        // Set each element to zero
        Arrays.fill(array, 0.toByte())
    }

    //stops the camera when app is closed
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun performKeywordAction() {
        Toast.makeText(this, "Keyword detected!", Toast.LENGTH_SHORT).show()
        takePhoto()

        /*val file = File(filesDir.path +"/pictures/image.png")
        file.mkdirs()
        file.createNewFile()
        val outputFile = ImageCapture.OutputFileOptions.Builder(file).build()
        imageCapture?.takePicture(outputFile, cameraExecutor, object : OnImageSavedCallback{
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                Log.d("Saved", outputFileResults.toString())
            }

            override fun onError(exception: ImageCaptureException) {
                Log.d("Saved", exception.toString())
            }

        })*/
    }

    //python calling functions
    /*fun comparisonCall() {
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        val py = Python.getInstance()
        val pyobj = py.getModule("Comparison")
        val obj = pyobj.callAttr("comparison", Recent_image_1, Recent_image_2)
        Log.d("Python function comparison is being ran", obj.toString())
        Toast.makeText(this, "Python script is running", Toast.LENGTH_SHORT).show()
    }*/
}

