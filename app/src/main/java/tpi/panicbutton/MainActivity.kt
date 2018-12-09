package tpi.panicbutton

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val manager = supportFragmentManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var lat: Double = 0.0
    private var long: Double = 0.0

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
    @SuppressLint("MissingPermission")
    public fun getLocation() {

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location!!.latitude
                    long = location!!.longitude
                }
            }
    }

    public fun getLat(): Double {
        return lat
    }

    public fun getLong(): Double {
        return long
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createFragment(R.id.navigation_panic)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        getLocation()
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
}
