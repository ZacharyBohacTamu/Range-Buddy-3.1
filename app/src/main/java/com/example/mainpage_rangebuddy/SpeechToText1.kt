package com.example.mainpage_rangebuddy

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.speech.v1.RecognitionAudio
import com.google.cloud.speech.v1.RecognitionConfig
import com.google.cloud.speech.v1.RecognizeRequest
import com.google.cloud.speech.v1.SpeechClient
import com.google.cloud.speech.v1.SpeechSettings
import com.google.protobuf.ByteString
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.Arrays
import java.util.Locale

class SpeechToText1 : AppCompatActivity() {
    //private var startButton: Button? = null
    private lateinit var startButton: Button
    private val resultTextView: TextView? = null
    private var progressBar: ProgressBar? = null
    private var permissionToRecordAccepted = false
    private val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var recordingThread: Thread? = null
    private lateinit var byteArray: ByteArray
    private var mVoiceRecorder: VoiceRecorder? = null
    var handler = Handler()
    //private var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaPlayer: MediaPlayer


    private val mVoiceCallback: VoiceRecorder.Callback = object : VoiceRecorder.Callback() {
        override fun onVoiceStart() {}
        override fun onVoice(data: ByteArray, size: Int) {
            byteArray = appendByteArrays(byteArray, data)
        }

        override fun onVoiceEnd() {
            runOnUiThread { progressBar!!.visibility = View.VISIBLE }
            Log.e("kya", "" + byteArray)
            transcribeRecording(byteArray)
        }
    }
    private var speechClient: SpeechClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_page)
        startButton = findViewById(R.id.start_button)

        //resultTextView = findViewById(R.id.result_text_view);
        progressBar = findViewById(R.id.progress_bar)
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        startButton.setOnClickListener(View.OnClickListener {
            if (permissionToRecordAccepted) {
                if (startButton.getText().toString() == "Start") {
                    startButton.setText("Stop")
                    startVoiceRecorder()
                } else {
                    stopVoiceRecorder()
                }
            }
        })
        initializeSpeechClient()
    }

    private fun initializeSpeechClient() {
        /*try {
            val credentials =
                GoogleCredentials.fromStream(resources.openRawResource(R.raw.credentials))
            val credentialsProvider = FixedCredentialsProvider.create(credentials)
            speechClient = SpeechClient.create(
                SpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build()
            )
        } catch (e: IOException) {
            Log.e("kya", "InitException" + e.message)
        }*/
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
            progressBar!!.visibility = View.GONE
            resultTextView!!.text = transcript
            clearByteArray(byteArray)
            startButton!!.text = "Start"
            stopVoiceRecorder()

            // Check if the recognized text contains your specific keyword(s) or phrase(s)
            if (transcript.lowercase(Locale.getDefault()).contains("start application")) {
                // Perform actions associated with the keyword detection
                performKeywordAction()
            }
        }
    }

    private fun performKeywordAction() {
        Toast.makeText(this, "Keyword detected!", Toast.LENGTH_SHORT).show()
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

    private fun clearByteArray(array: ByteArray) {
        // Set each element to zero
        Arrays.fill(array, 0.toByte())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
        if (!permissionToRecordAccepted) {
            startButton!!.isEnabled = false
        }
    }

    private fun playSound() {
        mediaPlayer = MediaPlayer.create(this, R.raw.transcribe_voice)
        mediaPlayer.setOnCompletionListener(OnCompletionListener { mediaPlayer.release() })
        mediaPlayer.start()
    }

    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }
}