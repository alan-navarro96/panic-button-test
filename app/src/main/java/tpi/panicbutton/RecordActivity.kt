package tpi.panicbutton

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.record_activity.*
import java.io.IOException
import java.util.*

class RecordActivity : Fragment() {
    private var play: Button? = null
    private var stop: Button? = null
    private var record: Button? = null
    private var myAudioRecorder: MediaRecorder? = null
    private var outputFile: String? = null
    private lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.record_activity, container, false)

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        play = play_button as Button
        stop = stop_button as Button
        record = record_button as Button
        stop!!.isEnabled = false
        play!!.isEnabled = false
        recordInit()

        record!!.setOnClickListener {
            try {

                if (myAudioRecorder == null) {
                    recordInit()
                }

                myAudioRecorder!!.prepare()
                myAudioRecorder!!.start()

            } catch (ise: IllegalStateException) {
                ise.printStackTrace()
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }

            record!!.isEnabled = false
            stop!!.isEnabled = true
        }

        stop!!.setOnClickListener {
            try {
                myAudioRecorder!!.stop()
                myAudioRecorder!!.release()
                myAudioRecorder = null
                record!!.isEnabled = true
                stop!!.isEnabled = false
                play!!.isEnabled = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        play!!.setOnClickListener {
            val mediaPlayer = MediaPlayer()
            try {
                mediaPlayer.setDataSource(outputFile)
                mediaPlayer.prepare()
                mediaPlayer.start()

            } catch (e: Exception) {
                // make something
            }
        }
    }

    @SuppressLint("NewApi")
    private fun recordInit() {
        outputFile = Environment.getExternalStorageDirectory().absolutePath + "/recording " +
                Random() + " .3gp"

        myAudioRecorder = MediaRecorder()

        // Here, thisActivity is the current activity
        val MY_PERMISSIONS_REQUEST = 500
        if (ContextCompat.checkSelfPermission(context!!,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                MY_PERMISSIONS_REQUEST)

            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST)


        } else {

            myAudioRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            myAudioRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            myAudioRecorder!!.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
            myAudioRecorder!!.setOutputFile(outputFile)
        }
    }
}