package com.smartangle.controllersystemapp.common

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.smartangle.controllersystemapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val TAG = "MapActivity"

    private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private val LOCATION_PERMISSION_REQUEST_CODE = 1234
    private val DEFAULT_ZOOM = 15f

    //vars
    private var mLocationPermissionsGranted = false
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null

    var latititude : Double = 0.0
    var longtute : Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val mapFragment = supportFragmentManager
//                .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)

        getLocationPermission()



    }

    //use it when custom pin icon is vector image
  /*   fun getMarkerBitmapFromView(@DrawableRes resId: Int): Bitmap? {
        val customMarkerView: View = (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).
        inflate(R.layout.view_custom_marker, null)
        val markerImageView: ImageView = customMarkerView.findViewById(R.id.customPin) as ImageView
        markerImageView.setImageResource(resId)
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight())
        customMarkerView.buildDrawingCache()
        val returnedBitmap: Bitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        //val drawable: Drawable = customMarkerView?.background
        customMarkerView.background?.draw(canvas)
        customMarkerView.draw(canvas)
        return returnedBitmap
    }*/


//87:A9:08:78:46:C5:D7:4B:53:34:AF:9E:F5:D3:59:A1:EC:4B:C1:B1

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(31.19028343045079, 29.91991197690368)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        //Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show()
       // Toast.makeText(this, getString(R.string.drag_marker), Toast.LENGTH_SHORT).show()
        Log.d(TAG, "onMapReady: map is ready")
        mMap = googleMap
        if (mLocationPermissionsGranted) {
            getDeviceLocation()

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            mMap.setMyLocationEnabled(true)
            mMap.getUiSettings().setMyLocationButtonEnabled(true)
            init()

            mMap.setOnCameraMoveListener {

                // mMap.clear() //to remove the others pin
                val midLatLng: LatLng = mMap.cameraPosition.target
                Log.d("midLatLng" , "$midLatLng")

                latititude = midLatLng.latitude
                longtute = midLatLng.longitude
                val sydney = LatLng(latititude, longtute)
                if (marker==null)
                    mMap.addMarker(MarkerOptions().position(sydney)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_icon))
                            //.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_icon_gps)))
                            .draggable(true))
                else
                    marker?.position = sydney
            }
            /*   mMap.setOnCameraIdleListener {

                   //get latlng at the center by calling

                   //mCurrLocationMarker.remove()
                  // mMap.clear() //to remove the others pin
                   val midLatLng: LatLng = mMap.cameraPosition.target
                   Log.d("midLatLng" , "$midLatLng")

                   latititude = midLatLng.latitude
                   longtute = midLatLng.longitude
                   val sydney = LatLng(latititude, longtute)
                   if (marker==null)
                   mMap.addMarker(MarkerOptions().position(sydney)
                           .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_icon))
                           //.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_icon_gps)))
                           .draggable(true))
                   else
                       marker?.position = sydney

                   //   moveCamera(midLatLng, DEFAULT_ZOOM)
               }*/

            mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener{
                override fun onMarkerDragEnd(marker: Marker?) {
//                    Log.d("dragg", "onMarkerDragEnd")
//                    val mLocation = mMap.myLocation
//                    val newLocation: LatLng? = marker?.position
//                    newLocation?.latitude?.let { mLocation.latitude = it }
//                    newLocation?.longitude?.let { mLocation.longitude = it }
//                    Log.d("lat", "${newLocation?.latitude}")
//                    Log.d("long", "${newLocation?.longitude}")
//
//                    moveCamera(LatLng(mLocation.latitude, mLocation.longitude),
//                            DEFAULT_ZOOM)

                    val latLng = marker!!.position
                    val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
                    try {
                        val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)[0]
                        Log.d("lat", "${address?.latitude}")
                        Log.d("long", "${address?.longitude}")

                        moveCamera(LatLng(address.latitude, address.longitude),
                                DEFAULT_ZOOM)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }

                override fun onMarkerDragStart(marker: Marker?) {
                    Log.d("dragg", "onMarkerDragStart")

                 //   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mLocation.getLatitude(), mLocation.getLongitude()), 15.0f))

                }

                override fun onMarkerDrag(p0: Marker?) {
                    Log.d("dragg", "onMarkerDrag")

                }

            })
