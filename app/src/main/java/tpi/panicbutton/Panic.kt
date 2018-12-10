package tpi.panicbutton


import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.Manifest.permission
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.location.Criteria
import android.location.LocationManager
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import kotlinx.android.synthetic.main.fragment_panic.*
import java.util.jar.Manifest


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Panic : Fragment(), OnMapReadyCallback {

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mMapView: MapView
    private lateinit var mView: View

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val SEND_SMS_PERMISSION_REQUEST_CODE = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_panic, container, false)
//        System.err.println(activity!!)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMapView = mView.findViewById(R.id.map)
        if (mMapView != null) {
            mMapView.onCreate(null)
            mMapView.onResume()
            mMapView.getMapAsync(this)
        }

        button_alert.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(context!!,
                    android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity!!,
                    arrayOf(android.Manifest.permission.SEND_SMS), SEND_SMS_PERMISSION_REQUEST_CODE)
            } else {
//                sendPanicAlert()
                var act = (activity as MainActivity)
                act.sendSosAlert()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        MapsInitializer.initialize(context)
        mGoogleMap = googleMap
        mGoogleMap.uiSettings.isZoomControlsEnabled = true
        setUpMap()

    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(context!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        mGoogleMap.isMyLocationEnabled = true

//        var act = (activity as MainActivity)
//        act.getLocation()
//        var lat = act.getLat()
//        var long = act.getLong()
//
//        Toast()
//
//        val currentLatLng = LatLng(lat, long)
//        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            // Got last known location. In some rare situations this can be null.
            // 3
            if (location != null) {
//                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)

                val positionURL = "https://www.google.com/maps/search/?api=1&query=" + location.latitude + "," + location.longitude
                val message = "[test] SOS de panico: " + positionURL
                var act = (activity as MainActivity)
                act.setLocationMessage(message)

                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f))
            }
        }
    }

    fun sendSmsToAllContacts(message: String) {
        var act = (activity as MainActivity)
        var numbers = act.getSmsNumbers()
        numbers.forEach { phone ->
            Log.e("Message to", phone.number)
//            sendSMS(phone.number, message)
        }
    }

    fun sendSMS(number: String, message: String) {
        SmsManager.getDefault().sendTextMessage(number, null, message, null, null)
//        Toast.makeText(context!!, "SMS sent.", Toast.LENGTH_SHORT).show()
    }

}
