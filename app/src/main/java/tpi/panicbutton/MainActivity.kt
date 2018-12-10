package tpi.panicbutton

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.provider.Telephony
import android.support.v4.app.ActivityCompat
import android.support.v4.content.LocalBroadcastManager
import android.telephony.SmsManager
import android.widget.Toast
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.models.Tweet


class MainActivity : AppCompatActivity() {

    val manager = supportFragmentManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationMessage: String

    private lateinit var numbersToSMS: ArrayList<PhoneNumber>

    // broadcast
//    val broadCastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(contxt: Context?, intent: Intent?) {
//            Log.e("intent", "got here")
//            Log.e("intent", intent?.action)
//            when (intent?.action) {
//
//                Telephony.Sms.Intents.SMS_RECEIVED_ACTION -> sendSosAlert()
//            }
//        }
//    }

    private lateinit var smsBroadcastReceiver: SmsBroadcastReceiver


    fun setLocationMessage(message: String) {
        locationMessage = message
        Log.e("locationLoading", "location loaded correctly")
    }

    fun sendSosAlert() {
//        Log.e("broadcast receiver", "sms received")
//        Toast.makeText(this, "SOS enviado", Toast.LENGTH_SHORT).show()
        sendSmsToAllContacts(locationMessage)
        postTweet(locationMessage)


    }

    fun sendSmsToAllContacts(message: String) {
        numbersToSMS.forEach { phone ->
            Log.e("Message to", phone.number)
            sendSMS(phone.number, message)
        }
    }

    fun sendSMS(number: String, message: String) {
        SmsManager.getDefault().sendTextMessage(number, null, message, null, null)
//        Toast.makeText(context!!, "SMS sent.", Toast.LENGTH_SHORT).show()
    }

    fun postTweet(message: String) {
        val twitterApiClient = TwitterCore.getInstance().apiClient
        val statusesService = twitterApiClient.statusesService
        val call = statusesService.update(message, null, null, null, null, null, null, null , null)
        call.enqueue(object : Callback<Tweet>() {
            override fun success(result: Result<Tweet>) {
                Toast.makeText(applicationContext, "Exitosa publicacion a Twitter", Toast.LENGTH_SHORT).show()
                Log.e("post", "Correct")
            }

            override fun failure(exception: TwitterException) {
                Toast.makeText(applicationContext, "Fallada publicacion a Twitter", Toast.LENGTH_SHORT).show()
                Log.e("post", "Incorrect")
            }
        })
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_panic -> {

                createFragment(R.id.navigation_panic)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_information -> {

                createFragment(R.id.navigation_information)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
//                message.setText(R.string.title_settings)
                createFragment(R.id.navigation_settings)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    public fun getSmsNumbers(): ArrayList<PhoneNumber> {
        return numbersToSMS
    }

    public fun validatePhoneNumber(number: String): Boolean {
        if (number.length != 10) return false
        return number.matches("\\d+".toRegex())
    }

    public fun addSmsNumber(number: String) {
        if (!validatePhoneNumber(number)) {
            Toast.makeText(this, "Error en el numero", Toast.LENGTH_SHORT).show()
            return
        }
        numbersToSMS.add(PhoneNumber(number))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        numbersToSMS = ArrayList<PhoneNumber>()
        numbersToSMS.add(PhoneNumber("3166172464")) // "3012189158"

        Twitter.initialize(this)
        setContentView(R.layout.activity_main)

        createFragment(R.id.navigation_panic)
//        createFragment(R.id.navigation_information)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        getLocation()

        val config = TwitterConfig.Builder(this)
            .logger(DefaultLogger(Log.DEBUG))
            .twitterAuthConfig(
                TwitterAuthConfig(
                    getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
                    getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)
                )
            )
            .debug(true)
            .build()
        Twitter.initialize(config)


        if (ActivityCompat.checkSelfPermission(this,       Manifest.permission.RECEIVE_SMS  ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,       Manifest.permission.READ_SMS  ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS), 0)

        }

//        LocalBroadcastManager.getInstance(this)
//            .registerReceiver(broadCastReceiver, IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))


        smsBroadcastReceiver = SmsBroadcastReceiver()
        registerReceiver(smsBroadcastReceiver, IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))


        val listener = smsBroadcastReceiver.Listener(this)

        smsBroadcastReceiver.setListener(listener);
    }

    fun createFragment(type: Int) {
        val transaction = manager.beginTransaction()
        var fragment : Fragment
        if (type == R.id.navigation_panic) {
            fragment = Panic()
        } else if (type == R.id.navigation_information) {
            fragment = Information()
        } else {
            fragment = SettingsFragment()
        }
        transaction.replace(R.id.fragment_holder, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result to the fragment, which will then pass the result to the login
        // button.
        Log.e("worked", "sending to fragment")
        val fragment = manager.findFragmentById(R.id.fragment_holder)
        Log.e("worked", fragment.toString())
        fragment?.onActivityResult(requestCode, resultCode, data)
        Log.e("worked", "sent to fragment")
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(smsBroadcastReceiver)
    }

//    fun getData(): String {
//        return "YES"
//    }

}