//            mMap.setOnMapClickListener {
//                point ->
//                Toast.makeText(applicationContext, point.toString(), Toast.LENGTH_SHORT).show()
//                Log.d(TAG, "testlatlong ${point.latitude} ${point.longitude}")
//
//                val returnIntent = Intent()
//                returnIntent.putExtra("lattt", point.latitude)
//                returnIntent.putExtra("longh", point.longitude)
//                setResult(RESULT_OK, returnIntent)
//                finish()
//            }
//

        }
    }

    private fun init() {


        doneMap?.setOnClickListener {

                val returnIntent = Intent()
                returnIntent.putExtra("lattt", latititude)
                returnIntent.putExtra("longh", longtute)
            val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
                val address :Address? = geocoder.getFromLocation(latititude, longtute, 1)[0]
                returnIntent.putExtra("address",address?.getAddressLine(0)?:"")
                setResult(RESULT_OK, returnIntent)
                finish()

        }
//        Log.d(TAG, "init: initializing")
//        input_search?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
//            override fun onEditorAction(textView: TextView?, actionId: Int, keyEvent: KeyEvent): Boolean {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
//                        keyEvent.getAction() === KeyEvent.ACTION_DOWN || keyEvent.getAction() === KeyEvent.KEYCODE_ENTER) {
//
//                    //execute our method for searching
//                    geoLocate()
//                }
//                return false
//            }
//        })
    }

    private fun geoLocate() {
        Log.d(TAG, "geoLocate: geolocating")
        val searchString: String = input_search?.text.toString()
        val geocoder = Geocoder(this@MapsActivity)
        var list: List<Address> = ArrayList()
        try {
            list = geocoder.getFromLocationName(searchString, 1)
        } catch (e: IOException) {
            Log.e(TAG, "geoLocate: IOException: " + e.message)
        }
        if (list.size > 0) {
            val address: Address = list[0]
            val sydney = LatLng(address.latitude , address.longitude)
            mMap.addMarker(MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_icon)).title("Marker in Sydney").draggable(true))
            moveCamera(LatLng(address.latitude, address.longitude),
                    DEFAULT_ZOOM)
            Log.d(TAG, "geoLocate: found a location: " + address.toString())
            Log.d(TAG, "geoLocate: found a location:latttttLong ${address.latitude} ${address.longitude}")

            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    var marker : Marker?=null
    private fun getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location")
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        try {
            if (mLocationPermissionsGranted) {
                val location = mFusedLocationProviderClient?.getLastLocation()
                location?.addOnCompleteListener(object : OnCompleteListener<Location?> {
                    override fun onComplete(@NonNull task: Task<Location?>) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!")
                            if (task.result !=null) {
                                val currentLocation: Location = task.result as Location
                                val sydney = LatLng(currentLocation.latitude, currentLocation.longitude)
                                marker  =  mMap.addMarker(MarkerOptions().position(sydney)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_icon))
                                        //.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_icon_gps)))
                                        .draggable(true))
                                moveCamera(LatLng(currentLocation.latitude, currentLocation.longitude),
                                        DEFAULT_ZOOM)
                            }
                        } else {
                            Log.d(TAG, "onComplete: current location is null")
                            Toast.makeText(this@MapsActivity, "unable to get current location", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.message)
        }
    }

    private fun moveCamera(latLng: LatLng, zoom: Float) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude)
        latititude = latLng.latitude
        longtute = latLng.longitude

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    private fun initMap() {
        Log.d(TAG, "initMap: initializing map")

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions")
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                        FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.applicationContext,
                            COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true
                initMap()
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE)
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.d(TAG, "onRequestPermissionsResult: called.")
        mLocationPermissionsGranted = false

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.size > 0) {
                    var i = 0
                    while (i < grantResults.size) {
                        if (grantResults[i] !== PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false
                            Log.d(TAG, "onRequestPermissionsResult: permission failed")
                            return
                        }
                        i++
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted")
                    mLocationPermissionsGranted = true
                    //initialize our map
                    initMap()
                }
            }
        }
    }
}
